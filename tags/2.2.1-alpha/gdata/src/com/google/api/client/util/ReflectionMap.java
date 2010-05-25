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
 * Map that uses {@link ClassInfo} to parse the key/value pairs into a map.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class ReflectionMap extends AbstractMap<String, Object> {

  private final int size;
  private EntrySet entrySet;
  private final ClassInfo classInfo;
  private final Object object;

  public ReflectionMap(Object object) {
    this.object = object;
    ClassInfo classInfo = this.classInfo = ClassInfo.of(object.getClass());
    this.size = classInfo.getKeyCount();
  }

  // TODO: implement more methods for faster implementation!

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    EntrySet entrySet = this.entrySet;
    if (entrySet == null) {
      entrySet = this.entrySet = new EntrySet();
    }
    return entrySet;
  }

  final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
      return new EntryIterator();
    }

    @Override
    public int size() {
      return ReflectionMap.this.size;
    }
  }

  final class EntryIterator implements Iterator<Map.Entry<String, Object>> {

    private final Iterator<String> fieldNamesIterator;

    EntryIterator() {
      this.fieldNamesIterator =
          ReflectionMap.this.classInfo.getKeyNames().iterator();
    }

    public boolean hasNext() {
      return this.fieldNamesIterator.hasNext();
    }

    public Map.Entry<String, Object> next() {
      String fieldName = this.fieldNamesIterator.next();
      return new Entry(ReflectionMap.this.object, fieldName);
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  static final class Entry implements Map.Entry<String, Object> {

    private boolean isFieldValueComputed;

    private final String fieldName;

    private Object fieldValue;

    private final Object object;

    private final ClassInfo classInfo;

    public Entry(Object object, String fieldName) {
      this.classInfo = ClassInfo.of(object.getClass());
      this.object = object;
      this.fieldName = fieldName;
    }

    public String getKey() {
      return this.fieldName;
    }

    public Object getValue() {
      if (this.isFieldValueComputed) {
        return this.fieldValue;
      }
      this.isFieldValueComputed = true;
      FieldInfo fieldInfo = this.classInfo.getFieldInfo(this.fieldName);
      return this.fieldValue = fieldInfo.getValue(this.object);
    }

    public Object setValue(Object value) {
      FieldInfo fieldInfo = this.classInfo.getFieldInfo(this.fieldName);
      Object oldValue = getValue();
      fieldInfo.setValue(this.object, value);
      this.fieldValue = value;
      return oldValue;
    }
  }
}
