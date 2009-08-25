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
import com.google.gdata.data.IOutOfLineContent;
import com.google.gdata.data.Reference;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Variant of {@link Content} for entries that reference external content.
 */
public class OutOfLineContent extends Content
    implements IOutOfLineContent, Reference {

  /** The kind name for adaptation. */
  public static final String KIND = "out-of-line";

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<String, OutOfLineContent> KEY = ElementKey.of(
      Content.KEY.getId(), String.class, OutOfLineContent.class);

  /**
   * Metadata for the length attribute (hidden by default).
   */
  public static final AttributeKey<Long> LENGTH = AttributeKey.of(
      new QName("length"), Long.class);

  /**
   * The gd:etag attribute.
   *
   * See RFC 2616, Section 3.11.
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

    // Register supertype
    Content.registerMetadata(registry);

    ElementCreator builder = registry.build(KEY);
    builder.replaceAttribute(Content.SRC).setRequired(true);
    builder.addAttribute(LENGTH).setVisible(false);
    builder.addAttribute(ETAG);

    // Add adaptations
    registry.adapt(Content.KEY, KIND, KEY);
  }

  /**
   * Constructs a new instance using the default metadata.
   */
  public OutOfLineContent() {
    super(KEY);
  }

  /**
   * Constructs a new instance from a more generic {@link Content} type.
   *
   * @param content generic content
   */
  public OutOfLineContent(Content content) {
    super(KEY, content);
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param key the element key for this element.
   */
  protected OutOfLineContent(ElementKey<?, ?> key) {
    super(key);
  }

  /** @return the type of this content */
  @Override
  public int getType() {
    return IContent.Type.MEDIA;
  }

  /** Specifies the MIME Content type. */
  public void setMimeType(ContentType v) {
    setAttributeValue(TYPE, (v == null ? null : v.getMediaType()));
  }

  /**
   * Gets the external URI associated with this out-of-line content.
   *
   * @deprecated Use {@link #getSrc()} instead.
   *
   * @return external URI
   */
  @Deprecated
  public String getUri() {
    URI uri = getSrc();
    if (uri == null) {
      return null;
    }
    return uri.toString();
  }

  /**
   * Specifies the external URI.
   *
   * @deprecated Use {@link #setSrc(URI)} instead.
   *
   * @param v external URI
   */
  @Deprecated
  public void setUri(String v) {
    try {
      setSrc(v == null ? null : new URI(v));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /** Specifies the external URI. */
  public void setSrc(URI v) {
    setAttributeValue(SRC, v);
  }

  /** Specifies the file length (RSS only). */
  public void setLength(long length) {
    setAttributeValue(LENGTH, length == -1 ? null : length);
  }

  /**
   * Returns the external URI or {@code null} if none exists.
   *
   * This method exists only so that {@link Content} implements
   * {@link Reference}. Callers should use {@link #getSrc()}
   * instead whenever possible.
   */
  public String getHref() {
    URI uri = getAttributeValue(SRC);
    return uri == null ? null : uri.toString();
  }

  /**
   * Sets the external URI.
   *
   * This method exists only so that {@link Content} implements
   * {@link Reference}. Callers should use {@link #setSrc(URI)}
   * instead whenever possible.
   *
   * @param href external URI or {@code null}
   * @throws IllegalArgumentException if {@code href} is not a valid URI
   */
  public void setHref(String href) {
    try {
      setAttributeValue(SRC, (href == null) ? null : new URI(href));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Not a URI: " + href, e);
    }
  }

  public long getLength() {
    Long length = getAttributeValue(LENGTH);
    return length == null ? -1 : length;
  }

  /**
   * Returns the HTTP etag for the referenced content or {@code null} if
   * unknown.
   */
  public String getEtag() {
    return getAttributeValue(ETAG);
  }

  /**
   * Sets the HTTP etag for the referenced content. A value of {@code null}
   * indicates it is unknown.
   *
   * @param etag HTTP etag value
   */
  public void setEtag(String etag) {
    setAttributeValue(ETAG, etag);
  }
}
