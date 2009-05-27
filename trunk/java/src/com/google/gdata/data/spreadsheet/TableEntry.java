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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;

/**
 * Describes a table entry in the feed of a spreadsheet's tables.
 *
 * 
 */
@Kind.Term(TableEntry.KIND)
public class TableEntry extends BaseEntry<TableEntry> {

  /**
   * Table table category kind term value.
   */
  public static final String KIND = Namespaces.gSpreadPrefix + "table";

  /**
   * Table table category kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public TableEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public TableEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(TableEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(TableEntry.class, Data.getDefaultDescription(true,
        false));
    new Data().declareExtensions(extProfile);
    extProfile.declare(TableEntry.class, Header.getDefaultDescription(true,
        false));
    extProfile.declare(TableEntry.class, Worksheet.getDefaultDescription(true,
        false));
  }

  /**
   * Returns the data region of a table.
   *
   * @return data region of a table
   */
  public Data getData() {
    return getExtension(Data.class);
  }

  /**
   * Sets the data region of a table.
   *
   * @param data data region of a table or <code>null</code> to reset
   */
  public void setData(Data data) {
    if (data == null) {
      removeExtension(Data.class);
    } else {
      setExtension(data);
    }
  }

  /**
   * Returns whether it has the data region of a table.
   *
   * @return whether it has the data region of a table
   */
  public boolean hasData() {
    return hasExtension(Data.class);
  }

  /**
   * Returns the header row.
   *
   * @return header row
   */
  public Header getHeader() {
    return getExtension(Header.class);
  }

  /**
   * Sets the header row.
   *
   * @param header header row or <code>null</code> to reset
   */
  public void setHeader(Header header) {
    if (header == null) {
      removeExtension(Header.class);
    } else {
      setExtension(header);
    }
  }

  /**
   * Returns whether it has the header row.
   *
   * @return whether it has the header row
   */
  public boolean hasHeader() {
    return hasExtension(Header.class);
  }

  /**
   * Returns the worksheet where the table lives.
   *
   * @return worksheet where the table lives
   */
  public Worksheet getWorksheet() {
    return getExtension(Worksheet.class);
  }

  /**
   * Sets the worksheet where the table lives.
   *
   * @param worksheet worksheet where the table lives or <code>null</code> to
   *     reset
   */
  public void setWorksheet(Worksheet worksheet) {
    if (worksheet == null) {
      removeExtension(Worksheet.class);
    } else {
      setExtension(worksheet);
    }
  }

  /**
   * Returns whether it has the worksheet where the table lives.
   *
   * @return whether it has the worksheet where the table lives
   */
  public boolean hasWorksheet() {
    return hasExtension(Worksheet.class);
  }

  /**
   * Returns the link for the feed of a table's records.
   *
   * @return Link for the feed of a table's records or {@code null} for none.
   */
  public Link getRecordsFeedLink() {
    return getLink(SpreadsheetLink.Rel.RECORDS_FEED, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{TableEntry " + super.toString() + "}";
  }

}
