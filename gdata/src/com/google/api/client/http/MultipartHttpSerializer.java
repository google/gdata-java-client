package com.google.api.client.http;

import java.io.IOException;
import java.io.OutputStream;

public final class MultipartHttpSerializer implements HttpSerializer {

  // TODO: test it!

  private static final byte[] CR_LF = "\r\n".getBytes();
  private static final byte[] HEADER = "Media multipart posting".getBytes();
  private static final byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
  private static final byte[] CONTENT_TRANSFER_ENCODING =
      "Content-Transfer-Encoding: binary".getBytes();
  private static final byte[] TWO_DASHES = "--".getBytes();
  private static final byte[] END_OF_PART = "END_OF_PART".getBytes();

  private final byte[] metadataContentTypeBytes;
  private final byte[] mediaTypeBytes;
  private final HttpSerializer metadata;
  private final HttpSerializer content;
  private final long length;

  public MultipartHttpSerializer(HttpSerializer metadata, HttpSerializer content) {
    byte[] metadataContentTypeBytes = metadata.getContentType().getBytes();
    byte[] mediaTypeBytes = metadata.getContentType().getBytes();
    long metadataLength = metadata.getContentLength();
    this.length =
        metadataLength + content.getContentLength() + mediaTypeBytes.length
            + metadataContentTypeBytes.length + HEADER.length + 2
            * CONTENT_TYPE.length + CONTENT_TRANSFER_ENCODING.length + 3
            * END_OF_PART.length + 10 * CR_LF.length + 4 * TWO_DASHES.length;
    this.metadata = metadata;
    this.content = content;
    this.metadataContentTypeBytes = metadataContentTypeBytes;
    this.mediaTypeBytes = mediaTypeBytes;
  }

  public void forRequest(HttpRequest request) {
    request.headers.put("MIME-version", "1.0");
    request.setContent(this);
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
    metadata.writeTo(out);
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
    content.writeTo(out);
    out.write(CR_LF);
    out.write(TWO_DASHES);
    out.write(END_OF_PART);
    out.write(TWO_DASHES);
    out.flush();
  }

  public String getContentEncoding() {
    return null;
  }

  public long getContentLength() {
    return this.length;
  }

  public String getContentType() {
    return "multipart/related; boundary=\"END_OF_PART\"";
  }

}
