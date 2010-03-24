// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.codesearch.v2.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class CodeSearchAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("gcs", "http://schemas.google.com/codesearch/2006");
    builder.addNamespace("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private CodeSearchAtom() {
  }
}
