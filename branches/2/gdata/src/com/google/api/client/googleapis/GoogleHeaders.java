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

package com.google.api.client.googleapis;

import com.google.api.client.escape.PercentEscaper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;

/**
 * HTTP headers for Google API's.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GoogleHeaders {

  private static final PercentEscaper SLUG_ESCAPER =
      new PercentEscaper(" !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~", false);

  /**
   * Sets up the given HTTP transport with basic default behaviors for working
   * with Google API's.
   * 
   * @param transport HTTP transport
   * @since 2.3
   */
  public static void setUp(HttpTransport transport) {
    MethodOverrideIntercepter.setFor(transport);
  }

  /**
   * Sets the {@code "Slug"} header for the given file name into the given HTTP
   * headers, properly escaping the header value. See <a
   * href="http://tools.ietf.org/html/rfc5023#section-9.7">The Slug Header</a>.
   */
  public static void setSlug(HttpHeaders headers, String fileName) {
    headers.set("Slug", SLUG_ESCAPER.escape(fileName));
  }

  /**
   * Sets the {@code "User-Agent"} header for the given application name of the
   * form {@code "[company-id]-[app-name]-[app-version]"} into the given HTTP
   * headers.
   * 
   * @since 2.3
   */
  public static void setUserAgent(HttpHeaders headers, String applicationName) {
    headers.userAgent = applicationName;
  }

  /**
   * Sets the {@code "GData-Version"} header for the given version into the
   * given HTTP headers.
   * 
   * @since 2.3
   */
  public static void setGDataVersion(HttpHeaders headers, String version) {
    headers.set("GData-Version", version);
  }

  /**
   * Sets the {@code "GData-Key"} header for the given developer ID into the
   * given HTTP headers.
   * 
   * @since 2.3
   */
  public static void setGDataKey(HttpHeaders headers, String developerId) {
    headers.set("X-GData-Key", "key=" + developerId);
  }

  /**
   * Sets the {@code "GData-Version"} header for the given application name of
   * the form {@code "[company-id]-[app-name]-[app-version]"} into the given
   * HTTP headers.
   * 
   * @since 2.3
   */
  public static void setGDataClient(HttpHeaders headers, String applicationName) {
    headers.set("X-GData-Client", applicationName);
  }

  /**
   * Sets the Google Login {@code "Authorization"} header for the given
   * authentication token into the given HTTP headers.
   * 
   * @since 2.3
   */
  public static void setGoogleLogin(HttpHeaders headers, String authToken) {
    headers.authorization = getGoogleLoginValue(authToken);
  }

  /**
   * Returns Google Login {@code "Authorization"} header value based on the
   * given authentication token.
   * 
   * @since 2.3
   */
  public static String getGoogleLoginValue(String authToken) {
    return "GoogleLogin auth=" + authToken;
  }

  /**
   * Sets the {@code "X-HTTP-Method-Override"} header for the given HTTP method
   * into the given HTTP headers.
   * 
   * @since 2.3
   */
  public static void setMethodOverride(HttpHeaders headers, String method) {
    headers.set("X-HTTP-Method-Override", method);
  }
}
