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
 * The EntityTooLargeException should be thrown by a service provider
 * when its refusing to process a request because request entity is larger
 * than service provider is willing to process.
 *
 * 
 */
public class EntityTooLargeException extends ServiceException {

  public EntityTooLargeException(String message) {
    super(message);
    initResponseCode();
  }

  public EntityTooLargeException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  public EntityTooLargeException(Throwable cause) {
    super(cause);
    initResponseCode();
  }

  public EntityTooLargeException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  public EntityTooLargeException(ErrorContent errorCode) {
    super(errorCode);
    initResponseCode();
  }

  public EntityTooLargeException(ErrorContent errorCode, Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }


  private void initResponseCode() {
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
  }
}
