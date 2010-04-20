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
  public volatile LowLevelHttpTransport lowLevelHttpTransport;

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

  /**
   * Authorization header provider to use for all requests or {@code null} for
   * none.
   */
  public volatile AuthorizationHeaderProvider authorizationHeaderProvider;

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

  /**
   * Sets the default header for the given name and value or remove the default
   * header if value is {@code null}.
   */
  public void setDefaultHeader(String name, String value) {
    if (value == null) {
      this.defaultHeaders.remove(name);
    } else {
      this.defaultHeaders.put(name, value);
    }
  }

  /**
   * Sets the {@code "Accept-Encoding"} default header or remove the default
   * header if value is {@code null}.
   */
  public void setAcceptEncodingHeader(String value) {
    setDefaultHeader("Accept-Encoding", value);
  }

  /**
   * Sets the {@code "User-Agent"} default header or remove the default header
   * if value is {@code null}.
   */
  public void setUserAgentHeader(String value) {
    setDefaultHeader("User-Agent", value);
  }

  /**
   * Sets the {@code "Authorization"} default header (without logging value) or
   * remove the default header if value is {@code null}.
   */
  public void setAuthorizationHeader(String value) {
    if (value == null) {
      this.defaultHeadersNoLogging.remove("Authorization");
    } else {
      this.defaultHeadersNoLogging.put("Authorization", value);
    }
  }

  /** Builds a {@code DELETE} request for the given request URI. */
  public HttpRequest buildDeleteRequest(String uri) throws IOException {
    return new HttpRequest(this, "DELETE", uri, getLowLevelHttpTransport()
        .buildDeleteRequest(uri));
  }

  /** Builds a {@code GET} request for the given request URI. */
  public HttpRequest buildGetRequest(String uri) throws IOException {
    return new HttpRequest(this, "GET", uri, getLowLevelHttpTransport()
        .buildGetRequest(uri));
  }

  /** Builds a {@code PATCH} request for the given request URI. */
  public HttpRequest buildPatchRequest(String uri) throws IOException {
    LowLevelHttpTransport lowLevelHttpTransport = getLowLevelHttpTransport();
    if (!lowLevelHttpTransport.supportsPatch()) {
      throw new IllegalArgumentException("HTTP transport doesn't support PATCH");
    }
    return new HttpRequest(this, "PATCH", uri, getLowLevelHttpTransport()
        .buildPatchRequest(uri));
  }

  /** Builds a {@code POST} request for the given request URI. */
  public HttpRequest buildPostRequest(String uri) throws IOException {
    return new HttpRequest(this, "POST", uri, getLowLevelHttpTransport()
        .buildPostRequest(uri));
  }

  /** Builds a {@code PUT} request for the given request URI. */
  public HttpRequest buildPutRequest(String uri) throws IOException {
    return new HttpRequest(this, "PUT", uri, getLowLevelHttpTransport()
        .buildPutRequest(uri));
  }
}
