package com.google.api.data.client.v2.atom;

import com.google.api.data.client.entity.Entity;

/**
 * Arbitrary Atom entity object that stores all unknown keys. Subclasses can
 * declare public fields for keys they know, and those keys will be taken into
 * account as well.
 */
public class AtomEntity extends Entity implements Cloneable {

  @Override
  public AtomEntity clone() {
    return Atom.clone(this);
  }
}
