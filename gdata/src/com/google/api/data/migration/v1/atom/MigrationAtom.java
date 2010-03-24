// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.migration.v1.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class MigrationAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("apps", "http://schemas.google.com/apps/2006");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("batch", "http://schemas.google.com/gdata/batch");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private MigrationAtom() {
  }
}
