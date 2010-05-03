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

package com.google.api.client.http.auth.googleapis.clientlogin;

import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedFormHttpSerializer;
import com.google.api.client.http.auth.googleapis.AuthKeyValueParser;
import com.google.api.client.http.googleapis.GoogleTransport;

import java.io.IOException;

/**
 * Client login authentication method as described in <a
 * href="http://code.google.com/apis/accounts/docs/AuthForInstalledApps.html"
 * >ClientLogin for Installed Applications</a>.
 */
public final class ClientLogin {

  @Name("source")
  public String applicationName;

  @Name("service")
  public String authTokenType;

  @Name("Email")
  public String username;

  @Name("Passwd")
  public String password;

  @Name("logintoken")
  public String captchaToken;

  @Name("logincaptcha")
  public String captchaAnswer;

  /** Entity to parse a success response. */
  public static final class Response {

    @Name("Auth")
    public String auth;

    public String getAuthorizationHeaderValue() {
      return GoogleTransport.getGoogleLoginAuthorizationHeaderValue(this.auth);
    }

    /**
     * Sets the authorization header for the given Google transport using the
     * authentication token.
     */
    public void setAuthorizationHeader(GoogleTransport googleTransport) {
      googleTransport.setGoogleLoginAuthorizationHeader(this.auth);
    }
  }

  /** Entity to parse an error response. */
  public static final class ErrorInfo {

    @Name("Error")
    public String error;

    @Name("Url")
    public String url;

    @Name("CaptchaToken")
    public String captchaToken;

    @Name("CaptchaUrl")
    public String captchaUrl;
  }

  /**
   * Authenticates based on the provided field values.
   * 
   * @throws HttpResponseException if the authentication response has an error
   *         code, such as for a CAPTCHA challenge. Call {@code
   *         exception.response.parseAs(ClientLoginAuthenticator.ErrorInfo.class)
   *         * } to parse the response.
   * @throws IOException some other kind of I/O exception
   */
  public Response authenticate() throws HttpResponseException, IOException {
    HttpTransport transport = new HttpTransport();
    AuthKeyValueParser.setAsParserOf(transport);
    HttpRequest request =
        transport
            .buildPostRequest("https://www.google.com/accounts/ClientLogin");
    request.setContentNoLogging(new UrlEncodedFormHttpSerializer(this));
    return request.execute().parseAs(Response.class);
  }
}
