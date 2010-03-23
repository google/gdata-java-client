// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.LineWrapper;

import java.io.PrintWriter;

/**
 * Defines a single file generator, which manages a single generated file.
 */
interface FileGenerator {

  /** Whether to generate this file. */
  boolean isGenerated();
  
  /** Generates the content of the file into the given print writer. */
  void generate(PrintWriter out);

  /**
   * Returns the output file path relative to the root gdata output
   * directory.
   */
  String getOutputFilePath();
  
  /** Returns the line wrapper to use or {@code null} for none. */
  LineWrapper getLineWrapper();
}
