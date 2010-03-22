// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.util.List;
import java.util.NoSuchElementException;

public final class EntityUnknownFieldIterator implements FieldIterator {

  private final int size;

  private int index = -1;

  private final List<String> fieldNames; 

  private final List<Object> fieldValues; 
    
  EntityUnknownFieldIterator(Entity entity) {
    List<String> fieldNames = this.fieldNames = entity.unknownFieldNames;
    this.fieldValues = entity.unknownFieldValues;
    this.size = fieldNames == null ? 0 : fieldNames.size();
  }

  public String nextFieldName() {
    int index = ++this.index;
    if (index == this.size) {
      throw new NoSuchElementException();
    }
    return this.fieldNames.get(index);
  }

  public Object getFieldValue() {
    return this.fieldValues.get(this.index);
  }

  public boolean hasNext() {
    return this.index + 1 < this.size;
  }

}
