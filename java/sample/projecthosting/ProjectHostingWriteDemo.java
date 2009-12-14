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

import com.google.gdata.client.projecthosting.ProjectHostingService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.projecthosting.Cc;
import com.google.gdata.data.projecthosting.CcUpdate;
import com.google.gdata.data.projecthosting.IssueCommentsEntry;
import com.google.gdata.data.projecthosting.IssuesEntry;
import com.google.gdata.data.projecthosting.Label;
import com.google.gdata.data.projecthosting.Owner;
import com.google.gdata.data.projecthosting.OwnerUpdate;
import com.google.gdata.data.projecthosting.SendEmail;
import com.google.gdata.data.projecthosting.Status;
import com.google.gdata.data.projecthosting.Summary;
import com.google.gdata.data.projecthosting.Updates;
import com.google.gdata.data.projecthosting.Username;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Demonstrates how to use the Google Data API's Java client library to
 * interface with the Google Code Issue Tracker Data API.
 * There are examples for the following operations:
 *
 * <ol>
 * <li>Creating a new issue</li>
 * <li>Updating the issue by adding a comment with updates</li>
 * <li>Closing the issue by adding a comment with status "Fixed"</li>
 * </ol>
 *
 * 
 */
public class ProjectHostingWriteDemo {

  /** Disable default public constructor */
  private ProjectHostingWriteDemo() {
  }

  /** Client that provides high level operations of the API */
  private ProjectHostingClient client;

  private String username;

  /**
   * Constructs the command line application.
   *
   * @throws AuthenticationException if authentication fails
   * @throws MalformedURLException if there's a problem with URL
   */
  public ProjectHostingWriteDemo(
      ProjectHostingService service, String project, String username,
      String password) throws AuthenticationException, MalformedURLException {
    this.username = username;
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
    if (help || (project == null) || (username == null) || (password == null)) {
      usage();
      System.exit(help ? 0 : 1);
    }

    ProjectHostingService service =
        new ProjectHostingService("projecthosting-write-demo");

    try {
      new ProjectHostingWriteDemo(service, project, username, password).run();
    } catch (AuthenticationException e) {
      System.out.println("The username/password entered is invalid.");
      System.exit(1);
    }
  }

  /**
   * Creates a new issue and adds two comments to it, first to update the
   * issue and second to close it.
   *
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  private void run() throws IOException, ServiceException {
    // Create an issue
    IssuesEntry issueInserted = client.insertIssue(makeNewIssue());

    String issueId = client.getIssueId(issueInserted.getId());
    System.out.println("Issue #" + issueId + " created");

    // Add comments and updates to the issue created
    addComment(issueId, makeUpdatingComment());
    addComment(issueId, makePlainComment());
    addComment(issueId, makeClosingComment());

    // Print the issue and comments added
    System.out.println("-----------------------------------------------------");
    System.out.println("Issue created and comments added:");
    client.printIssueAndComments(issueInserted);
  }

  /**
   * Adds the given comment to the given issue.
   *
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  private void addComment(String issueId, IssueCommentsEntry issueComment)
      throws IOException, ServiceException {
    IssueCommentsEntry commentInserted = client.insertComment(
        issueId, issueComment);
    String commentId = client.getCommentId(commentInserted.getId());
    System.out.println("Comment #" + commentId + " added in issue #" + issueId);
  }

  /**
   * Creates a new issue that can be inserted to the issues feed.
   */
  protected IssuesEntry makeNewIssue() {
    Person author = new Person();
    author.setName(username);

    Owner owner = new Owner();
    owner.setUsername(new Username(username));

    Cc cc = new Cc();
    cc.setUsername(new Username(username));

    IssuesEntry entry = new IssuesEntry();
    entry.getAuthors().add(author);

    // Uncomment the following line to set the owner along with issue creation.
    // It's intentionally commented out so we can demonstrate setting the owner
    // field using setOwnerUpdate() as shown in makeUpdatingComment() below.
    // entry.setOwner(owner);

    entry.setContent(new HtmlTextConstruct("issue description"));
    entry.setTitle(new PlainTextConstruct("issue summary"));
    entry.setStatus(new Status("New"));
    entry.addLabel(new Label("Priority-High"));
    entry.addLabel(new Label("Milestone-2009"));
    entry.addCc(cc);
    entry.setSendEmail(new SendEmail("False"));

    return entry;
  }

  /**
   * Creates a comment that updates an existing issue.
   */
  protected IssueCommentsEntry makeUpdatingComment() {
    Person author = new Person();
    author.setName(username);

    // Create issue updates
    Updates updates = new Updates();
    updates.setSummary(new Summary("New issue summary"));
    updates.setStatus(new Status("Accepted"));
    updates.setOwnerUpdate(new OwnerUpdate(username));
    updates.addLabel(new Label("-Priority-High"));
    updates.addLabel(new Label("Priority-Low"));
    updates.addLabel(new Label("-Milestone-2009"));
    updates.addLabel(new Label("Milestone-2010"));
    updates.addLabel(new Label("Type-Enhancement"));
    updates.addCcUpdate(new CcUpdate("-" + username));

    // Create issue comment entry
    IssueCommentsEntry entry = new IssueCommentsEntry();
    entry.getAuthors().add(author);
    entry.setContent(new HtmlTextConstruct("some comment"));
    entry.setUpdates(updates);
    entry.setSendEmail(new SendEmail("False"));

    return entry;
  }

  /**
   * Creates a comment without any updates.
   */
  protected IssueCommentsEntry makePlainComment() {
    Person author = new Person();
    author.setName(username);

    // Create issue comment entry
    IssueCommentsEntry entry = new IssueCommentsEntry();
    entry.getAuthors().add(author);
    entry.setContent(new HtmlTextConstruct("Some comment"));
    entry.setSendEmail(new SendEmail("False"));

    return entry;
  }

  /**
   * Creates a comment that closes an issue by setting Status to "Fixed".
   */
  protected IssueCommentsEntry makeClosingComment() {
    Person author = new Person();
    author.setName(username);

    // Set the Status as Fixed
    Updates updates = new Updates();
    updates.setStatus(new Status("Fixed"));

    // Create issue comment entry
    IssueCommentsEntry entry = new IssueCommentsEntry();
    entry.getAuthors().add(author);
    entry.setContent(new HtmlTextConstruct("This was fixed last week."));
    entry.setUpdates(updates);
    entry.setSendEmail(new SendEmail("False"));

    return entry;
  }

  /**
   * Prints usage of this application.
   */
  private static void usage() {
    System.out.println(
        "Syntax: ProjectHostingWriteDemo --project <project> "
        + "--username <username> --password <password>\n"
        + "\t<project>\tProject on which the demo will run. This demo will "
        + "create a new issue in the given project and add comments to it.\n"
        + "\t<username>\tGoogle Account username\n"
        + "\t<password>\tGoogle Account password\n");
  }
}
