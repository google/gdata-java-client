// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

public final class AtomFeedResponse extends AtomResponse {

  AtomFeedResponse(AtomClient client, XmlPullParser parser,
      InputStream inputStream) {
    super(client, parser, inputStream);
  }

  public <T, I> AtomFeedParser<T, I> useFeedParser(Class<T> feedClass,
      Class<I> itemClass) {
    return this.client.useFeedParser(this.parser, this.inputStream, feedClass,
        itemClass);
  }

  public <T> AtomMultiKindFeedParser<T> useMultiKindFeedParser(
      Class<T> feedClass, Class<?>... itemClasss) {
    return this.client.useMultiKindFeedParser(this.parser, this.inputStream,
        feedClass, itemClasss);
  }
}
