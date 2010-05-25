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

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Strings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * HTTP response.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class HttpResponse {

  /** HTTP response content or {@code null} before {@link #getContent()}. */
  private InputStream content;

  /** Content encoding or {@code null}. */
  public final String contentEncoding;

  /**
   * Content length or less than zero if not known. May be reset by
   * {@link #getContent()} if response had GZip compression.
   */
  private long contentLength;

  /** Content type or {@code null} for none. */
  public final String contentType;

  /**
   * HTTP headers. Note that only if a header name is used for multiple headers,
   * only the last one is retained.
   */
  public final HttpHeaders headers = new HttpHeaders();

  /** Whether received a successful status code {@code >= 200 && < 300}. */
  public final boolean isSuccessStatusCode;

  /** Low-level HTTP response. */
  private LowLevelHttpResponse response;

  /** Status code. */
  public final int statusCode;

  /** Status message or {@code null}. */
  public final String statusMessage;

  /** HTTP transport. */
  public final HttpTransport transport;

  /**
   * Whether to disable response content logging during {@link #getContent()}
   * (unless {@link Level#ALL} is loggable which forces all logging).
   * <p>
   * Useful for example if content has sensitive data such as an authentication
   * token. Defaults to {@code false}.
   */
  public boolean disableContentLogging;

  HttpResponse(HttpTransport transport, LowLevelHttpResponse response) {
    this.transport = transport;
    this.response = response;
    this.contentLength = response.getContentLength();
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
    StringBuilder logbuf = null;
    if (loggable) {
      logbuf = new StringBuilder();
      logbuf.append("-------------- RESPONSE --------------").append(
          Strings.LINE_SEPARATOR);
      String statusLine = response.getStatusLine();
      if (statusLine != null) {
        logbuf.append(statusLine);
      } else {
        logbuf.append(code);
        if (message != null) {
          logbuf.append(' ').append(message);
        }
      }
      logbuf.append(Strings.LINE_SEPARATOR);
    }
    // headers
    int size = response.getHeaderCount();
    ArrayMap<String, Object> headers = this.headers.unknownFields;
    for (int i = 0; i < size; i++) {
      String headerName = response.getHeaderName(i);
      String headerValue = response.getHeaderValue(i);
      headers.set(i, headerName, headerValue);
      if (loggable) {
        logbuf.append(headerName + ": " + headerValue).append(
            Strings.LINE_SEPARATOR);
      }
    }
    if (loggable) {
      logger.config(logbuf.toString());
    }
  }

  /**
   * Returns the content of the HTTP response.
   * <p>
   * The result is cached, so subsequent calls will be fast.
   * 
   * @return input stream content of the HTTP response or {@code null} for none
   * @throws IOException I/O exception
   */
  public InputStream getContent() throws IOException {
    LowLevelHttpResponse response = this.response;
    if (response == null) {
      return this.content;
    }
    InputStream content = this.response.getContent();
    this.response = null;
    if (content != null) {
      Logger logger = HttpTransport.LOGGER;
      boolean loggable =
          !this.disableContentLogging && logger.isLoggable(Level.CONFIG)
              || logger.isLoggable(Level.ALL);
      if (loggable) {
        byte[] debugContent = readStream(content);
        logger.config("Response size: " + debugContent.length + " bytes");
        content = new ByteArrayInputStream(debugContent);
      }
      // gzip encoding
      String contentEncoding = this.contentEncoding;
      if (contentEncoding != null && contentEncoding.contains("gzip")) {
        content = new GZIPInputStream(content);
        this.contentLength = -1;
      }
      if (loggable) {
        // print content using a buffered input stream that can be re-read
        String contentType = this.contentType;
        if (contentType != null && (contentType.startsWith("application/"))
            || contentType.startsWith("text/")) {
          byte[] debugContent = readStream(content);
          if (debugContent.length != 0) {
            logger.config(new String(debugContent));
          }
          content = new ByteArrayInputStream(debugContent);
        }
      }
      this.content = content;
    }
    return content;
  }

  /**
   * Gets the content of the HTTP response from {@link #getContent()} and
   * ignores the content if there is any.
   * 
   * @throws IOException I/O exception
   */
  public void ignore() throws IOException {
    InputStream content = getContent();
    if (content != null) {
      content.close();
    }
  }

  /**
   * Returns the HTTP response content parser to use for the content type of
   * this HTTP response or {@code null} for none.
   */
  public HttpParser getParser() {
    return this.transport.getParser(this.contentType);
  }

  /**
   * Parses the content of the HTTP response from {@link #getContent()} and
   * reads it into a data class of key/value pairs using the parser returned by
   * {@link #getParser()} .
   * 
   * @return parsed data class or {@code null} for no content
   * @throws IOException I/O exception
   * @throws IllegalArgumentException if no parser is defined for the given
   *         content type or if there is no content type defined in the HTTP
   *         response
   */
  public <T> T parseAs(Class<T> dataClass) throws IOException {
    HttpParser parser = getParser();
    if (parser == null) {
      InputStream content = getContent();
      if (this.contentType == null) {
        if (content != null) {
          throw new IllegalArgumentException(
              "Missing Content-Type header in response: " + parseAsString());
        }
        return null;
      }
      throw new IllegalArgumentException("No parser defined for Content-Type: "
          + contentType);
    }
    return parser.parse(this, dataClass);
  }

  /**
   * Parses the content of the HTTP response from {@link #getContent()} and
   * reads it into a string.
   * 
   * @return parsed string or {@code null} for no content
   * @throws IOException I/O exception
   */
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

  /**
   * Returns whether the given HTTP response status code is a success code
   * {@code >= 200 and < 300}.
   */
  public static boolean isSuccessStatusCode(int statusCode) {
    return statusCode >= 200 && statusCode < 300;
  }
}
