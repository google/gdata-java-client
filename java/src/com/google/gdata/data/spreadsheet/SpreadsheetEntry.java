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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.OutOfLineContent;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * One spreadsheet, when listing all spreadsheets you have access to.
 *
 * 
 */
@Kind.Term(SpreadsheetEntry.KIND)
public class SpreadsheetEntry extends BaseEntry<SpreadsheetEntry> {

  /**
   * Kind category term used to label the entries that contains spreadsheet
   * data.
   */
  public static final String KIND = Namespaces.gSpreadPrefix + "spreadsheet";

  /**
   * Category used to label entries that contain spreadsheet data.
   */
  public static final Category CATEGORY =
    new Category(Namespaces.gSpread, KIND);

  /**
   * Constructs a new uninitialized entry, to be populated by the
   * GData parsers.
   */
  public SpreadsheetEntry() {
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public SpreadsheetEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }

  /**
   * Gets the link with which you can open up the spreadsheet in a Web
   * browser, the full Google Spreadsheets user interface.
   *
   * @return a link to open up the web browser with
   */
  public Link getSpreadsheetLink() {
    return super.getHtmlLink();
  }

  /**
   * Gets the URL for this spreadsheet's worksheets feed.
   *
   * You can then create a query using this URL to query this worksheet's
   * sheets.
   *
   *
   * @return a URL to get a feed of worksheets
   */
  public URL getWorksheetFeedUrl() {
    try {
      return new URL(getWorksheetFeedUrlString());
    } catch(MalformedURLException e) {
      throw new RuntimeException("Error in GData server", e);
    }
  }

  /**
   * Gets the URL for this spreadseet's worksheets feed as a string.
   *
   * <p>This checks for version compatibility also.
   *
   * and call that from here, just like in WorksheetEntry.
   *
   * @return a string representing the URL for the worksheets feed
   */
  private String getWorksheetFeedUrlString() {
    Version spreadsheetVersion = state.service.getProtocolVersion();

    if (spreadsheetVersion.isCompatible(SpreadsheetService.Versions.V1)) {
      Link feedLink = this.getLink(Namespaces.WORKSHEETS_LINK_REL,
                                   Link.Type.ATOM);
      return feedLink.getHref();
    } else { // must be SpreadsheetService.Versions.V2; only 2 versions for now
      return ((OutOfLineContent)(this.getContent())).getUri();
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
    String url = getWorksheetFeedUrlString() + "/default";
    return state.service.getEntry(new URL(url), WorksheetEntry.class);
  }

  /**
   * Gets the non-user-friendly key that is used to access the spreadsheet.
   *
   * This is the key that can be used to open the spreadsheet in a Web
   * browser, such as, http://spreadsheets.google.com/ccc?key={key}.
   *
   * @return the Google Spreadsheets key, in "o10110101.1010101" format
   */
  public String getKey() {
    String result = state.id;
    int position = result.lastIndexOf("/");

    if (position > 0) {
      result = result.substring(position + 1);
    }

    return result;
  }


  /**
   * Declares extensions (although Spreadsheet Kind currently has none).
   */
  public void declareExtensions(ExtensionProfile extProfile) {

    // No special extensions.
  }
}
