// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.http.HttpParser;
import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.v2.jsonc.Jsonc;

import java.io.IOException;

public final class JacksonHttpParser implements HttpParser {

  public static final JacksonHttpParser INSTANCE = new JacksonHttpParser();

  public static void set(HttpTransport transport) {
    transport.setParser(INSTANCE);
  }

  public String getContentType() {
    return Jsonc.CONTENT_TYPE;
  }

  public <T> T parse(HttpResponse response, Class<T> entityClass)
      throws IOException {
    return Jackson.parse(response, entityClass);
  }

  private JacksonHttpParser() {
  }
}
