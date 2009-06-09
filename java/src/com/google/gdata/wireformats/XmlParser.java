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


package com.google.gdata.wireformats;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.util.common.base.StringUtil;
import com.google.common.collect.Maps;
import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.XmlEventSource;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.util.LogUtils;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * XML parser, branched from {@link com.google.gdata.util.XmlParser}.
 * <p>
 * The plan is to integrate it tighter with the {@code WireFormatParser}
 * interface, remove unnecessary interfaces and parameters (in our new
 * data model, there is only one {@link XmlParser.ElementHandler}),
 * and move it away from the util package.
 * <p>
 * Existing data classes continue to use the old XML parser until they've
 * been migrated to the new data model, at which point they will start to
 * use the new XML parser.
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
 * {@link #parse(Element)} method.
 *
 * @see     XmlParser.ElementHandler
 */
public class XmlParser extends DefaultHandler implements WireFormatParser {


  private static final Logger logger =
    Logger.getLogger(XmlParser.class.getName());


  /**
   * Base class for custom element handlers.
   * <p>
   * Marked package private, as want to limit exposure to the
   * wireformats package and anticipate removing the class
   * entirely, or merging it with {@link XmlHandler}.
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
  static class ElementHandler {


    /** This element's QName. Used for error reporting. */
    public String qName;


    /** This element's text() value. */
    public String value;

    /** Temporary buffer for building up the text() value. */
    private StringBuffer buffer;

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
     *
     * @param   qn
     *            Child element's qualified name.
     *
     * @param   attrs
     *            Child element attributes. These attributes will be
     *            communicated to the child element handler through its
     *            {@link #processAttribute} method. They are passed here because
     *            sometimes the value of some attribute determines the element's
     *            content type, so different element handlers may be needed.
     *
     * @param namespaces
     *            List of namespaces in effect for child element.
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
    public ElementHandler getChildHandler(QName qn,
                                          Attributes attrs,
                                          List<XmlNamespace> namespaces)
        throws ParseException, IOException {

      if (xmlBlob == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.unrecognizedElement);
        pe.setInternalReason(
            "Unrecognized element '" + qn.getLocalName() + "'.");
        throw pe;
      } else {
        logger.fine("No child handler for " + qn.getLocalName() +
                    ". Treating as arbitrary foreign XML.");
        return null;
      }
    }

    /**
     * Called to process an attribute. Designed to be overridden by derived
     * classes.
     *
     * @param   qn
     *            Attribute's qualified name.
     *
     * @param   attrValue
     *            Attribute value.
     *
     * @throws  ParseException
     *            Invalid attribute.
     */
    public void processAttribute(QName qn,
                                 String attrValue) throws ParseException {}


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
                                  boolean fullTextIndex) throws IOException {

      assert okToInitializeXmlBlob;

      this.xmlBlob = xmlBlob;
      this.mixedContent = mixedContent;
      this.innerXmlStringWriter = new StringWriter();
      this.innerXml = new XmlWriter(innerXmlStringWriter);
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
      String attrValue = attrs.getValue("", attrName);

      try {
        result = parseBooleanValue(attrValue);
      } catch (ParseException ex) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.invalidAttributeValue);
        pe.setInternalReason("Invalid value for " + attrName
           + " attribute: " + attrValue);
        throw pe;
      }

      return result;
    }

    /**
     * Utility method to parse provided xsd:boolean value.
     *
     * @param booleanValue
     *          xsd:boolean value to parse
     *
     * @return the Boolean value or {@code null}
     *
     * @throws ParseException if value is not valid xsd:boolean.
     */
    protected Boolean parseBooleanValue(String booleanValue)
        throws ParseException {
      if (booleanValue == null) {
        return null;
      }

      if (booleanValue.equalsIgnoreCase("false") || booleanValue.equals("0")) {
        return Boolean.FALSE;
      }

      if (booleanValue.equalsIgnoreCase("true") || booleanValue.equals("1")) {
        return Boolean.TRUE;
      }

      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidBooleanAttribute);
      pe.setInternalReason(
          "Invalid value for boolean attribute: " + booleanValue);
      throw pe;
    }
  }

  /** Input properties for parsing */
  protected final StreamProperties props;

  /** XML event source, usually a SAX parser. */
  private final XmlEventSource eventSource;

  /** Root element handler. */
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
  protected Map<String, Stack<NamespaceDecl>> namespaceMap = Maps.newHashMap();

  /**
   * Namespace declarations for the current element.
   * Valid during {@link #startElement}.
   */
  ArrayList<XmlNamespace> elementNamespaces = new ArrayList<XmlNamespace>();

  /**
   * Construct XML parser for given reader.
   *
   * @param props stream properties for parsing
   * @param r reader where input is retrieved from
   * @param cs character set used to encode input
   */
  public XmlParser(StreamProperties props, Reader r, Charset cs) {
    this(props, new SaxEventSource(r));

    // Fix XML charset handling.
    Preconditions.checkNotNull(cs, "cs");
  }

  /**
   * Construct XML parser for a given event source.
   *
   * @param props stream properties for parsing
   * @param eventSource event source
   */
  public XmlParser(StreamProperties props, XmlEventSource eventSource) {
    Preconditions.checkNotNull(props, "stream properties");
    Preconditions.checkNotNull(eventSource, "eventSource");
    this.props = props;
    this.eventSource = eventSource;
  }

  public Element parse(Element element)
      throws IOException, ParseException, ContentValidationException {

    ValidationContext vc = new ValidationContext();
    ElementMetadata<?, ?> metadata = props.getRootMetadata();

    this.rootHandler = createRootHandler(vc, element, metadata);
    QName elementName = (metadata == null) ? element.getElementId()
        : metadata.getName();
    XmlNamespace elementNs = elementName.getNs();
    this.rootNamespace = elementNs == null ? null : elementNs.getUri();
    this.rootElementName = elementName.getLocalName();

    try {
      eventSource.parse(this);
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
    }

    return element.resolve(metadata);
  }

  /**
   * Create the xml handler for the root element.  Subclasses can supply their
   * own parse handlers.
   */
  protected XmlHandler createRootHandler(ValidationContext vc,
      Element element, ElementMetadata<?, ?> metadata) {
    return new XmlHandler(vc, null, element, metadata);
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
    } else if (curHandler != null && unrecognizedElements == 0) {
      // Common case, parsing an XML element. Event signals
      // the start of a child element.  Looks for child element
      // handler. If no handler is found, then parses element as
      // XML Blob (if enabled).
      try {
        curHandler = curHandler.getChildHandler(
            createQName(qName, namespace, localName), attrs, elementNamespaces);
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
        for (int i = 0; i < attrs.getLength(); i++) {

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
        for (int i = 0; i < attrs.getLength(); i++) {

          String attrNamespace = attrs.getURI(i);
          String attrQName = attrs.getQName(i);
          String attrLocalName = attrs.getLocalName(i);
          String attrValue = attrs.getValue(i);

          logger.finer("Attribute " + attrLocalName + "='" + attrValue + "'");

          curHandler.processAttribute(
              createQName(attrQName, attrNamespace, attrLocalName),
              attrValue);
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
          curHandler.buffer = new StringBuffer();
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
  public void setDocumentLocator(Locator newLocator) {
    this.locator = newLocator;
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
  private void ensureBlobNamespace(ElementHandler handler, String qName) {

    // Get the namespace.
    NamespaceDecl nsDecl = null;
    String alias = qName.substring(0, Math.max(0, qName.indexOf(":")));
    if (alias.equals("xml")) {
      // "xml:" doesn't need a declaration.
      return;
    }

    Stack<NamespaceDecl> mapping = namespaceMap.get(alias);
    if (mapping != null) {
      nsDecl = mapping.peek();
    }

    // The namespace might be null for a namespace-less element.
    assert alias.length() == 0 || nsDecl != null :
      "Namespace alias '" + alias + "' should be mapped in 'namespaceMap'.";

    // Make sure the namespace is described within the blob if it was
    // originally declared externally to it
    if (nsDecl != null && !nsDecl.inBlob && nsDecl.ns != null &&
        !handler.blobNamespaces.contains(alias)) {
      handler.blobNamespaces.add(alias);
      handler.xmlBlob.getNamespaces().add(
          new XmlNamespace(alias, nsDecl.ns.getUri()));
    }
  }

  private static QName createQName(
      String qName, String nsUri, String localName) {

    XmlNamespace ns = null;
    if (!StringUtil.isEmpty(nsUri)) {
      String[] parts = qName.split(":");
      if (parts.length == 2) {
        ns = new XmlNamespace(parts[0], nsUri);
      } else {
        ns = new XmlNamespace(null, nsUri);
      }
    }
    return new QName(ns, localName);

  }
}
