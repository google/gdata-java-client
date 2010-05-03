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

import com.google.api.client.util.Hide;

/**
 * OAuth 1.0a URI entity to request a temporary credentials token
 * ("request token") that has not yet been authorized from the authorization
 * server.
 * <p>
 * Use {@link #execute()} to execute the request. The request token acquired
 * with this request is found in {@link OAuthCredentialsResponse#token}. This
 * request token is used in {@link OAuthAuthorizeTokenRequestUri#requestToken}
 * to direct the end user to an authorization page to allow the end user to
 * authorize the request token.
 */
public class OAuthGetRequestToken extends AbstractOAuthGetToken {

  /**
   * Optional absolute URI back to which the server will redirect the resource
   * owner when the Resource Owner Authorization step is completed or {@code
   * null} for none.
   */
  @Hide
  public volatile String callback;

  /**
   * @param authorizationServerUri encoded authorization server URI
   */
  public OAuthGetRequestToken(String authorizationServerUri) {
    super(authorizationServerUri);
  }

  @Override
  public OAuthAuthorizer createAuthorizer() {
    OAuthAuthorizer result = super.createAuthorizer();
    result.callback = this.callback;
    return result;
  }
}
