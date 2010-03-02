// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serializer into an output stream.
 */
public interface GDataSerializer {

  /**
   * Returns the number of bytes of the content if known, or a negative number
   * if unknown.
   */
  long getContentLength();

  /**
   * Serializes to the provided output stream.
   * 
   * @throws IOException exception thrown trying to write to the output stream
   */
  void serialize(OutputStream out) throws IOException;
}
