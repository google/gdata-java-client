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
 * Describes a postal address.
 *
 * 
 */
public class PostalAddress extends Element {

  /** Postal address type. */
  public static final class Rel {

    /** Home address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work address. */
    public static final String WORK = Namespaces.gPrefix + "work";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      HOME,
      OTHER,
      WORK};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<String,
      PostalAddress> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "postalAddress"), String.class, PostalAddress.class);

  /**
   * Simple string value used to name this address.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Whether this is the primary postal address.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * Postal address type.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

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
    builder.addAttribute(LABEL);
    builder.addAttribute(PRIMARY);
    builder.addAttribute(REL);
  }

  /**
   * Constructs an instance using the default key.
   */
  public PostalAddress() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected PostalAddress(ElementKey<String, ? extends PostalAddress> key) {
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
  protected PostalAddress(ElementKey<String, ? extends PostalAddress> key,
      Element source) {
    super(key, source);
  }

  /**
   * Constructs a new instance with the given value.
   *
   * @param value address string.
   */
  public PostalAddress(String value) {
    this();
    setValue(value);
  }

  @Override
  public PostalAddress lock() {
    return (PostalAddress) super.lock();
  }

  /**
   * Returns the simple string value used to name this address.
   *
   * @return simple string value used to name this address
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the simple string value used to name this address.
   *
   * @param label simple string value used to name this address or {@code null}
   *     to reset
   * @return this to enable chaining setters
   */
  public PostalAddress setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the simple string value used to name this address.
   *
   * @return whether it has the simple string value used to name this address
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the whether this is the primary postal address.
   *
   * @return whether this is the primary postal address
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary postal address.
   *
   * @param primary whether this is the primary postal address or {@code null}
   *     to reset
   * @return this to enable chaining setters
   */
  public PostalAddress setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
    return this;
  }

  /**
   * Returns whether it has the whether this is the primary postal address.
   *
   * @return whether it has the whether this is the primary postal address
   */
  public boolean hasPrimary() {
    return super.hasAttribute(PRIMARY);
  }

  /**
   * Returns the postal address type.
   *
   * @return postal address type
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the postal address type.
   *
   * @param rel postal address type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public PostalAddress setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the postal address type.
   *
   * @return whether it has the postal address type
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the address string.
   *
   * @return address string
   */
  public String getValue() {
    return super.getTextValue(KEY);
  }

  /**
   * Sets the address string.
   *
   * @param value address string or {@code null} to reset
   * @return this to enable chaining setters
   */
  public PostalAddress setValue(String value) {
    super.setTextValue(value);
    return this;
  }

  /**
   * Returns whether it has the address string.
   *
   * @return whether it has the address string
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
    PostalAddress other = (PostalAddress) obj;
    return eq(getLabel(), other.getLabel())
        && eq(getPrimary(), other.getPrimary())
        && eq(getRel(), other.getRel())
        && eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getLabel() != null) {
      result = 37 * result + getLabel().hashCode();
    }
    if (getPrimary() != null) {
      result = 37 * result + getPrimary().hashCode();
    }
    if (getRel() != null) {
      result = 37 * result + getRel().hashCode();
    }
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


