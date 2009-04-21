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


package com.google.gdata.util;

import com.google.gdata.data.XmlEventSource;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An xml parser that can also handle parsing events from an
 * {@link XmlEventSource}.
 */
public class EventSourceParser extends XmlParser {

  private static final Logger LOGGER =
      Logger.getLogger(EventSourceParser.class.getName());

  public EventSourceParser(ElementHandler rootHandler, String rootNamespace,
      String rootElementName) {
    this.rootHandler = rootHandler;
    this.rootNamespace = rootNamespace;
    this.rootElementName = rootElementName;
  }
  
  public void parse(XmlEventSource source) throws ParseException, IOException {
    try {
      source.parse(this);
    } catch (SAXException e) {
      Exception rootException = e.getException();
      if (rootException instanceof ParseException) {
        throwParseException((ParseException)rootException);
      } else if (rootException instanceof IOException) {
        LogUtils.logException(LOGGER, Level.WARNING, null, e);
        throw (IOException)rootException;
      } else {
        LogUtils.logException(LOGGER, Level.FINE, null, e);
        throw new ParseException(e);
      }
    }
  }
}