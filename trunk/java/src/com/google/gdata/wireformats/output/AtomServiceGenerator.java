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
import com.google.gdata.data.introspection.IServiceDocument;
import com.google.gdata.data.introspection.ServiceDocument;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * Generates the metadata for an Atom Service (introspection) document.
 *
 * 
 */
public class AtomServiceGenerator extends XmlGenerator<IServiceDocument> {
 
  public AltFormat getAltFormat() {
    return AltFormat.ATOM_SERVICE;
  }

  public Class<IServiceDocument> getSourceType() {
    return IServiceDocument.class;
  }

  /**
   * Writes the Service document for the target feed.
   */
  @Override
  public void generateXml(XmlWriter xw, OutputProperties outProps, 
      IServiceDocument source) throws IOException {

    if (source instanceof ServiceDocument) {
      ((ServiceDocument) source).generate(xw, outProps.getExtensionProfile());
    } else if (source != null) {
      // This case is to handle case that may return Atom content but might
      // also return no data (like DELETE)
      throw new IllegalStateException("Unexpected source type: " + 
          source.getClass());
    }
  }
}
