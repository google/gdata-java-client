// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.net;

import com.google.api.data.client.http.HttpTransport;

import java.io.IOException;

final class NetHttpTransport implements HttpTransport {

  NetHttpTransport() {
  }

  public boolean supportsPatch() {
    return false;
  }

  public NetHttpRequest buildDeleteRequest(String uri) throws IOException {
    return new NetHttpRequest("DELETE", uri);
  }

  public NetHttpRequest buildGetRequest(String uri) throws IOException {
    return new NetHttpRequest("GET", uri);
  }

  public NetHttpRequest buildPatchRequest(String uri) throws IOException {
    return new NetHttpRequest("PATCH", uri);
  }

  public NetHttpRequest buildPostRequest(String uri) throws IOException {
    return new NetHttpRequest("POST", uri);
  }

  public NetHttpRequest buildPutRequest(String uri) throws IOException {
    return new NetHttpRequest("PUT", uri);
  }
}
