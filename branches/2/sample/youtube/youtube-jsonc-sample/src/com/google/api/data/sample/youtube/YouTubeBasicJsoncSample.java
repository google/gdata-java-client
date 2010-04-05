package com.google.api.data.sample.youtube;

import com.google.api.client.DateTime;
import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.googleapis.GoogleUriEntity;
import com.google.api.client.http.json.googleapis.JsonHttpParser;
import com.google.api.data.youtube.v2.YouTube;
import com.google.api.data.youtube.v2.YouTubePath;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class YouTubeBasicJsoncSample {

  private static final String APP_NAME = "google-youtubejsoncsample-1.0";

  private static final int MAX_VIDEOS_TO_SHOW = 5;

  public static class YouTubeUri extends GoogleUriEntity {

    public String author;

    @Name("max-results")
    public Integer maxResults;

    public YouTubeUri(String uri) {
      super(uri);
      this.alt = "jsonc";
    }
  }


  public static class Feed {
    public int itemsPerPage;
    public int startIndex;
    public int totalItems;
    public DateTime updated;
  }

  public static class VideoFeed extends Feed {
    public List<Video> items;
  }

  public static class Item {
    public String id;
    public String title;
    public DateTime updated;
  }

  public static class Video extends Item {
    public String description;
    public List<String> tags;
    public Player player;
  }

  public static class Player {
    // "default" is a Java keyword, so need to specify the JSON key manually
    @Name("default")
    public String defaultUrl;
  }

  public static void main(String[] args) throws Exception {
    // enableLogging();
    GoogleTransport transport = newTransport();
    VideoFeed feed = showVideos(transport);
  }

  private static GoogleTransport newTransport() {
    GoogleTransport transport = new GoogleTransport(APP_NAME);
    transport.setGDataVersionHeader(YouTube.VERSION);
    JsonHttpParser.setAsParserOf(transport);
    return transport;
  }

  private static VideoFeed showVideos(GoogleTransport transport)
      throws IOException {
    // build URI for the video feed for "searchstories"
    YouTubePath path = YouTubePath.videos();
    YouTubeUri uri = new YouTubeUri(path.build());
    uri.maxResults = MAX_VIDEOS_TO_SHOW;
    uri.author = "searchstories";
    // execute GData request for the feed
    HttpRequest request = transport.buildGetRequest(uri.build());
    VideoFeed feed = request.execute().parseAs(VideoFeed.class);
    System.out.println("Total number of videos: " + feed.totalItems);
    for (Video video : feed.items) {
      showVideo(video);
    }
    return feed;
  }

  private static void showVideo(Video video) {
    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println("Video title: " + video.title);
    System.out.println("Description: " + video.description);
    System.out.println("Updated: " + video.updated);
    System.out.println("Tags: " + video.tags);
    System.out.println("Play URL: " + video.player.defaultUrl);
  }

  private static void enableLogging() {
    Logger logger = Logger.getLogger("com.google.api.client");
    logger.setLevel(Level.ALL);
    logger.addHandler(new Handler() {

      @Override
      public void close() throws SecurityException {
      }

      @Override
      public void flush() {
      }

      @Override
      public void publish(LogRecord record) {
        // default ConsoleHandler will take care of >= INFO
        if (record.getLevel().intValue() < Level.INFO.intValue()) {
          System.out.println(record.getMessage());
        }
      }
    });
  }
}
