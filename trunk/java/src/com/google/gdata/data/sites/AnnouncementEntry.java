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
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.util.Namespaces;

/**
 * Describes an announcement entry.
 *
 * 
 */
@Kind.Term(AnnouncementEntry.KIND)
public class AnnouncementEntry extends BasePageEntry<AnnouncementEntry> {

  /**
   * Announcement announcement kind kind term value.
   */
  public static final String KIND = SitesNamespace.SITES_PREFIX +
      "announcement";

  /**
   * Announcement announcement kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "announcement");

  /**
   * Default mutable constructor.
   */
  public AnnouncementEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public AnnouncementEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
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
  public String toString() {
    return "{AnnouncementEntry " + super.toString() + "}";
  }

}

