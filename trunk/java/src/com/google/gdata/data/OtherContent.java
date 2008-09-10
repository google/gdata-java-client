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

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Variant of {@link Content} for entries containing miscellaneous
 * inlined content types.
 *
 * 
 */
public class OtherContent extends Content {

  /** @return the type of this content */
  @Override
  public int getType() {
    if (ext != null || xml != null) {
      return Content.Type.OTHER_XML;
    } else if (text != null) {
      return Content.Type.OTHER_TEXT;
    } else {
      return Content.Type.OTHER_BINARY;
    }
  }

  /** MIME type. */
  protected ContentType mimeType;
  /** @return the MIME type */
  public ContentType getMimeType() { return mimeType; }
  /** Specifies the MIME type. */
  public void setMimeType(ContentType v) { mimeType = v; }

  /**
   * Language. Derived from the current state of {@code xml:lang}.
   * Applies to inline text and binary types only.
   */
  protected String lang;
  /** @return  the human language that this content is written in */
  @Override
  public String getLang() { return lang; }
  /** Specifies the human language that this content is written in. */
  public void setLang(String v) { lang = v; }

  /** Extension content.  Allows an extension to be set as nested content. */
  protected Extension ext;
  /** Returns the nested xml content. */
  public Extension getXmlContent() { return ext; }
  /** Sets the nested xml content. */
  public void setXmlContent(Extension extension) {
    ext = extension;
    xml = null;
    text = null;
    bytes = null;
  }
  
  /** XML contents. Valid only for XML types (ending with "+xml" or "/xml"). */
  protected XmlBlob xml;
  /** @return the XML contents */
  public XmlBlob getXml() { return xml; }
  /** Specifies the XML contents. */
  public void setXml(XmlBlob v) {
    ext = null;
    xml = v;
    text = null;
    bytes = null;
  }
  
  /** Text contents. Valid only for text types (starting with "text/"). */
  protected String text;
  /** @return the plain text contents */
  public String getText() { return text; }
  /** Specifies the plain-text contents. */
  public void setText(String v) {
    ext = null;
    xml = null;
    text = v;
    bytes = null;
  }

  /** Binary Contents. Valid if neither XML nor text contents are valid. */
  protected byte[] bytes;
  /** @return the binary contents */
  public byte[] getBytes() { return bytes; }
  /** Specifies the binary contents. */
  public void setBytes(byte[] v) {
    ext = null;
    xml = null;
    bytes = v;
    text = null;
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            Extension Profile for nested extensions
   *
   * @throws  IOException
   */
  @Override
  public void generateAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(2);

    if (mimeType != null) {
      attrs.add(new XmlWriter.Attribute("type", mimeType.getMediaType()));
    }

    if (ext != null) {
      w.startElement(Namespaces.atomNs, "content", attrs, null);
      ext.generate(w, extProfile);
      w.endElement(Namespaces.atomNs, "content");
    } else if (xml != null) {
      XmlBlob.startElement(w, Namespaces.atomNs, "content", xml, attrs, null);
      XmlBlob.endElement(w, Namespaces.atomNs, "content", xml);
    } else {
      
      String value;
      if (text != null) {
        value = text;
        if (lang != null) {
          attrs.add(new XmlWriter.Attribute("xml:lang", lang));
        }
      } else if (bytes != null) {
        value = Base64.encode(bytes);
        if (lang != null) {
          attrs.add(new XmlWriter.Attribute("xml:lang", lang));
        }
      } else {
        value = null;
      }

      w.simpleElement(Namespaces.atomNs, "content", attrs, value);
    }
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            Extension Profile for nested extensions
   *
   * @throws  IOException
   */
  @Override
  public void generateRss(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    // If content has a link instead of a body, generate a <link> element.
    if (getType() == Content.Type.OTHER_TEXT) {
      w.simpleElement(Namespaces.rssNs, "description", null, text);
    } else {
      // RSS doesn't support non-text content, so we use the Atom type.
      generateAtom(w, extProfile);
    }
  }

  /** Parses XML in the Atom format. */
  public class AtomHandler extends XmlParser.ElementHandler {

    private final ExtensionProfile extProfile;
    private final int type;

    /**
     * Class constructor specifying attributes.
     *
     * @throws   IOException
     */
    AtomHandler(ExtensionProfile extProfile, Attributes attrs)
        throws IOException {

      this.extProfile = extProfile;
      String typeAttr = attrs.getValue("", "type");

      if ("application/atom+xml;type=feed".equals(typeAttr)) {
        ext = new Feed();
        type = Content.Type.OTHER_XML;
      } else if ("application/atom+xml;type=entry".equals(typeAttr)) {
        type = Content.Type.OTHER_XML;
        ext = new Entry();
      } else if (typeAttr.endsWith("+xml") || typeAttr.endsWith("/xml")) {
        type = Content.Type.OTHER_XML;
        xml = new XmlBlob();
        initializeXmlBlob(xml, true, true);
      } else if (typeAttr.startsWith("text/")) {
        type = Content.Type.OTHER_TEXT;
      } else {
        type = Content.Type.OTHER_BINARY;
      }
    }

    /**
     * Processes attributes.
     *
     * @throws   ParseException
     */
    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("type")) {
          try {
            mimeType = new ContentType(value);
          } catch (IllegalArgumentException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidMimeType, e);
          }
        }
      }
    }

    @Override
    public ElementHandler getChildHandler(String namespace, String localName,
        Attributes attrs) throws ParseException, IOException {
      
      if (ext != null) {
        return ext.getHandler(extProfile, namespace, localName, attrs);
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
    
    /**
     * Processes this element; overrides inherited method.
     */
    @Override
    public void processEndElement() throws ParseException {

      switch (type) {

        case Content.Type.OTHER_XML:
          // XML contents are processed through initializeXmlBlob()
          // or getChildHandler above.
          break;
  
        case Content.Type.OTHER_TEXT:
          text = value;
          lang = xmlLang;
          break;
  
        case Content.Type.OTHER_BINARY:
          if (value != null) {
            try {
              bytes = Base64.decode(value);
            } catch (Base64DecoderException e) {
              throw new ParseException(
                  CoreErrorDomain.ERR.invalidBase64);
            }
          }
  
          lang = xmlLang;
          break;
  
        default:
          throw new AssertionError("Invalid type for other content.");
      }
    }
  }
}
