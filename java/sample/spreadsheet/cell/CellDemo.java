/* Copyright (c) 2006 Google Inc.
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


package sample.spreadsheet.cell;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;

import sample.util.SimpleCommandLineParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Very minimalist demo of the cell feed.
 *
 * Using this demo, you can see how GData can read and write to individual
 * cells based on their position.
 *
 * Usage: java ListDemo username http://spreadsheets.google.com/ccc?id=.....
 *
 * 
 */
public class CellDemo {

  /**
   * The message for displaying the usage parameters.
   */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java CellDemo --username [user] --password [pass] ",
      "       --spreadsheet http://....spreadsheets.google.com/ccc?id=...",
      ""
  };

  /**
   * Welcome message, introducing the program.
   */
  private static final String[] WELCOME_MESSAGE = {
    "This is a demo of the cells feed!",
    "",
    "Using this interface, you can read/write to your spreadsheet's cells.",
    ""
  };

  /**
   * Help on all available commands.
   */
  private static final String[] COMMAND_HELP_MESSAGE = {
    "Commands:",
    " list                              [[shows all cells]]",
    " range minRow maxRow minCol maxCol [[rectangle]]",
    " set row# col# formula             [[sets a cell]]",
    "   example: set 1 3 =R1C2+1",
    " search adam                       [[full text query]]"
  };


  /** Our view of Google Spreadsheets as an authenticated Google user. */
  private SpreadsheetService service;

  /** The URL of the cells feed. */
  private URL cellFeedUrl;

  /** The output stream. */
  private PrintStream out;


  /**
   * Constructs a cell demo from the specified spreadsheet service,
   * which is used to authenticate to and access Google Spreadsheets.
   */
  public CellDemo(SpreadsheetService service, PrintStream outputStream) {
    this.service = service;
    this.out = outputStream;
  }


  /**
   * Log in to Google, under the Google Spreadsheets account.
   */
  public void login(String username, String password)
      throws Exception {

    // Authenticate
    service.setUserCredentials(username, password);
  }

  /**
   * Computes the URL of the cells feed for the spreadsheet.
   */
  public void computeCellFeedLocation(String spreadsheetUrl) 
      throws MalformedURLException {

    URL url = new URL(spreadsheetUrl);
    FeedURLFactory factory = new FeedURLFactory("http://" + url.getHost());

    // Parses the entire URL to find the spreadsheet key.
    String spreadsheetKey 
      = FeedURLFactory.getSpreadsheetKeyFromUrl(spreadsheetUrl);

    // Gets the default (first) worksheet's list feed for this spreadsheet
    cellFeedUrl = factory.getCellFeedUrl(spreadsheetKey, "default", "private", 
        "full");
  }

  /**
   * Sets the particular cell at row, col to the specified formula or value.
   *
   * @param row the row number, starting with 1
   * @param col the column number, starting with 1
   * @param formulaOrValue the value if it doesn't start with an '=' sign;
   *        if it is a formula, be careful that cells are specified in R1C1
   *        format instead of A1 format.
   */
  public void setCell(int row, int col, String formulaOrValue)
      throws Exception {
      
    CellEntry newEntry = new CellEntry(row, col, formulaOrValue);
    service.insert(cellFeedUrl, newEntry);
    out.println("Added!");
  }

  /**
   * Prints out the specified cell.
   *
   * @param cell the cell to print
   */
  public void printCell(CellEntry cell) {
    String shortId = cell.getId().substring(cell.getId().lastIndexOf('/') + 1);
    out.println(" -- Cell(" + shortId + "/" + cell.getTitle().getPlainText()
        + ") formula(" + cell.getCell().getInputValue()
        + ") numeric(" + cell.getCell().getNumericValue()
        + ") value(" + cell.getCell().getValue()
        + ")");
  }

  /**
   * Shows all cells that are in the spreadsheet.
   */
  public void showAllCells() throws Exception {
    CellFeed feed = service.getFeed(cellFeedUrl, CellFeed.class);

    for (CellEntry entry : feed.getEntries()) {
      printCell(entry);
    }
  }

  /**
   * Shows a particular range of cells, limited by minimum/maximum rows
   * and columns.
   *
   * @param minRow the minimum row, inclusive, 1-based
   * @param maxRow the maximum row, inclusive, 1-based
   * @param minCol the minimum column, inclusive, 1-based
   * @param maxCol the maximum column, inclusive, 1-based
   */
  public void showRange(int minRow, int maxRow, int minCol, int maxCol)
      throws Exception {
    CellQuery query = new CellQuery(cellFeedUrl);
    query.setMinimumRow(minRow);
    query.setMaximumRow(maxRow);
    query.setMinimumCol(minCol);
    query.setMaximumCol(maxCol);
    CellFeed feed = service.query(query, CellFeed.class);

    for (CellEntry entry : feed.getEntries()) {
      printCell(entry);
    }
  }

  /**
   * Performs a full-text search on cells.
   * @param fullTextSearchString a full text search string, with
   *        space-separated keywords
   */
  public void search(String fullTextSearchString) throws Exception {
    CellQuery query = new CellQuery(cellFeedUrl);
    query.setFullTextQuery(fullTextSearchString);
    CellFeed feed = service.query(query, CellFeed.class);

    out.println("Results for [" + fullTextSearchString + "]");

    for (CellEntry entry : feed.getEntries()) {
      printCell(entry);
    }
  }

  /**
   * Reads and executes one command.
   *
   * @param reader to read input from the keyboard
   */
  public boolean executeCommand(BufferedReader reader)
      throws Exception {
    for (String s : COMMAND_HELP_MESSAGE) {
      out.println(s);
    }

    System.err.print("Command: ");

    try {
      String command = reader.readLine();
      String[] parts = command.trim().split(" ", 2);
      String name = parts[0];
      String parameters = parts.length > 1 ? parts[1] : "";

      if (name.equals("list")) {
        showAllCells();
      } else if (name.equals("search")) {
        search(parameters);
      } else if (name.equals("range")) {
        String[] s = parameters.split(" ", 4);
        showRange(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
            Integer.parseInt(s[2]), Integer.parseInt(s[3]));
      } else if (name.equals("set")) {
        String[] s = parameters.split(" ", 3);
        setCell(Integer.parseInt(s[0]), Integer.parseInt(s[1]), s[2]);
      } else if (name.startsWith("q") || name.startsWith("exit")) {
        return false;
      } else {
        out.println("Unknown command.");
      }
    } catch (Exception e) {

      // Show *exactly* what went wrong.
      e.printStackTrace();
    }
    return true;
  }

  /**
   * Starts up the demo and prompts for commands.
   */
  public void run(String username, String password, String spreadsheetUrl)
      throws Exception {
    for (String s : WELCOME_MESSAGE) {
      out.println(s);
    }

    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));

    login(username, password);
    computeCellFeedLocation(spreadsheetUrl);

    while (executeCommand(reader)) {}
  }

  /**
   * Runs the demo.
   *
   * @param args the command-line arguments
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String spreadsheetUrl = parser.getValue("spreadsheet", "url", "s");
    boolean help = parser.containsKey("help", "h");
    
    if (help || username == null
        || password == null || spreadsheetUrl == null) {
      usage();
      System.exit(1);
    }
    
    CellDemo demo = new CellDemo(new SpreadsheetService("List Demo"), 
        System.out);

    demo.run(username, password, spreadsheetUrl);
  }


  /**
   * Prints out the usage.
   */
  private static void usage() {
    for (String s : USAGE_MESSAGE) {
      System.out.println(s);
    }
    for (String s : WELCOME_MESSAGE) {
      System.out.println(s);
    }
  }
}
