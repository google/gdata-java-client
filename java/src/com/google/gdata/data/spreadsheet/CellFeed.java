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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.batch.BatchUtils;

/**
 * The feed for spreadsheet cells of Google Spreadsheets.
 *
 * 
 */
@Kind.Term(CellEntry.KIND)
public class CellFeed
    extends BaseFeed<CellFeed, CellEntry> {

  /** Constructs a blank cells feed. */
  public CellFeed() {
    super(CellEntry.class);
    getCategories().add(CellEntry.CATEGORY);
  }

  /** Constructs a cells feed from an existing feed. */
  public CellFeed(BaseFeed sourceFeed) {
    super(CellEntry.class, sourceFeed);
    getCategories().add(CellEntry.CATEGORY);
  }

  /** Declares relevant extensions into the extension profile. */
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(CellFeed.class, RowCount.getDefaultDescription());
    extProfile.declare(CellFeed.class, ColCount.getDefaultDescription());
    super.declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }

  /**
   * Gets the total number of rows in the spreadsheet.
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
