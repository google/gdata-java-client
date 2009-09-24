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


package com.google.gdata.client.sites;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed containing descriptions of all activity within
 * a site.
 *
 * 
 */
public class ActivityQuery extends Query {

  /** Content of a the given kind(s). */
  private String kind;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public ActivityQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the content of a the given kind(s).
   *
   * @return content of a the given kind(s) or <code>null</code> to indicate
   *     that the parameter is not set.
   */
  public String getKind() {
    return kind;
  }

  /**
   * Sets the content of a the given kind(s).
   *
   * @param kind content of a the given kind(s) or <code>null</code> to remove
   *     this parameter if set.
   */
  public void setKind(String kind) {
    // check if setting to existing value
    if (this.kind == null ? kind != null : !this.kind.equals(kind)) {
      // set to new value for customer parameter
      this.kind = kind;
      setStringCustomParameter("kind", kind);
    }
  }

}

