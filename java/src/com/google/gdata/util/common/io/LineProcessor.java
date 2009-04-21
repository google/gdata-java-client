/* Copyright (c) 2008 Google Inc.
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

package com.google.gdata.util.common.io;

import java.io.IOException;

/**
 * A callback to be used with the streaming {@code readLines} methods.
 *
 * <p>{@link #processLine} will be called for each line that is read, and
 * should return {@code false} when you want to stop processing.
 *
 * 
 * @param <T> the type of the result of processing the lines seen
 * @see Characters#readLines(InputSupplier, LineProcessor)
 * @see Files#readLines(java.io.File, java.nio.charset.Charset, LineProcessor)
 * @see Resources#readLines(java.net.URL, java.nio.charset.Charset, LineProcessor)
 */
public interface LineProcessor<T> {

  /**
   * This method will be called once for each line.
   *
   * @return true if we want to continue processing, false if we want to stop 
   */
  boolean processLine(String line) throws IOException;

  /**
   * @return the result of processing all of the lines seen
   */
  T getResult();
}
