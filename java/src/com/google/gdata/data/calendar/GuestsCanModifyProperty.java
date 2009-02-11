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
 * Whether guests can modify event.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gCalAlias,
    nsUri = Namespaces.gCal,
    localName = GuestsCanModifyProperty.XML_NAME)
public class GuestsCanModifyProperty extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "guestsCanModify";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Value */
  private Boolean value = null;

  /**
   * Default mutable constructor.
   */
  public GuestsCanModifyProperty() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value value.
   */
  public GuestsCanModifyProperty(Boolean value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public Boolean getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(Boolean value) {
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
        ExtensionDescription.getDefaultDescription(
        GuestsCanModifyProperty.class);
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
    value = helper.consumeBoolean(VALUE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    GuestsCanModifyProperty other = (GuestsCanModifyProperty) obj;
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
    return "{GuestsCanModifyProperty value=" + value + "}";
  }


  public final static GuestsCanModifyProperty TRUE =
      new GuestsCanModifyProperty(true);
  public final static GuestsCanModifyProperty FALSE =
      new GuestsCanModifyProperty(false);
}
