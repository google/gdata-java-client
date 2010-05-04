/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.picasa;

import com.google.api.client.auth.oauth.OAuthAuthorizer;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUri;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.Entity;
import com.google.api.client.xml.atom.AtomHttpParser;
import com.google.api.data.picasa.v2.Picasa;
import com.google.api.data.picasa.v2.PicasaPath;
import com.google.api.data.picasa.v2.atom.PicasaAtom;
import com.google.api.data.sample.picasa.model.AlbumEntry;
import com.google.api.data.sample.picasa.model.AlbumFeed;
import com.google.api.data.sample.picasa.model.PhotoEntry;
import com.google.api.data.sample.picasa.model.UserFeed;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PicasaBasicAtomSample {
  static OAuthHmacSigner signer;
  static OAuthCredentialsResponse credentials;

  public static void main(String[] args) throws IOException {
    // enableLogging();
    try {
      GoogleTransport transport = setUpGoogleTransport();
      UserFeed feed = showAlbums(transport);
      AlbumEntry album = postAlbum(transport, feed);
      PhotoEntry postedPhoto = postPhoto(transport, album);
      album = getUpdatedAlbum(transport, album);
      album = updateTitle(transport, album);
      deleteAlbum(transport, album);
    } catch (HttpResponseException e) {
      if (e.response.getParser() != null) {
        System.err.println(e.response.parseAs(Entity.class));
      } else {
        System.err.println(e.response.parseAsString());
      }
      throw e;
    } finally {
      if (credentials != null) {
        try {
          GoogleOAuthGetAccessToken.revokeAccessToken(createOAuthAuthorizer());
        } catch (Exception e) {
          e.printStackTrace(System.err);
        }
      }
    }
  }

  private static GoogleTransport setUpGoogleTransport() throws IOException {
    GoogleTransport transport =
        new GoogleTransport("google-picasaatomsample-1.0");
    transport.setGDataVersionHeader(Picasa.VERSION);
    AtomHttpParser parser = new AtomHttpParser();
    parser.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    transport.setParser(parser);
    authorizeUsingOAuth(transport);
    // authorizeUsingClientLogin(transport);
    return transport;
  }

  private static OAuthAuthorizer createOAuthAuthorizer() {
    OAuthAuthorizer authorizer = new OAuthAuthorizer();
    authorizer.consumerKey = "anonymous";
    authorizer.signer = signer;
    authorizer.token = credentials.token;
    return authorizer;
  }

  private static void authorizeUsingOAuth(GoogleTransport transport)
      throws IOException {
    GoogleOAuthGetTemporaryToken temporaryToken =
        new GoogleOAuthGetTemporaryToken();
    signer = new OAuthHmacSigner();
    signer.clientSharedSecret = "anonymous";
    temporaryToken.signer = signer;
    temporaryToken.consumerKey = "anonymous";
    temporaryToken.scope = "http://picasaweb.google.com/data";
    temporaryToken.displayName =
        "Picasa Atom XML Sample for the GData Java library";
    OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
    signer.tokenSharedSecret = tempCredentials.tokenSecret;
    System.out
        .println("Please go open this web page in a browser to authorize:");
    GoogleOAuthAuthorizeTemporaryTokenUri authorizeUri =
        new GoogleOAuthAuthorizeTemporaryTokenUri();
    authorizeUri.temporaryToken = tempCredentials.token;
    System.out.println(authorizeUri.build());
    System.out.println();
    System.out.println("Press enter to continue...");
    new Scanner(System.in).nextLine();
    GoogleOAuthGetAccessToken accessToken = new GoogleOAuthGetAccessToken();
    accessToken.temporaryToken = tempCredentials.token;
    accessToken.signer = signer;
    accessToken.consumerKey = "anonymous";
    accessToken.verifier = "";
    credentials = accessToken.execute();
    signer.tokenSharedSecret = credentials.tokenSecret;
    transport.defaultHeaders.authorizer = createOAuthAuthorizer();
  }

  private static void authorizeUsingClientLogin(GoogleTransport transport)
      throws IOException {
    ClientLogin authenticator = new ClientLogin();
    authenticator.authTokenType = Picasa.AUTH_TOKEN_TYPE;
    Scanner s = new Scanner(System.in);
    System.out.println("Username: ");
    authenticator.username = s.nextLine();
    System.out.println("Password: ");
    authenticator.password = s.nextLine();
    authenticator.authenticate().setAuthorizationHeader(transport);
  }

  private static UserFeed showAlbums(GoogleTransport transport)
      throws IOException {
    // build URI for the default user feed of albums
    PicasaPath path = PicasaPath.feed();
    path.user = "default";
    // execute GData request for the feed
    UserFeed feed = UserFeed.executeGet(transport, path.build());
    System.out.println("User: " + feed.author.name);
    System.out.println("Total number of albums: " + feed.totalResults);
    // show albums
    for (AlbumEntry album : feed.albums) {
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
      AlbumFeed feed = AlbumFeed.executeGet(transport, album.getFeedLink());
      for (PhotoEntry photo : feed.photos) {
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

  private static AlbumEntry postAlbum(GoogleTransport transport, UserFeed feed)
      throws IOException {
    System.out.println();
    AlbumEntry newAlbum = new AlbumEntry();
    newAlbum.access = "private";
    newAlbum.title = "A new album";
    newAlbum.summary = "My favorite photos";
    AlbumEntry album = feed.insertAlbum(transport, newAlbum);
    showAlbum(transport, album);
    return album;
  }

  private static PhotoEntry postPhoto(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    String fileName = "picasaweblogo-en_US.gif";
    String photoUrlString = "http://www.google.com/accounts/lh2/" + fileName;
    URL photoUrl = new URL(photoUrlString);
    PhotoEntry photo =
        PhotoEntry.executeInsert(transport, album.getFeedLink(), photoUrl,
            fileName);
    System.out.println("Posted photo: " + photo.title);
    return photo;
  }

  private static AlbumEntry getUpdatedAlbum(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    album = AlbumEntry.executeGet(transport, album.getSelfLink());
    showAlbum(transport, album);
    return album;
  }

  private static AlbumEntry updateTitle(GoogleTransport transport,
      AlbumEntry album) throws IOException {
    AlbumEntry patched = album.clone();
    patched.title = "My favorite web logos";
    album = patched.executePatchRelativeToOriginal(transport, album);
    showAlbum(transport, album);
    return album;
  }

  private static void deleteAlbum(GoogleTransport transport, AlbumEntry album)
      throws IOException {
    album.executeDelete(transport);
    System.out.println();
    System.out.println("Album deleted.");
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
