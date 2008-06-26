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


package sample.spreadsheet.gui;

import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * This class helps you access your spreadsheet like a list or like a
 * database.
 *
 *
 * In this model, each row is treated like a separate entry, with multiple
 * fields.  Each field is named based on the top row of your speradsheet.
 *
 * 
 */
public class ListFeedHelper {

  /** The SpreadsheetService to contact Google with. */
  private SpreadsheetService service;

  /** List feed URL. */
  private URL feedUrl;


  /**
   * Creates a list feed helper for a particular URL.
   *
   * @param inService the SpreadsheetService to contact Google with
   * @param inFeedUrl the URL of the list feed; to get this URL, you can
   *        use SpreadsheetFeedHelper, or if you have a WorksheetEntry
   *        you can use its method to get the list feed URL
   */
  public ListFeedHelper(SpreadsheetService inService, URL inFeedUrl) {
    service = inService;
    feedUrl = inFeedUrl;
  }

  /**
   * Adds a new list entry to the spreadsheet.
   *
   * @param entry the new entry
   * @throws IOException if there was a problem contacting Google
   * @throws ServiceException if there was an error processnig the request
   */
  public ListEntry addListEntry(ListEntry entry)
      throws IOException, ServiceException {
    return service.insert(feedUrl, entry);
  }

  /**
   * Gets a list of all the list entries.
   *
   * @return a list of all non-empty rows in the spreadsheet
   * @throws IOException if there was a problem contacting Google
   * @throws ServiceException if there was an error processnig the request
   */
  public List<ListEntry> getAllListEntries()
      throws IOException, ServiceException {
    ListQuery query = new ListQuery(feedUrl);
    ListFeed feed = service.query(query, ListFeed.class);
    return feed.getEntries();
  }

  /**
   * Gets a list of all entries that match a full-text search.
   *
   * Full-text searches look for the keywords you specify, that may
   * occur in any order in the row.  Right now only the simplest syntax
   * is supported, as a convenience feature.
   *
   * @param search the search string like "adam technical writer"
   * @return all matching entries
   * @throws IOException if there was a problem contacting Google
   * @throws ServiceException if there was an error processnig the request
   */
  public List<ListEntry> getFullTextSearch(String search)
      throws IOException, ServiceException {
    ListQuery query = new ListQuery(feedUrl);
    query.setFullTextQuery(search);
    ListFeed feed = service.query(query, ListFeed.class);
    return feed.getEntries();
  }

  /**
   * Gets a list of all entries that match a structured query.
   *
   * Structured queries are in format:
   *    name = 'adam' and (job = 'technical writer' or job = 'artist')
   *
   * In short, it is case-insensitive, supporting both SQL-like and
   * C-like syntaxes for all varieties of new and seasoned programmers.
   *
   * @param structuredQuery the search string
   * @return all matching entries
   * @throws IOException if there was a problem contacting Google
   * @throws ServiceException if there was an error processnig the request
   */
  public List<ListEntry> getStructuredQuery(
       String structuredQuery)
       throws IOException, ServiceException {
    ListQuery query = new ListQuery(feedUrl);
    query.setFullTextQuery(structuredQuery);
    ListFeed feed = service.query(query, ListFeed.class);
    return feed.getEntries();
  }
}
