// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.util.Iterator;
import java.util.Map;

public final class MapFieldIterator<V> implements FieldIterator {

  private Map.Entry<String, V> entry;

  private final Iterator<Map.Entry<String, V>> entryIterator;
  
  public MapFieldIterator(Map<String, V> map) {
    this.entryIterator = map.entrySet().iterator();
  }
  
  public String nextFieldName() {
    Map.Entry<String, V> entry = this.entry = this.entryIterator.next();
    return entry.getKey();
  }

  public V getFieldValue() {
    return this.entry.getValue();
  }

  public boolean hasNext() {
    return this.entryIterator.hasNext();
  }
}
