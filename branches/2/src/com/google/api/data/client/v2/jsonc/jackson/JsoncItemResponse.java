// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

public final class JsoncItemResponse extends JsoncResponse {

  JsoncItemResponse(JsonParser parser) {
    super(parser);
  }

  public <T> T parseItem(Class<T> destinationClass) throws IOException {
    return Jackson.parseItem(this.parser, destinationClass);
  }

  public <T> T parsePartialItem(Class<T> destinationClass) throws IOException {
    return Jackson.parsePartialItem(this.parser, destinationClass);
  }
}
