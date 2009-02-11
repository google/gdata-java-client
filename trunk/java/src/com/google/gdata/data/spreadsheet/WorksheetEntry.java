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

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.OutOfLineContent;
import com.google.gdata.util.Version;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * One worksheet, when listing all worksheets within a spreadsheet.
 *
 * For instance, this might list Sheet1, Sheet2, Sheet3, etc.
 *
 * 
 */
@Kind.Term(WorksheetEntry.KIND)
public class WorksheetEntry extends BaseEntry<WorksheetEntry> {

  /**
   * Kind category term used to label the entries that contains worksheet data.
   */
  public static final String KIND = Namespaces.gSpreadPrefix + "worksheet";

  /**
   * Category used to label entries that contain worksheet data.
   */
  public static final Category CATEGORY =
    new Category(Namespaces.gSpread, KIND);

  /**
   * Constructs a new uninitialized entry to be populated by the
   * GData parsers.
   */
  public WorksheetEntry() {
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new entry with the given row count and column count
   * @param rowCount the number of rows in the worksheet
   * @param colCount the number of columns in a worksheet
   */
  public WorksheetEntry(int rowCount, int colCount) {
    getCategories().add(CATEGORY);
    addExtension(new RowCount(rowCount));
    addExtension(new ColCount(colCount));
  }

  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public WorksheetEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }

  /**
   * Declares any extensions.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(WorksheetEntry.class, RowCount.getDefaultDescription());
    extProfile.declare(WorksheetEntry.class, ColCount.getDefaultDescription());
  }

  /**
   * Gets the URL for this worksheet's list feed.
   *
   * <p>You can then create a query using this URL to query this worksheet's
   * rows, using the very powerful query model.
   *
   * @return a URL to get a feed of worksheets
   */
  public URL getListFeedUrl() {
    try {
      return new URL(getFeedUrlString(Namespaces.LIST_LINK_REL));
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
    }
  }

  /**
   * Gets the URL for this worksheet's cells feed.
   *
   * <p>With this feed, you can query for arbitrary ranges of cells.
   *
   * @return a URL to the cells feed
   */
  public URL getCellFeedUrl() {
    try {
      return new URL(getFeedUrlString(Namespaces.CELLS_LINK_REL));
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
    }
  }

  private String getFeedUrlString(String linkRelKind) {
    Version spreadsheetVersion = state.service.getProtocolVersion();

    if (spreadsheetVersion.isCompatible(SpreadsheetService.Versions.V1)) {
      Link feedLink = this.getLink(linkRelKind, Link.Type.ATOM);
      return feedLink.getHref();
    } else { // must be SpreadsheetService.Versions.V2; only 2 versions for now
      // List or Cells feed Url?
      if (linkRelKind.equals(Namespaces.LIST_LINK_REL)) {
        // the list feed is stored as a <content> tag
        return ((OutOfLineContent)(this.getContent())).getUri();
      } else { // it must be Namespaces.CELLS_LINK_REL
        // the cells feed is stored in the <link> tag
        Link feedLink = this.getLink(linkRelKind, Link.Type.ATOM);
        return feedLink.getHref();
      }
    }
  }

  /**
   * Gets the total number of rows.
   *
   * This refers to the hard bound on rows.  It is possible that your
   * spreadsheet has many, many empty rows, all of which are counted in
   * this count.
   *
   * Column positions 1 to getRowCount() are valid.
   */
  public int getRowCount() {
    RowCount count = getExtension(RowCount.class);
    if (count != null) {
      return count.getCount();
    } else {
      return 0;
    }
  }

  /**
   * Sets the total number of rows.
   *
   * If the new number of rows is greater than the old, (new-old)
   * blank rows will be appended to the end.  If the new number of
   * rows is less than the old, then (old-new) rows will be removed
   * from the end which will DELETE ALL DATE IN DELETED ROWS.
   *
   * @param count the new row count.
   */
  public void setRowCount(int count) {
    setExtension(new RowCount(count));
  }

  /**
   * Gets the total number of columns.
   *
   * This refers to the hard bound on columns.  It is possible that your
   * spreadsheet has many empty columns, all of which are counted in
   * this count.
   *
   * Column positions 1 to getColCount() are valid.
   */
  public int getColCount() {
    ColCount count = getExtension(ColCount.class);
    if (count != null) {
      return count.getCount();
    } else {
      return 0;
    }
  }

  /**
   * Sets the total number of columns.
   *
   * If the new number of columns is greater than the old, (new-old)
   * blank columns will be appended to the end.  If the new number of
   * columns is less than the old, then (old-new) columns will be removed
   * from the end which will DELETE ALL DATE IN DELETED COLUMNS.
   *
   * @param count the new column count.
   */
  public void setColCount(int count) {
    setExtension(new ColCount(count));
  }
}
