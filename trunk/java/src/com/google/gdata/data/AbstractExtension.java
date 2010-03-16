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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Common extension implementation for sharing code among implementers of
 * {@link Extension}.  The information contained in this class is:
 * <ol>
 * <li>XML namespace of the extension</li>
 * <li>XML local name of the extension</li>
 * <li>if the extension is mutable</li>
 * </ol>
 *
 * 
 */
public abstract class AbstractExtension implements Extension {

  /** XML namespace for this extension or <code>null</code> if not defined */
  protected final XmlNamespace namespace;

  /** XML local name for this extension or <code>null</code> if not defined */
  protected final String localName;

  /** Indicates if strict XML parsing/generation validation enabled/disabled */
  private static ThreadLocal<Boolean> strictValidation =
      new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() {
          return Boolean.TRUE;
        }
      };
  public static final boolean isStrictValidation() {
    return strictValidation.get();
  }
  public static final void enableStrictValidation() {
    strictValidation.set(Boolean.TRUE);
  }
  public static final void disableStrictValidation() {
    strictValidation.set(Boolean.FALSE);
  }

  /**
   * Indicates that the extension is constant after construction ({@code false}
   * by default).
   */
  private boolean immutable;
  public final boolean isImmutable() { return immutable; }
  public final void setImmutable(boolean isImmutable) {
    this.immutable = isImmutable;
  }

  /**
   * Constructs an extension bound to a specific XML representation.  The
   * concrete subclass must have an {@link ExtensionDescription.Default}
   * attribute defined to use this constructor.
   */
  protected AbstractExtension() {
    Class<? extends AbstractExtension> extensionClass = this.getClass();
    ExtensionDescription.Default defAnnot = extensionClass
        .getAnnotation(ExtensionDescription.Default.class);
    if (defAnnot != null) {
      this.namespace = new XmlNamespace(defAnnot.nsAlias(),
          defAnnot.nsUri());
      this.localName = defAnnot.localName();
    } else {
      this.namespace = null;
      this.localName = null;
    }
  }

  /**
   * Constructs an extension bound to a specific XML representation.  Note: this
   * is here for backwards compatibility and may be removed at some point in the
   * future.
   *
   * @param namespace the namespace of the value element
   * @param localName the local name of the value element
   */
  protected AbstractExtension(XmlNamespace namespace, String localName) {
    this.namespace = namespace;
    this.localName = localName;
  }

  /**
   * Gets the extension's namespace.
   */
  public final XmlNamespace getExtensionNamespace() {
    return namespace;
  }

  /**
   * Gets the extension's localname.
   */
  public final String getExtensionLocalName() {
    return localName;
  }

  /**
   * Checks the attributes to see if there are any problems.  Default
   * implementation does nothing, though generally this is discouraged unless
   * there really are no restrictions.
   *
   * @throws IllegalStateException if any problems are found with the
   *                                       attributes
   */
  protected void validate() throws IllegalStateException {
  }

  /**
   * Puts attributes into the attribute generator.  Called from
   * {@link #generate(XmlWriter,ExtensionProfile)}.  Default implementation
   * does nothing, though generally this is discouraged unless there really are
   * no attributes.
   *
   * @param generator attribute generator
   */
  protected void putAttributes(AttributeGenerator generator) {
  }

  /**
   * Consumes attributes from the attribute helper.  May also use
   * {@link AttributeHelper#consumeContent} to consume the element's text
   * content.  Called from {@link #getHandler}.  Default implementation does
   * nothing, though generally this is discouraged unless there really are no
   * attributes.
   *
   * @param helper attribute helper
   * @throws ParseException any parsing exception
   */
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
  }

  /**
   * Generates the XML into the XML writer.  Default implementation generates a
   * "simple" element with the attributes and content found in the attribute
   * generator.
   *
   * @param w         XML writer
   * @param p         extension profile
   * @param namespace XML namespace for this extension
   * @param localName XML local name for this extension
   * @param attrs     list of XML attributes
   * @param generator attribute generator
   * @throws IOException any I/O exception
   */
  protected void generate(XmlWriter w, ExtensionProfile p,
      XmlNamespace namespace, String localName,
      List<XmlWriter.Attribute> attrs, AttributeGenerator generator)
      throws IOException {
    w.simpleElement(namespace, localName, attrs, generator.getContent());
  }

  public void generate(XmlWriter w, ExtensionProfile p)
      throws IOException {

    // validate
    if (namespace == null) {
      String name = this.getClass().getName();
      throw new IllegalStateException(
          "No @ExtensionDescription.Default annotation found on subclass "
              + name.substring(name.lastIndexOf('.') + 1));
    }

    if (isStrictValidation()) {
      validate();
    }

    // generate attributes
    AttributeGenerator generator = new AttributeGenerator();
    putAttributes(generator);
    
    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();
    generateAttributes(attrs, generator);

    // generate XML
    generate(w, p, namespace, localName, attrs, generator);
  }
  
  /**
   * Generates the attributes in the generator into the list of attributes.
   */
  protected void generateAttributes(List<XmlWriter.Attribute> attrs,
      AttributeGenerator generator) {
    for (Map.Entry<String, String> entry : generator.entrySet()) {
      String value = entry.getValue();
      if (value != null) {
        attrs.add(new XmlWriter.Attribute(entry.getKey(), value));
      }
    }
  }

  /**
   * The default implementation uses the {@link AttributesHandler} to handle
   * parsing the extension.
   * 
   * @throws ParseException when an unexpected tag or badly-formatted
   *     XML is detected
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, Attributes attrs)
      throws ParseException {
    return new AttributesHandler(attrs);
  }

  /**
   * Base class for custom element handlers that uses {@link AttributeHelper}
   * to consume the attributes and the element's text content.
   *
   * 
   */
  protected class AttributesHandler extends XmlParser.ElementHandler {

    /** attribute helper or <code>null</code> to suppress its use */
    private final AttributeHelper helper;

    /**
     * Constructor.
     *
     * @param attrs XML attributes or <code>null</code> to suppress the use of
     *              {@link AttributeHelper}
     */
    public AttributesHandler(Attributes attrs) {
      helper = attrs == null ? null : new AttributeHelper(attrs);
      if (immutable) {
        throw new IllegalStateException("Cannot parse into immutable instance");
      }
    }

    @Override
    public void processEndElement() throws ParseException {
      /* don't call super.processEndElement() because it doesn't allow text()
      data */

      // consume attributes
      if (helper != null) {
        helper.setContent(value);
        consumeAttributes(helper);
      }

      if (isStrictValidation()) {
        // validate
        try {
          validate();
        } catch (IllegalStateException e) {
          throw new ParseException(e.getMessage(), e);
        }
      }
    }
  }

  /**
  * Throws an {@link IllegalStateException} if this instance is immutable.
  * Should only be used in a value-setter method.
  */
  protected final void throwExceptionIfImmutable() {
    if (immutable) {
      throw new IllegalStateException(localName + " instance is read only");
    }
  }

  /**
   * Throws an {@link IllegalStateException} if the value is required
   * and it is missing.
   *
   * @param attrName attribute name
   * @throws IllegalStateException to indicate that there are problems with the
   *                                       attributes
   */
  protected static final void throwExceptionForMissingAttribute(
      String attrName) {
    throw new IllegalStateException("Missing attribute: " + attrName);
  }

  /**
   * @param o given object
   * @return true if the given object is not null and is the same concrete class
   *         as this one
   */
  protected boolean sameClassAs(Object o) {
    return o != null && getClass().equals(o.getClass());
  }

  /**
   * @param o1 object 1 or <code>null</code>
   * @param o2 object 2 or <code>null</code>
   * @return true if the specified arguments are equal, or both null
   */
  protected static boolean eq(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }
}
