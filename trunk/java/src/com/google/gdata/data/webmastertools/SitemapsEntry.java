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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ValueConstruct;

/**
 * Defines an abstract base class for sitemap entry. It implements support for
 * common sitemap entry parameters and relies on the derived classes to add ones
 * that are specific to those types.
 *
 * 
 * @param <E> sitemaps entry implementation that derives from this class.
 */
public class SitemapsEntry<E extends SitemapsEntry<E>> extends BaseEntry<E> {

  private static final String SITEMAP_STATUS = "sitemap-status";
  private static final String SITEMAP_DOWNLOADED = "sitemap-last-downloaded";
  private static final String SITEMAP_URL_COUNT = "sitemap-url-count";

  /**
   * Constructs a new SitemapsEntry instance
   */
  public SitemapsEntry() {
    super();
  }
  
  /**
   * Constructs a new entry by doing a copy from another BaseEntry instance.
   */
  public SitemapsEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }  

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a SitemapsEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    extProfile.declare(
        SitemapsEntry.class,
        ExtensionDescription.getDefaultDescription(SitemapStatus.class));

    extProfile.declare(
        SitemapsEntry.class,
        ExtensionDescription.getDefaultDescription(LastDownloaded.class));

    extProfile.declare(
        SitemapsEntry.class,
        ExtensionDescription.getDefaultDescription(UrlCount.class));
    
    extProfile.setAutoExtending(true);
  }

  /**
   * Set sitemap status.
   *
   * @param value specifies sitemap status.
   */
  public void setSitemapStatus(String value) {
    SitemapStatus status = getExtension(SitemapStatus.class);
    if (status == null) {
      status = new SitemapStatus();
      setExtension(status);
    }

    status.setValue(value);
  }

  /**
   * Returns sitemap status.
   */
  public String getSitemapStatus() {
    SitemapStatus status = getExtension(SitemapStatus.class);
    if (status == null) {
      return null;
    }

    return status.getValue();
  }

  /**
   * Set last sitemap download time.
   */
  public void setSitemapDownloadTime(DateTime value) {
    LastDownloaded time = getExtension(LastDownloaded.class);
    if (time == null) {
      time = new LastDownloaded();
      setExtension(time);
    }

    time.setDateTime(value);
  }

  /**
   * Returns last sitemap download time.
   */
  public DateTime getSitemapDownloadTime() {
    LastDownloaded time = getExtension(LastDownloaded.class);
    if (time == null) {
      return null;
    }

    return time.getDateTime();
  }

  /**
   * Set sitemap URL count.
   */
  public void setSitemapUrlCount(int value) {
    UrlCount count = getExtension(UrlCount.class);
    if (count == null) {
      count = new UrlCount();
      setExtension(count);
    }

    count.setIntValue(value);
  }

  /**
   * Returns sitemap URL count.
   */
  public int getSitemapUrlCount() {
    UrlCount count = getExtension(UrlCount.class);
    if (count == null) {
      return 0;
    }

    return count.getIntValue();
  }

  /**
   * Represents <sitemap-status> node.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_STATUS)  
  public static class SitemapStatus extends ValueConstruct {
    public SitemapStatus() {
      super(Namespaces.WT_NAMESPACE, SITEMAP_STATUS, null);
    }
  }
  
  /**
   * Represents a single <sitemap-last-downloaded> node, that is a
   * date/time value.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_DOWNLOADED)
  public static class LastDownloaded extends DateTimeValueConstruct {
    public LastDownloaded() {
      super(SITEMAP_DOWNLOADED);
    }
  }
  
  /**
   * Represents a single <sitemap-url-count> node, that is an
   * int value.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_URL_COUNT)
  public static class UrlCount extends IntValueConstruct {
    public UrlCount() {
      super(SITEMAP_URL_COUNT);
    }
  }
}
