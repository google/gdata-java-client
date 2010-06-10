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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * OAuth 2.0 User Agent flow as specified in <a
 * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-06#section-2.6"
 * >User-Agent Flow</a>.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class UserAgentFlow {

  /**
   * URL builder for an authorization web page to allow the end user to
   * authorize the application to access their protected resources.
   * <p>
   * Sample usage:
   * 
   * <pre>
   * <code>static String getAuthorizationUrl(String authorizationServer,
   *     String clientId, String redirectUri) {
   *   AuthorizationUrl result = new AuthorizationUrl(authorizationServer);
   *   result.clientId = clientId;
   *   result.redirectUri = redirectUri;
   *   return result.build();
   * }
   * </code>
   * </pre>
   */
  public static class AuthorizationUrl extends GenericUrl {

    /**
     * @param encodedAuthorizationServerUrl encoded authorization server URL
     */
    public AuthorizationUrl(String encodedAuthorizationServerUrl) {
      super(encodedAuthorizationServerUrl);
    }

    /** (REQUIRED) The parameter value MUST be set to "user_agent". */
    @Key
    public final String type = "user_agent";

    /** (REQUIRED) The client identifier. */
    @Key("client_id")
    public String clientId;

    /**
     * REQUIRED unless a redirection URI has been established between the client
     * and authorization server via other means. An absolute URI to which the
     * authorization server will redirect the user-agent to when the end-user
     * authorization step is completed. The authorization server SHOULD require
     * the client to pre-register their redirection URI. Authorization servers
     * MAY restrict the redirection URI to not include a query component as
     * defined by [RFC3986] section 3.
     */
    @Key("redirect_uri")
    public String redirectUri;

    /**
     * (OPTIONAL) An opaque value used by the client to maintain state between
     * the request and callback. The authorization server includes this value
     * when redirecting the user-agent back to the client.
     */
    @Key
    public String state;

    /**
     * (OPTIONAL) The scope of the access request expressed as a list of
     * space-delimited strings. The value of the "scope" parameter is defined by
     * the authorization server. If the value contains multiple space-delimited
     * strings, their order does not matter, and each string adds an additional
     * access range to the requested scope.
     */
    @Key
    public String scope;

    /**
     * (OPTIONAL) The parameter value must be set to "true" or "false". If set
     * to "true", the authorization server MUST NOT prompt the end-user to
     * authenticate or approve access. Instead, the authorization server
     * attempts to establish the end-user's identity via other means (e.g.
     * browser cookies) and checks if the end-user has previously approved an
     * identical access request by the same client and if that access grant is
     * still active. If the authorization server does not support an immediate
     * check or if it is unable to establish the end-user's identity or approval
     * status, it MUST deny the request without prompting the end-user. Defaults
     * to "false" if omitted.
     */
    @Key
    public Boolean immediate;
  }

  /**
   * Parses the redirect URL after end user grants or denies authorization.
   * <p>
   * Use {@link AccessProtectedResource} to authorize executed HTTP requests
   * based on the {@link #accessToken}.
   * <p>
   * Sample usage:
   * 
   * <pre>
   * <code>static void processRedirectUrl(HttpTransport transport, String redirectUrl)
   *     throws URISyntaxException {
   *   AuthorizationResponse response = new AuthorizationResponse(redirectUrl);
   *   if (response.error != null) {
   *     throw new RuntimeException("Authorization denied");
   *   }
   *   AccessProtectedResource.usingAuthorizationHeader(transport,
   *       response.accessToken);
   * }
   * </code>
   * </pre>
   */
  public static class AuthorizationResponse extends GenericData {

    /**
     * (REQUIRED if the end user denies authorization) MUST be set to
     * "user_denied".
     */
    @Key
    public String error;

    /** (REQUIRED if the end user grants authorization) The access token. */
    @Key("access_token")
    public String accessToken;

    /**
     * (OPTIONAL) The duration in seconds of the access token lifetime.
     */
    @Key("expires_in")
    public Long expiresIn;

    /** (OPTIONAL) The refresh token. */
    @Key("refresh_token")
    public String refreshToken;

    /**
     * REQUIRED if the "state" parameter was present in the client authorization
     * request. Set to the exact value received from the client.
     */
    @Key
    public String state;

    /**
     * @param encodedRedirectUrl encoded redirect URL
     * @throws IllegalArgumentException URI syntax exception
     */
    public AuthorizationResponse(String encodedRedirectUrl) {
      try {
        UrlEncodedParser.parse(new URI(encodedRedirectUrl).getRawFragment(),
            this);
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  private UserAgentFlow() {
  }
}
