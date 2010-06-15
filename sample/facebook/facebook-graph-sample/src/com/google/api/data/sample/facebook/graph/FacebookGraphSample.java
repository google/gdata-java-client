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

package com.google.api.data.sample.facebook.graph;

import com.google.api.client.auth.oauth2.AccessProtectedResource;
import com.google.api.client.auth.oauth2.UserAgentAuthorizationRequestUrl;
import com.google.api.client.auth.oauth2.UserAgentAuthorizationResponse;
import com.google.api.client.http.HttpExecuteIntercepter;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonHttpParser;
import com.google.api.data.sample.facebook.graph.model.Debug;
import com.google.api.data.sample.facebook.graph.model.Page;
import com.google.api.data.sample.facebook.graph.model.User;
import com.google.api.data.sample.facebook.graph.model.UserData;

import java.io.IOException;
import java.util.Scanner;

/**
 * See <a href="http://developers.facebook.com/docs/reference/api">Graph API
 * Reference</a> and <a href="http://developers.facebook.com/docs/api">Graph
 * API</a> and <a
 * href="http://developers.facebook.com/docs/authentication">Authentication</a>.
 * 
 * @author Yaniv Inbar
 */
public class FacebookGraphSample {

  static final String CLIENT_ID = "102073869841647";
  static final String BASE_AUTHORIZATION_URL =
      "https://graph.facebook.com/oauth/authorize";
  static final String REDIRECT_URL =
      "http://www.facebook.com/connect/login_success.html";

  public static void main(String[] args) throws IOException {
    Debug.enableLogging();
    HttpTransport transport = new HttpTransport();
    authorize(transport);
    run(transport);
  }

  private static void authorize(HttpTransport transport) {
    System.out
        .println("Please go open this web page in a browser to authorize:");
    UserAgentAuthorizationRequestUrl builder =
        new UserAgentAuthorizationRequestUrl(BASE_AUTHORIZATION_URL);
    builder.clientId = CLIENT_ID;
    builder.redirectUri = REDIRECT_URL;
    builder.set("display", "popup");
    System.out.println(builder.build());
    System.out.println();
    System.out
        .println("Copy/Paste the URL that the web browser has directed you to: ");
    String entered = new Scanner(System.in).nextLine();
    UserAgentAuthorizationResponse response =
        new UserAgentAuthorizationResponse(entered);
    if (response.error != null) {
      System.err.println("Authorization denied.");
      System.exit(1);
    }
    accessProtectedResource(transport, response.accessToken);
  }

  /**
   * Facebook Graph API doesn't conform to latest draft of the OAuth 2.0
   * specification so we cannot use {@link AccessProtectedResource} and instead
   * need to write this ourselves.
   */
  private static void accessProtectedResource(HttpTransport transport,
      final String accessToken) {
    transport.intercepters.add(new HttpExecuteIntercepter() {

      public void intercept(HttpRequest request) {
        request.url.set("access_token", accessToken);
      }
    });
  }

  private static void run(HttpTransport transport) throws IOException {
    JsonHttpParser parser = new JsonHttpParser();
    parser.contentType = "text/javascript";
    transport.addParser(parser);
    getPage(transport);
    getUser(transport);
    getMe(transport);
    getMyFriends(transport);
  }

  private static void getPage(HttpTransport transport) throws IOException {
    header("GET PAGE");
    HttpRequest request = transport.buildGetRequest();
    request.setUrl("https://graph.facebook.com/19292868552");
    Page node = request.execute().parseAs(Page.class);
    System.out.println(Json.toString(node));
  }

  private static void getUser(HttpTransport transport) throws IOException {
    header("GET USER");
    HttpRequest request = transport.buildGetRequest();
    request.setUrl("https://graph.facebook.com/yanivinbar");
    User node = request.execute().parseAs(User.class);
    System.out.println(Json.toString(node));
  }

  private static void getMe(HttpTransport transport) throws IOException {
    header("GET ME");
    HttpRequest request = transport.buildGetRequest();
    request.setUrl("https://graph.facebook.com/me");
    User node = request.execute().parseAs(User.class);
    System.out.println(Json.toString(node));
  }

  private static void getMyFriends(HttpTransport transport) throws IOException {
    header("GET MY FRIENDS");
    HttpRequest request = transport.buildGetRequest();
    request.setUrl("https://graph.facebook.com/me/friends");
    UserData node = request.execute().parseAs(UserData.class);
    System.out.println(Json.toString(node));
  }

  private static void header(String name) {
    System.out.println();
    System.out.println("============== " + name + " ==============");
  }
}
