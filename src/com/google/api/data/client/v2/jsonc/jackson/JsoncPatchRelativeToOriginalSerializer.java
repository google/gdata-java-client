// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.v2.ClassInfo;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

final class JsoncPatchRelativeToOriginalSerializer extends JsoncSerializer {

  private final Object originalItem;

  public JsoncPatchRelativeToOriginalSerializer(Object patchedItem,
      Object originalItem) {
    super(patchedItem);
    this.originalItem = originalItem;
  }

  @Override
  public void serializeData(JsonGenerator generator) throws IOException {
    IdentityHashMap<String, Object> changedFields =
        new IdentityHashMap<String, Object>();
    Object originalItem = this.originalItem;
    StringBuilder fieldsMaskBuf = new StringBuilder();
    boolean first = true;
    Object item = this.item;
    // TODO: handle Map
    // TODO: handle JsoncObject
    ClassInfo classInfo = ClassInfo.of(item.getClass());
    for (String name : classInfo.getNames()) {
      Field field = classInfo.getField(name);
      Object patchedValue = ClassInfo.getValue(field, item);
      Object originalValue = ClassInfo.getValue(field, originalItem);
      Class<?> fieldClass = field.getType();
      // TODO: handle Map and List values
      if (patchedValue != originalValue && ClassInfo.isImmutable(fieldClass)
          && (patchedValue == null || !patchedValue.equals(originalValue))) {
        if (first) {
          first = false;
        } else {
          fieldsMaskBuf.append(',');
        }
        fieldsMaskBuf.append(name);
        changedFields.put(name, patchedValue);
      }
    }
    generator.writeStartObject();
    generator.writeStringField("fields", fieldsMaskBuf.toString());
    generator.writeArrayFieldStart("entry");
    serialize(generator, changedFields);
  }
}
