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
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing an email address.
 *
 * 
 */
public class Email extends ExtensionPoint implements Extension {

  /** The email type. */
  public static final class Rel {
    public static final String GENERAL = null;
    public static final String HOME = Namespaces.gPrefix + "home";
    public static final String WORK = Namespaces.gPrefix + "work";
    public static final String OTHER = Namespaces.gPrefix + "other";
  }

  /** The email type. */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /** Label. */
  protected String label;
  public String getLabel() { return label; }
  public void setLabel(String v) { label = v; }

  /** Email address. */
  protected String address;
  public String getAddress() { return address; }
  public void setAddress(String v) { address = v; }

  /** Email quota. */
  protected String quota;
  public String getQuota() { return quota; }
  public void setQuota(String v) { quota = v; }

  /** Whether this is the primary email address */
  protected boolean primary;
  public boolean getPrimary() { return primary; }
  public void setPrimary(boolean p) { primary = p; }

  /** Display name of the email address */
  protected String displayName;
  public String getDisplayName() { return displayName; }
  public void setDisplayName(String n) { displayName = n; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Email.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("email");
    desc.setRepeatable(true);
    return desc;
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (label != null) {
      attrs.add(new XmlWriter.Attribute("label", label));
    }

    if (address != null) {
      attrs.add(new XmlWriter.Attribute("address", address));
    }

    if (quota != null) {
      attrs.add(new XmlWriter.Attribute("quota", quota));
    }

    if (primary) {
      attrs.add(new XmlWriter.Attribute("primary", true));
    }
    
    if (displayName != null) {
      attrs.add(new XmlWriter.Attribute("displayName", displayName));
    }

    generateStartElement(w, Namespaces.gNs, "email", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "email");
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <g:email> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Email.class);
    }

    @Override
    public void processAttribute(String namespace, String localName,
        String value) throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("label")) {
          label = value;
        } else if (localName.equals("address")) {
          address = value;
        } else if (localName.equals("quota")) {
          quota = value;
        } else if (localName.equals("primary")) {
          Boolean pr = parseBooleanValue(value);
          primary = (pr != null) ? pr : false;
        } else if (localName.equals("displayName")) {
          displayName = value;
        }
      }
    }

    @Override
    public void processEndElement() throws ParseException {

      if (address == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingAddressAttribute);
      }

      super.processEndElement();
    }
  }
}
