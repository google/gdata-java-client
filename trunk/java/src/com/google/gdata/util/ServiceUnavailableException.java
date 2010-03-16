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

/**
 * Thrown when an exception occurs that is temporary; i.e., the user should
 * try again.  This includes failing backends, rate limiting, and so on.
 * Set the retryTime in seconds to tell the client when to retry.
 * <p>
 *
 * 
 */
public class ServiceUnavailableException extends ServiceException {

  private int retryTime = -1;

  public ServiceUnavailableException(String message) {
    super(message);
    initResponseCode();
  }

  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public ServiceUnavailableException(Throwable cause) {
    super(cause.getMessage(), cause);
    initResponseCode();
  }

  public ServiceUnavailableException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public ServiceUnavailableException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public ServiceUnavailableException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_UNAVAILABLE);
  }

  public int getRetryTime() {
    return retryTime;
  }

  public void setRetryTime(int retryTime) {
    this.retryTime = retryTime;
  }

}
