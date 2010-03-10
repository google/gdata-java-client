package com.google.api.data.client.v2.android.atom;

import android.util.Xml;

import com.google.api.data.client.v2.atom.GDataXmlParserFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class AndroidGDataXmlParserFactory implements GDataXmlParserFactory {

  public XmlPullParser createParser() {
    return Xml.newPullParser();
  }

  public XmlSerializer createSerializer() {
    return Xml.newSerializer();
  }

}
