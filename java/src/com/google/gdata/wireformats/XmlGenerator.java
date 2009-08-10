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
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.AttributeMetadata;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ElementVisitor;
import com.google.gdata.model.QName;
import com.google.gdata.wireformats.output.OutputProperties;

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
   * {@link #XmlGenerator(OutputProperties, Writer, Charset, boolean, XmlNamespace)}
   * and giving it a different namespace.
   */
  private static final XmlNamespace USE_ROOT_ELEMENT_NAMESPACE =
      new XmlNamespace("__USE_ROOT_ELEMENT_NAMESPACE__");

  /**
   * Metadata for the root element
   */
  protected final ElementMetadata<?, ?> rootMetadata;

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
   * call {@link #XmlGenerator(OutputProperties, Writer, Charset, boolean, XmlNamespace)}
   * with the namespace that should be used as the default.
   */
  public XmlGenerator(StreamProperties props, Writer w, Charset cs,
      boolean prettyPrint) {
    this(props, w, cs, prettyPrint, USE_ROOT_ELEMENT_NAMESPACE);
  }

  /**
   * Creates a new xml generator for generating xml output, using the
   * given namespace as the default namespace.
   */
  public XmlGenerator(StreamProperties props, Writer w, Charset cs,
      boolean prettyPrint, XmlNamespace defaultNamespace) {
    EnumSet<WriterFlags> flags = EnumSet.of(WriterFlags.WRITE_HEADER);
    if (prettyPrint) {
      flags.add(WriterFlags.PRETTY_PRINT);
    }

    try {
      this.xw = new XmlWriter(w, flags, cs.name());
    } catch (IOException ioe) {
      throw new RuntimeException("Unable to create XML generator", ioe);
    }

    this.rootMetadata = props.getRootMetadata();
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
     * @param parent the parent element.
     * @param e the element to start.
     * @param metadata the metadata for the element
     * @return true if child elements should be written, false if the element
     *     was fully written.
     * @throws IOException if an error occurs while writing to the writer.
     */
    public boolean startElement(XmlWriter xw, Element parent, Element e,
        ElementMetadata<?, ?> metadata) throws IOException;

    /**
     * Write the text content for an element.
     */
    public void textContent(XmlWriter xw, Element e,
        ElementMetadata<?, ?> metadata) throws IOException;

    /**
     * End an element, writing a close tag if needed.
     */
    public void endElement(XmlWriter xw, Element e,
        ElementMetadata<?, ?> metadata) throws IOException;
  }

  /**
   * The XmlElementGenerator class provides the default implementation of the
   * {@link ElementGenerator interface}.   It will generate start and end
   * elements based directly on the element metadata, attributes, and value.
   */
  public static class XmlElementGenerator implements ElementGenerator {

    public boolean startElement(XmlWriter xw, Element parent, Element e,
        ElementMetadata<?, ?> metadata) throws IOException {

      Collection<XmlNamespace> namespaces = getNamespaces(parent, e, metadata);
      List<XmlWriter.Attribute> attrs = getAttributes(e, metadata);

      QName name = getName(e, metadata);
      xw.startElement(name.getNs(), name.getLocalName(), attrs, namespaces);
      return true;
    }

    /**
     * Returns the QName of an element, possibly using the given metadata for
     * the name if it is not {@code null}.
     */
    protected QName getName(Element e, ElementMetadata<?, ?> metadata) {
      return (metadata == null) ? e.getElementId() : metadata.getName();
    }

    /**
     * Get a collection of namespaces for the current element and parent.  This
     * will only return namespaces for the root element, because we bubble all
     * namespaces up to the root for wire efficiency.
     */
    protected Collection<XmlNamespace> getNamespaces(
        Element parent, Element e, ElementMetadata<?, ?> metadata) {
      if (parent == null) {
        return GeneratorUtils.calculateNamespaces(e, metadata).values();
      }
      return null;
    }

    /**
     * Get a list of attributes for the given element.
     */
    protected List<XmlWriter.Attribute> getAttributes(Element e,
        ElementMetadata<?, ?> metadata) {
      List<XmlWriter.Attribute> attrs = null;
      Iterator<Attribute> attributeIterator = e.getAttributeIterator(metadata);
      if (attributeIterator.hasNext()) {
        ElementKey<?, ?> key = e.getElementKey();
        attrs = new ArrayList<XmlWriter.Attribute>();
        while (attributeIterator.hasNext()) {
          Attribute attribute = attributeIterator.next();
          AttributeKey<?> attKey = attribute.getAttributeKey();
          AttributeMetadata<?> attMeta = (metadata == null) ? null
              : metadata.bindAttribute(attKey);
          QName qName = attMeta != null ? attMeta.getName() : attKey.getId();
          String alias = (qName.getNs() != null) ?
              qName.getNs().getAlias() : null;
          attrs.add(new XmlWriter.Attribute(alias, qName.getLocalName(),
              attribute.getValue().toString()));
        }
      }
      return attrs;
    }

    public void textContent(XmlWriter xw, Element e,
        ElementMetadata<?, ?> metadata) throws IOException {
      Object value = (metadata == null) ? e.getTextValue()
          : metadata.generateValue(e, metadata);
      if (value != null) {
        String valStr = value.toString();
        if (valStr.length() > 0) {
          xw.characters(valStr);
        }
      }
    }

    public void endElement(XmlWriter xw, Element e,
        ElementMetadata<?, ?> metadata) throws IOException {
      QName name = getName(e, metadata);
      xw.endElement(name.getNs(), name.getLocalName());
    }
  }

  // A singleton default generator that is used in all cases where element
  // generation has not been customized via metadata.
  private static final ElementGenerator DEFAULT_GENERATOR =
      new XmlElementGenerator();

  public void generate(Element element) throws IOException {

    generate(element, rootMetadata);
  }

  public void generate(Element element, ElementMetadata<?, ?> metadata) 
      throws IOException {

    if (metadata != null && 
        !metadata.getKey().equals(element.getElementKey())) {
      throw new IllegalStateException(
          "Element key (" + element.getElementKey() + 
          ") does not match metadata key (" + metadata.getKey() + ")");
    }
    try {
      element.visit(this, metadata);
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
   * @param metadata the element metadata
   * @return the element generator for elements of this type.
   */
  private ElementGenerator getElementGenerator(ElementMetadata<?, ?> metadata) {
    if (metadata != null) {
      XmlWireFormatProperties xmlProperties =
          (XmlWireFormatProperties) metadata.getProperties();
      if (xmlProperties != null) {
        ElementGenerator elementGenerator = xmlProperties.getElementGenerator();
        if (elementGenerator != null) {
          return elementGenerator;
        }
      }
    }
    return DEFAULT_GENERATOR;
  }

  public boolean visit(Element parent, Element e,
      ElementMetadata<?, ?> metadata) throws StoppedException {
    try {
      if (parent == null) {
        setRootNamespace(metadata, e);
      }
      if (metadata == null || metadata.isSelected(e)) {
        ElementGenerator gen = getElementGenerator(metadata);
        return gen.startElement(xw, parent, e, metadata);
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
  private void setRootNamespace(ElementMetadata<?, ?> meta, Element e) {
    XmlNamespace rootNs = defaultNamespace;

    // If no default has been set, we use the namespace of the root element as
    // the default namespace.
    if (rootNs == USE_ROOT_ELEMENT_NAMESPACE) {
      if (meta != null) {
        rootNs = meta.getDefaultNamespace();
      } else {
        rootNs = e.getElementId().getNs();
      }
    }
    if (rootNs != null) {
      xw.setDefaultNamespace(rootNs);
    }
  }

  public void visitComplete(Element parent, Element e,
      ElementMetadata<?, ?> metadata) throws StoppedException {
    try {
      if (metadata == null || metadata.isSelected(e)) {
        ElementGenerator elementGenerator = getElementGenerator(metadata);
        elementGenerator.textContent(xw, e, metadata);
        elementGenerator.endElement(xw, e, metadata);
      }
    } catch (IOException ioe) {
      throw new StoppedException(ioe);
    }
  }
}

