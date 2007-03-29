/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.util.common.base.StringUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;


/**
 * Implements a simple XML writer on top of java.io.PrintWriter.
 * This implementation can be conveniently used to generate XML responses in
 * servlets.
 *
 * The XmlWriter class exposes a number of protected methods that enable it
 * to be subclassed for the purposes of customizing its output.  See
 * {@link com.google.javascript.util.JsonWriter} for an example.
 *
 *
 *
 */
public class XmlWriter {

  /**
   * The Namespace class represents an XML Namespace that can be associated
   * with elements or attributes.
   */
  public final static class Namespace {

    String alias;
    final String uri;

    /** Initializes a namespace. */
    public Namespace(String alias, String uri) {
      this.alias = alias;
      this.uri = uri;
    }

    /**
     * Returns the prefix alias for the namespace.
     * @returns namespace alias.
     */
    final public String getAlias() { return alias; }

    /**
     * Returns the fully qualified URI for the namespace.
     * @returns namespace URI.
     */
    final public String getUri() { return uri; }

    @Override
    public boolean equals(Object obj) {
      if (! (obj instanceof Namespace))
        return false;
      Namespace other = (Namespace) obj;
      if (alias == null) {
        return (other.alias == null) && uri.equals(other.uri);
      } else {
        return alias.equals(other.alias) && uri.equals(other.uri);
      }
    }

    @Override
    public int hashCode() {
      if (alias == null) {
        return uri.hashCode();
      } else {
        return alias.hashCode() & uri.hashCode();
      }
    }
  }

  /**
   * The Attribute class represents an XML attribute.
   */
  public final static class Attribute {

    final String nsAlias;
    final String name;
    final String value;

    /**
     * Constructs an unqualified XML attribute.
     *
     * @param the attribute name.
     * @param the attribute value.
     */
    public Attribute(String name, String value) {
      this(null, name, value);
    }

    /**
     * Constructs an namespace-qualified XML attribute.
     *
     * @param the attribute namespace prefix alias.
     * @param the attribute name.
     * @param the attribute value.
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
          name = name.substring(separator+1);
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
   * track of namespace alias/URI mappings.  This class may be subclassed
   * by XmlWriter subclasses that want to track additional information about
   * output elements.  The {@link #createElement} method can be overriden
   * to instantiate a specialized Element subclass.
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
    public List<Namespace> nsDecls;

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
     * Indicates the number of repeating child elements written within
     * the scope of the current {@link #startRepeatingElement} /
     * {@link endRepeatingElement} pair for this element.  The value will be
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
     * Constructs an element. This constructor should never be directly
     * called, always use the {@link #createElement} method to create new
     * elements.
     *
     * @see #createElement
     */
    protected Element(String nsAlias, String nsUri, String name) {
      nsDecls = new ArrayList<Namespace>();
      this.nsAlias = nsAlias;
      this.nsUri = nsUri;
      this.name = name;
    }

    /**
     * Adds a namespace declaration to the element, avoiding duplicates.
     */
    void addNamespace(Namespace ns) {
      if(!nsDecls.contains(ns)) {
        nsDecls.add(ns);
      }
    }
  }

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
   * The default namespace that will take effect on the next
   * element transition.
   */
  private String nextDefaultNamespace = null;

  /**
   * Constructs an XmlWriter instance associated that will generate
   * XML content to an underlying {@link Writer}.
   *
   * @param   w output writer object.
   * @throws  IOException thrown by the underlying writer.
   */
  public XmlWriter(Writer w) throws IOException {

    writer = w;

    /*
     * Create the element stack and push an initial dummy element on it.
     * This enables parent checking and other basic functionality for
     * the root element without requiring special case handling of an
     * empty element stack.
     */
    elementStack = new Stack<Element>();
    elementStack.push(createElement(null, null, null));
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
    this(w);
    writeHeader(encoding);
  }


  // Included for back compat only, deprecated.
  public XmlWriter(Writer w, boolean includeHeader) throws IOException {
    this(w);
    if (includeHeader) {
      writeHeader(null);
    }
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
   * Sets the default namespace. It takes effect on the next element.
   *
   * @param namespace the new namespace to set as the default at the start
   *                  of the next element.
   */
  public void setDefaultNamespace(Namespace namespace) {
    if (!namespace.uri.equals(defaultNamespace)) {
      nextDefaultNamespace = namespace.uri;
      defaultNamespace = namespace.uri;
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
  public void startElement(Namespace namespace,
                           String name,
                           Collection<Attribute> attrs,
                           Collection<Namespace> namespaceDecls)
      throws IOException {

    Element element;
    if (namespace != null) {
      element = createElement(namespace.alias, namespace.uri, name);
    } else {
      element = createElement(null, null, name);
    }

    Element parentElement = currentElement();
    if (parentElement != null) {
      element.xmlLang = parentElement.xmlLang;

      if (parentElement.repeatingCount != Element.NOT_REPEATING) {
        element.repeatingIndex = parentElement.repeatingCount++;
      }
    }

    elementStack.push(element);

    if (nextDefaultNamespace != null) {
      Namespace defaultNs = new Namespace(null, nextDefaultNamespace);
      element.addNamespace(defaultNs);
      nextDefaultNamespace = null;
    }

    if (namespaceDecls != null) {
      for (Namespace ns: namespaceDecls) {
        ensureNamespace(ns);
      }
    }

    if (namespace != null) {
      element.nsAlias = ensureNamespace(namespace);
    }

    writeOpenTagStart(element.nsAlias, name);

    for (Namespace ns: element.nsDecls) {
      if (ns.alias != null && ns.alias.length() > 0) {
        writeAttribute("xmlns", ns.alias, ns.uri);  // xmlns:name=uri
      } else {
        writeAttribute(null, "xmlns", ns.uri);    // xmlns=uri
      }
    }

    if (attrs != null) {

      for (Attribute attr: attrs) {

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

    writeOpenTagEnd();
  }

  /**
   * Ends the current element. It is expected to be on top of the stack.
   *
   * @param   namespace element namespace.
   * @param   name element name.
   */
  public void endElement(Namespace namespace, String name) throws IOException {
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
    elementStack.pop();

    // Write the footer if we're down to just the dummy element.
    if (elementStack.size() == 1) {
      writeFooter();
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
  public void simpleElement(Namespace namespace,
                            String name,
                            List<Attribute> attrs,
                            String value) throws IOException {

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

      for (Namespace ns: element.nsDecls) {
        if (ns.alias != null && ns.uri.equals(namespaceUri)) {
          return ns.alias;
        }
      }
    }
    return null;
  }

  /**
   * Ensures the namespace is in scope and returns its alias.
   * The top of the stack is assumed to be the current element.
   * If the namespace is not found, it is appended to the current element.
   *
   * @return  namespace alias or {@code null} for the default namespace.
   */
  private String ensureNamespace(Namespace namespace) {

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
      ensureUniqueNamespaceAlias(current, namespace);
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
  private void ensureUniqueNamespaceAlias(Element element,
                                          Namespace namespace) {
    boolean unique;
    int serial = 0;

    do {
      unique = true;
      for (Namespace ns: element.nsDecls) {
        if (namespace.alias.equals(ns.alias)) {
          unique = false;
          namespace.alias = "ns" + String.valueOf(++serial);
          break;
        }
      }
    } while (!unique);
  }

  /**
   * Writes basic XML headers including XML version and encoding.
   * @param encoding the XML encoding for the output.  Can be {@code null}.
   * @throws  IOException thrown by the underlying writer.
   */
  protected void writeHeader(String encoding) throws IOException {
    writer.write("<?xml");
    writeAttribute("version", "1.0");
    if (encoding != null) {
      writeAttribute("encoding", encoding);
    }
    writer.write("?>");
  }

  /**
   * Writes any closing information to the output writer.  This is used by
   * subclasses to write anything they need to close out the object.
   * {@link #close()} should not be used because it closes the underlying
   * stream, which we don't always want to do.
   *
   * @throws IOException thrown by the underlying writer.
   */
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
  protected void writeOpenTagStart(String nsAlias, String name)
      throws IOException {

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
  protected void writeCloseTag(String nsAlias, String name)
      throws IOException {
    writer.write("</");
    writeQualifiedName(nsAlias, name);
    writer.write(">");
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

    if (s == null) {
      return;
    }

    writer.write(StringUtil.xmlContentEscape(s));
  }

  /**
   * Writes inner XML provided as a string. Used to write out XML blobs.
   *
   * @param xml  XML blob string.
   * @throws IOException thrown by the underlying writer.
   */
  public void innerXml(String xml) throws IOException {
    if (xml != null) {
      writer.write(xml);
    }
  }

  /**
   * Writes a string without XML entity escaping. Used by
   * {@link com.google.gdata.util.XmlParser} to generate XML blobs.
   *
   * @param s the raw content to write without escaping.
   * @throws IOException thrown by the underlying writer.
   */
  public void writeUnescaped(String s) throws IOException {
    writer.write(s);
  }
}
