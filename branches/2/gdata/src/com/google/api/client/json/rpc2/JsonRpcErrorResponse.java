/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.json.rpc2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

/**
 * JSON-RPC 2.0 error response object.
 * <p>
 * There is also an optional {@code "data"} key of a Primitive or Structured
 * value that contains additional information about the error. The value of this
 * member is defined by the Server (e.g. detailed error information, nested
 * errors etc.). Since its type is not known in advance, it is not declared
 * here.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class JsonRpcErrorResponse extends GenericData {

  /** A Number that indicates the error type that occurred. */
  @Key
  public int code;

  /**
   * A String providing a short description of the error. The message SHOULD be
   * limited to a concise single sentence.
   */
  @Key
  public String message;
}
