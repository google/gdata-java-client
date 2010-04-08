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

package com.google.api.client.http.auth.googleapis.authsub;

import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UriEntity;
import com.google.api.client.http.auth.googleapis.AuthKeyValueParser;
import com.google.api.client.http.googleapis.GoogleTransport;

import java.io.IOException;

/**
 * AuthSub authentication as described in <a
 * href="http://code.google.com/apis/accounts/docs/AuthSub.html">AuthSub for Web
 * Applications</a>.
 */
public class AuthSub {

  // TODO: support for signing requests!

  /**
   * URI entity that builds an AuthSub request URI to retrieve a single-use
   * token. See <a href=
   * "http://code.google.com/apis/accounts/docs/AuthSub.html#AuthSubRequest"
   * >documentation</a>.
   */
  public static final class RequestUri extends UriEntity {

    /**
     * (required) URL the user should be redirected to after a successful login.
     * This value should be a page on the web application site, and can include
     * query parameters.
     */
    public String next;

    /**
     * (required) URL identifying the service(s) to be accessed; see
     * documentation for the service for the correct value(s). The resulting
     * token enables access to the specified service(s) only. To specify more
     * than one scope, list each one separated with a space (encodes as "%20").
     */
    public String scope;

    /**
     * (optional) Forces a mobile version of the approval page. The only
     * accepted value is "mobile".
     */
    public String btmpl;

    /**
     * (optional) String value identifying a particular Google Apps (hosted)
     * domain account to be accessed (for example, 'mycollege.edu'). Use
     * "default" to specify a regular Google account ('username@gmail.com').
     */
    public String hd;

    /**
     * (optional) An ISO 639 country code identifying what language the approval
     * page should be translated in (for example, 'hl=en' for English). The
     * default is the user's selected language.
     */
    public String hl;

    /**
     * (optional) Boolean flag indicating whether the authorization transaction
     * should issue a secure token (1) or a non-secure token (0). Secure tokens
     * are available to registered applications only.
     */
    public int secure;

    /**
     * (optional) Boolean flag indicating whether the one-time-use token may be
     * exchanged for a session token (1) or not (0).
     */
    public int session;

    public RequestUri() {
      super("https://www.google.com/accounts/AuthSubRequest");
    }
  }

  /**
   * URI entity with a token parameter that can be used to extract the AuthSub
   * single-use token from the AuthSubRequest response.
   */
  public static class TokenUriEntity extends UriEntity {

    public String token;

    public TokenUriEntity(String uri) {
      super(uri);
    }
  }

  /** Entity to parse a success response for an AuthSubSessionToken request. */
  public static final class SessionTokenResponse {

    @Name("Token")
    public String token;

    /**
     * Sets the authorization header for the given Google transport using the
     * session token.
     */
    public void setAuthorizationHeader(GoogleTransport googleTransport) {
      AuthSub.setAuthorizationHeader(googleTransport, this.token);
    }
  }

  /** Entity to parse a success response for an AuthSubTokenInfo request. */
  public static final class TokenInfoResponse {

    @Name("Secure")
    public boolean secure;

    @Name("Target")
    public String target;

    @Name("Scope")
    public String scope;
  }

  /**
   * Exchanges the single-use token for a session token as described in <a href=
   * "http://code.google.com/apis/accounts/docs/AuthSub.html#AuthSubSessionToken"
   * >AuthSubSessionToken</a>.
   * 
   * @throws HttpResponseException if the authentication response has an error
   *         code
   * @throws IOException some other kind of I/O exception
   */
  public static SessionTokenResponse exchangeForSessionToken(
      String singleUseToken) throws IOException {
    HttpTransport transport = new HttpTransport();
    AuthKeyValueParser.setAsParserOf(transport);
    setAuthorizationHeader(transport, singleUseToken);
    HttpRequest request =
        transport
            .buildGetRequest("https://www.google.com/accounts/AuthSubSessionToken");
    return request.execute().parseAs(SessionTokenResponse.class);
  }

  /**
   * Revokes the given session token.
   * <p>
   * See <a href=
   * "http://code.google.com/apis/accounts/docs/AuthSub.html#AuthSubRevokeToken"
   * >AuthSubRevokeToken</a> for protocol details.
   * 
   * @throws HttpResponseException if the authentication response has an error
   *         code
   * @throws IOException some other kind of I/O exception
   */
  public static void revokeToken(String sessionToken) throws IOException {
    HttpTransport transport = new HttpTransport();
    AuthKeyValueParser.setAsParserOf(transport);
    setAuthorizationHeader(transport, sessionToken);
    HttpRequest request =
        transport
            .buildGetRequest("https://www.google.com/accounts/AuthSubRevokeToken");
    request.execute().ignore();
  }

  /**
   * Retries the token information as described in <a href=
   * "http://code.google.com/apis/accounts/docs/AuthSub.html#AuthSubTokenInfo"
   * >AuthSubTokenInfo</a>.
   * 
   * @throws HttpResponseException if the authentication response has an error
   *         code
   * @throws IOException some other kind of I/O exception
   */
  public static TokenInfoResponse requestTokenInfo(String token)
      throws IOException {
    HttpTransport transport = new HttpTransport();
    AuthKeyValueParser.setAsParserOf(transport);
    setAuthorizationHeader(transport, token);
    HttpRequest request =
        transport
            .buildGetRequest("https://www.google.com/accounts/AuthSubRevokeToken");
    return request.execute().parseAs(TokenInfoResponse.class);
  }

  /**
   * Returns {@code AuthSub} authorization header value based on the given
   * authentication token.
   */
  public static String getAuthorizationHeaderValue(String token) {
    return "AuthSub token=" + token;
  }

  /**
   * Sets the authorization header for the given HTTP transport based on the
   * given token.
   */
  public static void setAuthorizationHeader(HttpTransport httpTransport,
      String token) {
    httpTransport.setAuthorizationHeader(getAuthorizationHeaderValue(token));
  }

  private AuthSub() {
  }
}
