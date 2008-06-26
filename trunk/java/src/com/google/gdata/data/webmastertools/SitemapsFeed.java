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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;

/**
 * The SitemapsFeed class customizes the generic BaseFeed class to define
 * a feed of sitemaps that Webmaster Tools user has for a given site. It also
 * lists available options for News and Mobile sitemap at the feed level. User
 * is supposed to pick one of the options when submitting Mobile or News
 * sitemap.
 * 
 * Example:
 *   <pre class="code">
 *     <feed xmlns="http://www.w3.org/2005/Atom"
 *           xmlns:wt="http://schemas.google.org/webmasters/tools/2007">
 *       ...
 *       <category scheme='http://schemas.google.com/g/2005#kind' 
 *        term='http://schemas.google.org/webmasters/tools/2007#sitemap-types'/>
 *       <wt:sitemap-mobile>
 *         <wt:markup-language>HTML</wt:markup-language>
 *         <wt:markup-language>WAP</wt:markup-language>
 *       </wt:sitemap-mobile>
 *       <wt:sitemap-news>
 *         <wt:publication-label>Value1</wt:publication-label>
 *         <wt:publication-label>Value2</wt:publication-label>
 *         <wt:publication-label>Value3</wt:publication-label>
 *       </wt:sitemap-news>
 *       <entry>..</entry>
 *       ...
 *     <feed>
 *   </pre>
 *
 * 
 */
public class SitemapsFeed extends BaseFeed<SitemapsFeed, SitemapsEntry> {
  
  /* 
   * The declaration above generates warning because it should be
   * SitemapsEntry<?> but then there is a problem with passing
   * SitemapsEntry<?>.class to the super constructor.
   */ 

  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITEMAPS_FEED);

  /**
   * Constructs a new {@code SitemapsFeed} instance that is parameterized to
   * contain {@code SitemapsEntry} instances.
   */
  public SitemapsFeed() {
    super(SitemapsEntry.class);
    this.getCategories().add(CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Add any feed-level extension declarations here.
    super.declareExtensions(extProfile);
    
    extProfile.declare(
        SitemapsFeed.class,
        ExtensionDescription.getDefaultDescription(SitemapMobile.class));
    extProfile.declare(
        SitemapsFeed.class,
        ExtensionDescription.getDefaultDescription(SitemapNews.class));

    extProfile.declareAdditionalNamespace(Namespaces.WT_NAMESPACE);
  }

  /**
   * Sets the mobile sitemap options. The user is supposed to pick one of the
   * options specified in this sitemap when submitting a mobile sitemap.
   */
  public SitemapMobile getSitemapMobile() {
    return getExtension(SitemapMobile.class);
  }

  /**
   * Sets the mobile sitemap options. The user is supposed to pick one of the
   * options specified in this sitemap when submitting a mobile sitemap.
   */
  public void setSitemapMobile(SitemapMobile sitemap) {
    setExtension(sitemap);
  }

  /**
   * Sets the News sitemap options. The user is supposed to pick one of the
   * options specified in this sitemap when submitting a News sitemap.
   */
  public SitemapNews getSitemapNews() {
    return getExtension(SitemapNews.class);
  }

  /**
   * Sets the News sitemap options. The user is supposed to pick one of the
   * options specified in this sitemap when submitting a News sitemap.
   */
  public void setSitemapNews(SitemapNews sitemap) {
    setExtension(sitemap);
  }
}
