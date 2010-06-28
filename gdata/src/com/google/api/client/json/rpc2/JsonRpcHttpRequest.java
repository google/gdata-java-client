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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonHttpContent;
import com.google.api.client.json.JsonHttpParser;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON-RPC 2.0 HTTP request for a batch of RPC requests.
 * <p>
 * Note that only POST requests are supported. GET requests are not supported.
 * Also, there is no support for single RPC requests are that are not enclosed
 * in an array.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public final class JsonRpcHttpRequest {

  // TODO: ability to specify result class
  
  /** RPC server URL. */
  public GenericUrl rpcServerUrl;

  /** HTTP transport to use for executing HTTP requests. */
  public HttpTransport transport;

  /** JSON-RPC request objects to send on when executing. */
  public final List<JsonRpcRequest> requestObjects =
      new ArrayList<JsonRpcRequest>();

  /**
   * Executes over HTTP the JSON RPC requests objects specified in
   * {@link #requestObjects}, and parses the JSON-RPC HTTP response.
   * <p>
   * The {@link #requestObjects} field is cleared after the HTTP response is
   * received.
   * 
   * @return list of JSON RPC response objects
   * @throws IOException I/O exception
   */
  public List<JsonRpcResponse> execute() throws IOException {
    HttpResponse response = executeNoParse();
    ArrayList<JsonRpcRequest> result = new ArrayList<JsonRpcRequest>();
    JsonParser parser = JsonHttpParser.parserForResponse(response);
    return (List<JsonRpcResponse>) Json.parseArrayAndClose(parser, List.class,
        JsonRpcResponse.class, null);
  }

  /**
   * Executes over HTTP the JSON-RPC requests objects specified in
   * {@link #requestObjects}.
   * <p>
   * The {@link #requestObjects} field is cleared.
   * 
   * @return HTTP response
   * @throws IOException I/O exception
   */
  public HttpResponse executeNoParse() throws IOException {
    transport.addParser(new JsonHttpParser());
    List<JsonRpcRequest> requestObjects = this.requestObjects;
    HttpRequest httpRequest = transport.buildPostRequest();
    httpRequest.url = rpcServerUrl;
    JsonHttpContent content = new JsonHttpContent();
    content.data = requestObjects;
    httpRequest.content = content;
    try {
      return httpRequest.execute();
    } finally {
      this.requestObjects.clear();
    }
  }
}
