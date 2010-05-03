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

import com.google.api.client.javanet.NetGData;
import com.google.api.client.util.ArrayMap;

import java.io.IOException;
import java.util.logging.Logger;

/** HTTP transport. */
public class HttpTransport {

  static final Logger LOGGER = Logger.getLogger(HttpTransport.class.getName());

  /**
   * Whether to disabel GZip compression of request and response content.
   * Default is {@code false}.
   */
  public static final boolean DISABLE_GZIP =
      Boolean.getBoolean(HttpTransport.class.getName() + ".DisableGZip");

  /**
   * Low level HTTP transport interface to use or {@code null} to use the
   * default of {@code java.net} transport.
   */
  private volatile LowLevelHttpTransport lowLevelHttpTransport;

  /**
   * Sets to the given low level HTTP transport.
   * 
   * @param lowLevelHttpTransport low level HTTP transport or {@code null} to
   *        use the default of {@code java.net} transport
   */
  public void setLowLevelHttpTransport(
      LowLevelHttpTransport lowLevelHttpTransport) {
    this.lowLevelHttpTransport = lowLevelHttpTransport;
  }

  /**
   * Returns the low-level HTTP transport to use. If
   * {@link #setLowLevelHttpTransport(LowLevelHttpTransport)} hasn't been
   * called, it uses the default of {@code java.net} transport.
   */
  protected LowLevelHttpTransport useLowLevelHttpTransport() {
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
   * Default HTTP headers. These transport default headers are put into a
   * request's headers when its build method is called.
   */
  public volatile HttpHeaders defaultHeaders = new HttpHeaders();

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
      defaultHeaders.setAcceptEncoding("gzip");
    }
  }

  /** Builds a {@code DELETE} request for the given request URI. */
  public HttpRequest buildDeleteRequest(String uri) throws IOException {
    return new HttpRequest(this, "DELETE", uri, useLowLevelHttpTransport()
        .buildDeleteRequest(uri));
  }

  /** Builds a {@code GET} request for the given request URI. */
  public HttpRequest buildGetRequest(String uri) throws IOException {
    return new HttpRequest(this, "GET", uri, useLowLevelHttpTransport()
        .buildGetRequest(uri));
  }

  /** Builds a {@code PATCH} request for the given request URI. */
  public HttpRequest buildPatchRequest(String uri) throws IOException {
    LowLevelHttpTransport lowLevelHttpTransport = useLowLevelHttpTransport();
    if (!lowLevelHttpTransport.supportsPatch()) {
      throw new IllegalArgumentException("HTTP transport doesn't support PATCH");
    }
    return new HttpRequest(this, "PATCH", uri, useLowLevelHttpTransport()
        .buildPatchRequest(uri));
  }

  /** Builds a {@code POST} request for the given request URI. */
  public HttpRequest buildPostRequest(String uri) throws IOException {
    return new HttpRequest(this, "POST", uri, useLowLevelHttpTransport()
        .buildPostRequest(uri));
  }

  /** Builds a {@code PUT} request for the given request URI. */
  public HttpRequest buildPutRequest(String uri) throws IOException {
    return new HttpRequest(this, "PUT", uri, useLowLevelHttpTransport()
        .buildPutRequest(uri));
  }
}
