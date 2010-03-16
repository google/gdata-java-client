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


package sample.youtube;

import com.google.gdata.client.Query;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demo of partial response and partial patch to change keywords on uploaded
 * videos.
 *
 * 
 */
public class YouTubePartialDemo {

  /**
   * Feed url for user uploaded videos.
   */
  public static final String UPLOADS_URL =
      "http://gdata.youtube.com/feeds/api/users/default/uploads";

  /**
   * Input stream for reading user input.
   */
  private static final BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(System.in));

  /** Steam to print status messages to. */
  private final PrintStream output;

  /** Constructor */
  private YouTubePartialDemo(PrintStream out) {
    this.output = out;
  }

  /**
   * Shows the usage of how to run the sample from the command-line.
   */
  private static void printUsage() {
    System.out.println("Usage: java YouTubePartialDemo.jar "
        + " --username <user@gmail.com> " + " --password <pass> "
        + " --key <developer key>");
  }

  /**
   * Displays a menu of the main activities a user can perform.
   */
  private void printMenu() {
    System.out.println("\n");
    System.out.println("Choose one of the following demo options:");
    System.out.println("\t1) Retrieve My uploaded videos with keywords");
    System.out.println("\t2) Update keywords for an uploaded video");
    System.out.println("\t0) Exit");
    System.out.println("\nEnter Number (0-2): ");
  }

  /**
   * Reads a line of text from the standard input.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return A line of text read from the standard input.
   */
  private static String readLine() throws IOException {
    return bufferedReader.readLine();
  }

  /**
   * Reads a line of text from the standard input and returns the parsed
   * integer representation.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return An integer read from the standard input.
   */
  private static int readInt() throws IOException {
    String input = readLine();

    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }

  /**
   * Print title, url of user's videos.
   */
  private void printVideos(YouTubeService service)
      throws IOException, ServiceException {

    Query query = new Query(new URL(UPLOADS_URL));
    query.setFields("title,entry(title, media:group/media:player)");
    VideoFeed videoFeed = service.query(query, VideoFeed.class);

    output.println(videoFeed.getTitle() + ":");
    int count = 1;
    for (VideoEntry entry : videoFeed.getEntries()) {
      output.println(count + ") "
          + entry.getTitle().getPlainText() + ": "
          + entry.getMediaGroup().getPlayer().getUrl());
    }
  }

  /**
   * Solicits the user for a video ID (hash) or tries to figure one out
   * from the video's watch URL.
   *
   * @return String containing a video ID.
   * @throws IOException If there are problems reading user input.
   */
  private static String readVideoID() throws IOException {
    System.out.println(
        "Input a valid video ID or a watch URL :");

    String input = readLine();
    if (input.equals("")) {
      throw new IOException("Invalid video id");
    }

    Pattern p = Pattern.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*");
    Matcher m = p.matcher(input);

    if (m.matches()) {
      input = m.group(1);
    }

    return input;
  }

  /**
   * Updates keywords associated with a selected video.
   */
  private void updateVideoKeywords(YouTubeService service)
      throws IOException, ServiceException {
    // Identify video to update
    output.println("First choose a video to update:");
    String videoID = readVideoID();
    URL entryUrl = new URL(UPLOADS_URL + "/" + videoID);

    // Select fields to update
    String fields = "@gd:etag,media:group/media:keywords";
    Query query = new Query(entryUrl);
    query.setFields(fields);

    // Get representation for the interested fields
    VideoEntry videoEntry = null;
    try {
      videoEntry = service.getEntry(query.getUrl(), VideoEntry.class);
    } catch (ServiceException se) {
      // an invalid video ID was used.
    }
    if (videoEntry == null) {
      output.println("Sorry, the video ID you entered was not valid.\n");
      return;
    }
    output.println("Current Keywords: "
        + videoEntry.getMediaGroup().getKeywords().getKeywords());

    // add a new keyword
    output.println("Specify a keyword to add: ");
    String keyword = readLine();
    videoEntry.getMediaGroup().getKeywords().addKeyword(keyword);
    VideoEntry updatedEntry = service.patch(entryUrl, fields, videoEntry);
    output.println("Keywords after update: "
        + updatedEntry.getMediaGroup().getKeywords().getKeywords());
  }

  public static void main(String[] args) throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String developerKey = parser.getValue("key", "k");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null || developerKey == null) {
      printUsage();
      System.exit(1);
    }

    YouTubeService service = new YouTubeService("gdata-YTPartialDemo-1",
        developerKey);

    try {
      service.setUserCredentials(username, password);
    } catch (AuthenticationException e) {
      System.out.println("Invalid login credentials.");
      System.exit(1);
    }

    YouTubePartialDemo demo = new YouTubePartialDemo(System.out);
    while (true) {
      try {
        demo.printMenu();
        int choice = readInt();

        switch (choice) {
          case 1:
            // Prints out the user's uploaded videos
            demo.printVideos(service);
            break;
          case 2:
            demo.updateVideoKeywords(service);
            break;
          case 0:
            System.exit(1);
            break;
        }
      } catch (IOException e) {
        // Communications error
        System.err.println(
            "There was a problem communicating with the service.");
        e.printStackTrace();
      } catch (ServiceException e) {
        // Server side error
        System.err.println("The server had a problem handling your request.");
        e.printStackTrace();
      }
    }
  }

}
