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


package com.google.gdata.client.contacts;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the following feeds:<ul>
 * <li>The feed of contacts</li>
 * <li>The feed of contact groups</li>
 * </ul>
 *
 * 
 */
public class ContactQuery extends Query {

  /** Group id. */
  private String group;

  /** Order of entries in a feed. */
  private OrderBy orderBy = OrderBy.NONE;

  /** Should tombstones be returned. */
  private Boolean showDeleted = false;

  /** Direction of sorting. */
  private SortOrder sortOrder = SortOrder.NONE;

  /** Order of entries in a feed.  Default value is {@link #NONE}. */
  public enum OrderBy {

    /** Order the returned feed's entries by their &lt;app:edited&gt; values. */
    EDITED("edited"),

    /** Order the returned feed's entries by their &lt;updated&gt; values. */
    LAST_MODIFIED("lastmodified"),

    /** None. */
    NONE("none");

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

  /** Direction of sorting.  Default value is {@link #NONE}. */
  public enum SortOrder {

    /** Ascending. */
    ASCENDING("ascending"),

    /** Descending. */
    DESCENDING("descending"),

    /** None. */
    NONE("none");

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
  public ContactQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the group id.  Results are limited to contacts belonging to this
   * group..
   *
   * @return group id or <code>null</code> to indicate that the parameter is not
   *     set.
   */
  public String getGroup() {
    return group;
  }

  /**
   * Sets the group id.  Results are limited to contacts belonging to this
   * group..
   *
   * @param group group id or <code>null</code> to remove this parameter if set.
   */
  public void setGroup(String group) {
    // check if setting to existing value
    if (this.group == null ? group != null : !this.group.equals(group)) {
      // set to new value for customer parameter
      this.group = group;
      setStringCustomParameter("group", group);
    }
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
   *     this parameter to default value {@link OrderBy#NONE}.
   */
  public void setOrderBy(OrderBy orderBy) {
    if (orderBy == null) {
      orderBy = OrderBy.NONE;
    }
    // check if setting to existing value
    if (!this.orderBy.equals(orderBy)) {
      // set to new value for customer parameter
      this.orderBy = orderBy;
      setStringCustomParameter("orderby",
          orderBy == OrderBy.NONE ? null : orderBy.toValue());
    }
  }

  /**
   * Returns the should tombstones be returned.
   *
   * @return should tombstones be returned.
   */
  public Boolean getShowDeleted() {
    return showDeleted;
  }

  /**
   * Sets the should tombstones be returned.
   *
   * @param showDeleted should tombstones be returned or <code>null</code> to
   *     reset this parameter to default value <code>false</code>.
   */
  public void setShowDeleted(Boolean showDeleted) {
    if (showDeleted == null) {
      showDeleted = false;
    }
    // check if setting to existing value
    if (!this.showDeleted.equals(showDeleted)) {
      // set to new value for customer parameter
      this.showDeleted = showDeleted;
      setStringCustomParameter("showdeleted",
          !showDeleted ? null : showDeleted.toString());
    }
  }

  /**
   * Returns the direction of sorting.
   *
   * @return direction of sorting.
   */
  public SortOrder getSortOrder() {
    return sortOrder;
  }

  /**
   * Sets the direction of sorting.
   *
   * @param sortOrder direction of sorting or <code>null</code> to reset this
   *     parameter to default value {@link SortOrder#NONE}.
   */
  public void setSortOrder(SortOrder sortOrder) {
    if (sortOrder == null) {
      sortOrder = SortOrder.NONE;
    }
    // check if setting to existing value
    if (!this.sortOrder.equals(sortOrder)) {
      // set to new value for customer parameter
      this.sortOrder = sortOrder;
      setStringCustomParameter("sortorder",
          sortOrder == SortOrder.NONE ? null : sortOrder.toValue());
    }
  }

}
