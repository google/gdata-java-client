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


package sample.spreadsheet;

import com.google.gdata.data.Person;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.FeedURLFactory;

import com.google.gdata.client.spreadsheet.CellQuery;

import com.google.gdata.data.Feed;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.CellFeed;

import com.google.gdata.data.Entry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.CellEntry;

import com.google.gdata.data.spreadsheet.Cell;

import sample.util.SimpleCommandLineParser;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * An application that serves as a sample to show how the SpreadhseetService
 * can be used to obtain an index of spreadsheets with author and worksheets.
 *
 * 
 */
public class IndexClient {

  private SpreadsheetService service;

  private FeedURLFactory factory;

  /**
   * Creates a client object for which the provided username and password
   * produces a valid authentication.
   *
   * @param username the Google service user name
   * @param password the corresponding password for the user name
   * @throws Exception if error is encountered, such as invalid username and
   * password pair
   */
  public IndexClient(String username, String password) throws Exception {
    factory = FeedURLFactory.getDefault();
    service = new SpreadsheetService("gdata-sample-spreadhsheetindex");
    service.setUserCredentials(username, password);
  }

  /**
   * Retrieves the spreadsheets that the authenticated user has access to.
   *
   * @return a list of spreadsheet entries
   * @throws Exception if error in retrieving the spreadsheet information
   */
  public List<SpreadsheetEntry> getSpreadsheetEntries() throws Exception {
    SpreadsheetFeed feed = service.getFeed(
        factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
    return feed.getEntries();
  }

  /**
   * Retrieves the worksheet entries from a spreadsheet entry.
   *
   * @param spreadsheet the spreadsheet entry containing the worksheet entries
   * @return a list of worksheet entries
   * @throws Exception if error in retrieving the spreadsheet information
   */
  public List<WorksheetEntry> getWorksheetEntries(SpreadsheetEntry spreadsheet)
      throws Exception {
    return spreadsheet.getWorksheets();
  }

  /**
   * Retrieves the columns headers from the cell feed of the worksheet
   * entry.
   *
   * @param worksheet worksheet entry containing the cell feed in question
   * @return a list of column headers
   * @throws Exception if error in retrieving the spreadsheet information
   */
  public List<String> getColumnHeaders(WorksheetEntry worksheet)
      throws Exception {
    List<String> headers = new ArrayList<String>();

    // Get the appropriate URL for a cell feed
    URL cellFeedUrl = worksheet.getCellFeedUrl();

    // Create a query for the top row of cells only (1-based)
    CellQuery cellQuery = new CellQuery(cellFeedUrl);
    cellQuery.setMaximumRow(1);

    // Get the cell feed matching the query
    CellFeed topRowCellFeed = service.query(cellQuery, CellFeed.class);

    // Get the cell entries fromt he feed
    List<CellEntry> cellEntries = topRowCellFeed.getEntries();
    for (CellEntry entry : cellEntries) {

      // Get the cell element from the entry
      Cell cell = entry.getCell();
      headers.add(cell.getValue());
    }

    return headers;
  }

  /**
   * Prints the usage of this application.
   */
  private static void usage() {
    System.out.println("Usage: java IndexClient --username [user] " +
        "--password [pass] [--authors] [--worksheets] [--headers]");
    System.out.println("\nA simple application that uses the provided Google\n"
        + "Account username and password to create\n"
        + "an index of the user's spreadsheets against\n"
        + "the user's Google Spreadsheet account.\n");
  }

  /**
   * Main entry point.  Parses arguments and creates and invokes the
   * IndexClient.
   */
  public static void main(String[] args) throws Exception {

    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "passwd", "pw", "p");
    boolean help = parser.containsKey("help", "h");
    if (help || (username == null) || (password == null)) {
      usage();
      System.exit(1);
    }

    boolean author = parser.containsKey("author", "a");
    boolean columns = parser.containsKey("headers", "header", "h");
    boolean worksheets = parser.containsKey("worksheets", "worksheet", "w");

    IndexClient client = new IndexClient(username, password);

    for (SpreadsheetEntry spreadsheet : client.getSpreadsheetEntries()) {
      
      System.out.print(spreadsheet.getTitle().getPlainText());
      if (author) {
        for (Person person : spreadsheet.getAuthors()) {
          System.out.println(" - " + person.getName());
        }
      } else {
        System.out.println();
      }  //authors (or not)

      if (worksheets || columns) {
        List<WorksheetEntry> entries = client.getWorksheetEntries(spreadsheet);
        for (WorksheetEntry worksheet : entries) {
          System.out.println("\t" + worksheet.getTitle().getPlainText());

          if (columns) {
            List<String> headers = client.getColumnHeaders(worksheet);
            for (String header : headers) {
              System.out.println("\t\t" + header);
            }
          }  // columns

        }
      }  // worksheets

    }  // spreadsheets
    
  }
}
