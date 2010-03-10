// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * Factory for creating new XML pull parsers and XML serializers.
 */
public interface GDataXmlParserFactory {

  /**
   * Creates a new XML pull parser.
   * 
   * @throws XmlPullParserException if parser could not be created
   */
  XmlPullParser createParser() throws XmlPullParserException;

  /**
   * Creates a new XML serializer.
   * 
   * @throws XmlPullParserException if serializer could not be created
   */
  XmlSerializer createSerializer() throws XmlPullParserException;
}
