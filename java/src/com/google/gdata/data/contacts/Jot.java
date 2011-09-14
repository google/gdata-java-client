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


package com.google.gdata.data.contacts;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Contact's jot.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Jot.XML_NAME)
public class Jot extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "jot";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  private static final AttributeHelper.EnumToAttributeValue<Rel>
      REL_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Rel>();

  /** Jot type */
  private Rel rel = null;

  /** Value */
  private String value = null;

  /** Jot type. */
  public enum Rel {

    /** Home jot. */
    HOME,

    /** Keywords jot. */
    KEYWORDS,

    /** Other jot. */
    OTHER,

    /** User jot. */
    USER,

    /** Work jot. */
    WORK

  }

  /**
   * Default mutable constructor.
   */
  public Jot() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param rel jot type.
   * @param value value.
   */
  public Jot(Rel rel, String value) {
    super();
    setRel(rel);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the jot type.
   *
   * @return jot type
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the jot type.
   *
   * @param rel jot type or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the jot type.
   *
   * @return whether it has the jot type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (rel == null) {
      throwExceptionForMissingAttribute(REL);
    }
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Jot.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(REL, rel, REL_ENUM_TO_ATTRIBUTE_VALUE);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    rel = helper.consumeEnum(REL, true, Rel.class, null,
        REL_ENUM_TO_ATTRIBUTE_VALUE);
    value = helper.consume(null, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Jot other = (Jot) obj;
    return eq(rel, other.rel)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (rel != null) {
      result = 37 * result + rel.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Jot rel=" + rel + " value=" + value + "}";
  }

}

