/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

/**
 * One error found in the body of a 
 * {@link com.google.gdata.util.ServiceException}
 * or in the content of a
 * {@link com.google.gdata.data.batch.BatchStatus}.
 * 
 * @see com.google.api.gbase.client.ServiceErrors
 */
public class ServiceError {
  private final String type;
  private final String field;
  private final String reason;

  /** The {@value #DATA_TYPE} error type. */
  public static final String DATA_TYPE = "data";

  /** The {@value #REQUEST_TYPE} error type. */
  public static final String REQUEST_TYPE = "request";

  /**
   * Creates a new error.
   *
   * @param type error type, may be null, {@link #DATA_TYPE}
   *   or {@link #REQUEST_TYPE}
   * @param field error field, may be null
   * @param reason error message
   */
  public ServiceError(String type, String field, String reason) {
    this.type = type;
    this.field = field;
    this.reason = reason;
  }

  /**
   * Creates a new error with just an error message.
   *
   * @param reason error message
   */
  public ServiceError(String reason) {
    this(null, null, reason);
  }

  /**
   * Returns a convenient string representation.
   *
   * @return a string representation
   */
  @Override
  public String toString() {
    if (field == null) {
      return reason;
    } else {
      return field + ": " + reason;
    }
  }

  /**
   * Gets the error type.
   *
   * @return error type or null
   */
  public String getType() {
    return type;
  }

  /**
   * Gets the error field.
   *
   * @return error field or null
   */
  public String getField() {
    return field;
  }

  /**
   * Gets the error message (plain text).
   *
   * @return error message
   */
  public String getReason() {
    return reason;
  }
}