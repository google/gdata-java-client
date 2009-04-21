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

import com.google.gdata.wireformats.StreamPropertiesBuilder;

/**
 * The OutputPropertiesBuilder class is a builder class that aids in the
 * construction of new {@link OutputProperties} instances.
 * 
 * 
 */
public class OutputPropertiesBuilder 
    extends StreamPropertiesBuilder<OutputPropertiesBuilder> {

  /**
   * Constructs a new OutputPropertiesBuilder with no properties set.
   */
  public OutputPropertiesBuilder() {
  }

  /**
   * Constructs a new OutputPropertiesBuilder with properties set from an
   * existing {@link OutputProperties} instance.
   * 
   * @param source output properties instance to copy from
   */
  public OutputPropertiesBuilder(OutputProperties source) {
    super(source);
  }
  
  /**
   * Returns a new {@link OutputProperties} instance initialized with the
   * property values set on the builder.
   */
  public OutputProperties build() {
    return new OutputPropertiesImpl(this);
  }
  /**
   * The OutputPropertiesImpl class is a simple immutable value object that
   * implements the {@link OutputProperties} interface.
   */
  private static class OutputPropertiesImpl extends StreamPropertiesImpl 
      implements OutputProperties {
    
    /**
     * Constructs a new OutputPropertiesImpl instance with the property values
     * from a builder.
     * 
     * @param builder builder instnace
     */
    private OutputPropertiesImpl(OutputPropertiesBuilder builder) {
      super(builder);
    }
  }
}
