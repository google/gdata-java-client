// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.books.v1.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class BooksAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("app", "http://purl.org/atom/app#");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("batch", "http://schemas.google.com/gdata/batch");
    builder.addNamespace("dc", "http://purl.org/dc/terms");
    builder.addNamespace("gbs", "http://schemas.google.com/books/2008");
    builder.addNamespace("gd", "http://schemas.google.com/g/2005");
    builder.addNamespace("openSearch",
        "http://a9.com/-/spec/opensearchrss/1.0/");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private BooksAtom() {
  }
}
