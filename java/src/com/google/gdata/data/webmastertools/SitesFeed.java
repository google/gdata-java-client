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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Category;

/**
 * The SitesFeed class customizes the generic BaseFeed class to define
 * a feed of web sites that Webmaster Tools user has.
 *
 * 
 */
public class SitesFeed extends BaseFeed<SitesFeed, SitesEntry> {

  /**
   * Constructs a new {@code SitesFeed} instance that is parameterized to
   * contain {@code SitesEntry} instances.
   */
  public SitesFeed() {
    super(SitesEntry.class);
    this.getCategories().add(CATEGORY);
  }

  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITES_FEED);

  /**
   * Declares feed extensions. We only add Webmaster Tools namespace to the
   * feed, all the other extensions are defined on the feed entry level, see
   * {@link SitesEntry}. 
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Add any feed-level extension declarations here.
    super.declareExtensions(extProfile);
    extProfile.declareAdditionalNamespace(Namespaces.WT_NAMESPACE);
  }
}
