// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.health.v2.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class HealthAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("ccr", "urn:astm-org:CCR");
    builder.addNamespace("gd", "http://schemas.google.com/g/2005");
    builder.addNamespace("h9m", "http://schemas.google.com/health/metadata");
    builder.addNamespace("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private HealthAtom() {
  }
}
