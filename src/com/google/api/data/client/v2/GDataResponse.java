// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.InputStream;

public interface GDataResponse {

  boolean isSuccessStatusCode();

  InputStream getContent() throws IOException;

  long getContentLength();

  int getStatusCode();

  String getReasonPhrase();

  String getContentType();

}
