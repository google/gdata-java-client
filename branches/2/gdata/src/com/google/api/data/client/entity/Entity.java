// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Arbitrary entity object that stores all unknown field name/value pairs.
 * <p>
 * Subclasses can declare public fields for fields they do know, using the Java
 * field name as the field name or the {@link Name} annotation to override the
 * field name to use. Null field names are not allowed, but null field values
 * are allowed.
 */
public class Entity {

  List<String> unknownFieldNames;
  List<Object> unknownFieldValues;

  // TODO: efficient parsing when know that field names don't duplicate?

  private final ClassInfo classInfo = ClassInfo.of(getClass());

  /** Returns a new field iterator. */
  public final FieldIterator getFieldIterator() {
    return FieldIterators.of(this);
  }

  /**
   * Returns the field value for the given field name or {@code null} if there
   * is no such name or if its value is {@code null}.
   */
  public final Object get(String name) {
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(name);
    if (fieldInfo != null) {
      return fieldInfo.getValue(this);
    }
    List<String> unknownFieldNames = this.unknownFieldNames;
    if (unknownFieldNames != null) {
      int size = unknownFieldNames.size();
      for (int i = 0; i < size; i++) {
        if (unknownFieldNames.get(i).equals(name)) {
          return this.unknownFieldValues.get(i);
        }
      }
    }
    return null;
  }

  /**
   * Sets the given field value (may be {@code null}) for the given field name.
   * Any existing value for the field will be overwritten.
   */
  public final void set(String name, Object value) {
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(name);
    if (fieldInfo != null) {
      fieldInfo.setValue(this, value);
      return;
    }
    List<String> unknownFieldNames = this.unknownFieldNames;
    List<Object> unknownFieldValues;
    if (unknownFieldNames == null) {
      this.unknownFieldNames = unknownFieldNames = new ArrayList<String>(1);
      this.unknownFieldValues = unknownFieldValues = new ArrayList<Object>(1);
    } else {
      unknownFieldValues = this.unknownFieldValues;
      int size = unknownFieldNames.size();
      for (int i = 0; i < size; i++) {
        if (unknownFieldNames.get(i).equals(name)) {
          unknownFieldValues.set(i, value);
          return;
        }
      }
    }
    unknownFieldNames.add(name);
    unknownFieldValues.add(value);
  }

  /** Removes the unknown field of the given name. */
  public final void remove(String name) {
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(name);
    if (fieldInfo != null) {
      throw new IllegalArgumentException();
    }
    List<String> unknownFieldNames = this.unknownFieldNames;
    if (unknownFieldNames != null) {
      List<Object> unknownFieldValues = this.unknownFieldValues;
      int size = unknownFieldNames.size();
      for (int i = 0; i < size; i++) {
        if (unknownFieldNames.get(i).equals(name)) {
          if (size == 1) {
            this.unknownFieldNames = null;
            this.unknownFieldValues = null;
          } else {
            unknownFieldNames.remove(i);
            unknownFieldValues.remove(i);
          }
          return;
        }
      }
    }
  }

  /**
   * Sets the fields of this entity using the fields of the given entity. Any
   * fields from the given entity whose value is null are ignored. Otherwise,
   * the given entity field's value overrides any existing value in this entity.
   * 
   * @param from entity to merge from or {@code null} to ignore
   * @return merged entity
   */
  public final Entity setFrom(Object from) {
    if (from == null) {
      return this;
    }
    FieldIterator fieldIterator = FieldIterators.of(from);
    while (fieldIterator.hasNext()) {
      String name = fieldIterator.nextFieldName();
      Object value = fieldIterator.getFieldValue();
      if (value != null) {
        set(name, value);
      }
    }
    return this;
  }
}
