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


package com.google.gdata.data.photos;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Access level for an album, either public or unlisted.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.PHOTOS_ALIAS,
    nsUri = Namespaces.PHOTOS,
    localName = GphotoAccess.XML_NAME)
public class GphotoAccess extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "access";

  private static final AttributeHelper.EnumToAttributeValue<Value>
      VALUE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Value>();

  /** Access level */
  private Value value = null;

  /** Access level. */
  public enum Value {

    /** Unlisted album access. */
    PRIVATE,

    /** Public album access. */
    PUBLIC

  }

  /**
   * Default mutable constructor.
   */
  public GphotoAccess() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value access level.
   */
  public GphotoAccess(Value value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the access level.
   *
   * @return access level
   */
  public Value getValue() {
    return value;
  }

  /**
   * Sets the access level.
   *
   * @param value access level or <code>null</code> to reset
   */
  public void setValue(Value value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the access level.
   *
   * @return whether it has the access level
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
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
        ExtensionDescription.getDefaultDescription(GphotoAccess.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.setContent(VALUE_ENUM_TO_ATTRIBUTE_VALUE.getAttributeValue(
        value));
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    value = helper.consumeEnum(null, false, Value.class, null,
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
    GphotoAccess other = (GphotoAccess) obj;
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
    return "{GphotoAccess value=" + value + "}";
  }


  /**
   * Constructs an access from a string, for backwards compatibility.
   */
  public GphotoAccess(String access) {
    super();
    Value value = access == null ? null : Value.valueOf(access.toUpperCase());
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the access as a string, for backwards compatibility.
   */
  public String asString() {
    return value == null ? null : value.name().toLowerCase();
  }
}
