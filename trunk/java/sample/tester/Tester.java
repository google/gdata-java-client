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

// All Rights Reserved.

package sample.tester;

import com.google.gdata.client.GoogleService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;
import sample.util.SimpleCommandLineParser;

import java.net.URL;

/**
 * An application that serves as a sample to show how the GData
 * Service object can be used to create/read/update/delete data
 * in a Google service.
 *
 * 
 */
public class Tester {

  public static void main(String[] args) throws Exception {

    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String serviceName = parser.getValue("serviceName", "service", "s");
    String appName = parser.getValue("appName", "app", "a");
    String feedUrlString = parser.getValue("feedUrl", "feed", "f");
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    boolean updateEntry = parser.containsKey("update");
    boolean help = parser.containsKey("help", "h");

    if (help) {
      usage();
      System.exit(1);
    }

    if (serviceName == null) {
      throw new IllegalArgumentException("Must specify service name");
    }

    if (appName == null) {
      throw new IllegalArgumentException("Must specify application name");
    }

    if (feedUrlString == null) {
      throw new IllegalArgumentException("Must specify feed URL");
    }

    GoogleService service = new GoogleService(serviceName, appName);

    try {

      // URL of service endpoint.
      URL feedUrl = new URL(feedUrlString);

      // Set up authentication.
      if (username != null) {
        if (password == null) {
          throw new IllegalArgumentException("Must specify password");
        }
        service.setUserCredentials(username, password);
      }

      // Send the query request and receive the response.
      Feed feed = service.getFeed(feedUrl, Feed.class);

      // Print the title and update time of the returned feed.
      System.out.println("Feed title " + feed.getTitle().getPlainText() +
                         " (" + feed.getUpdated() + ")");

      // Print the title and update time and body of each entry.
      System.out.println("Entries:");
      for (Entry e : feed.getEntries()) {
        String content =
            (e.getContent() != null ?
            ((TextContent) e.getContent()).getContent().getPlainText() :
            "");
        System.out.println("  " + e.getTitle().getPlainText() +
                           " (" + e.getUpdated() + ")" +
                           (content.length() > 0 ? ": " : "") + content);
      }

      // Insert, update, and delete an entry if so requested.
      if (updateEntry) {
        BaseEntry newEntry = new Entry();
        newEntry.setTitle(new PlainTextConstruct("Sample entry title"));
        newEntry.setContent(new PlainTextConstruct("Sample entry content"));
        BaseEntry e = service.insert(feedUrl, newEntry);
        System.out.println("Inserted an entry, ID is " + e.getId());
        e.setContent(new PlainTextConstruct("New sample entry content"));
        service.update(new URL(e.getEditLink().getHref()), e);
        System.out.println("Updated the entry");
        service.delete(new URL(e.getEditLink().getHref()));
        System.out.println("Deleted the entry");
      }

    } catch (ServiceException e) {
      throw new RuntimeException(e.getMessage() + "\n" + e.getResponseBody());
    }

  }

  /**
   * Prints usage of this application.
   */
  private static void usage() {
    System.out.println("Usage: java Tester --serviceName [service] " +
                       "--appName [app] --feedUrl [feed] --username [user] " +
                       "--password [pass] --update");
    System.out.println(
        "\nA generic client for querying a GData feed and optionally\n" +
        "inserting/updating/deleting entries.\n\n" +
        "It uses the specified Google Account username and password to\n" +
        "query the specified feed URL and displays the title and content\n" +
        "of each entry returned. If --update is specified, it will insert\n" + 
        "an entry, update it, and then delete it.\n");
  }

}
