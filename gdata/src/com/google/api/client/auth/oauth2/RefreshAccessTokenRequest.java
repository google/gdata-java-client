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

import com.google.api.client.util.Key;

/**
 * OAuth 2.0 request to refresh an access token as specified in <a
 * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-06#section-3"
 * >Refreshing an Access Token</a>.
 * <p>
 * The most commonly set fields are {@link #clientId}, {@link #clientSecret},
 * and {@link #refreshToken}. Call {@link #execute()} to execute the request.
 * <p>
 * Sample usage:
 * 
 * <pre>
 * <code>static AccessTokenResponse executeRefreshToken(String refreshToken)
 *     throws IOException {
 *   RefreshAccessTokenRequest tokenRequest =
 *       new RefreshAccessTokenRequest(ACCESS_TOKEN_URL);
 *   tokenRequest.clientId = CLIENT_ID;
 *   tokenRequest.clientSecret = CLIENT_SECRET;
 *   tokenRequest.refreshToken = refreshToken;
 *   return tokenRequest.execute();
 * }</code>
 * </pre>
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class RefreshAccessTokenRequest extends AbstractAccessTokenRequest {

  /**
   * (REQUIRED) The refresh token associated with the access token to be
   * refreshed.
   */
  @Key("refresh_token")
  public String refreshToken;

  /**
   * @param encodedAuthorizationServerUrl encoded authorization server URL
   */
  public RefreshAccessTokenRequest(String encodedAuthorizationServerUrl) {
    super(encodedAuthorizationServerUrl, "refresh");
  }
}
