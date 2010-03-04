// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;


public interface HttpTransport {
  boolean supportsPatch();

  HttpRequest deleteRequest(String uri) throws IOException;
  HttpRequest getRequest(String uri) throws IOException;
  HttpRequest patchRequest(String uri) throws IOException;
  HttpRequest postRequest(String uri) throws IOException;
  HttpRequest putRequest(String uri) throws IOException;
}
