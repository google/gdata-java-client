/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.util;



import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.parsing.SecureGenericXMLFactory;
import com.google.gdata.client.CoreErrorDomain;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * XML parser.
 * <p>
 * This is a thin layer on top of a SAX parser. The key concept
 * necessary to understand this parser is <i>Element Handler</i>.
 * Element handlers are type-specific parsers. Each handler instance
 * contains an instance of the Java type corresponding to the XML
 * type it parses. At any given time, one handler is active, and zero
 * or more handlers are kept on the stack. This corresponds directly
 * to the set of currently opened XML tags.
 * <p>
 * To use this parser, one must define an {@link
 * XmlParser.ElementHandler} type (usually one per XML schema type),
 * specify the root element handler, and pass a reader to the
 * {@link #parse(Reader, com.google.gdata.util.XmlParser.ElementHandler, String,
 *               String)} method.
 * <p>
 *
 * 
 * 
 * @see     XmlParser.ElementHandler
 */
public class XmlParser extends DefaultHandler {


  private static final Logger logger =
    Logger.getLogger(XmlParser.class.getName());


  // The SAXParserFactory used to create underlying SAXParser instances.
  private static SAXParserFactory parserFactory;

  // Always return secure SAX parser, which is secured against XXE attacks
  private static SAXParserFactory getSAXParserFactory()
      throws ParserConfigurationException, SAXException {
    SAXParserFactory factory;
    try {
      factory = SecureGenericXMLFactory.getSAXParserFactory(
          SAXParserFactory.newInstance());
      // "http://xml.org/sax/features/external-parameter-entities" is a feature
      // that is set by the SecureGenericXMLFactory and not supported on some
      // platform such as Android.
      // Unfortunately, the Android implementation doesn't throw an exception
      // when setting the feature. The exception is thrown when a new SAXParser
      // is instantiated in the newSAXParser method.
      // The following line check for such behavior.
      factory.newSAXParser();
    } catch (ParserConfigurationException e) {
      // OK. Cannot create secure xml parser. Use insecure one.
      // The factory instantiated in the try block can't be reused due to
      // side-effect in SecureGenericXMLFactory.getSAXParserFactory.
      factory = SAXParserFactory.newInstance();
    }
    factory.setNamespaceAware(true);
    return factory;
  }


  /**
   * Base class for custom element handlers.
   * <p>
   * To implement a new element handler, one must create a new class
   * extending this class, override {@link #getChildHandler} if nested
   * elements need to be parsed, override {@link #processAttribute} if
   * attributes need to be parsed, and override {@link
   * #processEndElement()} to receive the text() value and post-process
   * the element.
   * <p>
   * If the handler wishes to store unrecognized XML contents in an {@link
   * XmlBlob} value, it must call {@link #initializeXmlBlob} either in the
   * constructor, in parent's {@link #getChildHandler}, or in {@link
   * #processAttribute}. The resulting {@link XmlBlob} value is available
   * following the invocation of {@link #processEndElement()}
   * through the object passed to {@link #initializeXmlBlob}.
   * <p>
   *
   * This class implements overridable methods to support unrecognized XML
   * parsing if desired.
   *
   * 
   */
  public static class ElementHandler {


    /** This element's QName. Used for error reporting. */
    public String qName;


    /** This element's text() value. */
    public String value;

    /** Temporary buffer for building up the text() value. */
    private StringBuilder buffer;

    /**
     * The current state of {@code xml:lang}.
     * See http://www.w3.org/TR/REC-xml/#sec-lang-tag for more information.
     */
    public String xmlLang;


    /**
     * The current state of {@code xml:base}.
     * See http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html for
     * more information.
     */
    public String xmlBase;


    /** Keeps track of the element stack. */
    ElementHandler parent;


    /**
     * If the handler is parsing unrecognized XML, this object stores the
     * output.
     */
    XmlBlob xmlBlob = null;


    /**
     * Flag indicating whether it's still OK to call {@link #initializeXmlBlob}.
     */
    boolean okToInitializeXmlBlob = true;


    /** Flag indicating whether mixed content unrecognized XML is allowed. */
    boolean mixedContent = false;


    /**
     * Flag indicating whether unrecognized XML should be processed for
     * full-text indexing. If set, the resulting string ready for indexing is
     * stored in {@link XmlBlob#fullText}. Non-contiguous strings within this
     * index are separated by '\n'.
     */
    boolean fullTextIndex = false;


    /** This element's inner XML writer. Used internally by XmlParser. */
    XmlWriter innerXml;


    /** Namespaces used by this blob. */
    Set<String> blobNamespaces = new HashSet<String>();


    /** String writer underlying {@link #innerXml}. */
    StringWriter innerXmlStringWriter;


    /** String writer underlying the full-text index string. */
    StringWriter fullTextIndexWriter;


    /**
     * Determines a handler for a child element.
     * <p>
     *
     * The default implementation doesn't recognize anything. The result is a
     * schema error <i>unless</i> the parent handler accepts unrecognized XML.
     *
     * {@link com.google.gdata.wireformats.XmlParser}.
     * localname/namespace.
     *
     * @param   namespace
     *            Child element's namespace URI.
     *
     * @param   qualifiedName
     *            Child element's qualified name.
     *
     * @param   localName
     *            Child element's local name.
     *
     * @param   attrs
     *            Child element's attributes. These attributes will be
     *            communicated to the child element handler through its
     *            {@link #processAttribute} method. They are passed here because
     *            sometimes the value of some attribute determines the element's
     *            content type, so different element handlers may be needed.
     *
     * @return  Child element's handler, or {@code null} if the child is
     *          unrecognized.
     *
     * @throws  ParseException
     *            Invalid child element.
     *
     * @throws  IOException
     *            Internal I/O exception (e.g., thrown by XML blob writer).
     */
    public ElementHandler getChildHandler(String namespace,
                                          String qualifiedName,
                                          String localName,
                                          Attributes attrs,
                                          List<XmlNamespace> namespaces)
        throws ParseException, IOException {
      return getChildHandler(namespace, localName, attrs);
    }


    /**
     * Determines a handler for a child element.
     * <p>
     *
     * The default implementation doesn't recognize anything. The result is a
     * schema error <i>unless</i> the parent handler accepts unrecognized XML.
     *
     * {@link com.google.gdata.wireformats.XmlParser}.
     * localname/namespace.
     *
     * @param   namespace
     *            Child element namespace URI.
     *
     * @param   localName
     *            Child element name.
     *
     * @param   attrs
     *            Child element attributes. These attributes will be
     *            communicated to the child element handler through its
     *            {@link #processAttribute} method. They are passed here because
     *            sometimes the value of some attribute determines the element's
     *            content type, so different element handlers may be needed.
     *
     * @return  Child element handler, or {@code null} if the child is
     *          unrecognized.
     *
     * @throws  ParseException
     *            Invalid child element.
     *
     * @throws  IOException
     *            Internal I/O exception (e.g., thrown by XML blob writer).
     */
    public ElementHandler getChildHandler(String namespace,
                                          String localName,
                                          Attributes attrs)
        throws ParseException, IOException {

      if (xmlBlob == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.unrecognizedElement);
        pe.setInternalReason("Unrecognized element '" + localName + "'.");
        throw pe;
      } else {
        logger.fine("No child handler for " + localName +
                    ". Treating as arbitrary foreign XML.");
        return null;
      }
    }

    /**
     * Called to process an attribute. Designed to be overridden by derived
     * classes.
     *
     * @param   namespace
     *            Attribute namespace URI.
     *
     * @param   qualifiedName
     *            Attribute's qualified name.
     *
     * @param   localName
     *            Attribute's local name.
     *
     * @param   attrValue
     *            Attribute value.
     *
     * @throws  ParseException
     *            Invalid attribute.
     */
    public void processAttribute(String namespace,
                                 String qualifiedName,
                                 String localName,
                                 String attrValue) throws ParseException {
      processAttribute(namespace, localName, attrValue);
    }

    /**
     * Called to process an attribute. Designed to be overridden by derived
     * classes.
     *
     * @param   namespace
     *            Attribute namespace URI.
     *
     * @param   localName
     *            Attribute name.
     *
     * @param   value
     *            Attribute value.
     *
     * @throws  ParseException
     *            Invalid attribute.
     */
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) throws ParseException {}


    /**
     * Called to process this element when the closing tag is encountered.
     * The default implementation refuses to accept text() content, unless
     * the handler is configured to accept unrecognized XML with mixed content.
     */
    public void processEndElement() throws ParseException {
      if (value != null && !value.trim().equals("") && !mixedContent) {
        throw new ParseException(
            CoreErrorDomain.ERR.textNotAllowed);
      }
    }


    /**
     * If a derived class wishes to retrieve all unrecognized XML in a blob,
     * it calls this method. It must be called in the constructor, in
     * the parent element handler, or in {@link #processAttribute}.
     *
     * @param   xmlBlob
     *            Supplies the XML blob that stores the resulting XML.
     *
     * @param   mixedContent
     *            Specifies that the handler accepts mixed content XML.
     *
     * @param   fullTextIndex
     *            Flag indicating whether unrecognized XML should be processed
     *            for full-text indexing. If set, the resulting string ready for
     *            indexing is stored in {@link XmlBlob#fullText}.
     */
    public void initializeXmlBlob(XmlBlob xmlBlob,
                                  boolean mixedContent,
                                  boolean fullTextIndex) {

      assert okToInitializeXmlBlob;

      this.xmlBlob = xmlBlob;
      this.mixedContent = mixedContent;
      this.innerXmlStringWriter = new StringWriter();
      try {
        this.innerXml = new XmlWriter(innerXmlStringWriter);

        // The XmlWriter constructor doesn't actually throw an IOException, so
        // once that constructor is fixed we can remove this catch block.
      } catch (IOException impossible) {
        throw new AssertionError(impossible);
      }
      this.fullTextIndex = fullTextIndex;
      if (fullTextIndex) {
        this.fullTextIndexWriter = new StringWriter();
      }
    }


    /**
     * Utility routine that combines the current state of {@code xml:base}
     * with the specified URI to obtain an absolute URI.
     * <p>
     *
     * See http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html
     * for more information.
     *
     * @param   uriValue
     *            URI to be interpreted in the context of {@code xml:base}.
     *
     * @return  Corresponding absolute URI.
     *
     * @throws  ParseException
     *            Invalid URI.
     */
    public String getAbsoluteUri(String uriValue) throws ParseException {
      try {
        return getCumulativeXmlBase(xmlBase, uriValue);
      } catch (URISyntaxException e) {
        throw new ParseException(e.getMessage());
      }
    }

    /**
     * Utility method to return the value of an xsd:boolean attribute.
     *
     * @param attrs
     *          Elements attributes to test against.
     *
     * @param attrName
     *          Attribute name.
     *
     * @return the Boolean value if the attribute is present, or
     *         {@code null} otherwise.
     *
     * @throws ParseException if attribute value is not valid xsd:boolean.
     */
    public Boolean getBooleanAttribute(Attributes attrs, String attrName)
        throws ParseException {
      Boolean result = null;
      String value = attrs.getValue("", attrName);

      try {
        result = parseBooleanValue(value);
      } catch (ParseException ex) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.invalidAttributeValue);
        pe.setInternalReason("Invalid value for " + attrName +
            " attribute: " + value);
        throw pe;
      }

      return result;
    }

    /**
     * Utility method to parse provided xsd:boolean value.
     *
     * @param value
     *          xsd:boolean value to parse
     *
     * @return the Boolean value or {@code null}
     *
     * @throws ParseException if value is not valid xsd:boolean.
     */
    protected Boolean parseBooleanValue(String value)
        throws ParseException {
      if (value == null) {
        return null;
      }

      if (value.equalsIgnoreCase("false") || value.equals("0")) {
        return Boolean.FALSE;
      }

      if (value.equalsIgnoreCase("true") || value.equals("1")) {
        return Boolean.TRUE;
      }

      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidBooleanAttribute);
      pe.setInternalReason("Invalid value for boolean attribute: " + value);
      throw pe;
    }
  }


  /**
   * Root element handler.
   */
  protected ElementHandler rootHandler;


  /** Root element namespace URI. */
  protected String rootNamespace;


  /** Root element name. */
  protected String rootElementName;


  /** Top of the element handler stack. */
  ElementHandler curHandler;


  /** Number of unrecognized elements on the stack. */
  int unrecognizedElements = 0;


  /** Document locator used to get line and column numbers for SAX events. */
  Locator locator;

  /**
   * Used to track namespace declarations seen within the current parse
   * stream.
   */
  private static class NamespaceDecl {

    private NamespaceDecl(XmlNamespace ns) {
      this.ns = ns;
    }

    /** The declared namespace */
    XmlNamespace ns;

    /**
     * {@code true} if the namespace declaration occurs inside an XmlBlob.
     */
    boolean inBlob;
  }

  /**
   * Set of all namespace declarations valid at the current location.
   * Includes namespace declarations from all ancestor elements.
   */
  protected HashMap<String, Stack<NamespaceDecl>> namespaceMap =
    new HashMap<String, Stack<NamespaceDecl>>();


  /**
   * Namespace declarations for the current element.
   * Valid during {@link #startElement}.
   */
  ArrayList<XmlNamespace> elementNamespaces =
    new ArrayList<XmlNamespace>();


  /**
   * Parses XML.
   *
   * @param   reader
   *            Supplies the XML to parse.
   *
   * @param   rootHandler
   *            The root element handler corresponding to the expected document
   *            type.
   *
   * @param   rootNamespace
   *            Root element namespace URI.
   *
   * @param   rootElementName
   *            Root element name.
   *
   * @throws  IOException
   *            Thrown by {@code reader}.
   *
   * @throws  ParseException
   *            XML failed to validate against the schema implemented by
   *            {@code rootHandler}.
   */
  public void parse(Reader reader,
                    ElementHandler rootHandler,
                    String rootNamespace,
                    String rootElementName)
      throws IOException,
             ParseException {

    InputSource is = new InputSource(reader);

    this.rootHandler = rootHandler;
    this.rootNamespace = rootNamespace;
    this.rootElementName = rootElementName;
    parse(is);
  }



  /**
   * Parses XML.
   *
   * @param   input
   *            Supplies the XML to parse.
   *
   * @param   rootHandler
   *            The root element handler corresponding to the expected document
   *            type.
   *
   * @param   rootNamespace
   *            Root element namespace URI.
   *
   * @param   rootElementName
   *            Root element name.
   *
   * @throws  IOException
   *            Thrown by {@code input}.
   *
   * @throws  ParseException
   *            XML failed to validate against the schema implemented by
   *            {@code rootHandler}.
   */
  public void parse(InputStream input,
                    ElementHandler rootHandler,
                    String rootNamespace,
                    String rootElementName)
      throws IOException,
             ParseException {

    InputSource is = new InputSource(input);

    this.rootHandler = rootHandler;
    this.rootNamespace = rootNamespace;
    this.rootElementName = rootElementName;
    parse(is);
  }


  /**
   * Parses XML from a content source provided to the parser at
   * construction time.
   *
   * @param   rootHandler
   *            The root element handler corresponding to the expected document
   *            type.
   *
   * @param   rootNamespace
   *            Root element namespace URI.
   *
   * @param   rootElementName
   *            Root element name.
   *
   * @throws  IOException
   *            Thrown by {@code reader}.
   *
   * @throws  ParseException
   *            XML failed to validate against the schema implemented by
   *            {@code rootHandler}.
   */
  public void parse(ElementHandler rootHandler,
                    String rootNamespace,
                    String rootElementName)
      throws IOException,
             ParseException {

    throw new IllegalStateException("No content source defined");

  }


  /**
   * Parses XML.
   *
   * @param   is
   *            Supplies the XML to parse.
   *
   * @throws  IOException
   *            Thrown by {@code is}.
   *
   * @throws  ParseException
   *            XML failed to validate against the schema implemented by
   *            {@code rootHandler}.
   *
   */
  protected void parse(InputSource is)
      throws IOException,
             ParseException {

    try {

      // Lazy initialization of the parser factory.  There is a minor
      // init-time race condition here if two parsers are created
      // simultaneously, but the getSAXParserFactory() impl is thread-safe
      // and worse case scenario is that multiple parser factories are
      // initially created during the race.  Double-checked locking bug
      // makes it harder to do better w/out significant overhead.
      if (parserFactory == null) {
        parserFactory = getSAXParserFactory();
      }

      SAXParser sp = parserFactory.newSAXParser();
      ParserAdapter pa = new ParserAdapter(sp.getParser());
      pa.setContentHandler(this);
      pa.parse(is);

    } catch (SAXException e) {

      Exception rootException = e.getException();

      if (rootException instanceof ParseException) {

        throwParseException((ParseException) rootException);

      } else if (rootException instanceof IOException) {

        LogUtils.logException(logger, Level.WARNING, null, e);
        throw (IOException) rootException;

      } else {

        LogUtils.logException(logger, Level.FINE, null, e);
        throw new ParseException(e);
      }
    } catch (ParserConfigurationException e) {

        LogUtils.logException(logger, Level.WARNING, null, e);
        throw new ParseException(e);
    }
  }


  /** Throws a parse exception with line/column information. */
  protected void throwParseException(ParseException e) throws ParseException {

    if (locator != null) {

      String elementLocation = "";
      if (curHandler != null) {
        elementLocation += ", element " + curHandler.qName;
      }

      String location =
        "[Line " + String.valueOf(locator.getLineNumber()) +
        ", Column " + String.valueOf(locator.getColumnNumber()) +
        elementLocation + "] ";

      LogUtils.logException(logger, Level.FINE, location, e);

      throw new ParseException(location + e.getMessage(), e);

    } else {

      LogUtils.logException(logger, Level.FINE, null, e);
      throw e;
    }
  }


  /**
   * Computes a cumulative value of {@code xml:base} based on a prior value
   * and a new value. If the new value is an absolute URI, it is returned
   * unchanged. If the new value is a relative URI, it is combined with the
   * prior value.
   *
   * @param   curBase
   *            Current value of {@code xml:base} or {@code null}.
   *
   * @param   newBase
   *            New value of {@code xml:base}.
   *
   * @return  Combined value of {@code xml:base}, which is guaranteed to be
   *          an absolute URI.
   *
   * @throws  URISyntaxException
   *            Invalid value of {@code xml:base} (doesn't parse as a valid
   *            relative/absolute URI depending on {@code xml:base} context).
   */
  static String getCumulativeXmlBase(String curBase, String newBase)
      throws URISyntaxException {

    URI newBaseUri = new URI(newBase);

    if (curBase == null || curBase.equals("")) {

      // We require an absolute URI.
      if (!newBaseUri.isAbsolute()) {
        throw new URISyntaxException(
          newBase, "No xml:base established--need an absolute URI.");
      }

      return newBase;
    }

    URI curBaseUri = new URI(curBase);
    URI resultUri = curBaseUri.resolve(newBaseUri);
    assert resultUri.isAbsolute();
    return resultUri.toString();
  }


  /** SAX callback. */
  @Override
  public void startElement(String namespace,
                           String localName,
                           String qName,
                           Attributes attrs) throws SAXException {

    logger.fine("Start element " + qName);

    ElementHandler parentHandler = curHandler;

    if (curHandler == null) {

      // Edge case, parsing the root element.
      if (namespace.equals(rootNamespace) &&
          localName.equals(rootElementName)) {

        curHandler = rootHandler;
      } else if (rootElementName != null) {

        throw new SAXException(
            new ParseException("Invalid root element, expected " +
               "(namespace uri:local name) of " +
               "(" + rootNamespace + ":" + rootElementName + ")" +
               ", found (" + namespace + ":" + localName
               ));
      }

    } else if (unrecognizedElements == 0) {
      // Common case, parsing an XML element. Event signals
      // the start of a child element.  Looks for child element
      // handler. If no handler is found, then parses element as
      // XML Blob (if enabled).
      try {
        curHandler = curHandler.getChildHandler(
            namespace, qName, localName, attrs, elementNamespaces);
      } catch (ParseException e) {
        throw new SAXException(e);
      } catch (IOException e) {
        throw new SAXException(e);
      }
    }

    if (curHandler != null && unrecognizedElements == 0) {
      // Common case: A child handler was found.  Keep parsing.
      curHandler.parent = parentHandler;
      curHandler.qName = qName;

      // Propagate xml:lang and xml:base.
      if (parentHandler != null) {
        curHandler.xmlLang = parentHandler.xmlLang;
        curHandler.xmlBase = parentHandler.xmlBase;
      }

      try {

        // First pass to extract xml:lang and xml:base.
        for (int i = attrs.getLength() - 1; i >= 0; --i) {

          String attrNamespace = attrs.getURI(i);
          String attrLocalName = attrs.getLocalName(i);
          String attrValue = attrs.getValue(i);

          if (attrNamespace.equals("http://www.w3.org/XML/1998/namespace")) {

            if (attrLocalName.equals("lang")) {

              curHandler.xmlLang = attrValue;
              logger.finer("xml:lang=" + attrValue);

            } else if (attrLocalName.equals("base")) {

              curHandler.xmlBase = getCumulativeXmlBase(curHandler.xmlBase,
                                                        attrValue);
              logger.finer("xml:base=" + curHandler.xmlBase);
            }
          }
        }

        // Second pass to process attributes.
        for (int i = attrs.getLength() - 1; i >= 0; --i) {

          String attrNamespace = attrs.getURI(i);
          String attrQName = attrs.getQName(i);
          String attrLocalName = attrs.getLocalName(i);
          String attrValue = attrs.getValue(i);

          logger.finer("Attribute " + attrLocalName + "='" + attrValue + "'");

          curHandler.processAttribute(
              attrNamespace, attrQName, attrLocalName, attrValue);
        }

      } catch (ParseException e) {
        throw new SAXException(e);
      } catch (URISyntaxException e) {
        throw new SAXException(new ParseException(e.getMessage()));
      } catch (NumberFormatException e) {
        throw new SAXException(
          new ParseException("Invalid integer format. " + e.getMessage()));
      }

      // If the current handler accepts random XML, process the state acquired
      // so far.
      curHandler.okToInitializeXmlBlob = false;

      if (curHandler.xmlBlob != null) {

        // Store xml:lang and xml:base state, if any.
        if (curHandler.xmlLang != null) {
          curHandler.xmlBlob.setLang(curHandler.xmlLang);
        }

        if (curHandler.xmlBase != null) {
          curHandler.xmlBlob.setBase(curHandler.xmlBase);
        }
      }

    } else { // curHandler == null || unrecognizedElements > 0

      // INVARIANT: We're processing an unrecognized (blob) element.
      // If curHandler == null, we just encountered our first
      // unrecognized element, so make this element part of the
      // XmlBlob of the parent element.  Otherwise,
      // unrecognizedElements > 0 (&& curHandler != NULL), and we're
      // processing an element nested inside an unrecognized element
      // (ie.a structured XmlBlob).

      ++unrecognizedElements;

      // Flag all namespace declarations on the current element as occurring
      // within a blob
      for (XmlNamespace ns : elementNamespaces) {
        Stack<NamespaceDecl> nsDecls = namespaceMap.get(ns.getAlias());
        if (nsDecls != null && nsDecls.size() > 0) {
          nsDecls.peek().inBlob = true;
        }
      }

      if (curHandler == null) {
        curHandler = parentHandler;
      }

      // This element hasn't been recognized by the handler.
      // If the handler allows foreign XML, we'll start accumulating it as
      // a string.
      if (curHandler != null && curHandler.innerXml != null) {

        ArrayList<XmlWriter.Attribute> attrList =
          new ArrayList<XmlWriter.Attribute>(attrs.getLength());
        for (int i = attrs.getLength() - 1; i >= 0; --i) {

          String qNameAttr = attrs.getQName(i);
          ensureBlobNamespace(curHandler, qNameAttr);

          String value = attrs.getValue(i);

          XmlWriter.Attribute attr = new XmlWriter.Attribute(qNameAttr, value);
          attrList.add(attr);

          if (curHandler.fullTextIndex) {
            curHandler.fullTextIndexWriter.write(value);
            curHandler.fullTextIndexWriter.write(" ");
          }
        }

        try {
          ensureBlobNamespace(curHandler, qName);
          curHandler.innerXml.startElement(null, qName, attrList,
                                           elementNamespaces);
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
    }

    // Make way for next element's state.
    elementNamespaces.clear();
  }


  /** SAX callback. */
  @Override
  public void endElement(String namespace, String localName, String qName)
      throws SAXException {

    logger.fine("End element " + qName);

    if (unrecognizedElements > 0) {

      --unrecognizedElements;

      if (curHandler != null && curHandler.innerXml != null) {
        try {
          curHandler.innerXml.endElement();
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }

    } else if (curHandler != null) {

      if (curHandler.xmlBlob != null) {

        StringBuffer blob = curHandler.innerXmlStringWriter.getBuffer();
        if (blob.length() != 0) {
          curHandler.xmlBlob.setBlob(blob.toString());

          if (curHandler.fullTextIndex) {
            curHandler.xmlBlob.setFullText(
              curHandler.fullTextIndexWriter.toString());
          }
        }
      }

      try {
        if (curHandler.buffer != null) {
          curHandler.value = curHandler.buffer.toString();

          // Free the memory associated with the buffer.
          curHandler.buffer = null;
        }
        curHandler.processEndElement();
      } catch (ParseException e) {
        throw new SAXException(e);
      }

      curHandler = curHandler.parent;
    }
  }

  /** SAX callback. */
  @Override
  public void characters(char[] text, int start, int len) throws SAXException {

    if (curHandler != null) {

      if (unrecognizedElements == 0) {

        if (curHandler.buffer == null) {
          curHandler.buffer = new StringBuilder();
        }

        curHandler.buffer.append(text, start, len);
      }

      if (curHandler.innerXml != null &&
          (curHandler.mixedContent || unrecognizedElements > 0)) {

        if (curHandler.fullTextIndex) {
          curHandler.fullTextIndexWriter.write(text, start, len);
          curHandler.fullTextIndexWriter.write("\n");
        }

        try {
          curHandler.innerXml.characters(new String(text, start, len));
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
    }
  }


  /** SAX callback. */
  @Override
  public void ignorableWhitespace(char[] text, int start, int len)
      throws SAXException {

    if (curHandler != null && curHandler.innerXml != null &&
        (curHandler.mixedContent || unrecognizedElements > 0)) {

      try {
        curHandler.innerXml.writeUnescaped(new String(text, start, len));
      } catch (IOException e) {
        throw new SAXException(e);
      }
    }
  }


  /** SAX callback. */
  @Override
  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }


  /** SAX callback. */
  @Override
  public void startPrefixMapping(String alias, String uri) {

    Stack<NamespaceDecl> mapping = namespaceMap.get(alias);
    if (mapping == null) {
      mapping = new Stack<NamespaceDecl>();
      namespaceMap.put(alias, mapping);
    }

    XmlNamespace ns = new XmlNamespace(alias, uri);
    NamespaceDecl nsDecl = new NamespaceDecl(ns);
    mapping.push(nsDecl);
    elementNamespaces.add(ns);
  }


  /** SAX callback. */
  @Override
  public void endPrefixMapping(String alias) {
    namespaceMap.get(alias).pop();
  }


  /** Ensures that the namespace from the QName is stored with the blob. */
  private void ensureBlobNamespace(ElementHandler handler, String qName)
      throws SAXException {

    // Get the namespace.
    NamespaceDecl nsDecl = null;
    String alias = qName.substring(0, Math.max(0, qName.indexOf(":")));
    if (alias.equals("xml")) {
      // "xml:" doesn't need a declaration.
      return;
    }

    Stack<NamespaceDecl> mapping = namespaceMap.get(alias);
    if (mapping != null && mapping.size() != 0) {
      nsDecl = mapping.peek();
    }

    // The namespace might be null for a namespace-less element.
    if (nsDecl == null && alias.length() != 0) {
        throw new SAXException(
            new ParseException("Undeclared namespace prefix: " + alias));
    }

    // Make sure the namespace is described within the blob if it was
    // originally declared externally to it
    if (nsDecl != null && !nsDecl.inBlob && nsDecl.ns != null &&
        !handler.blobNamespaces.contains(alias)) {
      handler.blobNamespaces.add(alias);
      handler.xmlBlob.namespaces.add(
          new XmlNamespace(alias, nsDecl.ns.getUri()));
    }
  }
}
