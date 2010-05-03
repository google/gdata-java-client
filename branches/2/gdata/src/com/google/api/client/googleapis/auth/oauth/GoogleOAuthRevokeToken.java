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

import com.google.api.client.auth.oauth.AbstractOAuthRequest;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthAuthorizer;
import com.google.api.client.util.Hide;

import java.io.IOException;

/**
 * Google OAuth 1.0a URI entity to request to revoke a long-lived access token
 * from the Google Authorization server.
 * <p>
 * Use {@link #execute()} to execute the request.
 */
public final class GoogleOAuthRevokeToken extends AbstractOAuthRequest {

  /**
   * Required long-lived access token. It can be retrieved from the
   * {@link OAuthCredentialsResponse#token} returned from
   * {@link GoogleOAuthGetAccessToken#execute()}.
   */
  @Hide
  public volatile String accessToken;

  public GoogleOAuthRevokeToken() {
    super("https://www.google.com/accounts/AuthSubRevokeToken");
  }

  /**
   * Revokes the access token.
   * 
   * @throws IOException I/O exception
   */
  public void execute() throws IOException {
    executeRequest().ignore();
  }

  @Override
  public OAuthAuthorizer createAuthorizer() {
    OAuthAuthorizer result = super.createAuthorizer();
    result.token = this.accessToken;
    return result;
  }
}
