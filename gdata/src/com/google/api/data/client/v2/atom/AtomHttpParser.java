// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.HttpParser;
import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpTransport;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public final class AtomHttpParser implements HttpParser {

  public static final AtomHttpParser INSTANCE = new AtomHttpParser();

  public static void set(HttpTransport transport) {
    transport.setParser(INSTANCE);
  }

  public String getContentType() {
    return Atom.CONTENT_TYPE;
  }

  public <T> T parse(HttpResponse response, Class<T> entityClass)
      throws IOException {
    try {
      return Atom.parse(response, entityClass);
    } catch (XmlPullParserException e) {
      IOException exception = new IOException();
      exception.initCause(e);
      throw exception;
    }
  }

  private AtomHttpParser() {
  }
}
