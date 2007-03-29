/* Copyright (c) 2006 Google Inc.
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
    extProfile.declareEntryExtension(RowCount.getDefaultDescription());
    extProfile.declareEntryExtension(ColCount.getDefaultDescription());
  }
  
  
  /**
   * Gets the URL for this worksheet's list feed.
   * 
   * You can then create a query using this URL to query this worksheet's
   * rows, using the very powerful query model.
   * 
   * @return a URL to get a feed of worksheets
   */
  public URL getListFeedUrl() {
    Link feedLink = this.getLink(Namespaces.LIST_LINK_REL, Link.Type.ATOM);
    try {
      return new URL(feedLink.getHref());
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
    }
  }
  
  /**
   * Gets the URL for this worksheet's cells feed.
   * 
   * With tis feed, you can query for arbitrary ranges of cells.
   * 
   * @return a URL to the cells feed
   */
  public URL getCellFeedUrl() {
    Link feedLink = this.getLink(Namespaces.CELLS_LINK_REL, Link.Type.ATOM);
    try {
      return new URL(feedLink.getHref());
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
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
    return getExtension(RowCount.class).getCount();
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
    return getExtension(ColCount.class).getCount();
  }
}
