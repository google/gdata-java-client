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

import com.google.gdata.data.IAtom;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.WireFormat;

/**
 * An RSS generator that can generate from both old and new data model formats.
 *
 * 
 */
public class RssDualGenerator extends DualModeGenerator<IAtom> {

  public RssDualGenerator() {
    super(new RssGenerator());
  }

  public Class<IAtom> getSourceType() {
    return IAtom.class;
  }

  public AltFormat getAltFormat() {
    return AltFormat.RSS;
  }

  @Override
  public WireFormat getWireFormat() {
    return WireFormat.XML;
  }
}
