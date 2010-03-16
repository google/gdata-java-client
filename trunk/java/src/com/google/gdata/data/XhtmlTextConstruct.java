/* Copyright (c) 2008 Google Inc.
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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlParser;

import com.google.gdata.util.common.html.HtmlToText;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * XHTML variant of {@link TextConstruct}.
 *
 * 
 */
public class XhtmlTextConstruct extends TextConstruct {

  /** Class constructor. */
  public XhtmlTextConstruct() {}

  /**
   * Class constructor specifying the XHTML content for this
   * text construct to contain.
   */
  public XhtmlTextConstruct(XmlBlob xhtml) {
    this.xhtml = xhtml;
  }

  /** 
   * Class constructor specifying the XHTML content for this
   * text construct to contain, plus the human language that
   * the text is written in.
   */
  public XhtmlTextConstruct(XmlBlob xhtml, String lang) {
    this.xhtml = xhtml;
    this.lang = lang;
  }

  /** @return the type (XHTML) of this text construct */
  @Override
  public int getType() { return Type.XHTML; }
  
  /** @return {@code true} if this text construct has no contents */
  @Override
  public boolean isEmpty() { return xhtml == null; }


  /** XHTML contents. */
  protected XmlBlob xhtml;
  /** @return the XHTML contents of this text construct */
  public XmlBlob getXhtml() {
    if (xhtml == null) {
      xhtml = new XmlBlob();  // init on demand
    }
    return xhtml;
  }
  /** Specifies the XHTML contents of this text construct. */
  public void setXhtml(XmlBlob v) { xhtml = v; }

  /**
   * @return a plain-text representation of this text construct,
   *         or {@code null} if there is no text content available.
   */
  @Override
  public String getPlainText() {

    if (xhtml == null) {
      return null;
    }
    try {

      StringWriter sw = new StringWriter();
      XmlWriter xw = new XmlWriter(sw);
      xw.innerXml(xhtml.getBlob());

      return HtmlToText.htmlToPlainText(sw.toString());

    } catch (IOException e) {
      // IOException isn't very meaningful to callers, so it's better to
      // return null in this case.
      return null;
    }
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   *
   * @param   elementName
   *            Atom element name
   *
   * @throws  IOException
   */
  @Override
  public void generateAtom(XmlWriter w,
                           String elementName) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(2);

    attrs.add(new XmlWriter.Attribute("type", "xhtml"));

    if (lang != null) {
      attrs.add(new XmlWriter.Attribute("xml:lang", lang));
    }

    XmlBlob.startElement(w, Namespaces.atomNs, elementName, xhtml, attrs, null);
    XmlBlob.endElement(w, Namespaces.atomNs, elementName, xhtml);
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   *
   * @param   elementName
   *            RSS element name
   *
   * @param   rssFormat
   *            the restrictions on what HTML tags are allowed
   *
   * @throws  IOException
   */
  @Override
  public void generateRss(XmlWriter w,
                          String elementName,
                          RssFormat rssFormat) throws IOException {

    switch (rssFormat) {

    case FULL_HTML:

      // Because RSS doesn't allow child markup, we convert XHTML into an
      // encoded string, just like HTML.

      StringWriter sw = new StringWriter();
      XmlWriter xw = new XmlWriter(sw);
      xw.innerXml(xhtml.getBlob());

      // At this point, sw contains the <xhtml:div> element.
      w.simpleElement(Namespaces.rssNs, elementName, null, sw.toString());
      break;

    case PLAIN_TEXT:
      w.simpleElement(Namespaces.rssNs, elementName, null, getPlainText());
      break;

    default:
      assert false;
    }
  }

  /** Parses XML in the Atom format. */
  public class AtomHandler extends XmlParser.ElementHandler {

    /**
     * Class constructor.
     *
     * @throws IOException
     */
    public AtomHandler() throws IOException {
      xhtml = new XmlBlob();
      initializeXmlBlob(xhtml, true, true);
      lang = xmlLang;
    }

    /**
     * Sets the language.
     *
     * @throws ParseException
     */
    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals(Namespaces.xml) && localName.equals("lang")) {
        lang = xmlLang;
      }
    }
  }
}
