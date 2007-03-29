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


package com.google.gdata.data.appsforyourdomain;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Google Apps for Your Domain schema extension describing the email service.
 * Note that this does not represent a particular email address, like the GData
 * extension Email.
 *
 * 
 */
public class Email extends ExtensionPoint implements Extension {

  /** Email address. */
  protected String address;
  public String getAddress() { return address; }
  public void setAddress(String v) { address = v; }


  /** Email quota. */
  protected String quota;
  public String getQuota() { return quota; }
  public void setQuota(String v) { quota = v; }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Email.class);
    desc.setNamespace(Namespaces.APPS_NAMESPACE);
    desc.setLocalName("email");
    desc.setRepeatable(false);
    return desc;
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (address != null) {
      attrs.add(new XmlWriter.Attribute("address", address));
    }
    
    if (quota != null) {
      attrs.add(new XmlWriter.Attribute("quota", quota));
    }

    generateStartElement(w, Namespaces.APPS_NAMESPACE, "email", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.APPS_NAMESPACE, "email");
  }


  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }


  /** <apps:email> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {
      super(extProfile, Email.class);
    }

    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("address")) {
          address = value;
        } else if (localName.equals("quota")) {
          quota = value;
        }
      }
    }


    public void processEndElement() throws ParseException {

      if (quota == null) {
        throw new ParseException("apps:email/@quota is required.");
      }

      super.processEndElement();
    }
  }
}
