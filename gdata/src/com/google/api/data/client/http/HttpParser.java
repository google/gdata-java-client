// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;

/** Parses an HTTP response into an entity object. */
public interface HttpParser {

  /** Returns the content type. */
  String getContentType();

  /**
   * Parses the given HTTP response into a new instance of the the given entity
   * class. How the parsing is performed is not restricted by this interface,
   * and is instead defined by the concrete implementation.
   */
  <T> T parse(HttpResponse response, Class<T> entityClass) throws IOException;
}
