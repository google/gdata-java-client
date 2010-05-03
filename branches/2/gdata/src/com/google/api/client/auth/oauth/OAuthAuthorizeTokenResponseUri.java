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

import com.google.api.client.http.UriEntity;
import com.google.api.client.util.Name;

/** URI entity that parses the response to authorize the OAuth request token. */
public class OAuthAuthorizeTokenResponseUri extends UriEntity {

  /** The temporary credentials identifier received from the client. */
  @Name("oauth_token")
  public String token;

  /** The verification code. */
  @Name("oauth_verifier")
  public String verifier;

  public OAuthAuthorizeTokenResponseUri(String encodedUri) {
    super(encodedUri);
  }
}
