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
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Java representation for the gm:attributes tag for in the itemtypes  feed.
 */
@ExtensionDescription.Default(
    nsAlias = GoogleBaseNamespaces.GM_ALIAS,
    nsUri = GoogleBaseNamespaces.GM_URI,
    localName = "attributes")
public class GmAttributes implements Extension {

  private final List<GmAttribute> attributes =
      new ArrayList<GmAttribute>();

  private final Set<GoogleBaseAttributeId> attributeIds =
      new LinkedHashSet<GoogleBaseAttributeId>();
 
  /**
   * Gets an unmodifiable list of GoogleBaseAttributeId objects.
   *
   * @return attribute list, which might be empty
   * @deprecated use {@link #getAttributes()} instead. 
   */
  public List<GoogleBaseAttributeId> getAttributeIds() {
    return Collections.unmodifiableList(
        new ArrayList<GoogleBaseAttributeId>(attributeIds));
  }

  /**
   * Gets an unmodifiable collection of GmAttribute objects.
   *
   * @return attribute list, which might be empty
   */
  public Collection<GmAttribute> getAttributes() {
    return Collections.unmodifiableCollection(attributes);
  }

  /**
   * Adds a new attribute to the list with no importance specified.
   *
   * @param name attribute name
   * @param type attribute type, may be null
   * @throws NullPointerException if name is null
   * @deprecated use 
   *    {@link #addAttribute(GoogleBaseAttributeId, GmAttribute.Importance)} 
   *    instead. This method will be removed in a future release
   */
  public void addAttribute(String name, GoogleBaseAttributeType type) {
    GoogleBaseAttributeId attributeId = new GoogleBaseAttributeId(name, type);
    addAttribute(attributeId, null);
  }

  /**
   * Adds a new attribute to the list.
   * 
   * <p> Note: The importance parameter should not be null. The current
   * implementation allows null values for loadind old attributes persisted on 
   * client side. The null check will be enabled in a future release. 
   * 
   * @param attributeId the id for this attribute
   * @param importance attribute importance, may be null if no importance is
   *        defined
   * @throws NullPointerException if name is null
   * @throws IllegalArgumentException if an attribute with the same name and 
   *        type has already been added
   */
  public void addAttribute(GoogleBaseAttributeId attributeId, 
      GmAttribute.Importance importance) {
    if (attributeIds.contains(attributeId)) {
      throw new IllegalArgumentException("Attribute id already registered " +
            "in the attributes group " + attributeId);
    }
    
    attributes.add(new GmAttribute(attributeId, importance));
    attributeIds.add(attributeId);
  }
  
  /** Generate the XML representation for this tag. */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (attributes.isEmpty()) {
      // Nothing to write
      return;
    }
    w.startElement(GoogleBaseNamespaces.GM, "attributes", null, null);

    w.startRepeatingElement();
    for (GmAttribute attribute : attributes) {
      List<XmlWriter.Attribute> attributes =
        new ArrayList<XmlWriter.Attribute>();
      GoogleBaseAttributeId id = attribute.getAttributeId();
      attributes.add(new XmlWriter.Attribute("name", id.getName()));
      if (id.getType() != null) {
        attributes.add(new XmlWriter.Attribute("type", id.getType().getName()));
      }
      if (attribute.getImportance() != null) {
        attributes.add(new XmlWriter.Attribute("importance", 
            attribute.getImportance().getXmlValue()));
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
    attributes.clear();

    return new XmlParser.ElementHandler() {
      /** Parses gm:attribute sub-elements. */
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName, Attributes attrs)
          throws ParseException, IOException {
        if (namespace.equals(GoogleBaseNamespaces.GM_URI)
            && "attribute".equals(localName)) {
          AttributeHelper helper = new AttributeHelper(attrs);

          String nameAttr = helper.consume("name", true);
          GoogleBaseAttributeType typeAttr = GoogleBaseAttributeType
              .getInstance(helper.consume("type", false));
          GmAttribute.Importance importanceAttr = helper
              .consumeEnum("importance", false, GmAttribute.Importance.class);

          GoogleBaseAttributeId attributeId = 
              new GoogleBaseAttributeId(nameAttr, typeAttr);
          addAttribute(attributeId, importanceAttr);

          helper.assertAllConsumed();
          return new XmlParser.ElementHandler();
        } else {
          return super.getChildHandler(namespace, localName, attrs);
        }
      }
    };
  }
  
  /**
   * The information defining an attribute, as contained by the gm:attribute
   * element in the itemtypes feed.
   */
 public static class GmAttribute {
   /**
    * Enumeration defining the importance levels that an attribute can have. 
    */
   public enum Importance {
     /**
      * The attribute is very important in defining the characteristics of an
      * item. In the future, the presence of the required attributes might be
      * checked for the item types defined by Google.
      */
     REQUIRED,
     
     /**
      * The attribute is important for providing good quality for an item, but
      * the item is meaningful even if this attribute is not specified. 
      * Providing a value for a recommended attribute increases the chances of
      * showing the item in a Google search.   
      */
     RECOMMENDED,
     
     /** 
      * The attribute usually provides behavioral or other meta information for
      * the item it belongs to. This information is not very important for 
      * direct attribute searches, but can affect where and when an attribute
      * is shown.
      */
     OPTIONAL,
     
     /**
      * The attribute is deprecated and should not be used when creating new
      * items.
      */
     DEPRECATED;

     /** The string defining this instance in an Xml document. */
     private final String xmlValue;
     
     Importance() {
       xmlValue = this.name().toLowerCase();
     }
     
     /** Gets the importance level as in an Xml document. */
     String getXmlValue() {
       return xmlValue;
     }
   }
   
   /** The attribute id identifying this attribute. */
   private final GoogleBaseAttributeId attributeId;
   
   /** The importance for this attribute. */
   private final Importance importance;

   /** 
    * Creates a GmAttribute instance with the specified id and importance. A 
    * null importance value should be provided when no importance is defined.
    *  
    * @param attributeId the id of the attribute
    * @param importance the importance, or null if no information is available
    * @throws NullPointerException if the attributeId is null.
    */
   public GmAttribute(GoogleBaseAttributeId attributeId, Importance importance) 
       {
     if (attributeId == null) {
       throw new NullPointerException("AttributeId is null.");
     }
     
     this.attributeId = attributeId;
     this.importance = importance;
   }
   
   /**
    * Returns the id of this attribute.
    */
   public GoogleBaseAttributeId getAttributeId() {
     return attributeId;
   }

   /**
    * Returns the importance for this attribute, or null if not defined.
    */
   public Importance getImportance() {
     return importance;
   }  
 }
}
