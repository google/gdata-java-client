/* Copyright (c) 2007 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Java representation for the gm:attribute tag for in the itemtypes  feed.
 */
@ExtensionDescription.Default(
    nsAlias = GoogleBaseNamespaces.GM_ALIAS,
    nsUri = GoogleBaseNamespaces.GM_URI,
    localName = "attributes")
public class GmAttributes implements Extension {

  private final List<GoogleBaseAttributeId> attributeIds =
      new ArrayList<GoogleBaseAttributeId>();

  /**
   * Gets a modifiable list of GoogleBaseAttributeId.
   *
   * @return attribute list, which might be empty
   */
  public List<GoogleBaseAttributeId> getAttributeIds() {
    return attributeIds;
  }

  /**
   * Adds a new attribute to the list.
   *
   * @param name attribute name
   * @param type attribute type, may be null
   * @exception NullPointerException if name is null
   */
  public void addAttribute(String name, GoogleBaseAttributeType type) {
    attributeIds.add(new GoogleBaseAttributeId(name, type));
  }

  /** Generate the XML representation for this tag. */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (attributeIds.isEmpty()) {
      // Nothing to write
      return;
    }
    w.startElement(GoogleBaseNamespaces.GM, "attributes", null, null);

    w.startRepeatingElement();
    for (GoogleBaseAttributeId id : attributeIds) {
      List<XmlWriter.Attribute> attributes =
          new ArrayList<XmlWriter.Attribute>();
      attributes.add(new XmlWriter.Attribute("name", id.getName()));
      if (id.getType() != null) {
        attributes.add(new XmlWriter.Attribute("type", id.getType().getName()));
      }
      w.simpleElement(GoogleBaseNamespaces.GM, "attribute", attributes, null);
    }
    w.endRepeatingElement();

    w.endElement();
  }

  /** Creates a parser for this tag. */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs)
      throws ParseException, IOException {
    // Reset the list
    attributeIds.clear();

    return new XmlParser.ElementHandler() {
      /** Parses gm:attribute sub-elements. */
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName, Attributes attrs)
          throws ParseException, IOException {
        if (namespace.equals(GoogleBaseNamespaces.GM_URI)
            && "attribute".equals(localName)) {
          AttributeHelper helper = new AttributeHelper(attrs);
          addAttribute(helper.consume("name", true),
              GoogleBaseAttributeType.getInstance(
                  helper.consume("type", false)));
          helper.assertAllConsumed();
          return new XmlParser.ElementHandler();
        } else {
          return super.getChildHandler(namespace, localName, attrs);
        }
      }
    };
  }
}
