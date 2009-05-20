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

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.wireformats.ContentValidationException;
import com.google.gdata.wireformats.WireFormat;
import com.google.gdata.wireformats.WireFormatGenerator;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * An {@link OutputGenerator} using a {@link WireFormat} to serialize to the
 * output stream.
 *
 * 
 */
public abstract class WireFormatOutputGenerator<T>
    extends CharacterGenerator<T> {

  /**
   * Returns the wire format to use when generating this output.
   */
  public abstract WireFormat getWireFormat();

  /**
   * Generates content to the writer based upon the provided request/response.
   */
  @Override
  public void generate(Writer w, OutputProperties outProps, T source)
      throws IOException {

    WireFormat wireFormat = getWireFormat();
    String encoding = getCharsetEncoding(outProps);
    Charset cs = Charset.forName(encoding);
    WireFormatGenerator gen = wireFormat.createGenerator(
        outProps, w, cs, usePrettyPrint(outProps));

    // Only types that extends Element are supported by the wire format code
    if (source instanceof Element) {
      Element elem = (Element) source;
      try {

        ElementMetadata<?, ?> meta = outProps.getMetadataRegistry().bind(
            elem.getElementKey(), outProps.getMetadataContext());

        Element resolved = elem.resolve(meta);
        gen.generate(resolved);
      } catch (ContentValidationException e) {
        throw new IOException("Invalid content: " + e.getMessage());
      }
      w.flush();
    } else {
      throw new IllegalArgumentException("Output object was not an Element: " +
          source);
    }
  }
}

