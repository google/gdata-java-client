// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public final class Atom {
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
    for (String name : classInfo.getNames()) {
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
        fieldClass = ClassInfo.getListParameter(field);
      }
      if (fieldClass != null && !ClassInfo.isImmutable(fieldClass)
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

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    if (item == null) {
      return null;
    }
    Class<? extends T> itemClass = (Class<? extends T>) item.getClass();
    // TODO: support enum for Atom string value?
    // TODO: support Java arrays?
    // don't need to clone immutable types
    if (ClassInfo.isImmutable(itemClass)) {
      return item;
    }
    // TODO: handle array?
    if (List.class.isAssignableFrom(itemClass)) {
      List<Object> itemList = (List<Object>) item;
      int size = itemList.size();
      List<Object> result =
          (List<Object>) ClassInfo.newListInstance(itemClass, size);
      for (int i = 0; i < size; i++) {
        Object value = itemList.get(i);
        result.add(clone(value));
      }
      return (T) result;
    }
    if (Map.class.isAssignableFrom(itemClass)) {
      Map<String, Object> itemMap = (Map<String, Object>) item;
      Map<String, Object> result =
          (Map<String, Object>) ClassInfo.newMapInstance(itemClass);
      for (Map.Entry<String, Object> entry : itemMap.entrySet()) {
        itemMap.put(entry.getKey(), clone(entry.getValue()));
      }
      return (T) result;
    }
    // clone basic Atom object
    T result = ClassInfo.newInstance(itemClass);
    Field[] fields = itemClass.getFields();
    int numFields = fields.length;
    for (int i = 0; i < numFields; i++) {
      // deep clone of each field
      Field field = fields[i];
      Class<?> fieldType = field.getType();
      Object thisValue = FieldInfo.getFieldValue(field, item);
      if (thisValue != null && !Modifier.isFinal(field.getModifiers())) {
        FieldInfo.setFieldValue(field, result, clone(thisValue));
      }
    }
    // TODO: clone AtomObject
    return result;
  }

  private Atom() {
  }
}
