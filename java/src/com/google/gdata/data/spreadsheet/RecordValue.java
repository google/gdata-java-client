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
 * Describes a value in a column of the record entry.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gSpreadCustomAlias,
    nsUri = Namespaces.gSpreadCustom,
    localName = RecordValue.XML_NAME)
public class RecordValue extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "*";

  /**
   * Default mutable constructor.
   */
  public RecordValue() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable displayed value of the cell or <code>null</code> for
   *     a mutable displayed value of the cell
   */
  public RecordValue(String value) {
    super(Namespaces.gSpreadCustomNs, XML_NAME, null, value);
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
        ExtensionDescription.getDefaultDescription(RecordValue.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{RecordValue value=" + getValue() + "}";
  }

}
