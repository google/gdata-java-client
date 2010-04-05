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

  HttpRequest(HttpTransport transport, LowLevelHttpRequest lowLevelHttpRequest) {
    this.transport = transport;
    this.lowLevelHttpRequest = lowLevelHttpRequest;
    this.headers.putAll(transport.defaultHeaders);
    this.headersNoLogging.putAll(transport.defaultHeadersNoLogging);
  }

  /** Sets the {@code "If-Match"} header to the given etag value. */
  public void setIfMatchHeader(String etag) {
    this.headers.put("If-Match", etag);
  }

  /** Sets the {@code "If-None-Match"} header to the given etag value. */
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
    // log method and uri
    if (loggable) {
      logger.config(lowLevelHttpRequest.getRequestLine());
    }
    // headers
    addHeaders(this.headers, true);
    addHeaders(this.headersNoLogging, false);
    // content
    HttpSerializer content = this.content;
    if (content != null) {
      if (loggable) {
        logger.config("Content-Type: " + content.getContentType());
        String contentEncoding = content.getContentEncoding();
        if (contentEncoding != null) {
          logger.config("Content-Encoding: " + contentEncoding);
        }
        long contentLength = content.getContentLength();
        if (contentLength >= 0) {
          logger.config("Content-Length: " + contentLength);
        }
      }
      lowLevelHttpRequest.setContent(content);
    }
    // execute
    HttpResponse response =
        new HttpResponse(transport, lowLevelHttpRequest.execute());
    if (!response.isSuccessStatusCode()) {
      throw new HttpResponseException(response);
    }
    return response;
  }

  private void addHeaders(ArrayMap<String, String> headers, boolean logValues) {
    Logger logger = HttpTransport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    LowLevelHttpRequest lowLevelHttpRequest = this.lowLevelHttpRequest;
    int size = headers.size();
    for (int i = 0; i < size; i++) {
      String name = headers.getKey(i);
      String value = headers.getValue(i);
      if (loggable) {
        if (logValues) {
          logger.config(name + ": " + value);
        } else {
          logger.config(name + ": <Not Logged>");
        }
      }
      lowLevelHttpRequest.addHeader(name, value);
    }
  }
}
