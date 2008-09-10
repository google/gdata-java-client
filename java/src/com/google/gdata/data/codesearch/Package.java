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


package com.google.gdata.data.codesearch;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.codesearch.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing a package.
 * <gcs:package name="package_name" uri="cvs://wherever_it_comes_from"/>
 *
 * 
 */
public class Package extends ExtensionPoint implements Extension {
  public static final String EXTENSION_PACKAGE = "package";
  public static final String ATTRIBUTE_NAME = "name";
  public static final String ATTRIBUTE_URI = "uri";
  /** Embedded name. */
  protected String name;
  public String getName() {
    return name;
  }
  /** Embedded uri. */
  protected String uri;
  public String getUri() {
    return uri;
  }

  /**
   * @return Description of this extension
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Package.class);
    desc.setNamespace(Namespaces.gCSNs);
    desc.setLocalName(EXTENSION_PACKAGE);
    desc.setRepeatable(false);
    return desc;
  }
  
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    ArrayList<XmlWriter.Attribute> attributes =
      new ArrayList<XmlWriter.Attribute>();
    if (this.name != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_NAME,
                                             this.name));
    }
    if (this.uri != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_URI,
                                             this.uri));
    }
    w.simpleElement(Namespaces.gCSNs, EXTENSION_PACKAGE,
                    attributes, "");
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace, String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <c:package> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {
    public Handler(ExtensionProfile extProfile) {

      super(extProfile, Package.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {

      if ("".equals(namespace)) {
        if (ATTRIBUTE_NAME.equals(localName)) {
          name = value;
        }
        if (ATTRIBUTE_URI.equals(localName)) {
          uri = value;
        }

      }
    }

    @Override
    public void processEndElement() throws ParseException {
      if (name == null) {
        throw new ParseException(Namespaces.gCS + EXTENSION_PACKAGE + "/@" +
                                 ATTRIBUTE_NAME + " is required.");
      }
      if (uri == null) {
        throw new ParseException(Namespaces.gCS + EXTENSION_PACKAGE + "/@" +
                                 ATTRIBUTE_URI + " is required.");
      }
    }
  }
}
