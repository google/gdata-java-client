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


package com.google.gdata.client.http;

import com.google.gdata.client.AuthTokenFactory.AuthToken;

import java.net.URL;

/**
 * The HttpAuthToken interface represents a token used to authenticate
 * an HTTP request.  It encapsulates the functionality to create
 * the "Authorization" header to be appended to a HTTP request.
 */
public interface HttpAuthToken extends AuthToken {

  /**
   * Returns an authorization header to be used for a HTTP request
   * for the respective authentication token.
   *
   * @param requestUrl the URL being requested
   * @param requestMethod the HTTP method of the request
   * @return the "Authorization" header to be used for the request
   */
  public String getAuthorizationHeader(URL requestUrl,
                                       String requestMethod);
}
