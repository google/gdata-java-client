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


package sample.docs;

import com.google.gdata.client.Query;
import com.google.gdata.client.docs.DocsService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An application that serves as a sample to show how the Documents List
 * Service can be used to search your documents and upload files.
 *  
 * 
 */
public class DocumentListDemo {
  
  /** The message for displaying the usage parameters. */
  private static final String[] USAGE_MESSAGE = {
    "Usage: java DocumentListDemo.jar --username <user> --password <pass>",
    "    [--server <host:port>]  Where is the feed (default = docs.google.com)",
    "    [--log]                 Enable logging of requests",
    ""
  };

  /** Welcome message, introducing the program. */
  private static final String[] WELCOME_MESSAGE = {
      "This is a demo of the document list feed!", "",
      "Using this interface, you can read and upload your documents.",
      ""};

  /** Help on all available commands. */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      " upload full/file/path         "
          + "[[write the filename of a file to upload]]",
      " list                          [[shows all documents]]",
      " spreadsheets                  [[shows all of your spreadsheets]]",
      " documents                     " 
          + "[[shows all of your word processing documents]]",
      " search <search_text>          [[full text query]]",
      " exit                          [[quit sample]]"};

  private static final String DEF_FEED_HOST = "docs.google.com";
  private static final String FEED_URL_PATH = "/feeds/documents/private/full";
  private static final String DOCUMENT_CATEGORY = "/-/document";
  private static final String SPREADSHEET_CATEGORY = "/-/spreadsheet";

  /** Our view of doclist service as an authenticated Google user. */
  private DocsService service;

  /** The URL of the doclist feed. */
  private URL documentListFeedUrl;

  /** The output stream. */
  private PrintStream out;

  /**
   * Constructs a document list demo from the specified document list service, 
   * which is used to authenticate to and access Google Documents.
   *
   * @param service the connection to the Google Documents service.
   * @param outputStream a handle for stdout.
   * @throws MalformedURLException if the URL for the docs feed is invalid.
   */
  public DocumentListDemo(DocsService service, PrintStream outputStream) 
      throws MalformedURLException {
    this.service = service;
    this.out = outputStream;
  }
  
  /**
   * Log in to Google, under the Google Docs account.
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
   * Prints out the specified document entry.
   *
   * @param doc the document entry to print
   */
  public void printDocumentEntry(DocumentListEntry doc) {
    String shortId = doc.getId().substring(doc.getId().lastIndexOf('/') + 1);
    out.println(
        " -- Document(" + doc.getTitle().getPlainText() + "/" + shortId + ")");
  }

  
  /**
   * Shows all documents that are in the documents list.
   *
   * @throws ServiceException when the request causes an error in the Google
   *         Docs service.
   * @throws IOException when an error occurs in communication with the Google
   *         Docs service.
   */
  public void showAllDocs() throws IOException, ServiceException {
    DocumentListFeed feed = service.getFeed(documentListFeedUrl, 
        DocumentListFeed.class);

    out.println("List of all documents:");
    for (DocumentListEntry entry : feed.getEntries()) {
      printDocumentEntry(entry);
    }
  }
  
  /**
   * Shows all word processing documents that are in the documents list.
   *
   * @throws ServiceException when the request causes an error in the Google
   *         Docs service.
   * @throws IOException when an error occurs in communication with the Google
   *         Docs service.
   */
  public void showAllDocuments() throws IOException, ServiceException {
    DocumentListFeed feed = service.getFeed(
        new URL(documentListFeedUrl.toString() + DOCUMENT_CATEGORY), 
        DocumentListFeed.class);

    out.println("List of all word documents:");
    for (DocumentListEntry entry : feed.getEntries()) {
      printDocumentEntry(entry);
    }
  }
  
  /**
   * Shows all wor processing documents that are in the documents list.
   *
   * @throws ServiceException when the request causes an error in the Google
   *         Docs service.
   * @throws IOException when an error occurs in communication with the Google
   *         Docs service.
   */
  public void showAllSpreadsheets() throws IOException, ServiceException {
    DocumentListFeed feed = service.getFeed(
        new URL(documentListFeedUrl.toString() + SPREADSHEET_CATEGORY), 
        DocumentListFeed.class);

    out.println("List of all spreadsheets:");
    for (DocumentListEntry entry : feed.getEntries()) {
      printDocumentEntry(entry);
    }
  }
  
  /**
   * Performs a full-text search on your documents.
   *
   * @param fullTextSearchString a full text search string, with space-separated
   *        keywords
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws IOException when an error occurs in communication with the Doclis
   *         service.
   */
  public void search(String fullTextSearchString)
      throws IOException, ServiceException {
    Query query = new Query(documentListFeedUrl);
    query.setFullTextQuery(fullTextSearchString);
    DocumentListFeed feed = service.query(query, DocumentListFeed.class);

    out.println("Results for [" + fullTextSearchString + "]");

    for (DocumentListEntry entry : feed.getEntries()) {
      printDocumentEntry(entry);
    }
  }

  /**
   * Performs a full-text search on your documents.
   *
   * @param filePath path to uploaded file. 
   * 
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   */
  public void uploadFile(String filePath)
      throws IOException, ServiceException {
    DocumentEntry newDocument = new DocumentEntry();
    File documentFile = new File(filePath);
    newDocument.setFile(documentFile);

    // Set the title for the new document. For this example we just use the
    // filename of the uploaded file.
    newDocument.setTitle(new PlainTextConstruct(documentFile.getName()));
    DocumentListEntry uploaded =
        service.insert(documentListFeedUrl, newDocument);
    printDocumentEntry(uploaded);
  }
  
  /**
   * Reads and executes one command.
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

      if (name.equals("list")) {
        showAllDocs();
      } else if (name.equals("documents")) {
        showAllDocuments();
      } else if (name.equals("spreadsheets")) {
        showAllSpreadsheets();
      } else if (name.equals("search")) {
        search(parameters);
      } else if (name.equals("upload")) {
        uploadFile(parameters);
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
   *
   * @param username name of user to authenticate (e.g. yourname@gmail.com)
   * @param password password to use for authentication
   * @param feedUrl URL of the feed to connect to
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   */
  public void run(String username, String password, URL feedUrl)
      throws AuthenticationException {
    for (String s : WELCOME_MESSAGE) {
      out.println(s);
    }

    documentListFeedUrl = feedUrl;

    BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));

    // Login and prompt the user to enter a command
    login(username, password);
    
    while (executeCommand(reader)) {
    }
  }

  
  /**
   * Runs the demo.
   *
   * @param args the command-line arguments
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   * @throws MalformedURLException if the URL for the docs feed is invalid.
   */
  public static void main(String[] args)
      throws AuthenticationException, MalformedURLException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String server = parser.getValue("server", "s");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null) {
      usage();
      System.exit(1);
    }

    DocumentListDemo demo = new DocumentListDemo(
        new DocsService("Document List Demo"), System.out);
    if (server == null) {
      server = DEF_FEED_HOST;
    }

    if (parser.containsKey("log", "l")) {
      turnOnLogging();
    }

    demo.run(username, password, new URL("http://" + server + FEED_URL_PATH));
  }

  private static void turnOnLogging() {

    // Configure the logging mechanisms
    Logger httpLogger =
        Logger.getLogger("com.google.gdata.client.http.HttpGDataRequest");
    httpLogger.setLevel(Level.ALL);
    Logger xmlLogger = Logger.getLogger("com.google.gdata.util.XmlParser");
    xmlLogger.setLevel(Level.ALL);

    // Create a log handler which prints all log events to the console
    ConsoleHandler logHandler = new ConsoleHandler();
    logHandler.setLevel(Level.ALL);
    httpLogger.addHandler(logHandler);
    xmlLogger.addHandler(logHandler);
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
