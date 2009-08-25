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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.data.ICategory;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Category class represents the data model for the Atom category element.
 */
public class Category extends Element implements ICategory {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Category> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "category"), Category.class);

  /**
   * The category scheme attribute.
   */
  public static final AttributeKey<String> SCHEME = AttributeKey.of(
      new QName("scheme"));

  /**
   * The category term attribute.
   */
  public static final AttributeKey<String> TERM = AttributeKey.of(
      new QName("term"));

  /**
   * The category label attribute.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(
      new QName("label"));

  /**
   * Qualified name for the XML lang attribute
   */
  public static final AttributeKey<String> XML_LANG = AttributeKey.of(
      new QName(Namespaces.xmlNs, "lang"));

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY)
        .setCardinality(Cardinality.SET);
    builder.addAttribute(XML_LANG);
    builder.addAttribute(SCHEME);
    builder.addAttribute(TERM).setRequired(true);
    builder.addAttribute(LABEL);
  }

  /**
   * Constructs a new category instance using the default metadata.
   */
  public Category() {
    super(KEY);
  }

  /**
   * Constructs a new category instance using the specified element key.
   *
   * @param key the element key for the category.
   */
  protected Category(ElementKey<?, ? extends Category> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as
   * the key for the element.
   *
   * @param key the element key to use for the category
   * @param source source element
   */
  protected Category(ElementKey<?, ? extends Category> key, Element source) {
    super(key, source);
  }

  // A simple pattern matcher for the "{scheme}term" syntax
  private static final Pattern categoryPattern =
    Pattern.compile("(\\{([^\\}]+)\\})?(.+)");


  /**
   * Constructs a new category from a Category string. The format of
   * the String is the same as the one used to represent a category in
   * a GData query: an optional scheme surrounded by braces, followed
   * by a term.
   *
   * @param category the category string
   */
  public Category(String category) {
    this();
    Matcher m = categoryPattern.matcher(category);
    if (!m.matches()) {
      throw new IllegalArgumentException("Invalid category: " + category);
    }

    if (m.group(2) != null) {
      setScheme(m.group(2));
    }

    setTerm(m.group(3));
  }


  /**
   * Constructs a new category with the specified scheme and term values.
   */
  public Category(String scheme, String term) { this(scheme, term, null); }

  /**
   * Constructs a new category with the specified scheme, term, and label
   * values.
   */
  public Category(String scheme, String term, String label) {
    this();
    setScheme(scheme);
    if (term == null) {
      throw new NullPointerException("Invalid term. Cannot be null");
    }
    setTerm(term);
    setLabel(label);
  }

   @Override
   public Category lock() {
     return (Category) super.lock();
   }

  /**
   * Returns the category scheme or {@code null} if the category does not have
   * a scheme.
   */
  public String getScheme() {
    return getAttributeValue(SCHEME);
  }

  /**
   * Sets the category scheme.  A value of {@code null} indicates that there is
   * no category scheme.
   * @param scheme category scheme URI.
   */
  public void setScheme(String scheme) {
    setAttributeValue(SCHEME, scheme);
  }

  /**
   * Returns the category term.
   * @return category term value.
   */
  public String getTerm() {
    return getAttributeValue(TERM);
  }

  /**
   * Sets the category term value.
   * @param term
   */
  public void setTerm(String term) {
    Preconditions.checkNotNull(term, "Null category term");
    setAttributeValue(TERM, term);
  }

  /**
   * Returns the category label or {@code null} if there is no label value.
   * @return category label (or @code null}.
   */
  public String getLabel() {
    return getAttributeValue(LABEL);
  }

  /**
   * Sets the category label.   A value of {@code null} indicates that there is
   * no label.
   * @param label category label value.
   */
  public void setLabel(String label) {
    setAttributeValue(LABEL, label);
  }

  /**
   * Returns the language associated with the category label (or {@code null}
   * if undefined).
   */
  public String getLabelLang() {
    return getAttributeValue(XML_LANG);
  }

  /**
   * Sets the language associated with the category label (or {@code nulll} if
   * undefined).
   * @param lang label language.
   */
  public void setLabelLang(String lang) {
    setAttributeValue(XML_LANG, lang);
  }

  @Override
  protected Element narrow(ElementMetadata<?, ?> meta, ValidationContext vc) {
    return adapt(this, meta, getScheme());
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    String scheme = getScheme();
    if (scheme != null) {
      sb.append("{");
      sb.append(scheme);
      sb.append("}");
    }
    // Label syntax is not in the query model, so no need to define
    // public constants for the delimiters.
    sb.append(getTerm());
    String label = getLabel();
    if (label != null) {
      sb.append("(");
      sb.append(label);
      sb.append(")");
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (! (obj instanceof Category)) {
      return false;
    }

    Category other = (Category) obj;
    String scheme = getScheme();
    if (scheme == null) {
      if (other.getScheme() != null) {
        return false;
      }
    } else if (!scheme.equals(other.getScheme())) {
      return false;
    }
    String term = getTerm();
    if (term == null) {
      if (other.getTerm() != null) {
        return false;
      }
    } else if (!term.equals(other.getTerm())) {
      return false;
    }

    String label = getLabel();
    if (label == null) {
      if (other.getLabel() != null) {
        return false;
      }
    } else if (!label.equals(other.getLabel())) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 17;
    String scheme = getScheme();
    result = 37 * result + ((scheme != null) ? scheme.hashCode() : 0);
    String term = getTerm();
    result = 37 * result + ((term != null) ? term.hashCode() : 0);
    String label = getLabel();
    result = 37 * result + ((label != null) ? label.hashCode() : 0);
    return result;
  }
}
