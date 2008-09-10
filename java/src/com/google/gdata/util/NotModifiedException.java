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
 * The NotModifiedException should be thrown by a service provider
 * when an attempt is made to access content using a date-conditional
 * GET and the resource has not been modified since the provided date.
 *
 * 
 */
public class NotModifiedException extends ServiceException {

  public NotModifiedException() {
    super("Not Modified");
    initResponseCode();
  }

  public NotModifiedException(String message) {
    super(message);
    initResponseCode();
  }

  public NotModifiedException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public NotModifiedException(HttpURLConnection httpConn)
      throws IOException{
    super(httpConn);
    initResponseCode();
  }

  public NotModifiedException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public NotModifiedException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_NOT_MODIFIED);
  }
}
