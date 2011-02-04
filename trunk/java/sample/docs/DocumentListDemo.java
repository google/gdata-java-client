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
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.RevisionEntry;
import com.google.gdata.data.docs.RevisionFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
  private DocumentList documentList;
  private PrintStream out;

  private static final String APPLICATION_NAME = "JavaGDataClientSampleAppV3.0";

  /**
   * The message for displaying the usage parameters.
   */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java DocumentListDemo.jar --username <user> --password <pass>",
      "Usage: java DocumentListDemo.jar --authSub <token>",
      "    [--host <host:port>]          Where is the feed (default = docs.google.com)",
      "    [--log]                       Enable logging of requests",
      ""};

  /**
   * Welcome message, introducing the program.
   */
  private final String[] WELCOME_MESSAGE = {
      "", "This is a demo of the document list feed!",
      "Using this interface, you can read and upload your documents.",
      "Type 'help' for a list of commands.", ""};

  /**
   * Help on all available commands.
   */
  private final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      "    create <object_type> <name>               [[create an object]]",
      "    trash <resource_id> [delete]              [[puts the object into the trash]]",
      "    download <resource_id> <file_path>        [[downloads the object to the folder"
          + " specified by file_path]]",
      "    list [object_type] [...]                  [[lists objects]]",
      "    move <resource_id> <folder_id>            [[moves an object into a folder]]",
      "    perms <operation> [...]                   [[lists or modifies file permissions]]",
      "    remove <resource_id> <folder_resource_id> [[removes an object from a folder]]",
      "    search <search_text>                      [[search documents for text strings]]",
      "    asearch <search_option>                   [[advanced search]]",
      "    upload <file_path> <title>                [[uploads a object]]",
      "    revisions <resource_id>                   [[lists revisions of a document]]",
      "",
      "    help [command]                            [[display this message, or info about"
          + " the specified command]]",
      "    exit                                      [[exit the program]]"};

  private final String[] COMMAND_HELP_CREATE = {
      "create <object_type> <name>",
      "    object_type: document, spreadsheet, folder.",
      "    name: the name for the new object"};
  private final String[] COMMAND_HELP_TRASH = {
      "trash <resource_id> [delete]",
      "    resource_id: the resource id of the object to be deleted",
      "    \"delete\": Specify to permanently delete the document instead of just trashing it."};
  private final String[] COMMAND_HELP_DOWNLOAD = {
      "download <resource_id> <file_path>",
      "    resource_id: the resource id of the file you wish to download",
      "    file_path: the path to the directory to save the file in"};
  private final String[] COMMAND_HELP_LIST = {
      "list [object_type]",
      "    object_type: all, starred, documents, spreadsheets, pdfs, presentations, folders.\n"
          + "        (defaults to 'all')", "list folder <folder_id>",
      "    folder_id: The id of the folder you want the contents list for."};
  private final String[] COMMAND_HELP_MOVE = {
      "move <resource_id> <folder_id>",
      "   resource_id: the resource id of the object to be moved",
      "    folder_id: the folder to move the document into"};
  private final String[] COMMAND_HELP_PERMS = {
      "perms list <resource_id>",
      "    resource_id: the resource id of the object you wish to add/list permissions for",
      "perms add <role> <scope> <email> <object_id>",
      "    role: \"reader\", \"writer\", \"owner\"",
      "    scope: \"user\", \"domain\"",
      "    email: The user's email address or domain name (\"user@gmail.com\", \"domain.com\")",
      "    resource_id: The resource id of the object to change permissions for.",
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

  private final String[] COMMAND_HELP_REMOVE = {
      "remove <object_id> <folder_id>",
      "    object_id: the id of the object to remove from the folder",
      "    folder_id: the id of the folder to remove the object from"};
  private final String[] COMMAND_HELP_SEARCH = {
      "search <search_text>",
      "    search_text: A string to be used for a full text query"};
  private final String[] COMMAND_HELP_ASEARCH = {
      "asearch [<query_param>=<value>] [<query_param2>=<value2>] ...",
      "    query_param: title, title-exact, opened-min, opened-max, owner, writer, reader, "
          + "showfolders, etc.", "    value: The value of the parameter"};
  private final String[] COMMAND_HELP_UPLOAD = {
      "upload <file_path> <title>", "    file_path: file to upload",
      "    title: A title to call the document"};
  private final String[] COMMAND_HELP_REVISIONS = {
      "revisions <resource_id>", "    resource_id: document resource id"};
  private final String[] COMMAND_HELP_HELP = {
      "help [command]", "    Weeeeeeeeeeeeee..."};
  private final String[] COMMAND_HELP_EXIT = {
      "exit", "    Exit the program."};
  private final String[] COMMAND_HELP_ERROR = {"unknown command"};

  private final Map<String, String[]> HELP_MESSAGES;
  {
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
    HELP_MESSAGES.put("revisions", COMMAND_HELP_REVISIONS);
    HELP_MESSAGES.put("help", COMMAND_HELP_HELP);
    HELP_MESSAGES.put("exit", COMMAND_HELP_EXIT);
    HELP_MESSAGES.put("error", COMMAND_HELP_ERROR);
  }

  /**
   * Constructor
   *
   * @param outputStream Stream to print output to.
   * @throws DocumentListException
   */
  public DocumentListDemo(PrintStream outputStream, String appName, String host)
      throws DocumentListException {
    out = outputStream;
    documentList = new DocumentList(appName, host);
  }

  /**
   * Authenticates the client using ClientLogin
   *
   * @param username User's email address
   * @param password User's password
   * @throws DocumentListException
   * @throws AuthenticationException
   */
  public void login(String username, String password) throws AuthenticationException,
      DocumentListException {
    documentList.login(username, password);
  }

  /**
   * Authenticates the client using AuthSub
   *
   * @param authSubToken authsub authorization token.
   * @throws DocumentListException
   * @throws AuthenticationException
   */
  public void login(String authSubToken)
      throws AuthenticationException, DocumentListException {
    documentList.loginWithAuthSubToken(authSubToken);
  }

  /**
   * Prints out the specified document entry.
   *
   * @param doc the document entry to print.
   */
  public void printDocumentEntry(DocumentListEntry doc) {
    StringBuffer output = new StringBuffer();

    output.append(" -- " + doc.getTitle().getPlainText() + " ");
    if (!doc.getParentLinks().isEmpty()) {
      for (Link link : doc.getParentLinks()) {
        output.append("[" + link.getTitle() + "] ");
      }
    }
    output.append(doc.getResourceId());

    out.println(output);
  }

  /**
   * Prints out the specified revision entry.
   *
   * @param doc the revision entry to print.
   */
  public void printRevisionEntry(RevisionEntry entry) {
    StringBuffer output = new StringBuffer();

    output.append(" -- " + entry.getTitle().getPlainText());
    output.append(", created on " + entry.getUpdated().toUiString() + " ");
    output.append(" by " + entry.getModifyingUser().getName() + " - "
        + entry.getModifyingUser().getEmail() + "\n");
    output.append("    " + entry.getHtmlLink().getHref());

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
   *     args[0] = "create" args[1] = object_type ("folder", "document", "spreadsheet")
   *     args[2] = title (what to name the document/folder)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeCreate(String[] args) throws IOException,
      MalformedURLException, ServiceException, DocumentListException {
    if (args.length == 3) {
      printDocumentEntry(documentList.createNew(args[2], args[1]));
    } else {
      printMessage(COMMAND_HELP_CREATE);
    }
  }

  /**
   * Executes the "trash" command.
   *
   * @param args arguments for the "trash" command.
   *     args[0] = "trash"
   *     args[1] = resourceid (the resource id of the object to be trashed)
   *     args[2] = delete (where to delete permanently or not)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeTrash(String[] args) throws IOException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      documentList.trashObject(args[1], true);
    } else if (args.length == 2) {
      documentList.trashObject(args[1], false);
    } else {
      printMessage(COMMAND_HELP_TRASH);
    }
  }

  /**
   * Executes the "download" command.
   *
   * @param args arguments for the "download" command.
   *     args[0] = "download"
   *     args[1] = resourceId (the resource id of the object to be downloaded)
   *     args[2] = filepath (the path and filename to save the object as)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeDownload(String[] args) throws IOException,
      ServiceException, DocumentListException {
    if (args.length == 3) {
      String docType = documentList.getResourceIdPrefix(args[1]);
      if (docType.equals("spreadsheet")) {
        String format = documentList.getDownloadFormat(args[1],
            getTypeFromFilename(args[2]));
        documentList.downloadSpreadsheet(args[1], args[2], format);
      } else if (docType.equals("presentation")) {
        String format = documentList.getDownloadFormat(args[1],
            getTypeFromFilename(args[2]));
        documentList.downloadPresentation(args[1], args[2], format);
      } else if (docType.equals("document")) {
        String format = documentList.getDownloadFormat(args[1],
            getTypeFromFilename(args[2]));
        documentList.downloadDocument(args[1], args[2], format);
      } else {
        MediaContent mc = (MediaContent) documentList.getDocsListEntry(args[1]).getContent();
        String fileExtension = mc.getMimeType().getSubType();
        URL exportUrl = new URL(mc.getUri());

        // PDF file cannot be exported in different formats.
        String requestedFormat = args[2]
            .substring(args[2].lastIndexOf(".") + 1);
        if (!requestedFormat.equals(fileExtension)) {

          String[] formatWarning = {"Warning: "
              + mc.getMimeType().getMediaType() + " cannot be downloaded as a "
              + requestedFormat + ". Using ." + fileExtension + " instead."};
          printMessage(formatWarning);
        }
        String newFilePath = args[2].substring(0, args[2].lastIndexOf(".") + 1) + fileExtension;
        documentList.downloadFile(exportUrl, newFilePath);
      }
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
   *        "presentations", "starred", "trashed")
   *     args[2] = folderId (required if args[1] is "folder")
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeList(String[] args) throws IOException,
      ServiceException, DocumentListException {
    DocumentListFeed feed = null;
    String msg = "";

    switch (args.length) {
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
   *     args[0] = "move" args[1] = resourceid (the resourceid of the object to move)
   *     args[2] = folderResourceId (the resource id of the folder to move the object to)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeMove(String[] args) throws IOException,
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
   *         args[2] = resourceId
   *     args[1] = "add", "change"
   *         args[2] = role
   *         args[3] = scope
   *         args[4] = email
   *         args[5] = resourceId
   *     args[1] = "remove"
   *         args[2] = scope
   *         args[3] = email
   *         args[4] = resourceId
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executePerms(String[] args) throws IOException,
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
   *     args[1] = resourceId
   *     args[2] = folderReourceId
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeRemove(String[] args) throws IOException,
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
  private void executeSearch(String[] args) throws IOException,
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
  private void executeAdvancedSearch(String[] args) throws IOException,
      ServiceException, DocumentListException {
    if (args.length <= 1) {
      printMessage(COMMAND_HELP_ASEARCH);
      return;
    }

    HashMap<String, String> searchParameters = new HashMap<String, String>();
    for (int i = 1; i < args.length; ++i) {
      searchParameters.put(args[i].substring(0, args[i].indexOf("=")), args[i]
          .substring(args[i].indexOf("=") + 1));
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
  private void executeUpload(String[] args) throws IOException,
      ServiceException, DocumentListException, InterruptedException {
    if (args.length == 3) {
      DocumentListEntry entry = documentList.uploadFile(args[1], args[2]);
      printDocumentEntry(entry);
    } else {
      printMessage(COMMAND_HELP_UPLOAD);
    }
  }

  /**
   * Execute the "revisions" command.
   *
   * @param args arguments for the "upload" command.
   *     args[0] = "revisions"
   *     args[1] = resourceId (the resource id of the object to fetch revisions for)
   *
   * @throws IOException when an error occurs in communication with the Doclist
   *         service.
   * @throws MalformedURLException when an malformed URL is used.
   * @throws ServiceException when the request causes an error in the Doclist
   *         service.
   * @throws DocumentListException
   */
  private void executeRevisions(String[] args) throws IOException,
      ServiceException, DocumentListException {
    if (args.length == 2) {
      RevisionFeed feed = documentList.getRevisionsFeed(args[1]);
      if (feed != null) {
        out.println("List of revisions...");
        for (RevisionEntry entry : feed.getEntries()) {
          printRevisionEntry(entry);
        }
      } else {
        printMessage(COMMAND_HELP_REVISIONS);
      }
    } else {
      printMessage(COMMAND_HELP_REVISIONS);
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
   * @param command the entire command entered by the user to be broken up into
   *        arguments.
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
      // if upload command, break into three args (command, file_path, title)
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
   * @throws IOException
   * @throws ServiceException
   */
  private boolean executeCommand(BufferedReader reader)
      throws IOException, ServiceException, InterruptedException {
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
      } else if (name.equals("revisions")) {
        executeRevisions(args);
      } else if (name.equals("help")) {
        executeHelp(args);
      } else if (name.startsWith("q") || name.startsWith("exit")) {
        return false;
      } else {
        out.println("Unknown command. Type 'help' for a list of commands.");
      }
    } catch (DocumentListException e) {
      // Show *exactly* what went wrong.
      e.printStackTrace();
    }
    return true;
  }

  /**
   * Starts up the demo and prompts for commands.
   *
   * @throws ServiceException
   * @throws IOException
   */
  public void run() throws IOException, ServiceException, InterruptedException {
    printMessage(WELCOME_MESSAGE);

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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
   * @throws DocumentListException
   * @throws ServiceException
   * @throws IOException
   */
  public static void main(String[] args)
      throws DocumentListException, IOException, ServiceException,
      InterruptedException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String authSub = parser.getValue("authSub", "auth", "a");
    String user = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String host = parser.getValue("host", "s");
    boolean help = parser.containsKey("help", "h");

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

    DocumentListDemo demo = new DocumentListDemo(System.out, APPLICATION_NAME,
        host);

    if (password != null) {
      demo.login(user, password);
    } else {
      demo.login(authSub);
    }

    demo.run();
  }
}

