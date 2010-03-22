// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.util.Iterator;

public final class ReflectionFieldIterator implements FieldIterator {

  private boolean isFieldValueComputed;

  private String fieldName;

  private Object fieldValue;

  private final Iterator<String> fieldNamesIterator;

  private final ClassInfo classInfo;

  private final Object object;

  ReflectionFieldIterator(Object object) {
    this.object = object;
    ClassInfo classInfo = this.classInfo = ClassInfo.of(object.getClass());
    this.fieldNamesIterator = classInfo.getFieldNames().iterator();
  }

  public String nextFieldName() {
    String fieldName = this.fieldName = this.fieldNamesIterator.next();
    this.isFieldValueComputed = false;
    return fieldName;
  }

  public Object getFieldValue() {
    if (this.isFieldValueComputed) {
      return this.fieldValue;
    }
    this.isFieldValueComputed = true;
    return this.fieldValue =
        this.classInfo.getFieldInfo(this.fieldName).getValue(this.object);
  }

  public boolean hasNext() {
    return this.fieldNamesIterator.hasNext();
  }
}
