// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.net;

import com.google.api.data.client.v2.HttpTransport;

import java.io.IOException;

final class NetHttpTransport implements HttpTransport {

  NetHttpTransport() {
  }

  public boolean supportsPatch() {
    return false;
  }

  public NetHttpRequest deleteRequest(String uri) throws IOException {
    return new NetHttpRequest("DELETE", uri);
  }

  public NetHttpRequest getRequest(String uri) throws IOException {
    return new NetHttpRequest("GET", uri);
  }

  public NetHttpRequest patchRequest(String uri) throws IOException {
    return new NetHttpRequest("PATCH", uri);
  }

  public NetHttpRequest postRequest(String uri) throws IOException {
    return new NetHttpRequest("POST", uri);
  }

  public NetHttpRequest putRequest(String uri) throws IOException {
    return new NetHttpRequest("PUT", uri);
  }
}
