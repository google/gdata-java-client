package com.google.api.data.client.v2.jsonc;

import com.google.api.data.client.entity.Entity;

/**
 * Arbitrary JSON entity object that stores all unknown keys. Subclasses can
 * declare public fields for keys they know, and those keys will be taken into
 * account as well.
 */
public class JsoncEntity extends Entity implements Cloneable {

  @Override
  public String toString() {
    return Jsonc.toString(this);
  }

  @Override
  public JsoncEntity clone() {
    return Jsonc.clone(this);
  }
}
