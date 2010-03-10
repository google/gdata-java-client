package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.atom.GDataXmlParserFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

/** Default GData XML parser factory. */
public final class DefaultGDataXmlParserFactory implements
    GDataXmlParserFactory {

  private final XmlPullParserFactory factory;

  public DefaultGDataXmlParserFactory() throws XmlPullParserException {
    factory =
        XmlPullParserFactory.newInstance(System
            .getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
    factory.setNamespaceAware(true);
  }

  public XmlPullParser createParser() throws XmlPullParserException {
    return this.factory.newPullParser();
  }

  public XmlSerializer createSerializer() throws XmlPullParserException {
    return this.factory.newSerializer();
  }
}
