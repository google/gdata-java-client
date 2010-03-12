// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.net;

import com.google.api.data.client.http.HttpSerializer;
import com.google.api.data.client.http.LowLevelHttpRequestInterface;
import com.google.api.data.client.http.LowLevelHttpResponseInterface;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

final class NetHttpRequest implements LowLevelHttpRequestInterface {

  private final HttpURLConnection connection;
  private HttpSerializer serializer;

  NetHttpRequest(String requestMethod, String uri) throws IOException {
    HttpURLConnection connection =
        this.connection = (HttpURLConnection) new URL(uri).openConnection();
    connection.setRequestMethod(requestMethod);
    connection.setUseCaches(false);
  }

  public void addHeader(String name, String value) {
    this.connection.addRequestProperty(name, value);
  }

  public LowLevelHttpResponseInterface execute() throws IOException {
    HttpURLConnection connection = this.connection;
    // write content
    HttpSerializer serializer = this.serializer;
    if (serializer != null) {
      connection.setDoOutput(true);
      addHeader("Content-Type", serializer.getContentType());
      String contentEncoding = serializer.getContentEncoding();
      if (contentEncoding != null) {
        addHeader("Content-Encoding", contentEncoding);
      }
      long contentLength = serializer.getContentLength();
      if (contentLength >= 0) {
        addHeader("Content-Length", Long.toString(contentLength));
      }
      serializer.writeTo(connection.getOutputStream());
    }
    // Set the http.strictPostRedirect property to prevent redirected
    // POST/PUT/DELETE from being mapped to a GET. This
    // system property was a hack to fix a jdk bug w/out changing back
    // compat behavior. It's bogus that this is a system (and not a
    // per-connection) property, so we just change it for the duration
    // of the connection.
    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4023866
    String httpStrictPostRedirect =
        System.getProperty("http.strictPostRedirect");
    try {
      System.setProperty("http.strictPostRedirect", "true");
      connection.connect();
      return new NetHttpResponse(connection);

    } finally {
      if (httpStrictPostRedirect == null) {
        System.clearProperty("http.strictPostRedirect");
      } else {
        System.setProperty("http.strictPostRedirect", httpStrictPostRedirect);
      }
    }
  }

  public void setContent(HttpSerializer serializer) {
    this.serializer = serializer;
  }

}
