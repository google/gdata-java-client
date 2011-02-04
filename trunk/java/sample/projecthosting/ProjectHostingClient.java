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
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.Person;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.projecthosting.BlockedOn;
import com.google.gdata.data.projecthosting.BlockedOnUpdate;
import com.google.gdata.data.projecthosting.Blocking;
import com.google.gdata.data.projecthosting.Cc;
import com.google.gdata.data.projecthosting.CcUpdate;
import com.google.gdata.data.projecthosting.IssueCommentsEntry;
import com.google.gdata.data.projecthosting.IssueCommentsFeed;
import com.google.gdata.data.projecthosting.IssuesEntry;
import com.google.gdata.data.projecthosting.IssuesFeed;
import com.google.gdata.data.projecthosting.Label;
import com.google.gdata.data.projecthosting.Owner;
import com.google.gdata.data.projecthosting.Updates;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a simple client that provides high-level operations on the Google
 * Code Project Hosting Issue Tracker Data API. It can also be used as a
 * command-line application to test out some of the features of the API.
 *
 * 
 */
public class ProjectHostingClient {

  private ProjectHostingService service;

  private static final String FEED_URI_BASE =
      "https://code.google.com/feeds/issues";

  private static final String PROJECTION = "/full";

  // User-provided input
  private String project;
  private String username;
  private String password;

  /** Issues API base URL constructed from the given project name. */
  private String issuesBaseUri;

  /** Default issues feed URL constructed from the given project name. */
  private URL issuesFeedUrl;

  /** Group 1 of the regex will match the ID of the issue. */
  private Pattern issueIdPattern;

  /** Group 1 of the regex will match the ID of the comment. */
  private Pattern commentIdPattern;

  private static final String DIVIDER =
      "-----------------------------------------------------------------------";

  /**
   * Constructs a new client.
   *
   * @throws AuthenticationException if authentication fails
   * @throws MalformedURLException if there's a problem with URL
   */
  public ProjectHostingClient(
      ProjectHostingService service, String project, String username,
      String password) throws AuthenticationException, MalformedURLException {
    this.service = service;
    this.project = project;
    this.username = username;
    this.password = password;

    // Login via ClientLogin
    if ((username != null) && (password != null)) {
      service.setUserCredentials(username, password);
    }

    issuesBaseUri = FEED_URI_BASE + "/p/" + project + "/issues";
    issuesFeedUrl = makeIssuesFeedUrl(project);
    String issuesBaseUriHttp = issuesBaseUri.replaceFirst("https", "http");
    issueIdPattern = Pattern.compile(
        issuesBaseUriHttp + PROJECTION + "/(\\d+)$");
    commentIdPattern = Pattern.compile(
        issuesBaseUriHttp + "/\\d+/comments" + PROJECTION + "/(\\d+)$");
  }

  /**
   * Getter method for {@code issuesFeedUrl}.
   */
  protected URL getIssuesFeedUrl() {
    return issuesFeedUrl;
  }

  /**
   * Setter method for {@code issuesFeedUrl}.
   */
  protected void setIssuesFeedUrl(URL url) {
    issuesFeedUrl = url;
  }

  /**
   * Constructs issues feed URL.
   *
   * @param proj name of the project for the issues feed
   * @throws MalformedURLException if there's a problem with URL
   */
  protected URL makeIssuesFeedUrl(String proj)
      throws MalformedURLException {
    return new URL(FEED_URI_BASE + "/p/" + proj + "/issues" + PROJECTION);
  }

  /**
   * Constructs issue entry URL.
   *
   * @param issueId ID number of an issue to construct the issue entry URL
   * @throws MalformedURLException if there's a problem with URL
   */
  protected URL makeIssueEntryUrl(String issueId)
      throws MalformedURLException {
    return new URL(issuesBaseUri + PROJECTION + "/" + issueId);
  }

  /**
   * Constructs comments feed URL.
   *
   * @param issueId ID number of an issue to construct the comments URL
   * @throws MalformedURLException if there's a problem with URL
   */
  protected URL makeCommentsFeedUrl(String issueId)
      throws MalformedURLException {
    return new URL(issuesBaseUri + "/" + issueId + "/comments" + PROJECTION);
  }

  /**
   * Constructs comment entry URL.
   *
   * @param issueId ID number of an issue the comment belongs to
   * @param commentId ID number of the comment
   * @throws MalformedURLException if there's a problem with URL
   */
  protected URL makeCommentEntryUrl(String issueId, String commentId)
      throws MalformedURLException {
    return new URL(issuesBaseUri + "/" + issueId + "/comments" + PROJECTION
                   + "/" + commentId);
  }

  /**
   * Retrieves issues feed.
   *
   * @param feedUrl feed URL of issues to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssuesFeed getIssuesFeed(URL feedUrl)
      throws IOException, ServiceException {
    return service.getFeed(feedUrl, IssuesFeed.class);
  }

  /**
   * Retrieves a particular issue entry.
   *
   * @param issueId ID number of an issue to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssuesEntry getIssueEntry(String issueId)
      throws IOException, ServiceException {
    return getIssueEntry(makeIssueEntryUrl(issueId));
  }

  /**
   * Retrieves a particular issue entry.
   *
   * @param entryUrl URL of an issue entry to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssuesEntry getIssueEntry(URL entryUrl)
      throws IOException, ServiceException {
    return service.getEntry(entryUrl, IssuesEntry.class);
  }

  /**
   * Retrieves comments feed.
   *
   * @param issueId ID number of an issue to retrieve comments from
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsFeed getCommentsFeed(String issueId)
      throws IOException, ServiceException {
    return getCommentsFeed(makeCommentsFeedUrl(issueId));
  }

  /**
   * Retrieves comments feed.
   *
   * @param feedUrl comments feed URL to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsFeed getCommentsFeed(URL feedUrl)
      throws IOException, ServiceException {
    return service.getFeed(feedUrl, IssueCommentsFeed.class);
  }

  /**
   * Retrieves a particular comment entry.
   *
   * @param issueId ID number of an issue the comment belongs to
   * @param commentId ID number of a comment to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsEntry getCommentEntry(
      String issueId, String commentId)
      throws IOException, ServiceException {
    return getCommentEntry(makeCommentEntryUrl(issueId, commentId));
  }

  /**
   * Retrieves a particular comment entry.
   *
   * @param entryUrl comment entry URL to retrieve
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsEntry getCommentEntry(URL entryUrl)
      throws IOException, ServiceException {
    return service.getEntry(entryUrl, IssueCommentsEntry.class);
  }

  /**
   * Inserts an issue entry to the issues feed.
   *
   * @param entry issue entry to insert
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssuesEntry insertIssue(IssuesEntry entry)
      throws IOException, ServiceException  {
    return service.insert(issuesFeedUrl, entry);
  }

  /**
   * Inserts a comment entry to the comments feed.
   *
   * @param issueId ID number of an issue to insert the comment entry to
   * @param entry comment entry to insert
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsEntry insertComment(
      String issueId, IssueCommentsEntry entry)
      throws IOException, ServiceException {
    return insertComment(makeCommentsFeedUrl(issueId), entry);
  }

  /**
   * Inserts a comment entry to the comments feed.
   *
   * @param commentsFeedUrl comments feed URL to insert the comment entry to
   * @param entry comment entry to insert
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssueCommentsEntry insertComment(
      URL commentsFeedUrl, IssueCommentsEntry entry)
      throws IOException, ServiceException {
    return service.insert(commentsFeedUrl, entry);
  }

  /**
   * Queries issues using the given query parameters.
   *
   * @param query issues query object with query parameters set
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected IssuesFeed queryIssues(IssuesQuery query)
      throws IOException, ServiceException {
    return service.query(query, IssuesFeed.class);
  }

  /**
   * Returns the numeric issue ID from the given {@code issueUrl}.
   *
   * @param issueUrl URI to find the issue ID from
   */
  protected String getIssueId(String issueUrl) {
    Matcher matcher = issueIdPattern.matcher(issueUrl);
    return matcher.matches() ? matcher.group(1) : null;
  }

  /**
   * Returns the numeric commentId from the given {@code commentUrl}.
   *
   * @param commentUrl URI to find the comment ID from
   */
  protected String getCommentId(String commentUrl) {
    Matcher matcher = commentIdPattern.matcher(commentUrl);
    return matcher.matches() ? matcher.group(1) : null;
  }

  /**
   * Prints issues in the given issues feed.
   *
   * @param issuesFeed issues feed to print
   */
  protected void printIssues(IssuesFeed issuesFeed) {
    for (IssuesEntry issueEntry : issuesFeed.getEntries()) {
      printIssue(issueEntry);
    }
  }

  /**
   * Prints an issue entry in human-readable format.
   *
   * @param entry issue entry to print
   */
  protected void printIssue(IssuesEntry entry) {
    System.out.println(DIVIDER);
    if (entry.getId() != null) {
      String issueId = getIssueId(entry.getId());
      System.out.printf("Issue #%s:\t%s\n", issueId, entry.getId());
    } else {
      System.out.println("Issue");
    }

    if (entry.getTitle() != null) {
      System.out.println("\tSummary\n\t\t" + entry.getTitle().getPlainText());
    }

    Person author = entry.getAuthors().get(0);
    printPerson("Reporter", author.getName(), author.getUri());

    TextContent textContent = (TextContent) entry.getContent();
    if ((textContent != null) && (textContent.getContent() != null)) {
      HtmlTextConstruct textConstruct =
          (HtmlTextConstruct) textContent.getContent();
      System.out.println("\tDescription\n\t\t" + textConstruct.getHtml());
    }

    if (entry.hasStatus()) {
      System.out.println("\tStatus\n\t\t" + entry.getStatus().getValue());
    }

    if (entry.hasOwner()) {
      Owner owner = entry.getOwner();
      printPerson(
          "Owner", owner.getUsername().getValue(),
          (owner.getUri() == null) ? null : owner.getUri().getValue());
    }

    if (entry.getLabels().size() > 0) {
      System.out.println("\tLabel");
      for (Label label : entry.getLabels()) {
        System.out.println("\t\t" + label.getValue());
      }
    }

    if (entry.getCcs().size() > 0) {
      System.out.println("\tCC");
      for (Cc cc : entry.getCcs()) {
        printPerson(
            null, cc.getUsername().getValue(),
            (cc.getUri() == null) ? null : cc.getUri().getValue());
      }
    }

    if (entry.getBlockedOns().size() > 0) {
      System.out.println("\tBlockedOn");
      for (BlockedOn blockedOn : entry.getBlockedOns()) {
        System.out.print("\t\t");
        if (blockedOn.hasProject()) {
          System.out.print(blockedOn.getProject().getValue() + ":");
        }
        System.out.println(blockedOn.getId().getValue());
      }
    }

    if (entry.getBlockings().size() > 0) {
      System.out.println("\tBlocking");
      for (Blocking blocking : entry.getBlockings()) {
        System.out.print("\t\t");
        if (blocking.hasProject()) {
          System.out.print(blocking.getProject().getValue() + ":");
        }
        System.out.println(blocking.getId().getValue());
      }
    }

    if (entry.hasMergedInto()) {
      System.out.print("\tMergedInto\n\t\t");
      if (entry.getMergedInto().hasProject()) {
        System.out.print(entry.getMergedInto().getProject().getValue() + ":");
      }
      System.out.println(entry.getMergedInto().getId().getValue());
    }
  }

  /**
   * Prints Username and URI associated with the labeled person.
   *
   * @param label label to print
   * @param name username of the person to print
   * @param uri uri of the person to print, usually the profile url
   */
  protected void printPerson(String label, String name, String uri) {
    if (label != null) {
      System.out.printf("\t%s\n", label);
    }
    System.out.print("\t\t" + name);
    if (uri != null) {
      System.out.println("\t" + uri);
    } else {
      System.out.println();
    }
  }

  /**
   * Prints issues and their comments.
   *
   * @param issuesFeed issues feed to print
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected void printIssuesAndComments(IssuesFeed issuesFeed)
      throws IOException, ServiceException {
    for (IssuesEntry issueEntry : issuesFeed.getEntries()) {
      printIssueAndComments(issueEntry);
    }
  }

  /**
   * Prints an issue and its comments.
   *
   * @param issueId ID number of an issue to print
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected void printIssueAndComments(String issueId)
      throws IOException, ServiceException {
    printIssueAndComments(getIssueEntry(issueId));
  }

  /**
   * Prints an issue and its comments.
   *
   * @param issueUrl URL of an issue to print
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected void printIssueAndComments(URL issueUrl)
      throws IOException, ServiceException {
    printIssueAndComments(getIssueEntry(issueUrl));
  }

  /**
   * Prints an issue and its comments.
   *
   * @param issue issue entry to print
   * @throws IOException if there is a problem communicating with the server
   * @throws ServiceException if the service is unable to handle the request
   */
  protected void printIssueAndComments(IssuesEntry entry)
      throws IOException, ServiceException {
    printIssue(entry);
    String issueId = getIssueId(entry.getId());
    IssueCommentsFeed commentsFeed = getCommentsFeed(issueId);
    printComments(commentsFeed);
  }

  /**
   * Prints comments in the given comments feed.
   *
   * @param commentsFeed comments feed to print
   */
  protected void printComments(IssueCommentsFeed commentsFeed) {
    for (IssueCommentsEntry commentEntry : commentsFeed.getEntries()) {
      printComment(commentEntry);
    }
  }

  /**
   * Prints a comment entry in human-readable format.
   *
   * @param entry comment entry to print
   */
  protected void printComment(IssueCommentsEntry entry) {
    System.out.println(DIVIDER);
    if (entry.getId() != null) {
      String commentId = getCommentId(entry.getId());
      System.out.printf("Comment #%s:\t%s\n", commentId, entry.getId());
    } else {
      System.out.println("Comment");
    }

    Person author = entry.getAuthors().get(0);
    printPerson("Author", author.getName(), author.getUri());

    TextContent textContent = (TextContent) entry.getContent();
    if ((textContent != null) && (textContent.getContent() != null)) {
      HtmlTextConstruct textConstruct =
          (HtmlTextConstruct) textContent.getContent();
      System.out.println("\tComment\n\t\t" + textConstruct.getHtml());
    }

    if (entry.hasUpdates()) {
      Updates updates = entry.getUpdates();

      if (updates.hasSummary()) {
        System.out.println("\tSummary\n\t\t" + updates.getSummary().getValue());
      }

      if (updates.hasStatus()) {
        System.out.println("\tStatus\n\t\t" + updates.getStatus().getValue());
      }

      if (updates.hasOwnerUpdate()) {
        System.out.println(
            "\tOwner\n\t\t" + updates.getOwnerUpdate().getValue());
      }

      if (updates.getLabels().size() > 0) {
        System.out.println("\tLabel");
        for (Label label : updates.getLabels()) {
          System.out.println("\t\t" + label.getValue());
        }
      }

      if (updates.getCcUpdates().size() > 0) {
        System.out.println("\tCC");
        for (CcUpdate cc : updates.getCcUpdates()) {
          System.out.println("\t\t" + cc.getValue());
        }
      }

      if (updates.getBlockedOnUpdates().size() > 0) {
        System.out.println("\tBlockedOnUpdate");
        for (BlockedOnUpdate blockedOnUpdate : updates.getBlockedOnUpdates()) {
          System.out.println("\t\t" + blockedOnUpdate.getValue());
        }
      }

      if (updates.hasMergedIntoUpdate()) {
        System.out.println(
            "\tMergedIntoUpdate\n\t\t" +
            updates.getMergedIntoUpdate().getValue());
      }
    }
  }
}
