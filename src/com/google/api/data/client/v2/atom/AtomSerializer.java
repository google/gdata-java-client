// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.HttpSerializer;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

class AtomSerializer implements HttpSerializer {

  final AtomClient client;
  final Object entry;

  AtomSerializer(AtomClient client, Object entry) {
    this.client = client;
    this.entry = entry;
  }

  public final long getContentLength() {
    return -1;
  }

  public final String getContentEncoding() {
    return null;
  }

  public String getContentType() {
    return AtomClient.CONTENT_TYPE;
  }

  public final void writeTo(OutputStream out) throws IOException {
    XmlSerializer serializer;
    try {
      serializer = this.client.createSerializer();
    } catch (XmlPullParserException e) {
      throw new IllegalStateException(e);
    }
    serializer.setOutput(out, "UTF-8");
    serialize(serializer);
  }

  void serialize(XmlSerializer serializer) throws IOException {
    serializeEntry(serializer, this.entry);
  }

  void serializeEntry(XmlSerializer serializer, Object entry)
      throws IOException {
    serializer.startDocument(null, null);
    this.client.setNamespacePrefixes(serializer);
    this.client.serializeElement(serializer, AtomClient.ATOM_NAMESPACE,
        "entry", entry);
    serializer.endDocument();
  }
}
