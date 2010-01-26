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
 * Stores a limited amount of custom data as an auxiliary property of the
 * enclosing entity.
 *
 * 
 */
public class ExtendedProperty extends Element {

  /** Limit on who may see and modify this extended property. */
  public static final class Realm {

    /** Shared extended property. */
    public static final String SHARED = Namespaces.gPrefix + "shared";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      SHARED};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Realm() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      ExtendedProperty> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "extendedProperty"), Void.class, ExtendedProperty.class);

  /**
   * Name of the property expressed as a URI.
   */
  public static final AttributeKey<String> NAME = AttributeKey.of(new
      QName(null, "name"), String.class);

  /**
   * Limit on who may see and modify this extended property.
   */
  public static final AttributeKey<String> REALM = AttributeKey.of(new
      QName(null, "realm"), String.class);

  /**
   * Property value.
   */
  public static final AttributeKey<String> VALUE = AttributeKey.of(new
      QName(null, "value"), String.class);

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
    builder.addAttribute(REALM);
    builder.addAttribute(VALUE);
  }

  /**
   * Constructs an instance using the default key.
   */
  public ExtendedProperty() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected ExtendedProperty(ElementKey<?, ? extends ExtendedProperty> key) {
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
  protected ExtendedProperty(ElementKey<?, ? extends ExtendedProperty> key,
      Element source) {
    super(key, source);
  }

  @Override
  public ExtendedProperty lock() {
    return (ExtendedProperty) super.lock();
  }

  /**
   * Returns the name of the property expressed as a URI.
   *
   * @return name of the property expressed as a URI
   */
  public String getName() {
    return super.getAttributeValue(NAME);
  }

  /**
   * Sets the name of the property expressed as a URI.
   *
   * @param name name of the property expressed as a URI or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public ExtendedProperty setName(String name) {
    super.setAttributeValue(NAME, name);
    return this;
  }

  /**
   * Returns whether it has the name of the property expressed as a URI.
   *
   * @return whether it has the name of the property expressed as a URI
   */
  public boolean hasName() {
    return super.hasAttribute(NAME);
  }

  /**
   * Returns the limit on who may see and modify this extended property.
   *
   * @return limit on who may see and modify this extended property
   */
  public String getRealm() {
    return super.getAttributeValue(REALM);
  }

  /**
   * Sets the limit on who may see and modify this extended property.
   *
   * @param realm limit on who may see and modify this extended property or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public ExtendedProperty setRealm(String realm) {
    super.setAttributeValue(REALM, realm);
    return this;
  }

  /**
   * Returns whether it has the limit on who may see and modify this extended
   * property.
   *
   * @return whether it has the limit on who may see and modify this extended
   *     property
   */
  public boolean hasRealm() {
    return super.hasAttribute(REALM);
  }

  /**
   * Returns the property value.
   *
   * @return property value
   */
  public String getValue() {
    return super.getAttributeValue(VALUE);
  }

  /**
   * Sets the property value.
   *
   * @param value property value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public ExtendedProperty setValue(String value) {
    super.setAttributeValue(VALUE, value);
    return this;
  }

  /**
   * Returns whether it has the property value.
   *
   * @return whether it has the property value
   */
  public boolean hasValue() {
    return super.hasAttribute(VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    ExtendedProperty other = (ExtendedProperty) obj;
    return eq(getName(), other.getName())
        && eq(getRealm(), other.getRealm())
        && eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getName() != null) {
      result = 37 * result + getName().hashCode();
    }
    if (getRealm() != null) {
      result = 37 * result + getRealm().hashCode();
    }
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


