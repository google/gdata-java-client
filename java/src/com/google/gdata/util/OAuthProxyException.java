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


package com.google.gdata.util;

import com.google.gdata.client.authn.oauthproxy.OAuthProxyResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Represents an error thrown by the OAuth Proxy in an App Engine environment.
 * Provides getters/setters for OAuth Proxy-specific parameters.
 *
 * 
 */
public class OAuthProxyException extends ServiceException {

  private final OAuthProxyResponse response;

  /**
   * Creates a new {@link OAuthProxyException}, and populates the OAuth Proxy
   * specific parameters from the {@link HttpURLConnection}.
   *
   * @param httpConn The http response.
   * @throws IOException
   */
  public OAuthProxyException(HttpURLConnection httpConn) throws IOException {
    super(httpConn);
    response = super.getOAuthProxyResponse();
  }

  /** Gets the value of the "x_oauth_state" header from the http response. */
  public String getState() {
    return response.getState();
  }

  /**
   * Gets the value of the "x_oauth_approval_url" header from the http
   * response.
   */
  public String getApprovalUrl() {
    return response.getApprovalUrl();
  }

  /** Gets the value of the "x_oauth_error" header from the http response. */
  public String getError() {
    return response.getError();
  }

  /**
   * Gets the value of the "x_oauth_error_text" header from the http
   * response.
   */
  public String getErrorText() {
    return response.getErrorText();
  }
}
