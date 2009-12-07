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


package com.google.gdata.data.gtt;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Describes a document source.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = GttNamespace.GTT_ALIAS,
    nsUri = GttNamespace.GTT,
    localName = DocumentSource.XML_NAME)
public class DocumentSource extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "documentSource";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "url" attribute name */
  private static final String URL = "url";

  private static final AttributeHelper.EnumToAttributeValue<Type>
      TYPE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.EnumToAttributeValue<Type>() {
    public String getAttributeValue(Type enumValue) {
      return enumValue.toValue();
    }
  };

  /** Type */
  private Type type = null;

  /** Url */
  private String url = null;

  /** Type. */
  public enum Type {

    /** Aea document source. */
    AEA("aea"),

    /** Aes document source. */
    AES("aes"),

    /** Doc document source. */
    DOC("doc"),

    /** Html document source. */
    HTML("html"),

    /** Knol document source. */
    KNOL("knol"),

    /** Odt document source. */
    ODT("odt"),

    /** PlainText document source. */
    PLAINTEXT("plainText"),

    /** Rtf document source. */
    RTF("rtf"),

    /** Srt document source. */
    SRT("srt"),

    /** Sub document source. */
    SUB("sub"),

    /** Wiki document source. */
    WIKI("wiki");

    private final String value;

    private Type(String value) {
      this.value = value;
    }

    /**
     * Returns the value used in the XML.
     *
     * @return value used in the XML
     */
    public String toValue() {
      return value;
    }

  }

  /**
   * Default mutable constructor.
   */
  public DocumentSource() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param type type.
   * @param url url.
   */
  public DocumentSource(Type type, String url) {
    super();
    setType(type);
    setUrl(url);
    setImmutable(true);
  }

  /**
   * Returns the type.
   *
   * @return type
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type type or <code>null</code> to reset
   */
  public void setType(Type type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the type.
   *
   * @return whether it has the type
   */
  public boolean hasType() {
    return getType() != null;
  }

  /**
   * Returns the url.
   *
   * @return url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url.
   *
   * @param url url or <code>null</code> to reset
   */
  public void setUrl(String url) {
    throwExceptionIfImmutable();
    this.url = url;
  }

  /**
   * Returns whether it has the url.
   *
   * @return whether it has the url
   */
  public boolean hasUrl() {
    return getUrl() != null;
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(DocumentSource.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(TYPE, type, TYPE_ENUM_TO_ATTRIBUTE_VALUE);
    generator.put(URL, url);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    type = helper.consumeEnum(TYPE, false, Type.class, null,
        TYPE_ENUM_TO_ATTRIBUTE_VALUE);
    url = helper.consume(URL, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    DocumentSource other = (DocumentSource) obj;
    return eq(type, other.type)
        && eq(url, other.url);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (url != null) {
      result = 37 * result + url.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{DocumentSource type=" + type + " url=" + url + "}";
  }

}

