// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
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
    return this.nameToFieldInfoMap.get(fieldName.intern());
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

  public Iterable<String> getNames() {
    return Collections.unmodifiableSet(this.nameToFieldInfoMap.keySet());
  }

  public static <T> T newInstance(Class<T> clazz) {
    T newInstance;
    try {
      newInstance = clazz.newInstance();
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "unable to create new instance of class "
              + clazz
              + " (is it public and does it have a public default constructor?)",
          e);
    } catch (InstantiationException e) {
      StringBuilder buf =
          new StringBuilder("unable to create new instance of class " + clazz);
      if (Modifier.isAbstract(clazz.getModifiers())) {
        buf.append(" because it is abstract");
      }
      if (clazz.getEnclosingClass() != null
          && !Modifier.isStatic(clazz.getModifiers())) {
        buf.append(" because it is not static");
      }
      throw new IllegalArgumentException(buf.toString());
    }
    return newInstance;
  }

  /**
   * Returns whether the given class is a primitive type or one of the supported
   * GData immutable types like number and date/time.
   */
  public static boolean isImmutable(Class<?> clazz) {
    return clazz.isPrimitive() || clazz == Character.class
        || clazz == String.class || clazz == Integer.class
        || clazz == Long.class || clazz == Short.class || clazz == Byte.class
        || clazz == Float.class || clazz == Double.class
        || clazz == BigInteger.class || clazz == BigDecimal.class
        || clazz == DateTime.class || clazz == Boolean.class;
  }

  public static <T> T newMapInstance(Class<T> mapClass) {
    if (mapClass == null || mapClass.isAssignableFrom(IdentityHashMap.class)) {
      @SuppressWarnings("unchecked")
      T result = (T) new IdentityHashMap<String, Object>();
      return result;
    }
    return ClassInfo.newInstance(mapClass);
  }

  public static <T> T newListInstance(Class<T> listClass, int size) {
    if (listClass == null || listClass.isAssignableFrom(ArrayList.class)) {
      @SuppressWarnings("unchecked")
      T result = (T) new ArrayList<Object>(size);
      return result;
    }
    try {
      Constructor<T> constructor = listClass.getConstructor(int.class);
      return constructor.newInstance(size);
    } catch (IllegalAccessException e) {
      throw constructorException(listClass, e);
    } catch (InstantiationException e) {
      throw constructorException(listClass, e);
    } catch (SecurityException e) {
      throw constructorException(listClass, e);
    } catch (NoSuchMethodException e) {
      throw constructorException(listClass, e);
    } catch (InvocationTargetException e) {
      throw constructorException(listClass, e);
    }
  }

  private static IllegalArgumentException constructorException(
      Class<?> listClass, Exception e) {
    return new IllegalArgumentException(
        "unable to create new instance of class " + listClass, e);
  }

  public static Class<?> getListParameter(Field field) {
    Class<?> subFieldClass = null;
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
