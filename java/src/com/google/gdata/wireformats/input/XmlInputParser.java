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
import com.google.gdata.data.XmlEventSource;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;
import java.io.Reader;

/**
 * The XmlInputParser class is an abstract base class supporting the creation
 * of new {@link InputParser} implementations that parse XML content.  Concrete
 * class will need to implement the both the
 * {@link #parse(Reader, InputProperties, Class)} and
 * {@link #parse(XmlEventSource, InputProperties, Class)} methods to handle
 * parsing from the two possible source types.
 */
public abstract class XmlInputParser<T> extends CharacterParser<T> {

  /**
   * Creates a new XmlInputParser instance for the provided representation and
   * result type.
   * 
   * @param altFormat parsed representation.
   * @param resultClass parser output result type.
   */
  public XmlInputParser(AltFormat altFormat, Class<T> resultClass) {
    super(altFormat, resultClass);
  }

  public <R extends T> R parse(ParseSource parseSource, InputProperties inProps,
      Class<R> targetClass) throws IOException, ServiceException {
    XmlEventSource eventSource = parseSource.getEventSource();
    if (eventSource == null) {
      return super.parse(parseSource, inProps, targetClass);
    }
    return parse(eventSource, inProps, targetClass);
  }
  
  /**
   * The parse method should be implemented by subclasses and should parse
   * input data from the provided {@link XmlEventSource} instance.
   * 
   * @param <R> result type
   * @param eventSource event source.
   * @param inProps input properties.
   * @param targetClass result type.
   * @return instance of result type.
   * @throws IOException error reading data from event source.
   * @throws ServiceException parsing error.
   */
  abstract protected <R extends T> R parse(XmlEventSource eventSource, 
      InputProperties inProps, Class<R> targetClass) 
      throws IOException, ServiceException;
}
