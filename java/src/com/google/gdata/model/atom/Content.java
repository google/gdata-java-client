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
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
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

  /**
   * Registers the default metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(CONSTRUCT)) {
      return;
    }

    // We put the default metadata into the construct (Content with no QName),
    // because we need these attributes as part of TextContent when it is used
    // as something other than atom:content.
    ElementCreator constructBuilder =
        registry.build(CONSTRUCT).setContentRequired(false);
    constructBuilder.addAttribute(TYPE);
    constructBuilder.addAttribute(SRC);
    constructBuilder.addAttribute(XML_LANG);

    // The basic atom:content just uses the same fields as the construct.
    registry.build(KEY);
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param key the element key for the content element.
   */
  public Content(ElementKey<?, ?> key) {
    super(key);
  }

  /**
   * Constructs a new instance using the specified element key and the source
   * {@link Element} instance's data (attributes, child elements, text content).
   *
   * @param key the element key for the content element.
   * @param source source content.
   */
  protected Content(ElementKey<?, ?> key, Element source) {
    super(key, source);
  }

  /**
   * Returns this content's type.   The list of valid value is defined in
   * {@link IContent.Type}.
   *
   * @see IContent.Type
   */
  public int getType() {
    return Type.TEXT;
  }

  /** Returns the human language that this content is written in */
  public String getLang() {
    return getAttributeValue(XML_LANG);
  }

  /** Sets the human language that this content was is written in. */
  public void setLang(String lang) {
    setAttributeValue(Content.XML_LANG, lang);
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
  protected Element narrow(ElementMetadata<?, ?> meta, ValidationContext vc) {

    // Don't create a new instance if type is already narrow.
    if (!Content.class.equals(this.getClass())) {
      return this;
    }

    URI src = getSrc();
    if (src == null) {
      String type = getAttributeValue(TYPE);
      if (type == null || type.equals("text") || type.equals("html") ||
          type.equals("xhtml")) {

        // In-line text content
        return adapt(this, meta, TextContent.KIND);
      } else {
        // Other in-line content
        return adapt(this, meta, OtherContent.KIND);
      }
    } else {
      // Out-of-line content
      return adapt(this, meta, OutOfLineContent.KIND);
    }
  }
}
