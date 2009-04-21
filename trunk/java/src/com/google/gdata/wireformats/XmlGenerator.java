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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.WriterFlags;
import com.google.gdata.model.Attribute;
import com.google.gdata.model.AttributeMetadata;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ElementVisitor;
import com.google.gdata.model.QName;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * XML generator that outputs a tree of {@link Element} objects
 * using the XML wire format.
 */
public class XmlGenerator implements WireFormatGenerator, ElementVisitor {

  /**
   * A namespace marker that means we should use the root element namespace
   * as the default namespace.  This can be overridden by calling
   * {@link #XmlGenerator(Writer, Charset, boolean, XmlNamespace)} and giving
   * it a different namespace.
   */
  private static final XmlNamespace USE_ROOT_ELEMENT_NAMESPACE =
      new XmlNamespace("__USE_ROOT_ELEMENT_NAMESPACE__");
  
  /**
   * XML writer used by this generator.
   */
  protected final XmlWriter xw;
  
  /**
   * The default namespace to use for this generator.
   */
  private final XmlNamespace defaultNamespace;
  
  /**
   * Creates a new xml generator for generating xml output.  This constructor
   * will use the namespace of the root element as the default namespace of the
   * output.  Callers that would like to change the default namespace should
   * call {@link #XmlGenerator(Writer, Charset, boolean, XmlNamespace)} with
   * the namespace that should be used as the default.
   */
  public XmlGenerator(Writer w, Charset cs, boolean prettyPrint) {
    this(w, cs, prettyPrint, USE_ROOT_ELEMENT_NAMESPACE);
  }
  
  /**
   * Creates a new xml generator for generating xml output, using the
   * given namespace as the default namespace.
   */
  public XmlGenerator(Writer w, Charset cs, boolean prettyPrint,
      XmlNamespace defaultNamespace) {
    EnumSet<WriterFlags> flags = EnumSet.of(WriterFlags.WRITE_HEADER);
    if (prettyPrint) {
      flags.add(WriterFlags.PRETTY_PRINT);
    }
    
    try {
      this.xw = new XmlWriter(w, flags, cs.name());
    } catch (IOException ioe) {
      throw new RuntimeException("Unable to create XML generator", ioe);
    }
    
    this.defaultNamespace = defaultNamespace;
  }
  
  /**
   * The ElementGenerator interface is implemented by helper classes that
   * will generate the start element, text content, and end element syntax
   * for an {@link Element} to an {@link XmlWriter}.
   * <p>
   * The generic implementation of this interface is provided by the
   * {@link XmlElementGenerator} class, but it may be overridden by element
   * types that want to take more direct control over XML output for a given
   * element.
   */
  public interface ElementGenerator {
    
    /**
     * Start an element.  If an ElementGenerator instances writes a full
     * element tag, it should return {@code false} to indicate that textContent
     * and child elements should not be added.
     * 
     * @param xw the xml writer to write to.
     * @param parent the parent element, or null if this is a root element.
     * @param e the element to start.
     * @return true if child elements should be written, false if the element
     *     was fully written.
     * @throws IOException if an error occurs while writing to the writer.
     */
    public boolean startElement(XmlWriter xw, Element parent, Element e)
        throws IOException;
    
    /**
     * Write the text content for an element.
     */
    public void textContent(XmlWriter xw, Element e) throws IOException;
    
    /**
     * End an element, writing a close tag if needed.
     */
    public void endElement(XmlWriter xw, Element e) throws IOException;
  }
  
  /**
   * The XmlElementGenerator class provides the default implementation of the
   * {@link ElementGenerator interface}.   It will generate start and end
   * elements based directly on the element metadata, attributes, and value.
   */
  public static class XmlElementGenerator implements ElementGenerator {

    public boolean startElement(XmlWriter xw, Element parent, Element e)
        throws IOException {

      Collection<XmlNamespace> namespaces = getNamespaces(parent, e);
      List<XmlWriter.Attribute> attrs = getAttributes(e);
      
      ElementMetadata<?, ?> meta = e.getMetadata();
      xw.startElement(meta.getName().getNs(), meta.getName().getLocalName(),
          attrs, namespaces);
      return true;
    }
    
    /**
     * Get a collection of namespaces for the current element and parent.
     */
    protected Collection<XmlNamespace> getNamespaces(Element parent,
        Element e) {
      if (parent == null) {
        return GeneratorUtils.calculateNamespaces(e).values();
      }
      return null;
    }
    
    /**
     * Get a list of attributes for the given element.
     */
    protected List<XmlWriter.Attribute> getAttributes(Element e) {
      List<XmlWriter.Attribute> attrs = null;
      Iterator<Attribute> attributeIterator = e.getAttributeIterator();
      if (attributeIterator.hasNext()) {
        attrs = new ArrayList<XmlWriter.Attribute>();
        while (attributeIterator.hasNext()) {
          Attribute attribute = attributeIterator.next();
          AttributeMetadata<?> attMeta = attribute.getMetadata();
          if (!attMeta.isVisible()) {
            continue;
          }
          QName qName = attMeta.getName();
          String alias = (qName.getNs() != null) ? 
              qName.getNs().getAlias() : null;
          attrs.add(new XmlWriter.Attribute(alias, qName.getLocalName(), 
              attribute.getValue().toString()));
        }
      }
      return attrs;
    }

    public void textContent(XmlWriter xw, Element e) throws IOException {
      ElementMetadata<?, ?> meta = e.getMetadata();
      String valueString = meta.getValueAsString(e);
      if (valueString != null && valueString.length() > 0) {
        xw.characters(valueString);
      }
    }
    
    public void endElement(XmlWriter xw, Element e) throws IOException {
      xw.endElement(e.getMetadata().getName().getNs(),
                    e.getMetadata().getName().getLocalName());
    }
  }
  
  // A singleton default generator that is used in all cases where element
  // generation has not been customized via metadata.
  private static final ElementGenerator DEFAULT_GENERATOR =
      new XmlElementGenerator();

  public void generate(Element element) throws IOException {
    try {
      element.visit(this, null);
    } catch (StoppedException se) {
      Throwable cause = se.getCause();
      if (cause instanceof IOException) {
        throw (IOException) cause;
      }
      throw se;  // unexpected
    }
  }
  
  /**
   * Returns the {@link ElementGenerator} that should be used to generator
   * the specified element.   The method will return the custom generator
   * configured in the {@link XmlWireFormatProperties} of element metadata, or
   * the default generator if none has been configured.
   * 
   * @param e the element.
   * @return the element generator for elements of this type.
   */
  private ElementGenerator getElementGenerator(Element e) {
    XmlWireFormatProperties xmlProperties = (XmlWireFormatProperties)
        e.getMetadata().getProperties();
    if (xmlProperties != null) {
      ElementGenerator elementGenerator = xmlProperties.getElementGenerator();
      if (elementGenerator != null) {
        return elementGenerator;
      }
    }
    return DEFAULT_GENERATOR;
  }

  public boolean visit(Element parent, Element e) throws StoppedException {
    try {
      if (parent == null) {
        setRootNamespace(e);
      }
      if (e.getMetadata().isVisible()) {
        return getElementGenerator(e).startElement(xw, parent, e);
      }
    } catch (IOException ioe) {
      throw new StoppedException(ioe);
    }
    return false;
  }
  
  /**
   * Sets the root element for generation.  This is used to derive the default
   * metadata that should be used.
   */
  private void setRootNamespace(Element element) {
    XmlNamespace rootNs = defaultNamespace;
    
    // If no default has been set, we use the namespace of the root element as
    // the default namespace.
    if (rootNs == USE_ROOT_ELEMENT_NAMESPACE) {
      rootNs = element.getMetadata().getName().getNs();
    }
    if (rootNs != null) {
      xw.setDefaultNamespace(rootNs);
    }
  }
  
  public void visitComplete(Element e) throws StoppedException {
    try {
      if (e.getMetadata().isVisible()) {
        ElementGenerator elementGenerator = getElementGenerator(e);
        elementGenerator.textContent(xw, e);
        elementGenerator.endElement(xw, e);
      }
    } catch (IOException ioe) {
      throw new StoppedException(ioe);
    }
  }
}
