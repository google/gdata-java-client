// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;

public interface LowLevelHttpTransportInterface {
  boolean supportsPatch();

  LowLevelHttpRequestInterface buildDeleteRequest(String uri) throws IOException;
  LowLevelHttpRequestInterface buildGetRequest(String uri) throws IOException;
  LowLevelHttpRequestInterface buildPatchRequest(String uri) throws IOException;
  LowLevelHttpRequestInterface buildPostRequest(String uri) throws IOException;
  LowLevelHttpRequestInterface buildPutRequest(String uri) throws IOException;
}
