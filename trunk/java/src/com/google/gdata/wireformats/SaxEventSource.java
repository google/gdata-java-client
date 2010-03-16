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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.util.common.xml.parsing.SecureGenericXMLFactory;
import com.google.gdata.data.XmlEventSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * {@link XmlEventSource} implementation based on SAX.
 *
 * 
 */
public class SaxEventSource implements XmlEventSource {
  private static final Logger logger =
      Logger.getLogger(SaxEventSource.class.getCanonicalName());

  /** The SAXParserFactory used to create underlying SAXParser instances. */
  private static final SAXParserFactory parserFactory = 
      createSAXParserFactory();

  /** Creates a secure SAX parser, which is secured against XXE attacks. */
  private static SAXParserFactory createSAXParserFactory() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      try {
        SAXParserFactory secureFactory =
            SecureGenericXMLFactory.getSAXParserFactory(factory);
        secureFactory.newSAXParser();
        factory = secureFactory;
      } catch (ParserConfigurationException e) {
        // OK. Cannot create secure xml parser. Use insecure one.
      }
      factory.setNamespaceAware(true);
      return factory;
    } catch (SAXException e) {
      throw new IllegalStateException(
          "Failed to create a SAX parser factory", e);
    }
  }

  /** Reader used by this parser. */
  private final Reader reader;

  /**
   * Creates a SAX event source backed by a reader
   * and a charset.
   */
  public SaxEventSource(Reader reader) {
    Preconditions.checkNotNull(reader, "reader");

    this.reader = reader;
  }
  
  public void parse(DefaultHandler handler) throws IOException, SAXException {
    SAXParser sp = createSaxParser();
    ParserAdapter pa = new ParserAdapter(sp.getParser());
    pa.setContentHandler(handler);
    pa.parse(new InputSource(reader));
  }

  private SAXParser createSaxParser() {
    try {
      return parserFactory.newSAXParser();
    } catch (ParserConfigurationException e) {
      // The parser factory failing for any reasons should be considered
      // a bug in this class, since the parser configuration is hardcoded.
      throw new IllegalStateException("Invalid parser configuration", e);
    } catch (SAXException e) {
      throw new IllegalStateException("Failed to create a SAX parser", e);
    }
  }

}
