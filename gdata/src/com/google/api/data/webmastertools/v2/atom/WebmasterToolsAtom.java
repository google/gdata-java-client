// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.webmastertools.v2.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class WebmasterToolsAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("gd", "http://schemas.google.com/g/2005");
    builder.addNamespace("wt",
        "http://schemas.google.com/webmaster/tools/2007");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private WebmasterToolsAtom() {
  }
}
