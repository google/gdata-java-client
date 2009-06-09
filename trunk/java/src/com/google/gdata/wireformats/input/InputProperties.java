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
package com.google.gdata.wireformats.input;

import com.google.gdata.wireformats.StreamProperties;

/**
 * The InputProperties interface represents properties of a input data being
 * parsed.
 *
 * @see InputParser
 */
public interface InputProperties extends StreamProperties {
  
  /**
   * Returns the expected root type that will be produced as a result of parsing
   * the content.   The parse result will be of this type or a subtype that
   * extends or implements the root type.
   */
  public Class<?> getRootType();
}
