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
 * This represents a single News sitemap entry in a list of sitemaps.
 * {@link SitemapsNewsEntry#setPublicationLabel} must be called to set the
 * News sitemap publication label.
 *
 * 
 */
@Kind.Term(Namespaces.KIND_SITEMAP_NEWS)
public class SitemapsNewsEntry extends SitemapsEntry<SitemapsNewsEntry> {

  private static final String NEWS_PUBLICATION_LABEL
      = "sitemap-news-publication-label";
  
  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITEMAP_NEWS);  

  /**
   * Constructs a new SitemapsNewsEntry instance
   */
  public SitemapsNewsEntry() {
    super();
    this.getCategories().add(CATEGORY);
  }
  
  /**
   * Constructs a new entry by doing a copy from another BaseEntry instance.
   */
  public SitemapsNewsEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }  

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a SitemapsNewsEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(
        SitemapsNewsEntry.class,
        ExtensionDescription.getDefaultDescription(PublicationLabel.class));
  }

  /**
   * Set publication label for News sitemap.
   */
  public void setPublicationLabel(String value) {
    PublicationLabel label = getExtension(PublicationLabel.class);
    if (label == null) {
      label = new PublicationLabel();
      setExtension(label);
    }

    label.setValue(value);
  }

  /**
   * Returns publication label for News sitemap.
   */
  public String getPublicationLabel() {
    PublicationLabel label = getExtension(PublicationLabel.class);
    if (label == null) {
      return null;
    }

    return label.getValue();
  }

  /**
   * We validate that publication label is set.
   */
  @Override
  protected void validate() throws IllegalStateException {
    super.validate();
    if (getPublicationLabel() == null) {
      throw new IllegalStateException(NEWS_PUBLICATION_LABEL + " is not set");
    }
  }

  /**
   * Represents <sitemap-news-publication-label> node, that is a publication
   * label for News sitemap.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = NEWS_PUBLICATION_LABEL)
  public static class PublicationLabel extends ValueConstruct {
    public PublicationLabel() {
      super(Namespaces.WT_NAMESPACE, NEWS_PUBLICATION_LABEL, null);
    }
  }
}
