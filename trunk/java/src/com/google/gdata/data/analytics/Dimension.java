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
 * Dimension value.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = Dimension.XML_NAME)
public class Dimension extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "dimension";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Name */
  private String name = null;

  /** Value */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public Dimension() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param name name.
   * @param value value.
   */
  public Dimension(String name, String value) {
    super();
    setName(name);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name name or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the name.
   *
   * @return whether it has the name
   */
  public boolean hasName() {
    return getName() != null;
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
        ExtensionDescription.getDefaultDescription(Dimension.class);
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
    Dimension other = (Dimension) obj;
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
    return "{Dimension name=" + name + " value=" + value + "}";
  }

}

