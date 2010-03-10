// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

final class LogHttpSerializer implements HttpSerializer {

  private final HttpSerializer httpSerializer;
  private final String contentType;
  private final String contentEncoding;
  private final long contentLength;

  LogHttpSerializer(HttpSerializer httpSerializer) {
    this.httpSerializer = httpSerializer;
    this.contentType = httpSerializer.getContentType();
    this.contentLength = httpSerializer.getContentLength();
    this.contentEncoding = httpSerializer.getContentEncoding();
  }

  public void writeTo(OutputStream out) throws IOException {
    Logger logger = HttpTransport.LOGGER;
    HttpSerializer httpSerializer = this.httpSerializer;
    if (logger.isLoggable(Level.CONFIG) && this.contentLength != 0
        && this.contentEncoding == null
        && isTextualContentType(this.contentType)) {
      ByteArrayOutputStream debugStream = new ByteArrayOutputStream();
      httpSerializer.writeTo(debugStream);
      byte[] debugContent = debugStream.toByteArray();
      logger.config(new String(debugContent));
      out.write(debugContent);
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

  static boolean isTextualContentType(String contentType) {
    return contentType != null && (contentType.startsWith("application/"))
        || contentType.startsWith("text/");
  }
}
