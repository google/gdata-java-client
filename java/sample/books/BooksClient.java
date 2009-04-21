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



package sample.books;

import com.google.gdata.client.Service;
import com.google.gdata.client.books.BooksService;
import com.google.gdata.client.books.VolumeQuery;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.Category;
import com.google.gdata.data.books.BooksCategory;
import com.google.gdata.data.books.VolumeEntry;
import com.google.gdata.data.books.VolumeFeed;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Title;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Demonstrates Books API operations using the Java client library.
 */
public class BooksClient {
  protected BooksClient() {}

  /**
   * Input stream for reading user input.
   */
  private static final BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(System.in));

  /**
   * The name of the server hosting the YouTube GDATA feeds.
   */
  public static final String BOOKS_GDATA_SERVER = "http://books.google.com";


  /**
   * The URL of the volumes feed
   */
  public static final String VOLUMES_FEED = BOOKS_GDATA_SERVER
      + "/books/feeds/volumes";

  /**
   * The URL of the user library feeds
   */
  public static final String USER_LIBRARY_FEED = BOOKS_GDATA_SERVER
      + "/books/feeds/users/me/collections/library/volumes";

  /**
   * The URL of the user annotation feed
   */
  public static final String USER_ANNOTATION_FEED = BOOKS_GDATA_SERVER
      + "/books/feeds/users/me/volumes";

  /**
   * Searches the VOLUMES_FEED for search terms and print each resulting
   * VolumeEntry.
   *
   * @param service a BooksService object.
   * @param authenticated whether the user is authenticated.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void searchVolumes(BooksService service,
                                    boolean authenticated)
      throws IOException, ServiceException {
    VolumeQuery query = new VolumeQuery(new URL(VOLUMES_FEED));

    // exclude no-preview book (by default, they are included)
    query.setMinViewability(VolumeQuery.MinViewability.PARTIAL);

    System.out.println("\nEnter search terms: ");
    String searchTerms = readLine();
    System.out.println();

    query.setFullTextQuery(searchTerms);

    printUnderlined("Running Search for '" + searchTerms + "'");
    VolumeFeed volumeFeed = service.query(query, VolumeFeed.class);
    printVolumeFeed(volumeFeed);

    if (authenticated) {
      handleSearchVolumes(service, volumeFeed);
    }
  }

  /**
   * Handle options after displaying a search feed, specifically reviewing or
   * adding to the user's library.
   *
   * @param service a BooksService object.
   * @param volumeFeed a volume feed object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void handleSearchVolumes(BooksService service,
                                          VolumeFeed volumeFeed)
      throws IOException, ServiceException {
    while (true) {
      System.out.println("\nWhat would you like to do?");
      System.out.println("\t1) Add a volume to my library");
      System.out.println("\t2) Submit a rating for a volume");
      System.out.println("\t0) Back to main menu");
      System.out.println("\nEnter Number (0-2): ");

      int choice = readInt();

      String volumeId;
      switch (choice) {
      case 1:
        volumeId = readVolumeId(volumeFeed);
        if (!volumeId.equals("")) {
          addToLibrary(service, volumeId);
        }
        break;
      case 2:
        volumeId = readVolumeId(volumeFeed);
        System.out.println("Please input a rating value (1-5)");
        int rating = readInt();
        if (rating >= 1 && rating <= 5 && !volumeId.equals("")) {
          addRating(service, volumeId, rating);
        }
        break;
      case 0:
      default:
        return;
      }
    }
  }

  /**
   * Prints a String, a newline, and a number of '-' characters equal to the
   * String's length.
   *
   * @param stringToUnderline - the string to print underlined
   */
  private static void printUnderlined(String stringToUnderline) {
    System.out.println(stringToUnderline);
    for (int i = 0; i < stringToUnderline.length(); ++i) {
      System.out.print("-");
    }
    System.out.println("\n");
  }

  /**
   * Adds a volume to the user's library
   *
   * @param service a BooksService object.
   * @param volumeId The volume id to insert
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void addToLibrary(BooksService service, String volumeId)
      throws IOException, ServiceException {
    VolumeEntry newEntry = new VolumeEntry();
    newEntry.setId(volumeId);
    try {
      service.insert(new URL(USER_LIBRARY_FEED), newEntry);
    } catch (ServiceException se) {
      System.out.println("There was an error adding your volume.\n");
      return;
    }
    System.out.println("Added " + volumeId);
  }

  /**
   * Adds a rating for a volume
   *
   * @param service a BooksService object.
   * @param volumeId The volume id to rate
   * @param value The rating to insert
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void addRating(BooksService service,
                                String volumeId, int value)
      throws IOException, ServiceException {
    VolumeEntry newEntry = new VolumeEntry();
    newEntry.setId(volumeId);
    Rating rating = new Rating();
    rating.setMin(1);
    rating.setMax(5);
    rating.setValue(value);
    newEntry.setRating(rating);
    try {
      service.insert(new URL(USER_ANNOTATION_FEED), newEntry);
    } catch (ServiceException se) {
      System.out.println("There was an error adding your rating.\n");
      return;
    }
    System.out.println("Added rating for " + volumeId);
  }

  /**
   * Prints a VolumeEntry
   *
   * @param entry The VolumeEntry to be printed
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printVolumeEntry(VolumeEntry entry) throws IOException,
      ServiceException {
    System.out.print("Title: ");
    for (Title t : entry.getTitles()) {
      System.out.print(t.getValue() + "\t");
    }
    System.out.println();
    System.out.print("Author: ");
    for (Creator c : entry.getCreators()) {
      System.out.print(c.getValue() + "\t");
    }
    System.out.println();
    if (entry.hasRating()) {
      System.out.println("Rating: " + entry.getRating().getAverage());
    }
    if (entry.hasReview()) {
      System.out.println("Review: " + entry.getReview().getValue());
    }
    boolean firstLabel = true;
    if (entry.getCategories().size() > 0) {
      for (Category c : entry.getCategories()) {
        if (c.getScheme() == BooksCategory.Scheme.LABELS_SCHEME) {
          if (firstLabel) {
            System.out.print("Labels: ");
            firstLabel = false;
          }
          System.out.print(c.getTerm() + "\t");
        }
      }
      if (!firstLabel) {
        System.out.println();
      }
    }
    if (entry.hasViewability()) {
      System.out.println("Viewability: " + entry.getViewability().getValue());
    }
    System.out.println();
  }

  /**
   * Show the feed of user annotations.
   *
   * @param service A BooksService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showUserAnnotations(Service service)
      throws IOException, ServiceException {
    VolumeFeed volumeFeed = service.getFeed(new URL(USER_ANNOTATION_FEED),
        VolumeFeed.class);
    printVolumeFeed(volumeFeed);
  }

  /**
   * Show the feed of user annotations.
   *
   * @param service A BooksService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showUserLibrary(Service service)
      throws IOException, ServiceException {
    VolumeFeed volumeFeed = service.getFeed(new URL(USER_LIBRARY_FEED),
        VolumeFeed.class);
    printVolumeFeed(volumeFeed);
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
   * Solicits the user for the index of a volume in a list
   *
   * @param volumeFeed A volume Feed.
   * @return String containing a volume ID.
   * @throws IOException If there are problems reading user input.
   */
  private static String readVolumeId(VolumeFeed volumeFeed) throws IOException {
    System.out.println("Input the index of one of the volumes listed above (1-"
                       + volumeFeed.getEntries().size() + ")");

    int input = readInt();
    if (input == 0) {
      return "";
    }

    return volumeFeed.getEntries().get(input - 1).getId();
  }

  /**
   * Displays a menu of the main activities a user can perform.
   */
  private static void printMenu() {
    System.out.println("\n");
    System.out.println("Choose one of the following demo options:");
    System.out.println("\t1) Search for books");
    System.out.println("\t2) My list of books (requires authentication)");
    System.out.println("\t3) My annotations (requires authentication)");
    System.out.println("\t0) Exit");
    System.out.println("\nEnter Number (0-3): ");
  }

  /**
   * Shows the usage of how to run the sample from the command-line.
   */
  private static void printUsage() {
    System.out.println("Usage:\n java BooksClient.jar");
    System.out.println("or with authentication:\n java BooksClient.jar "
        + " --username <user@gmail.com> " + " --password <pass> ");
  }


  /**
   * Print a volume feed of entries as a numbered list
   *
   * @param volumeFeed A feed of volumes
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printVolumeFeed(VolumeFeed volumeFeed)
      throws IOException, ServiceException {
    String title = volumeFeed.getTitle().getPlainText();
    System.out.println(title);

    List<VolumeEntry> volumeEntries = volumeFeed.getEntries();
    if (volumeEntries.size() == 0) {
      System.out.println("This feed contains no entries.");
      return;
    }
    System.out.println("Results " + volumeFeed.getStartIndex() + " - " +
                       (volumeFeed.getStartIndex() + volumeEntries.size() - 1) +
                       " of " + volumeFeed.getTotalResults());
    System.out.println();

    int count = 1;
    for (VolumeEntry entry : volumeEntries) {
      System.out.println("(Volume #" + String.valueOf(count) + ")");
      printVolumeEntry(entry);
      count++;
    }
    System.out.println();
  }

  /**
   * BooksClient is a sample command line application that
   * demonstrates many features of the Books Data API using the Java Client
   * library.
   *
   * This sample demonstrates both search and social activities in the API.
   * Social activities are only available to authenticated users.
   *
   * @param args Used to pass the username and password of a test account.
   */
  public static void main(String[] args) {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");
    boolean authenticated = (username != null) && (password != null);

    if (help) {
      printUsage();
      System.exit(1);
    }

    BooksService service = new BooksService("gdataSample-Books-1");

    if (authenticated) {
      try {
        service.setUserCredentials(username, password);
      } catch (AuthenticationException e) {
        System.out.println("Invalid login credentials.");
        System.exit(1);
      }
    }

    while (true) {
      try {

        printMenu();
        int choice = readInt();

        switch (choice) {
        case 1:
          // Search for books
          searchVolumes(service, authenticated);
          break;
        case 2:
          // View the user's library
          if (authenticated) {
            showUserLibrary(service);
          } else {
            System.out.println("You need to specify a user account");
            printUsage();
          }
          break;
        case 3:
          // View the user's annotations
          if (authenticated) {
            showUserAnnotations(service);
          } else {
            System.out.println("You need to specify a user account");
            printUsage();
          }
          break;
        case 0:
        default:
          System.out.println("Bye!");
          System.exit(0);
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
