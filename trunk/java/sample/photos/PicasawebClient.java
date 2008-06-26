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


package sample.photos;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Link;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.CommentEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.TagEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple client that provides high-level operations on the Picasa Web
 * Albums GData API. It can also be used as a command-line application to test
 * out some of the features of the API.
 *
 * 
 */
public class PicasawebClient {

  private static final String API_PREFIX
      = "http://picasaweb.google.com/data/feed/api/user/";

  private final PicasawebService service;

  /**
   * Constructs a new un-authenticated client.
   */
  public PicasawebClient(PicasawebService service) {
    this(service, null, null);
  }

  /**
   * Constructs a new client with the given username and password.
   */
  public PicasawebClient(PicasawebService service, String uname,
      String passwd) {
    this.service = service;

    if (uname != null && passwd != null) {
      try {
        service.setUserCredentials(uname, passwd);
      } catch (AuthenticationException e) {
        throw new IllegalArgumentException(
            "Illegal username/password combination.");
      }
    }
  }

  /**
   * Retrieves the albums for the given user.
   */
  public List<AlbumEntry> getAlbums(String username) throws IOException,
      ServiceException {

    String albumUrl = API_PREFIX + username;
    UserFeed userFeed = getFeed(albumUrl, UserFeed.class);

    List<GphotoEntry> entries = userFeed.getEntries();
    List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
    for (GphotoEntry entry : entries) {
      GphotoEntry adapted = entry.getAdaptedEntry();
      if (adapted instanceof AlbumEntry) {
        albums.add((AlbumEntry) adapted);
      }
    }
    return albums;
  }

  /**
   * Retrieves the albums for the currently logged-in user.  This is equivalent
   * to calling {@link #getAlbums(String)} with "default" as the username.
   */
  public List<AlbumEntry> getAlbums() throws IOException, ServiceException {
    return getAlbums("default");
  }

  /**
   * Retrieves the tags for the given user.  These are tags aggregated across
   * the entire account.
   */
  public List<TagEntry> getTags(String uname) throws IOException,
      ServiceException {
    String tagUrl = API_PREFIX + uname + "?kind=tag";
    UserFeed userFeed = getFeed(tagUrl, UserFeed.class);

    List<GphotoEntry> entries = userFeed.getEntries();
    List<TagEntry> tags = new ArrayList<TagEntry>();
    for (GphotoEntry entry : entries) {
      GphotoEntry adapted = entry.getAdaptedEntry();
      if (adapted instanceof TagEntry) {
        tags.add((TagEntry) adapted);
      }
    }
    return tags;
  }

  /**
   * Retrieves the tags for the currently logged-in user.  This is equivalent
   * to calling {@link #getTags(String)} with "default" as the username.
   */
  public List<TagEntry> getTags() throws IOException, ServiceException {
    return getTags("default");
  }

  /**
   * Retrieves the photos for the given album.
   */
  public List<PhotoEntry> getPhotos(AlbumEntry album) throws IOException,
      ServiceException {

    String feedHref = getLinkByRel(album.getLinks(), Link.Rel.FEED);
    AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

    List<GphotoEntry> entries = albumFeed.getEntries();
    List<PhotoEntry> photos = new ArrayList<PhotoEntry>();
    for (GphotoEntry entry : entries) {
      GphotoEntry adapted = entry.getAdaptedEntry();
      if (adapted instanceof PhotoEntry) {
        photos.add((PhotoEntry) adapted);
      }
    }
    return photos;
  }

  /**
   * Retrieves the comments for the given photo.
   */
  public List<CommentEntry> getComments(PhotoEntry photo) throws IOException,
      ServiceException {

    String feedHref = getLinkByRel(photo.getLinks(), Link.Rel.FEED);
    AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

    List<GphotoEntry> entries = albumFeed.getEntries();
    List<CommentEntry> comments = new ArrayList<CommentEntry>();
    for (GphotoEntry entry : entries) {
      GphotoEntry adapted = entry.getAdaptedEntry();
      if (adapted instanceof CommentEntry) {
        comments.add((CommentEntry) adapted);
      }
    }
    return comments;
  }

  /**
   * Retrieves the tags for the given taggable entry.  This is valid on user,
   * album, and photo entries only.
   */
  public List<TagEntry> getTags(GphotoEntry<?> parent) throws IOException,
      ServiceException {

    String feedHref = getLinkByRel(parent.getLinks(), Link.Rel.FEED);
    feedHref = addKindParameter(feedHref, "tag");
    AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

    List<GphotoEntry> entries = albumFeed.getEntries();
    List<TagEntry> tags = new ArrayList<TagEntry>();
    for (GphotoEntry entry : entries) {
      GphotoEntry adapted = entry.getAdaptedEntry();
      if (adapted instanceof TagEntry) {
        tags.add((TagEntry) adapted);
      }
    }
    return tags;
  }

  /**
   * Album-specific insert method to insert into the gallery of the current
   * user, this bypasses the need to have a top-level entry object for parent.
   */
  public AlbumEntry insertAlbum(AlbumEntry album)
      throws IOException, ServiceException {
    String feedUrl = API_PREFIX + "default";
    return service.insert(new URL(feedUrl), album);
  }

  /**
   * Insert an entry into another entry.  Because our entries are a hierarchy,
   * this lets you insert a photo into an album even if you only have the
   * album entry and not the album feed, making it quicker to traverse the
   * hierarchy.
   */
  public <T extends GphotoEntry> T insert(GphotoEntry<?> parent, T entry)
      throws IOException, ServiceException {

    String feedUrl = getLinkByRel(parent.getLinks(), Link.Rel.FEED);
    return service.insert(new URL(feedUrl), entry);
  }

  /**
   * Helper function to allow retrieval of a feed by string url, which will
   * create the URL object for you.  Most of the Link objects have a string
   * href which must be converted into a URL by hand, this does the conversion.
   */
  public <T extends GphotoFeed> T getFeed(String feedHref,
      Class<T> feedClass) throws IOException, ServiceException {
    System.out.println("Get Feed URL: " + feedHref);
    return service.getFeed(new URL(feedHref), feedClass);
    }

  /**
   * Helper function to add a kind parameter to a url.
   */
  public String addKindParameter(String url, String kind) {
    if (url.contains("?")) {
      return url + "&kind=" + kind;
    } else {
      return url + "?kind=" + kind;
    }
  }

  /**
   * Helper function to get a link by a rel value.
   */
  public String getLinkByRel(List<Link> links, String relValue) {
    for (Link link : links) {
      if (relValue.equals(link.getRel())) {
        return link.getHref();
      }
    }
    throw new IllegalArgumentException("Missing " + relValue + " link.");
  }
}
