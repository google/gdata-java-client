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

package com.google.api.client.auth.wrap;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriEntity;
import com.google.api.client.util.Name;

/**
 * Implements OAuth WRAP authorization as specified in <a
 * href="http://tools.ietf.org/html/draft-hardt-oauth-01">OAuth Web Resource
 * Authorization Profiles</a>.
 */
public class Wrap {

  /** URI entity with the {@code access_token} query parameter. */
  public static class AuthenticatedUriEntity extends UriEntity {

    @Name("access_token")
    public String accessToken;

    public AuthenticatedUriEntity(String uri) {
      super(uri);
    }
  }

  /**
   * Returns WRAP authorization header value based on the given access token.
   */
  public static String getAuthorizationHeaderValue(String accessToken) {
    return "WRAP access_token=\"" + accessToken + "\"";
  }

  /**
   * Sets the authorization header for the given HTTP transport based on the
   * given access token.
   */
  public static void setAuthorizationHeader(HttpTransport httpTransport,
      String accessToken) {
    httpTransport.defaultHeaders.setAuthorization(
        getAuthorizationHeaderValue(accessToken));
  }

  private Wrap() {
  }
}
