// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import com.google.api.data.client.v2.escape.CharEscapers;

import java.util.Collection;

/**
 * Builds a URI for accessing Google API feeds. It stores the path part and the
 * query parameters in a different way. Subclasses can extend the known query
 * parameters by declaring public fields whose name matches the name of the
 * query parameter.
 */
public class UriEntity extends Entity {
  // TODO: URI entity
  public String alt;
  public String fields;
  private String path;

  // TODO: clonable?

  public UriEntity(String uri) {
    // decode the path/query parts of URI
    // TODO: URI decoding
    int query = uri.indexOf('?');
    if (query == -1) {
      this.path = uri;
    } else {
      this.path = uri.substring(0, query);
      int cur = query;
      while (cur != uri.length()) {
        int next = uri.indexOf('&', cur + 1);
        if (next == -1) {
          next = uri.length();
        }
        int equals = uri.indexOf('=', cur + 1);
        if (equals <= cur + 1 || equals >= next - 1) {
          throw new IllegalArgumentException("malformed query parameter: "
              + uri.substring(cur + 1, next));
        }
        String name = uri.substring(cur + 1, equals).intern();
        String value = uri.substring(equals + 1, next);
        set(name, value);
        cur = next;
      }
    }
  }

  public final String getPath() {
    return this.path;
  }

  public final void setPath(String path) {
    this.path = path;
  }

  public final String build() {
    StringBuilder buf = new StringBuilder(this.path);
    boolean startedQuery = false;
    FieldIterator fieldIterator = FieldIterators.of(this);
    while (fieldIterator.hasNext()) {
      String name = fieldIterator.nextFieldName();
      Object value = fieldIterator.getFieldValue();
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
          buf.append(name).append('=');
          if (collectionValue != null) {
            boolean first = true;
            for (Object element : collectionValue) {
              if (first) {
                first = false;
              } else {
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
