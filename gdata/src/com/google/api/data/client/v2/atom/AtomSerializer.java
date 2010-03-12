// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpSerializer;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

public class AtomSerializer implements HttpSerializer {

  final NamespaceDictionary client;
  final Object entry;

  AtomSerializer(NamespaceDictionary client, Object entry) {
    this.client = client;
    this.entry = entry;
  }

  public static void setContent(HttpRequest request, NamespaceDictionary client,
      Object entry) {
    request.setContent(new AtomSerializer(client, entry));
  }

  public final long getContentLength() {
    return -1;
  }

  public final String getContentEncoding() {
    return null;
  }

  public String getContentType() {
    return Atom.CONTENT_TYPE;
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
    this.client.serializeElement(serializer, NamespaceDictionary.ATOM_NAMESPACE,
        "entry", entry);
    serializer.endDocument();
  }
}
