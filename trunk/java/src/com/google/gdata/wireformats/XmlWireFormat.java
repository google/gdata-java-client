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

import com.google.gdata.data.XmlEventSource;
import com.google.gdata.wireformats.input.InputProperties;
import com.google.gdata.wireformats.output.OutputProperties;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * JSON wire format properties.
 */
public class XmlWireFormat extends WireFormat {

  public XmlWireFormat() {
    super("xml");
  }

  @Override
  public WireFormatGenerator createGenerator(OutputProperties outProps,
      Writer w, Charset cs, boolean prettyPrint) {
    return new XmlGenerator(outProps, w, cs, prettyPrint);
  }

  @Override
  public WireFormatParser createParser(InputProperties inProps,
      Reader r, Charset cs) {
    return new XmlParser(inProps, r, cs);
  }

  @Override
  public WireFormatParser createParser(InputProperties inProps,
      XmlEventSource source) {
    return new XmlParser(inProps, source);
  }
}
