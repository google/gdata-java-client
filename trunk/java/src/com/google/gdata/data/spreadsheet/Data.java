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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * Describes a data region of a table.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gSpreadAlias,
    nsUri = Namespaces.gSpread,
    localName = Data.XML_NAME)
public class Data extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "data";

  /** XML "insertionMode" attribute name */
  private static final String INSERTIONMODE = "insertionMode";

  /** XML "numRows" attribute name */
  private static final String NUMROWS = "numRows";

  /** XML "startRow" attribute name */
  private static final String STARTROW = "startRow";

  private static final AttributeHelper.EnumToAttributeValue<InsertionMode>
      INSERTIONMODE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<InsertionMode>();

  /** Insertion mode */
  private InsertionMode insertionMode = null;

  /** Number of rows in the data section */
  private Integer numberOfRows = null;

  /** Index of the first row of the data section (inclusive) */
  private Integer startIndex = null;

  /** Insertion mode. */
  public enum InsertionMode {

    /** A new row for each record. */
    INSERT,

    /** Overwrite following rows before inserting new rows. */
    OVERWRITE

  }

  /**
   * Default mutable constructor.
   */
  public Data() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param insertionMode insertion mode.
   * @param numberOfRows number of rows in the data section.
   * @param startIndex index of the first row of the data section (inclusive).
   */
  public Data(InsertionMode insertionMode, Integer numberOfRows,
      Integer startIndex) {
    super();
    setInsertionMode(insertionMode);
    setNumberOfRows(numberOfRows);
    setStartIndex(startIndex);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Data.class)) {
      return;
    }
    extProfile.declare(Data.class, Column.getDefaultDescription(false, true));
  }

  /**
   * Returns the columns.
   *
   * @return columns
   */
  public List<Column> getColumns() {
    return getRepeatingExtension(Column.class);
  }

  /**
   * Adds a new column.
   *
   * @param column column
   */
  public void addColumn(Column column) {
    getColumns().add(column);
  }

  /**
   * Returns whether it has the columns.
   *
   * @return whether it has the columns
   */
  public boolean hasColumns() {
    return hasRepeatingExtension(Column.class);
  }

  /**
   * Returns the insertion mode.
   *
   * @return insertion mode
   */
  public InsertionMode getInsertionMode() {
    return insertionMode;
  }

  /**
   * Sets the insertion mode.
   *
   * @param insertionMode insertion mode or <code>null</code> to reset
   */
  public void setInsertionMode(InsertionMode insertionMode) {
    throwExceptionIfImmutable();
    this.insertionMode = insertionMode;
  }

  /**
   * Returns whether it has the insertion mode.
   *
   * @return whether it has the insertion mode
   */
  public boolean hasInsertionMode() {
    return getInsertionMode() != null;
  }

  /**
   * Returns the number of rows in the data section.
   *
   * @return number of rows in the data section
   */
  public Integer getNumberOfRows() {
    return numberOfRows;
  }

  /**
   * Sets the number of rows in the data section.
   *
   * @param numberOfRows number of rows in the data section or <code>null</code>
   *     to reset
   */
  public void setNumberOfRows(Integer numberOfRows) {
    throwExceptionIfImmutable();
    this.numberOfRows = numberOfRows;
  }

  /**
   * Returns whether it has the number of rows in the data section.
   *
   * @return whether it has the number of rows in the data section
   */
  public boolean hasNumberOfRows() {
    return getNumberOfRows() != null;
  }

  /**
   * Returns the index of the first row of the data section (inclusive).
   *
   * @return index of the first row of the data section (inclusive)
   */
  public Integer getStartIndex() {
    return startIndex;
  }

  /**
   * Sets the index of the first row of the data section (inclusive).
   *
   * @param startIndex index of the first row of the data section (inclusive) or
   *     <code>null</code> to reset
   */
  public void setStartIndex(Integer startIndex) {
    throwExceptionIfImmutable();
    this.startIndex = startIndex;
  }

  /**
   * Returns whether it has the index of the first row of the data section
   * (inclusive).
   *
   * @return whether it has the index of the first row of the data section
   *     (inclusive)
   */
  public boolean hasStartIndex() {
    return getStartIndex() != null;
  }

  @Override
  protected void validate() {
    if (startIndex == null) {
      throwExceptionForMissingAttribute(STARTROW);
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
        ExtensionDescription.getDefaultDescription(Data.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(INSERTIONMODE, insertionMode,
        INSERTIONMODE_ENUM_TO_ATTRIBUTE_VALUE);
    generator.put(NUMROWS, numberOfRows);
    generator.put(STARTROW, startIndex);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    insertionMode = helper.consumeEnum(INSERTIONMODE, false,
        InsertionMode.class, null, INSERTIONMODE_ENUM_TO_ATTRIBUTE_VALUE);
    numberOfRows = helper.consumeInteger(NUMROWS, false);
    startIndex = helper.consumeInteger(STARTROW, true);
  }

  @Override
  public String toString() {
    return "{Data insertionMode=" + insertionMode + " numberOfRows=" +
        numberOfRows + " startIndex=" + startIndex + "}";
  }

}
