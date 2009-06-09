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


package com.google.gdata.client.authn.oauth;

/**
 * Defines the parameters necessary for making OAuth requests against Google.
 * Currently the only parameter is "scope", which indicates the resources the
 * user can access.
 *
 * 
 */
public class GoogleOAuthParameters extends OAuthParameters {

  public static final String SCOPE_KEY = "scope";

  /**
   * Return the request scope.  The scope is a URI defined by each Google
   * service that indicates which resources the user has permissions to access.
   * It is used when retrieving the unauthorized request token.  Multiple scopes
   * are separated with a space.  This parameter is required for OAuth requests
   * against Google.
   */
  public String getScope() {
    return get(SCOPE_KEY, baseParameters);
  }

  /**
   * Sets the request scope.  See {@link #getScope()} to learn more about this
   * parameter.
   */
  public void setScope(String scope) {
    put(SCOPE_KEY, scope, baseParameters);
  }

  /**
   * Checks to see if the scope exists.  See {@link #getScope()} to learn more
   * about this parameter.
   *
   * @return true if the scope exists, false otherwise
   */
  public boolean checkScopeExists() {
    return checkExists(SCOPE_KEY, baseParameters);
  }

  /**
   * Checks to see if the scope exists.  Throws an exception if it does not.
   * See {@link #getScope()} to learn more about this parameter.
   *
   * @throws OAuthException if the scope does not exist
   */
  public void assertScopeExists() throws OAuthException {
    assertExists(SCOPE_KEY, baseParameters);
  }

  /**
   * Since the scope parameter may be different for each OAuth request, it is
   * cleared between requests, and must be manually set before each request.
   */
  @Override
  public void reset() {
    super.reset();
    remove(SCOPE_KEY, baseParameters);
  }
}
