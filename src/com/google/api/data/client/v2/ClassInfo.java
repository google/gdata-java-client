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
  // TODO: FieldInfo with name, field type
  private static final ThreadLocal<WeakHashMap<Class<?>, ClassInfo>> CACHE =
      new ThreadLocal<WeakHashMap<Class<?>, ClassInfo>>() {
        @Override
        public WeakHashMap<Class<?>, ClassInfo> get() {
          return new WeakHashMap<Class<?>, ClassInfo>();
        }
      };

  public final Class<?> clazz;
  private final IdentityHashMap<String, Field> nameToFieldMap =
      new IdentityHashMap<String, Field>();

  public static ClassInfo of(Class<?> clazz) {
    WeakHashMap<Class<?>, ClassInfo> cache = CACHE.get();
    ClassInfo classInfo = cache.get(clazz);
    if (classInfo == null) {
      classInfo = new ClassInfo(clazz);
      cache.put(clazz, classInfo);
    }
    return classInfo;
  }

  public Field getField(String name) {
    return this.nameToFieldMap.get(name.intern());
  }

  public Iterable<String> getNames() {
    return Collections.unmodifiableSet(this.nameToFieldMap.keySet());
  }

  public static Object getValue(Field field, Object obj)
      throws IllegalArgumentException {
    try {
      return field.get(obj);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static void setValue(Field field, Object obj, Object value)
      throws IllegalArgumentException {
    try {
      field.set(obj, value);
    } catch (SecurityException e) {
      throw new IllegalArgumentException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
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
    IdentityHashMap<String, Field> nameToFieldMap = this.nameToFieldMap;
    for (int fieldsIndex = 0; fieldsIndex < fieldsSize; fieldsIndex++) {
      Field field = fields[fieldsIndex];
      String fieldName;
      Name name = field.getAnnotation(Name.class);
      if (name == null) {
        fieldName = field.getName();
      } else {
        fieldName = name.value();
      }
      nameToFieldMap.put(fieldName.intern(), field);
    }
  }
}
