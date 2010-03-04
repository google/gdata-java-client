package com.google.api.data.client.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

final class GDataHttpSerializer implements HttpSerializer {
  private static final long MIN_GZIP_BYTES = 256;

  private final boolean isGZiped;
  private final GDataSerializer content;

  private final String contentType;

  final long contentLength;

  final String contentEncoding;

  static GDataHttpSerializer create(GDataSerializer content, String contentType) {
    long contentLength = content.getContentLength();
    return new GDataHttpSerializer(content, contentLength,
        contentLength >= MIN_GZIP_BYTES, contentType);
  }

  private GDataHttpSerializer(GDataSerializer content, long contentLength,
      boolean isGZiped, String contentType) {
    this.content = content;
    this.contentLength = contentLength;
    this.isGZiped = isGZiped;
    this.contentType = contentType;
    this.contentEncoding = isGZiped ? "gzip" : null;
  }

  public void writeTo(OutputStream out) throws IOException {
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG) && this.contentLength != 0
        && this.contentType != null
        && (this.contentType.startsWith("application/"))
        || this.contentType.startsWith("text/")) {
      ByteArrayOutputStream debugStream = new ByteArrayOutputStream();
      content.serialize(debugStream);
      byte[] debugContent = debugStream.toByteArray();
      logger.config(new String(debugContent));
      out.write(debugContent);
    } else if (isGZiped) {
      GZIPOutputStream zipper = new GZIPOutputStream(out);
      content.serialize(zipper);
      zipper.finish();
    } else {
      content.serialize(out);
    }
  }

}
