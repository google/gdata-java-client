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
import com.google.gdata.data.appsforyourdomain.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Apps name space element: "apps:emailList".  Used to model an emailList in
 * Google Apps for Your Domain.  Has attribute "name".
 *
 * Sample XML element.
 * <code>
 * &lt;apps:emailList xmlns:apps="http://schemas.google.com/apps/2006" name="staff"/&gt;
 * </code> 
 *
 */

public class EmailList extends ExtensionPoint implements Extension {
  public static final String EXTENSION_LOCAL_NAME = "emailList";
  public static final String ATTRIBUTE_NAME = "name";
  private static ExtensionDescription EXTENSION_DESC
     = new ExtensionDescription();

  static {
    EXTENSION_DESC.setExtensionClass(EmailList.class);
    EXTENSION_DESC.setNamespace(Namespaces.APPS_NAMESPACE);
    EXTENSION_DESC.setLocalName(EXTENSION_LOCAL_NAME);
    EXTENSION_DESC.setRepeatable(false);
  }

  // property "name"
  protected String name;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Description of this extension
   */
  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESC;
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extensionProfile)
      throws IOException {
    ArrayList<XmlWriter.Attribute> attributes
	= new ArrayList<XmlWriter.Attribute>();

    if (name != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_NAME, name));
    }

    generateStartElement(
        w, Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, attributes, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extensionProfile);
    
    w.endElement(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME);
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {

    /** <apps:emailList> parser. */
    return new ExtensionPoint.ExtensionHandler(extProfile, EmailList.class) {
      
      @Override
      public void processAttribute(String namespace, String localName,
          String value) {
        if ("".equals(namespace)) {
          if (ATTRIBUTE_NAME.equals(localName)) {
            name = value;
          }
        }
      }

      @Override
      public void processEndElement() throws ParseException {
        if (name == null) {
          throw new ParseException(
  	    Namespaces.APPS_NAMESPACE + ":" + EXTENSION_LOCAL_NAME
  	    + "/@" + ATTRIBUTE_NAME + " is required.");
        }

        super.processEndElement();
      }
    };
  }
}
