// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public final class AtomEntryResponse extends AtomResponse {
  AtomEntryResponse(AtomClient client, XmlPullParser parser,
      InputStream inputStream) {
    super(client, parser, inputStream);
  }

  public <T> T parseEntry(Class<T> destinationClass) throws IOException,
      XmlPullParserException {
    return this.client.parseEntry(this.parser, this.inputStream,
        destinationClass);
  }
}
