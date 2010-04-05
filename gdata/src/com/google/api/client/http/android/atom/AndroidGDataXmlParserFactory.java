package com.google.api.client.http.android.atom;

import android.util.Xml;

import com.google.api.client.xml.XmlParserFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class AndroidGDataXmlParserFactory implements XmlParserFactory {

  public XmlPullParser createParser() {
    return Xml.newPullParser();
  }

  public XmlSerializer createSerializer() {
    return Xml.newSerializer();
  }

}
