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


package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Name-value pair with optional type and unit.
 *
 * 
 */
public class CustomProperty extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<String,
      CustomProperty> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "customProperty"), String.class, CustomProperty.class);

  /**
   * Can be a URI to indicate a specific ontology.
   */
  public static final AttributeKey<String> NAME = AttributeKey.of(new
      QName(null, "name"), String.class);

  /**
   * Datatype such as string, integer, and date, or an ontology-specific URI.
   */
  public static final AttributeKey<String> TYPE = AttributeKey.of(new
      QName(null, "type"), String.class);

  /**
   * Units of data - can also be a URI to indicate a specific ontology.
   */
  public static final AttributeKey<String> UNIT = AttributeKey.of(new
      QName(null, "unit"), String.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(NAME).setRequired(true);
    builder.addAttribute(TYPE);
    builder.addAttribute(UNIT);
  }

  /**
   * Constructs an instance using the default key.
   */
  public CustomProperty() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected CustomProperty(ElementKey<String, ? extends CustomProperty> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected CustomProperty(ElementKey<String, ? extends CustomProperty> key,
      Element source) {
    super(key, source);
  }

  /**
   * Constructs a new instance with the given value.
   *
   * @param value value.
   */
  public CustomProperty(String value) {
    this();
    setValue(value);
  }

  @Override
  public CustomProperty lock() {
    return (CustomProperty) super.lock();
  }

  /**
   * Returns the can be a URI to indicate a specific ontology.
   *
   * @return can be a URI to indicate a specific ontology
   */
  public String getName() {
    return super.getAttributeValue(NAME);
  }

  /**
   * Sets the can be a URI to indicate a specific ontology.
   *
   * @param name can be a URI to indicate a specific ontology or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public CustomProperty setName(String name) {
    super.setAttributeValue(NAME, name);
    return this;
  }

  /**
   * Returns whether it has the can be a URI to indicate a specific ontology.
   *
   * @return whether it has the can be a URI to indicate a specific ontology
   */
  public boolean hasName() {
    return super.hasAttribute(NAME);
  }

  /**
   * Returns the datatype such as string, integer, and date, or an
   * ontology-specific URI.
   *
   * @return datatype such as string, integer, and date, or an ontology-specific
   *     URI
   */
  public String getType() {
    return super.getAttributeValue(TYPE);
  }

  /**
   * Sets the datatype such as string, integer, and date, or an
   * ontology-specific URI.
   *
   * @param type datatype such as string, integer, and date, or an
   *     ontology-specific URI or {@code null} to reset
   * @return this to enable chaining setters
   */
  public CustomProperty setType(String type) {
    super.setAttributeValue(TYPE, type);
    return this;
  }

  /**
   * Returns whether it has the datatype such as string, integer, and date, or
   * an ontology-specific URI.
   *
   * @return whether it has the datatype such as string, integer, and date, or
   *     an ontology-specific URI
   */
  public boolean hasType() {
    return super.hasAttribute(TYPE);
  }

  /**
   * Returns the units of data - can also be a URI to indicate a specific
   * ontology.
   *
   * @return units of data - can also be a URI to indicate a specific ontology
   */
  public String getUnit() {
    return super.getAttributeValue(UNIT);
  }

  /**
   * Sets the units of data - can also be a URI to indicate a specific ontology.
   *
   * @param unit units of data - can also be a URI to indicate a specific
   *     ontology or {@code null} to reset
   * @return this to enable chaining setters
   */
  public CustomProperty setUnit(String unit) {
    super.setAttributeValue(UNIT, unit);
    return this;
  }

  /**
   * Returns whether it has the units of data - can also be a URI to indicate a
   * specific ontology.
   *
   * @return whether it has the units of data - can also be a URI to indicate a
   *     specific ontology
   */
  public boolean hasUnit() {
    return super.hasAttribute(UNIT);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return super.getTextValue(KEY);
  }

  /**
   * Sets the value.
   *
   * @param value value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public CustomProperty setValue(String value) {
    super.setTextValue(value);
    return this;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return super.hasTextValue();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CustomProperty other = (CustomProperty) obj;
    return eq(getName(), other.getName())
        && eq(getType(), other.getType())
        && eq(getUnit(), other.getUnit())
        && eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getName() != null) {
      result = 37 * result + getName().hashCode();
    }
    if (getType() != null) {
      result = 37 * result + getType().hashCode();
    }
    if (getUnit() != null) {
      result = 37 * result + getUnit().hashCode();
    }
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


