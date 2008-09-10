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
 * The NotImplementedException should be thrown by a service provider
 * when an attempt is made to send a request that is not supported by the
 * target service.
 *
 * 
 */
public class NotImplementedException extends ServiceException {

  public NotImplementedException(String message) {
    super(message);
    initResponseCode();
  }

  public NotImplementedException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public NotImplementedException(Throwable cause) {
    super(cause);
    initResponseCode();
  }

  public NotImplementedException(HttpURLConnection httpConn)
      throws IOException{
    super(httpConn);
    initResponseCode();
  }

  public NotImplementedException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public NotImplementedException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_NOT_IMPLEMENTED);
  }
}
