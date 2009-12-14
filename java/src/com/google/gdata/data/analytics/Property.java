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


package com.google.gdata.data.analytics;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Generic account-level property.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = Property.XML_NAME)
public class Property extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "property";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Name of the property */
  private String name = null;

  /** Value of the property */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public Property() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param name name of the property.
   * @param value value of the property.
   */
  public Property(String name, String value) {
    super();
    setName(name);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the name of the property.
   *
   * @return name of the property
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the property.
   *
   * @param name name of the property or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the name of the property.
   *
   * @return whether it has the name of the property
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the value of the property.
   *
   * @return value of the property
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the property.
   *
   * @param value value of the property or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the value of the property.
   *
   * @return whether it has the value of the property
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
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
        ExtensionDescription.getDefaultDescription(Property.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(NAME, name);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    name = helper.consume(NAME, true);
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
    Property other = (Property) obj;
    return eq(name, other.name)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Property name=" + name + " value=" + value + "}";
  }

}

