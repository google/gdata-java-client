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


package com.google.gdata.wireformats;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.XmlEventSource;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.util.LogUtils;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.input.InputProperties;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A parser of xml event sources.
 *
 * 
 */
public class EventSourceParser extends XmlParser {

  private static final Logger LOGGER =
    Logger.getLogger(EventSourceParser.class.getName());

  private final XmlEventSource source;

  public EventSourceParser(InputProperties inProps, XmlEventSource source) {
    super(inProps, null, null);
    this.source = source;
  }

  @Override
  public Element parse(Element element)
      throws IOException, ParseException, ContentValidationException {

    ValidationContext vc = new ValidationContext();
    ElementMetadata<?, ?> metadata = getMetadata(element);

    this.rootHandler = new XmlHandler(vc, null, element, metadata);
    QName elementName = metadata.getName();
    XmlNamespace elementNs = elementName.getNs();
    this.rootNamespace = elementNs == null ? null : elementNs.getUri();
    this.rootElementName = elementName.getLocalName();

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

    return element.resolve(metadata);
  }
}
