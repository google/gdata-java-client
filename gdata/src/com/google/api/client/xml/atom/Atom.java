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

package com.google.api.client.xml.atom;

import com.google.api.client.ClassInfo;
import com.google.api.client.xml.Xml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public final class Atom {

  static final class StopAtAtomEntry extends Xml.CustomizeParser {

    static final StopAtAtomEntry INSTANCE = new StopAtAtomEntry();

    @Override
    public boolean stopAtStartTag(String namespace, String localName) {
      return "entry".equals(localName) && ATOM_NAMESPACE.equals(namespace);
    }
  }

  public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
  public static final String CONTENT_TYPE = "application/atom+xml";

  public static String getEntryFields(Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendEntryFields(buf, itemType);
    return buf.toString();
  }

  public static String getFeedFields(Class<?> feedType, Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendFeedFields(buf, feedType, itemType);
    return buf.toString();
  }

  public static void appendEntryFields(StringBuilder buf, Class<?> itemType) {
    ClassInfo classInfo = ClassInfo.of(itemType);
    boolean first = true;
    for (String name : classInfo.getFieldNames()) {
      Field field = classInfo.getField(name);
      if (Modifier.isFinal(field.getModifiers())) {
        continue;
      }
      if (first) {
        first = false;
      } else {
        buf.append(',');
      }
      buf.append(name);
      Class<?> fieldClass = field.getType();
      if (List.class.isAssignableFrom(fieldClass)) {
        // TODO: handle array of array?
        fieldClass = ClassInfo.getCollectionParameter(field);
      }
      if (fieldClass != null && !ClassInfo.isPrimitive(fieldClass)
          && !List.class.isAssignableFrom(fieldClass)
          && !Map.class.isAssignableFrom(fieldClass)) {
        buf.append('(');
        appendEntryFields(buf, fieldClass);
        buf.append(')');
      }
    }
  }

  public static void appendFeedFields(StringBuilder buf, Class<?> feedType,
      Class<?> itemType) {
    int length = buf.length();
    appendEntryFields(buf, feedType);
    if (buf.length() > length) {
      buf.append(",");
    }
    buf.append("entry(");
    appendEntryFields(buf, itemType);
    buf.append(')');
  }

  private Atom() {
  }
}
