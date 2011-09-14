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
 * Describes a phone number.
 *
 * 
 */
public class PhoneNumber extends Element {

  /** Programmatic value that identifies the type of phone number. */
  public static final class Rel {

    /** Assistant's phone number. */
    public static final String ASSISTANT = Namespaces.gPrefix + "assistant";

    /** Callback number. */
    public static final String CALLBACK = Namespaces.gPrefix + "callback";

    /** Car phone number. */
    public static final String CAR = Namespaces.gPrefix + "car";

    /** Company main number. */
    public static final String COMPANY_MAIN = Namespaces.gPrefix +
        "company_main";

    /** Fax number. */
    public static final String FAX = Namespaces.gPrefix + "fax";

    /** Home phone number. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Home fax number. */
    public static final String HOME_FAX = Namespaces.gPrefix + "home_fax";

    /** ISDN number. */
    public static final String ISDN = Namespaces.gPrefix + "isdn";

    /** Main number. */
    public static final String MAIN = Namespaces.gPrefix + "main";

    /** Cell phone number. */
    public static final String MOBILE = Namespaces.gPrefix + "mobile";

    /** Special type of number for which no other rel value makes sense. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Other fax number. */
    public static final String OTHER_FAX = Namespaces.gPrefix + "other_fax";

    /** Pager number. */
    public static final String PAGER = Namespaces.gPrefix + "pager";

    /** Radio phone number. */
    public static final String RADIO = Namespaces.gPrefix + "radio";

    /** Telex number. */
    public static final String TELEX = Namespaces.gPrefix + "telex";

    /** TTY/TDD number. */
    public static final String TTY_TDD = Namespaces.gPrefix + "tty_tdd";

    /** Work phone number. */
    public static final String WORK = Namespaces.gPrefix + "work";

    /** Work fax number. */
    public static final String WORK_FAX = Namespaces.gPrefix + "work_fax";

    /** Work cell phone number. */
    public static final String WORK_MOBILE = Namespaces.gPrefix + "work_mobile";

    /** Work pager number. */
    public static final String WORK_PAGER = Namespaces.gPrefix + "work_pager";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      ASSISTANT,
      CALLBACK,
      CAR,
      COMPANY_MAIN,
      FAX,
      HOME,
      HOME_FAX,
      ISDN,
      MAIN,
      MOBILE,
      OTHER,
      OTHER_FAX,
      PAGER,
      RADIO,
      TELEX,
      TTY_TDD,
      WORK,
      WORK_FAX,
      WORK_MOBILE,
      WORK_PAGER};

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
      PhoneNumber> KEY = ElementKey.of(new QName(Namespaces.gNs, "phoneNumber"),
      String.class, PhoneNumber.class);

  /**
   * Simple string value used to name this phone number.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Whether this is the primary phone number.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * Programmatic value that identifies the type of phone number.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * An optional "tel URI" useful for programmatic access.
   */
  public static final AttributeKey<String> URI = AttributeKey.of(new QName(null,
      "uri"), String.class);

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
    builder.addAttribute(URI);
  }

  /**
   * Constructs an instance using the default key.
   */
  public PhoneNumber() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected PhoneNumber(ElementKey<String, ? extends PhoneNumber> key) {
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
  protected PhoneNumber(ElementKey<String, ? extends PhoneNumber> key,
      Element source) {
    super(key, source);
  }

  /**
   * Constructs a new instance with the given value.
   *
   * @param value human-readable phone number.
   */
  public PhoneNumber(String value) {
    this();
    setValue(value);
  }

  @Override
  public PhoneNumber lock() {
    return (PhoneNumber) super.lock();
  }

  /**
   * Returns the simple string value used to name this phone number.
   *
   * @return simple string value used to name this phone number
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the simple string value used to name this phone number.
   *
   * @param label simple string value used to name this phone number or {@code
   *     null} to reset
   * @return this to enable chaining setters
   */
  public PhoneNumber setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the simple string value used to name this phone
   * number.
   *
   * @return whether it has the simple string value used to name this phone
   *     number
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the whether this is the primary phone number.
   *
   * @return whether this is the primary phone number
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary phone number.
   *
   * @param primary whether this is the primary phone number or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public PhoneNumber setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
    return this;
  }

  /**
   * Returns whether it has the whether this is the primary phone number.
   *
   * @return whether it has the whether this is the primary phone number
   */
  public boolean hasPrimary() {
    return super.hasAttribute(PRIMARY);
  }

  /**
   * Returns the programmatic value that identifies the type of phone number.
   *
   * @return programmatic value that identifies the type of phone number
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the programmatic value that identifies the type of phone number.
   *
   * @param rel programmatic value that identifies the type of phone number or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public PhoneNumber setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the programmatic value that identifies the type of
   * phone number.
   *
   * @return whether it has the programmatic value that identifies the type of
   *     phone number
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the An optional "tel URI" useful for programmatic access.
   *
   * @return An optional "tel URI" useful for programmatic access
   */
  public String getUri() {
    return super.getAttributeValue(URI);
  }

  /**
   * Sets the An optional "tel URI" useful for programmatic access.
   *
   * @param uri An optional "tel URI" useful for programmatic access or {@code
   *     null} to reset
   * @return this to enable chaining setters
   */
  public PhoneNumber setUri(String uri) {
    super.setAttributeValue(URI, uri);
    return this;
  }

  /**
   * Returns whether it has the An optional "tel URI" useful for programmatic
   * access.
   *
   * @return whether it has the An optional "tel URI" useful for programmatic
   *     access
   */
  public boolean hasUri() {
    return super.hasAttribute(URI);
  }

  /**
   * Returns the human-readable phone number.
   *
   * @return human-readable phone number
   */
  public String getValue() {
    return super.getTextValue(KEY);
  }

  /**
   * Sets the human-readable phone number.
   *
   * @param value human-readable phone number or {@code null} to reset
   * @return this to enable chaining setters
   */
  public PhoneNumber setValue(String value) {
    super.setTextValue(value);
    return this;
  }

  /**
   * Returns whether it has the human-readable phone number.
   *
   * @return whether it has the human-readable phone number
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
    PhoneNumber other = (PhoneNumber) obj;
    return eq(getLabel(), other.getLabel())
        && eq(getPrimary(), other.getPrimary())
        && eq(getRel(), other.getRel())
        && eq(getUri(), other.getUri())
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
    if (getUri() != null) {
      result = 37 * result + getUri().hashCode();
    }
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


