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


package com.google.gdata.data.calendar;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Describes how many times calendar was cleaned via Manage Calendars.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gCalAlias,
    nsUri = Namespaces.gCal,
    localName = TimesCleanedProperty.XML_NAME)
public class TimesCleanedProperty extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "timesCleaned";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Number of times calendar was cleaned */
  private Integer value = null;

  /**
   * Default mutable constructor.
   */
  public TimesCleanedProperty() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value number of times calendar was cleaned.
   */
  public TimesCleanedProperty(Integer value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the number of times calendar was cleaned.
   *
   * @return number of times calendar was cleaned
   */
  public Integer getValue() {
    return value;
  }

  /**
   * Sets the number of times calendar was cleaned.
   *
   * @param value number of times calendar was cleaned or <code>null</code> to
   *     reset
   */
  public void setValue(Integer value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the number of times calendar was cleaned.
   *
   * @return whether it has the number of times calendar was cleaned
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (value == null) {
      throwExceptionForMissingAttribute(VALUE);
    } else if (value < 0) {
      throw new IllegalStateException("value attribute must be non-negative: " +
          value);
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
        ExtensionDescription.getDefaultDescription(TimesCleanedProperty.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    value = helper.consumeInteger(VALUE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    TimesCleanedProperty other = (TimesCleanedProperty) obj;
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
    return "{TimesCleanedProperty value=" + value + "}";
  }

}
