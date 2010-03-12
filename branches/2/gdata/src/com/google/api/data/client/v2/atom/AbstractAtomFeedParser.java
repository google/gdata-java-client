// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract base class for a GData JSON-C feed parser when the feed type is
 * known in advance.
 */
public abstract class AbstractAtomFeedParser<T> {

  private boolean feedParsed;
  final XmlPullParser parser;
  final InputStream inputStream;
  final Class<T> feedClass;

  AbstractAtomFeedParser(XmlPullParser parser, InputStream inputStream,
      Class<T> feedClass) {
    this.parser = parser;
    this.inputStream = inputStream;
    this.feedClass = feedClass;
  }

  /**
   * Parse the feed and return a new parsed instance of the feed type. This
   * method can be skipped if all you want are the items.
   * 
   * @throws XmlPullParserException
   */
  public T parseFeed() throws IOException, XmlPullParserException {
    boolean close = true;
    try {
      feedParsed = true;
      T result = Atom.parseElement(parser, feedClass, true);
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
   * item type. If there is no item to parse, it will return {@code null} and
   * automatically close the parser (in which case there is no need to call
   * {@link #close()}.
   * 
   * @throws XmlPullParserException
   */
  public Object parseNextEntry() throws IOException, XmlPullParserException {
    XmlPullParser parser = this.parser;
    if (!this.feedParsed) {
      this.feedParsed = true;
      Atom.parseElement(parser, null, true);
    }
    boolean close = true;
    try {
      if (parser.getEventType() == XmlPullParser.START_TAG) {
        Object result = parseEntryInternal();
        parser.next();
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
    inputStream.close();
  }

  abstract Object parseEntryInternal() throws IOException,
      XmlPullParserException;
}
