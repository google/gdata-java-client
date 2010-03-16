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

import com.google.gdata.util.common.html.HtmlToText;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * HTML variant of {@link TextConstruct}.
 *
 * 
 */
public class HtmlTextConstruct extends TextConstruct {

  /** Class constructor. */
  public HtmlTextConstruct() {}

  /**
   * Class constructor specifying the HTML content for this
   * text construct to contain.
   */
  public HtmlTextConstruct (String html) {
    this.html = html;
  }

  /** 
   * Class constructor specifying the HTML content for this
   * text construct to contain, plus the human language that
   * the text is written in.
   */
  public HtmlTextConstruct (String html, String lang) {
    this.html = html;
    this.lang = lang;
  }

  /** @return the type (HTML) of this text construct */
  @Override
  public int getType() { return Type.HTML; }
  
  /** @return {@code true} if this text construct has no contents */
  @Override
  public boolean isEmpty() { return getHtml() == null; }

  /** HTML contents. */
  protected String html;
  /** @return the HTML contents of this text construct */
  public String getHtml() { return html; }
  /** Specifies the HTML contents of this text construct. */
  public void setHtml(String v) { html = v; }

  /**
   * @return a plain-text representation of this text construct or
   *         {@code null} if there is no html content.
   */
  @Override
  public String getPlainText() {
    return !isEmpty() ? HtmlToText.htmlToPlainText(html) : null;
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

    attrs.add(new XmlWriter.Attribute("type", "html"));

    if (lang != null) {
      attrs.add(new XmlWriter.Attribute("xml:lang", lang));
    }

    w.simpleElement(Namespaces.atomNs, elementName, attrs, html);
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
      w.simpleElement(Namespaces.rssNs, elementName, null, html);
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
     * Processes attributes.
     *
     * @throws ParseException
     */
    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (!namespace.equals("") || !localName.equals("type")) {

        // Don't understand other attributes.
        super.processAttribute(namespace, localName, value);
      }
    }

    /**
     * Processes this element; overrides inherited method.
     * 
     * @throws ParseException from subclasses.
     */
    @Override
    public void processEndElement() throws ParseException {

      if (value == null) {
        value = "";
      }

      html = value;
      lang = xmlLang;
    }
  }
}
