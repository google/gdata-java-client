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
import com.google.api.client.FieldInfo;
import com.google.api.client.http.HttpSerializer;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

public final class PatchRelativeToOriginalSerializer implements HttpSerializer {

  private final XmlNamespaceDictionary namespaceDictionary;
  private final Object patchedItem;
  private final Object originalItem;

  public PatchRelativeToOriginalSerializer(
      XmlNamespaceDictionary namespaceDictionary, Object patchedItem,
      Object originalItem) {
    this.namespaceDictionary = namespaceDictionary;
    this.patchedItem = patchedItem;
    this.originalItem = originalItem;
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
    ArrayMap<String, Object> patch = ArrayMap.create();
    Object originalItem = this.originalItem;
    StringBuilder fieldsMaskBuf = new StringBuilder();
    boolean first = true;
    Object patchedItem = this.patchedItem;
    // TODO: this needs a lot of work
    ClassInfo typeInfo = ClassInfo.of(patchedItem.getClass());
    for (String name : typeInfo.getFieldNames()) {
      Field field = typeInfo.getField(name);
      Object patchedValue = FieldInfo.getFieldValue(field, patchedItem);
      Object originalValue = FieldInfo.getFieldValue(field, originalItem);
      Class<?> fieldType = field.getType();
      if (patchedValue != originalValue && ClassInfo.isPrimitive(fieldType)
          && (patchedValue == null || !patchedValue.equals(originalValue))) {
        if (first) {
          first = false;
        } else {
          fieldsMaskBuf.append(',');
        }
        fieldsMaskBuf.append(name);
        patch.put(name, patchedValue);
      }
    }
    patch.put("@gd:fields", fieldsMaskBuf.toString());
    XmlSerializer serializer = Xml.createSerializer();
    serializer.setOutput(out, "UTF-8");
    this.namespaceDictionary.serialize(serializer, Atom.ATOM_NAMESPACE,
        "entry", patch);
  }
}
