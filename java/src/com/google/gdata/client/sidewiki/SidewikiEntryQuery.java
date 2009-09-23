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


package com.google.gdata.client.sidewiki;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of Sidewiki entries.
 *
 * 
 */
public class SidewikiEntryQuery extends Query {

  /** Order in which to sort entries. */
  private SortOrder sortOrder;

  /** Order in which to sort entries. */
  public enum SortOrder {

    /** Orders by entry creation date. */
    PUBLISHED("published"),

    /** Orders by entry quality. */
    QUALITY("quality"),

    /** Orders by entry last modification date. */
    UPDATED("updated");

    private final String value;

    private SortOrder(String value) {
      this.value = value;
    }

    /**
     * Returns the value used in the URL.
     *
     * @return value used in the URL.
     */
    public String toValue() {
      return value;
    }

  }

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public SidewikiEntryQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the order in which to sort entries.
   *
   * @return order in which to sort entries or <code>null</code> to indicate
   *     that the parameter is not set.
   */
  public SortOrder getSortOrder() {
    return sortOrder;
  }

  /**
   * Sets the order in which to sort entries.
   *
   * @param sortOrder order in which to sort entries or <code>null</code> to
   *     remove this parameter if set.
   */
  public void setSortOrder(SortOrder sortOrder) {
    // check if setting to existing value
    if (this.sortOrder == null ? sortOrder != null :
        !this.sortOrder.equals(sortOrder)) {
      // set to new value for customer parameter
      this.sortOrder = sortOrder;
      setStringCustomParameter("sortorder",
          sortOrder == null ? null : sortOrder.toValue());
    }
  }

}

