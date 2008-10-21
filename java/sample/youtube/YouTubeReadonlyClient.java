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
import com.google.gdata.client.Service;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Category;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.FeedLinkEntry;
import com.google.gdata.data.youtube.PlaylistEntry;
import com.google.gdata.data.youtube.PlaylistFeed;
import com.google.gdata.data.youtube.PlaylistLinkEntry;
import com.google.gdata.data.youtube.PlaylistLinkFeed;
import com.google.gdata.data.youtube.SubscriptionEntry;
import com.google.gdata.data.youtube.SubscriptionFeed;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Demonstrates basic Youtube Data API operations using the Java client library:
 * <ol>
 * <li> Retrieving standard Youtube feeds
 * <li> Searching a feed
 * <li> Searching a feed using categories and keywords
 * <li> Retrieving a user's uploaded videos
 * <li> Retrieve a user's favorite videos
 * <li> Retrieve responses for a video
 * <li> Retrieve comments for a video
 * <li> Retrieve a list of a user's playlists
 * <li> Retrieve a playlist
 * <li> Retrieve a list of a user's subscriptions
 * <li> Retrieve a user's profile
 * </ol>
 */
public class YouTubeReadonlyClient {

  /**
   * Input stream for reading user input.
   */
  private static final  BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(System.in)); 

  /**
   * The name of the server hosting the YouTube GDATA feeds
   */
  public static final String YOUTUBE_GDATA_SERVER = "http://gdata.youtube.com";

  /**
   * The prefix common to all standard feeds
   */
  public static final String STANDARD_FEED_PREFIX = YOUTUBE_GDATA_SERVER
      + "/feeds/api/standardfeeds/";

  /**
   * The URL of the "Most Recent" feed
   */
  public static final String MOST_RECENT_FEED = STANDARD_FEED_PREFIX
      + "most_recent";

  /**
   * The URL of the "Top Rated" feed
   */
  public static final String TOP_RATED_FEED = STANDARD_FEED_PREFIX
      + "top_rated";

  /**
   * The URL of the "Most Viewed" feed
   */
  public static final String MOST_VIEWED_FEED = STANDARD_FEED_PREFIX
      + "most_viewed";

  /**
   * The URL of the "Recently Featured" feed
   */
  public static final String RECENTLY_FEATURED_FEED = STANDARD_FEED_PREFIX
      + "recently_featured";

  /**
   * The URL of the "Watch On Mobile" feed
   */
  public static final String WATCH_ON_MOBILE_FEED = STANDARD_FEED_PREFIX
      + "watch_on_mobile";

  /**
   * The URL of the "Videos" feed
   */
  public static final String VIDEOS_FEED = YOUTUBE_GDATA_SERVER
      + "/feeds/api/videos";

  /**
   * The prefix of the User Feeds
   */
  public static final String USER_FEED_PREFIX = YOUTUBE_GDATA_SERVER
      + "/feeds/api/users/";

  /**
   * The URL suffix of the test user's uploads feed
   */
  public static final String UPLOADS_FEED_SUFFIX = "/uploads";

  /**
   * The URL suffix of the test user's favorites feed
   */
  public static final String FAVORITES_FEED_SUFFIX = "/favorites";

  /**
   * The URL suffix of the test user's subscriptions feed
   */
  public static final String SUBSCRIPTIONS_FEED_SUFFIX = "/subscriptions";

  /**
   * The URL suffix of the test user's playlists feed
   */
  public static final String PLAYLISTS_FEED_SUFFIX = "/playlists";

  /**
   * The default user if the user does not enter one.
   */
  private static String defaultTestUser = "YTdebates";

  /**
   * Prints a list of all standard feeds.
   *
   * @param service a YouTubeService object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printStandardFeeds(YouTubeService service)
      throws IOException, ServiceException {
    printVideoFeed(service, MOST_VIEWED_FEED, false);
    printVideoFeed(service, TOP_RATED_FEED, false);
    printVideoFeed(service, RECENTLY_FEATURED_FEED, false);
    printVideoFeed(service, WATCH_ON_MOBILE_FEED, false);
  }

  /**
   * Prints a String, a newline, and a number of '-' characters equal to the
   * String's length.
   *
   * @param feedTitle - the title to print underlined
   */
  private static void printUnderlined(String feedTitle) {
    System.out.println(feedTitle);
    for (int i = 0; i < feedTitle.length(); ++i) {
      System.out.print("-");
    }
    System.out.println("\n");
  }

  /**
   * Prints a VideoEntry, optionally showing its responses and comment feeds.
   *
   * @param prefix                   a string to be shown before each entry
   * @param videoEntry               the VideoEntry to be printed
   * @param showCommentsAndResponses true if the comments and responses feeds
   *                                 should be printed
   * @throws ServiceException
   *                                 If the service is unable to handle the
   *                                 request.
   * @throws IOException             error sending request or reading the feed.
   */
  private static void printVideoEntry(String prefix, VideoEntry videoEntry,
      boolean showCommentsAndResponses) throws IOException, ServiceException {
    System.out.println(prefix);
    if (videoEntry.getTitle() != null) {
      System.out.printf("Title: %s\n", videoEntry.getTitle().getPlainText());
    }
    if (videoEntry.getSummary() != null) {
      System.out.printf("Summary: %s\n",
          videoEntry.getSummary().getPlainText());
    }
    YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
    if(mediaGroup != null) {
      MediaPlayer mediaPlayer = mediaGroup.getPlayer();
      System.out.println("Web Player URL: " + mediaPlayer.getUrl());
      MediaKeywords keywords = mediaGroup.getKeywords();
      System.out.print("Keywords: ");
      for(String keyword : keywords.getKeywords()) {
        System.out.print(keyword + ",");
      }
      System.out.println();
      System.out.println("\tThumbnails:");
      for(MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
        System.out.println("\t\tThumbnail URL: " + mediaThumbnail.getUrl());
        System.out.println("\t\tThumbnail Time Index: " +
            mediaThumbnail.getTime());
        System.out.println();
      }
      System.out.println("\tMedia:");
      for(YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) {
        System.out.println("\t\tMedia Location: "+mediaContent.getUrl());
        System.out.println("\t\tMedia Type: "+mediaContent.getType());
        System.out.println("\t\tDuration: " + mediaContent.getDuration());
        System.out.println();
      }
      System.out.println();
    }
    if (showCommentsAndResponses) {
      printResponsesFeed(videoEntry);
      System.out.println("");
      printCommentsFeed(videoEntry);
      System.out.println("");
      System.out.println("");
    }
  }

  /**
   * Prints the responses feed of a VideoEntry.
   *
   * @param videoEntry the VideoEntry whose responses are to be printed
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printResponsesFeed(VideoEntry videoEntry)
      throws IOException, ServiceException {
    if (videoEntry.getVideoResponsesLink() != null) {
      String videoResponsesFeedUrl =
          videoEntry.getVideoResponsesLink().getHref();
      System.out.println();
      printVideoFeed((YouTubeService) videoEntry.getService(),
          videoResponsesFeedUrl, false);
    }
  }

  /**
   * Prints the comments feed of a VideoEntry.
   *
   * @param videoEntry the VideoEntry whose comments are to be printed
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printCommentsFeed(VideoEntry videoEntry)
      throws IOException, ServiceException {
    Comments comments = videoEntry.getComments();
    if (comments != null && comments.getFeedLink() != null) {
      System.out.println("\tComments:");
      printFeed(videoEntry.getService(), comments.getFeedLink().getHref(), 
          "Comment");
    }
  }

  /**
   * Prints a basic feed, such as the comments or responses feed, retrieved from
   * a feedUrl.
   *
   * @param service a YouTubeService object
   * @param feedUrl the url of the feed
   * @param prefix  a prefix string to be printed in front of each entry field
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printFeed(Service service, String feedUrl, String prefix)
      throws IOException, ServiceException {
    Feed feed = service.getFeed(new URL(feedUrl), Feed.class);

    for (Entry e : feed.getEntries()) {
      printEntry(e, prefix);
    }
  }

  /**
   * Prints a basic Entry, usually from a comments or responses feed.
   *
   * @param entry      the entry to be printed
   * @param prefix a prefix to be printed before each entry attribute
   */
  private static void printEntry(Entry entry, String prefix) {
    System.out.println("\t\t" + prefix + " Title: " 
        + entry.getTitle().getPlainText());
    if (entry.getContent() != null) {
      TextContent content = (TextContent) entry.getContent();
      System.out.println("\t\t" + prefix + " Content: " 
          + content.getContent().getPlainText());
    }
    System.out.println("\t\tURL: " + entry.getHtmlLink().getHref());
  }

  /**
   * Prints a PlaylistEntry by retrieving the Description of the PlayList,
   * followed by the Titles and URLs of each entry in the feed.
   *
   * @param prefix               a string to be printed before each entry
   * @param playlistLinkEntry    the PlaylistEntry to be printed
   * @param showPlaylistContents if true, show the list of videos in the
   *                             playlist
   * @throws ServiceException
   *                             If the service is unable to handle the
   *                             request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printPlaylistEntry(String prefix,
      PlaylistLinkEntry playlistLinkEntry, boolean showPlaylistContents)
      throws IOException, ServiceException {

    System.out.println(prefix);
    System.out.printf("Description: %s\n", playlistLinkEntry.getSummary().getPlainText());
    if (showPlaylistContents) {
      printPlaylist(playlistLinkEntry.getService(), playlistLinkEntry.getFeedUrl());
    }
  }

  /**
   * Prints a playlist feed as a series of Titles and URLs.
   *
   * @param service     a YouTubeService object
   * @param playlistUrl the url of the playlist
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printPlaylist(Service service, String playlistUrl)
      throws IOException, ServiceException {
    PlaylistFeed playlistFeed = service.getFeed(new URL(playlistUrl),
        PlaylistFeed.class);
    if (playlistFeed != null) {
      for (PlaylistEntry e : playlistFeed.getEntries()) {
        System.out.println("\tPlaylist Entry: " + e.getTitle().getPlainText());
        System.out.println("\tPlaylist URL: " + e.getHtmlLink().getHref());
      }
    }
  }

  /**
   * Prints a FeedLinkEntry as a Title and URL String.
   *
   * @param feedLinkEntry the FeedLinkEntry to be printed
   */
  private static void printFeedLinkEntry(FeedLinkEntry feedLinkEntry) {
    if (feedLinkEntry.getTitle() != null) {
      System.out.printf("Title: %s\n", feedLinkEntry.getTitle().getPlainText());
    }
    System.out.println("FeedLink: " + feedLinkEntry.getFeedUrl());
  }

  /**
   * Prints a SubscriptionEntry, which is a FeedLink entry.
   *
   * @param subEntry the SubscriptionEntry to be printed
   */
  private static void printSubscriptionEntry(SubscriptionEntry subEntry) {
    printFeedLinkEntry(subEntry);
  }

  /**
   * Reads a line of text from the standard input.
   * @throws IOException if unable to read a line from the standard input
   * @return a line of text read from the standard input
   */
  private static String readLine() throws IOException {
    return bufferedReader.readLine();
  }

  /**
   * Reads a line of text from the standard input, and returns the parsed
   * integer representation.
   * @throws IOException if unable to read a line from the standard input
   * @return an integer read from the standard input
   */
  private static int readInt() throws IOException {
    String input = readLine();
    return Integer.parseInt(input);
  }

  private static void printUsage() {
    System.out.println("Choose one of the following demo options:");
    System.out.println("\t1) Print Standard Feeds");
    System.out.println("\t2) Print Search Feed");
    System.out.println("\t3) Print Keyword Search Feed");
    System.out.println("\t4) Print Uploads Feed");
    System.out.println("\t5) Print Favorites Feed");
    System.out
        .println("\t6) Show Comments and Responses Feed for a single Video");
    System.out.println("\t7) Print Playlists Feed");
    System.out.println("\t8) Display a playlist");
    System.out.println("\t9) Print Subscriptions Feed");
    System.out.println("\t10) Print User Profile Feed");
    System.out.println("\t0) Exit");
    System.out.println("\nEnter Number (0-10): ");
  }

  /**
   * Fetchs a user's profile feed and prints out most of the attributes.
   *
   * @param service a YouTubeService object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printUserProfile(YouTubeService service)
      throws IOException, ServiceException {
    String testUser = promptForUser();
    printUnderlined("User Profile for '" + testUser + "'");
    UserProfileEntry userProfileEntry = service.getEntry(new URL(
        USER_FEED_PREFIX + testUser), UserProfileEntry.class);
    System.out.println("Username: " + userProfileEntry.getUsername());
    System.out.println("Age     : " + userProfileEntry.getAge());
    System.out.println("Gender  : " + userProfileEntry.getGender());
    System.out.println("Single? : " + userProfileEntry.getRelationship());
    System.out.println("Books   : " + userProfileEntry.getBooks());
    System.out.println("Company : " + userProfileEntry.getCompany());
    System.out.println("Describe: " + userProfileEntry.getAboutMe());
    System.out.println("Hobbies : " + userProfileEntry.getHobbies());
    System.out.println("Hometown: " + userProfileEntry.getHometown());
    System.out.println("Location: " + userProfileEntry.getLocation());
    System.out.println("Movies  : " + userProfileEntry.getMovies());
    System.out.println("Music   : " + userProfileEntry.getMusic());
    System.out.println("Job     : " + userProfileEntry.getOccupation());
    System.out.println("School  : " + userProfileEntry.getSchool());
  }

  /**
   * Prints a user's playlists feed.
   *
   * @param service              a YouTubeService object.
   * @param showPlaylistContents if true, print only the first playlist,
   *                             followed by all of its contained entries
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printPlaylists(YouTubeService service,
      boolean showPlaylistContents) throws IOException, ServiceException {
    String testUser = promptForUser();
    printPlaylistsFeed(service, USER_FEED_PREFIX + testUser
        + PLAYLISTS_FEED_SUFFIX, showPlaylistContents);
  }

  /**
   * Prints a user's subscriptions feed.
   * 
   * @param service a YouTubeService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printSubscriptions(YouTubeService service)
      throws IOException, ServiceException {
    String testUser = promptForUser();
    printSubscriptionsFeed(service, USER_FEED_PREFIX + testUser 
        + SUBSCRIPTIONS_FEED_SUFFIX);
  }

  /**
   * Prints a user's favorites feed.
   *
   * @param service a YouTubeService object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printFavorites(YouTubeService service)
      throws IOException, ServiceException {
    String testUser = promptForUser();
    printVideoFeed(service, USER_FEED_PREFIX + testUser + FAVORITES_FEED_SUFFIX, 
        false);
  }

  /**
   * Prints a user's uploads feed.
   *
   * @param service                  a YouTubeService object.
   * @param showCommentsAndResponses whether or not to just print the first
   *                                 entry, followed by comments and responses
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printUploads(YouTubeService service,
      boolean showCommentsAndResponses) throws IOException, ServiceException {
    String testUser = promptForUser();
    printVideoFeed(service, USER_FEED_PREFIX + testUser + UPLOADS_FEED_SUFFIX,
        showCommentsAndResponses);
  }

  /**
   * Prompts the user to enter his user name on the standard input.
   * @return a username entered by the user
   * @throws java.io.IOException if unable to read a line from standard input
   */
  private static String promptForUser() throws IOException {
    System.out.println("\nEnter YouTube username [default " + defaultTestUser 
        + "]: ");
    String line = readLine();
    if (line == null || "".equals(line.trim())) {
      line = defaultTestUser;
    } else {
      defaultTestUser = line;
    }
    return line;
  }

  /**
   * Fetches a feed known to be a VideoFeed, printing each VideoEntry with in
   * a numbered list, optionally prompting the user for the number of a video
   * entry which should have its comments and responses printed.
   *
   * @param service a YouTubeService object
   * @param feedUrl the url of the video feed to print
   * @param showCommentsAndResponses true if the user should be prompted for
   *                                 a video whose comments and responses should
   *                                 printed
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printVideoFeed(YouTubeService service, String feedUrl, 
      boolean showCommentsAndResponses) throws IOException, ServiceException {
    VideoFeed videoFeed = service.getFeed(new URL(feedUrl), VideoFeed.class);
    String title = videoFeed.getTitle().getPlainText();
    if (showCommentsAndResponses) {
      title += " with comments and responses";
    }
    printUnderlined(title);
    List<VideoEntry> videoEntries = videoFeed.getEntries();
    if (videoEntries.size() == 0) {
      System.out.println("This feed contains no entries.");
      return;
    }
    int count = 1;
    for (VideoEntry ve : videoEntries) {
      printVideoEntry("(Video #" + String.valueOf(count) + ")", ve, false);
      count++;
    }

    if (showCommentsAndResponses) {
      System.out.printf(
          "\nWhich video to show comments and responses for? (1-%d): \n", 
          count - 1);
      int whichVideo = readInt();
      printVideoEntry("", videoEntries.get(whichVideo - 1), true);
    }
    System.out.println();
  }

  /**
   * Prints a user's playlists feed.
   *
   * @param service              a YouTubeService object
   * @param feedUrl              the url of the feed
   * @param showPlaylistContents true if only one entry should be shown,
   *                             followed by the contents of the playlist
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printPlaylistsFeed(YouTubeService service, String feedUrl, 
      boolean showPlaylistContents) throws IOException, ServiceException {
    PlaylistLinkFeed playlistLinkFeed = service.getFeed(new URL(feedUrl),
        PlaylistLinkFeed.class);
    String title = playlistLinkFeed.getTitle().getPlainText();
    if (showPlaylistContents) {
      title += " with playlist content.";
    }
    printUnderlined(title);
    List<PlaylistLinkEntry> playlistEntries = playlistLinkFeed.getEntries();
    int count = 1;
    for (PlaylistLinkEntry pe : playlistEntries) {
      printPlaylistEntry("(Playlist #" + count + ")", pe, false);
      count++;
    }
    if (showPlaylistContents) {
      System.out.printf("\nWhich playlist do you want to see? (1-%d): \n",
          count - 1);
      int whichVideo = readInt();
      printPlaylistEntry("", playlistEntries.get(whichVideo - 1), true);
    }
    System.out.println();
  }

  /**
   * Prints a user's subscriptions feed.
   * 
   * @param service a YouTubeService object
   * @param feedUrl the url of the feed
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void printSubscriptionsFeed(YouTubeService service,
      String feedUrl) throws IOException, ServiceException {
    SubscriptionFeed subscriptionFeed = service.getFeed(new URL(feedUrl),
        SubscriptionFeed.class);
    printUnderlined(subscriptionFeed.getTitle().getPlainText());
    for (SubscriptionEntry se : subscriptionFeed.getEntries()) {
      printSubscriptionEntry(se);
    }
    System.out.println();
  }

  /**
   * Searches the VIDEOS_FEED for search terms and print each resulting
   * VideoEntry.
   *
   * @param service a YouTubeService object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void searchFeed(YouTubeService service)
      throws IOException, ServiceException {
    YouTubeQuery query = new YouTubeQuery(new URL(VIDEOS_FEED));
    // order results by the number of views (most viewed first)
    query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);

    // do not exclude restricted content from the search results 
    // (by default, it is excluded) 
    query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

    System.out.println("\nEnter search terms: ");
    String searchTerms = readLine();

    query.setFullTextQuery(searchTerms);

    printUnderlined("Running Search for '" + searchTerms + "'");
    VideoFeed videoFeed = service.query(query, VideoFeed.class);
    for (VideoEntry ve : videoFeed.getEntries()) {
      printVideoEntry("", ve, false);
    }
    System.out.println();
  }

  /**
   * Searches the VIDEOS_FEED for category keywords and prints each resulting
   * VideoEntry.
   *
   * @param service a YouTubeService object.
   * @throws ServiceException
   *                     If the service is unable to handle the request.
   * @throws IOException error sending request or reading the feed.
   */
  private static void searchFeedWithKeywords(YouTubeService service)
      throws IOException, ServiceException {
    YouTubeQuery query = new YouTubeQuery(new URL(VIDEOS_FEED));
    // order the results by the number of views
    query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
    
    // include restricted content in the search results
    query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

    // a category filter holds a collection of categories to limit the search
    Query.CategoryFilter categoryFilter = new Query.CategoryFilter();

    String keywordTerm = null;
    String allKeywords = "";

    do {
      System.out.println("\nEnter keyword or empty line when done: ");
      keywordTerm = readLine();
      // creates categories whose scheme is KEYWORD_SCHEME
      Category category = new Category(YouTubeNamespace.KEYWORD_SCHEME,
          keywordTerm);
      categoryFilter.addCategory(category);
      // keeps track of concatenated list of keywords entered so far
      if(!"".equals(keywordTerm))
        allKeywords += keywordTerm + ", ";
    } while(keywordTerm != null && !"".equals(keywordTerm));

    // adds the collection of keyword categories to the query
    query.addCategoryFilter(categoryFilter);

    printUnderlined("Running Search for '" + allKeywords + "'");
    VideoFeed videoFeed = service.query(query, VideoFeed.class);
    for (VideoEntry ve : videoFeed.getEntries()) {
      printVideoEntry("", ve, false);
    }
    System.out.println();
  }

  /**
   * YouTubeReadonlyClient is a sample command line application that
   * demonstrates many features of the YouTube data API using
   * the Java Client library.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    YouTubeService myService = new YouTubeService("gdataSample-YouTube-1");

    while (true) {
      try {
        printUsage();
        int choice = readInt();

        switch (choice) {
          case 1:
            // Fetches and prints the standard feeds.
            printStandardFeeds(myService);
            break;
          case 2:
            // Searches the VIDEO_FEED for user supplied search terms.
            searchFeed(myService);
            break;
          case 3:
            // Searches the VIDEO_FEED for user supplied category keyword terms.
            searchFeedWithKeywords(myService);
            break;
          case 4:
            // Fetches and prints a user's uploads feed.
            printUploads(myService, false);
            break;
          case 5:
            // Fetches and prints a user's favorites feed.
            printFavorites(myService);
            break;
          case 6:
            // Prompts the user for a username and displays the uploads feed
            // for that username as a numbered list. The user is asked to choose
            // the number of a video entry for which the comments and responses
            // feeds should be displayed.
            printUploads(myService, true);
            break;
          case 7:
            // Fetches and prints a list of a user's playlists feed.
            printPlaylists(myService, false);
            break;
          case 8:
            // Fetches and prints a numbered list of entries in a user's
            // playlists feed. The user is then asked to choose the number of
            // a playlist he wishes to see the contents of.
            printPlaylists(myService, true);
            break;
          case 9:
            // Fetches and prints a user's subscriptions feed.
            printSubscriptions(myService);
            break;
          case 10:
            // Fetches and prints a user's profile feed.
            printUserProfile(myService);
            break;
          case 0:
          default:
            System.exit(0);
        }
      } catch (IOException e) {
          // Communications error
          System.err.
              println("There was a problem communicating with the service.");
          e.printStackTrace();
      } catch (ServiceException e) {
          // Server side error
          System.err.println("The server had a problem handling your request.");
          e.printStackTrace();
      }
    }
  }
}
