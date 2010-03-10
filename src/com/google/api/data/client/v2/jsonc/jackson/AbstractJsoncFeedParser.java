// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;

/**
 * Abstract base class for a GData JSON-C feed parser when the feed class is
 * known in advance.
 */
public abstract class AbstractJsoncFeedParser<T> {

  private boolean feedParsed;
  final JsonParser parser;
  final Class<T> feedClass;

  AbstractJsoncFeedParser(JsonParser parser, Class<T> feedClass) {
    this.parser = parser;
    this.feedClass = feedClass;
  }

  /**
   * Parse the feed and return a new parsed instance of the feed class. This
   * method can be skipped if all you want are the items.
   */
  public T parseFeed() throws IOException {
    boolean close = true;
    try {
      this.feedParsed = true;
      T result = Jackson.parse(this.parser, this.feedClass, "items");
      close = false;
      return result;
    } finally {
      if (close) {
        close();
      }
    }
  }

  /**
   * Parse the next item in the feed and return a new parsed instanceof of the
   * item class. If there is no item to parse, it will return {@code null} and
   * automatically close the parser (in which case there is no need to call
   * {@link #close()}.
   */
  public Object parseNextItem() throws IOException {
    JsonParser parser = this.parser;
    if (!this.feedParsed) {
      this.feedParsed = true;
      Jackson.skipToKey(parser, "items");
    }
    boolean close = true;
    try {
      if (parser.nextToken() == JsonToken.START_OBJECT) {
        Object result = parseItemInternal();
        close = false;
        return result;
      }
    } finally {
      if (close) {
        close();
      }
    }
    return null;
  }

  /** Closes the underlying parser. */
  public void close() throws IOException {
    this.parser.close();
  }

  abstract Object parseItemInternal() throws IOException;
}
