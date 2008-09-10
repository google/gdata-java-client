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


package com.google.gdata.data.appsforyourdomain;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GData element representing a property in Google Apps.  The name 
 * identifies the property this element represents.
 *
 * 
 */
public class Property extends ExtensionPoint implements Extension {
  private static final String EXTENSION_LOCAL_NAME = "property";
  private static final String ATTRIBUTE_NAME = "name";
  private static final String ATTRIBUTE_VALUE = "value";
  private static final ExtensionDescription EXTENSION_DESCRIPTION
      = new ExtensionDescription();
  
  static {
    EXTENSION_DESCRIPTION.setExtensionClass(Property.class);
    EXTENSION_DESCRIPTION.setNamespace(Namespaces.APPS_NAMESPACE);
    EXTENSION_DESCRIPTION.setLocalName(EXTENSION_LOCAL_NAME);
    EXTENSION_DESCRIPTION.setRepeatable(true);
  }

  // attribute "name"
  protected String name;

  public String getName() {
    return name;
  }

  public void setName(String n) {
    name = n;
  }

  // attribute "value"
  protected String val;

  public String getValue() {
    return val;
  }

  public void setValue(String v) {
    val = v;
  }

  /**
   * @return suggested extension description
   */
  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESCRIPTION;
  }
  
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();
    if (name != null) {
      attrs.add(new XmlWriter.Attribute(ATTRIBUTE_NAME, name));
    }
    if (val != null) {
      attrs.add(new XmlWriter.Attribute(ATTRIBUTE_VALUE, val));
    }
    generateStartElement(
        w, Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, attrs, null);
    generateExtensions(w, extProfile);
    w.endElement(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME);
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }

  // <apps:property> parser
  private class Handler extends ExtensionPoint.ExtensionHandler {
    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Property.class);
    }
    
    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {
      if (namespace.equals("")) {
        if (localName.equals(ATTRIBUTE_NAME)) {
          name = value;
        } else if (localName.equals(ATTRIBUTE_VALUE)) {
          val = value;
        }
      }
    }
    
    @Override
    public void processEndElement() throws ParseException {
      if (name == null) {
        throw new ParseException("apps:property/@name is required.");
      }
      if (val == null) {
        throw new ParseException("apps:property/@value is required.");
      }
    }
  }
}
