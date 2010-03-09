// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;

public interface HttpTransport {
  boolean supportsPatch();

  HttpRequest buildDeleteRequest(String uri) throws IOException;
  HttpRequest buildGetRequest(String uri) throws IOException;
  HttpRequest buildPatchRequest(String uri) throws IOException;
  HttpRequest buildPostRequest(String uri) throws IOException;
  HttpRequest buildPutRequest(String uri) throws IOException;
}
