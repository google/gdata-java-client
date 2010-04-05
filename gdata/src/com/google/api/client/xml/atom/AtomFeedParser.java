/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.xml.atom;

import com.google.api.client.xml.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/** Atom feed parser when the item class is known in advance. */
public final class AtomFeedParser<T, I> extends AbstractAtomFeedParser<T> {

  private final Class<I> entryClass;

  public AtomFeedParser(XmlPullParser parser, InputStream inputStream,
      Class<T> feedClass, Class<I> entryClass) {
    super(parser, inputStream, feedClass);
    this.entryClass = entryClass;
  }

  @SuppressWarnings("unchecked")
  @Override
  public I parseNextEntry() throws IOException, XmlPullParserException {
    return (I) super.parseNextEntry();
  }

  @Override
  protected Object parseEntryInternal() throws IOException,
      XmlPullParserException {
    return Xml.parseElement(parser, entryClass, null);
  }
}
