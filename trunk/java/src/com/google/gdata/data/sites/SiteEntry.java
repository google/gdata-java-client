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


package com.google.gdata.data.sites;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;

import java.util.List;

/**
 * An entry representing a site.
 *
 * 
 */
public class SiteEntry extends BaseEntry<SiteEntry> {

  /**
   * Default mutable constructor.
   */
  public SiteEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public SiteEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(SiteEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(SiteEntry.class, SitesLink.getDefaultDescription(true,
        true));
    extProfile.declare(SiteEntry.class, SiteName.class);
    extProfile.declare(SiteEntry.class, Theme.class);
  }

  /**
   * Returns the site name.
   *
   * @return site name
   */
  public SiteName getSiteName() {
    return getExtension(SiteName.class);
  }

  /**
   * Sets the site name.
   *
   * @param siteName site name or <code>null</code> to reset
   */
  public void setSiteName(SiteName siteName) {
    if (siteName == null) {
      removeExtension(SiteName.class);
    } else {
      setExtension(siteName);
    }
  }

  /**
   * Returns whether it has the site name.
   *
   * @return whether it has the site name
   */
  public boolean hasSiteName() {
    return hasExtension(SiteName.class);
  }

  /**
   * Returns the theme.
   *
   * @return theme
   */
  public Theme getTheme() {
    return getExtension(Theme.class);
  }

  /**
   * Sets the theme.
   *
   * @param theme theme or <code>null</code> to reset
   */
  public void setTheme(Theme theme) {
    if (theme == null) {
      removeExtension(Theme.class);
    } else {
      setExtension(theme);
    }
  }

  /**
   * Returns whether it has the theme.
   *
   * @return whether it has the theme
   */
  public boolean hasTheme() {
    return hasExtension(Theme.class);
  }

  /**
   * Returns the link that provides the URI that can be used to post new entries
   * to the feed.
   *
   * @return Link that provides the URI that can be used to post new entries to
   *     the feed or {@code null} for none.
   */
  public Link getEntryPostLink() {
    return getLink(Link.Rel.ENTRY_POST, Link.Type.ATOM);
  }

  /**
   * Returns the source sites link.
   *
   * @return Source sites link or {@code null} for none.
   */
  public Link getSourceLink() {
    return getLink(SitesLink.Rel.SOURCE, Link.Type.ATOM);
  }

  /**
   * Returns the webAddressMapping sites link.
   *
   * @return WebAddressMapping sites link.
   */
  public List<Link> getWebAddressMappingLinks() {
    return getLinks(SitesLink.Rel.WEBADDRESSMAPPING, Link.Type.HTML);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{SiteEntry " + super.toString() + "}";
  }

}

