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


package com.google.gdata.client.gtt;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of documents.
 *
 * 
 */
public class DocumentQuery extends Query {

  /** Onlydeleted. */
  private Boolean onlydeleted;

  /** Sharedwith. */
  private String sharedWithEmailId;

  /** Showdeleted. */
  private Boolean showdeleted;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public DocumentQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the onlydeleted.
   *
   * @return onlydeleted or <code>null</code> to indicate that the parameter is
   *     not set.
   */
  public Boolean getOnlydeleted() {
    return onlydeleted;
  }

  /**
   * Sets the onlydeleted.
   *
   * @param onlydeleted onlydeleted or <code>null</code> to remove this
   *     parameter if set.
   */
  public void setOnlydeleted(Boolean onlydeleted) {
    // check if setting to existing value
    if (this.onlydeleted == null ? onlydeleted != null :
        !this.onlydeleted.equals(onlydeleted)) {
      // set to new value for customer parameter
      this.onlydeleted = onlydeleted;
      setStringCustomParameter("onlydeleted",
          onlydeleted == null ? null : onlydeleted.toString());
    }
  }

  /**
   * Returns the sharedwith.
   *
   * @return sharedwith or <code>null</code> to indicate that the parameter is
   *     not set.
   */
  public String getSharedWithEmailId() {
    return sharedWithEmailId;
  }

  /**
   * Sets the sharedwith.
   *
   * @param sharedWithEmailId sharedwith or <code>null</code> to remove this
   *     parameter if set.
   */
  public void setSharedWithEmailId(String sharedWithEmailId) {
    // check if setting to existing value
    if (this.sharedWithEmailId == null ? sharedWithEmailId != null :
        !this.sharedWithEmailId.equals(sharedWithEmailId)) {
      // set to new value for customer parameter
      this.sharedWithEmailId = sharedWithEmailId;
      setStringCustomParameter("sharedwith", sharedWithEmailId);
    }
  }

  /**
   * Returns the showdeleted.
   *
   * @return showdeleted or <code>null</code> to indicate that the parameter is
   *     not set.
   */
  public Boolean getShowdeleted() {
    return showdeleted;
  }

  /**
   * Sets the showdeleted.
   *
   * @param showdeleted showdeleted or <code>null</code> to remove this
   *     parameter if set.
   */
  public void setShowdeleted(Boolean showdeleted) {
    // check if setting to existing value
    if (this.showdeleted == null ? showdeleted != null :
        !this.showdeleted.equals(showdeleted)) {
      // set to new value for customer parameter
      this.showdeleted = showdeleted;
      setStringCustomParameter("showdeleted",
          showdeleted == null ? null : showdeleted.toString());
    }
  }

}

