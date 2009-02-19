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

import com.google.gdata.client.Service;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.CommentEntry;
import com.google.gdata.data.youtube.PlaylistEntry;
import com.google.gdata.data.youtube.PlaylistFeed;
import com.google.gdata.data.youtube.PlaylistLinkEntry;
import com.google.gdata.data.youtube.PlaylistLinkFeed;
import com.google.gdata.data.youtube.UserEventEntry;
import com.google.gdata.data.youtube.UserEventFeed;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Demonstrates authenticated YouTube Data API operations using the Java client
 * library.
 */
public class YouTubeWriteClient {

  /**
   * Input stream for reading user input.
   */
  private static final BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(System.in));

  /**
   * The name of the server hosting the YouTube GDATA feeds.
   */
  public static final String YOUTUBE_GDATA_SERVER = "http://gdata.youtube.com";


  /**
   * The URL of the videos feed
   */
  public static final String VIDEOS_FEED = YOUTUBE_GDATA_SERVER
      + "/feeds/api/videos";

  /**
   * The prefix of the user feeds
   */
  public static final String USER_FEED_PREFIX = YOUTUBE_GDATA_SERVER
      + "/feeds/api/users/";
  
  /**
   * The prefix of recent activity feeds
   */
  public static final String ACTIVITY_FEED_PREFIX = YOUTUBE_GDATA_SERVER
      + "/feeds/api/events";

  /**
   * The URL suffix of the test user's uploads feed
   */
  public static final String UPLOADS_FEED_SUFFIX = "/uploads";

  /**
   * The URL suffix of the test user's favorites feed
   */
  public static final String FAVORITES_FEED_SUFFIX = "/favorites";

  /**
   * The URL suffix of the test user's playlists feed
   */
  public static final String PLAYLISTS_FEED_SUFFIX = "/playlists";
  
  /**
   * The URL suffix of the friends activity feed
   */
  public static final String FRIENDS_ACTIVITY_FEED_SUFFIX = "/friendsactivity";

  /**
   * The default username.
   */
  public static final String DEFAULT_USER = "default";
  
  /**
   * The default video to use for examples.
   */
  public static final String DEFAULT_VIDEO_ID = "scoMN8DYkCw";

  /**
   * The URL used to upload video
   */
  public static final String VIDEO_UPLOAD_FEED = 
      "http://uploads.gdata.youtube.com/feeds/api/users/"
      + DEFAULT_USER + "/uploads";

  /**
   * Enum to deal with various playlist operations: 
   *    VIEW = print user's playlists 
   *    LIST = print contents of a playlist 
   *    CREATE = create new playlist
   *    ADD = add video to playlist
   */
  private enum PlaylistOperation {
    VIEW, LIST, CREATE, ADD
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
   * Prints a VideoEntry, optionally showing its responses and comment feeds.
   * 
   * @param entry The VideoEntry to be printed
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printVideoEntry(VideoEntry entry) throws IOException,
      ServiceException {
    System.out.println("Title:" + entry.getTitle().getPlainText());

    YouTubeMediaGroup mediaGroup = entry.getMediaGroup();

    if (mediaGroup != null) {
      if (mediaGroup.isPrivate()) {
        System.out.println("Video is private");
      }
      MediaPlayer player = mediaGroup.getPlayer();
      if (player != null) {
        System.out.println("Video URL: " + player.getUrl());
      }
    }

    if (entry.isDraft()) {
      System.out.println("Video is not live");
      YtPublicationState pubState = entry.getPublicationState();
      if (pubState.getState() == YtPublicationState.State.PROCESSING) {
        System.out.println("Video is still being processed.");
      } else if (pubState.getState() == YtPublicationState.State.REJECTED) {
        System.out.print("Video has been rejected because: ");
        System.out.println(pubState.getDescription());
        System.out.print("For help visit: ");
        System.out.println(pubState.getHelpUrl());
      } else if (pubState.getState() == YtPublicationState.State.FAILED) {
        System.out.print("Video failed uploading because: ");
        System.out.println(pubState.getDescription());
        System.out.print("For help visit: ");
        System.out.println(pubState.getHelpUrl());
      }
    }
  }


  /**
   * Prints a PlaylistEntry by retrieving the description of the playlist,
   * followed by the titles and URLs of each entry in the feed.
   * 
   * @param prefix A string to be printed before each entry.
   * @param playlistLinkEntry The PlaylistEntry to be printed
   * @param showPlaylistContents If true, show the list of videos in the
   *        playlist.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printPlaylistEntry(String prefix,
      PlaylistLinkEntry playlistLinkEntry, boolean showPlaylistContents)
      throws IOException, ServiceException {
    System.out.println(prefix);
    System.out.printf("Description: %s\n",
        playlistLinkEntry.getSummary().getPlainText());
    if (showPlaylistContents) {
      printPlaylist(
          playlistLinkEntry.getService(), playlistLinkEntry.getFeedUrl());
    }
  }

  /**
   * Prints a playlist feed as a series of titles and URLs.
   * 
   * @param service A YouTubeService object.
   * @param playlistUrl The url of the playlist.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
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
   * Solicits the user for a video ID (hash) or tries to figure one out 
   * from the video's watch URL.
   * 
   * @return String containing a video ID.
   * @throws IOException If there are problems reading user input.
   */
  private static String readVideoID() throws IOException {
    System.out.println(
        "Input a valid video ID or a watch URL (default: " + DEFAULT_VIDEO_ID
        + "):");

    String input = readLine();
    if (input.equals("")) {
      return DEFAULT_VIDEO_ID;
    }

    Pattern p = Pattern.compile("http.*\\?v=([a-zA-Z0-9_\\-]+)(?:&.)*");
    Matcher m = p.matcher(input);

    if (m.matches()) {
      input = m.group(1);
    }

    return input;
  }


  /**
   * Displays a menu of the main activities a user can perform.
   */
  private static void printMenu() {
    System.out.println("\n");
    System.out.println("Choose one of the following demo options:");
    System.out.println("\t1) My Uploads");
    System.out.println("\t2) My Playlists");
    System.out.println("\t3) My Favorites");
    System.out.println("\t4) Comment on a video");
    System.out.println("\t5) Upload a new video");
    System.out.println("\t6) Add a favorite");
    System.out.println("\t7) Print user activity");
    System.out.println("\t8) Print my friends' activity");
    System.out.println("\t0) Exit");
    System.out.println("\nEnter Number (0-8): ");
  }

  /**
   * Shows the usage of how to run the sample from the command-line.
   */
  private static void printUsage() {
    System.out.println("Usage: java YouTubeWriteClient.jar "
        + " --username <user@gmail.com> " + " --password <pass> "
        + " --key <developer key>");
  }


  /**
   * Displays a user's playlists and lets the user manipulate them.
   * 
   * @param service A YouTubeService object.
   * @throws IOExcep
   * tion Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showPlaylists(YouTubeService service) throws IOException,
      ServiceException {
    doPlaylistFeedOperation(service, PlaylistOperation.VIEW);

    while (true) {
      System.out.println("\nWhat would you like to do?");
      System.out.println("\t1) Create a new playlist");
      System.out.println("\t2) Add a video to a playlist");
      System.out.println("\t3) Print playlists");
      System.out.println("\t4) Print playlist contents");
      System.out.println("\t0) Back to main menu");
      System.out.println("\nEnter Number (0-5): ");

      int choice = readInt();

      switch (choice) {
      case 1:
        doPlaylistFeedOperation(service, PlaylistOperation.CREATE);
        break;
      case 2:
        doPlaylistFeedOperation(service, PlaylistOperation.ADD);
        break;
      case 3:
        doPlaylistFeedOperation(service, PlaylistOperation.VIEW);
        break;
      case 4:
        doPlaylistFeedOperation(service, PlaylistOperation.LIST);
        break;
      case 0:
      default:
        return;
      }
    }
  }

  /**
   * Shows a user's favorites and lets the user manipulate them.
   * 
   * @param service a YouTubeService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showFavorites(YouTubeService service) throws IOException,
      ServiceException {
    printVideoFeed(service, USER_FEED_PREFIX + DEFAULT_USER
        + FRIENDS_ACTIVITY_FEED_SUFFIX);
  }
  
  /**
   * Shows a user's activity feed.
   * 
   * @param service a YouTubeService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showActivity(YouTubeService service) throws IOException,
      ServiceException {
    
    System.out.println("Type in a comma separated list of YouTube usernames:");

    String users = readLine();
    printActivityFeed(service, ACTIVITY_FEED_PREFIX + "?author=" + users);
  }
  
  /**
   * Shows a user's friends' activity feed.
   * 
   * @param service a YouTubeService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void showFriendsActivity(YouTubeService service) throws IOException,
      ServiceException {
    printActivityFeed(service, USER_FEED_PREFIX + DEFAULT_USER
        + FRIENDS_ACTIVITY_FEED_SUFFIX);
  }

  /**
   * Demonstrates adding a comment to a video.
   * 
   * @param service The YouTubeService object controlling the connection to the
   *    server.
   * @throws IOException Error sending request or reading the response.
   */

  private static void addComment(YouTubeService service) throws IOException {
    System.out.println("First, choose a video you want to comment on.");
    String videoID = readVideoID();

    System.out.println("Okay, adding a comment to: " + videoID);

    URL entryUrl = new URL("http://gdata.youtube.com/feeds/api/videos/"
        + videoID);

    VideoEntry videoEntry = null;
    try {
      videoEntry = service.getEntry(entryUrl, VideoEntry.class);
    } catch (ServiceException se) {
      // an invalid video ID was used.
    }

    if (videoEntry == null) {
      System.out.println("Sorry, the video ID you entered was not valid.\n");
      return;
    }

    System.out.println("Enter your comment: ");

    String input = readLine();

    String commentUrl = videoEntry.getComments().getFeedLink().getHref();

    CommentEntry newComment = new CommentEntry();
    newComment.setContent(new PlainTextConstruct(input));

    try {
      service.insert(new URL(commentUrl), newComment);
    } catch (ServiceException se) {
      System.out.println("There was an error adding your comment.\n");
      return;
    }

    System.out.println("Comment added successfully!\n");
  }

  /**
   * Uploads a new video to YouTube.
   * 
   * @param service An authenticated YouTubeService object.
   * @throws IOException Problems reading user input.
   */
  private static void uploadVideo(YouTubeService service) throws IOException {
    System.out.println("First, type in the path to the movie file:");


    File videoFile = new File(readLine());

    if (!videoFile.exists()) {
      System.out.println("Sorry, that video doesn't exist.");
      return;
    }

    System.out.println(
        "What is the MIME type of this file? (ex. 'video/quicktime' for .mov)");

    String mimeType = readLine();

    System.out.println("What should I call this video?");
    String videoTitle = readLine();

    VideoEntry newEntry = new VideoEntry();

    YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();

    mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Tech"));
    mg.setTitle(new MediaTitle());
    mg.getTitle().setPlainTextContent(videoTitle);
    mg.setKeywords(new MediaKeywords());
    mg.getKeywords().addKeyword("gdata-test");
    mg.setDescription(new MediaDescription());
    mg.getDescription().setPlainTextContent(videoTitle);
    MediaFileSource ms = new MediaFileSource(videoFile, mimeType);
    newEntry.setMediaSource(ms);

    try {
      service.insert(new URL(VIDEO_UPLOAD_FEED), newEntry);
    } catch (ServiceException se) {
      System.out.println("Sorry, your upload was invalid:");
      System.out.println(se.getResponseBody());
      return;
    }

    System.out.println("Video uploaded successfully!");
  }

  /**
   * Adds a video as a favorite.
   * 
   * @param service An authenticated YouTubeService object.
   * @throws IOException Problems reading user input.
   */
  private static void addFavorite(YouTubeService service) throws IOException {
    System.out.println("First, choose a video to favorite.");
    String videoID = readVideoID();

    System.out.println("Okay, favoriting: " + videoID);

    URL entryUrl = new URL("http://gdata.youtube.com/feeds/api/videos/"
        + videoID);

    VideoEntry videoEntry = null;
    try {
      videoEntry = service.getEntry(entryUrl, VideoEntry.class);
    } catch (ServiceException se) {
      // an invalid video ID was used.
    }

    if (videoEntry == null) {
      System.out.println("Sorry, the video ID you entered was not valid.\n");
      return;
    }

    URL feedUrl = new URL(USER_FEED_PREFIX + DEFAULT_USER
        + FAVORITES_FEED_SUFFIX);

    try {
      service.insert(feedUrl, videoEntry);
    } catch (ServiceException e) {
      System.out.println("Error adding favorite.");
      return;
    }

    System.out.println("Video favorited.");

  }


  
  /**
   * Adds a video to a particular playlist.
   * 
   * @param service An authenticated YouTubeService object.
   * @param entry The PlaylistLinkEntry describing the playlist 
   *    to add the video to.
   * @throws IOException Error reading user input.
   */
  private static void addPlaylistVideo(YouTubeService service,
      PlaylistLinkEntry entry) throws IOException {
    System.out.println("Choose a video to add to this playlist.");

    String videoID = readVideoID();

    System.out.println("Okay, adding this video: " + videoID);

    URL entryUrl = new URL("http://gdata.youtube.com/feeds/api/videos/"
        + videoID);

    VideoEntry videoEntry = null;
    try {
      videoEntry = service.getEntry(entryUrl, VideoEntry.class);
    } catch (ServiceException se) {
      // an invalid video ID was used.
    }

    if (videoEntry == null) {
      System.out.println("Sorry, the video ID you entered was not valid.\n");
      return;
    }

    String playlistUrl = entry.getFeedUrl();
    PlaylistEntry playlistEntry = new PlaylistEntry(videoEntry);

    try {
      service.insert(new URL(playlistUrl), playlistEntry);
    } catch (ServiceException e) {
      System.out.println("Error adding vide to playlist");
      return;
    }

    System.out.println("Video added to the playlist!");
  }


  /**
   * Prints the user's uploaded videos.
   * 
   * @param service An authenticated YouTubeService object.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printUploads(YouTubeService service) throws IOException,
      ServiceException {

    printVideoFeed(service, USER_FEED_PREFIX + DEFAULT_USER
        + UPLOADS_FEED_SUFFIX);
  }

  /**
   * Fetches a feed known to be a VideoFeed, printing each VideoEntry with in a
   * numbered list, optionally prompting the user for the number of a video
   * entry which should have its comments and responses printed.
   * 
   * @param service An authenticated YouTubeService object
   * @param feedUrl The url of the video feed to print.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printVideoFeed(YouTubeService service, String feedUrl)
      throws IOException, ServiceException {
    VideoFeed videoFeed = service.getFeed(new URL(feedUrl), VideoFeed.class);
    String title = videoFeed.getTitle().getPlainText();

    printUnderlined(title);
    List<VideoEntry> videoEntries = videoFeed.getEntries();
    if (videoEntries.size() == 0) {
      System.out.println("This feed contains no entries.");
      return;
    }
    int count = 1;
    for (VideoEntry entry : videoEntries) {
      System.out.println("(Video #" + String.valueOf(count) + ")");
      printVideoEntry(entry);
      count++;
    }

    System.out.println();
  }
 
  /**
   * Fetches a feed of activities and prints information about them.
   * 
   * @param service An authenticated YouTubeService object
   * @param feedUrl The url of the video feed to print.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void printActivityFeed(YouTubeService service, String feedUrl)
      throws IOException, ServiceException {
    UserEventFeed activityFeed = service.getFeed(new URL(feedUrl), 
        UserEventFeed.class);
    String title = activityFeed.getTitle().getPlainText();

    printUnderlined(title);
    if (activityFeed.getEntries().size() == 0) {
      System.out.println("This feed contains no entries.");
      return;
    }
    for (UserEventEntry entry : activityFeed.getEntries()) {
      String user = entry.getAuthors().get(0).getName();
      if(entry.getUserEventType() == UserEventEntry.Type.VIDEO_UPLOADED) {
        System.out.println(user + " uploaded a video " + entry.getVideoId());
      }
      else if(entry.getUserEventType() == UserEventEntry.Type.VIDEO_RATED) {
        System.out.println(user + " rated a video " + entry.getVideoId() +
            " " + entry.getRating().getValue() + " stars");
      }
      else if(entry.getUserEventType() == UserEventEntry.Type.VIDEO_FAVORITED) {
        System.out.println(user + " favorited a video " + entry.getVideoId());
      }
      else if(entry.getUserEventType() == UserEventEntry.Type.VIDEO_SHARED) {
        System.out.println(user + " shared a video " + entry.getVideoId());
      }
      else if(entry.getUserEventType() == UserEventEntry.Type.VIDEO_COMMENTED) {
        System.out.println(user + " commented on video " + entry.getVideoId());
      }
      else if(entry.getUserEventType() 
          == UserEventEntry.Type.USER_SUBSCRIPTION_ADDED) {
        System.out.println(user + " subscribed to the channel of " + 
            entry.getUsername());
      }
      else if(entry.getUserEventType() == UserEventEntry.Type.FRIEND_ADDED) {
        System.out.println(user + " friended " + entry.getUsername());
      }
    }

    System.out.println();
  }

  /**
   * Performs a given operation on the user's playlists feed
   * 
   * @param service An authenticated YouTubeService object.
   * @param op The operation to perform on the playlists feed.
   * @throws IOException Error sending request or reading the feed.
   * @throws ServiceException If the service is unable to handle the request.
   */
  private static void doPlaylistFeedOperation(YouTubeService service,
      PlaylistOperation op) throws IOException, ServiceException {
    String feedUrl = USER_FEED_PREFIX + DEFAULT_USER + PLAYLISTS_FEED_SUFFIX;


    if (op == PlaylistOperation.CREATE) {
      System.out.println("Creating new playlist!");
      System.out.println("Enter a title: ");
      String title = readLine();
      System.out.println("Enter a description: ");
      String description = readLine();

      PlaylistLinkEntry newEntry = new PlaylistLinkEntry();
      newEntry.setTitle(new PlainTextConstruct(title));
      newEntry.setSummary(
          TextConstruct.create(TextConstruct.Type.TEXT, description, null));

      service.insert(new URL(feedUrl), newEntry);

      return;
    }


    PlaylistLinkFeed playlistLinkFeed = service.getFeed(new URL(feedUrl),
        PlaylistLinkFeed.class);
    String title = playlistLinkFeed.getTitle().getPlainText();

    printUnderlined(title);
    List<PlaylistLinkEntry> playlistEntries = playlistLinkFeed.getEntries();
    int count = 1;
    for (PlaylistLinkEntry pe : playlistEntries) {
      printPlaylistEntry("(Playlist #" + count + ")", pe, false);
      count++;
    }

    if (op == PlaylistOperation.LIST) {
      System.out.printf("\nWhich playlist do you want to see? (1-%d): \n",
          count - 1);
      int whichVideo = readInt();
      if (whichVideo < 1 || whichVideo > count - 1) {
        System.out.println("Invalid choice.");
      } else {
        printPlaylistEntry("", playlistEntries.get(whichVideo - 1), true);
      }
    }

    if (op == PlaylistOperation.ADD) {
      System.out.printf("\nWhich playlist do you want to add to? (1-%d): \n",
          count - 1);
      int whichVideo = readInt();
      if (whichVideo < 1 || whichVideo > count - 1) {
        System.out.println("Invalid choice.");
      } else {
        addPlaylistVideo(service, playlistEntries.get(whichVideo - 1));
      }
    }

    System.out.println();
  }

  /**
   * YouTubeWriteClient is a sample command line application that
   * demonstrates many features of the YouTube Data API using the Java Client
   * library.
   * 
   * This sample demonstrates both upload and write activities in the API.
   * 
   * @param args Used to pass the username and password of a test account.
   */
  public static void main(String[] args) {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String developerKey = parser.getValue("key", "k");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null || developerKey == null) {
      printUsage();
      System.exit(1);
    }

    YouTubeService service = new YouTubeService("gdataSample-YouTubeAuth-1",
        developerKey);

    try {
      service.setUserCredentials(username, password);
    } catch (AuthenticationException e) {
      System.out.println("Invalid login credentials.");
      System.exit(1);
    }

    while (true) {
      try {

        printMenu();
        int choice = readInt();

        switch (choice) {
        case 1:
          // Prints out the user's uploaded videos
          printUploads(service);
          break;
        case 2:
          // Accesses the user's playlists
          showPlaylists(service);
          break;
        case 3:
          // Accesses the user's favorites
          showFavorites(service);
          break;
        case 4:
          // Adds a comment to a video
          addComment(service);
          break;
        case 5:
          // Uploads a new video to YouTube
          uploadVideo(service);
          break;
        case 6:
          // adds a new favorite video
          addFavorite(service);
          break;
        case 7:
          // print the user's activities
          showActivity(service);
          break;
        case 8:
          // print the user's friends' activities
          showFriendsActivity(service);
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
