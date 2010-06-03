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

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

import java.util.HashMap;

/**
 * Stores HTTP headers used in an HTTP request or response, as defined in <a
 * href="http://tools.ietf.org/html/rfc2616#section-14">Header Field
 * Definitions</a>.
 * <p>
 * {@code null} is not allowed as a name or value of a header. Names are
 * case-insensitive.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class HttpHeaders extends GenericData {

  /** {@code "Accept-Encoding"} header. */
  @Key("Accept-Encoding")
  public String acceptEncoding;

  /** {@code "Authorization"} header. */
  @Key("Authorization")
  public String authorization;

  /**
   * {@code "Content-Encoding"} header.
   * 
   * @since 2.3
   */
  @Key("Content-Encoding")
  public String contentEncoding;

  /**
   * {@code "Content-Length"} header.
   * 
   * @since 2.3
   */
  @Key("Content-Length")
  public String contentLength;

  /**
   * {@code "Content-Type"} header.
   * 
   * @since 2.3
   */
  @Key("Content-Type")
  public String contentType;

  /**
   * {@code "Content-Range"} header.
   * 
   * @since 2.3
   */
  @Key("Content-Range")
  public String contentRange;

  /**
   * {@code "ETag"} header.
   * 
   * @since 2.3
   */
  @Key("ETag")
  public String etag;

  /**
   * {@code "Expires"} header.
   * 
   * @since 2.3
   */
  @Key("Expires")
  public String expires;

  /**
   * {@code "If-Modified-Since"} header.
   * 
   * @since 2.3
   */
  @Key("If-Modified-Since")
  public String ifModifiedSince;

  /** {@code "If-Match"} header. */
  @Key("If-Match")
  public String ifMatch;

  /** {@code "If-None-Match"} header. */
  @Key("If-None-Match")
  public String ifNoneMatch;

  /**
   * {@code "If-Unmodified-Since"} header.
   * 
   * @since 2.3
   */
  @Key("If-Unmodified-Since")
  public String ifUnmodifiedSince;

  /**
   * {@code "Location"} header.
   * 
   * @since 2.3
   */
  @Key("Location")
  public String location;

  /** {@code "MIME-Version"} header. */
  @Key("MIME-Version")
  public String mimeVersion;

  /**
   * {@code "Range"} header.
   * 
   * @since 2.3
   */
  @Key("Range")
  public String range;

  /**
   * {@code "Retry-After"} header.
   * 
   * @since 2.3
   */
  @Key("Retry-After")
  public String retryAfter;

  /** {@code "User-Agent"} header. */
  @Key("User-Agent")
  public String userAgent;

  /**
   * {@code "WWW-Authenticate"} header.
   * 
   * @since 2.3
   */
  @Key("WWW-Authenticate")
  public String authenticate;

  @Override
  public HttpHeaders clone() {
    return (HttpHeaders) super.clone();
  }

  /**
   * Map from lower-case field name to field name used to allow for case
   * insensitive HTTP headers.
   */
  private static HashMap<String, String> fieldNameMap;

  /**
   * Returns the map from lower-case field name to field name used to allow for
   * case insensitive HTTP headers.
   */
  private static HashMap<String, String> getFieldNameMap() {
    HashMap<String, String> fieldNameMap = HttpHeaders.fieldNameMap;
    if (fieldNameMap == null) {
      HttpHeaders.fieldNameMap = fieldNameMap = new HashMap<String, String>();
      for (String keyName : ClassInfo.of(HttpHeaders.class).getKeyNames()) {
        fieldNameMap.put(keyName.toLowerCase(), keyName);
      }
    }
    return fieldNameMap;
  }

  /**
   * Returns the field name to use when setting HTTP header fields for the given
   * header name, based on a case-insensitive search for that header name in the
   * pre-defined fields.
   */
  static String getCaseInsensitiveName(String headerName) {
    String fieldName = getFieldNameMap().get(headerName.toLowerCase());
    return fieldName == null ? headerName : fieldName;
  }
}
