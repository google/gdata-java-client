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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

public final class ClassInfo {
  private static final ThreadLocal<WeakHashMap<Class<?>, ClassInfo>> CACHE =
      new ThreadLocal<WeakHashMap<Class<?>, ClassInfo>>() {
        @Override
        public WeakHashMap<Class<?>, ClassInfo> get() {
          return new WeakHashMap<Class<?>, ClassInfo>();
        }
      };

  public final Class<?> clazz;
  private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap =
      new IdentityHashMap<String, FieldInfo>();
  private final IdentityHashMap<Field, FieldInfo> fieldToFieldInfoMap =
      new IdentityHashMap<Field, FieldInfo>();

  public static ClassInfo of(Class<?> clazz) {
    WeakHashMap<Class<?>, ClassInfo> cache = CACHE.get();
    ClassInfo classInfo = cache.get(clazz);
    if (classInfo == null) {
      classInfo = new ClassInfo(clazz);
      cache.put(clazz, classInfo);
    }
    return classInfo;
  }

  public FieldInfo getFieldInfo(String fieldName) {
    return fieldName == null ? null : this.nameToFieldInfoMap.get(fieldName
        .intern());
  }

  public FieldInfo getFieldInfo(Field field) {
    return this.fieldToFieldInfoMap.get(field);
  }

  public String getFieldName(Field field) {
    FieldInfo fieldInfo = getFieldInfo(field);
    return fieldInfo == null ? null : fieldInfo.name;
  }

  public Field getField(String fieldName) {
    FieldInfo fieldInfo = getFieldInfo(fieldName);
    return fieldInfo == null ? null : fieldInfo.field;
  }

  public int getFieldCount() {
    return this.nameToFieldInfoMap.size();
  }

  public Iterable<String> getFieldNames() {
    return Collections.unmodifiableSet(this.nameToFieldInfoMap.keySet());
  }

  public static <T> T newInstance(Class<T> clazz) {
    T newInstance;
    try {
      newInstance = clazz.newInstance();
    } catch (IllegalAccessException e) {
      throw handleExceptionForNewInstance(e, clazz);
    } catch (InstantiationException e) {
      throw handleExceptionForNewInstance(e, clazz);
    }
    return newInstance;
  }

  private static IllegalArgumentException handleExceptionForNewInstance(
      Exception e, Class<?> clazz) {
    StringBuilder buf =
        new StringBuilder("unable to create new instance of class ")
            .append(clazz.getName());
    boolean first = true;
    if (Modifier.isAbstract(clazz.getModifiers())) {
      buf.append(" (and) because it is abstract");
    }
    if (clazz.getEnclosingClass() != null
        && !Modifier.isStatic(clazz.getModifiers())) {
      buf.append(" (and) because it is not static");
    }
    if (!Modifier.isPublic(clazz.getModifiers())) {
      buf.append(" (and) because it is not public");
    } else {
      try {
        clazz.getConstructor();
      } catch (NoSuchMethodException e1) {
        buf.append(" (and) because it has no public default constructor");
      }
    }
    throw new IllegalArgumentException(buf.toString(), e);
  }

  /**
   * Returns whether the given class is one of the supported primitive types
   * like number and date/time.
   */
  public static boolean isPrimitive(Class<?> clazz) {
    return clazz.isPrimitive() || clazz == Character.class
        || clazz == String.class || clazz == Integer.class
        || clazz == Long.class || clazz == Short.class || clazz == Byte.class
        || clazz == Float.class || clazz == Double.class
        || clazz == BigInteger.class || clazz == BigDecimal.class
        || clazz == DateTime.class || clazz == Boolean.class;
  }

  /**
   * Returns whether to given value is {@code null} or its class is primitive as
   * defined by {@link #isPrimitive(Class)}.
   */
  public static boolean isPrimitive(Object value) {
    return value == null || isPrimitive(value.getClass());
  }

  public static Collection<Object> newCollectionInstance(
      Class<?> collectionClass) {
    if (collectionClass != null
        && 0 == (collectionClass.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE))) {
      @SuppressWarnings("unchecked")
      Collection<Object> result =
          (Collection<Object>) ClassInfo.newInstance(collectionClass);
      return result;
    }
    if (collectionClass == null
        || collectionClass.isAssignableFrom(ArrayList.class)) {
      return new ArrayList<Object>();
    }
    if (collectionClass.isAssignableFrom(HashSet.class)) {
      return new HashSet<Object>();
    }
    if (collectionClass.isAssignableFrom(TreeSet.class)) {
      return new TreeSet<Object>();
    }
    throw new IllegalArgumentException(
        "no default collection class defined for class: "
            + collectionClass.getName());
  }

  public static Map<String, Object> newMapInstance(Class<?> mapClass) {
    if (mapClass != null
        && 0 == (mapClass.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE))) {
      @SuppressWarnings("unchecked")
      Map<String, Object> result =
          (Map<String, Object>) ClassInfo.newInstance(mapClass);
      return result;
    }
    if (mapClass == null || mapClass.isAssignableFrom(ArrayMap.class)) {
      return ArrayMap.create();
    }
    if (mapClass == null || mapClass.isAssignableFrom(TreeMap.class)) {
      return new TreeMap<String, Object>();
    }
    throw new IllegalArgumentException(
        "no default map class defined for class: " + mapClass.getName());
  }

  public static Class<?> getCollectionParameter(Field field) {
    if (field != null) {
      Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType) {
        Type[] typeArgs =
            ((ParameterizedType) genericType).getActualTypeArguments();
        if (typeArgs.length == 1 && typeArgs[0] instanceof Class<?>) {
          return (Class<?>) typeArgs[0];
        }
      }
    }
    return null;
  }

  public static Class<?> getMapValueParameter(Field field) {
    if (field != null) {
      Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType) {
        Type[] typeArgs =
            ((ParameterizedType) genericType).getActualTypeArguments();
        if (typeArgs.length == 2 && typeArgs[1] instanceof Class<?>) {
          return (Class<?>) typeArgs[1];
        }
      }
    }
    return null;
  }

  private ClassInfo(Class<?> clazz) {
    this.clazz = clazz;
    Field[] fields = clazz.getFields();
    int fieldsSize = fields.length;
    IdentityHashMap<String, FieldInfo> nameToFieldInfoMap =
        this.nameToFieldInfoMap;
    IdentityHashMap<Field, FieldInfo> fieldToFieldInfoMap =
        this.fieldToFieldInfoMap;
    for (int fieldsIndex = 0; fieldsIndex < fieldsSize; fieldsIndex++) {
      Field field = fields[fieldsIndex];
      String fieldName;
      Hide hide = field.getAnnotation(Hide.class);
      if (hide != null) {
        continue;
      }
      Name name = field.getAnnotation(Name.class);
      if (name == null) {
        fieldName = field.getName();
      } else {
        fieldName = name.value();
      }
      fieldName = fieldName.intern();
      FieldInfo fieldInfo = new FieldInfo(field, fieldName);
      nameToFieldInfoMap.put(fieldName, fieldInfo);
      fieldToFieldInfoMap.put(field, fieldInfo);
    }
  }
}
