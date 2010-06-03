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

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpResponseException;
import com.google.api.data.sample.youtube.model.Debug;
import com.google.api.data.sample.youtube.model.Video;
import com.google.api.data.sample.youtube.model.VideoFeed;
import com.google.api.data.sample.youtube.model.YouTubeUrl;
import com.google.api.data.youtube.v2.YouTube;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Yaniv Inbar
 */
public class YouTubeJsoncSample {

  // TODO: sample of starting a web browser and listening to callback using
  // local server

  enum AuthType {
    OAUTH, CLIENT_LOGIN
  }

  private static AuthType AUTH_TYPE = AuthType.OAUTH;
  private static final int MAX_VIDEOS_TO_SHOW = 5;

  static OAuthHmacSigner signer;
  static OAuthCredentialsResponse credentials;

  public static void main(String[] args) throws IOException {
    Debug.enableLogging();
    try {
      GoogleTransport transport = setUpGoogleTransport();
      VideoFeed feed = showVideos(transport);
    } catch (HttpResponseException e) {
      System.err.println(e.response.parseAsString());
      throw e;
    } finally {
      if (credentials != null) {
        try {
          GoogleOAuthGetAccessToken.revokeAccessToken(createOAuthParameters());
        } catch (Exception e) {
          e.printStackTrace(System.err);
        }
      }
    }
  }

  private static GoogleTransport setUpGoogleTransport() throws IOException {
    GoogleTransport transport = new GoogleTransport();
    GoogleHeaders.setUserAgent(transport.defaultHeaders,
        "google-youtubejsoncsample-1.0");
    GoogleHeaders.setGDataVersion(transport.defaultHeaders, YouTube.VERSION);
    transport.addParser(new JsonCParser());
    if (AUTH_TYPE == AuthType.OAUTH) {
      authorizeUsingOAuth(transport);
    } else {
      authorizeUsingClientLogin(transport);
    }
    return transport;
  }


  private static OAuthParameters createOAuthParameters() {
    OAuthParameters authorizer = new OAuthParameters();
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
    temporaryToken.scope = "http://gdata.youtube.com/feeds";
    temporaryToken.displayName =
        "YouTube JSON-C Sample for the GData Java library";
    OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
    signer.tokenSharedSecret = tempCredentials.tokenSecret;
    System.out
        .println("Please go open this web page in a browser to authorize:");
    GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl =
        new GoogleOAuthAuthorizeTemporaryTokenUrl();
    authorizeUrl.temporaryToken = tempCredentials.token;
    System.out.println(authorizeUrl.build());
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
    createOAuthParameters().signRequestsUsingAuthorizationHeader(transport);
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
    // build URL for the video feed for "search stories"
    YouTubeUrl url = YouTubeUrl.fromRelativePath("videos");
    url.maxResults = MAX_VIDEOS_TO_SHOW;
    url.author = "searchstories";
    // execute GData request for the feed
    VideoFeed feed = VideoFeed.executeGet(transport, url);
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
}
