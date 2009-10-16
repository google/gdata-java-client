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


package sample.projecthosting;

import com.google.gdata.client.projecthosting.IssuesQuery;
import com.google.gdata.client.projecthosting.ProjectHostingService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.projecthosting.IssuesFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

/**
 * A Command-line application using the {@link ProjectHostingClient} to make
 * calls to the Google Code Issue Tracker Data API.
 * The following operations are handled:
 *
 * <ol>
 * <li>Retrieving the list of issues in a project</li>
 * <li>Querying issues with query parameters</li>
 * <li>Retrieving the list of comments in a given issue</li>
 * </ol>
 *
 * 
 */
public class ProjectHostingReadDemo {

  /** Disable default public constructor */
  private ProjectHostingReadDemo() {
  }

  /** Client that provides high level operations of the API */
  private ProjectHostingClient client;

  private static final String DIVIDER =
      "=====================================================================\n";

  /**
   * Constructs the command line application.
   *
   * @throws AuthenticationException if authentication fails
   * @throws MalformedURLException if there's a problem with URL
   */
  public ProjectHostingReadDemo(
      ProjectHostingService service, String project, String username,
      String password) throws AuthenticationException, MalformedURLException {
    client = new ProjectHostingClient(service, project, username, password);
  }

  /**
   * Main entry point. Parses arguments and creates and invokes the demo.
   */
  public static void main(String[] arg) throws Exception {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);

    // Parse command-line flags
    String project = parser.getValue("project");
    String username = parser.getValue("username");
    String password = parser.getValue("password");

    boolean help = parser.containsKey("help");
    if (help || (project == null)) {
      usage();
      System.exit(help ? 0 : 1);
    }

    if (username == null) {
      System.out.println(
          "***WARNING*** Unauthenticated user. To see any restricted issues, "
          + "you must authenticate yourself and have proper permission setting "
          + "for the project " + project + ".\n"
          + "You can authenticate yourself by specifying <username> and "
          + "<password> when you invoke this demo as follows:");
      usage();
    }

    ProjectHostingService service =
        new ProjectHostingService("projecthosting-read-demo");

    try {
      new ProjectHostingReadDemo(service, project, username, password).run();
    } catch (AuthenticationException e) {
      System.out.println("The username/password entered is invalid.");
      System.exit(1);
    }
  }

  /** Input stream for reading user input. */
  private static final BufferedReader IN =
      new BufferedReader(new InputStreamReader(System.in));

  /**
   * Prompts and returns user input as an integer.
   * It will keep asking until the user provides a valid number.
   *
   * @throws IOException if there is a I/O related problem
   */
  private static int readInteger(String name) throws IOException {
    while (true) {
      String input = readString(name);
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException nfe) {
        System.out.println("Invalid number " + input);
      }
    }
  }

  /**
   * Prompts and returns user input as a string.
   *
   * @throws IOException if there is a I/O related problem
   */
  private static String readString(String name) throws IOException {
    System.out.print("Please enter " + name + ": ");
    System.out.flush();
    String result = IN.readLine();
    return result.trim();
  }

  /**
   * Runs the main loop for reading issues and comments.
   * It offers a choice for users to read issues or a specific issue
   * and its comments and continues to do so until "exit" is selected.
   *
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  private void run() throws IOException, ServiceException {
    while (true) {
      System.out.println(
          DIVIDER
          + "Main menu:\n"
          + "[0] Exit\n"
          + "[1] Read issues\n"
          + "[2] Read an issue and its comments");
      int choice = readInteger("action");
      switch (choice) {
        case 0:
          return;
        case 1:
          addAndRunQuery();
          break;
        case 2:
          String issueId = readString("issue ID");
          client.printIssueAndComments(issueId);
          break;
        default:
          System.out.println("Invalid choice " + choice);
          break;
      }
    }
  }

  /**
   * Builds and runs a query. The user is prompted to add query parameters
   * until "done" is selected at which point it runs the query and displays
   * issues returned.
   * Choice #1 runs the query and shows the issues returned.
   * Choice #2-13 lets users add query parameters.
   *
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  private void addAndRunQuery() throws IOException, ServiceException {
    IssuesQuery query = new IssuesQuery(client.getIssuesFeedUrl());
    while (true) {
      System.out.println(
          DIVIDER
          + "Set query parameters. Choose [1] when you're done.\n"
          + "[0] Return to the main menu\n"
          + "[1] Done. Query issues now.\n"
          + "[2] Set full-text query\n"
          + "[3] Set published-min\n"
          + "[4] Set published-max\n"
          + "[5] Set updated-min\n"
          + "[6] Set updated-max\n"
          + "[7] Set start-index\n"
          + "[8] Set max-results\n"
          + "[9] Set owner\n"
          + "[10] Set reporter\n"
          + "[11] Set status\n"
          + "[12] Set label\n"
          + "[13] Set canned-query");
      int choice = readInteger("action");
      switch (choice) {
        case 0:
          return;
        case 1:
          IssuesFeed resultFeed = client.queryIssues(query);
          int numResult = resultFeed.getEntries().size();
          System.out.println(
              "Query returned " + numResult + " matching issues.");
          client.printIssues(resultFeed);
          return;
        case 2:
          String textQuery = readString("full-text query");
          query.setFullTextQuery(textQuery);
          break;
        case 3:
          String publishedMin = readString("published-min");
          query.setPublishedMin(DateTime.parseDate(publishedMin));
          break;
        case 4:
          String publishedMax = readString("published-max");
          query.setPublishedMax(DateTime.parseDate(publishedMax));
          break;
        case 5:
          String updatedMin = readString("updated-min");
          query.setUpdatedMin(DateTime.parseDate(updatedMin));
          break;
        case 6:
          String updatedMax = readString("updated-max");
          query.setUpdatedMax(DateTime.parseDate(updatedMax));
          break;
        case 7:
          int startIndex = readInteger("start-index");
          query.setStartIndex(startIndex);
          break;
        case 8:
          int maxResults = readInteger("max-results");
          query.setMaxResults(maxResults);
          break;
        case 9:
          String owner = readString("owner");
          query.setOwner(owner);
          break;
        case 10:
          String reporter = readString("reporter");
          query.setAuthor(reporter);
          break;
        case 11:
          String status = readString("status");
          query.setStatus(status);
          break;
        case 12:
          String label = readString("label");
          query.setLabel(label);
          break;
        case 13:
          String cannedQuery = readString("canned-query");
          query.setCan(cannedQuery);
          break;
        default:
          System.out.println("Invalid choice " + choice);
          break;
      }
    }
  }

  /**
   * Prints usage of this application.
   */
  private static void usage() {
    System.out.println(
        "Syntax: ProjectHostingReadDemo --project <project> "
        + "[--username <username> --password <password>]\n"
        + "\t<project>\tProject on which the demo will run.\n"
        + "\t<username>\tGoogle Account username\n"
        + "\t<password>\tGoogle Account password\n");
  }
}
