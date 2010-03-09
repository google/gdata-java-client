// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.v2.escape.CharEscapers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class UrlEncodedFormHttpSerializer implements HttpSerializer {

  private final byte[] content;

  UrlEncodedFormHttpSerializer(byte[] content) {
    this.content = content;
  }

  public String getContentEncoding() {
    return null;
  }

  public long getContentLength() {
    return this.content.length;
  }

  public String getContentType() {
    return "application/x-www-form-urlencoded";
  }

  public void writeTo(OutputStream out) throws IOException {
    out.write(content);
  }

  public static final class Builder {
    private final List<String> names = new ArrayList<String>();
    private final List<String> values = new ArrayList<String>();

    public void add(String name, String value) {
      if (name == null || value == null) {
        throw new NullPointerException();
      }
      names.add(name);
      values.add(value);
    }

    public UrlEncodedFormHttpSerializer build() {
      StringBuilder buf = new StringBuilder();
      List<String> names = this.names;
      List<String> values = this.values;
      int size = names.size();
      for (int i = 0; i < size; i++) {
        if (i != 0) {
          buf.append('&');
        }
        String name = CharEscapers.escapeUri(names.get(i));
        String value = CharEscapers.escapeUri(values.get(i));
        buf.append(name).append('=').append(value);
      }
      return new UrlEncodedFormHttpSerializer(buf.toString().getBytes());
    }
  }
}
