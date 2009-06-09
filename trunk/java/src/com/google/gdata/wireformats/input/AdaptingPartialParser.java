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

import com.google.gdata.data.ParseSource;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.gd.Partial;
import com.google.gdata.model.gd.PartialMetadata;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * The AdaptingPartialParser class provides an {@link InputParser}
 * implementation that is capable of adapting from a full representation
 * {@link InputParser} to parse a partial representation of the same type
 * wrapped inside of a {@link Partial} element.
 * 
 * @param <T> the expected output representation type.
 * 
 * 
 */
public class AdaptingPartialParser<T extends Element> 
    implements InputParser<T> {
  
  /**
   * Creates a new PartialParser instance that handles partial representations
   * of the same type of the provided full representation parser.
   * 
   * @param <T> expected response type for parser
   * @param parser full representation parser for type
   * @return partial representation parser for type
   */
  public static <T extends Element> InputParser<T> from(InputParser<T> parser) {
    return new AdaptingPartialParser<T>(parser);
  }

  /** The full representation parser for the parse type */
  private final InputParser<T> fullParser;
  
  private AdaptingPartialParser(InputParser<T> parser) {
    this.fullParser = parser;
  }

  public AltFormat getAltFormat() {
    return fullParser.getAltFormat();
  }

  public Class<? extends T> getResultType() {
    return fullParser.getResultType();
  }

  public <R extends T> R parse(ParseSource parseSource, InputProperties inProps,
      Class<R> resultClass) throws IOException, ServiceException {
    
    // Copy-modify the input properties to expect a partial element that
    // wraps the expected type
    ElementMetadata<?, ?> metadata = inProps.getRootMetadata();
    InputProperties partialProperties = 
        new InputPropertiesBuilder(inProps)
            .setElementMetadata(new PartialMetadata(metadata))
            .build();
    
    // Create a parser to parse the partial-wrapped representation
    InputParser<Partial> partialParser = new PartialParser(fullParser);
    Partial p = 
        partialParser.parse(parseSource, partialProperties, Partial.class);
    
    // Unwrap and return the inner partial representation
    return resultClass.cast(p.getElement(metadata.getKey()));
  }
  
  /**
   * The PartialParser provides an {@link InputParser} implementation that
   * is capable of parsing the Partial element containing the wrapped 
   * partial representation.
   */
  private static class PartialParser extends ElementParser<Partial> {

    public PartialParser(InputParser<? extends Element> parser) {
      super(parser.getAltFormat(), Partial.class);
    }
  }
}
