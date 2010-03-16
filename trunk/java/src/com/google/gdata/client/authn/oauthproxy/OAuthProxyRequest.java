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


package com.google.gdata.client.authn.oauthproxy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Stores the variables related to an OAuth Proxy request.
 *
 * 
 */
public class OAuthProxyRequest extends HashMap<String, String> {

  // Default values for certain headers.
  public static final String DEFAULT_ACCESS_TOKEN_URL =
      "https://www.google.com/accounts/OAuthGetAccessToken";
  public static final String DEFAULT_AUTHORIZATION_URL =
      "https://www.google.com/accounts/OAuthAuthorizeToken";
  public static final String DEFAULT_REQUEST_TOKEN_URL =
      "https://www.google.com/accounts/OAuthGetRequestToken";
  public static final String DEFAULT_SERVICE_NAME = "google";
  public static final String DEFAULT_USE_TOKEN = "always";

  // The requestTokenUrl is generated from the value of the url and the scope.
  // The two variables below store the intermediate variables.
  private String scope;
  private String requestTokenUrl;

  public OAuthProxyRequest() {
    super();
    // Set default values.
    setAccessTokenUrl(DEFAULT_ACCESS_TOKEN_URL);
    setAuthorizationUrl(DEFAULT_AUTHORIZATION_URL);
    setRequestTokenUrl(DEFAULT_REQUEST_TOKEN_URL);
    setServiceName(DEFAULT_SERVICE_NAME);
    setUseToken(DEFAULT_USE_TOKEN);
  }

  /**
   * Retrieves the value of the access token url.  The default value is
   * "https://www.google.com/accounts/OAuthGetAccessToken".
   */
  public String getAcessTokenUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_ACCESS_TOKEN_URL);
  }

  /** Sets the value of the access token url. */
  public void setAccessTokenUrl(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_ACCESS_TOKEN_URL, value);
  }

  /**
   * Retrieves the value of the authorization url.  The default value is
   * "https://www.google.com/accounts/OAuthAuthorizeToken".
   */
  public String getAuthorizationUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_AUTHORIZATION_URL);
  }

  /** Sets the value of the authorization url. */
  public void setAuthorizationUrl(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_AUTHORIZATION_URL, value);
  }

  /** Retrieves the value of the desired callback url. */
  public String getDesiredCallbackUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_DESIRED_CALLBACK_URL);
  }

  /** Sets the value of the desired callback url. */
  public void setDesiredCallbackUrl(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_DESIRED_CALLBACK_URL, value);
  }

  /** Retrieves the value of the received callback url. */
  public String getReceivedCallbackUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_RECEIVED_CALLBACK_URL);
  }

  /** Sets the value of the received callback url. */
  public void setReceivedCallbackUrl(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_RECEIVED_CALLBACK_URL, value);
  }

  /**
   * Retrieves the value of the request token url.  The returned value will
   * include the scope set in {@link #setScope(String)}.
   */
  public String getRequestTokenUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_REQUEST_TOKEN_URL);
  }

  /**
   * Sets the value of the request token url.  The value will include the scope
   * set in {@link #setScope(String)}.
   */
  public void setRequestTokenUrl(String value) {
    requestTokenUrl = value;
    storeRequestTokenUrl();
  }

  /** Retrieves the scope of the request. */
  public String getScope() {
    return scope;
  }

  /**
   * Sets the scope of the request.  This value will also be appended to the
   * request token url as the "scope" parameter.
   */
  public void setScope(String value) {
    scope = value;
    storeRequestTokenUrl();
  }

  /** Retrieves the service name.  Default value is "google". */
  public String getServiceName() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_SERVICE_NAME);
  }

  /** Sets the service name. */
  public void setServiceName(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_SERVICE_NAME, value);
  }

  /** Retrieves the OAuth State. */
  public String getState() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_STATE);
  }

  /** Sets the OAuth State. */
  public void setState(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_STATE, value);
  }

  /** Retrieves the token name. */
  public String getTokenName() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_TOKEN_NAME);
  }

  /** Sets the token name. */
  public void setTokenName(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_TOKEN_NAME, value);
  }

  /** Retrieves the "use token" parameter.  The default value is "always". */
  public String getUseToken() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_USE_TOKEN);
  }

  /** Sets the "use token" parameter. */
  public void setUseToken(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_USE_TOKEN, value);
  }

  /**
   * Combines the request token url and scope to create the final request token
   * url, and stores this in the parameter map.
   */
  private void storeRequestTokenUrl() {
    if (requestTokenUrl != null && scope != null) {
      requestTokenUrl += requestTokenUrl.indexOf('?') == -1 ? "?" : "&";
      try {
        requestTokenUrl += "scope=" + URLEncoder.encode(scope, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException(e);
      }
    }
    put(OAuthProxyProtocol.Header.X_OAUTH_REQUEST_TOKEN_URL, requestTokenUrl);
  }
}
