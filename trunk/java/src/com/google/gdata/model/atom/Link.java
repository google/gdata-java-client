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

import com.google.gdata.data.ILink;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.util.Namespaces;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class representing atom:link.
 */
public class Link extends Element implements ILink {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Link> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "link"), Link.class);

  /**
   * The href attribute.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(
      new QName("href"));

  /**
   * The rel attribute.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(
      new QName("rel"));

  /**
   * The type attribute.
   */
  public static final AttributeKey<String> TYPE = AttributeKey.of(
      new QName("type"));

  /**
   * The hreflang attribute.
   */
  public static final AttributeKey<String> HREFLANG = AttributeKey.of(
      new QName("hreflang"));

  /**
   * The title attribute.
   */
  public static final AttributeKey<String> TITLE = AttributeKey.of(
      new QName("title"));

  /**
   * The xml:lang attribute.
   */
  public static final AttributeKey<String> XML_LANG = AttributeKey.of(
      new QName(Namespaces.xmlNs, "lang"));

  /**
   * The length attribute.
   */
  public static final AttributeKey<Long> LENGTH = AttributeKey.of(
      new QName("length"), Long.class);

  /**
   * The etag attribute.
   */
  public static final AttributeKey<String> ETAG = AttributeKey.of(
      new QName(Namespaces.gNs, "etag"));

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY)
        .setCardinality(Cardinality.MULTIPLE);
    builder.addAttribute(REL);
    builder.addAttribute(TYPE);
    builder.addAttribute(HREF).setRequired(true);
    builder.addAttribute(HREFLANG);
    builder.addAttribute(TITLE);
    builder.addAttribute(XML_LANG);
    builder.addAttribute(LENGTH);
    builder.addAttribute(ETAG);
    builder.addElement(Content.KEY);
  }

  /**
   * Constructs a new instance using the default metadata.
   */
  public Link() {
    super(KEY);
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param key the element key for this link.
   */
  protected Link(ElementKey<?, ? extends Link> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param key the element key to use for this element.
   * @param source source element
   */
  protected Link(ElementKey<?, ? extends Link> key, Element source) {
    super(key, source);
  }

  /**
   * Constructs a new instance using the default metadata, and setting
   * the links rel, type, and href attributes.
   *
   * @deprecated Use {@link #Link(String, String, URI)} instead.
   */
  @Deprecated
  public Link(String rel, String type, String href) {
    this();
    setRel(rel);
    setType(type);
    setHref(href);
  }

  /**
   * Constructs a new instance using the default metadata, and setting
   * the links rel, type, and href attributes.
   */
  public Link(String rel, String type, URI href) {
    this();
    setRel(rel);
    setType(type);
    setHref(href);
  }

  /**
   * Link relation type.  Possible values include {@code self}, {@code
   * prev}, {@code next}, {@code enclosure}, etc.
   */
  public String getRel() {
    String rel = getAttributeValue(REL);
    return rel != null ? rel : Rel.ALTERNATE;
  }
  public void setRel(String v) {
    setAttributeValue(REL, v);
  }

  /** MIME type of the link target. */
  public String getType() {
    return getAttributeValue(TYPE);
  }
  public void setType(String v) {
    setAttributeValue(TYPE, v);
  }

  /** Link URI. */
  public String getHref() {
    return getAttributeValue(HREF);
  }
  public URI getHrefUri() {
    String href = getHref();
    try {
      return href == null ? null : new URI(href);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
  public void setHref(String v) {
    setAttributeValue(HREF, v);
  }
  public void setHref(URI v) {
    String href = v == null ? null : v.toString();
    setHref(href);
  }

  /** Language of resource pointed to by href. */
  public String getHrefLang() {
    return getAttributeValue(HREFLANG);
  }
  public void setHrefLang(String v) {
    setAttributeValue(HREFLANG, v);
  }

  /** Link title. */
  public String getTitle() {
    return getAttributeValue(TITLE);
  }
  public void setTitle(String v) {
    setAttributeValue(TITLE, v);
  }

  /** Language of link title. */
  public String getTitleLang() {
    return getAttributeValue(XML_LANG);
  }
  public void setTitleLang(String v) {
    setAttributeValue(XML_LANG, v);
  }

  /** Length of the resource pointed to by href, in bytes. */
  protected long length = -1;
  public long getLength() {
    Long value = getAttributeValue(LENGTH);
    if (value == null) {
      return -1;
    }
    return value;
  }
  public void setLength(long v) {
    setAttributeValue(LENGTH, v);
  }

  /** Etag of linked resource or {@code null} if unknown. */
  public String getEtag() {
    return getAttributeValue(ETAG);
  }
  public void setEtag(String v) {
    setAttributeValue(ETAG, v);
  }

  /**
   * Return the content of the link, or {@code null} if no content has been set.
   * This is used to inline an atom:content element inside an atom:link element.
   *
   * @return the atom:content element, or null if none exists.
   */
  public Content getContent() {
    return getElement(Content.KEY);
  }

  /**
   * Sets the atom:content element nested inside this atom:link.
   *
   * @param c the content to place inside the link.
   */
  public void setContent(Content c) {
    setElement(Content.KEY, c);
  }

  /**
   * Returns whether this link matches the given {@code rel} and {@code type}
   * values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   */
  public boolean matches(String relToMatch, String typeToMatch) {
    return (relToMatch == null || relToMatch.equals(getRel()))
        && (typeToMatch == null || typeToMatch.equals(getType()));
  }

  @Override
  protected Element narrow(ElementMetadata<?, ?> meta, ValidationContext vc) {
    return adapt(this, meta, getRel());
  }
}
