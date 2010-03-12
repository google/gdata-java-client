package com.google.api.data.sample.picasa;

import com.google.api.data.client.auth.clientlogin.ClientLoginAuthenticator;
import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.InputStreamHttpSerializer;
import com.google.api.data.client.v2.GData;
import com.google.api.data.client.v2.GDataUri;
import com.google.api.data.client.v2.Name;
import com.google.api.data.client.v2.jsonc.JsoncEntity;
import com.google.api.data.client.v2.jsonc.jackson.JacksonHttpParser;
import com.google.api.data.client.v2.jsonc.jackson.JsoncSerializer;
import com.google.api.data.picasa.v2.Picasa;
import com.google.api.data.picasa.v2.PicasaPath;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PicasaBasicJsoncSample {

  private static final String APP_NAME = "google-picasajsoncsample-1.0";

  private static final int MAX_ALBUMS_TO_SHOW = 3;
  private static final int MAX_PHOTOS_TO_SHOW = 5;

  public static class PicasaUri extends GDataUri {

    @Name("max-results")
    public Integer maxResults;

    public String kinds;

    public PicasaUri(String uri) {
      super(uri);
      this.alt = "jsonc";
    }
  }

  public static class Album extends Item {
    public String access;
    public final String kind = "album";
    public Links links;
    public int numPhotos;
  }

  public static class Feed {
    public String author;
    public Links links;
    public int totalItems;
  }

  public static class AlbumFeed extends Feed {
    public List<Album> items;
  }

  public static class PhotoFeed extends Feed {
    public List<Photo> items;
  }

  public static class Image {
    public String type;
    public String url;
  }

  public static class Item {
    public String description;
    public String etag;
    public String title;
  }

  public static class Links {
    public String edit;
    public String feed;
    public String post;
    public String self;
  }

  public static class Media {
    public Image image;
  }

  public static class Photo extends Item {
    public Media media;
  }

  public static void main(String[] args) throws Exception {
    // enableLogging();
    HttpTransport transport = authenticate();
    AlbumFeed feed = showAlbums(transport);
    Album album = postAlbum(transport, feed);
    Photo postedPhoto = postPhoto(transport, album);
    album = getUpdatedAlbum(transport, album);
    album = updateTitle(transport, album);
    deleteAlbum(transport, album);
  }

  private static HttpTransport authenticate() throws IOException {
    HttpTransport transport = new HttpTransport(APP_NAME);
    ClientLoginAuthenticator authenticator = new ClientLoginAuthenticator();
    authenticator.httpTransport = transport;
    authenticator.applicationName = APP_NAME;
    authenticator.authTokenType = Picasa.AUTH_TOKEN_TYPE;
    Scanner s = new Scanner(System.in);
    System.out.println("Username: ");
    authenticator.username = s.nextLine();
    System.out.println("Password: ");
    authenticator.password = s.nextLine();
    authenticator.authenticate();
    GData.setVersionHeader(transport, Picasa.VERSION);
    JacksonHttpParser.set(transport);
    return transport;
  }

  private static AlbumFeed showAlbums(HttpTransport transport)
      throws IOException {
    // build URI for the default user feed of albums
    PicasaPath path = PicasaPath.feed();
    path.user = "default";
    PicasaUri uri = new PicasaUri(path.build());
    uri.kinds = "album";
    uri.maxResults = MAX_ALBUMS_TO_SHOW;
    // execute GData request for the feed
    HttpRequest request = transport.buildGetRequest(uri.build());
    AlbumFeed feed = request.execute(AlbumFeed.class);
    System.out.println("User: " + feed.author);
    System.out.println("Total number of albums: " + feed.totalItems);
    // show albums
    for (Album album : feed.items) {
      showAlbum(transport, album);
    }
    return feed;
  }

  private static void showAlbum(HttpTransport transport, Album album)
      throws IOException {
    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println("Album title: " + album.title);
    System.out.println("Album ETag: " + album.etag);
    System.out.println("Description: " + album.description);
    System.out.println("Total number of photos: " + album.numPhotos);
    if (album.numPhotos != 0) {
      PicasaUri uri = new PicasaUri(album.links.feed);
      uri.kinds = "photo";
      uri.maxResults = MAX_PHOTOS_TO_SHOW;
      HttpRequest request = transport.buildGetRequest(uri.build());
      PhotoFeed feed = request.execute(PhotoFeed.class);
      for (Photo photo : feed.items) {
        System.out.println();
        System.out.println("Photo title: " + photo.title);
        System.out.println("Photo description: " + photo.title);
        System.out.println("Image MIME type: " + photo.media.image.type);
        System.out.println("Image URL: " + photo.media.image.url);
      }
    }
  }

  private static Album postAlbum(HttpTransport transport, AlbumFeed feed)
      throws IOException {
    System.out.println();
    Album newAlbum = new Album();
    newAlbum.access = "private";
    newAlbum.title = "A new album";
    newAlbum.description = "My favorite photos";
    HttpRequest request = transport.buildPostRequest(feed.links.post);
    JsoncSerializer.setContent(request, newAlbum);
    Album album = request.execute(Album.class);
    showAlbum(transport, album);
    return album;
  }

  private static Photo postPhoto(HttpTransport transport, Album album)
      throws IOException {
    String fileName = "picasaweblogo-en_US.gif";
    String photoUrlString = "http://www.google.com/accounts/lh2/" + fileName;
    URL photoUrl = new URL(photoUrlString);
    HttpRequest request = transport.buildPostRequest(album.links.feed);
    GData.setSlugHeader(request, fileName);
    InputStreamHttpSerializer.setContent(request, photoUrl.openStream(), -1,
        "image/jpeg", null);
    Photo photo = request.execute(Photo.class);
    System.out.println("Posted photo: " + photo.title);
    return photo;
  }

  private static Album getUpdatedAlbum(HttpTransport transport, Album album)
      throws IOException {
    HttpRequest request = transport.buildGetRequest(album.links.self);
    album = request.execute(Album.class);
    showAlbum(transport, album);
    return album;
  }

  private static Album updateTitle(HttpTransport transport, Album album)
      throws IOException {
    // must do a GET into JsoncEntity
    HttpRequest request = transport.buildGetRequest(album.links.self);
    JsoncEntity albumToEdit = request.execute(JsoncEntity.class);
    // now can safely do a PUT with the returned JsoncEntity
    albumToEdit.set("title", "My favorite web logos");
    request = transport.buildPutRequest(album.links.edit);
    GData.setIfMatchHeader(request, album.etag);
    JsoncSerializer.setContent(request, albumToEdit);
    album = request.execute(Album.class);
    // show updated album
    showAlbum(transport, album);
    return album;
  }

  private static void deleteAlbum(HttpTransport transport, Album album)
      throws IOException {
    HttpRequest request = transport.buildDeleteRequest(album.links.edit);
    GData.setIfMatchHeader(request, album.etag);
    request.executeIgnoreResponse();
    System.out.println();
    System.out.println("Album deleted.");
  }

  private static void enableLogging() {
    Logger logger = Logger.getLogger("com.google.api.data");
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
