// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

public final class AtomInfo {
  public SortedMap<String, String> namespaces;

  void parse(JsonParser parser) throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      if (key == "namespaces") {
        namespaces = new TreeMap<String, String>();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
          key = parser.getCurrentName();
          parser.nextToken();
          namespaces.put(key, parser.getText());
        }
      } else {
        throw new IllegalArgumentException("unrecognized key: " + key);
      }
    }
    if (namespaces == null || namespaces.isEmpty()) {
      throw new IllegalArgumentException("namespaces required");
    }
  }
}
