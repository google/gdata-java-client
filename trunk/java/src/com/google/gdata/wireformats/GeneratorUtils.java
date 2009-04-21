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


package com.google.gdata.wireformats;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.model.Attribute;
import com.google.gdata.model.Element;
import com.google.gdata.model.QName;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Utility functions shared across generators.
 * 
 * 
 */
public class GeneratorUtils {

  public static Map<String, XmlNamespace> calculateNamespaces(Element root) {
    Map<String, XmlNamespace> namespaceMap = Maps.newHashMap();
    calculateNamespaces(namespaceMap, root);
    return namespaceMap;
  }
  
  public static void calculateNamespaces(Map<String, XmlNamespace> namespaces,
      Element root) {
    QName name = root.getMetadata().getName();
    addNamespace(namespaces, name);
    
    Iterator<Attribute> attIter = root.getAttributeIterator();
    while (attIter.hasNext()) {
      Attribute att = attIter.next();
      addNamespace(namespaces, att.getMetadata().getName());
    }
    
    Iterator<Element> childIter = root.getElementIterator();
    while (childIter.hasNext()) {
      Element child = childIter.next();
      calculateNamespaces(namespaces, child);
    }
  }
  
  private static void addNamespace(Map<String, XmlNamespace> namespaces,
      QName name) {
    if (name == null) {
      return;
    }
    XmlNamespace ns = name.getNs();
    if (ns == null) {
      return;
    }
    String alias = ns.getAlias();
    if (alias == null) {
      return;
    }
    String uri = ns.getUri();
    if (namespaces.containsKey(uri)) {
      return;
    }
    namespaces.put(uri, ns);
  }
}
