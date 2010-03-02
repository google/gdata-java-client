// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.GDataSerializer;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

class AtomSerializer implements GDataSerializer {

  final AtomClient client;
  final Object entry;

  AtomSerializer(AtomClient client, Object entry) {
    this.client = client;
    this.entry = entry;
  }

  public final long getContentLength() {
    return -1;
  }

  public final void serialize(OutputStream out) throws IOException {
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
    this.client.serializeElement(serializer, AtomClient.ATOM_NAMESPACE, "entry", entry);
    serializer.endDocument();
  }
}
