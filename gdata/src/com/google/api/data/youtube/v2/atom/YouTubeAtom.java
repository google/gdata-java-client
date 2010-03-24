// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.youtube.v2.atom;

import com.google.api.data.client.v2.atom.NamespaceDictionary;

public final class YouTubeAtom {

  public static final NamespaceDictionary NAMESPACE_DICTIONARY;
  static {
    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();
    builder.addNamespace("", "http://www.w3.org/2005/Atom");
    builder.addNamespace("app", "http://www.w3.org/2007/app");
    builder.addNamespace("atom", "http://www.w3.org/2005/Atom");
    builder.addNamespace("batch", "http://schemas.google.com/gdata/batch");
    builder.addNamespace("gd", "http://schemas.google.com/g/2005");
    builder.addNamespace("georss", "http://www.georss.org/georss");
    builder.addNamespace("gml", "http://www.opengis.net/gml");
    builder.addNamespace("media", "http://search.yahoo.com/mrss/");
    builder.addNamespace("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
    builder.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    builder.addNamespace("yt", "http://gdata.youtube.com/schemas/2007");
    NAMESPACE_DICTIONARY = builder.build();
  }

  private YouTubeAtom() {
  }
}
