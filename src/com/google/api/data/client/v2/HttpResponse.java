// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.InputStream;

public interface HttpResponse {

  InputStream getContent() throws IOException;

  String getContentEncoding();

  long getContentLength();

  String getContentType();

  String getStatusLine();
  
  int getStatusCode() throws IOException;

  String getReasonPhrase() throws IOException;

}
