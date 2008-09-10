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


package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing a postal address.
 *
 * 
 */
public class PostalAddress implements Extension {

  /** The postal address type. */
  public static final class Rel {
    public static final String GENERAL = null;
    public static final String HOME = Namespaces.gPrefix + "home";
    public static final String WORK = Namespaces.gPrefix + "work";
    public static final String OTHER = Namespaces.gPrefix + "other";
  }

  /** The postal address type. */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /** Label. */
  protected String label;
  public String getLabel() { return label; }
  public void setLabel(String v) { label = v; }


  /** Address string. */
  protected String value;
  public String getValue() { return value; }
  public void setValue(String v) { value = v; }

  /** Whether this is the primary postal address */
  protected boolean primary;
  public boolean getPrimary() { return primary; }
  public void setPrimary(boolean p) { primary = p; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(PostalAddress.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("postalAddress");
    desc.setRepeatable(true);
    return desc;
  }

  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (label != null) {
      attrs.add(new XmlWriter.Attribute("label", label));
    }

    if (primary) {
      attrs.add(new XmlWriter.Attribute("primary", primary));
    }

    w.simpleElement(Namespaces.gNs, "postalAddress", attrs, value);
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs) {
    return new Handler();
  }


  /** <g:postalAddress> parser. */
  private class Handler extends XmlParser.ElementHandler {

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("label")) {
          label = value;
        } else if (localName.equals("primary")) {
          Boolean pr = parseBooleanValue(value);
          primary = (pr != null) ? pr : false;
        }
      }
    }

    @Override
    public void processEndElement() {
      PostalAddress.this.value = Handler.this.value;
    }
  }
}
