// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;

public abstract class AtomResponse {
  public final AtomClient client;
  public final XmlPullParser parser;
  final InputStream inputStream;

  AtomResponse(AtomClient client, XmlPullParser parser, InputStream inputStream) {
    this.client = client;
    this.parser = parser;
    this.inputStream = inputStream;
  }

  public void close() throws IOException {
    this.inputStream.close();
  }
}
