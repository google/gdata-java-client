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

import com.google.api.client.util.Strings;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HTTP request.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class HttpRequest {

  /** HTTP request headers. */
  public HttpHeaders headers;

  /**
   * Whether to disable request content logging during {@link #execute()}, for
   * example if content has sensitive data such as an authentication
   * information. Defaults to {@code false}.
   */
  public boolean disableContentLogging;

  /** HTTP request content or {@code null} for none. */
  public HttpContent content;

  /** HTTP transport. */
  private final HttpTransport transport;

  /** HTTP request method. */
  public final String method;

  /** HTTP request URL. */
  public GenericUrl url;

  /**
   * @param transport HTTP transport
   * @param method HTTP request method
   */
  HttpRequest(HttpTransport transport, String method) {
    this.transport = transport;
    this.headers = transport.defaultHeaders.clone();
    this.method = method;
  }

  /** Sets the {@link #url} based on the given encoded URL string. */
  public void setUrl(String encodedUrl) {
    this.url = new GenericUrl(encodedUrl);
  }

  /**
   * Execute the HTTP request and returns the HTTP response.
   * <p>
   * Note that regardless of the returned status code, the HTTP response content
   * has not been parsed yet, and must be parsed by the calling code.
   * <p>
   * Almost all details of the request and response are logged if
   * {@link Level#CONFIG} is loggable. The only exception is the value of the
   * {@code Authorization} header which is only logged if {@link Level#ALL} is
   * loggable.
   * 
   * @return HTTP response for an HTTP success code
   * @throws HttpResponseException for an HTTP error code
   * @see HttpResponse#isSuccessStatusCode
   */
  public HttpResponse execute() throws IOException {
    HttpTransport transport = this.transport;
    // first run the execute intercepters
    for (HttpExecuteIntercepter intercepter : transport.intercepters) {
      intercepter.intercept(this);
    }
    // build low-level HTTP request
    LowLevelHttpTransport lowLevelHttpTransport =
        HttpTransport.useLowLevelHttpTransport();
    String method = this.method;
    GenericUrl url = this.url;
    String urlString = url.build();
    LowLevelHttpRequest lowLevelHttpRequest;
    if (method.equals("DELETE")) {
      lowLevelHttpRequest = lowLevelHttpTransport.buildDeleteRequest(urlString);
    } else if (method.equals("GET")) {
      lowLevelHttpRequest = lowLevelHttpTransport.buildGetRequest(urlString);
    } else if (method.equals("PATCH")) {
      if (!lowLevelHttpTransport.supportsPatch()) {
        throw new IllegalArgumentException(
            "HTTP transport doesn't support PATCH");
      }
      lowLevelHttpRequest = lowLevelHttpTransport.buildPatchRequest(urlString);
    } else if (method.equals("POST")) {
      lowLevelHttpRequest = lowLevelHttpTransport.buildPostRequest(urlString);
    } else if (method.equals("PUT")) {
      lowLevelHttpRequest = lowLevelHttpTransport.buildPutRequest(urlString);
    } else {
      throw new IllegalArgumentException("illegal method: " + method);
    }
    Logger logger = HttpTransport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    StringBuilder logbuf = null;
    // log method and URL
    if (loggable) {
      logbuf = new StringBuilder();
      logbuf.append("-------------- REQUEST  --------------").append(
          Strings.LINE_SEPARATOR);
      logbuf.append(method).append(' ').append(urlString).append(
          Strings.LINE_SEPARATOR);
    }
    for (Map.Entry<String, Object> headerEntry : this.headers.entrySet()) {
      String value = (String) headerEntry.getValue();
      if (value != null) {
        String name = headerEntry.getKey();
        if (logbuf != null) {
          logbuf.append(name).append(": ");
          if ("Authorization".equals(name) && !logger.isLoggable(Level.ALL)) {
            logbuf.append("<Not Logged>");
          } else {
            logbuf.append(value);
          }
          logbuf.append(Strings.LINE_SEPARATOR);
        }
        lowLevelHttpRequest.addHeader(name, value);
      }
    }
    // content
    HttpContent content = this.content;
    if (content != null) {
      if (loggable && !this.disableContentLogging) {
        content = new LogContent(content);
      }
      content = new GZipContent(content);
      if (loggable) {
        logbuf.append("Content-Type: " + content.getType()).append(
            Strings.LINE_SEPARATOR);
        String contentEncoding = content.getEncoding();
        if (contentEncoding != null) {
          logbuf.append("Content-Encoding: " + contentEncoding).append(
              Strings.LINE_SEPARATOR);
        }
        long contentLength = content.getLength();
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
