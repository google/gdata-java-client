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

package com.google.api.client.http;

import com.google.api.client.auth.Authorizer;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** HTTP response. */
public final class HttpRequest {

  /** HTTP headers. */
  public final HttpHeaders headers;

  private final LowLevelHttpRequest lowLevelHttpRequest;
  private HttpSerializer content;
  private final HttpTransport transport;

  public final String method;
  public final String uri;

  HttpRequest(HttpTransport transport, String method, String uri,
      LowLevelHttpRequest lowLevelHttpRequest) {
    this.transport = transport;
    this.method = method;
    this.uri = uri;
    this.lowLevelHttpRequest = lowLevelHttpRequest;
    this.headers = transport.defaultHeaders.clone();
  }

  public void setContent(HttpSerializer serializer) {
    setContentNoLogging(new LogHttpSerializer(serializer));
  }

  public void setContentNoLogging(HttpSerializer serializer) {
    this.content = new GZipHttpSerializer(serializer);
  }

  /**
   * Execute the HTTP request and returns the HTTP response.
   * <p>
   * Note that regardless of the returned status code, the HTTP response content
   * has not been parsed yet, and must be parsed by the calling code.
   * 
   * @return HTTP response for an HTTP success code
   * @throws HttpResponseException for an HTTP error code
   * @see HttpResponse#isSuccessStatusCode
   */
  public HttpResponse execute() throws IOException {
    LowLevelHttpRequest lowLevelHttpRequest = this.lowLevelHttpRequest;
    HttpTransport transport = this.transport;
    Logger logger = HttpTransport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    StringBuilder logbuf = null;
    // log method and uri
    String method = this.method;
    String uri = this.uri;
    if (loggable) {
      logbuf = new StringBuilder();
      logbuf.append(method).append(' ').append(uri).append(
          Strings.LINE_SEPARATOR);
    }
    // headers
    HttpHeaders headers = this.headers;
    Authorizer authorizer = headers.authorizer;
    if (authorizer != null) {
      String authValue = authorizer.computeHeader(method, uri);
      headers.setAuthorization(authValue);
    }
    ArrayMap<String, String> values = headers.values;
    ArrayList<String> privateNames = headers.privateNames;
    int size = values.size();
    for (int i = 0; i < size; i++) {
      String name = values.getKey(i);
      String value = values.getValue(i);
      if (logbuf != null) {
        if (privateNames.contains(name)) {
          logbuf.append(name + ": <Not Logged>");
        } else {
          logbuf.append(name + ": " + value);
        }
        logbuf.append(Strings.LINE_SEPARATOR);
      }
      lowLevelHttpRequest.addHeader(name, value);
    }
    // content
    HttpSerializer content = this.content;
    if (content != null) {
      if (loggable) {
        logbuf.append("Content-Type: " + content.getContentType()).append(
            Strings.LINE_SEPARATOR);
        String contentEncoding = content.getContentEncoding();
        if (contentEncoding != null) {
          logbuf.append("Content-Encoding: " + contentEncoding).append(
              Strings.LINE_SEPARATOR);
        }
        long contentLength = content.getContentLength();
        if (contentLength >= 0) {
          logbuf.append("Content-Length: " + contentLength).append(
              Strings.LINE_SEPARATOR);
        }
      }
      lowLevelHttpRequest.setContent(content);
    }
    if (loggable) {
      logger.config(logbuf.toString());
    }
    // execute
    HttpResponse response =
        new HttpResponse(transport, lowLevelHttpRequest.execute());
    if (!response.isSuccessStatusCode) {
      throw new HttpResponseException(response);
    }
    return response;
  }
}
