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

import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for generating GData output.
 * 
 * 
 * 
 * 
 * @param <S> source data type that will be used as the input to the generator
 */
public interface OutputGenerator<S> {
  
  /**
   * Returns the alternate representation format produced the generator.
   */
  public AltFormat getAltFormat();
  
  /**
   * Returns the {@link Class} that represents the expected source data type for
   * output generation.
   */
  public Class<S> getSourceType();
  
  /**
   * Generates content to the output stream based upon the provided
   * request/response.
   *
   * @param contentStream the target stream for content generation.
   * @param outProps output properties for the generated output
   * @param source source object for output generation
   */
  public void generate(OutputStream contentStream, OutputProperties outProps,
      S source) throws IOException;
}
