// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

import java.util.Map;

public class FieldIterators {

  @SuppressWarnings("unchecked")
  public static FieldIterator of(Object object) {
    if (object instanceof Map<?, ?>) {
      return new MapFieldIterator((Map<String, ?>) object);
    }
    ReflectionFieldIterator reflect = new ReflectionFieldIterator(object);
    if (object instanceof Entity) {
      Entity entity = (Entity) object;
      return new CombineFieldIterator(reflect, new EntityUnknownFieldIterator(
          entity));
    }
    return reflect;
  }

  private FieldIterators() {
  }

}
