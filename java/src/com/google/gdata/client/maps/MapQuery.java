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


package com.google.gdata.client.maps;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of user-created maps.
 *
 * 
 */
public class MapQuery extends Query {

  /** The last feature of the previous page in the feed. */
  private String prevId;

  /** Should tombstones be returned. */
  private Boolean showDeleted;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public MapQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the the last feature of the previous page in the feed.
   *
   * @return the last feature of the previous page in the feed or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getPrevId() {
    return prevId;
  }

  /**
   * Sets the the last feature of the previous page in the feed.
   *
   * @param prevId the last feature of the previous page in the feed or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setPrevId(String prevId) {
    // check if setting to existing value
    if (this.prevId == null ? prevId != null : !this.prevId.equals(prevId)) {
      // set to new value for customer parameter
      this.prevId = prevId;
      setStringCustomParameter("previd", prevId);
    }
  }

  /**
   * Returns the should tombstones be returned.
   *
   * @return should tombstones be returned or <code>null</code> to indicate that
   *     the parameter is not set.
   */
  public Boolean getShowDeleted() {
    return showDeleted;
  }

  /**
   * Sets the should tombstones be returned.
   *
   * @param showDeleted should tombstones be returned or <code>null</code> to
   *     remove this parameter if set.
   */
  public void setShowDeleted(Boolean showDeleted) {
    // check if setting to existing value
    if (this.showDeleted == null ? showDeleted != null :
        !this.showDeleted.equals(showDeleted)) {
      // set to new value for customer parameter
      this.showDeleted = showDeleted;
      setStringCustomParameter("showdeleted",
          showDeleted == null ? null : showDeleted.toString());
    }
  }

}

