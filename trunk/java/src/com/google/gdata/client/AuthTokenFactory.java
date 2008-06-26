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


package com.google.gdata.client;

import com.google.gdata.client.GoogleService.SessionExpiredException;
import com.google.gdata.util.AuthenticationException;

/**
 * An interface to a factory that creates authentication tokens.
 * 
 * 
 */
public interface AuthTokenFactory {

  /**
   * A marker interface for authentication tokens. Auth tokens are
   * protocol-specific (e.g. HTTP auth tokens) so there are no common
   * methods.
   */
  public static interface AuthToken {
  }
  
  /**
   * A listener interface that's used to inform interested
   * parties that an authentication token has changed. Listeners
   * can for example clear out user data (e.g. cookies).
   */
  public interface TokenListener {
    public void tokenChanged(AuthToken newToken);
  }

  /**
   * Get an authentication token.
   * 
   * @return Authentication token.
   */
  public AuthToken getAuthToken();

  /**
   * Handles a session expired exception.
   */
  public void handleSessionExpiredException(
      SessionExpiredException sessionExpired)
      throws SessionExpiredException, AuthenticationException;
}
