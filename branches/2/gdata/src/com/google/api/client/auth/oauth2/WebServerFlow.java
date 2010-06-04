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
import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.util.DataUtil;
import com.google.api.client.util.Key;

import java.io.IOException;

/**
 * OAuth 2.0 Web Server flow as specified in <a
 * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-05#section-3.6">Web
 * Server Flow</a>.
 * <p>
 * The Web Server flow starts with redirecting the web browser to the
 * authorization web page. Use {@link AuthorizationUrl} to construct that URL.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class WebServerFlow {

  /**
   * URL builder for an authorization web page to allow the end user to
   * authorize the application to access their protected resources.
   * <p>
   * The most commonly-set fields are {@link #clientId} and {@link #redirectUri}
   * , and possibly {@link #scope}. After the end-user grants or denies the
   * request, they will be redirected to the {@link #redirectUri} with query
   * parameters set by the authorization server. Use
   * {@link AuthorizationResponseUrl} to parse the redirect URL.
   * 
   * <p>
   * Sample usage:
   * 
   * <pre>
   * <code>static String getAuthorizationUrl(String authorizationServer,
   *     String clientId, String redirectUri, String scope) {
   *   AuthorizationUrl result = new AuthorizationUrl(authorizationServer);
   *   result.clientId = clientId;
   *   result.redirectUri = redirectUri;
   *   result.scope = scope;
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

    /** (REQUIRED) The parameter value MUST be set to "web_server". */
    @Key
    public final String type = "web_server";

    /** (REQUIRED) The client identifier as described in Section 3.1. */
    @Key("client_id")
    public String clientId;

    /**
     * REQUIRED unless a redirection URI has been established between the client
     * and authorization server via other means. An absolute URI to which the
     * authorization server will redirect the user-agent to when the end-user
     * authorization step is completed. The authorization server MAY require the
     * client to pre-register their redirection URI. Authorization servers MAY
     * restrict the redirection URI to not include a query component as defined
     * by [RFC3986] section 3.
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
   * To check if the end user grants authorization, check if {@link #error} is
   * {@code null}. If the end user grants authorization, the next step is to
   * request an access token using {@link AccessTokenRequest}. Use the
   * {@link #code} in {@link AccessTokenRequest#code}.
   */
  public static class AuthorizationResponseUrl extends GenericUrl {

    /**
     * (REQUIRED if the end user denies authorization) MUST be set to
     * "user_denied".
     */
    @Key
    public String error;

    /**
     * (REQUIRED if the end user grants authorization) The verification code
     * generated by the authorization server.
     */
    @Key
    public String code;

    /**
     * REQUIRED if the "state" parameter was present in the client authorization
     * request. Set to the exact value received from the client.
     */
    @Key
    public String state;

    /**
     * @param encodedRedirectUrl encoded redirect URL
     */
    public AuthorizationResponseUrl(String encodedRedirectUrl) {
      super(encodedRedirectUrl);
    }
  }

  /**
   * Request an access token.
   * <p>
   * The most commonly set fields are {@link #clientId}, {@link #clientSecret},
   * {@link #code}, and {@link #redirectUri}. Call {@link #execute()} to execute
   * the request.
   */
  public static class AccessTokenRequest {

    /** (REQUIRED) The parameter value MUST be set to "web_server". */
    @Key
    public final String type = "web_server";

    /** (REQUIRED) The client identifier as described in Section 3.1. */
    @Key("client_id")
    public String clientId;

    /**
     * (REQUIRED if the client identifier has a matching secret) The client
     * secret as described in Section 3.1.
     */
    @Key("client_secret")
    public String clientSecret;

    /**
     * (REQUIRED) The verification code received from the authorization server.
     */
    @Key
    public String code;

    /** (REQUIRED) The redirection URI used in the initial request. */
    @Key("redirect_uri")
    public String redirectUri;

    /**
     * (OPTIONAL) The access token secret type as described by Section 5.3. If
     * omitted, the authorization server will issue a bearer token (an access
     * token without a matching secret) as described by Section 5.2.
     */
    @Key("secret_type")
    public String secretType;

    /**
     * (OPTIONAL) The response format requested by the client. Value MUST be one
     * of "json", "xml", or "form" or {@code null } for "json". Default value is
     * {@code "form"}.
     */
    @Key
    public String format = "form";

    /**
     * {@code true} for an HTTP GET request or the default {@code false} for an
     * HTTP POST request.
     */
    public boolean useHttpGetRequest;

    /** Encoded authorization server URL. */
    public final String encodedAuthorizationServerUrl;

    /**
     * HTTP parser used to parse the response from the server. Default value is
     * to construct {@link UrlEncodedParser}.
     */
    public HttpParser parser = new UrlEncodedParser();

    /**
     * @param encodedAuthorizationServerUrl encoded authorization server URL
     */
    public AccessTokenRequest(String encodedAuthorizationServerUrl) {
      this.encodedAuthorizationServerUrl = encodedAuthorizationServerUrl;
    }

    /**
     * Executes request for an access token, and returns the parsed access token
     * response.
     * 
     * @return parsed access token response
     * @throws HttpResponseException for an HTTP error response; use
     *         {@link AccessTokenErrorResponse} to parse
     *         {@link HttpResponseException#response}
     * @throws IOException I/O exception
     */
    public AccessTokenResponse execute() throws IOException {
      HttpTransport transport = new HttpTransport();
      transport.addParser(this.parser);
      boolean useHttpGetRequest = this.useHttpGetRequest;
      HttpRequest request;
      if (useHttpGetRequest) {
        request = transport.buildGetRequest();
      } else {
        request = transport.buildPostRequest();
      }
      GenericUrl url = new GenericUrl(this.encodedAuthorizationServerUrl);
      if (useHttpGetRequest) {
        url.putAll(DataUtil.mapOf(this));
      } else {
        UrlEncodedContent content = new UrlEncodedContent();
        content.setData(this);
        request.content = content;
      }
      request.url = url;
      return request.execute().parseAs(AccessTokenResponse.class);
    }
  }

  private WebServerFlow() {
  }
}
