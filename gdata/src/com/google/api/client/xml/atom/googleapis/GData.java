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

package com.google.api.client.xml.atom.googleapis;

import com.google.api.client.ClassInfo;
import com.google.api.client.Entity;
import com.google.api.client.FieldInfo;

import java.util.Collection;
import java.util.Map;

/** Utilities for working with the Atom XML of Google Data API's. */
public class GData {

  /**
   * Returns the fields mask to use for the given reflection-based object
   * entity. It cannot be a {@link Map}, {@link Entity} or a {@link Collection}.
   */
  public static String getFieldsFor(Class<?> entity) {
    StringBuilder buf = new StringBuilder();
    appendFieldsFor(buf, entity);
    return buf.toString();
  }

  /**
   * Returns the fields mask to use for the given reflection-based object entity
   * for the feed class and for the entry class. This should only be used if the
   * feed class does not contain the entry class as a field. The object entities
   * cannot be a {@link Map}, {@link Entity} or a {@link Collection}.
   */
  public static String getFeedFields(Class<?> feedClass, Class<?> entryClass) {
    StringBuilder buf = new StringBuilder();
    appendFeedFields(buf, feedClass, entryClass);
    return buf.toString();
  }

  private static void appendFieldsFor(StringBuilder buf, Class<?> entity) {
    if (Map.class.isAssignableFrom(entity)
        || Collection.class.isAssignableFrom(entity)) {
      throw new IllegalArgumentException(
          "cannot specify field mask for a Map or Collection class: " + entity);
    }
    ClassInfo classInfo = ClassInfo.of(entity);
    boolean first = true;
    for (String name : classInfo.getFieldNames()) {
      FieldInfo fieldInfo = classInfo.getFieldInfo(name);
      if (fieldInfo.isFinal) {
        continue;
      }
      if (first) {
        first = false;
      } else {
        buf.append(',');
      }
      buf.append(name);
      // TODO: handle Java arrays?
      Class<?> fieldClass = fieldInfo.type;
      if (Collection.class.isAssignableFrom(fieldClass)) {
        // TODO: handle Java collection of Java collection or Java map?
        fieldClass = ClassInfo.getCollectionParameter(fieldInfo.field);
      }
      // TODO: implement support for map when server implements support for *
      if (fieldClass != null) {
        if (fieldInfo.isPrimitive) {
          if (name.charAt(0) != '@' && !name.equals("text()")) {
            // TODO: wait for bug fix from server
            // buf.append("/text()");
          }
        } else if (!Collection.class.isAssignableFrom(fieldClass)
            && !Map.class.isAssignableFrom(fieldClass)) {
          buf.append('(');
          appendFieldsFor(buf, fieldClass);
          buf.append(')');
        }
      }
    }
  }

  private static void appendFeedFields(StringBuilder buf, Class<?> feedClass,
      Class<?> entryClass) {
    int length = buf.length();
    appendFieldsFor(buf, feedClass);
    if (buf.length() > length) {
      buf.append(",");
    }
    buf.append("entry(");
    appendFieldsFor(buf, entryClass);
    buf.append(')');
  }


  private GData() {
  }
}
