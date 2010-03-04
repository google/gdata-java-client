// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.FieldInfo;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

final class AtomPatchRelativeToOriginalSerializer extends AtomSerializer {

  private final Object originalItem;

  AtomPatchRelativeToOriginalSerializer(AtomClient client, Object patchedItem,
      Object originalItem) {
    super(client, patchedItem);
    this.originalItem = originalItem;
  }

  @Override
  void serialize(XmlSerializer serializer) throws IOException {
    IdentityHashMap<String, Object> patch = new IdentityHashMap<String, Object>();
    Object originalItem = this.originalItem;
    StringBuilder fieldsMaskBuf = new StringBuilder();
    boolean first = true;
    Object entry = this.entry;
    // TODO: this needs a lot of work
    ClassInfo typeInfo = ClassInfo.of(entry.getClass());
    for (String name : typeInfo.getNames()) {
      Field field = typeInfo.getField(name);
      Object patchedValue = FieldInfo.getFieldValue(field, entry);
      Object originalValue = FieldInfo.getFieldValue(field, originalItem);
      Class<?> fieldType = field.getType();
      if (patchedValue != originalValue && ClassInfo.isImmutable(fieldType)
          && (patchedValue == null || !patchedValue.equals(originalValue))) {
        if (first) {
          first = false;
        } else {
          fieldsMaskBuf.append(',');
        }
        fieldsMaskBuf.append(name);
        patch.put(name, patchedValue);
      }
    }
    patch.put("@gd:fields", fieldsMaskBuf.toString());
    serializeEntry(serializer, patch);
  }
  
}
