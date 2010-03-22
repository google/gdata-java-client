// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.DateTime;
import com.google.api.data.client.entity.ClassInfo;
import com.google.api.data.client.entity.FieldIterator;
import com.google.api.data.client.entity.FieldIterators;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NamespaceDictionary {

  static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
  static final String GD_NAMESPACE = "http://schemas.google.com/g/2005";

  private final HashMap<String, String> namespaceAliasToUriMap;

  NamespaceDictionary(HashMap<String, String> namespaceAliasToUriMap) {
    this.namespaceAliasToUriMap = namespaceAliasToUriMap;
  }

  public static final class Builder {
    private final HashMap<String, String> namespaceAliasToUriMap =
        new HashMap<String, String>();

    public void addNamespace(String alias, String uri) {
      this.namespaceAliasToUriMap.put(alias, uri);
    }

    @SuppressWarnings("unchecked")
    public NamespaceDictionary build() {
      return new NamespaceDictionary(
          (HashMap<String, String>) this.namespaceAliasToUriMap.clone());
    }
  }

  XmlSerializer createSerializer() throws XmlPullParserException {
    return Atom.getParserFactory().createSerializer();
  }

  void setNamespacePrefixes(XmlSerializer serializer) throws IOException {
    // TODO: too many namespaces: should only use ones actually in use
    for (Map.Entry<String, String> entry : namespaceAliasToUriMap.entrySet()) {
      serializer.setPrefix(entry.getKey(), entry.getValue());
    }
  }

  String getNamespaceUriForAlias(String alias) {
    String result = namespaceAliasToUriMap.get(alias.intern());
    if (result == null) {
      throw new IllegalArgumentException("unrecognized alias: "
          + (alias.length() == 0 ? "(default)" : alias));
    }
    return result;
  }

  public String toString(String elementName, Object element) {
    try {
      StringWriter writer = new StringWriter();
      XmlSerializer serializer = createSerializer();
      serializer.setOutput(writer);
      serializer.startDocument(null, null);
      setNamespacePrefixes(serializer);
      serializeElement(serializer, elementName, element);
      serializer.endDocument();
      return writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    } catch (XmlPullParserException e) {
      throw new IllegalArgumentException(e);
    }
  }

  class ElementSerializer {
    Object textValue = null;
    final List<String> attributeNames = new ArrayList<String>();
    final List<Object> attributeValues = new ArrayList<Object>();
    final List<String> subElementNames = new ArrayList<String>();
    final List<Object> subElementValues = new ArrayList<Object>();

    ElementSerializer(Object elementValue) {
      Class<?> valueClass = elementValue.getClass();
      if (ClassInfo.isImmutable(valueClass)) {
        this.textValue = elementValue;
      } else {
        FieldIterator fieldIterator = FieldIterators.of(elementValue);
        while (fieldIterator.hasNext()) {
          String fieldName = fieldIterator.nextFieldName().intern();
          Object fieldValue = fieldIterator.getFieldValue();
          if (fieldValue != null) {
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

    void serialize(XmlSerializer serializer, String elementName)
        throws IOException {
      int colon = elementName.indexOf(':');
      String elementLocalName = elementName.substring(colon + 1);
      String elementNamespaceUri =
          getNamespaceUriForAlias(colon == -1 ? "" : elementName.substring(0,
              colon));
      serialize(serializer, elementNamespaceUri, elementLocalName);
    }

    void serialize(XmlSerializer serializer, String elementNamespaceUri,
        String elementLocalName) throws IOException {
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
            serializeElement(serializer, subElementName, subElement);
          }
        } else {
          serializeElement(serializer, subElementName, subElementValue);
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

  void serializeElement(XmlSerializer serializer, String elementName,
      Object elementValue) throws IOException {
    new ElementSerializer(elementValue).serialize(serializer, elementName);
  }

  void serializeElement(XmlSerializer serializer, String elementNamespaceUri,
      String elementLocalName, Object elementValue) throws IOException {
    new ElementSerializer(elementValue).serialize(serializer,
        elementNamespaceUri, elementLocalName);
  }
}
