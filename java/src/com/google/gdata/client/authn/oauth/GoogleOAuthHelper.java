/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.client.authn.oauth;

/**
 * Extends the {@link OAuthHelper} class with details specific to making
 * OAuth requests with Google.
 *
 * 
 */
public class GoogleOAuthHelper extends OAuthHelper {

  /**
   * Google's OAuth endpoints
   */
  private static String requestTokenUrl =
      "https://www.google.com/accounts/OAuthGetRequestToken";
  private static String userAuthorizationUrl =
      "https://www.google.com/accounts/OAuthAuthorizeToken";
  private static String accessTokenUrl =
    "https://www.google.com/accounts/OAuthGetAccessToken";
  private static String revokeTokenUrl =
    "https://www.google.com/accounts/AuthSubRevokeToken";

  /**
   * Creates a new GoogleOAuthHelper, which is an {@link OAuthHelper} with
   * specific urls.
   *
   * @param signer the {@link OAuthSigner} to use when signing the request
   */
  public GoogleOAuthHelper(OAuthSigner signer) {
    super(requestTokenUrl, userAuthorizationUrl, accessTokenUrl, revokeTokenUrl,
        signer);
  }

  /**
   * Creates a new GoogleOAuthHelper, which is an {@link OAuthHelper} with
   * specific urls.
   *
   * @param signer the {@link OAuthSigner} to use when signing the request
   * @param httpClient the {@link OAuthHttpClient} to use when making http
   *        requests
   */
  GoogleOAuthHelper(OAuthSigner signer, OAuthHttpClient httpClient) {
    super(requestTokenUrl, userAuthorizationUrl, accessTokenUrl, revokeTokenUrl,
        signer, httpClient);
  }
}
