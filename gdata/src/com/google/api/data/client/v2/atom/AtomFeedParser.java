// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/** GData Atom feed parser when the item class is known in advance. */
public final class AtomFeedParser<T, I> extends AbstractAtomFeedParser<T> {

  private final Class<I> entryClass;

  AtomFeedParser(XmlPullParser parser, InputStream inputStream,
      Class<T> feedClass, Class<I> entryClass) {
    super(parser, inputStream, feedClass);
    this.entryClass = entryClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public I parseNextEntry() throws IOException, XmlPullParserException {
    return (I) super.parseNextEntry();
  }

  @Override
  Object parseEntryInternal() throws IOException, XmlPullParserException {
    return Atom.parseElement(parser, entryClass, false);
  }
}
