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
import com.google.gdata.data.spreadsheet.Data;
import com.google.gdata.data.spreadsheet.Header;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.util.Namespaces;

/**
 * Describes a list page entry.
 *
 * 
 */
@Kind.Term(ListPageEntry.KIND)
public class ListPageEntry extends BasePageEntry<ListPageEntry> {

  /**
   * Listpage list page kind kind term value.
   */
  public static final String KIND = SitesNamespace.SITES_PREFIX + "listpage";

  /**
   * Listpage list page kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "listpage");

  /**
   * Default mutable constructor.
   */
  public ListPageEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ListPageEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ListPageEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ListPageEntry.class, Data.getDefaultDescription(true,
        false));
    new Data().declareExtensions(extProfile);
    extProfile.declare(ListPageEntry.class,
        new ExtensionDescription(FeedLink.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "feedLink", true, false, false));
    new FeedLink().declareExtensions(extProfile);
    extProfile.declare(ListPageEntry.class, Header.getDefaultDescription(true,
        false));
    extProfile.declare(ListPageEntry.class,
        Worksheet.getDefaultDescription(true, false));
  }

  /**
   * Returns the data region of a table.
   *
   * @return data region of a table
   */
  public Data getData() {
    return getExtension(Data.class);
  }

  /**
   * Sets the data region of a table.
   *
   * @param data data region of a table or <code>null</code> to reset
   */
  public void setData(Data data) {
    if (data == null) {
      removeExtension(Data.class);
    } else {
      setExtension(data);
    }
  }

  /**
   * Returns whether it has the data region of a table.
   *
   * @return whether it has the data region of a table
   */
  public boolean hasData() {
    return hasExtension(Data.class);
  }

  /**
   * Returns the A feedLink element representing the feed for list items in this
   * page.
   *
   * @return A feedLink element representing the feed for list items in this
   *     page
   */
  public FeedLink getFeedLink() {
    return getExtension(FeedLink.class);
  }

  /**
   * Sets the A feedLink element representing the feed for list items in this
   * page.
   *
   * @param feedLink A feedLink element representing the feed for list items in
   *     this page or <code>null</code> to reset
   */
  public void setFeedLink(FeedLink feedLink) {
    if (feedLink == null) {
      removeExtension(FeedLink.class);
    } else {
      setExtension(feedLink);
    }
  }

  /**
   * Returns whether it has the A feedLink element representing the feed for
   * list items in this page.
   *
   * @return whether it has the A feedLink element representing the feed for
   *     list items in this page
   */
  public boolean hasFeedLink() {
    return hasExtension(FeedLink.class);
  }

  /**
   * Returns the header row.
   *
   * @return header row
   */
  public Header getHeader() {
    return getExtension(Header.class);
  }

  /**
   * Sets the header row.
   *
   * @param header header row or <code>null</code> to reset
   */
  public void setHeader(Header header) {
    if (header == null) {
      removeExtension(Header.class);
    } else {
      setExtension(header);
    }
  }

  /**
   * Returns whether it has the header row.
   *
   * @return whether it has the header row
   */
  public boolean hasHeader() {
    return hasExtension(Header.class);
  }

  /**
   * Returns the worksheet where the table lives.
   *
   * @return worksheet where the table lives
   */
  public Worksheet getWorksheet() {
    return getExtension(Worksheet.class);
  }

  /**
   * Sets the worksheet where the table lives.
   *
   * @param worksheet worksheet where the table lives or <code>null</code> to
   *     reset
   */
  public void setWorksheet(Worksheet worksheet) {
    if (worksheet == null) {
      removeExtension(Worksheet.class);
    } else {
      setExtension(worksheet);
    }
  }

  /**
   * Returns whether it has the worksheet where the table lives.
   *
   * @return whether it has the worksheet where the table lives
   */
  public boolean hasWorksheet() {
    return hasExtension(Worksheet.class);
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
    return "{ListPageEntry " + super.toString() + "}";
  }

}

