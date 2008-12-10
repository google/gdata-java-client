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
 * This represents a single Mobile sitemap entry in a list of sitemaps.
 * {@link SitemapsMobileEntry#setMarkupLanguage} must be called to set the
 * markup language.
 *
 * 
 */
@Kind.Term(Namespaces.KIND_SITEMAP_MOBILE)
public class SitemapsMobileEntry extends SitemapsEntry<SitemapsMobileEntry> {

  private static final String MOBILE_MARKUP_LANG
      = "sitemap-mobile-markup-language";
  
  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITEMAP_MOBILE);

  /**
   * Constructs a new SitemapsMobileEntry instance
   */
  public SitemapsMobileEntry() {
    super();
    this.getCategories().add(CATEGORY);
  }
  
  /**
   * Constructs a new entry by doing a copy from another BaseEntry instance.
   */
  public SitemapsMobileEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }  

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a SitemapsMobileEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(
        SitemapsMobileEntry.class,
        ExtensionDescription.getDefaultDescription(MarkupLanguage.class));
  }

  /**
   * Set mark-up language for mobile sitemap.
   */
  public void setMarkupLanguage(String value) {
    MarkupLanguage lang = getExtension(MarkupLanguage.class);
    if (lang == null) {
      lang = new MarkupLanguage();
      setExtension(lang);
    }

    lang.setValue(value);
  }

  /**
   * Returns mark-up language for mobile sitemap.
   */
  public String getMarkupLanguage() {
    MarkupLanguage lang = getExtension(MarkupLanguage.class);
    if (lang == null)
      return null;

    return lang.getValue();
  }

  /**
   * We validate that markup language is set.
   */
  @Override
  protected void validate() throws IllegalStateException {
    super.validate();
    if (getMarkupLanguage() == null) {
      throw new IllegalStateException(MOBILE_MARKUP_LANG + " is not set");
    }
  }

  /**
   * Represents a single <sitemap-mobile-markup-language> node.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = MOBILE_MARKUP_LANG)
  public static class MarkupLanguage extends ValueConstruct {
    public MarkupLanguage() {
      super(Namespaces.WT_NAMESPACE, MOBILE_MARKUP_LANG, null);
    }
  }
}
