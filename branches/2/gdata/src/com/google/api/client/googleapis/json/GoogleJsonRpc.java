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

package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonHttpContent;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * Google JSON-RPC support.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public final class GoogleJsonRpc {

  /** JSON-RPC request content. */
  public static class RpcRequest {

    @Key
    public String method;

    @Key
    public Object params;
  }

  /**
   * Returns a new Google JSON-RPC request for the given HTTP transport.
   * <p>
   * The {@link HttpRequest#content} will be {@link JsonHttpContent}, and the
   * {@link JsonHttpContent#data} will be {@link RpcRequest}.
   * </p>
   * <p>
   * <p>
   * Sample usage:
   * </p>
   * 
   * @param transport HTTP transport
   * @return new Google JSON-RPC request
   */
  public static HttpRequest buildHttpRequest(HttpTransport transport,
      RpcRequest... rpcRequests) {
    Preconditions.checkArgument(rpcRequests.length != 0,
        "RPC requests not specified");
    HttpRequest request = transport.buildPostRequest();
    request.setUrl("https://www.googleapis.com/rpc");
    JsonHttpContent content = new JsonHttpContent();
    if (rpcRequests.length == 1) {
      content.data = rpcRequests[0];
    } else {
      content.data = Arrays.asList(rpcRequests);
    }
    request.content = content;
    return request;
  }

  private GoogleJsonRpc() {
  }
}
