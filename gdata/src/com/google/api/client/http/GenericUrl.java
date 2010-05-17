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

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * URL builder in which the query parameters are specified as generic data
 * key/value pairs.
 * 
 * <p>
 * The query parameters are specified with the data key name as the parameter
 * name, and the data value as the parameter value. Subclasses can declare
 * fields for known query parameters using the {@link Key} annotation. {@code
 * null} parameter names are not allowed, but {@code null} query values are
 * allowed.
 * </p>
 * 
 * <p>
 * The following features are not supported:
 * <ul>
 * <li>Repeated query parameters</li>
 * <li>User-information or fragment components.</li>
 * <li>Encoded slash character ('/') in the path</li>
 * </ul>
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GenericUrl extends GenericData {

  /** Scheme (lowercase), for example {@code "https"}. */
  public String scheme;

  /** Host, for example {@code "www.google.com"}. */
  public String host;

  /** Port number or {@code -1} if undefined, for example {@code 443}. */
  public int port = -1;

  /**
   * Path component or {@code null} for none, for example {@code
   * "/m8/feeds/contacts/default/full"}.
   */
  public String path;

  public GenericUrl() {
  }

  /**
   * Constructs from an encoded URL.
   * <p>
   * Any known query parameters with pre-defined fields as data keys will be
   * parsed based on their data type. Any unrecognized query parameter will
   * always be parsed as a string.
   * 
   * @param encodedUrl encoded URL, including any existing query parameters that
   *        should be parsed
   * @throws IllegalArgumentException if URL has a syntax error
   */
  public GenericUrl(String encodedUrl) {
    // figure out the pre-query/query components
    int query = encodedUrl.indexOf('?');
    String preQuery;
    if (query == -1) {
      preQuery = encodedUrl;
    } else {
      preQuery = encodedUrl.substring(0, query);
      UrlEncodedParser.parse(encodedUrl.substring(query + 1), this);
    }
    // parse the pre-query part
    URI uri;
    try {
      uri = new URI(preQuery);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    this.scheme = uri.getScheme().toLowerCase();
    this.host = uri.getHost();
    this.port = uri.getPort();
    this.path = uri.getPath();
  }

  @Override
  public int hashCode() {
    int result = this.scheme.hashCode();
    result = result * 31 + this.host.hashCode();
    result = result * 31 + this.port;
    result = result * 31 + (this.path == null ? 0 : path.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj) || !(obj instanceof GenericUrl)) {
      return false;
    }
    GenericUrl other = (GenericUrl) obj;
    String path = this.path;
    String otherPath = other.path;
    return this.scheme.equals(other.scheme) && this.host.equals(other.host)
        && this.port == other.port
        && (path == null ? otherPath == null : path.equals(otherPath));
  }

  @Override
  public String toString() {
    return build();
  }

  @Override
  public GenericUrl clone() {
    return (GenericUrl) super.clone();
  }

  /**
   * Constructs the string representation of the URL, including the path
   * specified by {@link #path} and the query parameters specified by this
   * generic URL.
   */
  public final String build() {
    // scheme, host, port, and path
    StringBuilder buf = new StringBuilder();
    buf.append(this.scheme).append("://").append(this.host);
    int port = this.port;
    if (port != -1) {
      buf.append(':').append(port);
    }
    String path = this.path;
    if (path != null && path.length() != 0) {
      int cur = 0;
      boolean notDone = true;
      while (notDone) {
        int slash = path.indexOf('/', cur);
        notDone = slash != -1;
        String sub = notDone ? path.substring(cur, slash) : path.substring(cur);
        buf.append(CharEscapers.escapeUriPath(sub));
        if (notDone) {
          buf.append('/');
        }
        cur = slash + 1;
      }
    }
    // compute parameters in sorted order
    SortedMap<String, String> params = new TreeMap<String, String>();
    for (Map.Entry<String, Object> fieldEntry : entrySet()) {
      Object value = fieldEntry.getValue();
      if (value != null) {
        params.put(escape(fieldEntry.getKey()), escape(value));
      }
    }
    // now add parameters to URL
    boolean startedQuery = false;
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (startedQuery) {
        buf.append("&");
      } else {
        buf.append("?");
        startedQuery = true;
      }
      buf.append(entry.getKey()).append('=').append(entry.getValue());
    }
    return buf.toString();
  }

  private static String escape(Object value) {
    String string = value.toString();
    if (value instanceof Number) {
      return string;
    }
    return CharEscapers.escapeUriQuery(string);
  }
}
