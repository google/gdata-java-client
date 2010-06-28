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

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

/**
 * JSON-RPC 2.0 response object.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class JsonRpcResponse extends GenericData {

  /**
   * A String specifying the version of the JSON-RPC protocol. MUST be exactly
   * "2.0".
   */
  @Key
  public final String jsonrpc = "2.0";

  /**
   * (Required) It MUST be the same as the value of the id member in the Request
   * Object. If there was an error in detecting the id in the Request object
   * (e.g. Parse error/Invalid Request), it MUST be Null.
   */
  @Key
  public Object id;

  /** Success result, required on success, but must be excluded on error. */
  @Key
  public GenericJson result;

  /** Error result, required on error, but must be excluded on success. */
  @Key
  public JsonRpcErrorResponse error;
}
