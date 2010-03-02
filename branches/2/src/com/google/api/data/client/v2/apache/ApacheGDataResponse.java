// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.GDataResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

final class ApacheGDataResponse implements GDataResponse {
  private final long contentLength;
  private final int statusCode;
  private InputStream content;
  private final String reasonPhrase;
  private String contentType;
  private HttpEntity entity;

  ApacheGDataResponse(HttpResponse response) {
    StatusLine statusLine = response.getStatusLine();
    this.statusCode = statusLine.getStatusCode();
    this.reasonPhrase = statusLine.getReasonPhrase();
    HttpEntity entity = response.getEntity();
    this.entity = entity;
    if (entity != null) {
      Header contentTypeHeader = entity.getContentType();
      if (contentTypeHeader != null) {
        this.contentType = contentTypeHeader.getValue();
      }
      this.contentLength = entity.getContentLength();
    } else {
      this.contentLength = -1;
    }
    Logger logger = ApacheGData.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      logger.config(response.getStatusLine().toString());
      if (entity != null) {
        logger.config(entity.getContentType().toString());
        if (entity.getContentEncoding() != null) {
          logger.config(entity.getContentEncoding().toString());
        }
      }
    }
  }

  public boolean isSuccessStatusCode() {
    int statusCode = this.statusCode;
    return statusCode >= 200 && statusCode < 300;
  }

  public InputStream getContent() throws IOException {
    InputStream content = this.content;
    HttpEntity entity = this.entity;
    if (entity != null) {
      content = entity.getContent();
      Logger logger = ApacheGData.LOGGER;
      if (logger.isLoggable(Level.CONFIG)) {
        byte[] debugContent = readStream(content);
        logger.config("Response size: " + debugContent.length + " bytes");
        content = new ByteArrayInputStream(debugContent);
      }
      // gzip encoding
      Header header = entity.getContentEncoding();
      if (header != null) {
        String contentEncoding = header.getValue();
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
          content = new GZIPInputStream(content);
        }
      }
      if (logger.isLoggable(Level.CONFIG)) {
        // print content using a buffered input stream that can be re-read
        String contentType = this.contentType;
        if (entity.getContentLength() != 0 && contentType != null
            && (contentType.startsWith("application/"))
            || contentType.startsWith("text/")) {
          byte[] debugContent = readStream(content);
          logger.config(new String(debugContent));
          content = new ByteArrayInputStream(debugContent);
        }
      }
      this.entity = null;
    }
    return content;
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getReasonPhrase() {
    return this.reasonPhrase;
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

}
