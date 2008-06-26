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

package sample.spreadsheet.worksheet;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import sample.util.SimpleCommandLineParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

/**
 * Demo of the CRUD operation on the worksheets feed.
 * 
 * Using this demo, you can see how the Google data API can you can manage the
 * worksheets in your spreadsheets.
 * 
 * Usage: java WorksheetDemo --username [username] --password [password]
 * 
 * 
 */
public class WorksheetDemo {

  /** The message for displaying the usage parameters. */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java WorksheetDemo --username [user] --password [pass] ", ""};

  /** Welcome message, introducing the program. */
  private static final String[] WELCOME_MESSAGE = {
      "This is a demo of the worksheets feed!",
      "",
      "Using this interface, you can manage the worksheets in your "
          + "spreadsheet.", ""};

  /** Help on all available commands. */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      " load                                      [[load a spreadsheet]]",
      " list                                      [[show all worksheets]]",
      " create title, #rows, #cols                [[create a new worksheet]]",
      " update oldTitle, newTitle, #rows, #cols   "
          + "[[update the title and size of a worksheet]]",
      " delete title                              [[delete a worksheet]]",
      " quit or q                                 [[quit demo]]"};

  /** Our view of Google Spreadsheets as an authenticated Google user. */
  private SpreadsheetService service;

  /** The URL of the worksheet feed. */
  private URL worksheetFeedUrl;

  /** The output stream. */
  private PrintStream out;

  /** A factory that generates the appropriate feed URLs. */
  private FeedURLFactory factory;

  /**
   * Constructs a worksheet demo using the given spreadsheet service and output
   * stream. The spreadsheet service is used to authenticate to and access
   * Google Spreadsheets.
   * 
   * @param service the connection to the Google Spreadsheets service.
   * @param outputStream a handle for stdout.
   */
  public WorksheetDemo(SpreadsheetService service, PrintStream outputStream) {
    this.service = service;
    this.out = outputStream;
    this.factory = FeedURLFactory.getDefault();
  }

  /**
   * Log in to Google, under the Google Spreadsheets account.
   * 
   * @param username name of user to authenticate (e.g. yourname@gmail.com)
   * @param password password to use for authentication
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   */
  public void login(String username, String password)
      throws AuthenticationException {

    // Authenticate
    service.setUserCredentials(username, password);
  }

  /**
   * Displays the given list of entries and prompts the user to select the index
   * of one of the entries. NOTE: The displayed index is 1-based and is
   * converted to 0-based before being returned.
   * 
   * @param reader to read input from the keyboard
   * @param entries the list of entries to display
   * @param type describes the type of things the list contains
   * @return the 0-based index of the user's selection
   * @throws IOException if an I/O error occurs while getting input from user
   */
  private int getIndexFromUser(BufferedReader reader, List entries, String type)
      throws IOException {
    for (int i = 0; i < entries.size(); i++) {
      BaseEntry entry = (BaseEntry) entries.get(i);
      System.out.println("\t(" + (i + 1) + ") "
          + entry.getTitle().getPlainText());
    }
    int index = -1;
    while (true) {
      out.print("Enter the number of the spreadsheet to load: ");
      String userInput = reader.readLine();
      try {
        index = Integer.parseInt(userInput);
        if (index < 1 || index > entries.size()) {
          throw new NumberFormatException();
        }
        break;
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number for your selection.");
      }
    }
    return index - 1;
  }

  /**
   * Uses the user's credentials to get a list of spreadsheets. Then asks the
   * user which spreadsheet to load. If the selected spreadsheet has multiple
   * worksheets then the user will also be prompted to select what sheet to use.
   * 
   * @param reader to read input from the keyboard
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   * 
   */
  public void loadSheet(BufferedReader reader) throws IOException,
      ServiceException {
    SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(),
        SpreadsheetFeed.class);
    List<SpreadsheetEntry> spreadsheets = feed.getEntries();
    int spreadsheetIndex = getIndexFromUser(reader, spreadsheets, "spreadsheet");
    SpreadsheetEntry spreadsheet = feed.getEntries().get(spreadsheetIndex);
    worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();
    System.out.println("Spreadsheet loaded.");
  }

  /**
   * Lists all the worksheets in the loaded spreadsheet.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  private void listAllWorksheets() throws IOException, ServiceException {
    WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl,
        WorksheetFeed.class);
    for (WorksheetEntry worksheet : worksheetFeed.getEntries()) {
      String title = worksheet.getTitle().getPlainText();
      int rowCount = worksheet.getRowCount();
      int colCount = worksheet.getColCount();
      System.out.println("\t" + title + " - rows:" + rowCount + " cols: "
          + colCount);
    }
  }


  /**
   * Creates a new worksheet in the loaded spreadsheets, using the title and
   * sizes given.
   * 
   * @param title a String containing a name for the new worksheet.
   * @param rowCount the number of rows the new worksheet should have.
   * @param colCount the number of columns the new worksheet should have.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  private void createWorksheet(String title, int rowCount, int colCount)
      throws IOException, ServiceException {
    WorksheetEntry worksheet = new WorksheetEntry();
    worksheet.setTitle(new PlainTextConstruct(title));
    worksheet.setRowCount(rowCount);
    worksheet.setColCount(colCount);
    service.insert(worksheetFeedUrl, worksheet);
  }

  /**
   * Updates the worksheet specified by the oldTitle parameter, with the given
   * title and sizes. Note that worksheet titles are not unique, so this method
   * just updates the first worksheet it finds. Hey, it's just sample code - no
   * refunds!
   * 
   * @param oldTitle a String specifying the worksheet to update.
   * @param newTitle a String containing the new name for the worksheet.
   * @param rowCount the number of rows the new worksheet should have.
   * @param colCount the number of columns the new worksheet should have.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  private void updateWorksheet(String oldTitle, String newTitle, int rowCount,
      int colCount) throws IOException, ServiceException {
    WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl,
        WorksheetFeed.class);
    for (WorksheetEntry worksheet : worksheetFeed.getEntries()) {
      String currTitle = worksheet.getTitle().getPlainText();
      if (currTitle.equals(oldTitle)) {
        worksheet.setTitle(new PlainTextConstruct(newTitle));
        worksheet.setRowCount(rowCount);
        worksheet.setColCount(colCount);
        worksheet.update();
        System.out.println("Worksheet updated.");
        return;
      }
    }

    // If it got this far, the worksheet wasn't found.
    System.out.println("Worksheet not found: " + oldTitle);
  }

  /**
   * Deletes the worksheet specified by the title parameter. Note that worksheet
   * titles are not unique, so this method just updates the first worksheet it
   * finds.
   * 
   * @param title a String containing the name of the worksheet to delete.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  private void deleteWorksheet(String title) throws IOException,
      ServiceException {
    WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl,
        WorksheetFeed.class);
    for (WorksheetEntry worksheet : worksheetFeed.getEntries()) {
      String currTitle = worksheet.getTitle().getPlainText();
      if (currTitle.equals(title)) {
        worksheet.delete();
        System.out.println("Worksheet deleted.");
        return;
      }
    }

    // If it got this far, the worksheet wasn't found.
    System.out.println("Worksheet not found: " + title);
  }

  /**
   * Parses and executes a command.
   * 
   * @param reader to read input from the keyboard
   * @return false if the user quits, true on exception
   */
  public boolean executeCommand(BufferedReader reader) {
    for (String s : COMMAND_HELP_MESSAGE) {
      out.println(s);
    }

    System.err.print("Command: ");

    try {
      String command = reader.readLine();
      String[] parts = command.trim().split(" ", 2);
      String name = parts[0];
      String parameters = parts.length > 1 ? parts[1] : "";

      if (name.equals("load")) {
        loadSheet(reader);
      } else if (name.equals("list")) {
        listAllWorksheets();
      } else if (name.equals("create")) {
        String[] split = parameters.split(" ", 3);
        createWorksheet(split[0], Integer.parseInt(split[1]), Integer
            .parseInt(split[2]));
      } else if (name.equals("update")) {
        String[] split = parameters.split(" ", 4);
        updateWorksheet(split[0], split[1], Integer.parseInt(split[2]), Integer
            .parseInt(split[3]));
      } else if (name.equals("delete")) {
        deleteWorksheet(parameters);
      } else if (name.equals("q") || name.equals("quit")) {
        return false;
      } else {
        out.println("Unknown command.");
      }
    } catch (ServiceException se) {
      // Show *exactly* what went wrong.
      se.printStackTrace();
    } catch (IOException ioe) {
      // Show *exactly* what went wrong.
      ioe.printStackTrace();
    }
    return true;
  }

  /**
   * Starts up the demo and prompts for commands.
   * 
   * @param username name of user to authenticate (e.g. yourname@gmail.com)
   * @param password password to use for authentication
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   */
  public void run(String username, String password)
      throws AuthenticationException {
    for (String s : WELCOME_MESSAGE) {
      out.println(s);
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // Login and prompt the user to pick a sheet to use.
    login(username, password);
    try {
      loadSheet(reader);
    } catch (ServiceException se) {
      // Show *exactly* what went wrong.
      se.printStackTrace();
    } catch (IOException ioe) {
      // Show *exactly* what went wrong.
      ioe.printStackTrace();
    }

    while (executeCommand(reader)) {
    }
  }

  /**
   * Runs the demo.
   * 
   * @param args the command-line arguments
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   */
  public static void main(String[] args) throws AuthenticationException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null) {
      usage();
      System.exit(1);
    }

    final String appName = "sampleCo-WorksheetDemo-0.9";
    WorksheetDemo demo = new WorksheetDemo(new SpreadsheetService(appName),
        System.out);

    demo.run(username, password);
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
