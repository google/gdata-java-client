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


package com.google.gdata.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The AuthenticationException class is used to represent a GData service
 * failure due to authentication.
 *
 * 
 */
public class AuthenticationException extends ServiceException {

  private String authHeader;
  private String scheme;
  private Map<String,String> parameters = new HashMap<String,String>();

  /**
   * Constructs a new AuthenticationException instance based upon
   * the contents of a WWW-Authenticate header as described by
   * RFC2617.
   */
  public AuthenticationException(String message, String authHeader) {
    super(message);
    initFromAuthHeader(authHeader);
  }


  /**
   * Creates a new AuthenticationException without any authentication
   * header information. The WWW-Authenticate header will have
   * to be set separately to build a valid HTTP 401 error response.
   */
  public AuthenticationException(String message) {
    super(message);
  }


  /**
   * Constructs a new AuthenticationException using header and error
   * stream information from an HTTP connection.
   */
  public AuthenticationException(HttpURLConnection httpConn) 
      throws IOException {
    super(httpConn);
    initFromAuthHeader(httpConn.getHeaderField("WWW-Authenticate"));
  }

  // Used to extract scheme info from the WWW-Authenticate header.
  private static final Pattern SCHEME_PATTERN =
    Pattern.compile("([^\\s$]*)\\s*(.*)?");

  
  private static String TOKEN = 
    "[\\p{ASCII}&&[^\\p{Cntrl} \t;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+";

  // Used to extract auth params from the WWW-Authenticate header.
  private static final Pattern PARAM_PATTERN =
    Pattern.compile(
        "(" + TOKEN + ")" +       // param name  (G1)
        "\\s*=\\s*" +
        "(?:" +
          "\"([^\"]*)\"" +        // value as quoted string (G2)
          "|" +
          "(" + TOKEN + ")?" +    // value as token (G3)
        ")");

  /**
   * Initializes internal state from the contents of a WWW-Authenticate
   * header.
   */
  private void initFromAuthHeader(String authHeader) {
    this.authHeader = authHeader;
    if (authHeader == null)
      throw new NullPointerException("No authentication header information");

    Matcher authMatcher = SCHEME_PATTERN.matcher(authHeader);
    if (!authMatcher.matches()) {
      throw new IllegalStateException("Unable to parse auth header: " +
                                      authHeader);
    }
    scheme = authMatcher.group(1);    
    if (authMatcher.groupCount() > 1) {
      Matcher paramMatcher = PARAM_PATTERN.matcher(authMatcher.group(2));
      while (paramMatcher.find()) {
        String value = paramMatcher.group(2);
        if (value == null) {
          value = paramMatcher.group(3);
        }
        parameters.put(paramMatcher.group(1), value);
      }
    }
  }

  public String getScheme() {
    return scheme;
  }

  public String getRealm() {
    return parameters.get("realm");
  }

  public Map<String,String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }


  public String getAuthHeader() {
    return authHeader;
  }
}
