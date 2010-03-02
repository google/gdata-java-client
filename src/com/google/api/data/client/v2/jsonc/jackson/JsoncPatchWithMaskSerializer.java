// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;

final class JsoncPatchWithMaskSerializer extends
    JsoncSerializer {

  private final String partialMask;

  public JsoncPatchWithMaskSerializer(Object patchedItem,
      String partialMask) {
    super(patchedItem);
    this.partialMask = partialMask;
  }

  @Override
  public void serializeData(JsonGenerator generator) throws IOException {
    generator.writeStringField("fields", this.partialMask);
    generator.writeArrayFieldStart("entry");
    serialize(generator, this.item);
  }
}
