// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;

public interface LowLevelHttpRequestInterface {

  void addHeader(String name, String value);

  void setContent(HttpSerializer serializer);

  LowLevelHttpResponseInterface execute() throws IOException;
}
