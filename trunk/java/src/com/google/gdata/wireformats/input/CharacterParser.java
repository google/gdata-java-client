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
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.util.io.base.UnicodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * The CharacterParser class is  abstract base class to support the
 * implementation of an {@link InputParser} for character-based content types.
 * <p>
 * It encapsulates the code for mapping from an {@link InputStream} to an
 * {@link Reader} based upon the input content type and then delegates to the
 * abstract {@link #parse(Reader, InputProperties, Class)} method to handle
 * the actual parsing of the character data.
 * 
 * 
 */
public abstract class CharacterParser<T> extends AbstractParser<T> {
  
  /**
   * Constructs a new CharacterParser that parses content in a particular
   * alt format and returns instances of the specified type.
   * 
   * @param altFormat representation handled by this parser
   * @param resultClass base type of results produced by parser.
   */
  protected CharacterParser(AltFormat altFormat, Class<T> resultClass) {
    super(altFormat, resultClass);
  }
  
  /**
   * Returns the expected character set encoding for content based upon the
   * input properties content type.
   * 
   * @param inProps input properties
   * @return expected character set encoding
   */
  protected String getCharset(InputProperties inProps) {
    return inProps.getContentType().getCharset();
  }

  /**
   * The parse method will use the character encoding found in the output
   * properties instance to construct an appropriate {@link Reader} and then
   * delegate to the {@link #parse(Reader, InputProperties, Class)} method
   * to perform the parsing.
   */
  public <R extends T> R parse(ParseSource parseSource, InputProperties inProps,
      Class<R> targetClass) throws IOException, ServiceException {
    Reader reader;
    reader = parseSource.getReader();
    if (reader == null) {
      InputStream inputStream = parseSource.getInputStream();
      if (inputStream != null) {
        try {
          String charset = getCharset(inProps);
          if (charset == null) {
            // case where charset has not been specified in the Content-Type.
            charset = "UTF-8";
          }
          if (charset.toLowerCase().startsWith("utf-")) {
            reader = new UnicodeReader(inputStream, charset);
          } else {
            reader = new InputStreamReader(inputStream, charset);
          }
        } catch (UnsupportedEncodingException e) {
          throw new ParseException("Unsupported encoding: " + 
              e.getLocalizedMessage(), e);
        }
      } else {
        // Means an XmlEventSource was passed in, should be handled at a higher
        // level by XmlInputParser
        throw new IllegalStateException("XML event source not supported");
      }
    }
    return parse(reader, inProps, targetClass);
  }
  
  /**
   * Parses character content with the specified properties to produce a result
   * of an expected type.  Concrete subclasses will provide an implementation
   * of this method that constructs a result type instance of the result class
   * and then parses into it from the provided {@link Reader}.
   * 
   * @param <R> expected result type
   * @param inputReader reader to parse data from
   * @param inProps input properties
   * @param resultClass class to instantiate and parse result into.
   * @return result object from parse
   * @throws IOException if an error occurred reading data while parsing
   * @throws ServiceException if an error occurred within the content
   */
  abstract public <R extends T> R parse(Reader inputReader, 
      InputProperties inProps, Class<R> resultClass) 
      throws IOException, ServiceException;
}
