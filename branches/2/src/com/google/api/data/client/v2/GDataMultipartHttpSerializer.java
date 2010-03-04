package com.google.api.data.client.v2;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class GDataMultipartHttpSerializer implements HttpSerializer {

  private static final byte[] CR_LF = "\r\n".getBytes();
  private static final byte[] HEADER = "Media multipart posting".getBytes();
  private static final byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
  private static final byte[] CONTENT_TRANSFER_ENCODING =
      "Content-Transfer-Encoding: binary".getBytes();
  private static final byte[] TWO_DASHES = "--".getBytes();
  private static final byte[] END_OF_PART = "END_OF_PART".getBytes();

  private final byte[] metadataContentTypeBytes;
  private final byte[] mediaTypeBytes;
  private final GDataSerializer metadata;
  private final InputStream mediaContent;
  final long length;

  // TODO: is gzip allowed here?
  static GDataMultipartHttpSerializer create(GDataSerializer metadata,
      String metadataContentType, String mediaType, InputStream mediaContent,
      long mediaContentLength) {
    byte[] metadataContentTypeBytes = metadataContentType.getBytes();
    byte[] mediaTypeBytes = mediaType.getBytes();
    long length = -1;
    long metadataLength = metadata.getContentLength();
    if (metadataLength >= 0) {
      length =
          metadataLength + mediaContentLength + mediaTypeBytes.length
              + metadataContentTypeBytes.length + HEADER.length + 2
              * CONTENT_TYPE.length + CONTENT_TRANSFER_ENCODING.length + 3
              * END_OF_PART.length + 10 * CR_LF.length + 4 * TWO_DASHES.length;
    }
    return new GDataMultipartHttpSerializer(metadata, metadataContentTypeBytes,
        length, mediaTypeBytes, mediaContent);
  }

  private GDataMultipartHttpSerializer(GDataSerializer metadata,
      byte[] metadataContentTypeBytes, long length, byte[] mediaTypeBytes,
      InputStream mediaContent) {
    this.length = length;
    this.metadata = metadata;
    this.mediaContent = mediaContent;
    this.metadataContentTypeBytes = metadataContentTypeBytes;
    this.mediaTypeBytes = mediaTypeBytes;
  }

  public void writeTo(OutputStream out) throws IOException {
    out.write(HEADER);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(CR_LF);
    out.write(CONTENT_TYPE);
    out.write(metadataContentTypeBytes);
    out.write(CR_LF);
    out.write(CR_LF);
    metadata.serialize(out);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(CR_LF);
    out.write(CONTENT_TYPE);
    out.write(mediaTypeBytes);
    out.write(CR_LF);
    out.write(CONTENT_TRANSFER_ENCODING);
    out.write(CR_LF);
    out.write(CR_LF);
    InputStream mediaContent = this.mediaContent;
    try {
      byte[] tmp = new byte[4096];
      int bytesRead;
      while ((bytesRead = mediaContent.read(tmp)) != -1) {
        out.write(tmp, 0, bytesRead);
      }
    } finally {
      mediaContent.close();
    }
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(TWO_DASHES);
    out.flush();
  }

}
