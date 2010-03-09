// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

final class GZipHttpSerializer implements HttpSerializer {
  private static final long MIN_GZIP_BYTES = 256;

  private final HttpSerializer httpSerializer;

  public final boolean isGZiped;

  public final String contentEncoding;
  public final String contentType;

  private final long contentLength;

  GZipHttpSerializer(HttpSerializer httpSerializer) {
    this.httpSerializer = httpSerializer;
    String contentEncoding = httpSerializer.getContentEncoding();
    long contentLength = this.contentLength = httpSerializer.getContentLength();
    String contentType = this.contentType = httpSerializer.getContentType();
    boolean isGZiped =
        this.isGZiped =
            !Transport.DISABLE_GZIP && contentLength >= MIN_GZIP_BYTES
                && contentEncoding == null
                && LogHttpSerializer.isTextualContentType(contentType);
    if (isGZiped) {
      contentEncoding = "gzip";
    }
    this.contentEncoding = contentEncoding;
  }

  public void writeTo(OutputStream out) throws IOException {
    HttpSerializer httpSerializer = this.httpSerializer;
    if (this.isGZiped) {
      GZIPOutputStream zipper = new GZIPOutputStream(out);
      httpSerializer.writeTo(zipper);
      zipper.finish();
    } else {
      httpSerializer.writeTo(out);
    }
  }

  public String getContentEncoding() {
    return this.contentEncoding;
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public String getContentType() {
    return this.contentType;
  }
}
