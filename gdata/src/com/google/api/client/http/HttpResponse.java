/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http;

import com.google.api.client.ArrayMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/** Encapsulates an HTTP response. */
public final class HttpResponse {

  private final boolean isSuccessStatusCode;
  private final long contentLength;
  private final int statusCode;
  private volatile InputStream content;
  private final String contentType;
  private volatile LowLevelHttpResponse response;
  private final String statusMessage;
  private final String contentEncoding;
  private final ArrayMap<String, String> headerNameToValueMap;
  private final HttpTransport transport;

  /**
   * Disables logging of content, for example if content has sensitive data such
   * as an authentication token. Defaults to {@code false}.
   */
  public volatile boolean disableContentLogging;

  HttpResponse(HttpTransport transport, LowLevelHttpResponse response) {
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
    int size = response.getHeaderCount();
    ArrayMap<String, String> headerNameToValueMap =
        this.headerNameToValueMap = ArrayMap.create(size);
    for (int i = 0; i < size; i++) {
      String headerName = response.getHeaderName(i);
      String headerValue = response.getHeaderValue(i);
      headerNameToValueMap.set(i, headerName, headerValue);
      if (loggable) {
        logger.config(headerName + ": " + headerValue);
      }
    }
  }

  public boolean isSuccessStatusCode() {
    return this.isSuccessStatusCode;
  }

  public InputStream getContent() throws IOException {
    LowLevelHttpResponse response = this.response;
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
      if (!this.disableContentLogging && logger.isLoggable(Level.CONFIG)) {
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
    getContent().close();
  }

  public <T> T parseAs(Class<T> entityClass) throws IOException {
    String contentType = getContentType();
    HttpParser parser = this.transport.getParser(contentType);
    if (parser == null) {
      throw new IllegalArgumentException("No parser defined for content type: "
          + contentType);
    }
    return parser.parse(this, entityClass);
  }

  public String parseAsString() throws IOException {
    InputStream content = getContent();
    if (content == null) {
      return null;
    }
    try {
      long contentLength = this.contentLength;
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

  /** Returns the content length or less than zero if not known. */
  public long getContentLength() {
    return this.contentLength;
  }

  /** Returns the status code. */
  public int getStatusCode() {
    return this.statusCode;
  }

  /** Returns the status message. */
  public String getStatusMessage() {
    return this.statusMessage;
  }

  /** Returns the content type. */
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
}
