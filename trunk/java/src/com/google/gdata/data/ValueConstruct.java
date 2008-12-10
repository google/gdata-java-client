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


/**
 * The ValueConstruct class is an abstract Extension class that can
 * be used to subclassed to create a GData extension element with a
 * single value, like:
 * <code>
 *   <foo:bar value="some value"/>
 * </code>
 * or
 * <code>
 *   <foo:baz>some value</foo:baz>
 * </code>
 * Using constructor parameters, a customized subclass has full
 * control over the value construct element namespace and tag name,
 * as well whether the value is contained within an XML attribute
 * or the element text value.
 * <p>
 * A subclass can override the {@link #setValue(String)}
 * method to do customized validation of any value set directly
 * by a client or as a result of XML parsing.
 * <p>
 * Two ValueConstruct instances are considered equal if they have the same
 * concrete subclass and the value of the two instances are equal.  The
 * namespace, tagname, and attribute names <b>are not</b> taken into account
 * by the equality comparison; they are assumed to be equivalent for all
 * instances of a particular concrete subclass.
 *
 * 
 * 
 */
public abstract class ValueConstruct extends AbstractExtension {

  /**
   * The XML attribute name for the value construct.  When {@code null},
   * indicates that the value is contained within the XML element text
   * content.
   */
  protected final String attrName;

  /**
   * The value of the value construct.
   */
  private String value;

  /**
   * Indicates that the value is required ({@code true} by default).
   */
  private boolean required = true;
  public final boolean isRequired() { return required; }
  protected final void setRequired(boolean isRequired) {
    this.required = isRequired;
  }

  /**
   * Constructs a value construct bound to a specific XML representation.  The
   * concrete subclass must have an {@link ExtensionDescription.Default}
   * attribute defined, and should always use constant values for the {@code
   * attrName} parameter.
   *
   * @param attrName the name of attribute that contains the value, or {@code
   *                 null} if the value is contained within the element
   *                 content.
   */
  protected ValueConstruct(String attrName) {
    super();
    this.attrName = attrName;
  }

  /**
   * Constructs an ValueConstruct bound to a specific XML representation
   * A concrete subclass should always use constants value for all three
   * parameters.
   *
   * @param namespace the namespace of the value element.
   * @param localName the local name of the value element.
   * @param attrName the name of attribute that contains the value, or
   *        {@code null} if the value is contained within the element content.
   */
  protected ValueConstruct(XmlNamespace namespace,
                           String localName,
                           String attrName) {
    this(namespace, localName, attrName, null);
  }

  /**
   * Constructs a new ValueConstruct instance bound to a specific XML
   * representation.  A concrete subclass should always use constant values
   * for the {@code namespace}, {@code localName}, and {@code attrName}
   * parameters. If an initial value is provided and it is not {@code null},
   * an immutable instance will be created that is initialized to this
   * value and may not be modified by {@link #setValue(String)}.
   * 
   * @param namespace the namespace of the value element.
   * @param localName the local name of the value element.
   * @param attrName the name of attribute that contains the value, or
   *        {@code null} if the value is contained within the element content.
   * @param value the value that should be used to initialize the value
   *        construct instance.   After construction, the value will be
   *        immutable.
   */
  protected ValueConstruct(XmlNamespace namespace,
                           String localName,
                           String attrName,
                           String value) {
    super(namespace, localName);
    this.attrName = attrName;
    if (value != null) {
      setValue(value);
      setImmutable(true);
    }
  }

  /**
   * Returns the value of the value construct.
   * @return the current value.
   */
  public String getValue() { return value; }

  /**
   * Sets the value.  Subclasses can override this method to do
   * additional validation of the value.
   *
   * @param v new value for the value construct or <code>null</code> to reset.
   * @throws IllegalArgumentException if the value is invalid for the construct.
   * @throws IllegalStateException if the value construct is read only
   */
  public void setValue(String v) {
    throwExceptionIfImmutable();
    value = v;  }


  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return value != null;
  }

  @Override
  public void putAttributes(AttributeGenerator generator) {
    if (attrName != null) {
      generator.put(attrName, value);
    } else {
      generator.setContent(value);
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    String actualValue;
    if (attrName != null) {
      actualValue = helper.consume(this.attrName, isRequired());
    } else {
      actualValue = helper.consumeContent(isRequired());
    }
    try {
      setValue(actualValue);
    } catch (IllegalArgumentException iae) {
      throw new ParseException(iae.getMessage(), iae);
    }
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile p)
      throws IOException {
    // for backwards compatibility don't generate if no value
    if (value != null) {
      super.generate(w, p);
    }
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, Attributes attrs)
      throws ParseException {
    // for backwards compatibility set value to null first
    value = null;
    return super.getHandler(p, namespace, localName, attrs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!sameClassAs(o)) {
      return false;
    }
    ValueConstruct vc = (ValueConstruct) o;
    return eq(value, vc.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }
}
