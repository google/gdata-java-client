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
 * GData schema extension describing a phone number.
 *
 * 
 */
public class PhoneNumber implements Extension {

  /** The phone number type. */
  public static final class Rel {
    public static final String GENERAL = null;
    public static final String MOBILE = Namespaces.gPrefix + "mobile";
    public static final String HOME = Namespaces.gPrefix + "home";
    public static final String WORK = Namespaces.gPrefix + "work";
    public static final String WORK_MOBILE = Namespaces.gPrefix + "work_mobile";
    public static final String CALLBACK = Namespaces.gPrefix + "callback";
    public static final String ASSISTANT = Namespaces.gPrefix + "assistant";
    public static final String COMPANY_MAIN = Namespaces.gPrefix + "company_main";
    public static final String INTERNAL_EXTENSION = Namespaces.gPrefix + "internal-extension";
    public static final String FAX = Namespaces.gPrefix + "fax";
    public static final String HOME_FAX = Namespaces.gPrefix + "home_fax";
    public static final String WORK_FAX = Namespaces.gPrefix + "work_fax";
    public static final String OTHER_FAX = Namespaces.gPrefix + "other_fax";
    public static final String PAGER = Namespaces.gPrefix + "pager";
    public static final String WORK_PAGER = Namespaces.gPrefix + "work_pager";
    public static final String CAR = Namespaces.gPrefix + "car";
    public static final String SATELLITE = Namespaces.gPrefix + "satellite";
    public static final String RADIO = Namespaces.gPrefix + "radio";
    public static final String TTY_TDD = Namespaces.gPrefix + "tty_tdd";
    public static final String ISDN = Namespaces.gPrefix + "isdn";
    public static final String TELEX = Namespaces.gPrefix + "telex";
    public static final String OTHER = Namespaces.gPrefix + "other";
    public static final String MAIN = Namespaces.gPrefix + "main";
  }

  /** The phone number type. */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /** Label. */
  protected String label;
  public String getLabel() { return label; }
  public void setLabel(String v) { label = v; }

  /** "Tel URI" (formal representation of number; see RFC 3966). */
  protected String uri;
  public String getUri() { return uri; }
  public void setUri(String v) { uri = v; }

  /** Human-readable phone number. */
  protected String phoneNumber;
  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String v) { phoneNumber = v; }

  /** Whether this is the primary phone number */
  protected boolean primary;
  public boolean getPrimary() { return primary; }
  public void setPrimary(boolean p) { primary = p; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(PhoneNumber.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("phoneNumber");
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

    if (uri != null) {
      attrs.add(new XmlWriter.Attribute("uri", uri));
    }

    if (primary) {
      attrs.add(new XmlWriter.Attribute("primary", true));
    }

    w.simpleElement(Namespaces.gNs, "phoneNumber", attrs, phoneNumber);
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs) {
    return new Handler();
  }


  /** <g:phoneNumber> parser. */
  private class Handler extends XmlParser.ElementHandler {

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("label")) {
          label = value;
        } else if (localName.equals("uri")) {
          uri = value;
        } else if (localName.equals("primary")) {
          Boolean pr = parseBooleanValue(value);
          primary = (pr != null) ? pr : false;
        }
      }
    }

    @Override
    public void processEndElement() {
      phoneNumber = value;
    }
  }
}
