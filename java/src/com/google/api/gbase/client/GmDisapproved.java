/* Copyright (c) 2007 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Object representation of the gm:disapproved tag.
 */
public class GmDisapproved implements Extension {

  /**
   * Creates a gm:disapproved tag.
   */
  public GmDisapproved() {

  }

  /** Returns a description for this extension. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GmDisapproved.class);
    desc.setNamespace(GoogleBaseNamespaces.GM);
    desc.setLocalName("disapproved");
    desc.setRepeatable(false);
    return desc;
  }

  /** Generates a gm:status tag if the status name is set. */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    w.simpleElement(GoogleBaseNamespaces.GM, "disapproved", null, null);
  }

  /** Reads a gm:disapproved tag. */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs)
      throws ParseException, IOException {
    return new XmlParser.ElementHandler();
  }
}
