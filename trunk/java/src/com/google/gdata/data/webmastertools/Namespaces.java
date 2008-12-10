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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.Category;

/**
 * Webmaster Tools XML namespace definitions. Defines constants that are used by
 * Webmaster Tools GData classes that do actual XML formatting.
 *
 * 
 */
public final class Namespaces {

  /** Namespace URI for Webmaster Tools GData extensions */
  public static final String WT
      = "http://schemas.google.com/webmasters/tools/2007";

  /** Namespace prefix for Webmaster Tools GData extensions */
  public static final String WT_ALIAS = "wt";

  /** Prefix for Webmaster Tools GData extensions */
  public static final String WT_PREFIX = WT + "#";

  /**
   * Webmaster tools GData namespace.
   */
  public static final XmlNamespace WT_NAMESPACE =
      new XmlNamespace(
          WT_ALIAS,
          WT);

  /**
   * Same as above, for compatibility with automatically generated feeds.
   */
  public static final XmlNamespace WT_NS = WT_NAMESPACE;

  /**
   * Category term linked to {@link SitesEntry}.
   */
  public static final String KIND_SITE_INFO
      = WT + "#site-info";

  /**
   * Category term linked to {@link SitesFeed}.
   */
  public static final String KIND_SITES_FEED
      = WT + "#sites-feed";

  /**
   * Category term linked to {@link SitemapsFeed}.
   */
  public static final String KIND_SITEMAPS_FEED
      = WT + "#sitemaps-feed";

  /**
   * Category term linked to {@link SitemapsEntry}. Represents regular sitemap.
   */
  public static final String KIND_SITEMAP_REGULAR
      = WT + "#sitemap-regular";

  /**
   * Category term linked to {@link SitemapsEntry}. Represents mobile sitemap.
   */
  public static final String KIND_SITEMAP_MOBILE
      = WT + "#sitemap-mobile";

  /**
   * Category term linked to {@link SitemapsEntry}. Represents news sitemap.
   */
  public static final String KIND_SITEMAP_NEWS
      = WT + "#sitemap-news";

  /**
   * Category term linked to {@link MessageEntry}.
   */
  public static final String KIND_MESSAGE
      = WT + "#message";

  /**
   * Category term linked to {@link MessagesFeed}.
   */
  public static final String KIND_MESSAGES_FEED
      = WT + "#messages-feed";

  /**
   * Helper method to create {@link Category} objects.
   *
   * @param kindTerm category term to use.
   * @return newly created {@link Category} object.
   */
  public static Category createCategory(String kindTerm) {
    return new Category(
        com.google.gdata.util.Namespaces.gKind,
        kindTerm);
  }
}
