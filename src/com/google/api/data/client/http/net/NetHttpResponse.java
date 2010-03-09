// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.net;

import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

final class NetHttpResponse implements HttpResponse {

  private final HttpURLConnection connection;
  private final int responseCode;
  private final String responseMessage;

  NetHttpResponse(HttpURLConnection connection) throws IOException {
    this.connection = connection;
    this.responseCode = connection.getResponseCode();
    this.responseMessage = connection.getResponseMessage();
  }

  public int getStatusCode() {
    return responseCode;
  }

  public InputStream getContent() throws IOException {
    HttpURLConnection connection = this.connection;
    return Response.isSuccessStatusCode(responseCode) ? connection
        .getInputStream() : connection.getErrorStream();
  }

  public String getContentEncoding() {
    return this.connection.getContentEncoding();
  }

  public long getContentLength() {
    String string = this.connection.getHeaderField("Content-Length");
    return string == null ? -1 : Long.parseLong(string);
  }

  public String getContentType() {
    return this.connection.getHeaderField("Content-Type");
  }

  public String getReasonPhrase() {
    return this.responseMessage;
  }

  public String getStatusLine() {
    String result = this.connection.getHeaderField(0);
    return result != null && result.startsWith("HTTP/1.") ? result : null;
  }

}
