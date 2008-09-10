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


package com.google.gdata.data;

import java.io.InputStream;
import java.io.Reader;
import com.google.gdata.util.XmlParser;

/**
 * The ParseSource class represents a data input source that can be used to
 * read parseable GData content.
 */
public class ParseSource {

  // Exactly one of these will be non-null, given the contructor model.
  private final Reader reader;
  private final InputStream inputStream;
  private final XmlEventSource eventSource;

  /**
   * Constructs a new GData input source using data from a {@link Reader}.
   */
  public ParseSource(Reader reader) {
    this.reader = reader;
    this.inputStream = null;
    this.eventSource = null;
  }

  /**
   * Constructs a new GData input source using data from an {@link InputStream}.
   */
  public ParseSource(InputStream inputStream) {
    this.inputStream = inputStream;
    this.reader = null;
    this.eventSource = null;
  }

  /**
   * Constructs a new GData input source using data from an {@link XmlParser}.
   */
  public ParseSource(XmlEventSource eventSource) {
    this.eventSource = eventSource;
    this.reader = null;
    this.inputStream = null;
  }

  /**
   * Returns the {@link Reader} associated with the input source or
   * {@code null} if associated with a different source type.
   */
  final public Reader getReader() {
    return reader;
  }

  /**
   * Returns the {@link Reader} associated with the input source or
   * {@code null} if associated with a different source type.
   */
  final public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * Returns the {@link Reader} associated with the input source or
   * {@code null} if associated with a different source type.
   */
  final public XmlEventSource getEventSource() {
    return eventSource;
  }
}
