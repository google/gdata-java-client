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
 * Contact's user defined field.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = UserDefinedField.XML_NAME)
public class UserDefinedField extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "userDefinedField";

  /** XML "key" attribute name */
  private static final String KEY = "key";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Key */
  private String key = null;

  /** Value */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public UserDefinedField() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param key key.
   * @param value value.
   */
  public UserDefinedField(String key, String value) {
    super();
    setKey(key);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the key.
   *
   * @return key
   */
  public String getKey() {
    return key;
  }

  /**
   * Sets the key.
   *
   * @param key key or <code>null</code> to reset
   */
  public void setKey(String key) {
    throwExceptionIfImmutable();
    this.key = key;
  }

  /**
   * Returns whether it has the key.
   *
   * @return whether it has the key
   */
  public boolean hasKey() {
    return getKey() != null;
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
    if (key == null) {
      throwExceptionForMissingAttribute(KEY);
    }
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
        ExtensionDescription.getDefaultDescription(UserDefinedField.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(KEY, key);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    key = helper.consume(KEY, true);
    value = helper.consume(VALUE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    UserDefinedField other = (UserDefinedField) obj;
    return eq(key, other.key)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (key != null) {
      result = 37 * result + key.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{UserDefinedField key=" + key + " value=" + value + "}";
  }

}

