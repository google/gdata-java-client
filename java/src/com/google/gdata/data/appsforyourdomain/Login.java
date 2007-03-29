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
import com.google.gdata.data.appsforyourdomain.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A name space element: "apps:login".  Used to model a user account in Google
 * Apps for Your Domain.  Has attributes "userName", "password, and
 * "suspended".
 *
 * 
 * 
 *
 */

public class Login extends ExtensionPoint implements Extension {
  public static final String EXTENSION_LOCAL_NAME = "login";
  public static final String ATTRIBUTE_USER_NAME = "userName";
  public static final String ATTRIBUTE_PASSWORD = "password";
  public static final String ATTRIBUTE_SUSPENDED = "suspended";
  public static final String ATTRIBUTE_IPWHITELISTED = "ipWhitelisted";

  // property "userName"
  protected String userName;
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  // property "password"
  protected String password;
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /* 
   * property "suspended"
   * The suspended attribute is optional and set to null when unspecified by
   * client.
   */
  protected Boolean suspended = null;
  public Boolean getSuspended() { return suspended; }
  public void setSuspended(Boolean b) { suspended = b; } 

  /* 
   * property "ipWhitelisted"
   * The ipWhitelisted attribute is optional and set to null when unspecified by
   * client.
   */
  protected Boolean ipWhitelisted = null;
  public Boolean getIpWhitelisted() { return ipWhitelisted; }
  public void setIpWhitelisted(Boolean b) { ipWhitelisted = b; }

  /**
   * @return Description of this extension
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription extensionDescription = new ExtensionDescription();
    extensionDescription.setExtensionClass(Login.class);
    extensionDescription.setNamespace(Namespaces.APPS_NAMESPACE);
    extensionDescription.setLocalName(EXTENSION_LOCAL_NAME);
    extensionDescription.setRepeatable(false);
    return extensionDescription;
  }

  public void generate(XmlWriter w, ExtensionProfile extensionProfile)
      throws IOException {
    ArrayList<XmlWriter.Attribute> attributes =
      new ArrayList<XmlWriter.Attribute>();

    if (userName != null) {
      attributes.add(
        new XmlWriter.Attribute(ATTRIBUTE_USER_NAME, userName)
      );
    }

    if (password != null) {
      attributes.add(
        new XmlWriter.Attribute(ATTRIBUTE_PASSWORD, password)
      );
    }

    if (suspended != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_SUSPENDED, suspended));
    }

    if (ipWhitelisted != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_IPWHITELISTED,
          ipWhitelisted));
    }

    generateStartElement(
        w, Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, attributes, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extensionProfile);

    w.endElement(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME);
  }

  public ElementHandler getHandler(
      ExtensionProfile extProfile, String namespace,
      String localName, Attributes attrs)
      throws ParseException, IOException {
    return new Handler(extProfile, attrs);
  }

  /** <apps:login> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {
    public Handler(ExtensionProfile extProfile, Attributes attrs)
        throws ParseException, IOException {
      super(extProfile, Login.class);
      ipWhitelisted = getBooleanAttribute(attrs, "ipWhitelisted");
    }

    public void processAttribute(
        String namespace, String localName, String value)
        throws ParseException {
      if ("".equals(namespace)) {
        if (ATTRIBUTE_USER_NAME.equals(localName)) {
          userName = value;
        } else if (ATTRIBUTE_PASSWORD.equals(localName)) {
          password = value;
        } else if (ATTRIBUTE_SUSPENDED.equals(localName)) {
	  if (value.trim().equalsIgnoreCase("true")) {
	    suspended = true;
	  } else if (value.trim().equalsIgnoreCase("false")) {
	    suspended = false;
	  }
        }
      }
    }

    public void processEndElement() throws ParseException {
      super.processEndElement();
    }
  }
}
