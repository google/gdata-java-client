/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.xml;

import com.google.api.client.ClassInfo;
import com.google.api.client.DateTime;
import com.google.api.client.Entities;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XmlNamespaceDictionary {

  public final HashMap<String, String> namespaceAliasToUriMap =

  new HashMap<String, String>();

  // TODO: need ability to add more namespaces as we parse!

  public void setNamespacePrefixes(XmlSerializer serializer) throws IOException {
    // TODO: too many namespaces: should only use ones actually in use
    for (Map.Entry<String, String> entry : namespaceAliasToUriMap.entrySet()) {
      serializer.setPrefix(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Shows a debug string representation of an item entity.
   * 
   * @param element element entity ({@link XmlEntity}, {@link Map}, or any
   *        object with public fields)
   * @param elementName optional XML element local name prefixed by its
   *        namespace alias -- for example {@code "atom:entry"} -- or {@code
   *        null} to make up something
   */
  public String toStringOf(String elementName, Object element) {
    try {
      StringWriter writer = new StringWriter();
      XmlSerializer serializer = Xml.createSerializer();
      serializer.setOutput(writer);
      serialize(serializer, elementName, element, false);
      return writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Shows a debug string representation of an item entity.
   * 
   * @param element element entity ({@link XmlEntity}, {@link Map}, or any
   *        object with public fields)
   * @param elementNamespaceUri XML namespace URI or {@code null} for no
   *        namespace
   * @param elementLocalName XML local name
   * @throws IOException I/O exception
   */
  public void serialize(XmlSerializer serializer, String elementNamespaceUri,
      String elementLocalName, Object element) throws IOException {
    serialize(serializer, elementNamespaceUri, elementLocalName, element, true);
  }

  /**
   * Shows a debug string representation of an item entity.
   * 
   * @param element element entity ({@link XmlEntity}, {@link Map}, or any
   *        object with public fields)
   * @param elementName XML element local name prefixed by its namespace alias
   * @throws IOException I/O exception
   */
  public void serialize(XmlSerializer serializer, String elementName,
      Object element) throws IOException {
    serialize(serializer, elementName, element, true);
  }

  private void serialize(XmlSerializer serializer, String elementNamespaceUri,
      String elementLocalName, Object element, boolean errorOnUnknown)
      throws IOException {
    startDoc(serializer, element, errorOnUnknown).serialize(serializer,
        elementNamespaceUri, elementLocalName);
    serializer.endDocument();
  }

  private void serialize(XmlSerializer serializer, String elementName,
      Object element, boolean errorOnUnknown) throws IOException {
    startDoc(serializer, element, errorOnUnknown).serialize(serializer,
        elementName);
    serializer.endDocument();
  }

  private ElementSerializer startDoc(XmlSerializer serializer, Object element,
      boolean errorOnUnknown) throws IOException {
    serializer.startDocument(null, null);
    setNamespacePrefixes(serializer);
    return new ElementSerializer(element, errorOnUnknown);
  }

  class ElementSerializer {
    private final boolean errorOnUnknown;
    Object textValue = null;
    final List<String> attributeNames = new ArrayList<String>();
    final List<Object> attributeValues = new ArrayList<Object>();
    final List<String> subElementNames = new ArrayList<String>();
    final List<Object> subElementValues = new ArrayList<Object>();

    ElementSerializer(Object elementValue, boolean errorOnUnknown) {
      this.errorOnUnknown = errorOnUnknown;
      Class<?> valueClass = elementValue.getClass();
      if (ClassInfo.isPrimitive(valueClass)) {
        this.textValue = elementValue;
      } else {
        for (Map.Entry<String, Object> entry : Entities.mapOf(elementValue)
            .entrySet()) {
          Object fieldValue = entry.getValue();
          if (fieldValue != null) {
            String fieldName = entry.getKey();
            if (fieldName == "text()") {
              this.textValue = fieldValue;
            } else if (fieldName.charAt(0) == '@') {
              this.attributeNames.add(fieldName.substring(1));
              this.attributeValues.add(fieldValue);
            } else {
              this.subElementNames.add(fieldName);
              this.subElementValues.add(fieldValue);
            }
          }
        }
      }
    }

    String getNamespaceUriForAlias(String alias) {
      String result =
          XmlNamespaceDictionary.this.namespaceAliasToUriMap
              .get(alias.intern());
      if (result == null) {
        if (this.errorOnUnknown) {
          throw new IllegalArgumentException("unrecognized alias: "
              + (alias.length() == 0 ? "(default)" : alias));
        }
        return "http://unknown/" + alias;
      }
      return result;
    }

    void serialize(XmlSerializer serializer, String elementName)
        throws IOException {
      String elementLocalName = null;
      String elementNamespaceUri = null;
      if (elementName != null) {
        int colon = elementName.indexOf(':');
        elementLocalName = elementName.substring(colon + 1);
        String alias = colon == -1 ? "" : elementName.substring(0, colon);
        elementNamespaceUri = getNamespaceUriForAlias(alias);
        if (elementNamespaceUri == null) {
          elementNamespaceUri = "http://unknown/" + alias;
        }
      }
      serialize(serializer, elementNamespaceUri, elementLocalName);
    }

    void serialize(XmlSerializer serializer, String elementNamespaceUri,
        String elementLocalName) throws IOException {
      boolean errorOnUnknown = this.errorOnUnknown;
      if (elementLocalName == null) {
        if (errorOnUnknown) {
          throw new IllegalArgumentException("XML name not specified");
        }
        elementLocalName = "unknownName";
      }
      serializer.startTag(elementNamespaceUri, elementLocalName);
      // attributes
      List<String> attributeNames = this.attributeNames;
      List<Object> attributeValues = this.attributeValues;
      int num = attributeNames.size();
      for (int i = 0; i < num; i++) {
        String attributeName = attributeNames.get(i);
        int colon = attributeName.indexOf(':');
        String attributeLocalName = attributeName.substring(colon + 1);
        String attributeNamespaceUri =
            colon == -1 ? null : getNamespaceUriForAlias(attributeName
                .substring(0, colon));
        serializer.attribute(attributeNamespaceUri, attributeLocalName,
            toSerializedValue(attributeValues.get(i)));
      }
      // text
      Object textValue = this.textValue;
      if (textValue != null) {
        serializer.text(toSerializedValue(textValue));
      }
      // elements
      List<String> subElementNames = this.subElementNames;
      List<Object> subElementValues = this.subElementValues;
      num = subElementNames.size();
      for (int i = 0; i < num; i++) {
        Object subElementValue = subElementValues.get(i);
        String subElementName = subElementNames.get(i);
        if (subElementValue instanceof List<?>) {
          for (Object subElement : (List<?>) subElementValue) {
            new ElementSerializer(subElement, errorOnUnknown).serialize(
                serializer, subElementName);
          }
        } else {
          new ElementSerializer(subElementValue, errorOnUnknown).serialize(
              serializer, subElementName);
        }
      }
      serializer.endTag(elementNamespaceUri, elementLocalName);
    }
  }

  static String toSerializedValue(Object value) {
    if (value instanceof Float) {
      Float f = (Float) value;
      if (f.floatValue() == Float.POSITIVE_INFINITY) {
        return "INF";
      }
      if (f.floatValue() == Float.NEGATIVE_INFINITY) {
        return "-INF";
      }
    }
    if (value instanceof Double) {
      Double d = (Double) value;
      if (d.doubleValue() == Double.POSITIVE_INFINITY) {
        return "INF";
      }
      if (d.doubleValue() == Double.NEGATIVE_INFINITY) {
        return "-INF";
      }
    }
    if (value instanceof String || value instanceof Number
        || value instanceof Boolean) {
      return value.toString();
    }
    if (value instanceof DateTime) {
      return ((DateTime) value).toStringRfc3339();
    }
    throw new IllegalArgumentException("unrecognized value type: "
        + value.getClass());
  }
}
