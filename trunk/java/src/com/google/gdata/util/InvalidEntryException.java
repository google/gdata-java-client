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
 * Thrown when the format of a ServiceRequest is invalid.  Possible causes
 * are XML that is not well formed, or the presence of extension elements
 * that are not expected for the target feed.
 * <p>
 *
 * 
 */
public class InvalidEntryException extends ServiceException {

  public InvalidEntryException(String message) {
    super(message);
    initResponseCode();
  }

  public InvalidEntryException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public InvalidEntryException(Throwable cause) {
    super(cause.getMessage(), cause);
    initResponseCode();
  }

  public InvalidEntryException(HttpURLConnection httpConn) throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public InvalidEntryException(ErrorContent errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public InvalidEntryException(ErrorContent errorCode, Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_BAD_REQUEST);
  }
}
