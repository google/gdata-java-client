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


package com.google.gdata.model.atompub;

import com.google.common.collect.Maps;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.ContentModel;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.atom.Category;
import com.google.gdata.util.Namespaces;

import java.util.List;
import java.util.Map;

/**
 * Value of the app:categories tag.
 *
 * 
 */
public class Categories extends Element {

  /** Indicates whether the list of categories is a fixed or an open set. */
  public enum Fixed {

    /** Is not fixed. */
    NO,

    /** Is fixed. */
    YES;

    /**
     * Returns the string representation of this instance, suitable for use in
     * output. This is a lowercase version of the name.
     */
    @Override
    public String toString() {
      return name().toLowerCase();
    }

    private static final Map<String, Fixed> VALUE_MAP = Maps.newHashMap();
    static {
      for (Fixed value : Fixed.values()) {
        VALUE_MAP.put(value.toString(), value);
      }
    }

    /**
     * Convert from a string representation of Fixed to an instance. Unlike
     * {@code valueOf(String)} this method will return null if an enum for the
     * given value does not exist.
     *
     * @param value the string representation to look up.
     * @return an instance if one matched the given string, or {@code null} if
     *     none was found.
     */
    public static Fixed fromString(String value) {
      return VALUE_MAP.get(value);
    }
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Categories> KEY = ElementKey.of(new QName(Namespaces.atomPubStandardNs,
      "categories"), Void.class, Categories.class);

  /**
   * Indicates whether the list of categories is a fixed or an open set.
   */
  public static final AttributeKey<Fixed> FIXED = AttributeKey.of(new
      QName(null, "fixed"), Fixed.class);

  /**
   * An IRI reference to a Category Document.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(new
      QName(null, "href"), String.class);

  /**
   * Default scheme of the contained category elements.
   */
  public static final AttributeKey<String> SCHEME = AttributeKey.of(new
      QName(null, "scheme"), String.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.addAttribute(FIXED);
    builder.addAttribute(HREF);
    builder.addAttribute(SCHEME);
    builder.addElement(Category.KEY).setCardinality(
        ContentModel.Cardinality.MULTIPLE);
  }

  /**
   * Default mutable constructor.
   */
  public Categories() {
    this(DefaultRegistry.get(KEY));
  }

  /**
   * Lets subclasses create an instance using custom metadata.
   */
  protected Categories(ElementMetadata<Void, ? extends Categories> metadata) {
    super(metadata);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param metadata metadata to use for this element.
   * @param source source element
   */
  public Categories(ElementMetadata<Void, ? extends Categories> metadata,
      Element source) {
    super(metadata, source);
  }

  /**
   * Immutable constructor.
   *
   * @param fixed indicates whether the list of categories is a fixed or an open
   *     set.
   * @param href an IRI reference to a Category Document.
   * @param scheme default scheme of the contained category elements.
   */
  public Categories(Fixed fixed, String href, String scheme) {
    this();
    setFixed(fixed);
    setHref(href);
    setScheme(scheme);
    setImmutable(true);
  }

  /**
   * Returns the categories.
   *
   * @return categories
   */
  public List<Category> getCategories() {
    return super.getElements(Category.KEY);
  }

  /**
   * Adds a new category.
   *
   * @param category category
   */
  public void addCategory(Category category) {
    super.addElement(Category.KEY, category);
  }

  /**
   * Removes an existing category.
   *
   * @param category category
   * @return true if the category was removed
   */
  public boolean removeCategory(Category category) {
    return super.removeElement(Category.KEY, category);
  }

  /**
   * Returns whether it has the categories.
   *
   * @return whether it has the categories
   */
  public boolean hasCategories() {
    return super.hasElement(Category.KEY);
  }

  /**
   * Returns the indicates whether the list of categories is a fixed or an open
   * set.
   *
   * @return indicates whether the list of categories is a fixed or an open set
   */
  public Fixed getFixed() {
    return super.getAttributeValue(FIXED);
  }

  /**
   * Sets the indicates whether the list of categories is a fixed or an open
   * set.
   *
   * @param fixed indicates whether the list of categories is a fixed or an open
   *     set or <code>null</code> to reset
   */
  public void setFixed(Fixed fixed) {
    throwExceptionIfImmutable();
    if (fixed == null) {
      super.removeAttribute(FIXED);
    } else {
      super.addAttribute(FIXED, fixed);
    }
  }

  /**
   * Returns whether it has the indicates whether the list of categories is a
   * fixed or an open set.
   *
   * @return whether it has the indicates whether the list of categories is a
   *     fixed or an open set
   */
  public boolean hasFixed() {
    return getFixed() != null;
  }

  /**
   * Returns the an IRI reference to a Category Document.
   *
   * @return an IRI reference to a Category Document
   */
  public String getHref() {
    return super.getAttributeValue(HREF);
  }

  /**
   * Sets the an IRI reference to a Category Document.
   *
   * @param href an IRI reference to a Category Document or <code>null</code> to
   *     reset
   */
  public void setHref(String href) {
    throwExceptionIfImmutable();
    if (href == null) {
      super.removeAttribute(HREF);
    } else {
      super.addAttribute(HREF, href);
    }
  }

  /**
   * Returns whether it has the an IRI reference to a Category Document.
   *
   * @return whether it has the an IRI reference to a Category Document
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the default scheme of the contained category elements.
   *
   * @return default scheme of the contained category elements
   */
  public String getScheme() {
    return super.getAttributeValue(SCHEME);
  }

  /**
   * Sets the default scheme of the contained category elements.
   *
   * @param scheme default scheme of the contained category elements or
   *     <code>null</code> to reset
   */
  public void setScheme(String scheme) {
    throwExceptionIfImmutable();
    if (scheme == null) {
      super.removeAttribute(SCHEME);
    } else {
      super.addAttribute(SCHEME, scheme);
    }
  }

  /**
   * Returns whether it has the default scheme of the contained category
   * elements.
   *
   * @return whether it has the default scheme of the contained category
   *     elements
   */
  public boolean hasScheme() {
    return getScheme() != null;
  }

  @Override
  public String toString() {
    return "{Categories fixed=" + getAttributeValue(FIXED) + " href=" +
        getAttributeValue(HREF) + " scheme=" + getAttributeValue(SCHEME) + "}";
  }

}
