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


package com.google.gdata.model.atom;

import com.google.gdata.data.IContent;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

import java.net.URI;

/**
 * Base class for entry content.
 */
public class Content extends Element implements IContent {

  /**
   * The key for Content used as a construct.  This will apply to all uses
   * of Content regardless of QName.
   */
  public static final ElementKey<String, Content> CONSTRUCT =
      ElementKey.of(null, String.class, Content.class);

  /**
   * The key for a Content element used as atom:content.
   */
  public static final ElementKey<String, Content> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "content"), String.class, Content.class);

  /**
   * Key for the XML lang attribute
   */
  public static final AttributeKey<String> XML_LANG = AttributeKey.of(
      new QName(Namespaces.xmlNs, "lang"));

  /**
   * The content type attribute.
   */
  public static final AttributeKey<String> TYPE = AttributeKey.of(
      new QName("type"));

  /**
   * The src attribute.
   */
  public static final AttributeKey<URI> SRC = AttributeKey.of(
      new QName("src"), URI.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    // We put the default metadata into the construct (Content with no QName),
    // because we need these attributes as part of TextContent when it is used
    // as something other than atom:content.
    ElementCreator constructBuilder =
        DefaultRegistry.build(CONSTRUCT).setContentRequired(false);
    constructBuilder.addAttribute(TYPE);
    constructBuilder.addAttribute(SRC);
    constructBuilder.addAttribute(XML_LANG);

    // The basic atom:content just uses the same fields as the construct.
    DefaultRegistry.build(KEY);
  }

  /** Defines the possible content types. */
  public static class Type {

    public static final int TEXT = 1;
    public static final int HTML = 2;
    public static final int XHTML = 3;
    public static final int OTHER_TEXT = 4;     // inlined text
    public static final int OTHER_XML = 5;      // inlined xml
    public static final int OTHER_BINARY = 6;   // inlined base64 binary
    public static final int MEDIA = 7;          // external media
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param elementMetadata metadata describing the expected attributes and
   *        child elements.
   */
  public Content(ElementMetadata<?, ?> elementMetadata) {
    super(elementMetadata);
  }

  /**
   * Constructs a new instance using the specified element metadata and
   * copying all attributes, elements, and text content from the source
   * Content instance.
   *
   * @param elementMetadata metadata describing the expected attributes and
   *        child elements.
   * @param source source content.
   */
  public Content(ElementMetadata<?, ?> elementMetadata, Content source) {
    super(elementMetadata, source);
  }

  /** Returns this content's type. */
  public int getType() {
    return Type.TEXT;
  }

  /** Returns the human language that this content is written in */
  public String getLang() {
    return getAttributeValue(XML_LANG);
  }

  /** Sets the human language that this content was is written in. */
  public void setLang(String lang) {
    addAttribute(Content.XML_LANG, lang);
  }

  /** @return the MIME content type, or {@code null} if none exists */
  public ContentType getMimeType() {
    String type = getAttributeValue(TYPE);
    if (type == null) {
      return null;
    }
    return new ContentType(type);
  }

  /** @return the external URI, or {@code null} if none exists */
  public URI getSrc() {
    return getAttributeValue(SRC);
  }

  @Override
  protected Element narrow(ValidationContext vc) {

    // Don't create a new instance if type is already narrow.
    if (!Content.class.equals(this.getClass())) {
      return this;
    }

    URI src = getAttributeValue(SRC);
    if (src == null) {
      String type = getAttributeValue(TYPE);
      if (type == null || type.equals("text") || type.equals("html") ||
          type.equals("xhtml")) {

        // In-line text content
        return adapt(this, TextContent.KIND);
      } else {
        // Other in-line content
        return adapt(this, OtherContent.KIND);
      }
    } else {
      // Out-of-line content
      return adapt(this, OutOfLineContent.KIND);
    }
  }
}
