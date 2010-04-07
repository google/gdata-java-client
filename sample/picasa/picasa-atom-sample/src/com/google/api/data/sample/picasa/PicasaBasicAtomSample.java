package com.google.api.data.sample.picasa;

import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.InputStreamHttpSerializer;
import com.google.api.client.http.UriEntity;
import com.google.api.client.http.auth.googleapis.clientlogin.ClientLogin;
import com.google.api.client.http.googleapis.GoogleHttp;
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.xml.atom.AtomHttpParser;
import com.google.api.client.http.xml.atom.AtomSerializer;
import com.google.api.client.xml.XmlEntity;
import com.google.api.data.picasa.v2.Picasa;
import com.google.api.data.picasa.v2.PicasaPath;
import com.google.api.data.picasa.v2.atom.PicasaAtom;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PicasaBasicAtomSample {

  private static final String APP_NAME = "google-picasaatomsample-1.0";

  private static final int MAX_ALBUMS_TO_SHOW = 3;
  private static final int MAX_PHOTOS_TO_SHOW = 5;

  public static class PicasaUri extends UriEntity {

    @Name("max-results")
    public Integer maxResults;

    public String kinds;

    public PicasaUri(String uri) {
      super(uri);
    }
  }

  public static class Link {

    @Name("@href")
    public String href;

    @Name("@rel")
    public String rel;
  }

  public static String getLink(List<Link> links, String rel) {
    for (Link link : links) {
      if (rel.equals(link.rel)) {
        return link.href;
      }
    }
    return null;
  }

  public static class Category {

    @Name("@scheme")
    public String scheme;

    @Name("@term")
    public String term;
  }

  public static Category newKind(String kind) {
    Category category = new Category();
    category.scheme = "http://schemas.google.com/g/2005#kind";
    category.term = "http://schemas.google.com/photos/2007#" + kind;
    return category;
  }

  public static class AlbumEntry extends Entry {

    @Name("gphoto:access")
    public String access;

    public Category category = newKind("album");

    @Name("gphoto:numphotos")
    public int numPhotos;
  }

  public static class Author {

    public String name;
  }

  public static class Feed {

    public Author author;

    @Name("openSearch:totalResults")
    public int totalResults;

    @Name("link")
    public List<Link> links;

    String getPostLink() {
      return getLink(links, "http://schemas.google.com/g/2005#post");
    }
  }

  public static class AlbumFeed extends Feed {
    @Name("entry")
    public List<AlbumEntry> entries;
  }

  public static class PhotoFeed extends Feed {
    @Name("entry")
    public List<PhotoEntry> entries;
  }

  public static class Entry {

    @Name("@gd:etag")
    public String etag;

    @Name("link")
    public List<Link> links;

    public String summary;

    public String title;

    public String updated;

    public String getEditLink() {
      return getLink(links, "edit");
    }

    public String getFeedLink() {
      return getLink(links, "http://schemas.google.com/g/2005#feed");
    }

    public String getSelfLink() {
      return getLink(links, "self");
    }
  }

  public static class MediaContent {

    @Name("@type")
    public String type;

    @Name("@url")
    public String url;
  }

  public static class MediaGroup {
    @Name("media:content")
    public MediaContent content;
  }


  public static class PhotoEntry extends Entry {

    public Category category = newKind("photo");

    @Name("media:group")
    public MediaGroup mediaGroup;
  }

  public static void main(String[] args) throws Exception {
    // enableLogging();
    GoogleTransport transport = authenticate();
    AlbumFeed feed = showAlbums(transport);
    AlbumEntry album = postAlbum(transport, feed);
    PhotoEntry postedPhoto = postPhoto(transport, album);
    album = getUpdatedAlbum(transport, album);
    album = updateTitle(transport, album);
    deleteAlbum(transport, album);
  }

  private static GoogleTransport authenticate() throws IOException {
    ClientLogin authenticator = new ClientLogin();
    authenticator.authTokenType = Picasa.AUTH_TOKEN_TYPE;
    Scanner s = new Scanner(System.in);
    System.out.println("Username: ");
    authenticator.username = s.nextLine();
    System.out.println("Password: ");
    authenticator.password = s.nextLine();
    GoogleTransport transport = new GoogleTransport(APP_NAME);
    authenticator.authenticate().setAuthorizationHeader(transport);
    transport.setGDataVersionHeader(Picasa.VERSION);
    AtomHttpParser parser = new AtomHttpParser();
    parser.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    transport.setParser(parser);
    return transport;
  }

  private static AlbumFeed showAlbums(GoogleTransport transport)
      throws IOException {
    // build URI for the default user feed of albums
    PicasaPath path = PicasaPath.feed();
    path.user = "default";
    PicasaUri uri = new PicasaUri(path.build());
    uri.kinds = "album";
    uri.maxResults = MAX_ALBUMS_TO_SHOW;
    // execute GData request for the feed
    HttpRequest request = transport.buildGetRequest(uri.build());
    AlbumFeed feed = request.execute().parseAs(AlbumFeed.class);
    System.out.println("User: " + feed.author.name);
    System.out.println("Total number of albums: " + feed.totalResults);
    // show albums
    for (AlbumEntry album : feed.entries) {
      showAlbum(transport, album);
    }
    return feed;
  }

  private static void showAlbum(GoogleTransport transport, AlbumEntry album)
      throws IOException {
    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println("Album title: " + album.title);
    System.out.println("Updated: " + album.updated);
    System.out.println("Album ETag: " + album.etag);
    if (album.summary != null) {
      System.out.println("Description: " + album.summary);
    }
    if (album.numPhotos != 0) {
      System.out.println("Total number of photos: " + album.numPhotos);
      PicasaUri uri = new PicasaUri(album.getFeedLink());
      uri.kinds = "photo";
      uri.maxResults = MAX_PHOTOS_TO_SHOW;
      HttpRequest request = transport.buildGetRequest(uri.build());
      PhotoFeed feed = request.execute().parseAs(PhotoFeed.class);
      for (PhotoEntry photo : feed.entries) {
        System.out.println();
        System.out.println("Photo title: " + photo.title);
        if (photo.summary != null) {
          System.out.println("Photo description: " + photo.summary);
        }
        System.out.println("Image MIME type: " + photo.mediaGroup.content.type);
        System.out.println("Image URL: " + photo.mediaGroup.content.url);
      }
    }
  }

  private static AlbumEntry postAlbum(GoogleTransport transport, AlbumFeed feed)
      throws IOException {
    System.out.println();
    AlbumEntry newAlbum = new AlbumEntry();
    newAlbum.access = "private";
    newAlbum.title = "A new album";
    newAlbum.summary = "My favorite photos";
    HttpRequest request = transport.buildPostRequest(feed.getPostLink());
    request.setContent(new AtomSerializer(PicasaAtom.NAMESPACE_DICTIONARY,
        newAlbum));
    AlbumEntry album = request.execute().parseAs(AlbumEntry.class);
    showAlbum(transport, album);
    return album;
  }

  private static PhotoEntry postPhoto(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    String fileName = "picasaweblogo-en_US.gif";
    String photoUrlString = "http://www.google.com/accounts/lh2/" + fileName;
    URL photoUrl = new URL(photoUrlString);
    HttpRequest request = transport.buildPostRequest(album.getFeedLink());
    GoogleHttp.setSlugHeader(request, fileName);
    request.setContent(new InputStreamHttpSerializer(photoUrl.openStream(), -1,
        "image/jpeg", null));
    PhotoEntry photo = request.execute().parseAs(PhotoEntry.class);
    System.out.println("Posted photo: " + photo.title);
    return photo;
  }

  private static AlbumEntry getUpdatedAlbum(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    PicasaUri selfUri = new PicasaUri(album.getSelfLink());
    HttpRequest request = transport.buildGetRequest(selfUri.build());
    album = request.execute().parseAs(AlbumEntry.class);
    showAlbum(transport, album);
    return album;
  }

  private static AlbumEntry updateTitle(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    // must do a GET into AtomEntity
    HttpRequest request = transport.buildGetRequest(album.getSelfLink());
    XmlEntity albumToEdit = request.execute().parseAs(XmlEntity.class);
    // now can safely do a PUT with the returned AtomEntity
    albumToEdit.set("title", "My favorite web logos");
    request = transport.buildPutRequest(album.getEditLink());
    request.setIfMatchHeader(album.etag);
    request.setContent(new AtomSerializer(PicasaAtom.NAMESPACE_DICTIONARY,
        albumToEdit));
    album = request.execute().parseAs(AlbumEntry.class);
    // show updated album
    showAlbum(transport, album);
    return album;
  }

  private static void deleteAlbum(GoogleTransport transport, AlbumEntry album)
      throws IOException {
    HttpRequest request = transport.buildDeleteRequest(album.getEditLink());
    request.setIfMatchHeader(album.etag);
    request.execute().ignore();
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
