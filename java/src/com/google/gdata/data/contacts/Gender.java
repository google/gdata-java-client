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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Contact's gender.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Gender.XML_NAME)
public class Gender extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "gender";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  private static final AttributeHelper.EnumToAttributeValue<Value>
      VALUE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Value>();

  /** Gender */
  private Value value = null;

  /** Gender. */
  public enum Value {

    /** Female gender. */
    FEMALE,

    /** Male gender. */
    MALE

  }

  /**
   * Default mutable constructor.
   */
  public Gender() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value Gender.
   */
  public Gender(Value value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the Gender.
   *
   * @return Gender
   */
  public Value getValue() {
    return value;
  }

  /**
   * Sets the Gender.
   *
   * @param value Gender or <code>null</code> to reset
   */
  public void setValue(Value value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the Gender.
   *
   * @return whether it has the Gender
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (value == null) {
      throwExceptionForMissingAttribute(VALUE);
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
        ExtensionDescription.getDefaultDescription(Gender.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(VALUE, value, VALUE_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    value = helper.consumeEnum(VALUE, true, Value.class, null,
        VALUE_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Gender other = (Gender) obj;
    return eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Gender value=" + value + "}";
  }

}

