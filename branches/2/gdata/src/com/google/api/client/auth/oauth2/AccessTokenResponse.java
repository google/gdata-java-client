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

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Key;

/**
 * OAuth 2.0 access token success response as specified in <a
 * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.3.2.1"
 * >Access Token Response</a>.
 * <p>
 * Use {@link #authorize(HttpTransport)} to authorize HTTP requests based on the
 * {@link #accessToken}.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class AccessTokenResponse {

  /** (REQUIRED) The access token issued by the authorization server. */
  @Key("access_token")
  public String accessToken;

  /**
   * (OPTIONAL) The duration in seconds of the access token lifetime.
   */
  @Key("expires_in")
  public Long expiresIn;

  /**
   * (OPTIONAL) The refresh token used to obtain new access tokens using the
   * same end-user access grant as described in Section 4.
   */
  @Key("refresh_token")
  public String refreshToken;

  /**
   * (REQUIRED if requested by the client) The corresponding access token secret
   * as requested by the client.
   */
  @Key("access_token_secret")
  public String accessTokenSecret;

  /**
   * (OPTIONAL) The scope of the access token as a list of space- delimited
   * strings. The value of the "scope" parameter is defined by the authorization
   * server. If the value contains multiple space-delimited strings, their order
   * does not matter, and each string adds an additional access range to the
   * requested scope.
   */
  @Key
  public String scope;

  /**
   * Uses the value of the {@link #accessToken} to authorize HTTP requests by
   * setting the {@code "access_token"} query parameter.
   */
  public final void authorize(HttpTransport transport) {
    AccessTokenIntercepter.authorize(transport, this.accessToken);
  }
}
