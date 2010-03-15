package com.google.api.data.client.v2;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Arbitrary entity object that stores all unknown keys. Subclasses can declare
 * public fields for keys they know, and those keys will be taken into account
 * as well.
 */
public class GDataEntity {

  private IdentityHashMap<String, Object> unknown;

  private final ClassInfo classInfo = ClassInfo.of(getClass());

  public final Object get(String key) {
    key = key.intern();
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(key);
    if (fieldInfo != null) {
      return fieldInfo.getValue(this);
    }
    Object value = null;
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown != null) {
      value = unknown.get(key);
    }
    return value;
  }

  public final void set(String key, Object value) {
    key = key.intern();
    Field field = this.classInfo.getField(key);
    if (field != null) {
      FieldInfo.setFieldValue(field, this, value);
      return;
    }
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown == null) {
      this.unknown = unknown = new IdentityHashMap<String, Object>();
    }
    unknown.put(key, value);
  }

  public final Map<String, Object> getUnknownKeyMap() {
    IdentityHashMap<String, Object> unknown = this.unknown;
    if (unknown == null) {
      return Collections.emptyMap();
    }
    return Collections.unmodifiableMap(unknown);
  }

  /**
   * Merges content of another object to this entity. If there are any
   * conflicts, the passed in entity wins.
   * 
   * @param from entity to merge from or {@code null} to ignore
   * @return merged entity
   */
  public final GDataEntity merge(Object from) {
    if (from == null) {
      return this;
    }
    ClassInfo classInfo = ClassInfo.of(from.getClass());
    for (String name : classInfo.getNames()) {
      Field field = classInfo.getField(name);
      Object value = FieldInfo.getFieldValue(field, from);
      set(name, value);
    }
    if (from instanceof GDataEntity) {
      for (Map.Entry<String, Object> entry : ((GDataEntity) from)
          .getUnknownKeyMap().entrySet()) {
        set(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }
}
