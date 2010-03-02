// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import com.google.api.data.client.v2.escape.CharEscapers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;


/**
 * Builds a URI for accessing data feeds. It stores the path part and the query
 * parameters in a different way. Subclasses can extend the known query
 * parameters by declaring public fields whose name matches the name of the
 * query parameter.
 */
public class GDataUri {
  public String alt;
  public String fields;
  private String path;
  private IdentityHashMap<String, Object> unknown = null;
  private final ClassInfo classInfo = ClassInfo.of(getClass());

  // TODO: clonable?

  public GDataUri(String uri) {
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

  public final Object get(String name) {
    name = name.intern();
    Field field = this.classInfo.getField(name);
    if (field != null) {
      return ClassInfo.getValue(field, this);
    }
    Object value = null;
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown != null) {
      value = unknown.get(name);
    }
    return value;
  }

  public final void set(String name, Object value) {
    name = name.intern();
    Field field = this.classInfo.getField(name);
    if (field != null) {
      ClassInfo.setValue(field, this, value);
      return;
    }
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown == null) {
      unknown = new IdentityHashMap<String, Object>();
    }
    unknown.put(name.intern(), value);
  }

  public final String build() {
    StringBuilder buf = new StringBuilder(this.path);
    boolean startedQuery = false;
    ClassInfo classInfo = this.classInfo;
    for (String name : classInfo.getNames()) {
      Object value = ClassInfo.getValue(classInfo.getField(name), this);
      startedQuery = appendValue(startedQuery, buf, name, value);
    }
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown != null) {
      for (Map.Entry<String, Object> entry : unknown.entrySet()) {
        startedQuery =
            appendValue(startedQuery, buf, entry.getKey(), entry.getValue());
      }
    }
    return buf.toString();
  }

  private static boolean appendValue(boolean startedQuery, StringBuilder buf,
      String name, Object value) {
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
    return startedQuery;
  }

  private static String escape(Object value) {
    String string = value.toString();
    if (value instanceof Number) {
      return string;
    }
    return CharEscapers.escapeUriQuery(string);
  }
}
