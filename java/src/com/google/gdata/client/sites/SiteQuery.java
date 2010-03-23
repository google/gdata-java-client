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
 * Describes a query for the feed containing descriptions of a user's sites.
 *
 * 
 */
public class SiteQuery extends Query {

  /** Include all sites that can be viewed. */
  private String includeAllSites;

  /** Handle web address mappings. */
  private Boolean withMappings;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public SiteQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the include all sites that can be viewed.
   *
   * @return include all sites that can be viewed or <code>null</code> to
   *     indicate that the parameter is not set.
   */
  public String getIncludeAllSites() {
    return includeAllSites;
  }

  /**
   * Sets the include all sites that can be viewed.
   *
   * @param includeAllSites include all sites that can be viewed or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setIncludeAllSites(String includeAllSites) {
    // check if setting to existing value
    if (this.includeAllSites == null ? includeAllSites != null :
        !this.includeAllSites.equals(includeAllSites)) {
      // set to new value for customer parameter
      this.includeAllSites = includeAllSites;
      setStringCustomParameter("include-all-sites", includeAllSites);
    }
  }

  /**
   * Returns the handle web address mappings.
   *
   * @return handle web address mappings or <code>null</code> to indicate that
   *     the parameter is not set.
   */
  public Boolean getWithMappings() {
    return withMappings;
  }

  /**
   * Sets the handle web address mappings.
   *
   * @param withMappings handle web address mappings or <code>null</code> to
   *     remove this parameter if set.
   */
  public void setWithMappings(Boolean withMappings) {
    // check if setting to existing value
    if (this.withMappings == null ? withMappings != null :
        !this.withMappings.equals(withMappings)) {
      // set to new value for customer parameter
      this.withMappings = withMappings;
      setStringCustomParameter("with-mappings",
          withMappings == null ? null : withMappings.toString());
    }
  }

}

