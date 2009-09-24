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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.util.Namespaces;

/**
 * Describes a file cabinet page entry.
 *
 * 
 */
@Kind.Term(FileCabinetPageEntry.KIND)
public class FileCabinetPageEntry extends BasePageEntry<FileCabinetPageEntry> {

  /**
   * Filecabinet file cabinet page kind kind term value.
   */
  public static final String KIND = SitesNamespace.SITES_PREFIX + "filecabinet";

  /**
   * Filecabinet file cabinet page kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "filecabinet");

  /**
   * Default mutable constructor.
   */
  public FileCabinetPageEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public FileCabinetPageEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(FileCabinetPageEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(FileCabinetPageEntry.class,
        new ExtensionDescription(FeedLink.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "feedLink", true, false, false));
    new FeedLink().declareExtensions(extProfile);
  }

  /**
   * Returns the A feedLink representing the files in this file cabinet.
   *
   * @return A feedLink representing the files in this file cabinet
   */
  public FeedLink getFeedLink() {
    return getExtension(FeedLink.class);
  }

  /**
   * Sets the A feedLink representing the files in this file cabinet.
   *
   * @param feedLink A feedLink representing the files in this file cabinet or
   *     <code>null</code> to reset
   */
  public void setFeedLink(FeedLink feedLink) {
    if (feedLink == null) {
      removeExtension(FeedLink.class);
    } else {
      setExtension(feedLink);
    }
  }

  /**
   * Returns whether it has the A feedLink representing the files in this file
   * cabinet.
   *
   * @return whether it has the A feedLink representing the files in this file
   *     cabinet
   */
  public boolean hasFeedLink() {
    return hasExtension(FeedLink.class);
  }

  /**
   * Returns the attachments sites link.
   *
   * @return Attachments sites link or {@code null} for none.
   */
  public Link getAtomAttachmentsLink() {
    return getLink(SitesLink.Rel.ATTACHMENTS, Link.Type.ATOM);
  }

  /**
   * Returns the replies sites link.
   *
   * @return Replies sites link or {@code null} for none.
   */
  public Link getAtomRepliesLink() {
    return getLink(SitesLink.Rel.REPLIES, Link.Type.ATOM);
  }

  /**
   * Returns the attachments sites link.
   *
   * @return Attachments sites link or {@code null} for none.
   */
  public Link getHtmlAttachmentsLink() {
    return getLink(SitesLink.Rel.ATTACHMENTS, Link.Type.HTML);
  }

  /**
   * Returns the replies sites link.
   *
   * @return Replies sites link or {@code null} for none.
   */
  public Link getHtmlRepliesLink() {
    return getLink(SitesLink.Rel.REPLIES, Link.Type.HTML);
  }

  /**
   * Returns the parent sites link.
   *
   * @return Parent sites link or {@code null} for none.
   */
  public Link getParentLink() {
    return getLink(SitesLink.Rel.PARENT, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{FileCabinetPageEntry " + super.toString() + "}";
  }

}

