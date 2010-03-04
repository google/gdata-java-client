// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.InputStream;

public interface HttpRequest {

  void addHeader(String name, String value);

  void setContent(long contentLength, String contentType,
      String contentEncoding, HttpSerializer serializer);

  void setContent(long contentLength, String contentType,
      InputStream inputStream);

  HttpResponse execute() throws IOException;
}
