// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.GDataSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

final class GDataSerializerEntity extends GDataEntity {

  private static final long MIN_GZIP_BYTES = 256;

  private final boolean isGZiped;
  private final GDataSerializer content;

  static GDataSerializerEntity create(GDataSerializer content,
      String contentType) {
    long contentLength = content.getContentLength();
    return new GDataSerializerEntity(content, contentLength,
        contentLength >= MIN_GZIP_BYTES, contentType);
  }

  private GDataSerializerEntity(GDataSerializer content, long contentLength,
      boolean isGZiped, String contentType) {
    super(contentLength);
    this.content = content;
    this.isGZiped = isGZiped;
    if (isGZiped) {
      setContentEncoding("gzip");
    }
    setContentType(contentType);
  }

  public void writeTo(OutputStream outstream) throws IOException {
    Logger logger = ApacheGData.LOGGER;
    if (logger.isLoggable(Level.CONFIG) && getContentLength() != 0
        && getContentType().getValue() != null
        && (getContentType().getValue().startsWith("application/"))
        || getContentType().getValue().startsWith("text/")) {
      ByteArrayOutputStream debugStream = new ByteArrayOutputStream();
      content.serialize(debugStream);
      byte[] debugContent = debugStream.toByteArray();
      logger.config(new String(debugContent));
      outstream.write(debugContent);
    } else if (isGZiped) {
      GZIPOutputStream zipper = new GZIPOutputStream(outstream);
      content.serialize(zipper);
      zipper.finish();
    } else {
      content.serialize(outstream);
    }
  }

}
