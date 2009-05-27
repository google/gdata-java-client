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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Describes a worksheet where the table lives.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gSpreadAlias,
    nsUri = Namespaces.gSpread,
    localName = Worksheet.XML_NAME)
public class Worksheet extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "worksheet";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /**
   * Default mutable constructor.
   */
  public Worksheet() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param name immutable worksheet name or <code>null</code> for a mutable
   *     worksheet name
   */
  public Worksheet(String name) {
    super(Namespaces.gSpreadNs, XML_NAME, NAME, name);
  }

  /**
   * Returns the worksheet name.
   *
   * @return worksheet name
   */
  public String getName() {
    return getValue();
  }

  /**
   * Sets the worksheet name.
   *
   * @param name worksheet name or <code>null</code> to reset
   */
  public void setName(String name) {
    setValue(name);
  }

  /**
   * Returns whether it has the worksheet name.
   *
   * @return whether it has the worksheet name
   */
  public boolean hasName() {
    return hasValue();
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
        ExtensionDescription.getDefaultDescription(Worksheet.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Worksheet name=" + getValue() + "}";
  }

}
