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
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.WireFormat;

/**
 * The ElementGenerator class implements a simple {WireFormatOutputGenerator}
 * that can generate documents from any GDOM {@link Element} representation.
 * 
 * @param <E> the element type expected as input to the generator
 * 
 * 
 */
public class ElementGenerator<E extends Element> 
    extends WireFormatOutputGenerator<E> {
  
  public static <E extends Element> ElementGenerator<E> of(AltFormat altFormat, 
      Class<E> inputType) {
    return new ElementGenerator<E>(altFormat, inputType);
  }
  
  private final AltFormat altFormat;
  private final Class<E> inputType;
  
  private ElementGenerator(AltFormat altFormat, Class<E> inputType) {
    this.altFormat = altFormat;
    this.inputType = inputType;
  }

  @Override
  public WireFormat getWireFormat() {
    return altFormat.getWireFormat();
  }

  public AltFormat getAltFormat() {
    return altFormat;
  }

  public Class<E> getSourceType() {
    return inputType;
  }
}
