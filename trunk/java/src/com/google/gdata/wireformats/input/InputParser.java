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
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * Wire format interface implemented by helper classes that parse data from an
 * InputStream and return an object representation of a particular type.
 *
 * 
 *
 * @param <T> base type of result objects returned by the parser.
 */
public interface InputParser<T> {

  /**
   * Returns the alternate representation format that is expected as input to
   * the parser.
   */
  public AltFormat getAltFormat();

  /**
   * Returns the target type that is populated by the parser from the input
   * stream.
   */
  public Class<? extends T> getResultType();

  /**
   * Parses data in the supported representation format from the input stream
   * based upon the provided input properties into the provided target object.
   * 
   * @param parseSource providing the source of the data
   * @param inProps properties describing the input data
   * @param resultClass specific type of result expected from the parse
   */
  public <R extends T> R parse(ParseSource parseSource, InputProperties inProps,
      Class<R> resultClass) throws IOException, ServiceException;
}
