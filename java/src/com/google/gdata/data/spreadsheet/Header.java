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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Describes a header row.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gSpreadAlias,
    nsUri = Namespaces.gSpread,
    localName = Header.XML_NAME)
public class Header extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "header";

  /** XML "row" attribute name */
  private static final String ROW = "row";

  /** Position of the header */
  private Integer row = null;

  /**
   * Default mutable constructor.
   */
  public Header() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param row position of the header.
   */
  public Header(Integer row) {
    super();
    setRow(row);
    setImmutable(true);
  }

  /**
   * Returns the position of the header.
   *
   * @return position of the header
   */
  public Integer getRow() {
    return row;
  }

  /**
   * Sets the position of the header.
   *
   * @param row position of the header or <code>null</code> to reset
   */
  public void setRow(Integer row) {
    throwExceptionIfImmutable();
    this.row = row;
  }

  /**
   * Returns whether it has the position of the header.
   *
   * @return whether it has the position of the header
   */
  public boolean hasRow() {
    return getRow() != null;
  }

  @Override
  protected void validate() {
    if (row == null) {
      throwExceptionForMissingAttribute(ROW);
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
        ExtensionDescription.getDefaultDescription(Header.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(ROW, row);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    row = helper.consumeInteger(ROW, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Header other = (Header) obj;
    return eq(row, other.row);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (row != null) {
      result = 37 * result + row.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Header row=" + row + "}";
  }

}
