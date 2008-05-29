/* Copyright (c) 2006 Google Inc.
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


package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GData schema extension describing an entity's auxiliary property.
 *
 * 
 */
public class ExtendedProperty extends ExtensionPoint {

  /**
   * Property name expressed as an URI (required). Extended property
   * URIs follow the {scheme}#{local-name} convention.
   * <p>
   * <i>eg:</i> <tt>http://schemas.google.com/g/2005#exif.focalLength</tt>
   */
  protected String name;

  public String getName() {
    return name;
  }

  public void setName(String n) {
    name = n;
  }


  /** Property value (required). */
  protected String val;

  public String getValue() {
    return val;
  }

  public void setValue(String v) {
    val = v;
  }

  public boolean hasValue() {
    return val != null;
  }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(ExtendedProperty.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("extendedProperty");
    desc.setRepeatable(true);
    return desc;
  }

  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (name != null) {
      attrs.add(new XmlWriter.Attribute("name", name));
    }

    if (val != null) {
      attrs.add(new XmlWriter.Attribute("value", val));
    }

    generateStartElement(w, Namespaces.gNs, "extendedProperty", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "extendedProperty");
  }


  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }

  /**
   * Overrides arbitrary XML initialization - ExtendedProperty needs
   * mixed content as well.
   */
  @Override
  protected void initializeArbitraryXml(ExtensionProfile profile,
      Class<? extends ExtensionPoint> extPoint, ElementHandler handler)
      throws IOException {

      handler.initializeXmlBlob(xmlBlob,
          /* mixedContent */ true,
          /* fullTextIndex */ false);
  }

  /** <g:extendedProperty> parser */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) throws ParseException,
        IOException {

      super(extProfile, ExtendedProperty.class);
    }

    public void processAttribute(String namespace,
                                 String localName,
                                 String value) throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("name")) {
          name = value;
        } else if (localName.equals("value")) {
          val = value;
        }
      }
    }

    public void processEndElement() throws ParseException {

      if (name == null) {
        throw new ParseException("g:extendedProperty/@name is required.");
      }

      XmlBlob xmlBlob = getXmlBlob();

      if (val != null && xmlBlob.getBlob() != null) {
        throw new ParseException("g:extendedProperty/@value and XML are mutually exclusive.");
      }

      if (val == null && xmlBlob.getBlob() == null) {
        throw new ParseException("exactly one of g:extendedProperty/@value, XML is required.");
      }
    }
  }
}
