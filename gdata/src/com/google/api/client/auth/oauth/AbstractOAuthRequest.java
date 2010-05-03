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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriEntity;
import com.google.api.client.util.Hide;

import java.io.IOException;

/**
 * OAuth 1.0a URI entity to make a request to the authorization server.
 */
public abstract class AbstractOAuthRequest extends UriEntity {

  /**
   * Required identifier portion of the client credentials (equivalent to a
   * username).
   */
  @Hide
  public volatile String consumerKey;

  /** Required OAuth signature algorithm. */
  @Hide
  public volatile OAuthSigner signer;

  /**
   * @param authorizationServerUri encoded authorization server URI 
   */
  protected AbstractOAuthRequest(String authorizationServerUri) {
    super(authorizationServerUri);
  }

  /** Returns a new instance of the OAuth authorizer. */
  public OAuthAuthorizer createAuthorizer() {
    OAuthAuthorizer result = new OAuthAuthorizer();
    result.consumerKey = this.consumerKey;
    result.signer = this.signer;
    return result;
  }

  /**
   * Executes the request.
   * 
   * @return HTTP response
   * @throws IOException I/O exception
   */
  protected HttpResponse executeRequest() throws IOException {
    HttpRequest request = new HttpTransport().buildGetRequest(build());
    request.headers.authorizer = createAuthorizer();
    return request.execute();
  }
}
