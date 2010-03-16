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
 

package com.google.gdata.util.common.xml;


import com.google.gdata.util.common.base.CharMatcher;
import com.google.gdata.util.common.base.StringUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Implements a simple XML writer on top of java.io.PrintWriter.
 * This implementation can be conveniently used to generate XML responses in
 * servlets.
 *
 * <p>The XmlWriter class exposes a number of protected methods that enable it
 * to be subclassed for the purposes of customizing its output.  See
 * {@link com.google.javascript.util.JsonWriter} for an example.
 *
 * <h2>Optional Behaviors</h2>
 *
 * There are several behaviors of this class that are optionally available.
 * These features are enabled by passing a {@code Set<XmlWriter.WriterFlags>}
 * to the constructor:
 *
 * <pre>new XmlWriter(sw, EnumSet.of(WriterFlags.WRITE_HEADER,
 *     WriterFlags.EXPAND_EMPTY, WriterFlags.PRETTY_PRINT), null)</pre>
 *
 * The caller can supply any of the values enumerated in
 * {@link XmlWriter.WriterFlags}, or none.
 *
 * Once a feature has been enabled in the constructor, it cannot be turned off.
 *
 * <h3>Including XML Header</h3>
 * The {@code WRITE_HEADER} flags causes {@code XmlWriter} to emit an XML
 * header at the beginning of the XML document:
 *
 * <pre>&lt;?xml version='1.0'?&gt;</pre>
 *
 * <h3>Expanding Empty Elements</h3>
 * The {@code EXPAND_EMPTY} flags causes {@code XmlWriter} to emit "expanded"
 * empty elements (elements consisting of distinct begin and end tags):
 *
 * <pre>
 * &lt;foo&gt;
 *    &lt;wee really="yeah"&gt&lt;/wee&gt;
 * &lt;/foo&gt;</pre>
 *
 * <h3>Pretty Printing</h3>
 * <p>The {@code PRETTY_PRINT} flag enables pretty printing. This feature
 * formats the XML output with using new lines and tab characters:
 *
 * <pre>
 * &lt;foo&gt;
 *    &lt;bar&gt;
 *        &lt;wee really="yeah"/&gt;
 *    &lt;/bar&gt;
 * &lt;/foo&gt;</pre>
 *
 * <h4>Caveats</h4>
 * Elements containing mixed content (i.e. both text children and full-fledged
 * elements) will not generally be formatted correctly. If your XML document
 * contains mixed content, you may not want to use pretty printing:
 *
 * <p>Will produce wonky formatting:
 * <pre>
 *  w.startElement(null, "txt", null, null);
 *  w.simpleElement(null, "fooey", null, null);
 *  w.characters("Kleenex");
 *  w.endElement(null, "txt");</pre>
 *
 * <pre>
 * &lt;txt&gt;
 *    &lt;fooey/&gt;Kleenex
 * &lt;/txt&gt;</pre>
 *
 * <p>You can ensure correct formatting of mixed content in your document
 * by using the {@link #innerXml(String)} method to write raw XML.
 *
 * <p>Correctly formatted:
 * <pre>
 *  w.startElement(null, "txt", null, null);
 *  w.innerXml("&lt;fooey/&gt;");
 *  w.characters("Kleenex");
 *  w.endElement(null, "txt");</pre>
 *
 * <pre>
 * &lt;txt&gt;&lt;fooey/&gt;Kleenex&lt;/txt&gt;</pre>
 *
 * 
 * 
 */
public class XmlWriter {

  /**
   * Enumeration type that can be used to configure the XmlWriter behavior.
   */
  public enum WriterFlags {

    /** Output xml header */
    WRITE_HEADER,

    /** Use start/end element pair format for empty elements */
    EXPAND_EMPTY,

    /** Pretty print the XML document. Adds indentation and new lines. */
    PRETTY_PRINT,
  }

  protected final Set<WriterFlags> flags;

  /**
   * The Namespace class represents an XML Namespace that can be used in output
   * with an XML writer.
   * 
   * @deprecated Use the {@link XmlNamespace} class instead.
   */
  @Deprecated
  public static final class Namespace extends XmlNamespace {
    
    public Namespace(String alias, String uri) {
      super(alias, uri);
    }
  }

  /**
   * The Attribute class represents an XML attribute.
   */
  public static final class Attribute {

    public final String nsAlias;
    public final String name;
    public final String value;

    /**
     * Constructs an unqualified XML attribute.
     *
     * @param name the attribute name.
     * @param value the attribute value.
     */
    public Attribute(String name, String value) {
      this(null, name, value);
    }

    /**
     * Constructs an namespace-qualified XML attribute.
     *
     * @param nsAlias the attribute namespace prefix alias.
     * @param name the attribute name.
     * @param value the attribute value.
     */
    public Attribute(String nsAlias, String name, String value) {

      /**
       * Back compat for clients that use the two-arg constructor to
       * write namespace qualified attributes of the form "alias:name".
       */
      if (nsAlias == null) {
        int separator = name.indexOf(':');
        if (separator > 0) {
          nsAlias = name.substring(0, separator);
          name = name.substring(separator + 1);
        }
      }
      this.nsAlias = nsAlias;
      this.name = name;
      this.value = value;
    }

    /*
     * Constructs an xsd:boolean-valued XML attribute.
     *
     * @param the attribute name.
     * @param the attribute value.
     */
    public Attribute(String name, boolean value) {
      this(null, name, value ? "true" : "false");
    }
  }

  /**
   * The Element class contains information about an XML element. Used to keep
   * track of namespace alias/URI mappings. This class may be subclassed by
   * XmlWriter subclasses that want to track additional information about output
   * elements. The {@link #createElement(String, String, String)} method can be
   * overridden to instantiate a specialized Element subclass.
   */
  protected static class Element {

    /**
     * Sentinel value used for repeating elements.
     * @see #repeatingCount
     * @see #repeatingIndex
     */
    public static final int NOT_REPEATING = -1;

    /**
     *  Namespace declarations associated with this element.
     */
    public List<XmlNamespace> nsDecls;

    /**
     * Namespace prefix for the element.
     */
    public String nsAlias;

    /**
     * Full namespace uri for the element.
     */
    public final String nsUri;

    /**
     * Local name of the element.
     */
    public final String name;

    /**
     * xml:lang attribute of the element.
     */
    public String xmlLang;

    /**
     * True if the element has attributes.
     */
    public boolean hasAttributes;

    /**
     * True if this element contains unformatted children (text nodes,
     * raw blobs of XML, or unescaped strings.
     */
    public boolean unformattedChildren;

    /**
     * Indicates the number of repeating child elements written within
     * the scope of the current {@link #startRepeatingElement()} /
     * {@link #endRepeatingElement()} pair for this element.  The value will be
     * {@link #NOT_REPEATING} if not currently writing repeating elements.
     */
    public int repeatingCount = NOT_REPEATING;

    /**
     * If an element is a part of a series of repeating element within its
     * parent, this will contain the zero-based index of the child, else
     * the value will be {@link #NOT_REPEATING}.
     */
    public int repeatingIndex = NOT_REPEATING;

    /**
     * The default namespace uri that will be active after closure of this
     * element.  If {@code null}, indicates that the default namespace for
     * the enclosing scope is the same as for the element scope.
     */
    public String enclosingDefaultNamespace;

    /**
     * Set to {@code false} if the closing bracket of the element's start
     * tag has been written (i.e. if {@link #writeOpenTagEnd()} has been
     * called yet).  Used to optimized elements that have no nested content
     * or children.
     */
    public boolean openTagEnded;

    /**
     * Constructs an element. This constructor should never be directly called,
     * always use the {@link #createElement(String, String, String)} method to
     * create new elements.
     */
    protected Element(String nsAlias, String nsUri, String name) {
      nsDecls = new ArrayList<XmlNamespace>();
      this.nsAlias = nsAlias;
      this.nsUri = nsUri;
      this.name = name;
    }

    /**
     * Adds a namespace declaration to the element, avoiding duplicates.
     */
    public void addNamespace(XmlNamespace ns) {
      if (!nsDecls.contains(ns)) {
        nsDecls.add(ns);
      }
    }
  }

  // indentation unit to use when pretty printing
  private static final String INDENTATION_UNIT = "\t";

  /**
   * The underlying output Writer associated with this XmlWriter.
   */
  protected final Writer writer;

  /**
   * Stack of currently opened elements.
   */
  private final Stack<Element> elementStack;

  /**
   * Current default namespace.
   */
  private String defaultNamespace;

  /**
   * Encoding of output
   */
  protected String encoding;

  private String nextDefaultNamespace;

  private Boolean standalone;

  /**
   * Constructor that allows standalone directive to be provided.  Please note
   * that using this constructor explicity causes the standalone header value
   * to be written and WRITE_HEADER flag to be set.
   *
   * @param   w output writer object.
   * @param   f writer configuration flags or {@code null for no flags}
   * @param   encoding charset encoding.
   * @param   standalone boolean where true=yes and false=no.
   * @throws  IOException thrown by the underlying writer
   *
   * @see WriterFlags
   */
  public XmlWriter(Writer w, Set<WriterFlags> f, String encoding,
      boolean standalone) throws IOException {
    this(w, f, encoding);
    this.standalone = standalone;
  }

  /**
   * The default namespace that will take effect on the next
   * element transition.
   *
   * @param   w output writer object.
   * @param   f writer configuration flags or {@code null for no flags}
   * @param   encoding charset encoding.  When non-null, implicitly causes
   *          the WRITE_HEADER flag to be set.
   * @throws  IOException thrown by the underlying writer.
   *
   * @see WriterFlags
   */
  @SuppressWarnings("unused")  // for IOException not thrown here
  public XmlWriter(Writer w, Set<WriterFlags> f, String encoding) throws IOException {

    this.writer = w;
    this.flags = f != null ? f : EnumSet.noneOf(WriterFlags.class);
    this.encoding = encoding;

    /*
     * Create the element stack and push an initial dummy element on it.
     * This enables parent checking and other basic functionality for
     * the root element without requiring special case handling of an
     * empty element stack.
     */
    this.elementStack = new Stack<Element>();
    Element rootElement = createElement(null, null, null);
    rootElement.openTagEnded = true;
    elementStack.push(rootElement);
  }

  /**
   * Constructs an XmlWriter instance associated that will generate
   * XML content to an underlying {@link Writer}.
   *
   * @param   w output writer object.
   * @throws  IOException thrown by the underlying writer.
   */
  public XmlWriter(Writer w) throws IOException {
    this(w, null, null);
  }

  /**
   * Constructor that writers header including encoding information.
   *
   * @param   w Output writer object.
   * @param   encoding output encoding to use in declaration.
   *
   * @throws  IOException thrown by the underlying writer.
   */
  public XmlWriter(Writer w, String encoding) throws IOException {
    this(w, EnumSet.of(WriterFlags.WRITE_HEADER), encoding);
  }

  /**
   * @deprecated see {@link #XmlWriter(Writer, Set, String)}
   */
  @Deprecated
  public XmlWriter(Writer w, boolean includeHeader) throws IOException {
    this(w, EnumSet.of(WriterFlags.WRITE_HEADER), null);
  }

  /**
   * Closes the XmlWriter and the underlying output writer.
   *
   * @throws  IOException thrown by the underlying writer.
   */
  public void close() throws IOException {
    writer.close();
  }

  /**
   * Flushes the XmlWriter and the underlying output writer.
   *
   * @throws  IOException thrown by the underlying writer.
   */
  public void flush() throws IOException {
    writer.flush();
  }

  /**
   * Sets the default namespace. It takes effect on the next element.
   *
   * @param namespace the new namespace to set as the default at the start
   *                  of the next element.
   */
  public void setDefaultNamespace(XmlNamespace namespace) {
    if (!namespace.uri.equals(defaultNamespace)) {
      nextDefaultNamespace = namespace.uri;
    }
  }

  /**
   * Constructs an Element instance that describes an XML element that
   * is about to be written.
   */
  protected Element createElement(String nsAlias, String nsUri, String name) {
    return new Element(nsAlias, nsUri, name);
  }

  /**
   * Returns the current element, or {@code null} if no element is being
   * written.
   */
  protected Element currentElement() {
    try {
      return elementStack.peek();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  /**
   * Return parent of current element. Stack has a dummy root element so
   * every real element has non-null a parent.
   *
   * @return Parent element.
   */
  protected Element parentElement() {
    return elementStack.get(elementStack.size() - 1 - 1);
  }

  /**
   * Starts an element. This element can be a parent to other elements.
   *
   * @param name element name.
   */
  public void startElement(String name) throws IOException {
    startElement(null, name, null, null);
  }

  /**
   * Starts an element. This element can be a parent to other elements.
   *
   * @param   namespace element namespace.
   * @param   name element name.
   * @param   attrs attributes. Can be {@code null}.
   * @param   namespaceDecls extra namespace declarations. Can be {@code null}.
   * @throws  IOException thrown by the underlying writer.
   */
  public void startElement(XmlNamespace namespace, String name,
                           Collection<Attribute> attrs,
                           Collection<? extends XmlNamespace> namespaceDecls)
      throws IOException {

    if (elementStack.size() == 1) {
      // start of root element
      writeBeginOutput();

      if (shouldWriteHeaderAndFooter()) {
        writeHeader(encoding);
      }
    }

    endOpenTag();

    Element element;
    if (namespace != null) {
      element = createElement(namespace.alias, namespace.uri, name);
    } else {
      element = createElement(null, null, name);
    }

    Element parentElement = currentElement();
    if (parentElement != null) {
      element.xmlLang = parentElement.xmlLang;

      // once formatting is turned off, we don't turn it back on until
      // we've departed our progenitor's tree
      element.unformattedChildren = parentElement.unformattedChildren;

      if (parentElement.repeatingCount != Element.NOT_REPEATING) {
        element.repeatingIndex = parentElement.repeatingCount++;
      }
    }

    elementStack.push(element);

    if (nextDefaultNamespace != null) {
      XmlNamespace defaultNs = new XmlNamespace(nextDefaultNamespace);
      defaultNamespace = nextDefaultNamespace;
      element.addNamespace(defaultNs);
      nextDefaultNamespace = null;
    }

    if (namespaceDecls != null) {
      for (XmlNamespace ns : namespaceDecls) {
        ensureNamespace(ns);
      }
    }

    if (namespace != null) {
      element.nsAlias = ensureNamespace(namespace);
    }

    writeOpenTagStart(element.nsAlias, name);

    for (XmlNamespace ns : element.nsDecls) {
      if (ns.alias != null && ns.alias.length() > 0) {
        writeAttribute("xmlns", ns.alias, ns.uri);  // xmlns:name=uri
      } else {
        writeAttribute(null, "xmlns", ns.uri);    // xmlns=uri
      }
    }

    if (attrs != null) {
      for (Attribute attr : attrs) {
        // Don't write out xml:lang if consistent with the current state.
        if (attr.name.equals("lang") && "xml".equals(attr.nsAlias)) {
          if (attr.value.equals(element.xmlLang)) {
            continue;
          } else {
            element.xmlLang = attr.value;
          }
        }
        writeAttribute(attr.nsAlias, attr.name, attr.value);
      }
    }

    if (flags.contains(WriterFlags.EXPAND_EMPTY)) {
      endOpenTag();
    }
  }

  /**
   * Tests whether header and footer should be included in output
   * @return true if header and footer should be included in output; false
   *     otherwise
   */
  protected boolean shouldWriteHeaderAndFooter() {
    return flags.contains(WriterFlags.WRITE_HEADER)
        || encoding != null || standalone != null;
  }

  /**
   * Ends the start tag for an element.
   */
  protected void endOpenTag() throws IOException {
    Element element = currentElement();
    if (!element.openTagEnded) {
      writeOpenTagEnd();
      element.openTagEnded = true;
    }
  }

  /**
   * Ends the current element. It is expected to be on top of the stack.
   *
   * @param   namespace element namespace.
   * @param   name element name.
   */
  public void endElement(XmlNamespace namespace, String name) 
      throws IOException {
    Element element = currentElement();
    assert namespace == null || element.nsUri.equals(namespace.uri);
    assert element.name.equals(name);
    endElement();
  }

  /**
   * Ends the current element. No integrity checking is performed.
   */
  public void endElement() throws IOException {
    Element element = currentElement();
    writeCloseTag(element.nsAlias, element.name);
    defaultNamespace = element.enclosingDefaultNamespace;
    elementStack.pop();

    // Write the footer if we're down to just the dummy element.
    if (elementStack.size() == 1) {
      if (this.shouldWriteHeaderAndFooter()) {
        writeFooter();
      }

      writeEndOutput();
    }
  }

  /**
   * Emits a simple element (without child elements).
   *
   * @param name element name.
   * @param value element value. Can be {@code null}.
   * @throws  IOException thrown by the underlying writer.
   */
  public void simpleElement(String name, String value) throws IOException {
    simpleElement(null, name, null, value);
  }

  /**
   * Indicates that a series of repeating elements are about to
   * be written.
   */
  @SuppressWarnings("unused")
  public void startRepeatingElement() throws IOException {
    Element currentElement = currentElement();
    if (currentElement.repeatingCount != Element.NOT_REPEATING) {
      throw new IllegalStateException("Existing repeating element is active");
    }
    currentElement.repeatingCount = 0;
  }

  /**
   * Indicates that the series of repeating elements have been completely
   * written.
   */
  @SuppressWarnings("unused")
  public void endRepeatingElement() throws IOException {
    currentElement().repeatingCount = Element.NOT_REPEATING;
  }

  /**
   * Emits a simple element (without child elements).
   *
   * @param   namespace element namespace.
   * @param   name element name.
   * @param   attrs attributes. Can be {@code null}.
   * @param   value element value. Can be {@code null}.
   * @throws  IOException thrown by the underlying writer.
   */
  public void simpleElement(XmlNamespace namespace, String name,
      List<Attribute> attrs, String value) throws IOException {

    startElement(namespace, name, attrs, null);
    characters(value);
    endElement(namespace, name);
  }

  /**
   * Determines if the specified namespace is declared at the current scope.
   *
   * @return  namespace alias if declared.
   *          {@code null} otherwise.
   */
  private String checkNamespace(String namespaceUri) {

    // Walk the stack from the top to find an element with this ns declaration.
    for (int i = elementStack.size() - 1; i >= 0; --i) {

      Element element = elementStack.get(i);

      for (XmlNamespace ns : element.nsDecls) {
        if (ns.alias != null && ns.uri.equals(namespaceUri)) {
          return ns.alias;
        }
      }
    }
    return null;
  }

  /**
   * Returns true if the feature is enabled and if the current element
   * does not contain unformatted children.
   */
  private boolean isPrettyPrintingEnabled() {
    return flags.contains(WriterFlags.PRETTY_PRINT)
        && !currentElement().unformattedChildren;
  }

  /**
   * Returns the current level of indentation.
   */
  private int getIndentationLevel() {
    // substract 2 from the stack size: one for the dummy element
    // pushed in the constructor and one due to the timing of
    // OpenTag/CloseTag calls relative to push/pop.
    return elementStack.size() - 2;
  }

  // pure convenience, baby
  private void writeNewline() throws IOException {
    writer.write("\n");
  }

  private void writeIndentation() throws IOException {
    writeUnitsOfIndentation(getIndentationLevel());
  }

  /**
   * Write the specified number of indentation units to the writer.
   */
  private void writeUnitsOfIndentation(int i) throws IOException {
    for (; i > 0; i--) {
      writer.write(INDENTATION_UNIT);
    }
  }

  /**
   * Ensures the namespace is in scope and returns its alias.
   * The top of the stack is assumed to be the current element.
   * If the namespace is not found, it is appended to the current element.
   *
   * @return  namespace alias or {@code null} for the default namespace.
   */
  protected String ensureNamespace(XmlNamespace namespace) {

    // Return the default namespace if possible.  There is no need to add
    // here, as it has already been added (with a null alias) by startElement()
    // if the default namespace is set or changed.
    if (namespace.uri.equals(defaultNamespace)) {
      return null;
    }

    String alias = checkNamespace(namespace.uri);

    // If not found, insert this namespace.
    if (alias == null) {
      Element current = currentElement();
      namespace = ensureUniqueNamespaceAlias(current, namespace);
      current.addNamespace(namespace);
      alias = namespace.alias;
    }

    return alias;
  }

  /**
   * Ensures a unique namespace alias within an element.  If the alias has
   * already been associated with a different URI, then a unique alias prefix
   * value will be set for the namespace.
   *
   * @param element the element to check.
   * @param namespace the XML namespace to ensure.
   */
  private XmlNamespace ensureUniqueNamespaceAlias(Element element,
      XmlNamespace namespace) {
    boolean unique;
    int serial = 0;

    do {
      unique = true;
      for (XmlNamespace ns : element.nsDecls) {
        if (namespace.alias.equals(ns.alias)) {
          unique = false;
          namespace =
              new XmlNamespace("ns" + String.valueOf(++serial), namespace.uri);
          break;
        }
      }
    } while (!unique);
    return namespace;
  }

  /**
   * Returns the namespace URI associated with a given namespace alias.
   *
   * @param     nsAlias namespace alias, or {@code null} for default namespace.
   * @return    namespace URI, or {@code null} if not found.
   */
  protected String getNamespaceUri(String nsAlias) {
    if (nsAlias == null) {
      return defaultNamespace;
    }
    // Walk the stack from the top to find an element with this ns alias.
    for (int i = elementStack.size() - 1; i >= 0; --i) {
      Element element = elementStack.get(i);
      for (XmlNamespace ns : element.nsDecls) {
        if (ns.getAlias().equals(nsAlias)) {
          return ns.getUri();
        }
      }
    }
    return null;
  }

  /**
   * writes beginning of output if any.
   * @throws IOException
   */
  @SuppressWarnings("unused")
  protected void writeBeginOutput() throws IOException {
  }

  /**
   * Writes any closing information to the output writer.  This is used by
   * subclasses to write anything they need to close out the object.
   * {@link #close()} should not be used because it closes the underlying
   * stream, which we don't always want to do.
   * @throws IOException
   */
  @SuppressWarnings("unused")
  protected void writeEndOutput() throws IOException {
  }

  /**
   * Writes basic XML headers including XML version, standalone, and encoding.
   * @param enc the XML encoding for the output.  Can be {@code null}.
   * @throws  IOException thrown by the underlying writer.
   */
  protected void writeHeader(String enc) throws IOException {
    writer.write("<?xml");
    writeAttribute("version", "1.0");
    if (enc != null) {
      writeAttribute("encoding", enc);
    }
    if (standalone != null) {
      writeAttribute("standalone", (standalone ? "yes" : "no"));
    }
    writer.write("?>");
  }

  /**
   * Writes footer, if any, that corresponds to the header.
   * @throws IOException
   */
  @SuppressWarnings("unused")
  protected void writeFooter() throws IOException {
  }

  /**
   * Writes a namespace qualified element or attribute name.
   *
   * @param nsAlias namespace alias prefix.
   * @param name namespace-relative local name.
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeQualifiedName(String nsAlias, String name) 
      throws IOException {

    if (nsAlias != null && nsAlias.length() > 0) {
      writer.write(nsAlias);
      writer.write(':');
    }
    writer.write(name);
  }

  /**
   * Writes the start of the opening tag of an element.
   *
   * @param nsAlias namespace alias prefix for the element.
   * @param name tag name for the element.
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeOpenTagStart(String nsAlias, String name) throws IOException {

    // if pretty print is enabled, write out the indentation
    if (isPrettyPrintingEnabled()) {
      // add a new line if this is a nested element,
      // or if we've written the header
      if (getIndentationLevel() > 0
          || flags.contains(WriterFlags.WRITE_HEADER)) {
        writeNewline();
      }
      writeIndentation();
    }

    writer.write('<');
    writeQualifiedName(nsAlias, name);
  }

  /**
   * Writes the end of the opening tag of an element after all attributes
   * have been written.
   *
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeOpenTagEnd() throws IOException {
    writer.write('>');
  }

  /**
   * Writes the closing tag of an element, after all nested elements and
   * value text have been written.
   *
   * @param nsAlias namespace alias prefix for the element.
   * @param name tag name for the element.
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeCloseTag(String nsAlias, String name) throws IOException {

    Element element = currentElement();
    if (element.openTagEnded) {

      // if pretty print is enabled and indentation is not disabled,
      // write out the indentation
      if (isPrettyPrintingEnabled()) {
        writeNewline();
        writeIndentation();
      }

      writer.write("</");
      writeQualifiedName(nsAlias, name);
      writer.write(">");
    } else {
      writer.write("/>");
    }
  }

  /**
   * Writes an unqualfied XML attribute.
   *
   * @param name the name of the attribute.
   * @param value the value of the attribute.
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeAttribute(String name, String value) throws IOException {
    writeAttribute(null, name, value);
  }

  /**
   * Writes a namespace-qualified XML attribute.
   *
   * @param nsAlias namespace alias prefix for the attribute.
   * @param name the name of the attribute.
   * @param value the value of the attribute.
   * @throws IOException thrown by the underlying writer.
   */
  protected void writeAttribute(String nsAlias, String name, String value)
      throws IOException {

    writer.write(" ");
    writeQualifiedName(nsAlias, name);
    writer.write('=');
    writer.write('\'');
    if (value != null) {
      writer.write(StringUtil.xmlEscape(value));
    }
    writer.write('\'');
  }

  /**
   * Emits character data subject to XML escaping.
   *
   * @param s string to emit. Can be {@code null}.
   * @throws IOException thrown by the underlying writer.
   */
  public void characters(String s) throws IOException {
    characters(s, false);
  }

  /**
   * Emits character data subject to either XML escaping or CDATA escaping.
   *
   * @param s string to emit. Can be {@code null}.
   * @param useCData CDATA used if true, XML escaping if false
   * @throws IOException thrown by the underlying writer.
   */
  public void characters(String s, boolean useCData) throws IOException {
    // Implementation note: xmlContentEscaper and xmlCDataEscape already
    // filter out illegal control characters, so no need to do so here.
    if (s == null) {
      return;
    }
    endOpenTag();
    currentElement().unformattedChildren = true;
    String escaped;
    if (useCData) {
      escaped = "<![CDATA[" + StringUtil.xmlCDataEscape(s) + "]]>";
    } else {
      escaped = StringUtil.xmlContentEscape(s);
    }
    writer.write(escaped);
  }

  /**
   * Writes inner XML provided as a string. Used to write out XML blobs.
   *
   * @param xml  XML blob string.
   * @throws IOException thrown by the underlying writer.
   */
  public void innerXml(String xml) throws IOException {
    if (xml == null) {
      return;
    }

    writeUnescaped(xml);
  }

  /**
   * Writes a string without XML entity escaping.
   *
   * @param s the raw content to write without escaping.
   * @throws IOException thrown by the underlying writer.
   */
  public void writeUnescaped(String s) throws IOException {
    endOpenTag();
    currentElement().unformattedChildren = true;
    writer.write(s);
  }
}
