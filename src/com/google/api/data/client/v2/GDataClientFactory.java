// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

public interface GDataClientFactory {
  GDataClient createClient(String contentType, String applicationName,
      String authToken, String version);
}
