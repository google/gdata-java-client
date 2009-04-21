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

import com.google.gdata.data.IOutOfLineContent;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.util.ContentType;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Variant of {@link Content} for entries that reference external content.
 */
public class OutOfLineContent extends Content implements IOutOfLineContent {

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

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.replaceAttribute(Content.SRC).setRequired(true);
    builder.addAttribute(LENGTH).setVisible(false);
    DefaultRegistry.adapt(Content.KEY, KIND, KEY);
  }

  /**
   * Constructs a new instance using the default metadata.
   */
  public OutOfLineContent() {
    super(DefaultRegistry.get(KEY));
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param elementMetadata metadata describing the expected attributes and
   *        child elements.
   */
  public OutOfLineContent(ElementMetadata<?, ?> elementMetadata) {
    super(elementMetadata);
  }

  /**
   * Constructs a new instance from a more generic {@link Content} type.
   *
   * @param content generic content
   */
  public OutOfLineContent(Content content) {
    this();
    setMimeType(content.getMimeType());
    setSrc(content.getSrc());
  }

  /** @return the type of this content */
  @Override
  public int getType() {
    return Content.Type.MEDIA;
  }

  /** Specifies the MIME Content type. */
  public void setMimeType(ContentType v) {
    addAttribute(TYPE, v == null ? null : v.getMediaType());
  }

  /** @return always null, since language is undefined for external content. */
  @Override
  public String getLang() {
    return null;
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
    addAttribute(SRC, v);
  }

  /** Specifies the file length (RSS only). */
  public void setLength(long length) {
    if (length == -1) {
      removeAttribute(LENGTH);
    } else {
      addAttribute(LENGTH, length);
    }
  }

  public long getLength() {
    Long length = getAttributeValue(LENGTH);
    return length == null ? -1 : length;
  }
}
