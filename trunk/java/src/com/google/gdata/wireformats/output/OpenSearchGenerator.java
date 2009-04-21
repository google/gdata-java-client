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
import com.google.gdata.data.OpenSearchDescriptionDocument;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * Generates the output for an OpenSearch description document
 *
 * 
 */
public class OpenSearchGenerator
    extends XmlGenerator<OpenSearchDescriptionDocument> {

  public AltFormat getAltFormat() {
    return AltFormat.OPENSEARCH;
  }

  public Class<OpenSearchDescriptionDocument> getSourceType() {
    return OpenSearchDescriptionDocument.class;
  }

  /**
   * Writes the metadata based upon the specified feed.
   * encoding.
   */
  @Override
  public void generateXml(XmlWriter xw, OutputProperties outProps, 
      OpenSearchDescriptionDocument source)
      throws IOException {

    source.generate(xw);
  }
}
