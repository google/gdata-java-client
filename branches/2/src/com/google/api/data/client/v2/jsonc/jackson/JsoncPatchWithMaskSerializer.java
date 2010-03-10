// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.http.HttpRequest;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;

public final class JsoncPatchWithMaskSerializer extends JsoncSerializer {

  private final String partialMask;

  JsoncPatchWithMaskSerializer(Object patchedItem, String partialMask) {
    super(patchedItem);
    this.partialMask = partialMask;
  }

  public static void setContent(HttpRequest request, Object patchedItem,
      String partialMask) {
    request.setContent(new JsoncPatchWithMaskSerializer(patchedItem,
        partialMask));
  }

  @Override
  public void serializeData(JsonGenerator generator) throws IOException {
    generator.writeStringField("fields", this.partialMask);
    generator.writeArrayFieldStart("entry");
    serialize(generator, this.item);
  }
}
