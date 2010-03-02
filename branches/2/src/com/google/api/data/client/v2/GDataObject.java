package com.google.api.data.client.v2;

import com.google.api.data.client.v2.ClassInfo;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Arbitrary object that stores all unknown keys. Subclasses can declare public
 * fields for keys they know, and those keys will be taken into account as well.
 */
public class GDataObject {
  
  private IdentityHashMap<String, Object> unknown = null;
  
  private final ClassInfo classInfo = ClassInfo.of(getClass());
  
  public final Object get(String key) {
    key = key.intern();
    Field field = classInfo.getField(key);
    if (field != null) {
      return ClassInfo.getValue(field, this);
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
      ClassInfo.setValue(field, this, value);
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
}
