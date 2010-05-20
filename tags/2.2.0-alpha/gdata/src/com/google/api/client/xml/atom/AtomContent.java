/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.xml.atom;

import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlContent;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @since 2.2
 * @author Yaniv Inbar
 */
public class AtomContent extends XmlContent {

  public Object entry;

  public String getType() {
    return Atom.CONTENT_TYPE;
  }

  public final void writeTo(OutputStream out) throws IOException {
    XmlSerializer serializer = Xml.createSerializer();
    serializer.setOutput(out, "UTF-8");
    this.namespaceDictionary.serialize(serializer, Atom.ATOM_NAMESPACE,
        "entry", this.entry);
  }
}
