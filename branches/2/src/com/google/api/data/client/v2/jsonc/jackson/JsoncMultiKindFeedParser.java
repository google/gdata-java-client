// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.v2.ClassInfo;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

/** GData JSON-C feed parser when the item class can be computed from the kind. */
public final class JsoncMultiKindFeedParser<T> extends
    AbstractJsoncFeedParser<T> {

  private final HashMap<String, Class<?>> kindToItemClassMap =
      new HashMap<String, Class<?>>();

  JsoncMultiKindFeedParser(JsonParser parser, Class<T> feedClass,
      Class<?>... itemClasses) {
    super(parser, feedClass);
    int numItems = itemClasses.length;
    HashMap<String, Class<?>> kindToItemClassMap = this.kindToItemClassMap;
    for (int i = 0; i < numItems; i++) {
      Class<?> itemClass = itemClasses[i];
      ClassInfo classInfo = ClassInfo.of(itemClass);
      Field field = classInfo.getField("kind");
      if (field == null) {
        throw new IllegalArgumentException("missing kind field for "
            + itemClass.getName());
      }
      Object item = ClassInfo.newInstance(itemClass);
      String kind = (String) ClassInfo.getValue(field, item);
      if (kind == null) {
        throw new IllegalArgumentException("missing value for kind field in "
            + itemClass.getName());
      }
      kindToItemClassMap.put(kind, itemClass);
    }
  }

  @Override
  Object parseItemInternal() throws IOException {
    parser.nextToken();
    String key = parser.getCurrentName();
    if (key != "kind") {
      throw new IllegalArgumentException("expected kind field: " + key);
    }
    parser.nextToken();
    String kind = parser.getText();
    Class<?> itemClass = kindToItemClassMap.get(kind);
    if (itemClass == null) {
      throw new IllegalArgumentException("unrecognized kind: " + kind);
    }
    return Jackson.parse(parser, itemClass, null);
  }
}
