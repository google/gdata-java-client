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
import com.google.api.client.http.net.NetGData;

import java.io.IOException;
import java.util.logging.Logger;

public class HttpTransport {

  static final Logger LOGGER = Logger.getLogger(HttpTransport.class.getName());

  public static final boolean DISABLE_GZIP =
      Boolean.getBoolean(HttpTransport.class.getName() + ".DisableGZip");

  /**
   * Low level HTTP transport interface to use or {@code null} to use the
   * default of {@code java.net} transport. See
   * {@link #getLowLevelHttpTransport()}.
   */
  public LowLevelHttpTransport lowLevelHttpTransport;

  /** Returns the low-level HTTP transport to use. */
  public LowLevelHttpTransport getLowLevelHttpTransport() {
    LowLevelHttpTransport lowLevelHttpTransportInterface =
        this.lowLevelHttpTransport;
    if (lowLevelHttpTransportInterface == null) {
      // TODO: specify this in the metadata of the packaged jar?
      this.lowLevelHttpTransport =
          lowLevelHttpTransportInterface = NetGData.HTTP_TRANSPORT;
    }
    return lowLevelHttpTransportInterface;
  }

  /**
   * Default headers mapping from header name to header value. These transport
   * default headers are put into a request's headers when its build method is
   * called.
   */
  public final ArrayMap<String, String> defaultHeaders = ArrayMap.create();

  /**
   * Default headers mapping from header name to header value for headers whose
   * value must not be logged (for example authentication-related headers).
   * These transport default headers are put into a request's headers when its
   * build method is called.
   */
  public final ArrayMap<String, String> defaultHeadersNoLogging =
      ArrayMap.create();

  private final ArrayMap<String, HttpParser> contentTypeToParserMap =
      ArrayMap.create();

  public final void setParser(HttpParser parser) {
    this.contentTypeToParserMap.put(parser.getContentType(), parser);
  }

  public final HttpParser getParser(String contentType) {
    if (contentType == null) {
      return null;
    }
    int semicolon = contentType.indexOf(';');
    if (semicolon != -1) {
      contentType = contentType.substring(0, semicolon);
    }
    return this.contentTypeToParserMap.get(contentType);
  }

  public HttpTransport() {
    if (!DISABLE_GZIP) {
      setAcceptEncodingHeader("gzip");
    }
  }

  public void setAcceptEncodingHeader(String value) {
    this.defaultHeaders.put("Accept-Encoding", value);
  }

  public void setUserAgentHeader(String value) {
    this.defaultHeaders.put("User-Agent", value);
  }

  public void setAuthorizationHeader(String value) {
    this.defaultHeadersNoLogging.put("Authorization", value);
  }

  public HttpRequest buildDeleteRequest(String uri) throws IOException {
    return new HttpRequest(this, getLowLevelHttpTransport().buildDeleteRequest(
        uri));
  }

  public HttpRequest buildGetRequest(String uri) throws IOException {
    return new HttpRequest(this, getLowLevelHttpTransport()
        .buildGetRequest(uri));
  }

  public HttpRequest buildPatchRequest(String uri) throws IOException {
    LowLevelHttpTransport lowLevelHttpTransport = getLowLevelHttpTransport();
    if (!lowLevelHttpTransport.supportsPatch()) {
      throw new IllegalArgumentException("HTTP transport doesn't support PATCH");
    }
    return new HttpRequest(this, getLowLevelHttpTransport().buildPatchRequest(
        uri));
  }

  public HttpRequest buildPostRequest(String uri) throws IOException {
    return new HttpRequest(this, getLowLevelHttpTransport().buildPostRequest(
        uri));
  }

  public HttpRequest buildPutRequest(String uri) throws IOException {
    return new HttpRequest(this, getLowLevelHttpTransport()
        .buildPutRequest(uri));
  }
}
