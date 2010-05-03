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

package com.google.api.client.auth.oauth;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedFormHttpParser;

import java.io.IOException;

/**
 * OAuth 1.0a URI entity to request a credentials token from the authorization
 * server.
 */
public abstract class AbstractOAuthGetToken extends AbstractOAuthRequest {

  /**
   * @param authorizationServerUri encoded authorization server URI
   */
  protected AbstractOAuthGetToken(String authorizationServerUri) {
    super(authorizationServerUri);
  }

  /**
   * Executes the request.
   * 
   * @return OAuth credentials response object
   * @throws IOException I/O exception
   */
  public final OAuthCredentialsResponse execute() throws IOException {
    HttpResponse response = executeRequest();
    response.disableContentLogging = true;
    OAuthCredentialsResponse oauthResponse = new OAuthCredentialsResponse();
    UrlEncodedFormHttpParser.parse(response.parseAsString(), oauthResponse);
    return oauthResponse;
  }
}
