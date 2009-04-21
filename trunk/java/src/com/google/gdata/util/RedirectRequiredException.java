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
import java.util.List;

/**
 * Exception thrown when a redirect is required to satisfy a request.
 *
 * 
 */
public class RedirectRequiredException extends ServiceException {

  /**
   * Name of HTTP header containing redirect target location.
   */
  private static final String LOCATION = "Location";

  /**
   * Initialize the redirect required exception.
   *
   * @param sc the status code of the redirect - one of
   * {@link java.net.HttpURLConnection#HTTP_MOVED_PERM} or
   * {@link java.net.HttpURLConnection#HTTP_MOVED_TEMP}.
   * @param location the redirect location
   */
  public RedirectRequiredException(int sc, String location) {
    super("Redirect Required");
    setHttpErrorCodeOverride(sc);
    getHttpHeaders().put(LOCATION, Collections.singletonList(location));
  }

  public RedirectRequiredException(HttpURLConnection httpConn)
      throws IOException{
    super(httpConn);
    setHttpErrorCodeOverride(httpConn.getResponseCode());
    // Location header is saved in super constructor.
  }

  public String getRedirectLocation() {
    List<String> location = getHttpHeader(LOCATION);
    if (location != null && location.size() > 0) {
      return location.get(0);
    }
    return null;
  }
}
