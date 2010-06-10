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

package com.google.api.client.auth.oauth2;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.util.Key;

import java.io.IOException;

/**
 * Abstract OAuth 2.0 request for an access token.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class AbstractAccessTokenRequest {

  /** (REQUIRED). */
  @Key
  public final String type;

  /** (REQUIRED) The client identifier. */
  @Key("client_id")
  public String clientId;

  /**
   * (REQUIRED if the client identifier has a matching secret) The client
   * secret.
   */
  @Key("client_secret")
  public String clientSecret;

  /**
   * (OPTIONAL) The response format requested by the client. Value MUST be one
   * of "json", "xml", or "form" or {@code null } for "json". Default value is
   * {@code "form"}.
   */
  @Key
  public String format = "form";

  /** Encoded authorization server URL. */
  public final String encodedAuthorizationServerUrl;

  /**
   * HTTP parser used to parse the response from the server. Default value is to
   * construct {@link UrlEncodedParser}.
   */
  public HttpParser parser = new UrlEncodedParser();

  AbstractAccessTokenRequest(String encodedAuthorizationServerUrl, String type) {
    this.encodedAuthorizationServerUrl = encodedAuthorizationServerUrl;
    this.type = type;
  }

  /**
   * Executes request for an access token, and returns the parsed access token
   * response.
   * 
   * @return parsed access token response
   * @throws HttpResponseException for an HTTP error response; use
   *         {@link AccessTokenErrorResponse} to parse
   *         {@link HttpResponseException#response}
   * @throws IOException I/O exception
   */
  public final AccessTokenResponse execute() throws IOException {
    HttpTransport transport = new HttpTransport();
    transport.addParser(this.parser);
    HttpRequest request = transport.buildPostRequest();
    request.setUrl(this.encodedAuthorizationServerUrl);
    UrlEncodedContent content = new UrlEncodedContent();
    content.data = this;
    request.content = content;
    return request.execute().parseAs(AccessTokenResponse.class);
  }
}
