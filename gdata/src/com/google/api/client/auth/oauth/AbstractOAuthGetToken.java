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

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriEntity;
import com.google.api.client.http.UrlEncodedFormHttpParser;
import com.google.api.client.util.Hide;

import java.io.IOException;

/**
 * OAuth 1.0a URI entity to request a temporary or long-lived token from an
 * authorization server.
 * 
 * @since 2.2
 */
public abstract class AbstractOAuthGetToken extends UriEntity {

  /**
   * Required identifier portion of the client credentials (equivalent to a
   * username).
   */
  @Hide
  public volatile String consumerKey;

  /** Required OAuth signature algorithm. */
  @Hide
  public volatile OAuthSigner signer;

  /** {@code true} for POST request or the default {@code false} for GET request. */
  protected volatile boolean usePost;

  /**
   * @param authorizationServerUri encoded authorization server URI
   */
  protected AbstractOAuthGetToken(String authorizationServerUri) {
    super(authorizationServerUri);
  }

  /**
   * Executes the HTTP request for a temporary or long-lived token.
   * 
   * @return OAuth credentials response object
   * @throws HttpResponseException for an HTTP error code
   * @throws IOException I/O exception
   */
  public final OAuthCredentialsResponse execute() throws IOException {
    HttpTransport transport = new HttpTransport();
    String uri = build();
    HttpRequest request =
        this.usePost ? transport.buildPostRequest(uri) : transport
            .buildGetRequest(uri);
    request.headers.authorizer = createAuthorizer();
    HttpResponse response = request.execute();
    response.disableContentLogging = true;
    OAuthCredentialsResponse oauthResponse = new OAuthCredentialsResponse();
    UrlEncodedFormHttpParser.parse(response.parseAsString(), oauthResponse);
    return oauthResponse;
  }

  /**
   * Returns a new instance of the OAuth authorizer. Subclasses may override by
   * calling {@code super.createAuthorizer()} and then adding OAuth parameters.
   */
  public OAuthAuthorizer createAuthorizer() {
    OAuthAuthorizer result = new OAuthAuthorizer();
    result.consumerKey = this.consumerKey;
    result.signer = this.signer;
    return result;
  }
}
