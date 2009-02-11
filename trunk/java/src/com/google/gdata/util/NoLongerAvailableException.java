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
 * Thrown by a service provider when the resource identified by the request is
 * no longer available at the server and no forwarding address is known.
 * This condition is expected to be considered permanent.
 * Clients with link editing capabilities SHOULD delete references to the
 * Request-URI after user approval.
 * <p>
 *
 * 
 */
public class NoLongerAvailableException extends ServiceException {

  public NoLongerAvailableException() {
    super("No longer available");
    initResponseCode();
  }

  public NoLongerAvailableException(String message) {
    super(message);
    initResponseCode();
  }

  public NoLongerAvailableException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public NoLongerAvailableException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public NoLongerAvailableException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public NoLongerAvailableException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }


  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_GONE);
  }
}
