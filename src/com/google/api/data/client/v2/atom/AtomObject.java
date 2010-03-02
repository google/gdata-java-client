package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.GDataObject;

/**
 * Arbitrary Atom object that stores all unknown keys. Subclasses can declare
 * public fields for keys they know, and those keys will be taken into account
 * as well.
 */
public class AtomObject extends GDataObject implements Cloneable {

  @Override
  public AtomObject clone() {
    return Atom.clone(this);
  }
}
