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

import java.io.IOException;


/**
 * Google Apps for Your Domain schema extension describing the email service.
 * Note that this does not represent a particular email address, like the GData
 * extension Email.
 *
 * 
 */
public class Email extends ExtensionPoint implements Extension {

  public static final String EXTENSION_LOCAL_NAME = "email";

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Email.class);
    desc.setNamespace(Namespaces.APPS_NAMESPACE);
    desc.setLocalName(EXTENSION_LOCAL_NAME);
    desc.setRepeatable(false);
    return desc;
  }

  @Override


  public void generate(XmlWriter w, ExtensionProfile extensionProfile)
      throws IOException {
    generateStartElement(
        w, Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, null, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extensionProfile);

    w.endElement(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME);
  }

}

