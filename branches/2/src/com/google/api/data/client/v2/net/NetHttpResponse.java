// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.net;

import com.google.api.data.client.v2.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

final class NetHttpResponse implements HttpResponse {

  private final HttpURLConnection connection;

  NetHttpResponse(HttpURLConnection connection) {
    this.connection = connection;
  }

  public int getStatusCode() throws IOException {
    return this.connection.getResponseCode();
  }

  public InputStream getContent() throws IOException {
    return this.connection.getInputStream();
  }

  public String getContentEncoding() {
    return this.connection.getContentEncoding();
  }

  public long getContentLength() {
    return Long.parseLong(this.connection.getHeaderField("Content-Length"));
  }

  public String getContentType() {
    return this.connection.getHeaderField("Content-Type");
  }

  public String getReasonPhrase() throws IOException {
    return this.connection.getResponseMessage();
  }

  public String getStatusLine() {
    String result = this.connection.getHeaderField(0);
    return result != null && result.startsWith("HTTP/1.") ? result : null;
  }

}
