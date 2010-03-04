// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.InputStream;

/** GData error response to a GData request. */
public class GDataException extends Exception {

  static final long serialVersionUID = 1;

  /** Error status code of the response. */
  public final int errorCode;

  /** Error message of the response. */
  public final String errorMessage;

  private String content;

  /**
   * @param response GData response
   */
  public GDataException(GDataResponse response) {
    this.errorCode = response.getStatusCode();
    this.errorMessage = response.getStatusMessage();
  }

  /**
   * Parses the message from the given input stream which is assumed to be the
   * body of the response.
   * 
   * @param inputStream input stream or {@code null} to reset message to {@code
   *        null}
   */
  public void parseContent(InputStream inputStream, long contentLength)
      throws IOException {
    this.content = null;
    if (inputStream != null) {
      try {
        int bufferSize = contentLength == -1 ? 4096 : (int) contentLength;
        int length = 0;
        byte[] buffer = new byte[bufferSize];
        byte[] tmp = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(tmp)) != -1) {
          if (length + bytesRead > bufferSize) {
            bufferSize = Math.max(bufferSize << 1, length + bytesRead);
            byte[] newbuffer = new byte[bufferSize];
            System.arraycopy(buffer, 0, newbuffer, 0, length);
            buffer = newbuffer;
          }
          System.arraycopy(tmp, 0, buffer, length, bytesRead);
          length += bytesRead;
        }
        this.content = new String(buffer, 0, length);
      } finally {
        inputStream.close();
      }
    }
  }

  @Override
  public String getMessage() {
    StringBuilder messageBuilder = new StringBuilder().append(this.errorCode);
    String reasonPhrase = this.errorMessage;
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
