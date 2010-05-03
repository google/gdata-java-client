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

package com.google.api.data.sample.youtube;

import com.google.api.client.auth.oauth.OAuthAuthorizer;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTokenRequestUri;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetRequestToken;
import com.google.api.client.googleapis.json.JsonHttpParser;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.Entity;
import com.google.api.data.sample.youtube.model.Video;
import com.google.api.data.sample.youtube.model.VideoFeed;
import com.google.api.data.sample.youtube.model.YouTubeUri;
import com.google.api.data.youtube.v2.YouTube;
import com.google.api.data.youtube.v2.YouTubePath;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class YouTubeBasicJsoncSample {

  private static final int MAX_VIDEOS_TO_SHOW = 5;

  static OAuthHmacSigner signer;
  static OAuthCredentialsResponse credentials;

  public static void main(String[] args) throws IOException {
    enableLogging();
    try {
      GoogleTransport transport =
          new GoogleTransport("google-youtubejsoncsample-1.0");
      transport.setGDataVersionHeader(YouTube.VERSION);
      transport.setParser(new JsonHttpParser());
      authorizeUsingOAuth(transport);
      VideoFeed feed = showVideos(transport);
    } catch (HttpResponseException e) {
      if (e.response.getParser() != null) {
        System.err.println(e.response.parseAs(Entity.class));
      } else {
        System.err.println(e.response.parseAsString());
      }
      throw e;
    }
  }

  private static void authorizeUsingOAuth(GoogleTransport transport)
      throws IOException {
    GoogleOAuthGetRequestToken requestToken = new GoogleOAuthGetRequestToken();
    signer = new OAuthHmacSigner();
    signer.clientSharedSecret = "anonymous";
    requestToken.signer = signer;
    requestToken.consumerKey = "anonymous";
    requestToken.scope = "http://gdata.youtube.com/feeds";
    requestToken.displayName =
        "YouTube JSON-C Sample for the GData Java library";
    credentials = requestToken.execute();
    signer.tokenSharedSecret = credentials.tokenSecret;
    System.out
        .println("Please go open this web page in a browser to authorize:");
    GoogleOAuthAuthorizeTokenRequestUri authorizeUri =
        new GoogleOAuthAuthorizeTokenRequestUri();
    authorizeUri.requestToken = credentials.token;
    System.out.println(authorizeUri.build());
    System.out.println();
    System.out.println("Press enter to continue...");
    new Scanner(System.in).nextLine();
    GoogleOAuthGetAccessToken accessToken = new GoogleOAuthGetAccessToken();
    accessToken.requestToken = credentials.token;
    accessToken.signer = signer;
    accessToken.consumerKey = "anonymous";
    accessToken.verifier = "";
    credentials = accessToken.execute();
    signer.tokenSharedSecret = credentials.tokenSecret;
    transport.defaultHeaders.authorizer =
        OAuthAuthorizer.createFromTokenCredentials(signer, "anonymous",
            credentials.token, null);
  }

  private static void authorizeUsingClientLogin(GoogleTransport transport)
      throws IOException {
    ClientLogin authenticator = new ClientLogin();
    authenticator.authTokenType = YouTube.AUTH_TOKEN_TYPE;
    Scanner s = new Scanner(System.in);
    System.out.println("Username: ");
    authenticator.username = s.nextLine();
    System.out.println("Password: ");
    authenticator.password = s.nextLine();
    authenticator.authenticate().setAuthorizationHeader(transport);
  }

  private static VideoFeed showVideos(GoogleTransport transport)
      throws IOException {
    // build URI for the video feed for "searchstories"
    YouTubePath path = YouTubePath.videos();
    YouTubeUri uri = new YouTubeUri(path.build());
    uri.maxResults = MAX_VIDEOS_TO_SHOW;
    uri.author = "searchstories";
    // execute GData request for the feed
    VideoFeed feed = VideoFeed.executeGet(transport, uri.build());
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
