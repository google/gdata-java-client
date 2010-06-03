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
 * HTTP transport for Google API's. It's only purpose is to allow for method
 * overriding when the firewall does not accept DELETE, PATCH or PUT methods.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GoogleTransport extends HttpTransport {

  // TODO: deprecate GoogleTransport!
  // TODO: instead use an HttpExecuteIntercepter to override the request method!

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
   * 
   * @deprecated (scheduled to be removed in version 2.4) Use
   *             {@link GoogleHeaders#setUserAgent(HttpHeaders, String)}
   *             on {@link #defaultHeaders}
   */
  @Deprecated
  public String applicationName;

  /**
   * Sets the {@code "GData-Version"} header required by Google Data API's.
   * 
   * @param version version of the Google Data API being access, for example
   *        {@code "2"}.
   * @deprecated (scheduled to be removed in version 2.4) Use
   *             {@link GoogleHeaders#setGDataVersion(HttpHeaders, String)} on
   *             {@link #defaultHeaders}
   */
  @Deprecated
  public void setVersionHeader(String version) {
    GoogleHeaders.setGDataVersion(this.defaultHeaders, version);
  }

  /**
   * Sets the Client Login token (implemented as a {@code GoogleLogin} {@code
   * Authorization} header) based on the given authentication token. This is
   * primarily intended for use in the Android environment after retrieving the
   * authentication token from the AccountManager.
   * 
   * @deprecated (scheduled to be removed in version 2.4) Use
   *             {@link GoogleHeaders#setGoogleLogin(HttpHeaders, String)}
   */
  @Deprecated
  public void setClientLoginToken(String authToken) {
    this.defaultHeaders.authorization = getClientLoginHeaderValue(authToken);
  }

  /**
   * Returns Client Login authentication header value based on the given
   * authentication token.
   * 
   * @deprecated (scheduled to be removed in version 2.4) Use
   *             {@link GoogleHeaders#getGoogleLoginValue(String)}
   */
  @Deprecated
  public static String getClientLoginHeaderValue(String authToken) {
    return "GoogleLogin auth=" + authToken;
  }

  @Override
  public HttpRequest buildDeleteRequest() {
    if (!ENABLE_METHOD_OVERRIDE) {
      return super.buildDeleteRequest();
    }
    return buildMethodOverride("DELETE");
  }

  @Override
  public HttpRequest buildPatchRequest() {
    if (!ENABLE_METHOD_OVERRIDE && useLowLevelHttpTransport().supportsPatch()) {
      return super.buildPatchRequest();
    }
    return buildMethodOverride("PATCH");
  }

  @Override
  public HttpRequest buildPutRequest() {
    if (!ENABLE_METHOD_OVERRIDE) {
      return super.buildPutRequest();
    }
    return buildMethodOverride("PUT");
  }

  private HttpRequest buildMethodOverride(String method) {
    HttpRequest request = buildPostRequest();
    GoogleHeaders.setMethodOverride(request.headers, method);
    return request;
  }
}
