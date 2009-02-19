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

import sample.util.SimpleCommandLineParser;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * An application that serves as a sample to show how the Documents List
 * Service can be used to search your documents and upload files.
 *
 * 
 * 
 * 
 */
public class DocumentListDemo {
  private static DocumentList documentList;
  private PrintStream out;

  private static final String APPLICATION_NAME = "Java GData Client";

  /**
   * The message for displaying the usage parameters.
   */
  private static final String[] USAGE_MESSAGE = {
    "Usage: java DocumentListDemo.jar --username <user> --password <pass>",
    "Usage: java DocumentListDemo.jar --authSub <token>",
    "    [--auth_protocol <protocol>]  The protocol to use with authentication.",
    "    [--auth_host <host:port>]     The host of the auth server to use.",
    "    [--protocol <protocol>]       The protocol to use with the HTTP requests.",
    "    [--host <host:port>]          Where is the feed (default = docs.google.com)",
    "    [--log]                       Enable logging of requests",
    ""};

  /**
   * Welcome message, introducing the program.
   */
  private static final String[] WELCOME_MESSAGE = {
      "", "This is a demo of the document list feed!",
      "Using this interface, you can read and upload your documents.",
      "Type 'help' for a list of commands.",
      ""};

  /**
   * Help on all available commands.
   */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      "    create <object_type> <name>               [[create an object]]",
      "    trash <object_id>                         [[puts the object into the trash]]",
      "    download <object_id> <file_path>          [[downloads the object to the folder" +
      " specified by file_path]]",
      "    list [object_type] [...]                  [[lists objects]]",
      "    move <object_id> <folder_id>              [[moves an object into a folder]]",
      "    perms <operation> [...]                   [[lists or modifies file permissions]]",
      "    remove <object_id> <folder_id>            [[removes an object from a folder]]",
      "    search <search_text>                      [[search documents for text strings]]",
      "    asearch <search_option>                   [[advanced search]]",
      "    upload <file_path>                        [[uploads a object]]",
      "",
      "    help [command]                            [[display this message, or info about" +
      " the specified command]]",
      "    exit                                      [[exit the program]]"};

  private static final String[] COMMAND_HELP_CREATE = {
    "create <object_type> <name>",
    "    object_type: document, spreadsheet, folder.",
    "    name: the name for the new object"};
  private static final String[] COMMAND_HELP_TRASH = {
    "trash <object_id>",
    "    object_id: the id of the object to be deleted"};
  private static final String[] COMMAND_HELP_DOWNLOAD = {
    "download <object_id> <file_path>",
    "    object_id: the id of the file you wish to download",
    "    file_path: the path to the directory to save the file in"};
  private static final String[] COMMAND_HELP_LIST = {
    "list [object_type]",
    "    object_type: all, starred, documents, spreadsheets, pdfs, presentations, folders.\n" +
    "        (defaults to 'all')",
    "list folder <folder_id>",
    "    folder_id: The id of the folder you want the contents list for."};
  private static final String[] COMMAND_HELP_MOVE = {
    "move <object_id> <folder_id>",
    "    object_id: the id of the object to be moved",
    "    folder_id: the folder to move the document into"};
  private static final String[] COMMAND_HELP_PERMS = {
    "perms list <object_id>",
    "    object_id: id of the object you wish to add/list permissions for",
    "perms add <role> <scope> <email> <object_id>",
    "    role: \"reader\", \"writer\", \"owner\"",
    "    scope: \"user\", \"domain\"",
    "    email: The user's email address or domain name (\"user@gmail.com\", \"domain.com\")",
    "    object_id: The id of the object to change permissions for.",
    "perms change <role> <scope> <email> <object_id>",
    "    role: \"reader\", \"writer\", \"owner\"",
    "    scope: \"user\", \"domain\"",
    "    email: The user's email address or domain name (\"user@gmail.com\", \"domain.com\")",
    "    object_id: The id of the object to change permissions for.",
    "perms remove <scope> <email> <object_id>",
    "    role: \"reader\", \"writer\", \"owner\"",
    "    scope: \"user\", \"domain\"",
    "    email: The user's email address or domain name (\"user@gmail.com\", \"domain.com\")",
    "    object_id: The id of the object to change permissions for."};

  private static final String[] COMMAND_HELP_REMOVE = {
    "remove <object_id> <folder_id>",
    "    object_id: the id of the object to remove from the folder",
    "    folder_id: the id of the folder to remove the object from"};
  private static final String[] COMMAND_HELP_SEARCH = {
    "search <search_text>",
    "    search_text: A string to be used for a full text query"};
  private static final String[] COMMAND_HELP_ASEARCH = {
    "asearch [<query_param>=<value>] [<query_param2>=<value2>] ...",
    "    query_param: title, title-exact, opened-min, opened-max, owner, writer, reader, " +
    "showfolders, etc.",
    "    value: The value of the parameter"};
  private static final String[] COMMAND_HELP_UPLOAD = {
    "upload <file_path> <title>",
    "    file_path: file to upload",
    "    title: A title to call the document"};
  private static final String[] COMMAND_HELP_HELP = {
    "help [command]",
    "    Weeeeeeeeeeeeee..."};
  private static final String[] COMMAND_HELP_EXIT = {
    "exit",
    "    Exit the program."};
  private static final String[] COMMAND_HELP_ERROR = {"unknown command"};

  private static final Map<String, String[]> HELP_MESSAGES;
  static {
    HELP_MESSAGES = new HashMap<String, String[]>();
    HELP_MESSAGES.put("create", COMMAND_HELP_CREATE);
    HELP_MESSAGES.put("trash", COMMAND_HELP_TRASH);
    HELP_MESSAGES.put("download", COMMAND_HELP_DOWNLOAD);
    HELP_MESSAGES.put("list", COMMAND_HELP_LIST);
    HELP_MESSAGES.put("move", COMMAND_HELP_MOVE);
    HELP_MESSAGES.put("perms", COMMAND_HELP_PERMS);
    HELP_MESSAGES.put("remove", COMMAND_HELP_REMOVE);
    HELP_MESSAGES.put("search", COMMAND_HELP_SEARCH);
    HELP_MESSAGES.put("asearch", COMMAND_HELP_ASEARCH);
    HELP_MESSAGES.put("upload", COMMAND_HELP_UPLOAD);
    HELP_MESSAGES.put("help", COMMAND_HELP_HELP);
    HELP_MESSAGES.put("exit", COMMAND_HELP_EXIT);
    HELP_MESSAGES.put("error", COMMAND_HELP_ERROR);
  }

  /**
   * Constructor
   *
   * @param outputStream Stream to print output to.
   */
  public DocumentListDemo(PrintStream outputStream) {
    out = outputStream;
  }

  /**
   * Prints out the specified document entry.
   *
   * @param doc the document entry to print.
   */
  public void printDocumentEntry(DocumentListEntry doc) {
    StringBuffer output = new StringBuffer();
    String shortId = doc.getId().substring(doc.getId().lastIndexOf('/') + 1);
    Set<String> folders = doc.getFolders();

    output.append(" -- " + doc.getTitle().getPlainText() + " ");
    if (folders.size() > 0) {
      for (String folder : folders) {
        output.append("[" + folder + "] ");
      }
    }
    output.append(shortId);

    out.println(output);
  }

  /**
   * Prints out the specified ACL entry.
   *
   * @param entry the ACL entry to print.
   */
  public void printAclEntry(AclEntry entry) {
    out.println(" -- " + entry.getScope().getValue() + ": " + entry.getRole().getValue());
  }

  /**
   * Executes the "create" command.
   *
   * @param args arguments for the "create" command.
   *     args[0] = "create"
   *     args[1] = object_type ("folder", "document", "spreadsheet")
   *     args[2] = title (what to name the document/folder)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeCreate(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      if (args[1].equals("folder")) {
        printDocumentEntry(documentList.createFolder(args[2]));
      } else if (args[1].equals("document")) {
        printDocumentEntry(documentList.createDocument(args[2]));
      } else if (args[1].equals("spreadsheet")) {
        printDocumentEntry(documentList.createSpreadsheet(args[2]));
      } else {
        printMessage(COMMAND_HELP_CREATE);
      }
    } else {
      printMessage(COMMAND_HELP_CREATE);
    }
  }

  /**
   * Executes the "trash" command.
   *
   * @param args arguments for the "trash" command.
   *     args[0] = "trash"
   *     args[1] = objectId (the id of the object to be trashed)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeTrash(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 2) {
      documentList.trashObject(args[1]);
    } else {
      printMessage(COMMAND_HELP_TRASH);
    }
  }

  /**
   * Executes the "download" command.
   *
   * @param args arguments for the "download" command.
   *     args[0] = "download"
   *     args[1] = objectId (the id of the object to be downloaded)
   *     args[2] = filepath (the path and filename to save the object as)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeDownload(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      documentList.downloadFile(args[1], args[2], DocumentList
          .getDownloadFormat(args[1], getTypeFromFilename(args[2])));
    } else {
      printMessage(COMMAND_HELP_DOWNLOAD);
    }
  }

  /**
   * Execute the "list" command.
   *
   * @param args arguments for the "list" command.
   *     args[0] = "list"
   *     args[1] = category ("all", "folders", "documents", "spreadsheets", "pdfs",
   *         "presentations", "starred", "trashed")
   *     args[2] = folderId (required if args[1] is "folder")
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeList(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    DocumentListFeed feed = null;
    String msg = "";

    switch(args.length) {
    case 1:
      msg = "List of docs: ";
      feed = documentList.getDocsListFeed("all");
      break;
    case 2:
      msg = "List of all " + args[1] + ": ";
      feed = documentList.getDocsListFeed(args[1]);
      break;
    case 3:
      if (args[1].equals("folder")) {
        msg = "Contents of folder_id '" + args[2] + "': ";
        feed = documentList.getFolderDocsListFeed(args[2]);
      }
      break;
    }

    if (feed != null) {
      out.println(msg);
      for (DocumentListEntry entry : feed.getEntries()) {
        printDocumentEntry(entry);
      }
    } else {
      printMessage(COMMAND_HELP_LIST);
    }
  }

  /**
   * Execute the "move" command.
   *
   * @param args arguments for the "move" command.
   *     args[0] = "move"
   *     args[1] = objectId (the id of the object to move)
   *     args[2] = folderid (the id of the folder to move the object to)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeMove(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      printDocumentEntry(documentList.moveObjectToFolder(args[1], args[2]));
    } else {
      printMessage(COMMAND_HELP_MOVE);
    }
  }

  /**
   * Execute the "perms" command.
   *
   * @param args arguments for the "perms" command.
   *     args[0] = "perms"
   *     args[1] = "list"
   *         args[2] = objectId
   *     args[1] = "add", "change"
   *         args[2] = role
   *         args[3] = scope
   *         args[4] = email
   *         args[5] = objectId
   *     args[1] = "remove"
   *         args[2] = scope
   *         args[3] = email
   *         args[4] = objectId
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executePerms(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length < 3) {
      printMessage(COMMAND_HELP_PERMS);
      return;
    }

    if (args[1].equals("list") && args.length == 3) {
      AclFeed feed = documentList.getAclFeed(args[2]);
      if (feed != null) {
        for (AclEntry entry : feed.getEntries()) {
          printAclEntry(entry);
        }
      }
    } else if (args[1].equals("add") && args.length == 6) {
      AclRole role = new AclRole(args[2]);
      AclScope scope;
      if (args[3].equals("user")) {
        scope = new AclScope(AclScope.Type.USER, args[4]);
      } else if (args[3].equals("domain")) {
        scope = new AclScope(AclScope.Type.DOMAIN, args[4]);
      } else {
        printMessage(COMMAND_HELP_PERMS);
        return;
      }
      printAclEntry(documentList.addAclRole(role, scope, args[5]));
    } else if (args[1].equals("change") && args.length == 6) {
      AclRole role = new AclRole(args[2]);
      AclScope scope;
      if (args[3].equals("user")) {
        scope = new AclScope(AclScope.Type.USER, args[4]);
      } else if (args[3].equals("domain")) {
        scope = new AclScope(AclScope.Type.DOMAIN, args[4]);
      } else {
        printMessage(COMMAND_HELP_PERMS);
        return;
      }
      printAclEntry(documentList.changeAclRole(role, scope, args[5]));
    } else if (args[1].equals("remove") && args.length == 5) {
      documentList.removeAclRole(args[2], args[3], args[4]);
    } else {
      printMessage(COMMAND_HELP_PERMS);
    }
  }

  /**
   * Execute the "remove" command.
   *
   * @param args arguments for the "remove" command.
   *     args[0] = "remove"
   *     args[1] = objectId
   *     args[2] = folderid
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeRemove(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      documentList.removeFromFolder(args[1], args[2]);
    } else {
      printMessage(COMMAND_HELP_REMOVE);
    }
  }

  /**
   * Execute the "search" command.
   *
   * @param args arguments for the "search" command.
   *     args[0] = "search"
   *     args[1] = searchString
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeSearch(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 2) {
      HashMap<String, String> searchParameters = new HashMap<String, String>();
      searchParameters.put("q", args[1]);

      DocumentListFeed feed = documentList.search(searchParameters);
      out.println("Results for [" + args[1] + "]");
      for (DocumentListEntry entry : feed.getEntries()) {
        printDocumentEntry(entry);
      }
    } else {
      printMessage(COMMAND_HELP_SEARCH);
    }
  }

  /**
   * Execute the "asearch" (advanced search) command.
   *
   * @param args arguments for the "asearch" command.
   *     args[0] = "asearch"
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeAdvancedSearch(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length <= 1) {
      printMessage(COMMAND_HELP_ASEARCH);
      return;
    }

    HashMap<String, String> searchParameters = new HashMap<String, String>();
    for (int i = 1; i < args.length; ++i) {
      searchParameters.put(args[i].substring(0, args[i].indexOf("=")),
          args[i].substring(args[i].indexOf("=") + 1));
    }

    DocumentListFeed feed = documentList.search(searchParameters);
    out.println("Results for advanced search:");
    for (DocumentListEntry entry : feed.getEntries()) {
      printDocumentEntry(entry);
    }
  }

  /**
   * Execute the "upload" command.
   *
   * @param args arguments for the "upload" command.
   *     args[0] = "upload"
   *     args[1] = filepath (path and filename of the file to be uploaded)
   *     args[2] = title (title to be used for the uploaded file)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeUpload(String[] args) throws IOException, MalformedURLException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      DocumentListEntry entry = documentList.uploadFile(args[1], args[2]);
      printDocumentEntry(entry);
    } else {
      printMessage(COMMAND_HELP_UPLOAD);
    }
  }

  /**
   * Execute the "help" command.
   *
   * @param args arguments for the "help" command.
   *     args[0] = "help"
   *     args[1] = command
   */
  private void executeHelp(String[] args) {
    if (args.length == 1) {
      printMessage(COMMAND_HELP_MESSAGE);
    } else if (args.length == 2) {
      if (HELP_MESSAGES.containsKey(args[1])) {
        printMessage(HELP_MESSAGES.get(args[1]));
      } else {
        printMessage(HELP_MESSAGES.get("error"));
      }
    } else {
      printMessage(COMMAND_HELP_MESSAGE);
    }
  }

  /**
   * Gets the type of file from the extension on the filename.
   *
   * @param filename the filename to extract the type of file from.
   */
  private String getTypeFromFilename(String filename) {
    return filename.substring(filename.lastIndexOf(".") + 1);
  }


  /**
   * Parses the command entered by the user into individual arguments.
   *
   * @param command the entire command entered by the user to be broken up into arguments.
   */
  private String[] parseCommand(String command) {
    // Special cases:
    if (command.startsWith("search")) {
      // if search command, only break into two args (command, search_string)
      return command.trim().split(" ", 2);
    } else if (command.startsWith("create")) {
      // if create command, break into three args (command, file_type, title)
      return command.trim().split(" ", 3);
    } else if (command.startsWith("upload")) {
      // if uplaod command, break into three args (command, file_path, title)
      return command.trim().split(" ", 3);
    }

    // Default case, split into n args using a space as the separator.
    return command.trim().split(" ");

  }

  /**
   * Reads and executes one command.
   *
   * @param reader to read input from the keyboard
   * @return false if the user quits, true on exception
   */
  private boolean executeCommand(BufferedReader reader) {
    System.err.print("Command: ");

    try {
      String command = reader.readLine();
      if (command == null) {
        return false;
      }

      String[] args = parseCommand(command);
      String name = args[0];

      if (name.equals("create")) {
        executeCreate(args);
      } else if (name.equals("trash")) {
        executeTrash(args);
      } else if (name.equals("download")) {
        executeDownload(args);
      } else if (name.equals("list")) {
        executeList(args);
      } else if (name.equals("move")) {
        executeMove(args);
      } else if (name.equals("perms")) {
        executePerms(args);
      } else if (name.equals("remove")) {
        executeRemove(args);
      } else if (name.equals("search")) {
        executeSearch(args);
      } else if (name.equals("asearch")) {
        executeAdvancedSearch(args);
      } else if (name.equals("upload")) {
        executeUpload(args);
      } else if (name.equals("help")) {
        executeHelp(args);
      } else if (name.startsWith("q") || name.startsWith("exit")) {
        return false;
      } else {
        out.println("Unknown command. Type 'help' for a list of commands.");
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
   */
  public void run() {
    printMessage(WELCOME_MESSAGE);

    BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));

    while (executeCommand(reader)) {
    }
  }

  /**
   * Prints out a message.
   *
   * @param msg the message to be printed.
   */
  private static void printMessage(String[] msg) {
    for (String s : msg) {
      System.out.println(s);
    }
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
   * Runs the demo.
   *
   * @param args the command-line arguments
   *
   * @throws AuthenticationException if the service is unable to validate the
   *         username and password.
   * @throws MalformedURLException if the URL for the docs feed is invalid.
   * @throws DocumentListException
   */
  public static void main(String[] args)
      throws AuthenticationException, MalformedURLException, DocumentListException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String authProtocol = parser.getValue("auth_protocol");
    String authHost = parser.getValue("auth_host");
    String authSub = parser.getValue("authSub", "auth", "a");
    String user = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String protocol = parser.getValue("protocol");
    String host = parser.getValue("host", "s");
    boolean help = parser.containsKey("help", "h");

    if (authProtocol == null) {
      authProtocol = DocumentList.DEFAULT_AUTH_PROTOCOL;
    }

    if (authHost == null) {
      authHost = DocumentList.DEFAULT_AUTH_HOST;
    }

    if (protocol == null) {
      protocol = DocumentList.DEFAULT_PROTOCOL;
    }

    if (host == null) {
      host = DocumentList.DEFAULT_HOST;
    }

    if (help || (user == null || password == null) && authSub == null) {
      printMessage(USAGE_MESSAGE);
      System.exit(1);
    }

    if (parser.containsKey("log", "l")) {
      turnOnLogging();
    }

    DocumentListDemo demo = new DocumentListDemo(System.out);
    documentList = new DocumentList(APPLICATION_NAME, authProtocol, authHost, protocol, host);

    if (password != null) {
      documentList.login(user, password);
    } else {
      documentList.loginWithAuthSubToken(authSub);
    }

    demo.run();
  }
}
