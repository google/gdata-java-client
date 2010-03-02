// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import org.apache.http.entity.AbstractHttpEntity;

import java.io.InputStream;

abstract class GDataEntity extends AbstractHttpEntity {

  private final long contentLength;

  GDataEntity(long contentLength) {
    this.contentLength = contentLength;
  }

  public final InputStream getContent() {
    throw new UnsupportedOperationException();
  }

  public final long getContentLength() {
    return this.contentLength;
  }

  public final boolean isRepeatable() {
    return false;
  }

  public final boolean isStreaming() {
    return true;
  }
}
