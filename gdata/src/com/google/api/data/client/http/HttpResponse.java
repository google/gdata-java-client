// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public final class HttpResponse {

  private final boolean isSuccessStatusCode;
  private final long contentLength;
  private final int statusCode;
  private InputStream content;
  private String contentType;
  private LowLevelHttpResponseInterface response;
  private final String statusMessage;
  private final String contentEncoding;

  HttpResponse(LowLevelHttpResponseInterface response) {
    this.response = response;
    long contentLength = this.contentLength = response.getContentLength();
    String contentType = this.contentType = response.getContentType();
    String contentEncoding =
        this.contentEncoding = response.getContentEncoding();
    int code = response.getStatusCode();
    this.statusCode = code;
    this.isSuccessStatusCode = isSuccessStatusCode(code);
    String message = response.getReasonPhrase();
    this.statusMessage = message;
    Logger logger = HttpTransport.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      logger.config(response.getStatusLine());
      debugContentMetadata(logger, contentType, contentEncoding, contentLength);
    }
  }

  static void debugContentMetadata(Logger logger, String contentType,
      String contentEncoding, long contentLength) {
    logger.config("Content-Type: " + contentType);
    if (contentEncoding != null) {
      logger.config("Content-Encoding: " + contentEncoding);
    }
    if (contentLength >= 0) {
      logger.config("Content-Length: " + contentLength);
    }
  }

  public boolean isSuccessStatusCode() {
    return this.isSuccessStatusCode;
  }

  public InputStream getContentNoLogging() throws IOException {
    return getContent(false);
  }

  public InputStream getContent() throws IOException {
    return getContent(true);
  }

  private InputStream getContent(boolean logging) throws IOException {
    LowLevelHttpResponseInterface response = this.response;
    if (response == null) {
      return this.content;
    }
    InputStream content = this.response.getContent();
    this.response = null;
    if (content != null) {
      Logger logger = HttpTransport.LOGGER;
      if (logger.isLoggable(Level.CONFIG)) {
        byte[] debugContent = readStream(content);
        logger.config("Response size: " + debugContent.length + " bytes");
        content = new ByteArrayInputStream(debugContent);
      }
      // gzip encoding
      String contentEncoding = this.contentEncoding;
      if (contentEncoding != null && contentEncoding.contains("gzip")) {
        content = new GZIPInputStream(content);
      }
      if (logging && logger.isLoggable(Level.CONFIG)) {
        // print content using a buffered input stream that can be re-read
        String contentType = this.contentType;
        if (this.contentLength != 0 && contentType != null
            && (contentType.startsWith("application/"))
            || contentType.startsWith("text/")) {
          byte[] debugContent = readStream(content);
          logger.config(new String(debugContent));
          content = new ByteArrayInputStream(debugContent);
        }
      }
      this.content = content;
    }
    return content;
  }

  public String parseContentAsString() throws IOException {
    return parseContentAsString(getContent(), this.contentLength);
  }

  public static String parseContentAsString(InputStream content,
      long contentLength) throws IOException {
    if (content == null) {
      return null;
    }
    try {
      int bufferSize = contentLength == -1 ? 4096 : (int) contentLength;
      int length = 0;
      byte[] buffer = new byte[bufferSize];
      byte[] tmp = new byte[4096];
      int bytesRead;
      while ((bytesRead = content.read(tmp)) != -1) {
        if (length + bytesRead > bufferSize) {
          bufferSize = Math.max(bufferSize << 1, length + bytesRead);
          byte[] newbuffer = new byte[bufferSize];
          System.arraycopy(buffer, 0, newbuffer, 0, length);
          buffer = newbuffer;
        }
        System.arraycopy(tmp, 0, buffer, length, bytesRead);
        length += bytesRead;
      }
      return new String(buffer, 0, length);
    } finally {
      content.close();
    }
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getStatusMessage() {
    return this.statusMessage;
  }

  public String getContentType() {
    return this.contentType;
  }

  private static byte[] readStream(InputStream content) throws IOException {
    ByteArrayOutputStream debugStream = new ByteArrayOutputStream();
    try {
      byte[] tmp = new byte[4096];
      int bytesRead;
      while ((bytesRead = content.read(tmp)) != -1) {
        debugStream.write(tmp, 0, bytesRead);
      }
    } finally {
      content.close();
    }
    return debugStream.toByteArray();
  }

  public static boolean isSuccessStatusCode(int statusCode) {
    return statusCode >= 200 && statusCode < 300;
  }
}
