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


package com.google.gdata.util.common.xml.parsing;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * A source of secure XML parser factories, where "secure" means that the
 * parsers returned are safe against Xml eXtermal Entity (XXE) attacks.
 * An XXE attack uses dangerous URIs in the DTD to perform attacks such as read
 * server-side files or use the server as an HTTP / FTP proxy to defeat network
 * ACLs.
 * All returned parsers are based on the JDK built-in Xerces parser.
 * See: <a href="http://www.securityfocus.com/archive/1/297714"
 * >http://www.securityfocus.com/archive/1/297714</a>
 * 
 * 
 */
public final class SecureXMLParsing {

  /* This class has only static methods. */
  private SecureXMLParsing() {}

  /**
   * Returns a DOM XML parser factory which is secured against XXE attacks.
   * The type of parsers returned by this method are always JDK built-in Xerces
   * parsers.
   *
   * @return a secure DOM XML parser factory.
   */
  static public SecureJDKXercesDOMFactory getDOMParserFactory() {
    return new SecureJDKXercesDOMFactory();
  }

  /**
   * Returns a SAX XML parser factory which is secured against XXE attacks.
   * The type of parsers returned by this method are always JDK built-in Xerces
   * parsers.
   *
   * @return a secure SAX XML parser factory.
   */
  static public SecureJDKXercesSAXFactory getSAXParserFactory()
      throws ParserConfigurationException, SAXException {

    return new SecureJDKXercesSAXFactory();
  }

  /**
   * Returns an XMLReader which is secured against XXE attacks.
   * The type of readers returned by this method are always based on JDK
   * built-in Xerces parsers.
   *
   * @return a secured XMLReader object.
   */
  static public SecureJDKXercesXMLReader getXMLReader()
      throws ParserConfigurationException, SAXException {

    return new SecureJDKXercesXMLReader();
  }
}
