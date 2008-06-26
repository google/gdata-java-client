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

import com.google.gdata.client.http.GoogleGDataRequest.GoogleCookie;

import java.util.Set;

/**
 * An interface to a cookie manager. Cookie handling can be disabled
 * or enabled.
 *
 * 
 */
public interface CookieManager {

  /**
   * Enables or disables cookie handling.
   */
  public void setCookiesEnabled(boolean cookiesEnabled);

  /**
   * Returns  {@code true} if the GoogleService is handling cookies.
   */
  public boolean cookiesEnabled();

  /**
   * Clears all cookies.
   */
  public void clearCookies();

  /**
   * Adds a new GoogleCookie instance to the cache.
   */
  public void addCookie(GoogleCookie cookie);

  /**
   * Returns the set of associated cookies returned by previous requests.
   */
  public Set<GoogleCookie> getCookies();

}
