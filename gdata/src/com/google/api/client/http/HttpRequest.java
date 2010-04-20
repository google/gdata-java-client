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

import com.google.api.client.ArrayMap;
import com.google.api.client.Strings;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpRequest {

  /** Headers mapping from header name to header value. */
  public final ArrayMap<String, String> headers = ArrayMap.create();

  /**
   * Headers mapping from header name to header value for headers whose value
   * must not be logged (for example authentication-related headers).
   */
  public final ArrayMap<String, String> headersNoLogging = ArrayMap.create();

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
    this.headers.putAll(transport.defaultHeaders);
    this.headersNoLogging.putAll(transport.defaultHeadersNoLogging);
  }

  /**
   * Sets the header for the given name and value or remove the header if value
   * is {@code null}.
   */
  public void setDefaultHeader(String name, String value) {
    if (value == null) {
      this.headers.remove(name);
    } else {
      this.headers.put(name, value);
    }
  }

  /**
   * Sets the {@code "If-Match"} header to the given etag value or remove the
   * header if value is {@code null}.
   */
  public void setIfMatchHeader(String etag) {
    this.headers.put("If-Match", etag);
  }

  /**
   * Sets the {@code "If-None-Match"} header to the given etag value or remove
   * the header if value is {@code null}.
   */
  public void setIfNoneMatchHeader(String etag) {
    this.headers.put("If-None-Match", etag);
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
   * @see HttpResponse#isSuccessStatusCode()
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
    addHeaders(this.headers, logbuf, true);
    addHeaders(this.headersNoLogging, logbuf, false);
    AuthorizationHeaderProvider authorizationHeaderProvider =
        transport.authorizationHeaderProvider;
    if (authorizationHeaderProvider != null) {
      String authValue =
          authorizationHeaderProvider.getAuthorizationHeader(method, uri);
      addHeader("Authorization", authValue, logbuf, false);
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
    if (!response.isSuccessStatusCode()) {
      throw new HttpResponseException(response);
    }
    return response;
  }

  private void addHeaders(ArrayMap<String, String> headers,
      StringBuilder logbuf, boolean logValues) {
    LowLevelHttpRequest lowLevelHttpRequest = this.lowLevelHttpRequest;
    int size = headers.size();
    for (int i = 0; i < size; i++) {
      addHeader(headers.getKey(i), headers.getValue(i), logbuf, logValues);
    }
  }

  private void addHeader(String name, String value, StringBuilder logbuf,
      boolean logValue) {
    if (value != null) {
      if (logbuf != null) {
        if (logValue) {
          logbuf.append(name + ": " + value);
        } else {
          logbuf.append(name + ": <Not Logged>");
        }
        logbuf.append(Strings.LINE_SEPARATOR);
      }
      this.lowLevelHttpRequest.addHeader(name, value);
    }
  }
}
