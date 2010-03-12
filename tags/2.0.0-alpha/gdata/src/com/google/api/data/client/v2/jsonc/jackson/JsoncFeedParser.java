// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

/** GData JSON-C feed parser when the item class is known in advance. */
public final class JsoncFeedParser<T, I> extends
    AbstractJsoncFeedParser<T> {

  private final Class<I> itemClass;

  JsoncFeedParser(JsonParser parser, Class<T> feedClass, Class<I> itemClass) {
    super(parser, feedClass);
    this.itemClass = itemClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public I parseNextItem() throws IOException {
    return (I) super.parseNextItem();
  }

  @Override
  Object parseItemInternal() throws IOException {
    return Jackson.parse(parser, itemClass, null);
  }
}
