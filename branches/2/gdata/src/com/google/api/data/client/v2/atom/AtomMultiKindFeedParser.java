// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.FieldInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

/** GData Atom feed parser when the item class can be computed from the kind. */
public final class AtomMultiKindFeedParser<T> extends AbstractAtomFeedParser<T> {

  private final HashMap<String, Class<?>> kindToEntryClassMap =
      new HashMap<String, Class<?>>();

  AtomMultiKindFeedParser(AtomClient client, XmlPullParser parser,
      InputStream inputStream, Class<T> feedClass, Class<?>... itemClasses) {
    super(client, parser, inputStream, feedClass);
    int numItems = itemClasses.length;
    HashMap<String, Class<?>> kindToEntryClassMap = this.kindToEntryClassMap;
    for (int i = 0; i < numItems; i++) {
      Class<?> entryClass = itemClasses[i];
      ClassInfo typeInfo = ClassInfo.of(entryClass);
      Field field = typeInfo.getField("@gd:kind");
      if (field == null) {
        throw new IllegalArgumentException("missing @gd:kind field for "
            + entryClass.getName());
      }
      Object item = ClassInfo.newInstance(entryClass);
      String kind = (String) FieldInfo.getFieldValue(field, item);
      if (kind == null) {
        throw new IllegalArgumentException(
            "missing value for @gd:kind field in " + entryClass.getName());
      }
      kindToEntryClassMap.put(kind, entryClass);
    }
  }

  @Override
  Object parseEntryInternal() throws IOException, XmlPullParserException {
    XmlPullParser parser = this.parser;
    String kind = parser.getAttributeValue(AtomClient.GD_NAMESPACE, "kind");
    Class<?> itemClass = this.kindToEntryClassMap.get(kind);
    if (itemClass == null) {
      throw new IllegalArgumentException("unrecognized kind: " + kind);
    }
    return client.parseElement(parser, itemClass, false);
  }
}
