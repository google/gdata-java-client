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
import com.google.api.client.util.Entity;

import java.util.Collection;
import java.util.Map;

/**
 * Builds a URI as an entity.
 * <p>
 * It allows the query parameters of the URI to be built using the entity
 * fields. Subclasses can extend the known query parameters by declaring public
 * fields whose name matches the name of the query parameter.
 */
public class UriEntity extends Entity {
  private String path;

  // TODO: cloneable?

  /** Constructs from the given encoded URI. */
  public UriEntity(String encodedUri) {
    // TODO: encode path: tricky because need to remember which slashes need to
    // be escaped
    int query = encodedUri.indexOf('?');
    String path;
    if (query == -1) {
      path = encodedUri;
    } else {
      path = encodedUri.substring(0, query);
      UrlEncodedFormHttpParser.parse(encodedUri.substring(query + 1), this);
    }
    this.path = CharEscapers.decodeUri(path);
  }

  public final String getPath() {
    return this.path;
  }

  public final void setPath(String path) {
    this.path = path;
  }

  @Override
  public int hashCode() {
    return super.hashCode() * 31 + path.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj) || !(obj instanceof UriEntity)) {
      return false;
    }
    UriEntity other = (UriEntity) obj;
    return this.path.equals(other.path);
  }

  @Override
  public String toString() {
    return build();
  }

  public final String build() {
    StringBuilder buf = new StringBuilder(this.path);
    boolean startedQuery = false;
    for (Map.Entry<String, Object> fieldEntry : entrySet()) {
      Object value = fieldEntry.getValue();
      if (value != null) {
        Collection<Object> collectionValue = null;
        if (value instanceof Collection<?>) {
          @SuppressWarnings("unchecked")
          Collection<Object> collection = (Collection<Object>) value;
          collectionValue = collection;
        }
        if (value != null
            && (collectionValue == null || !collectionValue.isEmpty())) {
          if (startedQuery) {
            buf.append("&");
          } else {
            buf.append("?");
            startedQuery = true;
          }
          buf.append(fieldEntry.getKey()).append('=');
          if (collectionValue != null) {
            boolean first = true;
            for (Object element : collectionValue) {
              if (first) {
                first = false;
              } else {
                // TODO: need way to specify collection element separator, for
                // example space char
                // TODO: actually spaces would be preferred?
                buf.append(',');
              }
              buf.append(escape(element));
            }
          } else {
            buf.append(escape(value));
          }
        }
      }
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
