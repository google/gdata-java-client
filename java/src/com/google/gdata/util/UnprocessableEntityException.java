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

import com.google.gdata.client.GDataProtocol;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * The UnprocessableEntityException is thrown by a service provider when it
 * receives a request that it understands but where processing the request will
 * leave resources in an inconsistent state.   For example, a PATCH request
 * that removes required fields could not be processed without making the target
 * resource invalid.
 *
 * 
 */
public class UnprocessableEntityException extends ServiceException {

  /**
   * @param message
   */
  public UnprocessableEntityException(String message) {
    super(message);
    initResponseCode();
  }

  /**
   * @param message
   * @param cause
   */
  public UnprocessableEntityException(String message, Throwable cause) {
    super(message, cause);
    initResponseCode();
  }

  /**
   * @param cause
   */
  public UnprocessableEntityException(Throwable cause) {
    super(cause);
    initResponseCode();
  }

  /**
   * @param httpConn
   * @throws IOException
   */
  public UnprocessableEntityException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
    initResponseCode();
  }

  /**
   * @param errorCode
   */
  public UnprocessableEntityException(ErrorContent errorCode) {
    super(errorCode);
    initResponseCode();
  }

  /**
   * @param errorCode
   * @param cause
   */
  public UnprocessableEntityException(ErrorContent errorCode, Throwable cause) {
    super(errorCode, cause);
    initResponseCode();
  }

  private void initResponseCode() {
    setHttpErrorCodeOverride(GDataProtocol.Error.UNPROCESSABLE_ENTITY);
  }
}
