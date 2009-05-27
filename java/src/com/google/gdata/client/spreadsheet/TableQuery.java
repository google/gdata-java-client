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
 * Describes a query for the feed of table definitions.
 *
 * 
 */
public class TableQuery extends Query {

  /** Search terms for the title of a document. */
  private String titleQuery;

  /** Whether the title query should be taken as an exact string. */
  private Boolean titleExact;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public TableQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the search terms for the title of a document.
   *
   * @return search terms for the title of a document or <code>null</code> to
   *     indicate that the parameter is not set.
   */
  public String getTitleQuery() {
    return titleQuery;
  }

  /**
   * Sets the search terms for the title of a document.
   *
   * @param titleQuery search terms for the title of a document or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setTitleQuery(String titleQuery) {
    // check if setting to existing value
    if (this.titleQuery == null ? titleQuery != null :
        !this.titleQuery.equals(titleQuery)) {
      // set to new value for customer parameter
      this.titleQuery = titleQuery;
      setStringCustomParameter("title", titleQuery);
    }
  }

  /**
   * Returns the whether the title query should be taken as an exact string.
   *
   * @return whether the title query should be taken as an exact string or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public Boolean getTitleExact() {
    return titleExact;
  }

  /**
   * Sets the whether the title query should be taken as an exact string.
   *
   * @param titleExact whether the title query should be taken as an exact
   *     string or <code>null</code> to remove this parameter if set.
   */
  public void setTitleExact(Boolean titleExact) {
    // check if setting to existing value
    if (this.titleExact == null ? titleExact != null :
        !this.titleExact.equals(titleExact)) {
      // set to new value for customer parameter
      this.titleExact = titleExact;
      setStringCustomParameter("title-exact",
          titleExact == null ? null : titleExact.toString());
    }
  }

}
