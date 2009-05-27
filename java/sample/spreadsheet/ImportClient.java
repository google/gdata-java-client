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

import sample.util.SimpleCommandLineParser;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.Link;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An application that serves as a sample to show how the SpreadsheetService 
 * can be used to import delimited text file to a spreadsheet.
 *
 * 
 */
public class ImportClient {

  private SpreadsheetService service;

  private FeedURLFactory factory;

  public ImportClient() throws Exception {
    factory = FeedURLFactory.getDefault();
    service = new SpreadsheetService("gdata-sample-spreadsheetimport");
  }

  /**
   * Creates a client object for which the provided username and password
   * produces a valid authentication.
   *
   * @param username the Google service user name
   * @param password the corresponding password for the user name
   * @throws Exception if error is encountered, such as invalid username and
   * password pair
   */
  public ImportClient(String username, String password) throws Exception {
    this();
    service.setUserCredentials(username, password);
  }

  /**
   * Gets the SpreadsheetEntry for the first spreadsheet with that name
   * retrieved in the feed.
   *
   * @param spreadsheet the name of the spreadsheet
   * @return the first SpreadsheetEntry in the returned feed, so latest
   * spreadsheet with the specified name
   * @throws Exception if error is encountered, such as no spreadsheets with the
   * name
   */
  public SpreadsheetEntry getSpreadsheet(String spreadsheet)
      throws Exception {
    
      SpreadsheetQuery spreadsheetQuery 
        = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
      spreadsheetQuery.setTitleQuery(spreadsheet);
      SpreadsheetFeed spreadsheetFeed = service.query(spreadsheetQuery, 
          SpreadsheetFeed.class);
      List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
      if (spreadsheets.isEmpty()) {
        throw new Exception("No spreadsheets with that name");
      }

      return spreadsheets.get(0);
  }

  /**
   * Get the WorksheetEntry for the worksheet in the spreadsheet with the
   * specified name.
   *
   * @param spreadsheet the name of the spreadsheet
   * @param worksheet the name of the worksheet in the spreadsheet
   * @return worksheet with the specified name in the spreadsheet with the
   * specified name
   * @throws Exception if error is encountered, such as no spreadsheets with the
   * name, or no worksheet wiht the name in the spreadsheet
   */
  public WorksheetEntry getWorksheet(String spreadsheet, String worksheet) 
      throws Exception {

    SpreadsheetEntry spreadsheetEntry = getSpreadsheet(spreadsheet);

    WorksheetQuery worksheetQuery
      = new WorksheetQuery(spreadsheetEntry.getWorksheetFeedUrl());

    worksheetQuery.setTitleQuery(worksheet);
    WorksheetFeed worksheetFeed = service.query(worksheetQuery,
        WorksheetFeed.class);
    List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
    if (worksheets.isEmpty()) {
      throw new Exception("No worksheets with that name in spreadhsheet "
          + spreadsheetEntry.getTitle().getPlainText());
    }

    return worksheets.get(0);
  }

  /**
   * Clears all the cell entries in the worksheet.
   *
   * @param spreadsheet the name of the spreadsheet
   * @param worksheet the name of the worksheet
   * @throws Exception if error is encountered, such as bad permissions
   */
  public void purgeWorksheet(String spreadsheet, String worksheet) 
      throws Exception {

    WorksheetEntry worksheetEntry = getWorksheet(spreadsheet, worksheet);
    CellFeed cellFeed = service.getFeed(worksheetEntry.getCellFeedUrl(), 
        CellFeed.class);

    List<CellEntry> cells = cellFeed.getEntries();
    for (CellEntry cell : cells) {
      Link editLink = cell.getEditLink();
      service.delete(new URL(editLink.getHref()));
    }
  }
  
  /**
   * Inserts a cell entry in the worksheet.
   *
   * @param spreadsheet the name of the spreadsheet
   * @param worksheet the name of the worksheet
   * @param row the index of the row
   * @param column the index of the column
   * @param input the input string for the cell
   * @throws Exception if error is encountered, such as bad permissions
   */
  public void insertCellEntry(String spreadsheet, String worksheet,
      int row, int column, String input) throws Exception {
    
    URL cellFeedUrl = getWorksheet(spreadsheet, worksheet).getCellFeedUrl();

    CellEntry newEntry = new CellEntry(row, column, input);

    service.insert(cellFeedUrl, newEntry);
  }

  /**
   * Prints the usage of this application.
   */
  private static void usage() {
    System.out.println("Usage: java ImportClient --username [user] "
        + "--password [pass] --filename [file] --spreadsheet [name] "
        + "--worksheet [name] --delimiter [regex]");
    System.out.println("\nA simple application that uses the provided Google\n"
        + "Account username and password to locate the\n"
        + "spreadsheet and worksheet in user's Google\n"
        + "Spreadsheet account, and import the provided\n"
        + "delimited text file into the worksheet."); 
  }

  /**
   * Main entry point. Parses arguments and creates and invokes the
   * ImportClient.
   */
  public static void main(String[] args) throws Exception {

    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "passwd", "pw", "p");
    String filename = parser.getValue("filename", "file", "f");
    String spreadsheet = parser.getValue("spreadsheet", "s");
    String worksheet = parser.getValue("worksheet", "w");
    String delimiter = parser.getValue("delimiter", "delimit", "d");
    boolean help = parser.containsKey("help", "h");
    if (help || (username == null) || (password == null)
        || (spreadsheet == null) || (worksheet == null) 
        || (delimiter == null)) {
      usage();
      System.exit(1);
    }

    ImportClient client = new ImportClient(username, password);

    client.purgeWorksheet(spreadsheet, worksheet);

    Pattern delim = Pattern.compile(delimiter);
   
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine();
      int row = 0;
      while (line != null) {
      
        // Break up the line by the delimiter and insert the cells
        String[] cells = delim.split(line, -1);  
        for (int col = 0; col < cells.length; col++) {
          client.insertCellEntry(spreadsheet, worksheet, 
              row + 1, col + 1, cells[col]);
        }
      
        // Advance the loop
        line = reader.readLine();
        row++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
}
