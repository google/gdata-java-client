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
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.XmlParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Atom generator type.
 *
 * 
 */
public class Generator implements IGenerator {

  /** Version. */
  protected String version;
  public String getVersion() { return version; }
  public void setVersion(String v) { version = v; }

  /** URI. */
  protected String uri;
  public String getUri() { return uri; }
  public String getHref() { return uri; }
  public void setUri(String v) { uri = v; }

  /** Generator name. */
  protected String name;
  public String getName() { return name; }
  public void setName(String v) { name = v; }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            Output writer.
   *
   * @throws  IOException
   */
  public void generateAtom(XmlWriter w) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(2);

    if (version != null) {
      attrs.add(new XmlWriter.Attribute("version", version));
    }

    if (uri != null) {
      attrs.add(new XmlWriter.Attribute("uri", uri));
    }

    w.simpleElement(Namespaces.atomNs, "generator", attrs, name);
  }

  /** {@code <atom:generator>} parser. */
  public class AtomHandler extends XmlParser.ElementHandler {

    @Override
    public void processAttribute(String namespace, String localName,
        String value) {

      if (namespace.equals("") && localName.equals("version")) {
        version = value;
      } else if (namespace.equals("") && localName.equals("uri")) {
        uri = value;
      }
    }

    @Override
    public void processEndElement() {
      name = value;
    }
  }
}
