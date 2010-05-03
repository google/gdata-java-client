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

package com.google.api.client.util;


import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Arbitrary entity object that stores all unknown field name/value pairs.
 * <p>
 * Subclasses can declare public fields for fields they do know, using the Java
 * field name as the field name or the {@link Name} annotation to override the
 * field name to use. Null field names are not allowed, but null field values
 * are allowed.
 */
public class Entity extends AbstractMap<String, Object> implements Cloneable {

  private volatile EntrySet entrySet;

  private ArrayMap<String, Object> unknownFields = ArrayMap.create();

  // TODO: implement more methods for faster implementation

  private final ClassInfo classInfo = ClassInfo.of(getClass());

  /** Returns the unknown fields map. */
  public ArrayMap<String, Object> getUnknownFields() {
    return this.unknownFields;
  }

  @Override
  public int size() {
    return this.classInfo.getFieldCount() + unknownFields.size();
  }

  @Override
  public final Object get(Object name) {
    if (!(name instanceof String)) {
      return null;
    }
    String fieldName = (String) name;
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
    if (fieldInfo != null) {
      return fieldInfo.getValue(this);
    }
    return this.unknownFields.get(fieldName);
  }

  @Override
  public final Object put(String name, Object value) {
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(name);
    if (fieldInfo != null) {
      Object oldValue = fieldInfo.getValue(this);
      fieldInfo.setValue(this, value);
      return oldValue;
    }
    return this.unknownFields.put(name, value);
  }

  /**
   * Sets the given field value (may be {@code null}) for the given field name.
   * Any existing value for the field will be overwritten. It may be more
   * slightly more efficient than {@link #put(String, Object)} because it avoids
   * accessing the field's original value.
   */
  public final void set(String name, Object value) {
    FieldInfo fieldInfo = this.classInfo.getFieldInfo(name);
    if (fieldInfo != null) {
      fieldInfo.setValue(this, value);
      return;
    }
    this.unknownFields.put(name, value);
  }

  @Override
  public final void putAll(Map<? extends String, ?> map) {
    for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
      set(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public final Object remove(Object name) {
    if (name instanceof String) {
      String fieldName = (String) name;
      FieldInfo fieldInfo = this.classInfo.getFieldInfo(fieldName);
      if (fieldInfo != null) {
        throw new UnsupportedOperationException();
      }
      return this.unknownFields.remove(name);
    }
    return null;
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    EntrySet entrySet = this.entrySet;
    if (entrySet == null) {
      entrySet = this.entrySet = new EntrySet();
    }
    return entrySet;
  }

  @Override
  public Entity clone() {
    try {
      @SuppressWarnings("unchecked")
      Entity result = (Entity) super.clone();
      Entities.cloneInternal(this, result);
      result.unknownFields = Entities.clone(this.unknownFields);
      return result;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
      return new EntryIterator();
    }

    @Override
    public int size() {
      return Entity.this.size();
    }
  }

  final class EntryIterator implements Iterator<Map.Entry<String, Object>> {

    private boolean startedUnknown;
    private final Iterator<String> fieldNamesIterator;
    private final Iterator<Map.Entry<String, Object>> unknownIterator;

    EntryIterator() {
      this.fieldNamesIterator =
          Entity.this.classInfo.getFieldNames().iterator();
      this.unknownIterator = Entity.this.unknownFields.entrySet().iterator();
    }

    public boolean hasNext() {
      return !this.startedUnknown && this.fieldNamesIterator.hasNext()
          || this.unknownIterator.hasNext();
    }

    public Map.Entry<String, Object> next() {
      if (!this.startedUnknown) {
        Iterator<String> fieldNamesIterator = this.fieldNamesIterator;
        if (this.fieldNamesIterator.hasNext()) {
          return new ReflectionMap.Entry(Entity.this, fieldNamesIterator.next());
        } else {
          this.startedUnknown = true;
        }
      }
      return this.unknownIterator.next();
    }

    public void remove() {
      if (this.startedUnknown) {
        this.unknownIterator.remove();
      }
      throw new UnsupportedOperationException();
    }
  }
}
