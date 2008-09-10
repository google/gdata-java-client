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
 * The ResourceNotFoundException should be thrown by a service provider
 * when an attempt is made to perform an operation upon a resource (feed
 * or entry) that cannot be found.
 *
 * 
 */
public class ResourceNotFoundException extends ServiceException {

  public ResourceNotFoundException(String message) {
    super(message);
    initResponseCode();
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public ResourceNotFoundException(Throwable cause) {
    super(cause);
    initResponseCode();
  }

  public ResourceNotFoundException(HttpURLConnection httpConn)
      throws IOException{
    super(httpConn);
    initResponseCode();
  }

  public ResourceNotFoundException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public ResourceNotFoundException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_NOT_FOUND);
  }
}
