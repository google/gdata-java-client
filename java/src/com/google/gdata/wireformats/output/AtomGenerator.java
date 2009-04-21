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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IAtom;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.util.Namespaces;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * The AtomGenerator class is a concrete implementation of the OutputGenerator
 * interface that generates GData feeds and entries using the Atom XML Syntax.
 *
 * 
 */
public class AtomGenerator extends XmlGenerator<IAtom> {
 
  public AltFormat getAltFormat() {
    return AltFormat.ATOM;
  }

  public Class<IAtom> getSourceType() {
    return IAtom.class;
  }

  @Override
  public void generateXml(XmlWriter xw, OutputProperties outProps, IAtom source)
      throws IOException {

    xw.setDefaultNamespace(Namespaces.atomNs);
    ExtensionProfile extProfile = outProps.getExtensionProfile();
    if (source instanceof IFeed) {
      IFeed feed = (IFeed) source;
      if (feed instanceof BaseFeed) {
        ((BaseFeed<?, ?>) feed).generateAtom(xw, extProfile);
      } else {
        throw new IllegalArgumentException("Feed was not an instance of data.BaseFeed");
      }
    } else if (source instanceof IEntry) {
      IEntry entry = (IEntry) source;
      if (entry instanceof BaseEntry) {
        ((BaseEntry<?>) entry).generateAtom(xw, extProfile);
      } else {
        throw new IllegalArgumentException("Entry was not an instance of data.BaseEntry");
      }
    } else if (source != null) {
      // This case is to handle case that may return Atom content but might
      // also return no data (like DELETE)
      throw new IllegalStateException("Unexpected source type: " + source.getClass());
    }
  }
}
