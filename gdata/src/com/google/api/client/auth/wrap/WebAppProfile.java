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

package com.google.api.client.http.auth.wrap;

import com.google.api.client.Entity;
import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriEntity;
import com.google.api.client.http.UrlEncodedFormHttpParser;
import com.google.api.client.http.UrlEncodedFormHttpSerializer;

import java.io.IOException;

/**
 * Implements the Web App Profile of OAuth WRAP authorization as specified in <a
 * href="http://tools.ietf.org/html/draft-hardt-oauth-01#section-6.2">OAuth Web
 * Resource Authorization Profiles</a>.
 */
public class WebAppProfile {

  public static class AuthorizationRequestUri extends UriEntity {

    /** The Client Identifier (required). */
    @Name("wrap_client_id")
    public String clientId;

    /**
     * The Callback URL (required). An absolute URL to which the Authorization
     * Server will redirect the User back after the User has approved the
     * authorization request. Authorization Servers MAY require that the
     * wrap_callback URL match the previously registered value for the Client
     * Identifier.
     */
    @Name("wrap_callback")
    public String callback;

    /**
     * An opaque value that Clients can use to maintain state associated with
     * this request (optional). If this value is present, the Authorization
     * Server MUST return it to the Client's Callback URL.
     */
    @Name("wrap_client_state")
    public String clientState;

    /**
     * The Authorization Server MAY define authorization scope values for the
     * Client to include (optional).
     */
    @Name("wrap_scope")
    public String scope;

    public AuthorizationRequestUri(String uri) {
      super(uri);
    }
  }

  public static class AuthorizationResponseUri extends UriEntity {

    /** The Verification Code (required). */
    @Name("wrap_verification_code")
    public String verificationCode;

    /** Required if the Client sent the value in the authorization request. */
    @Name("wrap_client_state")
    public String clientState;

    public AuthorizationResponseUri(String uri) {
      super(uri);
    }
  }

  public static class UserDeniedUri extends UriEntity {

    /** Value is user_denied (required). */
    @Name("wrap_error_reason")
    public static final String ERROR_REASON = "user_denied";

    /** Required if the Client sent the value in the authorization request. */
    @Name("wrap_client_state")
    public String clientState;

    public UserDeniedUri(String uri) {
      super(uri);
    }
  }

  public static class RequestAccessToken extends Entity {

    /** The Client Identifier (required). */
    @Name("wrap_client_id")
    public String clientId;

    /** The Client Secret (required). */
    @Name("wrap_client_secret")
    public String clientSecret;

    /** The Verification Code (required). */
    @Name("wrap_verification_code")
    public String verificationCode;

    /** The Callback URL used to obtain the Verification Code (required). */
    @Name("wrap_callback")
    public String callback;

    public static class Response extends RefreshToken.Response {

      /** The Refresh Token (required). */
      @Name("wrap_refresh_token")
      public String refreshToken;
    }

    public static class Error extends Entity {

      @Name("wrap_error_reason")
      public String errorReason;
    }

    /**
     * Requests an access token from the given authentication server.
     * 
     * @param accessTokenUrl authentication server's access token URL
     * @return successful HTTP response; may parse using {@code
     *         parseAs(RequestAccessToken.Response.class)}
     * @throws HttpResponseException if the authentication response has an error
     *         code; may parse {@link HttpResponseException#response} using
     *         {@code parseAs(RequestAccessToken.Error.class)}
     * @throws IOException some other kind of I/O exception
     */
    public HttpResponse execute(String accessTokenUrl) throws IOException {
      return executeAccessToken(accessTokenUrl, this);
    }
  }

  public static class RefreshToken extends Entity {

    /**
     * The Refresh Token that was received from the access token request
     * (required).
     */
    @Name("wrap_refresh_token")
    public String refreshToken;

    public static class Response extends Entity {

      /** The Access Token (required). */
      @Name("wrap_access_token")
      public String accessToken;

      /**
       * The lifetime of the Access Token in seconds. For example, 3600
       * represents one hour (optional).
       */
      @Name("wrap_access_token_expires_in")
      public String accessTokenExpiresIn;

      /** Sets the authorization header for the given HTTP transport. */
      public void setAuthorizationHeader(HttpTransport transport) {
        Wrap.setAuthorizationHeader(transport, this.accessToken);
      }
    }

    /**
     * Requests an access token from the given authentication server.
     * 
     * @param refreshTokenUrl authentication server's refresh token URL
     * @return successful HTTP response; may parsing using {@code
     *         parseAs(RequestAccessToken.Response.class)}
     * @throws HttpResponseException if the authentication response has an error
     *         code
     * @throws IOException some other kind of I/O exception
     */
    public HttpResponse execute(String refreshTokenUrl) throws IOException {
      return executeAccessToken(refreshTokenUrl, this);
    }
  }

  static HttpResponse executeAccessToken(String authenticationServerUrl,
      Entity entity) throws IOException {
    HttpTransport transport = new HttpTransport();
    UrlEncodedFormHttpParser parser = new UrlEncodedFormHttpParser();
    parser.disableContentLogging = true;
    transport.setParser(parser);
    HttpRequest request = transport.buildPostRequest(authenticationServerUrl);
    request.setContentNoLogging(new UrlEncodedFormHttpSerializer(entity));
    return request.execute();
  }
}
