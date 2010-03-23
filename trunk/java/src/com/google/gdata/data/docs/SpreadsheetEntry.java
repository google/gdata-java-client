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


package com.google.gdata.data.docs;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * An entry representing a single spreadsheet within a 
 * {@link DocumentListFeed}.
 *
 * 
 * 
 */
@Kind.Term(SpreadsheetEntry.KIND)
public class SpreadsheetEntry extends DocumentListEntry {

  /**
   * Label for category.
   */
  public static final String LABEL = "spreadsheet";
  
  /**
   * Kind category term used to label the entries which are
   * of document type.
   */
  public static final String KIND = DocumentListFeed.DOCUMENT_NAMESPACE 
      + "#" + SpreadsheetEntry.LABEL;

  /**
   * Category used to label entries that contain spreadsheet data.
   */
  public static final Category CATEGORY =
    new Category(com.google.gdata.util.Namespaces.gKind, KIND, LABEL);

  /**
   * Constructs a new uninitialized entry, to be populated by the
   * GData parsers.
   */
  public SpreadsheetEntry() {
    super();
    getCategories().remove(DocumentListEntry.CATEGORY);
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public SpreadsheetEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
  }

  /**
   * Gets the URL for this spreadsheet's worksheets feed.
   * You can then create a query using this URL to query this worksheet's
   * sheets.
   * 
   * @return a URL to get a feed of worksheets
   */
  public URL getWorksheetFeedUrl() {
    Link feedLink = this.getLink(
        com.google.gdata.data.spreadsheet.Namespaces.WORKSHEETS_LINK_REL, 
        Link.Type.ATOM);
    try {
      return new URL(feedLink.getHref());
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
    }
  }

  /**
   * Gets all worksheet entries that are part of this spreadsheet.
   * 
   * You must be online for this to work.
   * 
   * @return the list of worksheet entries
   */
  public List<WorksheetEntry> getWorksheets()
      throws IOException, ServiceException {
    WorksheetFeed feed = state.service.getFeed(getWorksheetFeedUrl(),
        WorksheetFeed.class);
    return feed.getEntries();
  }

  /**
   * Gets the first worksheet in the spreadsheet.
   * 
   * This is very useful if your spreadsheet only has one worksheet.
   * 
   * @return the first worksheet
   */
  public WorksheetEntry getDefaultWorksheet()
      throws IOException, ServiceException {
    Link feedLink = this.getLink(
        com.google.gdata.data.spreadsheet.Namespaces.WORKSHEETS_LINK_REL, 
        Link.Type.ATOM);
    String url = feedLink.getHref() + "/default";
    return state.service.getEntry(new URL(url), WorksheetEntry.class);
  }
}
