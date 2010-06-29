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
import com.google.api.client.json.CustomizeJsonParser;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonHttpContent;
import com.google.api.client.json.JsonHttpParser;
import com.google.api.client.util.Base64;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * JSON-RPC 2.0 HTTP transport for RPC requests, including both singleton and
 * batched requests.
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public final class JsonRpcHttpTransport {

  /** RPC server URL. */
  public GenericUrl rpcServerUrl;

  /** HTTP transport to use for executing HTTP requests. */
  public HttpTransport transport;

  /**
   * Executes over HTTP POST the JSON-RPC requests objects specified in the
   * given JSON-RPC request object.
   * <p>
   * You may use {@link JsonHttpParser#parserForResponse(HttpResponse)
   * JsonHttpParser.parserForResponse}({@link #execute(JsonRpcRequest) execute}
   * (request)) to get the {@link JsonParser}, and
   * {@link Json#parseAndClose(JsonParser, Class, CustomizeJsonParser)} .
   * </p>
   * 
   * @param request JSON-RPC request object
   * @return HTTP response
   * @throws IOException I/O exception
   */
  public HttpResponse execute(JsonRpcRequest request) throws IOException {
    return internalExecute(request);
  }

  /**
   * Executes over HTTP POST the JSON-RPC requests objects specified in the
   * given JSON-RPC request objects.
   * <p>
   * Note that the request will always use batching -- i.e. JSON array of
   * requests -- even if there is only one request. You may use
   * {@link JsonHttpParser#parserForResponse(HttpResponse)
   * JsonHttpParser.parserForResponse}({@link #execute(List) execute}(requests))
   * to get the {@link JsonParser}, and
   * {@link Json#parseArrayAndClose(JsonParser, Collection, Class, CustomizeJsonParser)}
   * .
   * </p>
   * 
   * @param requests JSON-RPC request objects
   * @return HTTP response
   * @throws IOException I/O exception
   */
  public HttpResponse execute(List<JsonRpcRequest> requests) throws IOException {
    return internalExecute(requests);
  }

  /**
   * Executes over HTTP GET the JSON-RPC requests objects specified in the given
   * JSON-RPC request object.
   * <p>
   * You may use {@link JsonHttpParser#parserForResponse(HttpResponse)
   * JsonHttpParser.parserForResponse}( {@link #executeUsingGet(JsonRpcRequest)
   * executeUsingGet} (request)) to get the {@link JsonParser}, and
   * {@link Json#parseAndClose(JsonParser, Class, CustomizeJsonParser)} .
   * </p>
   * 
   * @param request JSON-RPC request object
   * @return HTTP response
   * @throws IOException I/O exception
   */
  public HttpResponse executeUsingGet(JsonRpcRequest request)
      throws IOException {
    HttpTransport transport = this.transport;
    HttpRequest httpRequest = transport.buildGetRequest();
    GenericUrl url = httpRequest.url = this.rpcServerUrl.clone();
    url.set("method", request.method);
    url.set("id", request.id);
    // base64 encode the params
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    JsonGenerator generator =
        Json.JSON_FACTORY.createJsonGenerator(byteStream, JsonEncoding.UTF8);
    try {
      Json.serialize(generator, request.params);
    } finally {
      generator.close();
    }
    url.set("params", new String(Base64.encode(byteStream.toByteArray()),
        "UTF-8"));
    return httpRequest.execute();
  }

  private HttpResponse internalExecute(Object data) throws IOException {
    HttpTransport transport = this.transport;
    HttpRequest httpRequest = transport.buildPostRequest();
    httpRequest.url = this.rpcServerUrl;
    JsonHttpContent content = new JsonHttpContent();
    content.data = data;
    httpRequest.content = content;
    return httpRequest.execute();
  }
}
