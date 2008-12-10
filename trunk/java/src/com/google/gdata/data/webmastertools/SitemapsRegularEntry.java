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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.ValueConstruct;

/**
 * This represents a single regular sitemap entry in a list of sitemaps. It
 * inherits common sitemap entry properties and adds a sitemap type property
 * that classifies the sitemap.
 *
 * 
 */
@Kind.Term(Namespaces.KIND_SITEMAP_REGULAR)
public class SitemapsRegularEntry extends SitemapsEntry<SitemapsRegularEntry> {

  private static final String SITEMAP_TYPE = "sitemap-type";

  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITEMAP_REGULAR);  

  /**
   * Constructs a new SitemapsRegularEntry instance
   */
  public SitemapsRegularEntry() {
    super();
    this.getCategories().add(CATEGORY);
  }
  
  /**
   * Constructs a new entry by doing a copy from another BaseEntry instance.
   */
  public SitemapsRegularEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }


  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a SitemapsRegularEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    
    extProfile.declare(
        SitemapsRegularEntry.class,
        ExtensionDescription.getDefaultDescription(SitemapType.class));
  }  

  /**
   * Sets sitemap type for a regular sitemap.
   */
  public void setSitemapType(String value) {
    SitemapType type = getExtension(SitemapType.class);
    if (type == null) {
      type = new SitemapType();
      setExtension(type);
    }

    type.setValue(value);
  }

  /**
   * Gets sitemap type of a regular sitemap.
   */
  public String getSitemapType() {
    SitemapType type = getExtension(SitemapType.class);
    if (type == null)
      return null;

    return type.getValue();
  }
  
  /**
   * We validate that sitemap type is set.
   */
  @Override
  protected void validate() throws IllegalStateException {
    super.validate();
    
    if (getSitemapType() == null) {
      throw new IllegalStateException(SITEMAP_TYPE
          + " is not set"); // COV_NF_LINE
    }
  }
  
  /**
   * Represents a single <sitemap-type> node that defines sitemap type.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_TYPE)
  public static class SitemapType extends ValueConstruct {
    public SitemapType() {
      super(Namespaces.WT_NAMESPACE, SITEMAP_TYPE, null);
    }
  }  
}
