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
import com.google.api.client.escape.Escaper;
import com.google.api.client.escape.PercentEscaper;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.util.Objects;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
 * <li>User-information component.</li>
 * <li>Encoded slash character ('/') in the path</li>
 * </ul>
 * See <a href="http://tools.ietf.org/html/rfc3986">RFC 3986: Uniform Resource
 * Identifier (URI)</a>
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GenericUrl extends GenericData {

  private static final Escaper URI_FRAGMENT_ESCAPER =
      new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);

  /** Scheme (lowercase), for example {@code "https"}. */
  public String scheme;

  /** Host, for example {@code "www.google.com"}. */
  public String host;

  /** Port number or {@code -1} if undefined, for example {@code 443}. */
  public int port = -1;

  /**
   * Path component or {@code null} for none, for example {@code
   * "/m8/feeds/contacts/default/full"}.
   * <p>
   * This field is ignored if {@link #pathParts} is not {@code null}.
   * 
   * @deprecated (scheduled to be removed in version 2.4) Use
   *             {@link #getRawPath()}, {@link #setRawPath(String rawPath)}, or
   *             {@link #appendRawPath(String)}
   */
  @Deprecated
  public String path;

  /**
   * Decoded path component by parts with each part separated by a {@code '/'}
   * or {@code null} for none, for example {@code
   * "/m8/feeds/contacts/default/full"} is represented by {@code "", "m8",
   * "feeds", "contacts", "default", "full"}.
   * <p>
   * Use {@link #appendRawPath(String)} to append to the path, which ensures
   * that no extra slash is added.
   * 
   * @since 2.3
   */
  public List<String> pathParts;

  /**
   * Fragment component or {@code null} for none.
   * 
   * @since 2.3
   */
  public String fragment;

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
    URI uri;
    try {
      uri = new URI(encodedUrl);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    this.scheme = uri.getScheme().toLowerCase();
    this.host = uri.getHost();
    this.port = uri.getPort();
    this.path = uri.getPath();
    this.pathParts = toPathParts(uri.getRawPath());
    this.fragment = uri.getFragment();
    String query = uri.getRawQuery();
    if (query != null) {
      UrlEncodedParser.parse(query, this);
    }
  }

  @Override
  public int hashCode() {
    int result = this.scheme.hashCode();
    result = result * 31 + this.host.hashCode();
    result = result * 31 + this.port;
    result = result * 31 + (this.path == null ? 0 : path.hashCode());
    result = result * 31 + (this.pathParts == null ? 0 : pathParts.hashCode());
    result = result * 31 + (this.fragment == null ? 0 : fragment.hashCode());
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
    return this.scheme.equals(other.scheme) && this.host.equals(other.host)
        && this.port == other.port
        && Objects.equal(this.fragment, other.fragment)
        && Objects.equal(this.path, other.path)
        && Objects.equal(this.pathParts, other.pathParts);
  }

  @Override
  public String toString() {
    return build();
  }

  @Override
  public GenericUrl clone() {
    GenericUrl result = (GenericUrl) super.clone();
    result.pathParts = new ArrayList<String>(this.pathParts);
    return result;
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
    List<String> pathParts = this.pathParts;
    if (pathParts != null) {
      appendRawPathFromParts(buf);
    } else {
      String path = this.path;
      if (path != null && path.length() != 0) {
        int cur = 0;
        boolean notDone = true;
        while (notDone) {
          int slash = path.indexOf('/', cur);
          notDone = slash != -1;
          String sub =
              notDone ? path.substring(cur, slash) : path.substring(cur);
          buf.append(CharEscapers.escapeUriPath(sub));
          if (notDone) {
            buf.append('/');
          }
          cur = slash + 1;
        }
      }
    }
    // query parameters (similar to UrlEncodedContent)
    boolean first = true;
    for (Map.Entry<String, Object> nameValueEntry : entrySet()) {
      Object value = nameValueEntry.getValue();
      if (value != null) {
        String name = CharEscapers.escapeUriQuery(nameValueEntry.getKey());
        String[] values;
        if (value instanceof Collection<?>) {
          Collection<?> collectionValue = (Collection<?>) value;
          for (Object repeatedValue : collectionValue) {
            first = appendParam(first, buf, name, repeatedValue);
          }
        } else {
          first = appendParam(first, buf, name, value);
        }
      }
    }
    // URL fragment
    String fragment = this.fragment;
    if (fragment != null) {
      buf.append('#').append(URI_FRAGMENT_ESCAPER.escape(fragment));
    }
    return buf.toString();
  }

  /**
   * Returns the raw encoded path computed from the {@link #pathParts}.
   * 
   * @return raw encoded path computed from the {@link #pathParts} or {@code
   *         null} if {@link #pathParts} is {@code null}
   * @since 2.3
   */
  public String getRawPath() {
    List<String> pathParts = this.pathParts;
    if (pathParts == null) {
      return null;
    }
    StringBuilder buf = new StringBuilder();
    appendRawPathFromParts(buf);
    return buf.toString();
  }

  /**
   * Sets the {@link #pathParts} from the given raw encoded path.
   * 
   * @param encodedPath raw encoded path or {@code null} to set
   *        {@link #pathParts} to {@code null}
   * @since 2.3
   */
  public void setRawPath(String encodedPath) {
    this.pathParts = toPathParts(encodedPath);
  }

  /**
   * Appends the given raw encoded path to the current {@link #pathParts},
   * setting field only if it is {@code null} or empty.
   * <p>
   * The last part of the {@link #pathParts} is merged with the first part of
   * the path parts computed from the given encoded path. Thus, if the current
   * raw encoded path is {@code "a"}, and the given encoded path is {@code "b"},
   * then the resulting raw encoded path is {@code "ab"}.
   * 
   * @param encodedPath raw encoded path or {@code null} to ignore
   * @since 2.3
   */
  public void appendRawPath(String encodedPath) {
    if (encodedPath != null && encodedPath.length() != 0) {
      List<String> pathParts = this.pathParts;
      List<String> appendedPathParts = toPathParts(encodedPath);
      if (pathParts == null || pathParts.isEmpty()) {
        this.pathParts = appendedPathParts;
      } else {
        int size = pathParts.size();
        pathParts.set(size - 1, pathParts.get(size - 1)
            + appendedPathParts.get(0));
        pathParts
            .addAll(appendedPathParts.subList(1, appendedPathParts.size()));
      }
    }
  }

  /**
   * Returns the decoded path parts for the given encoded path.
   * 
   * @param encodedPath slash-prefixed encoded path, for example {@code
   *        "/m8/feeds/contacts/default/full"}
   * @return decoded path parts, with each part assumed to be preceded by a
   *         {@code '/'}, for example {@code "", "m8", "feeds", "contacts",
   *         "default", "full"}, or {@code null} for {@code null} or {@code ""}
   *         input
   * @since 2.3
   */
  public static List<String> toPathParts(String encodedPath) {
    if (encodedPath == null || encodedPath.length() == 0) {
      return null;
    }
    List<String> result = new ArrayList<String>();
    int cur = 0;
    boolean notDone = true;
    while (notDone) {
      int slash = encodedPath.indexOf('/', cur);
      notDone = slash != -1;
      String sub;
      if (notDone) {
        sub = encodedPath.substring(cur, slash);
      } else {
        sub = encodedPath.substring(cur);
      }
      result.add(CharEscapers.decodeUri(sub));
      cur = slash + 1;
    }
    return result;
  }

  private void appendRawPathFromParts(StringBuilder buf) {
    List<String> pathParts = this.pathParts;
    int size = pathParts.size();
    for (int i = 0; i < size; i++) {
      String pathPart = pathParts.get(i);
      if (i != 0) {
        buf.append('/');
      }
      if (pathPart.length() != 0) {
        buf.append(CharEscapers.escapeUriPath(pathPart));
      }
    }
  }

  private static boolean appendParam(boolean first, StringBuilder buf,
      String name, Object value) {
    if (first) {
      first = false;
      buf.append('?');
    } else {
      buf.append('&');
    }
    buf.append(name).append('=').append(
        CharEscapers.escapeUriQuery(value.toString()));
    return first;
  }
}
