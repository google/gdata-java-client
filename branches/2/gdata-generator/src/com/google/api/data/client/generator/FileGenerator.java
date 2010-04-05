/* Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
