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

package com.google.api.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldInfo {
  public final Field field;
  public final String name;

  FieldInfo(Field field, String name) {
    this.field = field;
    this.name = name;
  }

  public Object getValue(Object obj) {
    return getFieldValue(this.field, obj);
  }

  public void setValue(Object obj, Object value) {
    setFieldValue(this.field, obj, value);
  }

  public ClassInfo getClassInfo() {
    return ClassInfo.of(this.field.getDeclaringClass());
  }

  public static Object getFieldValue(Field field, Object obj) {
    try {
      return field.get(obj);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static void setFieldValue(Field field, Object obj, Object value) {
    if (Modifier.isFinal(field.getModifiers())) {
      Object finalValue = getFieldValue(field, obj);
      if (value == null ? finalValue != null : !value.equals(finalValue)) {
        throw new IllegalArgumentException("expected final value <"
            + finalValue + "> but was <" + value + "> on " + field.getName()
            + " field in " + obj.getClass().getName());
      }
    } else
      try {
        field.set(obj, value);
      } catch (SecurityException e) {
        throw new IllegalArgumentException(e);
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException(e);
      }
  }

  public static FieldInfo of(Field field) {
    ClassInfo classInfo = ClassInfo.of(field.getDeclaringClass());
    return classInfo.getFieldInfo(field);
  }
}
