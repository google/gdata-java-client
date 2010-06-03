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

import com.google.api.client.auth.oauth2.UserAgentFlow.AuthorizationResponse;
import com.google.api.client.auth.oauth2.UserAgentFlow.AuthorizationUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonHttpParser;
import com.google.api.data.sample.facebook.graph.model.Debug;
import com.google.api.data.sample.facebook.graph.model.Page;
import com.google.api.data.sample.facebook.graph.model.User;
import com.google.api.data.sample.facebook.graph.model.UserData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * See <a href="http://developers.facebook.com/docs/reference/api">Graph API
 * Reference</a> and <a href="http://developers.facebook.com/docs/api">Graph
 * API</a> and <a
 * href="http://developers.facebook.com/docs/authentication">Authentication</a>.
 * 
 * @author Yaniv Inbar
 */
public class FacebookGraphSample {

  public static void main(String[] args) throws IOException, URISyntaxException {
    if (Debug.ENABLED) {
      enableLogging();
    }
    HttpTransport transport = new HttpTransport();
    transport.addParser(new JsonHttpParser("text/javascript"));
    authenticate(transport);
    getPage(transport);
    getUser(transport);
    getMe(transport);
    getMyFriends(transport);
  }

  private static void authenticate(HttpTransport transport)
      throws URISyntaxException {
    System.out
        .println("Please go open this web page in a browser to authorize:");
    AuthorizationUrl authorizeUrl =
        new AuthorizationUrl("https://graph.facebook.com/oauth/authorize");
    authorizeUrl.clientId = "122046651166114";
    authorizeUrl.redirectUri =
        "http://www.facebook.com/connect/login_success.html";
    authorizeUrl.set("display", "popup");
    System.out.println(authorizeUrl.build());
    System.out.println();
    System.out
        .println("Copy/Paste the URL that the web browser has directed you to: ");
    String entered = new Scanner(System.in).nextLine();
    final AuthorizationResponse response = new AuthorizationResponse(entered);
    if (response.error != null) {
      System.err.println("Authorization denied.");
      System.exit(1);
    }
    response.authorize(transport);
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

  private static void header(String name) {
    System.out.println();
    System.out.println("----------- " + name + " -----------");
  }
}
