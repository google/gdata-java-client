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


package com.google.gdata.client.blogger;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of a blog's posts.
 *
 * 
 */
public class BlogPostQuery extends Query {

  /** Order of entries in a feed. */
  private OrderBy orderBy = OrderBy.PUBLISHED;

  /** Order of entries in a feed.  Default value is {@link #PUBLISHED}. */
  public enum OrderBy {

    /** Order the returned feed's entries by their &lt;app:edited&gt; values. */
    EDITED("edited"),

    /** Order the returned feed's entries by their &lt;published&gt; values. */
    PUBLISHED("published"),

    /** Order the returned feed's entries by their &lt;updated&gt; values. */
    UPDATED("updated");

    private final String value;

    private OrderBy(String value) {
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
  public BlogPostQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the order of entries in a feed.
   *
   * @return order of entries in a feed.
   */
  public OrderBy getOrderBy() {
    return orderBy;
  }

  /**
   * Sets the order of entries in a feed.
   *
   * @param orderBy order of entries in a feed or <code>null</code> to reset
   *     this parameter to default value {@link OrderBy#PUBLISHED}.
   */
  public void setOrderBy(OrderBy orderBy) {
    if (orderBy == null) {
      orderBy = OrderBy.PUBLISHED;
    }
    // check if setting to existing value
    if (!this.orderBy.equals(orderBy)) {
      // set to new value for customer parameter
      this.orderBy = orderBy;
      setStringCustomParameter("orderby",
          orderBy == OrderBy.PUBLISHED ? null : orderBy.toValue());
    }
  }

}
