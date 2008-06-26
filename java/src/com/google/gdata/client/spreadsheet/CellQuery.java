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


package com.google.gdata.client.spreadsheet;


import com.google.gdata.client.Query;

import java.net.URL;


/**
 * Simple class for cells-feed-specific queries.
 * 
 * The main features include minimum and maximum row and column
 * numbers.
 * 
 * For instance, you could select only cells in the range of
 * rows 2 through 5 and columns 3 and beyond.  This would
 * correspond to:
 * 
 *     query.setMinimumRow(2);
 *     query.setMaximumRow(5);
 *     query.setMinimumCol(3);
 * 
 *
 * 
 * 
 */
public class CellQuery extends Query {

  /** String property for minimum row number. */
  public static final String MINIMUM_ROW = "min-row";

  /** String property for maximum row number. */
  public static final String MAXIMUM_ROW = "max-row";

  /** String property for minimum column number. */
  public static final String MINIMUM_COL = "min-col";

  /** String property for maximum column number. */
  public static final String MAXIMUM_COL = "max-col";

  /** String property for a cell range. */
  public static final String RANGE = "range";

  /** String porperty for flag to return empty cells. */
  public static final String RETURN_EMPTY = "return-empty";

  /**
   * Constructs an object representing a to-be-executed query on cells.
   * 
   * @param feedUrl the URI of the particular worksheet's cells feed
   */
  public CellQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Sets the minimum row number of cells that are returned.
   * @param value the row number, or null if there is no minimum
   */
  public void setMinimumRow(Integer value) {
    if ( value != null && value.intValue() > 0 ) {
      setIntegerCustomParameter(MINIMUM_ROW, value);
    } else {
      setIntegerCustomParameter(MINIMUM_ROW, null);
    }
  }

  /**
   * Sets the maximum row number of cells that are returned.
   * @param value the row number, or null if there is no maximum
   */
  public void setMaximumRow(Integer value) {
    if ( value != null && value.intValue() > 0 ) {
      setIntegerCustomParameter(MAXIMUM_ROW, value);
    } else {
      setIntegerCustomParameter(MAXIMUM_ROW, null);
    }
  }

  /**
   * Sets the minimum column number of cells that are returned.
   * @param value the column numer, or null if there is no minimum
   */
  public void setMinimumCol(Integer value) {
    if ( value != null && value.intValue() > 0 ) {
      setIntegerCustomParameter(MINIMUM_COL, value);
    } else {
      setIntegerCustomParameter(MINIMUM_COL, null);
    }
  }

  /**
   * Sets the maximum column number of cells that are returned.
   * @param value the column number, of null if there is no maximum
   */
  public void setMaximumCol(Integer value) {
    if ( value != null && value.intValue() > 0 ) {
      setIntegerCustomParameter(MAXIMUM_COL, value);
    } else {
      setIntegerCustomParameter(MAXIMUM_COL, null);
    }
  }

  /**
   * Sets the range for the cells to return.
   * @param value the range in A1 or R1C1 for the cells to be returned.
   */
  public void setRange(String value) {
    setStringCustomParameter(RANGE, value);
  }

  /**
   * Sets whether to return empty cells.
   * @param value if true, then empty cells will be returned in the feed.
   */
  public void setReturnEmpty(boolean value) {
    setStringCustomParameter(RETURN_EMPTY, "" + value);
  }

  /**
   * Gets the smallest row number possible in the cells to be returned.
   */
  public Integer getMinimumRow() {
    return getIntegerCustomParameter(MINIMUM_ROW);
  }

  /**
   * Gets the largest row number possible in the cells to be returned.
   */
  public Integer getMaximumRow() {
    return getIntegerCustomParameter(MAXIMUM_ROW);
  }

  /**
   * Gets the smallest column number possible in the cells to be returned.
   */
  public Integer getMinimumCol() {
    return getIntegerCustomParameter(MINIMUM_COL);
  }

  /**
   * Gets the largest column number possible in the cells to be returned.
   */
  public Integer getMaximumCol() {
    return getIntegerCustomParameter(MAXIMUM_COL);
  }

  /**
   * Gets the range of the cells to be returned.
   */
  public String getRange() {
    return getStringCustomParameter(RANGE);
  }

  /**
   * Gets whether the empty cells should be returned. 
   */
  public boolean getReturnEmpty() {
    return Boolean.parseBoolean(getStringCustomParameter(RETURN_EMPTY));
  }
                         
}
