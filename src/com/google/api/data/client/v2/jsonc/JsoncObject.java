package com.google.api.data.client.v2.jsonc;

import com.google.api.data.client.v2.GDataObject;

/**
 * Arbitrary JSON-C object that stores all unknown keys. Subclasses can declare
 * public fields for keys they know, and those keys will be taken into account
 * as well.
 */
public class JsoncObject extends GDataObject implements Cloneable {

  @Override
  public String toString() {
    return Jsonc.toString(this);
  }

  @Override
  public JsoncObject clone() {
    return Jsonc.clone(this);
  }
}
