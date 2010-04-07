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

package com.google.api.client.http.xml.atom.googleapis;

import com.google.api.client.ArrayMap;
import com.google.api.client.ClassInfo;
import com.google.api.client.Entities;
import com.google.api.client.http.HttpSerializer;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

public final class PatchRelativeToOriginalSerializer implements HttpSerializer {

  private final XmlNamespaceDictionary namespaceDictionary;
  private final Object patchedEntry;
  private final Object originalEntry;

  public PatchRelativeToOriginalSerializer(
      XmlNamespaceDictionary namespaceDictionary, Object patchedEntry,
      Object originalEntry) {
    this.namespaceDictionary = namespaceDictionary;
    this.patchedEntry = patchedEntry;
    this.originalEntry = originalEntry;
  }

  public String getContentType() {
    return "application/xml";
  }

  public String getContentEncoding() {
    return null;
  }

  public long getContentLength() {
    return -1;
  }

  public void writeTo(OutputStream out) throws IOException {
    ArrayMap<String, Object> differences = ArrayMap.create();
    StringBuilder fieldsMaskBuf = new StringBuilder();
    appendFields(differences, fieldsMaskBuf,
        Entities.mapOf(this.originalEntry), Entities.mapOf(this.patchedEntry));
    differences.put("@gd:fields", fieldsMaskBuf.toString());
    XmlSerializer serializer = Xml.createSerializer();
    serializer.setOutput(out, "UTF-8");
    this.namespaceDictionary.serialize(serializer, Atom.ATOM_NAMESPACE,
        "entry", differences);
  }

  private static void appendFields(ArrayMap<String, Object> differences,
      StringBuilder fieldsMaskBuf, Map<String, Object> original,
      Map<String, Object> patched) {
    boolean first = true;
    for (Map.Entry<String, Object> entry : Entities.mapOf(original).entrySet()) {
      String name = entry.getKey();
      Object originalValue = entry.getValue();
      Object patchedValue = patched.get(name);
      if (originalValue == patchedValue || originalValue != null
          && originalValue.equals(patchedValue)) {
        continue;
      }
      Class<?> clazz;
      boolean isPrimitive;
      if (originalValue == null) {
        isPrimitive = ClassInfo.isPrimitive(patchedValue);
        clazz = patchedValue.getClass();
      } else {
        isPrimitive = ClassInfo.isPrimitive(originalValue);
        clazz = originalValue.getClass();
      }
      if (first) {
        first = false;
      } else {
        fieldsMaskBuf.append(',');
      }
      fieldsMaskBuf.append(name);
      differences.put(name, patchedValue);
      if (!isPrimitive) {
        if (Collection.class.isAssignableFrom(clazz)) {
          // TODO: implement
          throw new UnsupportedOperationException("not yet implemented");
        } else {
          fieldsMaskBuf.append('(');
          appendFields(differences, fieldsMaskBuf, Entities
              .mapOf(originalValue), Entities.mapOf(patchedValue));
          fieldsMaskBuf.append(')');
        }
      }
    }
  }
}
