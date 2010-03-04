// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.HttpSerializer;

import org.apache.http.entity.AbstractHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class GDataEntity extends AbstractHttpEntity {

  private final long contentLength;
  private final HttpSerializer serializer;

  GDataEntity(long contentLength, HttpSerializer serializer) {
    this.contentLength = contentLength;
    this.serializer = serializer;
  }

  public InputStream getContent() {
    throw new UnsupportedOperationException();
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public boolean isRepeatable() {
    return false;
  }

  public boolean isStreaming() {
    return true;
  }

  public void writeTo(OutputStream out) throws IOException {
    serializer.writeTo(out);
  }
}
