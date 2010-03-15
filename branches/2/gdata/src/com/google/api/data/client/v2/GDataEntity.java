package com.google.api.data.client.v2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Arbitrary entity object that stores all unknown keys. Subclasses can declare
 * public fields for keys they know, and those keys will be taken into account
 * as well.
 */
public class GDataEntity {

  private IdentityHashMap<String, Object> unknown = null;

  private final ClassInfo classInfo = ClassInfo.of(getClass());

  public final Object get(String key) {
    key = key.intern();
    FieldInfo fieldInfo = classInfo.getFieldInfo(key);
    if (fieldInfo != null) {
      return fieldInfo.getValue(this);
    }
    Object value = null;
    if (unknown != null) {
      value = unknown.get(key);
    }
    return value;
  }

  public final void set(String key, Object value) {
    key = key.intern();
    Field field = classInfo.getField(key);
    if (field != null) {
      FieldInfo.setFieldValue(field, this, value);
      return;
    }
    if (unknown == null) {
      unknown = new IdentityHashMap<String, Object>();
    }
    unknown.put(key, value);
  }

  public final Map<String, Object> getUnknownKeyMap() {
    if (unknown == null) {
      return Collections.emptyMap();
    }
    return Collections.unmodifiableMap(unknown);
  }
  
  /**
   * Merges content of another object to this entity.
   * 
   * @param from entity to merge from
   * @return merged entity
   */
  public final GDataEntity merge(Object from) {
    if (from == null) {
      return this;
    }
    try {
      for (Field f : from.getClass().getFields()) {
        if (Modifier.isPublic(f.getModifiers())) {
          set(f.getName(), f.get(from));
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("should not happen");
    }
    if (from instanceof GDataEntity) {
      for (Map.Entry<String, Object> entry :
          ((GDataEntity) from).getUnknownKeyMap().entrySet()) {
        set(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }
}
