// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc;

import com.google.api.data.client.v2.DateTime;
import com.google.api.data.client.v2.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class Jsonc {

  public static String getItemFields(Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendItemFields(buf, itemType);
    return buf.toString();
  }

  public static String getFeedFields(Class<?> feedType, Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendFeedFields(buf, feedType, itemType);
    return buf.toString();
  }

  public static void appendItemFields(StringBuilder buf, Class<?> itemType) {
    ClassInfo typeInfo = ClassInfo.of(itemType);
    boolean first = true;
    for (String name : typeInfo.getNames()) {
      Field field = typeInfo.getField(name);
      if (Modifier.isFinal(field.getModifiers())) {
        continue;
      }
      if (first) {
        first = false;
      } else {
        buf.append(',');
      }
      buf.append(name);
      Class<?> fieldType = field.getType();
      if (List.class.isAssignableFrom(fieldType)) {
        // TODO: handle array of array?
        fieldType = ClassInfo.getListParameter(field);
      }
      if (fieldType != null && !ClassInfo.isImmutable(fieldType)
          && !List.class.isAssignableFrom(fieldType)
          && !Map.class.isAssignableFrom(fieldType)) {
        buf.append('(');
        appendItemFields(buf, fieldType);
        buf.append(')');
      }
    }
  }

  public static void appendFeedFields(StringBuilder buf, Class<?> feedType,
      Class<?> itemType) {
    int length = buf.length();
    appendItemFields(buf, feedType);
    if (buf.length() > length) {
      buf.append(",");
    }
    buf.append("items(");
    appendItemFields(buf, itemType);
    buf.append(')');
  }

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    if (item == null) {
      return null;
    }
    Class<? extends T> itemType = (Class<? extends T>) item.getClass();
    // TODO: support enum for JSON-C string value?
    // TODO: support Java arrays?
    // don't need to clone immutable types
    if (ClassInfo.isImmutable(itemType)) {
      return item;
    }
    // TODO: handle array?
    if (List.class.isAssignableFrom(itemType)) {
      List<Object> itemList = (List<Object>) item;
      int size = itemList.size();
      List<Object> result =
          (List<Object>) ClassInfo.newListInstance(itemType, size);
      for (int i = 0; i < size; i++) {
        Object value = itemList.get(i);
        result.add(clone(value));
      }
      return (T) result;
    }
    if (Map.class.isAssignableFrom(itemType)) {
      Map<String, Object> itemMap = (Map<String, Object>) item;
      Map<String, Object> result =
          (Map<String, Object>) ClassInfo.newMapInstance(itemType);
      for (Map.Entry<String, Object> entry : itemMap.entrySet()) {
        itemMap.put(entry.getKey(), clone(entry.getValue()));
      }
      return (T) result;
    }
    // clone basic JSON-C object
    T result = ClassInfo.newInstance(itemType);
    Field[] fields = itemType.getFields();
    int numFields = fields.length;
    for (int i = 0; i < numFields; i++) {
      // deep clone of each field
      Field field = fields[i];
      Class<?> fieldType = field.getType();
      Object thisValue = ClassInfo.getValue(field, item);
      if (thisValue != null && !Modifier.isFinal(field.getModifiers())) {
        ClassInfo.setValue(field, result, clone(thisValue));
      }
    }
    // TODO: clone JsoncObject
    return result;
  }

  public static String toString(Object item) {
    StringBuilder buf = new StringBuilder();
    appendValue(buf, item);
    return buf.toString();
  }

  private static void appendValue(StringBuilder buf, Object item) {
    if (item == null || item instanceof Boolean || item instanceof Integer
        || item instanceof Short || item instanceof Byte
        || item instanceof Float) {
      buf.append(item);
    } else if (item instanceof String || item instanceof Long
        || item instanceof Double || item instanceof BigInteger
        || item instanceof BigDecimal) {
      appendString(buf, item.toString());
    } else if (item instanceof DateTime) {
      appendString(buf, ((DateTime) item).toStringRfc3339());
    } else if (item instanceof List<?>) {
      buf.append('[');
      @SuppressWarnings("unchecked")
      List<Object> listValue = (List<Object>) item;
      int size = listValue.size();
      for (int i = 0; i < size; i++) {
        if (i != 0) {
          buf.append(',');
        }
        appendValue(buf, listValue.get(i));
      }
      buf.append(']');
    } else {
      buf.append('{');
      boolean firstField = true;
      if (!(item instanceof Map<?, ?>)) {
        ClassInfo typeInfo = ClassInfo.of(item.getClass());
        for (String name : typeInfo.getNames()) {
          Field field = typeInfo.getField(name);
          Object fieldValue = ClassInfo.getValue(field, item);
          if (fieldValue != null) {
            firstField = appendField(buf, firstField, name, fieldValue);
          }
        }
        if (item instanceof JsoncObject) {
          item = ((JsoncObject) item).getUnknownKeyMap();
        }
      }
      if (item instanceof Map<?, ?>) {
        @SuppressWarnings("unchecked")
        Map<String, Object> mapValue = (Map<String, Object>) item;
        for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
          firstField =
              appendField(buf, firstField, entry.getKey(), entry.getValue());
        }
      }
      buf.append('}');
    }
  }

  private static boolean appendField(StringBuilder buf, boolean firstField,
      String name, Object value) {
    if (firstField) {
      firstField = false;
    } else {
      buf.append(',');
    }
    appendString(buf, name);
    buf.append(":");
    appendValue(buf, value);
    return firstField;
  }

  private static void appendString(StringBuilder buf, String value) {
    // TODO: need to escape " in value
    buf.append('"').append(value).append('"');
  }
}
