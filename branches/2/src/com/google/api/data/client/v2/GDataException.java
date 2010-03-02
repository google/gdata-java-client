// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;

/** GData error response to a GData request. */
public class GDataException extends Exception {

  /** Error status code of the response. */
  public final int statusCode;

  /** Reason phrase of the response. */
  public final String reasonPhrase;

  private String content;

  /**
   * @param response GData response
   */
  public GDataException(GDataResponse response) {
    this.statusCode = response.getStatusCode();
    this.reasonPhrase = response.getReasonPhrase();
  }

  /**
   * Parses the message from the given input stream which is assumed to be the
   * body of the response.
   * 
   * @param inputStream input stream or {@code null} to reset message to {@code
   *        null}
   */
  public void parseContent(InputStream inputStream) throws IOException {
    this.content = null;
    if (inputStream != null) {
      try {
        ByteArrayBuffer buffer = new ByteArrayBuffer(4096);
        byte[] tmp = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(tmp)) != -1) {
          buffer.append(tmp, 0, bytesRead);
        }
        this.content = new String(buffer.buffer(), 0, buffer.length());
      } finally {
        inputStream.close();
      }
    }
  }

  @Override
  public String getMessage() {
    StringBuilder messageBuilder = new StringBuilder().append(this.statusCode);
    String reasonPhrase = this.reasonPhrase;
    if (reasonPhrase != null) {
      messageBuilder.append(' ').append(reasonPhrase);
    }
    String message = this.content;
    if (message != null) {
      messageBuilder.append(' ').append(message);
    }
    return messageBuilder.toString();
  }
}
