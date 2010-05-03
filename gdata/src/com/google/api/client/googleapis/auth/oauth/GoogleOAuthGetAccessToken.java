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

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthAuthorizer;
import com.google.api.client.http.HttpHeaders;

/**
 * Google OAuth 1.0a URI entity to request to exchange the request token for a
 * long-lived access token from the Google Authorization server.
 * <p>
 * Use {@link #execute()} to execute the request. The access token acquired with
 * this request is found in {@link OAuthCredentialsResponse#token}. This access
 * 
 * token must be stored. This access token may then be used to authorize HTTP
 * requests to protected resources in Google services by setting the
 * {@link OAuthAuthorizer} for {@link HttpHeaders#authorizer}.
 */
public final class GoogleOAuthGetAccessToken extends OAuthGetAccessToken {

  public GoogleOAuthGetAccessToken() {
    super("https://www.google.com/accounts/OAuthGetAccessToken");
  }
}
