// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.Response;
import com.google.api.data.client.v2.GDataException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/** GData Atom error response to a GData Atom request. */
public final class AtomException extends GDataException {

  static final long serialVersionUID = 1;

  /** Atom client. */
  public final AtomClient client;

  /** Input stream or {@code null} for none or if already parsed. */
  public InputStream inputStream;

  /** Parser or {@code null} for none or if already parsed. */
  public XmlPullParser parser;

  AtomException(AtomClient client, Response response) {
    super(response);
    this.client = client;
  }

  /** Parse the error information into the given error information class. */
  public <T> T parseErrorInfo(Class<T> errorInfoClass) throws IOException,
      XmlPullParserException {
    return client.parseEntry(this.parser, this.inputStream, errorInfoClass);
  }

  /** Close the input stream if necessary. */
  public void close() throws IOException {
    parser = null;
    if (inputStream != null) {
      inputStream.close();
      inputStream = null;
    }
  }
}
