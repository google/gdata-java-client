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


package com.google.gdata.wireformats.output;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.WriterFlags;
import com.google.gdata.util.ContentType;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * The XmlGenerator class is an abstract base class supporting the
 * implementation of the OutputGenerator interface for XML content types.
 * 
 * @param <S> source object type for output generation
 */
public abstract class XmlGenerator<S> extends CharacterGenerator<S> {

  /**
   * The base set of matching content types for XML content.  Custom subclasses
   * may add to this list.
   */
  protected static final List<ContentType> XML_CONTENT_TYPES =
      Collections.unmodifiableList(
          Arrays.asList(ContentType.TEXT_XML, ContentType.TEXT_PLAIN));
  
  
  /**
   * Returns an XmlWriter configured based upon request attributes.
   *
   * @param w The underlying writer to write to
   * @param outProps output properties
   * @param charset The writer's character encoding (determines which characters
   *     need to be escaped. Note that the writer must already be configured to
   *     use this character set. If the writer and the character set are out of
   *     sync, the generated XML may be overly escaped (not too bad) or
   *     malformed (pretty serious.)
   * @return An XmlWriter
   * @throws IOException If an error occurs creating the XmlWriter.
   */
  protected XmlWriter getXmlWriter(Writer w, OutputProperties outProps, 
      String charset) throws IOException {

    EnumSet<WriterFlags> writerFlags = EnumSet.of(WriterFlags.WRITE_HEADER);
    if (usePrettyPrint(outProps)) {
      writerFlags.add(WriterFlags.PRETTY_PRINT);
    }
    return new XmlWriter(w, writerFlags, charset);
  }

  /**
   * Creates a new  matching ContentType set that contains all of the basic
   * XML types plus an additional list of types.
   */
  protected static List<ContentType> createMatchingXmlList(
      ContentType ... types) {

    ArrayList<ContentType> matchingTypes =
        new ArrayList<ContentType>(types.length + XML_CONTENT_TYPES.size());
    for (ContentType contentType : types) {
      matchingTypes.add(contentType);
    }
    matchingTypes.addAll(XML_CONTENT_TYPES);
    return Collections.unmodifiableList(matchingTypes);
  }

  /**
   * Generates content to the writer based upon the provided request/response.
   */
  @Override
  public void generate(Writer w, OutputProperties outProps, S source)
      throws IOException {

    String charset = getCharsetEncoding(outProps);
    XmlWriter xw = getXmlWriter(w, outProps, charset);
    generateXml(xw, outProps, source);
    w.flush();
  }

  /**
   * Generates the XML content to the provided XML writer instance based
   * upon the query request/response attributes.
   */
  public abstract void generateXml(XmlWriter w,
                                   OutputProperties outProps,
                                   S source)
      throws IOException;
}
