// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
  private final HashMap<String, String> headerNameToValueMap =
      new HashMap<String, String>();
  private final HttpTransport transport;

  HttpResponse(HttpTransport transport, LowLevelHttpResponseInterface response) {
    this.transport = transport;
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
    boolean loggable = logger.isLoggable(Level.CONFIG);
    if (loggable) {
      logger.config(response.getStatusLine());
    }
    // headers
    HashMap<String, String> headerNameToValueMap = this.headerNameToValueMap;
    int size = response.getHeaderCount();
    for (int i = 0; i < size; i++) {
      String headerName = response.getHeaderName(i);
      String headerValue = response.getHeaderValue(i);
      headerNameToValueMap.put(headerName, headerValue);
      if (loggable) {
        logger.config(headerName + ": " + headerValue);
      }
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

  public void ignore() throws IOException {
    checkForError();
    getContent().close();
  }

  public <T> T parseAs(Class<T> entityClass) throws IOException {
    checkForError();
    String contentType = getContentType();
    HttpParser parser = this.transport.getParser(contentType);
    if (parser == null) {
      throw new IllegalArgumentException("No parser defined for content type: "
          + contentType);
    }
    return parser.parse(this, entityClass);
  }

  public String parseAsString() throws IOException {
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

  /**
   * Returns the value of the named header field.
   * <p>
   * If called on a connection that sets the same header multiple times with
   * possibly different values, only the last value is returned.
   * 
   * @param name the name of a header field.
   * @return the value of the named header field, or {@code null} if there is no
   *         such field in the header.
   */
  public String getHeaderValue(String name) {
    return this.headerNameToValueMap.get(name);
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

  private void checkForError() throws IOException {
    if (!isSuccessStatusCode()) {
      String contentType = getContentType();
      HttpParser parser = this.transport.getParser(contentType);
      if (parser == null) {
        HttpResponseException exception = new HttpResponseException(this);
        exception.content = parseAsString();
        throw exception;
      }
      // should throw an exception
      parser.parse(this, Object.class);
    }
  }
}
