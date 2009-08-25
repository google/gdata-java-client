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

import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.introspection.IWorkspace;
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

import java.util.List;

/**
 * Server-defined groups of Collections.  This is hand-written because it needs
 * versioning support that allows us to hide the title attribute by default.
 *
 * 
 */
public class Workspace extends Element implements IWorkspace {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Workspace> KEY = ElementKey.of(
      new QName(Namespaces.atomPubStandardNs, "workspace"), Workspace.class);

  /**
   * The title attribute.
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
    builder.addElement(Collection.KEY).setCardinality(Cardinality.MULTIPLE);
    builder.addElement(Source.TITLE).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public Workspace() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using custom key.
   */
  protected Workspace(ElementKey<?, ? extends Workspace> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key the element key to use for this element.
   * @param source source element
   */
  protected Workspace(ElementKey<?, ? extends Workspace> key, Element source) {
    super(key, source);
  }

  /**
   * Constructor with the title element.
   */
  public Workspace(TextContent title) {
    this();
    setTitle(title);
  }

  /**
   * Returns the collections.
   *
   * @return collections
   */
  public List<Collection> getCollections() {
    return super.getElements(Collection.KEY);
  }

  /**
   * Adds a new collection.
   *
   * @param collection collection
   */
  public void addCollection(Collection collection) {
    super.addElement(Collection.KEY, collection);
  }

  /**
   * Add a new collection with the given title and accept types.
   */
  public Collection addCollection(String collectionUri, String title,
      String... acceptedTypes) {
    Collection collection = new Collection(collectionUri,
        TextContent.plainText(title), acceptedTypes);
    addCollection(collection);
    return collection;
  }

  /**
   * Removes an existing collection.
   *
   * @param collection collection
   * @return true if the collection was removed
   */
  public boolean removeCollection(Collection collection) {
    return super.removeElement(Collection.KEY, collection);
  }

  /**
   * Returns whether it has the collections.
   *
   * @return whether it has the collections
   */
  public boolean hasCollections() {
    return super.hasElement(Collection.KEY);
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
  public void setTitle(TextContent title) {
    setAttributeValue(TITLE, (title == null ? null : title.getPlainText()));
    super.setElement(Source.TITLE, title);
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
  public Element resolve(ElementMetadata<?, ?> meta, ValidationContext vc) {
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

    return super.resolve(meta, vc);
  }

  @Override
  public String toString() {
    return "{Workspace" + super.toString() + "}";
  }
}
