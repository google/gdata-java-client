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

import com.google.common.collect.Lists;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.Service;
import com.google.gdata.client.Service.Versions;
import com.google.gdata.data.Reference;
import com.google.gdata.data.introspection.ICollection;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.atom.Source;
import com.google.gdata.model.atom.TextContent;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.Version;

import java.util.List;

/**
 * The Collection class defines the basic Java object model
 * representation and XML parsing/generation support for an
 * APP collection.
 *
 * <p>The implementation is versioned to support the AtomPub draft version 9
 * introspection format (used for the GData v1 implementation) as well
 * as the final RFC5023 format (used for all other versions).  The key
 * difference between the two is that draft used an attribute for the
 * collection title and a comma-delimited list for accepted MIME types,
 * where the final version uses atom:title and repeating app:accept
 * elements.
 *
 * 
 */
public class Collection extends Element implements Reference, ICollection {

  private final Version coreVersion = Service.getVersion();

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Collection> KEY = ElementKey.of(
      new QName(Namespaces.atomPubStandardNs, "collection"), Collection.class);

  /**
   * The href attribute.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(
      new QName("href"));

  /**
   * Qualified name of title attribute.
   */
  public static final AttributeKey<String> TITLE = AttributeKey.of(
      new QName("title"));

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY);
    builder.addAttribute(TITLE).setVisible(false);
    builder.addAttribute(HREF);
    builder.addElement(Accept.KEY).setCardinality(Cardinality.MULTIPLE);
    builder.addElement(Categories.KEY).setCardinality(Cardinality.MULTIPLE);
    builder.addElement(Source.TITLE).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public Collection() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using a custom key.
   */
  protected Collection(ElementKey<?, ? extends Collection> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param key the element key to use for this element
   * @param source source element
   */
  protected Collection(ElementKey<?, ? extends Collection> key,
      Element source) {
    super(key, source);
  }

  /**
   * Construct a collection with the given href.
   *
   * @param href href.
   */
  public Collection(String href) {
    this();
    setHref(href);
  }

  /**
   * Construct a collection with all fields.
   */
  public Collection(String href, TextContent title, String... accepts) {
    this();
    setHref(href);
    setTitle(title);
    for (String accept : accepts) {
      addAccept(accept);
    }
  }

  /**
   * Returns the accept elements.
   *
   * @return accept elements
   */
  public List<Accept> getAccepts() {
    List<Accept> accepts = super.getElements(Accept.KEY);

    if (coreVersion.isCompatible(Versions.V1)) {
      // Check for any accepts with a comma, in which case we split those into
      // multiple accept elements before returning.
      List<Accept> result = Lists.newArrayList();
      for (Accept accept : accepts) {
        String acceptValue = accept.getValue();
        if (acceptValue != null && acceptValue.indexOf(',') != -1) {
          String[] split = acceptValue.split(",");
          for (String part : split) {
            result.add(new Accept(part));
          }
        } else {
          result.add(accept);
        }
      }
      accepts = result;
    }

    return accepts;
  }

  /**
   * Returns a list of accept strings.
   */
  public List<String> getAcceptList() {
    List<Accept> accepts = getAccepts();
    List<String> result = Lists.newArrayListWithCapacity(accepts.size());
    for (Accept accept : accepts) {
      result.add(accept.getValue());
    }
    return result;
  }

  /**
   * Adds a new accept element.
   *
   * @param accept accept element
   */
  public Collection addAccept(Accept accept) {
    super.addElement(Accept.KEY, accept);
    return this;
  }

  /**
   * Adds a new accept string.
   *
   * @param accept accept string
   */
  public Collection addAccept(String accept) {
    super.addElement(Accept.KEY, new Accept(accept));
    return this;
  }

  /**
   * Removes an accept element.
   *
   * @param accept accept element
   * @return true if the accept was removed
   */
  public boolean removeAccept(Accept accept) {
    return super.removeElement(Accept.KEY, accept);
  }

  /**
   * Removes an accept string.
   *
   * @param acceptStr the string to remove
   * @return true if the acceptStr was removed.
   */
  public boolean removeAccept(String acceptStr) {
    boolean modified = false;
    for (Accept accept : getAccepts()) {
      if (acceptStr.equals(accept.getValue())) {
        super.removeElement(Accept.KEY, accept);
        modified = true;
      }
    }
    return modified;
  }

  /**
   * Returns whether it has the accept elements.
   *
   * @return whether it has the accept elements
   */
  public boolean hasAccepts() {
    return super.hasElement(Accept.KEY);
  }

  /**
   * Returns the app categories documents.
   *
   * @return app categories documents
   */
  public List<Categories> getCategorieses() {
    return super.getElements(Categories.KEY);
  }

  /**
   * Adds a new app categories document.
   *
   * @param categories app categories document
   */
  public Collection addCategories(Categories categories) {
    super.addElement(Categories.KEY, categories);
    return this;
  }

  /**
   * Returns whether it has the app categories documents.
   *
   * @return whether it has the app categories documents
   */
  public boolean hasCategorieses() {
    return super.hasElement(Categories.KEY);
  }

  /**
   * Returns the href.
   *
   * @return href
   */
  public String getHref() {
    return super.getAttributeValue(HREF);
  }

  /**
   * Sets the href.
   *
   * @param href href or <code>null</code> to reset
   */
  public void setHref(String href) {
    setAttributeValue(HREF, href);
  }

  /**
   * Returns whether it has the href.
   *
   * @return whether it has the href
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the title.
   *
   * @return title
   */
  public TextContent getTitle() {
    return super.getElement(Source.TITLE);
  }

  /**
   * Sets the title.
   *
   * @param title title or <code>null</code> to reset
   */
  public Collection setTitle(TextContent title) {
    setAttributeValue(TITLE, (title == null ? null : title.getPlainText()));
    super.setElement(Source.TITLE, title);
    return this;
  }

  /**
   * Returns whether it has the title.
   *
   * @return whether it has the title
   */
  public boolean hasTitle() {
    return super.hasElement(Source.TITLE);
  }

  @Override
  public Element resolve(ElementMetadata<?, ?> metadata, ValidationContext vc) {
    String titleAttribute = getAttributeValue(TITLE);
    TextContent title = getElement(Source.TITLE);

    // Make sure that the title is in both the attribute and element.
    if (titleAttribute != null) {
      if (title == null) {
        title = TextContent.plainText(titleAttribute);
        addElement(Source.TITLE, title);
      } else {
        String titleContent = title.getPlainText();

        // Verify that the attribute and element have the same value.
        if (!titleAttribute.equals(titleContent)) {
          vc.addError(this, CoreErrorDomain.ERR.duplicateTitle);
        }
      }
    } else if (title != null) {
      titleAttribute = title.getPlainText();
      setAttributeValue(TITLE, titleAttribute);
    }

    // HACK(sven):  In v1 accept was a comma separated list, in v2 it is
    // multiple elements.  For v1 output we need to merge all elements into one
    // single element, so do that during resolve.  When parsing V1 Collection
    // objects, we should only have a single accept so it should be fine.
    if (coreVersion.isCompatible(Service.Versions.V1)) {
      List<Accept> accepts = getAccepts();
      if (accepts.size() > 1) {
        StringBuilder sb = new StringBuilder();
        for (Accept accept : accepts) {
          if (sb.length() > 0) {
            sb.append(',');
          }
          sb.append(accept.getValue());
        }
        removeElement(Accept.KEY);
        addAccept(sb.toString());
      }
    }

    return super.resolve(metadata, vc);
  }

  @Override
  public String toString() {
    return "{Collection href=" + getAttributeValue(HREF) + "}";
  }
}
