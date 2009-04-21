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


package com.google.gdata.data.extensions;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Name-value pair with optional type and unit.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = CustomProperty.XML_NAME)
public class CustomProperty extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "customProperty";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "unit" attribute name */
  private static final String UNIT = "unit";

  /** Can be a URI to indicate a specific ontology */
  private String name = null;

  /** Datatype such as string, integer, and date, or an ontology-specific URI */
  private String type = null;

  /** Units of data - can also be a URI to indicate a specific ontology */
  private String unit = null;

  /** Value */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public CustomProperty() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param name can be a URI to indicate a specific ontology.
   * @param type datatype such as string, integer, and date, or an
   *     ontology-specific URI.
   * @param unit units of data - can also be a URI to indicate a specific
   *     ontology.
   * @param value value.
   */
  public CustomProperty(String name, String type, String unit, String value) {
    super();
    setName(name);
    setType(type);
    setUnit(unit);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the can be a URI to indicate a specific ontology.
   *
   * @return can be a URI to indicate a specific ontology
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the can be a URI to indicate a specific ontology.
   *
   * @param name can be a URI to indicate a specific ontology or
   *     <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the can be a URI to indicate a specific ontology.
   *
   * @return whether it has the can be a URI to indicate a specific ontology
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the datatype such as string, integer, and date, or an
   * ontology-specific URI.
   *
   * @return datatype such as string, integer, and date, or an ontology-specific
   *     URI
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the datatype such as string, integer, and date, or an
   * ontology-specific URI.
   *
   * @param type datatype such as string, integer, and date, or an
   *     ontology-specific URI or <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the datatype such as string, integer, and date, or
   * an ontology-specific URI.
   *
   * @return whether it has the datatype such as string, integer, and date, or
   *     an ontology-specific URI
   */
  public boolean hasType() {
    return getType() != null;
  }

  /**
   * Returns the units of data - can also be a URI to indicate a specific
   * ontology.
   *
   * @return units of data - can also be a URI to indicate a specific ontology
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Sets the units of data - can also be a URI to indicate a specific ontology.
   *
   * @param unit units of data - can also be a URI to indicate a specific
   *     ontology or <code>null</code> to reset
   */
  public void setUnit(String unit) {
    throwExceptionIfImmutable();
    this.unit = unit;
  }

  /**
   * Returns whether it has the units of data - can also be a URI to indicate a
   * specific ontology.
   *
   * @return whether it has the units of data - can also be a URI to indicate a
   *     specific ontology
   */
  public boolean hasUnit() {
    return getUnit() != null;
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
      throw new IllegalStateException("Missing text content");
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
        ExtensionDescription.getDefaultDescription(CustomProperty.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(NAME, name);
    generator.put(TYPE, type);
    generator.put(UNIT, unit);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    name = helper.consume(NAME, true);
    type = helper.consume(TYPE, false);
    unit = helper.consume(UNIT, false);
    value = helper.consume(null, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CustomProperty other = (CustomProperty) obj;
    return eq(name, other.name)
        && eq(type, other.type)
        && eq(unit, other.unit)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (unit != null) {
      result = 37 * result + unit.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{CustomProperty name=" + name + " type=" + type + " unit=" + unit +
        " value=" + value + "}";
  }

}
