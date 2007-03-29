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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
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

  /**
   * The XML namespace for this extension.
   */
  protected final XmlWriter.Namespace namespace;

  /**
   * The XML local name for this extension.
   */
  protected final String localName;

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
    if (defAnnot == null) {
      String name = extensionClass.getName();
      throw new IllegalArgumentException(
          "No @ExtensionDescription.Default annotation found on subclass "
          + name.substring(name.lastIndexOf('.') + 1));
    }
    this.namespace = new XmlWriter.Namespace(defAnnot.nsAlias(),
        defAnnot.nsUri());
    this.localName = defAnnot.localName();
  }

  /**
   * Constructs an extension bound to a specific XML representation.  Note: this
   * is here for backwards compatibility and may be removed at some point in the
   * future.
   *
   * @param namespace the namespace of the value element
   * @param localName the local name of the value element
   */
  protected AbstractExtension(XmlWriter.Namespace namespace, String localName) {
    this.namespace = namespace;
    this.localName = localName;
  }

  /**
   * Checks the internal state of this extension, including the attribute values
   * to see if there are any problems.  Default implementation does nothing,
   * though generally this is discouraged unless there really are no
   * restrictions.
   *
   * @throws IllegalStateException if any problems are found with the internal
   *                               state of the fields
   */
  protected void validate() {
  }

  /**
   * Puts attributes into the attribute generator.  May also use
   * {@link#setContent(String)} to set the element's text content.
   *
   * @param generator attribute generator
   */
  protected abstract void putAttributes(AttributeGenerator generator);

  /**
   * Consumes attributes from the attribute helper.  May also use
   * {@link#getContent()} to consume the element's text content.
   *
   * @param helper attribute helper
   * @throws ParseException any parsing exception
   */
  protected abstract void consumeAttributes(AttributeHelper helper)
      throws ParseException;

  public void generate(XmlWriter w, ExtensionProfile p)
      throws IOException {
    validate();
    AttributeGenerator generator = new AttributeGenerator();
    putAttributes(generator);
    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();
    for (Map.Entry<String, String> entry : generator.entrySet()) {
      String value = entry.getValue();
      if (value != null) {
        attrs.add(new XmlWriter.Attribute(entry.getKey(), value));
      }
    }
    w.simpleElement(namespace, localName, attrs, generator.getContent());
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, final Attributes attrs)
      throws ParseException, IOException {
    if (immutable) {
      throw new IllegalStateException("Cannot parse into immutable instance");
    }
    return new XmlParser.ElementHandler() {

      public void processEndElement() throws ParseException {
        final AttributeHelper attrsHelper = new AttributeHelper(attrs, value);
        consumeAttributes(attrsHelper);
        attrsHelper.assertAllConsumed();
        try {
          validate();
        } catch (IllegalStateException e) {
          throw new ParseException(e.getMessage(), e);
        }
      }
    };
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
   * Throws an {@link IllegalStateException} if the value is required and it is
   * missing.
   *
   * @param attrName attribute name
   */
  protected static final void throwExceptionForMissingAttribute(
      String attrName) {
      throw new IllegalStateException("Missing attribute: " + attrName);
  }

  /**
   * Returns true if the given object is not null and is the same concrete class
   * as this one.
   */
  protected boolean sameClassAs(Object o) {
    return o != null && getClass().equals(o.getClass());
  }

  /**
   * Returns true if the specified arguments are equal, or both null.
   */
  protected static boolean eq(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }
}
