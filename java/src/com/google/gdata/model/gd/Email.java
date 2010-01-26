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
 * Describes an email address.
 *
 * 
 */
public class Email extends Element {

  /** Email type. */
  public static final class Rel {

    /** Home email address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other email address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work email address. */
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
  public static final ElementKey<Void,
      Email> KEY = ElementKey.of(new QName(Namespaces.gNs, "email"), Void.class,
      Email.class);

  /**
   * Email address.
   */
  public static final AttributeKey<String> ADDRESS = AttributeKey.of(new
      QName(null, "address"), String.class);

  /**
   * DisplayName.
   */
  public static final AttributeKey<String> DISPLAY_NAME = AttributeKey.of(new
      QName(null, "displayName"), String.class);

  /**
   * Label.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Whether this is the primary email address.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * Email type.
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
    builder.addAttribute(ADDRESS).setRequired(true);
    builder.addAttribute(DISPLAY_NAME);
    builder.addAttribute(LABEL);
    builder.addAttribute(PRIMARY);
    builder.addAttribute(REL);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Email() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Email(ElementKey<?, ? extends Email> key) {
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
  protected Email(ElementKey<?, ? extends Email> key, Element source) {
    super(key, source);
  }

  @Override
  public Email lock() {
    return (Email) super.lock();
  }

  /**
   * Returns the email address.
   *
   * @return email address
   */
  public String getAddress() {
    return super.getAttributeValue(ADDRESS);
  }

  /**
   * Sets the email address.
   *
   * @param address email address or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Email setAddress(String address) {
    super.setAttributeValue(ADDRESS, address);
    return this;
  }

  /**
   * Returns whether it has the email address.
   *
   * @return whether it has the email address
   */
  public boolean hasAddress() {
    return super.hasAttribute(ADDRESS);
  }

  /**
   * Returns the displayName.
   *
   * @return displayName
   */
  public String getDisplayName() {
    return super.getAttributeValue(DISPLAY_NAME);
  }

  /**
   * Sets the displayName.
   *
   * @param displayName displayName or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Email setDisplayName(String displayName) {
    super.setAttributeValue(DISPLAY_NAME, displayName);
    return this;
  }

  /**
   * Returns whether it has the displayName.
   *
   * @return whether it has the displayName
   */
  public boolean hasDisplayName() {
    return super.hasAttribute(DISPLAY_NAME);
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the label.
   *
   * @param label label or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Email setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the label.
   *
   * @return whether it has the label
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the whether this is the primary email address.
   *
   * @return whether this is the primary email address
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary email address.
   *
   * @param primary whether this is the primary email address or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public Email setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
    return this;
  }

  /**
   * Returns whether it has the whether this is the primary email address.
   *
   * @return whether it has the whether this is the primary email address
   */
  public boolean hasPrimary() {
    return super.hasAttribute(PRIMARY);
  }

  /**
   * Returns the email type.
   *
   * @return email type
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the email type.
   *
   * @param rel email type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Email setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the email type.
   *
   * @return whether it has the email type
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Email other = (Email) obj;
    return eq(getAddress(), other.getAddress())
        && eq(getDisplayName(), other.getDisplayName())
        && eq(getLabel(), other.getLabel())
        && eq(getPrimary(), other.getPrimary())
        && eq(getRel(), other.getRel());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getAddress() != null) {
      result = 37 * result + getAddress().hashCode();
    }
    if (getDisplayName() != null) {
      result = 37 * result + getDisplayName().hashCode();
    }
    if (getLabel() != null) {
      result = 37 * result + getLabel().hashCode();
    }
    if (getPrimary() != null) {
      result = 37 * result + getPrimary().hashCode();
    }
    if (getRel() != null) {
      result = 37 * result + getRel().hashCode();
    }
    return result;
  }
}


