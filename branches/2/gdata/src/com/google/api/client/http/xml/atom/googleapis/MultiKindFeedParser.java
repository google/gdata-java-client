/* Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.client.http.xml.atom.googleapis;

import com.google.api.client.ClassInfo;
import com.google.api.client.FieldInfo;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.atom.AbstractAtomFeedParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

/** GData Atom feed parser when the item class can be computed from the kind. */
public final class MultiKindFeedParser<T> extends AbstractAtomFeedParser<T> {

  private final HashMap<String, Class<?>> kindToEntryClassMap =
      new HashMap<String, Class<?>>();

  public MultiKindFeedParser(XmlPullParser parser, InputStream inputStream,
      Class<T> feedClass, Class<?>... itemClasses) {
    super(parser, inputStream, feedClass);
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
  protected Object parseEntryInternal() throws IOException,
      XmlPullParserException {
    XmlPullParser parser = this.parser;
    String kind = parser.getAttributeValue(GData.GD_NAMESPACE, "kind");
    Class<?> itemClass = this.kindToEntryClassMap.get(kind);
    if (itemClass == null) {
      throw new IllegalArgumentException("unrecognized kind: " + kind);
    }
    return Xml.parseElement(parser, itemClass, null);
  }
}
