// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpResponseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/** GData Atom error response to a GData Atom request. */
public final class AtomException extends HttpResponseException {

  static final long serialVersionUID = 1;

  /** Input stream or {@code null} for none or if already parsed. */
  public InputStream inputStream;

  /** Parser or {@code null} for none or if already parsed. */
  public XmlPullParser parser;

  AtomException(HttpResponse response) {
    super(response);
  }

  /** Parse the error information into the given error information class. */
  public <T> T parseError(Class<T> errorInfoClass) throws IOException,
      XmlPullParserException {
    return Atom.parse(this.parser, this.inputStream, errorInfoClass);
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
