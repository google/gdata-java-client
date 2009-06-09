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

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

/**
 * A generic source of xml events that can be parsed by an instance of
 * {@link DefaultHandler}.
 * 
 * 
 */
public interface XmlEventSource {

  /**
   * Parse this event source with the given xml handler.
   * 
   * @param handler an xml handler that can handle the xml event source.
   * @throws SAXException if parsing fails.
   */
  public void parse(DefaultHandler handler) throws SAXException, IOException;
}
