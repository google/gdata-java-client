// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

public abstract class JsoncResponse {
  public final JsonParser parser;

  JsoncResponse(JsonParser parser) {
    this.parser = parser;
  }

  public void close() throws IOException {
    this.parser.close();
  }
}
