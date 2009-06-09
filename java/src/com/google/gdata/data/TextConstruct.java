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
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * Abstract base class for text construct type.
 *
 * 
 */
public abstract class TextConstruct implements ITextConstruct {

  /**
   * Defines the possible text construct types: TEXT, HTML, and XHTML.
   */
  public static class Type {
    public static final int TEXT = 1;
    public static final int HTML = 2;
    public static final int XHTML = 3;
  }

  /** Returns this text construct's type (text, HTML, or XHTML). */
  public abstract int getType();

  /**
   * Returns {@code true} if this text construct has no contents.
   */
  public abstract boolean isEmpty();

  /** Returns a plain-text representation of this text construct. */
  public abstract String getPlainText();

  /** Language. Derived from the current state of {@code xml:lang}. */
  protected String lang;

  /** @return  the human language that this text construct is written in */
  public String getLang() { return lang; }

  /** Specifies the human language that this text construct is written in. */
  public void setLang(String v) { lang = v; }

  /**
   * Creates a text construct.
   * This method is convenient for some service implementations.
   * Note that XHTML is treated somewhat differently from text and HTML.
   *
   * @param   type
   *            the type of the new text construct (TEXT, HTML, or XHTML)
   *
   * @param   textOrHtml
   *            the contents to put in this text construct, if the type is
   *            TEXT or HTML.
   *            If type is XHTML, set this parameter to {@code null}.
   *
   * @param   xhtml
   *            the contents to put in this text construct, if the type is
   *            XHTML.
   *            If type is TEXT or HTML, set this parameter to {@code null}.
   *
   * @return  a {@link TextConstruct}, or {@code null} if invalid type.
   */
  public static TextConstruct create(int type,
                                     String textOrHtml,
                                     XmlBlob xhtml) {

    switch (type) {

    case Type.TEXT:
      PlainTextConstruct ptc = new PlainTextConstruct(textOrHtml);
      return ptc;

    case Type.HTML:
      HtmlTextConstruct htc = new HtmlTextConstruct(textOrHtml);
      return htc;

    case Type.XHTML:
      XhtmlTextConstruct xtc = new XhtmlTextConstruct(xhtml);
      return xtc;

    default:
      assert false : "Invalid type: " + String.valueOf(type);
      return null;
    }
  }


  /**
   * Construct a new plain text content with the given text.
   */
  public static TextConstruct plainText(String text) {
    return new PlainTextConstruct(text);
  }

  /**
   * Construct a new html text content with the given html.
   */
  public static TextConstruct html(String html) {
    return new HtmlTextConstruct(html);
  }

  /**
   * Construct a new Xhtml text content from the given div.
   */
  public static TextConstruct xhtml(XmlBlob div) {
    return new XhtmlTextConstruct(div);
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
  public abstract void generateAtom(XmlWriter w,
                                    String elementName) throws IOException;


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
  public abstract void generateRss(XmlWriter w,
                                   String elementName,
                                   RssFormat rssFormat) throws IOException;


  /** Enumerates the kinds of restrictions on what HTML tags are allowed. */
  public enum RssFormat {

    /** HTML/XHTML is converted to plain text. */
    PLAIN_TEXT,

    /** All tags are allowed. */
    FULL_HTML
  }


  /**
   * Parses XML in the Atom format.
   *
   * @param   attrs
   *            XML attributes of the root TextConstruct node.
   *            Used to determine the type of this node.
   *
   * @return  a child handler
   *
   * @throws  ParseException
   * @throws  IOException
   */
  public static ChildHandlerInfo getChildHandler(Attributes attrs)
      throws ParseException, IOException {

    String type = attrs.getValue("", "type");
    ChildHandlerInfo childHandlerInfo = new ChildHandlerInfo();

    if (type == null || type.equals("text") || type.equals("text/plain")) {

      PlainTextConstruct ptc = new PlainTextConstruct();
      childHandlerInfo.handler = ptc.new AtomHandler();
      childHandlerInfo.textConstruct = ptc;

    } else if (type.equals("html") || type.equals("text/html")) {

      HtmlTextConstruct htc = new HtmlTextConstruct();
      childHandlerInfo.handler = htc.new AtomHandler();
      childHandlerInfo.textConstruct = htc;

    } else if (type.equals("xhtml")) {

      XhtmlTextConstruct xtc = new XhtmlTextConstruct();
      childHandlerInfo.handler = xtc.new AtomHandler();
      childHandlerInfo.textConstruct = xtc;

    } else {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidTextContentType);
      pe.setInternalReason(
          "Invalid text content type: '" + type + "'");
      throw pe;
    }

    return childHandlerInfo;
  }


  /**
   * Return type for {@link #getChildHandler(Attributes)};
   * contains an element handler and a text construct.
   */
  public static class ChildHandlerInfo {
    public XmlParser.ElementHandler handler;
    public TextConstruct textConstruct;
  }
}
