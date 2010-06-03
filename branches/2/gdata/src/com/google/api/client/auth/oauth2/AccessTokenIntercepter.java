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

import com.google.api.client.http.HttpExecuteIntercepter;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;

/**
 * Authorizes HTTP requests by setting the {@code "access_token"} query
 * parameter every time an HTTP request is executed.
 * <p>
 * Sample usage:
 * 
 * <pre>
 * <code>static void authenticate(HttpTransport transport, String accessToken) {
 *   AccessTokenIntercepter intercepter = new AccessTokenIntercepter();
 *   intercepter.accessToken = accessToken;
 *   intercepter.authorize(transport);
 * }
 * </code>
 * </pre>
 * 
 * @author Yaniv Inbar
 * @since 2.3
 */
public final class AccessTokenIntercepter implements HttpExecuteIntercepter {

  /** Access token to use. */
  public String accessToken;

  public void intercept(HttpRequest request) {
    request.url.set("access_token", this.accessToken);
  }

  /**
   * Sets this as the first HTTP request execute intercepter for the given HTTP
   * transport.
   */
  public void authorize(HttpTransport transport) {
    transport.removeIntercepters(AccessTokenIntercepter.class);
    AccessTokenIntercepter newIntercepter = new AccessTokenIntercepter();
    transport.intercepters.add(0, newIntercepter);
    newIntercepter.accessToken = this.accessToken;
  }
}
