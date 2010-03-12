// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;
import java.io.OutputStream;

/** Serializes content into an output stream. */
public interface HttpSerializer {

  /** Returns the content length or less than zero if not known. */
  long getContentLength();

  /**
   * Returns the content encoding (for example {@code "gzip"}) or {@code null}
   * for none.
   */
  String getContentEncoding();

  /** Returns the content type. */
  String getContentType();

  /** Writes the content to the given output stream. */
  void writeTo(OutputStream out) throws IOException;
}
