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

import com.google.api.client.util.Name;

/**
 * Entity to parse a success response to a request for temporary or token
 * credentials.
 */
public final class OAuthCredentialsResponse {

  /** Credentials token. */
  @Name("oauth_token")
  public volatile String token;

  /**
   * Credentials shared-secret for use with {@code "HMAC-SHA1"} signature
   * algorithm. Used for {@link OAuthHmacSigner#tokenSharedSecret}.
   */
  @Name("oauth_token_secret")
  public volatile String tokenSecret;

  /**
   * {@code "true"} for temporary credentials request or {@code null} for a
   * token credentials request. The parameter is used to differentiate from
   * previous versions of the protocol.
   */
  @Name("oauth_callback_confirmed")
  public volatile Boolean callbackConfirmed;
}
