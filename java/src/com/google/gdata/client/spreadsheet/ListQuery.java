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
 * 
 * 
 */
public class ListQuery extends Query {

  /**
   * The URI parameter for structured spreadsheet queries.
   */
  public static final String SPREADSHEET_QUERY = "sq";
  
  /**
   * The URI parameter for sorting options.
   */
  public static final String ORDERBY = "orderby";

  /**
   * The "orderby" string for sorting by a particular column.
   * For instance, "column:title" would sort by the column title.
   */
  public static final String ORDERBY_COLUMN = "column";

  /**
   * The "orderby" string for sorting by position.
   */
  public static final String ORDERBY_POSITION = "position";

  /**
   * The URI parameter for reversing sorted results.
   */
  public static final String REVERSE = "reverse";

  /**
   * Constructs a non-restrictive row-based query, given the URI of a list
   * feed for a particular worksheet.
   * 
   * @param feedUrl the feed's URI
   */
  public ListQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Sets the structured spreadsheet query.
   * 
   * @param query the query such as "name = 'Sonja' and state = 'Georgia'"
   */
  public void setSpreadsheetQuery(String query) {
    setStringCustomParameter(SPREADSHEET_QUERY, query);
  }
  
  /**
   * Gets the structured spreadsheet query string.
   */
  public String getSpreadsheetQuery() {
    return getStringCustomParameter(SPREADSHEET_QUERY);
  }
  
  /**
   * Sets the full-text query.
   * 
   * Matched entries must contain all the specified words somewhere in the
   * input.
   * 
   * @param query the full-text query string such as "Sonja Georgia"
   */
  public void setFullTextQuery(String query) {
    
    // overrides super simply for the javadoc
    super.setFullTextQuery(query);
  }

  // and setOrderBy
  // 
  // It might also be necessary to re-think the 
  // signatures
  //
  // ie. keep setSortColumn(String column)
  // and change setOrderby to orderByPostion(boolean enable)
  // or something
  

  /**
   * Sort by a specific column.
   * 
   * Convenience method for setOrderBy("column:" + column).
   * 
   * @param column
   */
  public void setSortColumn(String column) {
    if ( column == null || column.split("\\s").length > 1 ) {
      setOrderBy(null);
    } else {
      setOrderBy(ORDERBY_COLUMN + ":" + column);
    }
  }
  
  /**
   * Sets the sorting parameter, which can be "position", or
   * "column:(column name)".
   * 
   * @param orderby the order-by parameter
   */
  public void setOrderBy(String orderby) {
    if ( orderby == null || orderby.equals("position")
        || (orderby.startsWith(ORDERBY_COLUMN)
          && orderby.split("\\s").length == 1) ) {
      setStringCustomParameter(ORDERBY, orderby);
    }
  }
  
  /**
   * Gets the sorting query parameter.
   */
  public String getOrderBy() {
    return getStringCustomParameter(ORDERBY);
  }

  /**
   * Sets whether the results should be reversed, i.e. descending.
   * 
   * @param reverse whether to reverse results
   */
  public void setReverse(boolean reverse) {
    setStringCustomParameter(REVERSE, reverse ? "true" : null);
  }
  
  /**
   * Gets whether the query should be reversed.
   */
  public boolean isReverse() {
    return getStringCustomParameter(REVERSE) != null;
  }
}
