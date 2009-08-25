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


package com.google.gdata.model.gd;

import com.google.gdata.data.ILink;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.atom.Entry;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

/**
 * Describes a nested entry link.
 *
 * 
 */
public class EntryLink extends Element implements ILink {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, EntryLink> KEY = ElementKey.of(
      new QName(Namespaces.gNs, "entryLink"), EntryLink.class);

  /**
   * The entry URI.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(
      new QName("href"));

  /**
   * Whether the contained entry is read-only.
   */
  public static final AttributeKey<Boolean> READ_ONLY = AttributeKey.of(
      new QName("readOnly"), Boolean.class);

  /**
   * Qualified name of entry relation type attribute.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(
      new QName("rel"));

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY);
    builder.addAttribute(REL);
    builder.addAttribute(HREF);
    builder.addAttribute(READ_ONLY);
    builder.addElement(Entry.KEY);
  }

  /**
   * Default mutable constructor.
   */
  public EntryLink() {
    super(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  protected EntryLink(ElementKey<?, ? extends EntryLink> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected EntryLink(ElementKey<?, ? extends EntryLink> key, Element source) {
    super(key, source);
  }

  /**
   * Returns the nested entry.
   *
   * @return nested entry
   */
  public Entry getEntry() {
    return getElement(Entry.KEY);
  }

  /**
   * Sets the nested entry.
   *
   * @param entry nested entry or <code>null</code> to reset
   */
  public void setEntry(Entry entry) {
    setElement(Entry.KEY, entry);
  }

  /**
   * Returns whether it has the nested entry.
   *
   * @return whether it has the nested entry
   */
  public boolean hasEntry() {
    return hasElement(Entry.KEY);
  }

  /**
   * Returns the entry URI.
   *
   * @return entry URI
   */
  public String getHref() {
    return getAttributeValue(HREF);
  }

  /**
   * Sets the entry URI.
   *
   * @param href entry URI or <code>null</code> to reset
   */
  public void setHref(String href) {
    setAttributeValue(HREF, href);
  }

  /**
   * Returns whether it has the entry URI.
   *
   * @return whether it has the entry URI
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the whether the contained entry is read-only.
   *
   * @return whether the contained entry is read-only
   */
  public Boolean getReadOnly() {
    return getAttributeValue(READ_ONLY);
  }

  /**
   * Sets the whether the contained entry is read-only.
   *
   * @param readOnly whether the contained entry is read-only or
   *     <code>null</code> to reset
   */
  public void setReadOnly(Boolean readOnly) {
    setAttributeValue(READ_ONLY, readOnly);
  }

  /**
   * Returns whether it has the whether the contained entry is read-only.
   *
   * @return whether it has the whether the contained entry is read-only
   */
  public boolean hasReadOnly() {
    return getReadOnly() != null;
  }

  /**
   * Returns the entry relation type.
   *
   * @return entry relation type
   */
  public String getRel() {
    return getAttributeValue(REL);
  }

  /**
   * Sets the entry relation type.
   *
   * @param rel entry relation type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    setAttributeValue(REL, rel);
  }

  /**
   * Returns whether it has the entry relation type.
   *
   * @return whether it has the entry relation type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  public String toString() {
    return "{EntryLink href=" + getAttributeValue(HREF) + " readOnly="
        + getAttributeValue(READ_ONLY) + " rel=" + getAttributeValue(REL) + "}";
  }

  public String getType() {
    return ContentType.getAtomFeed().toString();
  }

  public void setType(String type) {
    throw new UnsupportedOperationException("Type property not modifiable in "
        + FeedLink.class.getSimpleName());
  }
}
