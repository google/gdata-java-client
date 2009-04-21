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


package com.google.gdata.wireformats;

import com.google.gdata.model.Element;
import com.google.gdata.util.ParseException;

import java.io.IOException;

/**
 * A parser that translates wire format content into
 * a in-memory representation.
 */
public interface WireFormatParser {

  /**
   * Parse content from a source provided to the parser at
   * construction time.
   *
   * @param element root of parsed element tree
   * @return root of parsed element tree
   * @throws IOException if content cannot be accessed
   * @throws ParseException if content cannot be parsed invalid
   * @throws ContentCreationException if content cannot be created 
   * @throws ContentValidationException if content fails metadata validation
   */
  public Element parse(Element element)
      throws IOException, ParseException, ContentCreationException,
             ContentValidationException;

}
