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

import com.google.gdata.client.CoreErrorDomain;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Exception that indicates a user is requesting too frequently.
 * For storage-limit errors, see {@link QuotaExceededException}.
 *
 * 
 */
public class RateLimitExceededException extends ServiceException {

  public RateLimitExceededException() {
    this(CoreErrorDomain.ERR.rateLimitExceeded);
  }

  public RateLimitExceededException(String message) {
    this(CoreErrorDomain.ERR.rateLimitExceeded.withExtendedHelp(message));
  }

  public RateLimitExceededException(String message, Throwable cause) {
    this(CoreErrorDomain.ERR.rateLimitExceeded.withExtendedHelp(message),
        cause);
  }

  public RateLimitExceededException(HttpURLConnection httpConn) 
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public RateLimitExceededException(Throwable cause) {
    this(CoreErrorDomain.ERR.rateLimitExceeded, cause);
  }

  public RateLimitExceededException(ErrorContent errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public RateLimitExceededException(ErrorContent errorCode, Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_FORBIDDEN);
  }
}
