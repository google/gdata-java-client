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

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.XmlBlob;

import java.util.Iterator;

/**
 * Variant of {@link Content} for entries containing miscellaneous
 * inlined content types.
 */
public class OtherContent extends Content {

  /** The kind name for adaptation. */
  public static final String KIND = "other";

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<String, OtherContent> KEY = ElementKey.of(
      Content.KEY.getId(), String.class, OtherContent.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.addElement(Feed.KEY);
    builder.addElement(Entry.KEY);
    DefaultRegistry.adapt(Content.KEY, KIND, KEY);
  }

  /**
   * Constructs a new instance using the default metadata.
   */
  public OtherContent() {
    super(DefaultRegistry.get(KEY));
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param elementMetadata metadata describing the expected attributes and
   *        child elements.
   */
  public OtherContent(ElementMetadata<?, ?> elementMetadata) {
    super(elementMetadata);
  }

  /**
   * Constructs a new instance from a more generic {@link Content} type.
   *
   * @param content generic content
   */
  public OtherContent(Content content) {
    super(DefaultRegistry.get(KEY), content);
  }

  /** @return the type of this content */
  @Override
  public int getType() {
    if (getXml() != null || getXmlContent() != null) {
      return Content.Type.OTHER_XML;
    }
    if (getMimeType().getMediaType().equals("text")) {
      return Content.Type.OTHER_TEXT;
    }
    return Content.Type.OTHER_BINARY;
  }

  /** Specifies the MIME type. */
  public void setMimeType(ContentType v) {
    addAttribute(TYPE, v.getMediaType());
  }

  /** @return the XML contents */
  public XmlBlob getXml() {
    return null;
  }

  /** Specifies the XML contents. */
  public void setXml(XmlBlob v) {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * Gets the nested xml content of this element.  This will return the first
   * xml element found inside this content.
   */
  public Element getXmlContent() {
    Iterator<Element> iter = getElementIterator();
    if (iter.hasNext()) {
      return iter.next();
    }
    return null;
  }

  /**
   * Sets the nested xml content of this element.
   */
  public void setXmlContent(Element content) {
    addElement(content);
  }

  /** @return the plain text contents */
  public String getText() {
    return getTextValue(KEY);
  }

  /** Specifies the plain-text contents. */
  public void setText(String v) {
    setTextValue(v);
  }

  /** @return the binary contents */
  public byte[] getBytes() {
    String value = getText();
    try {
      return value == null ? null : Base64.decode(value);
    } catch (Base64DecoderException e) {
      throw new IllegalStateException("Value was not base64 encoded: " + value);
    }
  }

  /** Specifies the binary contents. */
  public void setBytes(byte[] v) {
    setText(Base64.encode(v));
  }

  @Override
  public void validate(ValidationContext vc) {
    super.validate(vc);

    int maximumChildren = hasTextValue() ? 0 : 1;

    if (getElementCount() > maximumChildren) {
      vc.addError(this, "Content cannot contain more than "
          + maximumChildren + " child elements, but contains "
          + getElementCount());
    }
  }
}
