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


package sample.spreadsheet.table;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.TableQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.Column;
import com.google.gdata.data.spreadsheet.Data;
import com.google.gdata.data.spreadsheet.Header;
import com.google.gdata.data.spreadsheet.Worksheet;
import com.google.gdata.data.spreadsheet.TableEntry;
import com.google.gdata.data.spreadsheet.TableFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * An application to show the basic operations of the Table feed.
 * 
 * 
 */
public class TableDemo {

  /** The message for displaying the usage parameters. */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java TableDemo --username [user] --password [pass] ", ""};

  /** Help on setting up this demo. */
  private static final String[] WELCOME_MESSAGE = {
      "Using this demo, you can see how tables can be conveniently created,",
      "updated, queried, and deleted.", "",
      "Before starting this demo, make sure you create a spreadsheet."};

  /** Help on all available commands. */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      " load                   [[select a spreadsheet]]",
      " list                   [[shows all entries]]",
      " add title=Table1, summary=Test Table, worksheet=Sheet1, header=1, " 
           + "startrow=2, numrows=5, columns=a:Name;b:Phone;c:Address " 
           + "[[adds a table]]",
      " update <tableid> title=AwesomeTable [[updates a table]]",
      " delete <tableid>       [[deletes; use 'list' first]]",
      " search AwesomeTable    [[title search]]",  // add title exact.
      " exit"};

  /** Our view of Google Spreadsheets as an authenticated Google user. */
  private SpreadsheetService service;

  /** The URL of the table feed for the selected spreadsheet. */
  private URL tablesFeedUrl;

  /** A factory that generates the appropriate feed URLs. */
  private FeedURLFactory factory;

  /** The output stream to use. */
  private PrintStream out;

  
  /**
   * Caches entries.
   * 
   * In this example, every entry we display to the user is cached. We do this
   * because Google Data has a feature where if an entry is updated between the
   * time you download it and update or delete it, you will be alerted of the
   * edit. For example:
   * 
   * 1. In this sample app, I look at the table with title Table1. 2. Someone
   * editing the spreadsheet changes the title to UpdatedTable1. 
   * In this sample app, I try to change the title to Table2.
   * 
   * By caching the entry and updating the last entry, I am telling the server
   * that I am updating the age from Table1 to Table2. The server now has enough
   * information to tell me that the age has already been changed, because the
   * version ID that I am sending was the version ID for Table 1.
   * 
   * If you do not wish to cache entries, an alternative is to fetch the proper
   * entry ID from Google Data, update that fresh entry, and post. In that case,
   * no caching is necessary, and a version conflict is less likely. On the
   * other hand, you won't get alerted if someone else has changed the entry in
   * the meantime.
   * 
   * This would be achieved by getting an entry: TableEntry freshEntry=
   * service.getEntry( new URL(tablesFeedUrl.toString() + "/" + id),
   * TableEntry.class) (update freshEntry's fields) freshEntry.update();
   */
  private Map<String, TableEntry> entriesCached;

  /**
   * Starts up the demo with the specified service.
   * 
   * @param service the connection to the Google Spradsheets service.
   * @param outputStream a handle for stdout.
   */
  public TableDemo(SpreadsheetService service, PrintStream outputStream) {
    this.out = outputStream;
    this.service = service;
    this.factory = FeedURLFactory.getDefault();
    this.entriesCached = new HashMap<String, TableEntry>();
  }

  /**
   * Set the user credentials for theg given user name and password.
   * 
   * @param username the username of the user toauthenticate 
   *     (e.g. yourname@gmail.com)
   @ @param password the password of the user to authenticate.
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   */
  public void login(String username, String password)
      throws AuthenticationException {
    // Authenticate
    service.setUserCredentials(username, password);
  }

  /**
   * Parses the command line arguments given in the format 
   * "title=Table1,summary=This is a test", and sets the corresponding values 
   * on the given TableEntry.
   *
   * Values that are not specified are left alone. That is, if the entry already
   * contains "title=Table1", then setting summary will retain
   * the title as Table1.
   * 
   * @param entryToUpdate the entry to change based on the parameters
   * @param nameValuePairs the list of name/value pairs, containing the name of
   *        the title.
   */
  public TableEntry setEntryContentsFromString(TableEntry entryToUpdate, 
      String nameValuePairs) {
    Map<String, String> dataParams = Maps.newHashMap();
    Map<String, String> dataCols = Maps.newHashMap();

    // Split first by the commas between the different fields.
    for (String nameValuePair : nameValuePairs.split(",")) {

      // Then, split by the equal sign.  Attributes are specified as key=value.
      String[] parts = nameValuePair.split("=", 2);
      if (parts.length < 2) {
        System.out.println("Attributes are specified as key=value, " 
            + "for example, title=Table1");
      } 
      
      String tag = parts[0].trim(); // such as "name"
      String value = parts[1].trim(); // such as "Fred"
      
      if (tag.equals("title")) {
        entryToUpdate.setTitle(new PlainTextConstruct(value));
      } else if (tag.equals("summary")) {
        entryToUpdate.setSummary(new PlainTextConstruct(value));
      } else if (tag.equals("worksheet")) {
        entryToUpdate.setWorksheet(new Worksheet(value));
      } else if (tag.equals("header")) {
        entryToUpdate.setHeader(new Header(Integer.parseInt(value)));
      } else if (tag.equals("startrow") || tag.equals("insertionmode") 
            || tag.equals("numrows")) {
        dataParams.put(tag, value);
      } else if (tag.equals("columns")) {
        String[] columns = value.split(";");
        for (int i = 0; i < columns.length; i++) {
          String[] colInfo = columns[i].split(":");
          if (colInfo.length < 2) {
            System.out.println("Columns are specified as column:value, for " 
                + " example, B:UpdatedPhone");
          }
          String index = colInfo[0];
          String name = colInfo[1];
          dataCols.put(index, name);
        }
      }
    }
   
    // Update table data.
    Data data = getDataFromParams(
        entryToUpdate.getData(), dataParams, dataCols);
    entryToUpdate.setData(data);

    return entryToUpdate;
  }

  /**
   * Create a new data object from any data params that were specified.
   * Data params are numrows, startrow, insertionmode and columns.  
   * Any attributes that are not specified are left alone.  So, if data already
   * has insertionmode=insert, specifying numrows=4 and startrow=12 will leave
   * insertionmode as insert.
   *
   * @param data original data object.
   * @param dataParams new data params to use to update the original
   *     data object.
   * @param columnMap map of column index to value used to update the column 
   *     headers of the table.
   */
  public Data getDataFromParams(Data data, 
    Map<String, String> dataParams, Map<String, String> columnMap) {
    Data newData = new Data();
    if (data == null) {
      data = new Data();
    }
     
    if (dataParams.get("numrows") != null) {
      newData.setNumberOfRows(Integer.parseInt(dataParams.get("numrows")));
    } else {
      newData.setNumberOfRows(data.getNumberOfRows());
    }
 
    if (dataParams.get("startrow") != null) {
      newData.setStartIndex(Integer.parseInt(dataParams.get("startrow")));
    } else {
      newData.setStartIndex(data.getStartIndex());
    }
   
    String insertionMode = dataParams.get("insertionmode");
    if (insertionMode != null && insertionMode.equals("insert")) {
      newData.setInsertionMode(Data.InsertionMode.INSERT);
    }

    List<Column> existing = data.getColumns();
    // Add existing column data to column map.
    for (Column existingCol : existing) {
      String index = existingCol.getIndex();
      String name = existingCol.getName();
      // If column is being updated, set value, else add a new one.
      if (columnMap.get(index) == null) {
        columnMap.put(index, name);
      }
    }
    
    // Set columns on new data object.
    for (String key : columnMap.keySet()) {
      newData.addColumn(new Column(key, columnMap.get(key)));
    }
    return newData;
  }

  /**
   * Adds a new table entry, based on the user-specified name value pairs.
   * Note that the following parameters must be specified:
   *     title, startrow, numrows, columns.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  public void addNewEntry(String nameValuePairs) throws IOException,
      ServiceException {
    TableEntry newEntry = setEntryContentsFromString(new TableEntry(),
        nameValuePairs);
    service.insert(tablesFeedUrl, newEntry);
    out.println("Added table!");
  }

  /**
   * Prints the entire table entry, in a way that mildly resembles what the
   * actual XML looks like.
   * 
   * In addition, all printed entries are cached here. This way, they can be
   * updated or deleted, without having to retrieve the version identifier again
   * from the server.
   * 
   * @param entry the list entry to print
   */
  public void printAndCacheEntry(TableEntry entry) {

    // We only care about the entry id, chop off the leftmost part.
    // I.E., this turns http://spreadsheets.google.com/..../cpzh6 into cpzh6.
    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);

    // Cache all displayed entries so that they can be updated later.
    entriesCached.put(id, entry);

    out.println("-- id: " + id + "  title: " + entry.getTitle().getPlainText());
    out.println("<title>" + entry.getTitle().getPlainText() + "</title>");
    out.println("<summary>" + entry.getSummary().getPlainText() + "</summary>");
    out.println("<worksheet>" + entry.getWorksheet().getName()
        + "</worksheet>");
    out.println("<header>" + entry.getHeader().getRow() + "</header>");
    Data data = entry.getData();
    out.println("<data> insertionMode=" + data.getInsertionMode().name()
        + " startRow=" + data.getStartIndex() 
        + " numRows=" + data.getNumberOfRows());
    for (Column col: data.getColumns()) {
      out.println("     <column>" + col.getIndex() + " " + col.getName()
          + "</column>");
    }
    out.println("</data>");
  }

  /**
   * Displays the given list of entries and prompts the user to select the index
   * of one of the entries. NOTE: The displayed index is 1-based and is
   * converted to 0-based before being returned.
   * 
   * @param reader to read input from the keyboard
   * @param entries the list of entries to display
   * @param type describes the tyoe of things the list contains
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
    // Get the spreadsheet to load
    SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(),
        SpreadsheetFeed.class);
    List spreadsheets = feed.getEntries();
    int spreadsheetIndex = getIndexFromUser(reader, spreadsheets, 
        "spreadsheet");
    SpreadsheetEntry spreadsheet = (SpreadsheetEntry) spreadsheets
        .get(spreadsheetIndex);

    URL spreadsheetUrl = new java.net.URL(
        spreadsheet.getSpreadsheetLink().getHref());
    String baseUrl = new java.net.URL(spreadsheetUrl.getProtocol() + "://"
        + spreadsheetUrl.getHost()).toString();
    tablesFeedUrl = new java.net.URL(baseUrl + "/feeds/" + spreadsheet.getKey()
        + "/tables");
    System.out.println("Sheet loaded.");
  }

  /**
   * Lists all tables in the spreadsheet.
   * 
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  public void listAllEntries() throws IOException, ServiceException {
    TableFeed feed = service.getFeed(tablesFeedUrl, TableFeed.class);

    for (TableEntry entry : feed.getEntries()) {
      printAndCacheEntry(entry);
    }
    if (feed.getEntries().size() == 0) {
      System.out.println("No entries yet!");
    }
  }

  /**
   * Searches titles of tables with a text search string, finds tables that
   * match the given title and prints those entries out.
   * 
   * @param titleSearchString a string like "Table 2" will look for the
   *        string "Table 2" in the title of all tables available on the sheet.
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  public void search(String titleSearchString) throws IOException,
      ServiceException {
    TableQuery query = new TableQuery(tablesFeedUrl);
    
    query.setTitleQuery(titleSearchString);
    TableFeed feed = service.query(query, TableFeed.class);

    out.println("Results for [" + titleSearchString + "]");

    for (TableEntry entry : feed.getEntries()) {
      printAndCacheEntry(entry);
    }
  }

  /**
   * Deletes an entry by the ID.
   * 
   * This looks up the old cached row so that the version ID is known. A version
   * ID is used by GData to avoid edit collisions, so that you know if someone
   * has changed the row before you delete it.
   * 
   * For this reason, the cached version of the row, as you last saw it, is
   * kept, instead of querying the entry anew.
   * 
   * @param idToDelete the ID of the table to delete such as "0"
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  public void delete(String idToDelete) throws IOException, ServiceException {
    TableEntry entry = entriesCached.get(idToDelete); // Find the entry to
    // delete

    if (entry != null) {
      entry.delete(); // This deletes the existing entry.
      out.println("Deleted!");
    } else {
      out.println("I don't know that ID.");
      out.println("In GData, you must get an entry before deleting it,");
      out.println("so that you have the version ID.");
      out.println("You might have to 'list' first.");
    }
  }

  /**
   * Updates an existing entry.
   * 
   * See the comment in {@code delete} for why the entry is cached in a hash
   * map.
   * 
   * @param id the ID of the row to update
   * @param nameValuePairs the name value pairs, such as "title=UpdatedTitle"
   *        to change
   *        the table's title to UpdatedTitle
   * @throws ServiceException when the request causes an error in the Google
   *         Spreadsheets service.
   * @throws IOException when an error occurs in communication with the Google
   *         Spreadsheets service.
   */
  public void update(String id, String nameValuePairs) throws IOException,
      ServiceException {

    // The next line of code finds the entry to update.
    // See the javadoc on entriesCached.
    TableEntry entry = entriesCached.get(id);

    setEntryContentsFromString(entry, nameValuePairs);

    if (entry != null) {
      entry.update(); // This updates the existing entry.
      out.println("Updated!");
    } else {
      out.println("I don't know that ID.");
      out.println("In GData, you must get an entry before deleting it.");
      out.println("You might have to 'list' first.");
    }
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

      if (name.equals("add")) {
        addNewEntry(parameters);
      } else if (name.equals("load")) {
        loadSheet(reader);
      } else if (name.equals("list")) {
        listAllEntries();
      } else if (name.equals("search")) {
        search(parameters);
      } else if (name.equals("delete")) {
        delete(parameters);
      } else if (name.equals("update")) {
        String[] split = parameters.split(" ", 2);
        if (split.length < 2) {
          out.println("insufficient number of params for update.");
        }
        update(split[0], split[1]);
      } else if (name.startsWith("q") || name.startsWith("exit")) {
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

    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));

    // Login and prompt the user to pick a sheet to use.
    login(username, password);
    try {
      loadSheet(reader);
    } catch (Exception e) {
      e.printStackTrace();
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

    TableDemo demo = new TableDemo(new SpreadsheetService("Table Demo"),
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
