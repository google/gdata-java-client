// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;

public final class JsoncFeedResponse extends JsoncResponse {

  JsoncFeedResponse(JsonParser parser) {
    super(parser);
  }

  public <T, I> JsoncFeedParser<T, I> useFeedParser(Class<T> feedClass,
      Class<I> itemClass) {
    return Jackson.useFeedParser(this.parser, feedClass, itemClass);
  }

  public <T, I> JsoncFeedParser<T, I> usePartialParser(Class<T> feedClass,
      Class<I> itemClass) throws IOException {
    return Jackson.usePartialFeedParser(this.parser, feedClass, itemClass);
  }

  public <T> JsoncMultiKindFeedParser<T> useMultiKindFeedParser(
      Class<T> feedClass, Class<?>... itemClasss) {
    return Jackson.useMultiKindFeedParser(this.parser, feedClass, itemClasss);
  }

  public <T> JsoncMultiKindFeedParser<T> usePartialMultiKindFeedParser(
      Class<T> feedClass, Class<?>... itemClasss) throws IOException {
    return Jackson.usePartialMultiKindFeedParser(this.parser, feedClass,
        itemClasss);
  }
}
