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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * Interface for GData extension data types. It is designed to be hosted
 * within {@link ExtensionPoint}.
 *
 * 
 * 
 */
public interface Extension {


  /**
   * Generates an XML representation for the extension.
   *
   * @param w XML writer
   * @param extProfile extension profile
   */
  void generate(XmlWriter w, ExtensionProfile extProfile) throws IOException;


  /**
   * Gets an XML element handler for the extension.
   *
   * @param extProfile extension profile
   * @param namespace  extension namespace
   * @param localName tag name, without the namespace prefix
   * @param attrs tag attributes
   * @return an element handler
   * @throws ParseException when an unexpected tag or badly-formatted
   *   XML is detected
   */
  XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                      String namespace,
                                      String localName,
                                      Attributes attrs)
      throws ParseException, IOException;
}
