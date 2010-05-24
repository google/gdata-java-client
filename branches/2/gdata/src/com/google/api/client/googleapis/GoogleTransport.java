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

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;

/**
 * HTTP transport for Google API's.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GoogleTransport extends HttpTransport {

  /**
   * If {@code true}, the GData HTTP client library will use POST to send data
   * to the associated GData service and will specify the actual method using
   * the method override HTTP header. This can be used as a workaround for HTTP
   * proxies or gateways that do not handle PUT, PATCH, or DELETE HTTP methods
   * properly. If {@code false}, the regular verbs will be used.
   */
  public static boolean ENABLE_METHOD_OVERRIDE = false;

  /**
   * Required application name of the format {@code
   * "[company-id]-[app-name]-[app-version]"}.
   */
  public String applicationName;

  /**
   * Sets the {@code "GData-Version"} header required by Google Data API's.
   * 
   * @param version version of the Google Data API being access, for example
   *        {@code "2"}.
   */
  public void setVersionHeader(String version) {
    this.defaultHeaders.set("GData-Version", version);
  }

  /**
   * Sets the Client Login token (implemented as a {@code GoogleLogin} {@code
   * Authorization} header) based on the given authentication token. This is
   * primarily intended for use in the Android environment after retrieving the
   * authentication token from the AccountManager.
   */
  public void setClientLoginToken(String authToken) {
    this.defaultHeaders.authorization = getClientLoginHeaderValue(authToken);
  }

  /**
   * Returns Client Login authentication header value based on the given
   * authentication token.
   */
  public static String getClientLoginHeaderValue(String authToken) {
    return "GoogleLogin auth=" + authToken;
  }

  @Override
  public HttpRequest buildGetRequest() {
    checkApplicationName();
    return super.buildGetRequest();
  }

  @Override
  public HttpRequest buildPostRequest() {
    checkApplicationName();
    return super.buildPostRequest();
  }

  @Override
  public HttpRequest buildDeleteRequest() {
    if (!ENABLE_METHOD_OVERRIDE) {
      checkApplicationName();
      return super.buildDeleteRequest();
    }
    return buildMethodOverride("DELETE");
  }

  @Override
  public HttpRequest buildPatchRequest() {
    if (!ENABLE_METHOD_OVERRIDE && useLowLevelHttpTransport().supportsPatch()) {
      checkApplicationName();
      return super.buildPatchRequest();
    }
    return buildMethodOverride("PATCH");
  }

  @Override
  public HttpRequest buildPutRequest() {
    if (!ENABLE_METHOD_OVERRIDE) {
      checkApplicationName();
      return super.buildPutRequest();
    }
    return buildMethodOverride("PUT");
  }

  private HttpRequest buildMethodOverride(String method) {
    HttpRequest request = buildPostRequest();
    request.headers.set("X-HTTP-Method-Override", method);
    return request;
  }

  private void checkApplicationName() {
    String applicationName = this.applicationName;
    if (applicationName == null) {
      throw new IllegalArgumentException("applicationName not specified");
    }
    HttpHeaders defaultHeaders = this.defaultHeaders;
    if (defaultHeaders.userAgent == null) {
      defaultHeaders.userAgent =
          applicationName + " Google-API-Java/2.2.1-alpha(gzip)";
    }
  }
}
