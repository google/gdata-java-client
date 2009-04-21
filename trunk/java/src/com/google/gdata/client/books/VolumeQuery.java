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


package com.google.gdata.client.books;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the feed of information about one volume.
 *
 * 
 */
public class VolumeQuery extends Query {

  /** Ebook. */
  private String ebook;

  /** Min-viewability. */
  private MinViewability minViewability = MinViewability.NONE;

  /** Min-viewability.  Default value is {@link #NONE}. */
  public enum MinViewability {

    /** Full. */
    FULL("full"),

    /** None. */
    NONE("none"),

    /** Partial. */
    PARTIAL("partial");

    private final String value;

    private MinViewability(String value) {
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
  public VolumeQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the ebook.
   *
   * @return ebook or <code>null</code> to indicate that the parameter is not
   *     set.
   */
  public String getEbook() {
    return ebook;
  }

  /**
   * Sets the ebook.
   *
   * @param ebook ebook or <code>null</code> to remove this parameter if set.
   */
  public void setEbook(String ebook) {
    // check if setting to existing value
    if (this.ebook == null ? ebook != null : !this.ebook.equals(ebook)) {
      // set to new value for customer parameter
      this.ebook = ebook;
      setStringCustomParameter("ebook", ebook);
    }
  }

  /**
   * Returns the min-viewability.
   *
   * @return min-viewability.
   */
  public MinViewability getMinViewability() {
    return minViewability;
  }

  /**
   * Sets the min-viewability.
   *
   * @param minViewability min-viewability or <code>null</code> to reset this
   *     parameter to default value {@link MinViewability#NONE}.
   */
  public void setMinViewability(MinViewability minViewability) {
    if (minViewability == null) {
      minViewability = MinViewability.NONE;
    }
    // check if setting to existing value
    if (!this.minViewability.equals(minViewability)) {
      // set to new value for customer parameter
      this.minViewability = minViewability;
      setStringCustomParameter("min-viewability",
          minViewability == MinViewability.NONE ? null :
          minViewability.toValue());
    }
  }

}
