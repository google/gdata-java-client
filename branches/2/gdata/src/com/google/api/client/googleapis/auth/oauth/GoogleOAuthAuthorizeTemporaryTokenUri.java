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

package com.google.api.client.googleapis.auth.oauth;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUri;
import com.google.api.client.auth.oauth.OAuthCallbackUri;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.util.Name;

/**
 * Google OAuth 1.0a URL builder for a Google Accounts web page to allow the end
 * user to authorize the temporary token.
 * <p>
 * The {@link #temporaryToken} should be set from the
 * {@link OAuthCredentialsResponse#token} returned by
 * {@link GoogleOAuthGetTemporaryToken#execute()}. Use {@link #build()} to build
 * the authorization URL. If a {@link OAuthGetTemporaryToken#callback} was
 * specified, after the end user grants the authorization, the Google
 * authorization server will redirect to that callback URL. To parse the
 * response, use {@link OAuthCallbackUri}.
 * 
 * @since 2.2
 */
public final class GoogleOAuthAuthorizeTemporaryTokenUri extends
    OAuthAuthorizeTemporaryTokenUri {

  /**
   * Optionally use {@code "mobile"} to for a mobile version of the approval
   * page or {@code null} for normal.
   */
  @Name("btmpl")
  public volatile String template;

  /**
   * Optional value identifying a particular Google Apps (hosted) domain account
   * to be accessed (for example, 'mycollege.edu') or {@code null} or {@code
   * "default"} for a regular Google account ('username@gmail.com').
   */
  @Name("hd")
  public volatile String hostedDomain;

  /**
   * Optional ISO 639 country code identifying what language the approval page
   * should be translated in (for example, 'hl=en' for English) or {@code null}
   * for the user's selected language.
   */
  @Name("hl")
  public volatile String language;

  public GoogleOAuthAuthorizeTemporaryTokenUri() {
    super("https://www.google.com/accounts/OAuthAuthorizeToken");
  }
}
