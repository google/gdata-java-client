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


package sample.spreadsheet.list;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

import sample.util.SimpleCommandLineParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * An application to show the basic operations of the List feed.
 *
 * 
 */
public class ListDemo {

  /**
   * The message for displaying the usage parameters.
   */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java ListDemo --username [user] --password [pass] "
      + "--spreadsheetUrl http://....spreadsheets.google.com/ccc?id=...",
      ""
  };

  /**
   * Help on setting up this demo.
   */
  private static final String[] WELCOME_MESSAGE = {
      "Using this demo, you can see how rows can be conveniently inserted,",
      "queried, and deleted, almost like a user-friendly database.",
      "",
      "Before starting this demo, make sure you create a spreadsheet.  In the",
      "top row, put headers, such as 'Name', 'Day', 'Address', or any other",
      "important piece of information.  A good example might be:",
      "",
      "    Name      Day      Phone",
      "    Rosa      Tue      555-1212",
      "    Vladimir  Wed      555-3137",
      "    Sanjay    Thu      555-2127",
      "    Ejike     Fri      555-4444",
      "",
      "It is best practices to freeze the top row, in the 'Sorting' tab.",
      "This way, sorting will make sure the top row stays intact.  We",
      "suggest you leave your browser open while trying this out.",
      "",
      "It is important to know each row is identified by GData by a",
      "random-looking string like 'czhdp', which stays the same forever,",
      "even if the row is moved or changed.  This type of identifier",
      "is used throughout this demo. ",
      ""
  };

  /**
   * Help on all available commands.
   */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      " list                   [[shows all entries]]",
      " add name=Rosie,day=Tue [[adds an entry]]",
      " delete <id>            [[deletes; use 'list' first]]",
      " update <id> day=Wed    [[modifies]",
      " search Rosie Tue       [[full-text search]]",
      " query name = 'Rosie'   [[structured query]]"
  };


  /**
   * Our view of Google Spreadsheets as an authenticated Google user.
   */
  private SpreadsheetService service;

  /**
   * The URL of the list feed for the selected spreadsheet.
   */
  private URL listFeedUrl;

  /**
   * Caches entries.
   *
   * In this example, every entry we display to the user is cached.  We do
   * this because GData has a feature where if an entry is updated between
   * the time you download it and update or delete it, you will be alerted
   * of the edit.  For example:
   *
   *  1. In this sample app, I look at the row for Rosie, with age 26.
   *  2. Someone editing the spreadsheet changes Rosie's age to 29
   *  3. In this sample app, I try to change Rosie's age to 24
   *
   * By caching the entry and updating the last entry, I am telling the
   * server that I am updating the age from 26 to 24.  The server now has
   * enough information to tell me that the age has already been changed,
   * because the version ID that I am sending was the version ID for age
   * 26.
   *
   * If you do not wish to cache entries, an alternative is to fetch the
   * proper entry ID from GData, update that fresh entry, and post.  In that
   * case, no caching is necessary, and a version conflict is less likely. 
   * On the other hand, you won't get alerted if someone else has changed
   * the entry in the meantime.
   *
   * This would be achieved by getting an entry:
   *    ListEntry freshEntry= service.getEntry(
   *        new URL(listFeedUrl.toString() + "/" + id),
   *        ListEntry.class)
   *    (update freshEntry's fields)
   *    freshEntry.update();
   */
  private Map<String, ListEntry> entriesCached =
      new HashMap<String, ListEntry>();
  
  /**
   * The output stream to use.
   */
  private PrintStream out;

  /**
   * Starts up the demo with the specified service.
   */
  public ListDemo(SpreadsheetService service, PrintStream outputStream) {
    out = outputStream;
    this.service = service;
  }


  /**
   * Log in to Google, under a Google Spreadsheets account.
   */
  public void login(String username, String password)
      throws Exception {

    // Authenticate
    service.setUserCredentials(username, password);
  }

  /**
   * Computes the URL of the list feed for the spreadsheet.
   * It is via this URL that all requests are made.
   */
  public void computeListFeedLocation(String spreadsheetUrl) 
    throws MalformedURLException {

    URL url = new URL(spreadsheetUrl);
    FeedURLFactory factory = new FeedURLFactory("http://" + url.getHost());
    
    // Parses the entire URL to find the spreadsheet key.
    String spreadsheetKey =
        FeedURLFactory.getSpreadsheetKeyFromUrl(spreadsheetUrl);

    // Gets the default (first) worksheet's list feed for this spreadsheet
    listFeedUrl = factory.getListFeedUrl(spreadsheetKey, "default", "private", 
        "full");
  }

  /**
   * Parses a list of the format "name=Fred,age=20,friend=Wilma", and
   * sets the corresponding values in the list entry.
   *
   * Values that are not specified are left alone.  That is, if the entry
   * already contains "haircolor=black", then setting name, age, and friend
   * will retain the haircolor as black.
   *
   * @param entryToChange the entry to change based on the parameters
   * @param nameValuePairs the list of name/value pairs, containing
   *        the name of the title (in lowercase with all invalid characters
   *        removed), .
   */
  public void setEntryContentsFromString(
      ListEntry entryToChange, String nameValuePairs) {

    // Split first by the commas between the different fields.
    for (String nameValuePair : nameValuePairs.split(",")) {

      // Then, split by the equal sign.
      String[] parts = nameValuePair.split("=", 2);
      String tag = parts[0];   // such as "name"
      String value = parts[1]; // such as "Fred"

      entryToChange.getCustomElements().setValueLocal(tag, value);
    }
  }

  /**
   * Adds a new list entry, based on the user-specified name value pairs.
   *
   * @param nameValuePairs pairs such as "name=Rosa,phone=555-1212"
   */
  public void addNewEntry(String nameValuePairs) throws Exception {
    ListEntry newEntry = new ListEntry();

    setEntryContentsFromString(newEntry, nameValuePairs);

    service.insert(listFeedUrl, newEntry);

    out.println("Added!");
  }

  /**
   * Prints the entire list entry, in a way that mildly resembles what the
   * actual XML looks like.
   *
   * In addition, all printed entries are cached here.  This way, they
   * can be updated or deleted, without having to retrieve the version
   * identifier again from the server.
   *
   * @param entry the list entry to print
   */
  public void printAndCacheEntry(ListEntry entry) {

    // We only care about the entry id, chop off the leftmost part.
    // I.E., this turns http://spreadsheets.google.com/..../cpzh6 into cpzh6.
    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);

    // Cache all displayed entries so that they can be updated later.
    entriesCached.put(id, entry);

    out.println("-- id: " + id + "  title: " + entry.getTitle().getPlainText());

    for (String tag : entry.getCustomElements().getTags()) {
      out.println("     <gsx:" + tag + ">"
          + entry.getCustomElements().getValue(tag)
          + "</gsx:" + tag +">");
    }
  }

  /**
   * Lists all rows in the spreadsheet.
   */
  public void listAllEntries() throws Exception {
    ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);

    for (ListEntry entry : feed.getEntries()) {
      printAndCacheEntry(entry);
    }
  }

  /**
   * Searches rows with a full text search string, finding any rows that
   * match all the given words.
   *
   * @param fullTextSearchString a string like "Rosa 555" will look for the
   *        substrings Rosa and 555 to appear anywhere in the row
   */
  public void search(String fullTextSearchString) throws Exception {
    ListQuery query = new ListQuery(listFeedUrl);
    query.setFullTextQuery(fullTextSearchString);
    ListFeed feed = service.query(query, ListFeed.class);

    out.println("Results for [" + fullTextSearchString + "]");

    for (ListEntry entry : feed.getEntries()) {
      printAndCacheEntry(entry);
    }
  }

  /**
   * Performs a full database-like query on the rows.
   *
   * @param structuredQuery a query like: name = "Bob" and phone != "555-1212"
   */
  public void query(String structuredQuery) throws Exception {
    ListQuery query = new ListQuery(listFeedUrl);
    query.setSpreadsheetQuery(structuredQuery);
    ListFeed feed = service.query(query, ListFeed.class);

    out.println("Results for [" + structuredQuery + "]");

    for (ListEntry entry : feed.getEntries()) {
      printAndCacheEntry(entry);
    }
  }

  /**
   * Deletes an entry by the ID.
   *
   * This looks up the old cached row so that the version ID is known.
   * A version ID is used by GData to avoid edit collisions, so that you know
   * if someone has changed the row before you delete it.
   *
   * For this reason, the cached version of the row, as you last saw it,
   * is kept, instead of querying the entry anew.
   *
   * @param idToDelete the ID of the row to delete such as "cph6n"
   */
  public void delete(String idToDelete) throws Exception {
    ListEntry entry = entriesCached.get(idToDelete); // Find the entry to delete

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
   * See the comment in {@code delete} for why the entry is cached in a
   * hash map.
   *
   * @param id the ID of the row to update
   * @param nameValuePairs the name value pairs, such as "name=Rosa"
   *        to change the row's name field to Rosa
   */
  public void update(String id, String nameValuePairs) throws Exception {

    // The next line of code finds the entry to update.
    // See the javadoc on entriesCached.
    ListEntry entry = entriesCached.get(id);

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

      if (name.equals("add")) {
        addNewEntry(parameters);
      } else if (name.equals("list")) {
        listAllEntries();
      } else if (name.equals("search")) {
        search(parameters);
      } else if (name.equals("query")) {
        query(parameters);
      } else if (name.equals("delete")) {
        delete(parameters);
      } else if (name.equals("update")) {
        String[] split = parameters.split(" ", 2);
        update(split[0], split[1]);
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
    computeListFeedLocation(spreadsheetUrl);

    while (executeCommand(reader)) {}
  }

  /**
   * Runs the demo.
   * 
   * @param args the command-line arguments
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
    
    ListDemo demo = new ListDemo(new SpreadsheetService("List Demo"), 
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
