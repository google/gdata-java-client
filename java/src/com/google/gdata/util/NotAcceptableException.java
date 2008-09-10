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

import java.net.HttpURLConnection;
import java.io.IOException;

/**
 * Thrown by a service provider when the resource identified by the request is
 * only capable of generating response entities which have content
 * characteristics not acceptable according to the accept headers sent in the
 * request.
 *
 * 
 */
public class NotAcceptableException extends ServiceException {

  public NotAcceptableException() {
    super("Not Acceptable");
    initResponseCode();
  }

  public NotAcceptableException(String message) {
    super(message);
    initResponseCode();
  }

  public NotAcceptableException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public NotAcceptableException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public NotAcceptableException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
  }
}
