// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.lang.reflect.Field;

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
