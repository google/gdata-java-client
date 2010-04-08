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

package com.google.api.client.http.xml.atom;

import com.google.api.client.ClassInfo;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.xml.atom.googleapis.MultiKindFeedParser;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import com.google.api.client.xml.atom.AtomFeedParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public final class AtomHttp {

  public static XmlPullParser processAsXmlPullParser(HttpResponse response,
      InputStream content) throws XmlPullParserException {
    String contentType = response.getContentType();
    if (!contentType.startsWith(Atom.CONTENT_TYPE)) {
      throw new IllegalArgumentException("Wrong content type: expected <"
          + Atom.CONTENT_TYPE + "> but got <" + contentType + ">");
    }
    XmlPullParser result = Xml.createParser();
    result.setInput(content, null);
    return result;
  }


  public static <T> T parse(HttpResponse response,
      Class<T> classToInstantiateAndParse,
      XmlNamespaceDictionary namespaceDictionary) throws IOException,
      XmlPullParserException {
    InputStream content = response.getContent();
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      T result = ClassInfo.newInstance(classToInstantiateAndParse);
      Xml.parseElement(parser, result, namespaceDictionary, null);
      return result;
    } finally {
      content.close();
    }
  }

  public static <T, I> AtomFeedParser<T, I> useFeedParser(
      HttpResponse response, XmlNamespaceDictionary namespaceDictionary,
      Class<T> feedClass, Class<I> entryClass) throws XmlPullParserException,
      IOException {
    InputStream content = response.getContent();
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      AtomFeedParser<T, I> result = new AtomFeedParser<T, I>();
      result.parser = parser;
      result.inputStream = content;
      result.feedClass = feedClass;
      result.entryClass = entryClass;
      result.namespaceDictionary = namespaceDictionary;
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  public static <T, I> MultiKindFeedParser<T> useMultiKindFeedParser(
      HttpResponse response, XmlNamespaceDictionary namespaceDictionary,
      Class<T> feedClass, Class<?>... entryClasses)
      throws XmlPullParserException, IOException {
    InputStream content = response.getContent();
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      MultiKindFeedParser<T> result = new MultiKindFeedParser<T>();
      result.parser = parser;
      result.inputStream = content;
      result.feedClass = feedClass;
      result.namespaceDictionary = namespaceDictionary;
      result.setEntryClasses(entryClasses);
      return result;
    } finally {
      content.close();
    }
  }
}
