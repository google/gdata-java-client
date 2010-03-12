// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.auth;

public interface AuthorizedRequest {
  String getUri();
  int getHeaderCount();
  String getHeaderName(int index);
  String getHeaderValue(int index);
}
