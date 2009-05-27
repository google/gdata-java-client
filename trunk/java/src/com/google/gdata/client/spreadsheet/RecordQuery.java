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
 * Describes a query for the feed of a table's records.
 *
 * 
 */
public class RecordQuery extends Query {

  /** What column to use in ordering the entries in the feed. */
  private String orderBy;

  /** Whether to sort in descending or ascending order. */
  private Boolean reverse = false;

  /** Structured query on the full text in the worksheet. */
  private String spreadsheetQuery;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public RecordQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the what column to use in ordering the entries in the feed.
   *
   * @return what column to use in ordering the entries in the feed or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getOrderBy() {
    return orderBy;
  }

  /**
   * Sets the what column to use in ordering the entries in the feed.
   *
   * @param orderBy what column to use in ordering the entries in the feed or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setOrderBy(String orderBy) {
    // check if setting to existing value
    if (this.orderBy == null ? orderBy != null : !this.orderBy.equals(orderBy))
        {
      // set to new value for customer parameter
      this.orderBy = orderBy;
      setStringCustomParameter("orderby", orderBy);
    }
  }

  /**
   * Returns the whether to sort in descending or ascending order.
   *
   * @return whether to sort in descending or ascending order.
   */
  public Boolean getReverse() {
    return reverse;
  }

  /**
   * Sets the whether to sort in descending or ascending order.
   *
   * @param reverse whether to sort in descending or ascending order or
   *     <code>null</code> to reset this parameter to default value
   *     <code>false</code>.
   */
  public void setReverse(Boolean reverse) {
    if (reverse == null) {
      reverse = false;
    }
    // check if setting to existing value
    if (!this.reverse.equals(reverse)) {
      // set to new value for customer parameter
      this.reverse = reverse;
      setStringCustomParameter("reverse", !reverse ? null : reverse.toString());
    }
  }

  /**
   * Returns the structured query on the full text in the worksheet.
   *
   * @return structured query on the full text in the worksheet or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getSpreadsheetQuery() {
    return spreadsheetQuery;
  }

  /**
   * Sets the structured query on the full text in the worksheet.
   *
   * @param spreadsheetQuery structured query on the full text in the worksheet
   *     or <code>null</code> to remove this parameter if set.
   */
  public void setSpreadsheetQuery(String spreadsheetQuery) {
    // check if setting to existing value
    if (this.spreadsheetQuery == null ? spreadsheetQuery != null :
        !this.spreadsheetQuery.equals(spreadsheetQuery)) {
      // set to new value for customer parameter
      this.spreadsheetQuery = spreadsheetQuery;
      setStringCustomParameter("sq", spreadsheetQuery);
    }
  }

}
