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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * OAuth 2.0 User Agent flow as specified in <a
 * href="http://tools.ietf.org/html/draft-ietf-oauth-v2#section-3.5">User-Agent
 * Flow</a>.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public final class UserAgentFlow {

  /**
   * URL builder for an authorization web page to allow the end user to
   * authorize the application to access their protected resources.
   */
  public static class AuthorizationUrl extends GenericUrl {

    /**
     * @param encodedAuthorizationServerUrl encoded authorization server URL
     */
    public AuthorizationUrl(String encodedAuthorizationServerUrl) {
      super(encodedAuthorizationServerUrl);
    }

    /** REQUIRED. The parameter value MUST be set to "user_agent". */
    @Key
    public final String type = "user_agent";

    /** REQUIRED. The client identifier as described in Section 3.1. */
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
     * OPTIONAL. An opaque value used by the client to maintain state between
     * the request and callback. The authorization server includes this value
     * when redirecting the user-agent back to the client.
     */
    @Key
    public String state;

    /**
     * OPTIONAL. The scope of the access request expressed as a list of
     * space-delimited strings. The value of the "scope" parameter is defined by
     * the authorization server. If the value contains multiple space-delimited
     * strings, their order does not matter, and each string adds an additional
     * access range to the requested scope.
     */
    @Key
    public String scope;

    /**
     * OPTIONAL. The parameter value must be set to "true" or "false". If set to
     * "true", the authorization server MUST NOT prompt the end-user to
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

    /**
     * OPTIONAL. The access token secret type as described by Section 5.3. If
     * omitted, the authorization server will issue a bearer token (an access
     * token without a matching secret) as described by Section 5.2.
     */
    @Key("secret_type")
    public String secretType;
  }

  /** Parses the redirect URL after end user grants or denies authorization. */
  public static class AuthorizationResponse extends GenericData {

    /**
     * REQUIRED if the end user denies authorization. MUST be set to
     * "user_denied".
     */
    @Key
    public String error;

    /** REQUIRED if the end user grants authorization. The access token. */
    @Key("access_token")
    public String accessToken;

    /**
     * OPTIONAL. The duration in seconds of the access token lifetime.
     */
    @Key("expires_in")
    public Long expiresIn;

    /** OPTIONAL. The refresh token. */
    @Key("refresh_token")
    public String refreshToken;

    /**
     * REQUIRED if the "state" parameter was present in the client authorization
     * request. Set to the exact value received from the client.
     */
    @Key
    public String state;

    /**
     * REQUIRED if requested by the client. The corresponding access token
     * secret as requested by the client.
     */
    @Key("access_token_secret")
    public String accessTokenSecret;

    /**
     * @param encodedUrl encoded redirect URL
     * @throws URISyntaxException URI syntax exception
     */
    public AuthorizationResponse(String encodedUrl) throws URISyntaxException {
      UrlEncodedParser.parse(new URI(encodedUrl).getRawFragment(), this);
    }

    /**
     * Uses the value of the {@link #accessToken} to authorize HTTP requests by
     * setting the {@code "access_token"} query parameter.
     */
    public void authorize(HttpTransport transport) {
      AccessTokenIntercepter newIntercepter = new AccessTokenIntercepter();
      newIntercepter.accessToken = this.accessToken;
      newIntercepter.authorize(transport);
    }
  }

  private UserAgentFlow() {
  }
}
