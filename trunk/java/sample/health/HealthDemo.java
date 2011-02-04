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


package sample.health;

import com.google.gdata.client.health.HealthService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.health.ProfileFeed;
import com.google.gdata.data.health.ProfileEntry;
import com.google.gdata.data.health.RegisterFeed;
import com.google.gdata.data.health.RegisterEntry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Entry;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.AuthenticationException;

import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.List;
import java.util.ArrayList;

/**
 * A demo for GData Health API.
 *
 * 
 */
public class HealthDemo {

  /** The message for displaying the usage parameters. */
  private static final String[] USAGE_MESSAGE = {
      "Usage: java HealthDemo.jar --username <user> --password <pass>",
      "   [--log]     Enable logging of requests"
  };

  /** Help on all available commands. */
  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      " profile          [[Prints all of user's medical profiles]]",
      " register         [[Prints all of user's external notices]]",
      " exit             [[quit sample]]"};


  /** Welcome message, introducing the program. */
  private static final String[] WELCOME_MESSAGE = {
      "This is a demo of the Google health Profile Feed!", "",
      "Using this interface, you can view list of Profile entries.",
      ""};

  private static final String FEED_BASE_PATH =
      "https://www.google.com/health/feeds/";

  private static final String PROFILE_FEED_PATH = FEED_BASE_PATH + "profile/";
  private static final String REGISTER_FEED_PATH = FEED_BASE_PATH + "register/";

  private static final String PROFILE_LIST_URL_PATH = PROFILE_FEED_PATH + "list";

  private HealthService service;

  private List<String> profileIds = null;

  private PrintStream out;

  public HealthDemo() {
  }

  public HealthDemo(HealthService service, PrintStream outputStream)
      throws MalformedURLException {
    this.service = service;
    this.out = outputStream;
  }

  private List<String> getAllProfileIds() throws IOException, ServiceException {
    if (profileIds != null) {
      return profileIds;
    }
    profileIds = new ArrayList<String>();
    Feed profileListFeed = service.getFeed(
        new URL(PROFILE_LIST_URL_PATH), Feed.class);
    for (Entry profileListEntry : profileListFeed.getEntries()) {
      profileIds.add(
          ((TextContent) profileListEntry.getContent()).
              getContent().getPlainText());
    }
    return profileIds;
  }

  /**
   * Prints the CCR record for specified profile id.
   *
   * @param profileId for which ccr record is requested
   * @throws IOException
   * @throws ServiceException
   */
  public void showProfileForId(String profileId) 
      throws IOException, ServiceException {

    out.println("CCR records for profile id: " + profileId);
    ProfileFeed feed = service.getFeed(
        new URL(PROFILE_FEED_PATH + "ui/" + profileId), ProfileFeed.class);
    for (ProfileEntry entry : feed.getEntries()) {
      out.println(entry.getContinuityOfCareRecord().getXmlBlob().getBlob());
    }

  }

  /**
   * Prints notices corresponding to specified profile.
   *
   * @param profileId whose notices need to be printed.
   * @throws IOException
   * @throws ServiceException
   */
  public void showNoticesForId(String profileId)
      throws IOException, ServiceException {

    out.println("Notices for profile id: " + profileId);
    RegisterFeed feed = service.getFeed(
        new URL(REGISTER_FEED_PATH + "ui/" + profileId), RegisterFeed.class);

    if (feed.getEntries().size() == 0) {
      out.println("There are no notices for this profile");
    }

    int noticeIndex = 1;
    for (RegisterEntry entry : feed.getEntries()) {
      out.println(noticeIndex + ")" + entry.getTitle().getPlainText());
      noticeIndex++;
    }
  }

  private String selectProfile(BufferedReader reader)
      throws IOException, ServiceException {
    int profileIndex = 1;
    out.println("Please select a profile from the following list: ");
    for (String e : getAllProfileIds()) {
      out.println(profileIndex++ + ") " + e);
    }
    int choice = -1;
    while (choice < 1 || choice > profileIds.size()) {
      out.println("Select a profile: ");
      choice = Integer.parseInt(reader.readLine());
    }
    return getAllProfileIds().get(choice - 1);
  }

  /**
   * Reads and executes one command.
   *
   * @param reader to read input from the keyboard
   * @return false if the user quits, true on exception
   */
  private boolean executeCommand(BufferedReader reader) {
    for (String s : COMMAND_HELP_MESSAGE) {
      out.println(s);
    }

    System.err.print("Command: ");

    try {
      String name = reader.readLine();

      if (name.equals("profile")) {
        showProfileForId(selectProfile(reader));
      } else if (name.equals("register")) {
        showNoticesForId(selectProfile(reader));
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
   * @throws com.google.gdata.util.AuthenticationException
   *          if the service is unable to validate the username and password.
   */
  private void run(String username, String password)
      throws AuthenticationException {
    for (String s : WELCOME_MESSAGE) {
      out.println(s);
    }

    BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));

    service.setUserCredentials(username, password);

    while (executeCommand(reader)) {
    }
  }

  public static void main(String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null) {
      usage();
      System.exit(1);
    }

    if (parser.containsKey("log", "l")) {
      turnOnLogging();
    }

    HealthDemo demo = new HealthDemo(
        new HealthService("Sample Health Client"), System.out);
    demo.run(username, password);
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
