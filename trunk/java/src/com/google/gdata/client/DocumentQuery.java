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


package com.google.gdata.client;


import com.google.gdata.client.Query;

import java.net.URL;


/**
 * Simple class for document-based queries, i.e. things that have titles.
 *
 * 
 */
public class DocumentQuery extends Query {

  /**
   * The URI parameter for querying the title or name of a document.
   */
  public static final String TITLE = "title";

  /**
   * The URI parameter for a boolean flag for whether the title should be
   * matched exactly (case-insensitive or sensitive depending on service).
   */
  public static final String TITLE_EXACT = "title-exact";

  /**
   * The URI parameter for how the query should be sorted, can sort by
   * last-modified, last-accessed,  title and starred; will default to
   * last-modified.
   */
  public static final String ORDERBY = "orderby";

  /* Different sort modes that are supported */
  public static final String LAST_MODIFIED_SORT = "last-modified";
  public static final String LAST_VIEWED_SORT = "last-viewed";
  public static final String TITLE_SORT = "title";
  public static final String STARRED_SORT = "starred";
  public static final String DEFAULT_SORT_MODE = LAST_MODIFIED_SORT;

  /**
   * Constructs a query for querying spreadsheets that you have access to.
   *
   * @param feedUrl the feed's URI
   */
  public DocumentQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Sets the full-text title search.
   *
   * @param titleQuery the title query
   */
  public void setTitleQuery(String titleQuery) {
    setStringCustomParameter(TITLE, titleQuery);
  }

  /**
   * Gets full-text title query.
   */
  public String getTitleQuery() {
    return getStringCustomParameter(TITLE);
  }

  /**
   * Sets whether the title match should be exact (instead of keyword-based).
   *
   * By default, the match is not exact.
   *
   * @param exact whether the results should be exact
   */
  public void setTitleExact(boolean exact) {
    setStringCustomParameter(TITLE_EXACT, exact ? "true" : null);
  }

  /**
   * Gets whether the query should be exact (rather than keyword-based).
   *
   * @return whether the results should be exact
   */
  public boolean isTitleExact() {
    return getStringCustomParameter(TITLE_EXACT) != null;
  }

  /**
   * Sets the sort mode of the query. Can be one of the following:
   * last-modified (default), last-accessed, title, starred
   *
   * @param orderby the sort mode
   */
  public void setSortMode(String orderby) {
    setStringCustomParameter(ORDERBY, orderby);
  }

  /**
   * Gets the sort mode
   */
  public String getSortMode() {
    return getStringCustomParameter(ORDERBY);
  }

}
