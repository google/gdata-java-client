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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Describes a field.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gSpreadAlias,
    nsUri = Namespaces.gSpread,
    localName = Field.XML_NAME)
public class Field extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "field";

  /** XML "index" attribute name */
  private static final String INDEX = "index";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** Letter(s) or integer position of the column */
  private String index = null;

  /** User defined name of the column */
  private String name = null;

  /** Displayed value of the cell */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public Field() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param index letter(s) or integer position of the column.
   * @param name user defined name of the column.
   * @param value displayed value of the cell.
   */
  public Field(String index, String name, String value) {
    super();
    setIndex(index);
    setName(name);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the letter(s) or integer position of the column.
   *
   * @return letter(s) or integer position of the column
   */
  public String getIndex() {
    return index;
  }

  /**
   * Sets the letter(s) or integer position of the column.
   *
   * @param index letter(s) or integer position of the column or
   *     <code>null</code> to reset
   */
  public void setIndex(String index) {
    throwExceptionIfImmutable();
    this.index = index;
  }

  /**
   * Returns whether it has the letter(s) or integer position of the column.
   *
   * @return whether it has the letter(s) or integer position of the column
   */
  public boolean hasIndex() {
    return getIndex() != null;
  }

  /**
   * Returns the user defined name of the column.
   *
   * @return user defined name of the column
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the user defined name of the column.
   *
   * @param name user defined name of the column or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the user defined name of the column.
   *
   * @return whether it has the user defined name of the column
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the displayed value of the cell.
   *
   * @return displayed value of the cell
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the displayed value of the cell.
   *
   * @param value displayed value of the cell or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the displayed value of the cell.
   *
   * @return whether it has the displayed value of the cell
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
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
        ExtensionDescription.getDefaultDescription(Field.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(INDEX, index);
    generator.put(NAME, name);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    index = helper.consume(INDEX, false);
    name = helper.consume(NAME, true);
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
    Field other = (Field) obj;
    return eq(index, other.index)
        && eq(name, other.name)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (index != null) {
      result = 37 * result + index.hashCode();
    }
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
    return "{Field index=" + index + " name=" + name + " value=" + value + "}";
  }

}
