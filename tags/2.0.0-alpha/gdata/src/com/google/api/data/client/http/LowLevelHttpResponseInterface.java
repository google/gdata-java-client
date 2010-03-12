// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;
import java.io.InputStream;

public interface LowLevelHttpResponseInterface {

  InputStream getContent() throws IOException;

  String getContentEncoding();

  long getContentLength();

  String getContentType();

  String getStatusLine();
  
  int getStatusCode();

  String getReasonPhrase();

}
