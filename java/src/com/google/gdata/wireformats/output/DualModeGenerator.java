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


package com.google.gdata.wireformats.output;

import com.google.gdata.model.Element;

import java.io.IOException;
import java.io.Writer;

/**
 * A bridge between old and new data models for output generators.
 * 
 * 
 * 
 * @param <T> expected source object type
 */
public abstract class DualModeGenerator<T> 
    extends WireFormatOutputGenerator<T> {

  private final CharacterGenerator<T> oldGen;
  
  protected DualModeGenerator(CharacterGenerator<T> oldGen) {
    this.oldGen = oldGen;
  }

  @Override
  public void generate(Writer contentWriter, OutputProperties outProps,
      T source) throws IOException {
    if (isNewModel(source)) {
      super.generate(contentWriter, outProps, source);
    } else {
      oldGen.generate(contentWriter, outProps, source);
    }
  }
  
  /**
   * Returns true if the response contains data in the new data model.
   */
  private boolean isNewModel(T source) {
    return source instanceof Element; 
  }
}
