// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.v2.GDataException;
import com.google.api.data.client.v2.GDataResponse;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

/** JSON-C error response to a JSON-C request. */
public final class JsoncException extends GDataException {

  /** JSON-C error response to parse or {@code null} for none. */
  public JsonParser parser;

  JsoncException(GDataResponse response) {
    super(response);
  }

  /** Parse the JSON-C error response into the given error information type. */
  public <T> T parseError(Class<T> errorType) throws IOException {
    return Jackson.parseItem(this.parser, errorType);
  }
}
