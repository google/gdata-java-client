// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator.linewrap;

/**
 * Computes line wrapping on a section of text by inserting line separators and
 * updating the indentation as necessary. Implementations of this interface are
 * thread-safe.
 */
public interface LineWrapper {

  /** Computes line wrapping result for the given text. */
  String compute(String text);
}
