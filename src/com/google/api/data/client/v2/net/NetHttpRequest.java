// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.net;

import com.google.api.data.client.v2.HttpRequest;
import com.google.api.data.client.v2.HttpResponse;
import com.google.api.data.client.v2.HttpSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class NetHttpRequest implements HttpRequest {

  private final HttpURLConnection connection;

  NetHttpRequest(String requestMethod, String uri) throws IOException {
    HttpURLConnection connection =
        this.connection = (HttpURLConnection) new URL(uri).openConnection();
    connection.setRequestMethod(requestMethod);
  }

  public void addHeader(String name, String value) {
    this.connection.addRequestProperty(name, value);
  }

  public HttpResponse execute() throws IOException {
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

  public void setContent(long contentLength, String contentType,
      String contentEncoding, HttpSerializer serializer) {
    // TODO Auto-generated method stub
    
  }

  public void setContent(long contentLength, String contentType,
      InputStream inputStream) {
    // TODO Auto-generated method stub
    
  }

}
