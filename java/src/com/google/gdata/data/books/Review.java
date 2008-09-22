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


package com.google.gdata.data.books;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * User-provided review.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = BooksNamespace.GBS_ALIAS,
    nsUri = BooksNamespace.GBS,
    localName = Review.XML_NAME)
public class Review extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "review";

  /** XML "xml:lang" attribute name */
  private static final String LANG = "xml:lang";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** Language of link title */
  private String lang = null;

  /** Type of text construct (typically 'text', 'html' or 'xhtml') */
  private String type = null;

  /** Text content of the review */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public Review() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param lang language of link title.
   * @param type type of text construct (typically 'text', 'html' or 'xhtml').
   * @param value text content of the review.
   */
  public Review(String lang, String type, String value) {
    super();
    setLang(lang);
    setType(type);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the language of link title.
   *
   * @return language of link title
   */
  public String getLang() {
    return lang;
  }

  /**
   * Sets the language of link title.
   *
   * @param lang language of link title or <code>null</code> to reset
   */
  public void setLang(String lang) {
    throwExceptionIfImmutable();
    this.lang = lang;
  }

  /**
   * Returns whether it has the language of link title.
   *
   * @return whether it has the language of link title
   */
  public boolean hasLang() {
    return getLang() != null;
  }

  /**
   * Returns the type of text construct (typically 'text', 'html' or 'xhtml').
   *
   * @return type of text construct (typically 'text', 'html' or 'xhtml')
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of text construct (typically 'text', 'html' or 'xhtml').
   *
   * @param type type of text construct (typically 'text', 'html' or 'xhtml') or
   *     <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the type of text construct (typically 'text', 'html'
   * or 'xhtml').
   *
   * @return whether it has the type of text construct (typically 'text', 'html'
   *     or 'xhtml')
   */
  public boolean hasType() {
    return getType() != null;
  }

  /**
   * Returns the text content of the review.
   *
   * @return text content of the review
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the text content of the review.
   *
   * @param value text content of the review or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the text content of the review.
   *
   * @return whether it has the text content of the review
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (value == null) {
      throw new IllegalStateException("Missing text content");
    }
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
        ExtensionDescription.getDefaultDescription(Review.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LANG, lang);
    generator.put(TYPE, type);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    lang = helper.consume("lang", false);
    type = helper.consume(TYPE, false);
    value = helper.consume(null, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Review other = (Review) obj;
    return eq(lang, other.lang)
        && eq(type, other.type)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (lang != null) {
      result = 37 * result + lang.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Review lang=" + lang + " type=" + type + " value=" + value + "}";
  }

}
