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
import com.google.gdata.model.atom.Feed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

/**
 * Describes a nested feed link.
 *
 * 
 */
public class FeedLink extends Element implements ILink {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, FeedLink> KEY = ElementKey.of(
      new QName(Namespaces.gNs, "feedLink"), FeedLink.class);

  /**
   * Qualified name of hint at the number of entries in the feed attribute.
   */
  public static final AttributeKey<Integer> COUNT_HINT = AttributeKey.of(
      new QName("countHint"), Integer.class);

  /**
   * Qualified name of feed URI attribute.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(
      new QName("href"));

  /**
   * Qualified name of whether the contained feed is read-only attribute.
   */
  public static final AttributeKey<Boolean> READ_ONLY = AttributeKey.of(
      new QName("readOnly"), Boolean.class);

  /**
   * Qualified name of feed relation type attribute.
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
    builder.addAttribute(COUNT_HINT);
    builder.addElement(Feed.KEY);
  }

  /**
   * Default mutable constructor.
   */
  public FeedLink() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using a custom key.
   */
  protected FeedLink(ElementKey<?, ? extends FeedLink> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as
   * the key for the element.
   *
   * @param key element key to use for this element.
   * @param source source element
   */
  protected FeedLink(ElementKey<?, ? extends FeedLink> key, Element source) {
    super(key, source);
  }

  /**
   * Returns the hint at the number of entries in the feed.
   *
   * @return hint at the number of entries in the feed
   */
  public Integer getCountHint() {
    return getAttributeValue(COUNT_HINT);
  }

  /**
   * Sets the hint at the number of entries in the feed.
   *
   * @param countHint hint at the number of entries in the feed or
   *     <code>null</code> to reset
   */
  public void setCountHint(Integer countHint) {
    setAttributeValue(COUNT_HINT, countHint);
  }

  /**
   * Returns whether it has the hint at the number of entries in the feed.
   *
   * @return whether it has the hint at the number of entries in the feed
   */
  public boolean hasCountHint() {
    return getCountHint() != null;
  }

  /**
   * Returns the nested feed.
   *
   * @return nested feed
   */
  public Feed getFeed() {
    return getElement(Feed.KEY);
  }

  /**
   * Sets the nested feed.
   *
   * @param feed nested feed or <code>null</code> to reset
   */
  public void setFeed(Feed feed) {
    setElement(Feed.KEY, feed);
  }

  /**
   * Returns whether it has the nested feed.
   *
   * @return whether it has the nested feed
   */
  public boolean hasFeed() {
    return hasElement(Feed.KEY);
  }

  /**
   * Returns the feed URI.
   *
   * @return feed URI
   */
  public String getHref() {
    return getAttributeValue(HREF);
  }

  /**
   * Sets the feed URI.
   *
   * @param href feed URI or <code>null</code> to reset
   */
  public void setHref(String href) {
    setAttributeValue(HREF, href);
  }

  /**
   * Returns whether it has the feed URI.
   *
   * @return whether it has the feed URI
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the whether the contained feed is read-only.
   *
   * @return whether the contained feed is read-only
   */
  public Boolean getReadOnly() {
    return getAttributeValue(READ_ONLY);
  }

  /**
   * Sets the whether the contained feed is read-only.
   *
   * @param readOnly whether the contained feed is read-only or
   *     <code>null</code> to reset
   */
  public void setReadOnly(Boolean readOnly) {
    setAttributeValue(READ_ONLY, readOnly);
  }

  /**
   * Returns whether it has the whether the contained feed is read-only.
   *
   * @return whether it has the whether the contained feed is read-only
   */
  public boolean hasReadOnly() {
    return getReadOnly() != null;
  }

  /**
   * Returns the feed relation type.
   *
   * @return feed relation type
   */
  public String getRel() {
    return getAttributeValue(REL);
  }

  /**
   * Sets the feed relation type.
   *
   * @param rel feed relation type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    setAttributeValue(REL, rel);
  }

  /**
   * Returns whether it has the feed relation type.
   *
   * @return whether it has the feed relation type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  public String toString() {
    return "{FeedLink countHint=" + getAttributeValue(COUNT_HINT) + " href="
        + getAttributeValue(HREF) + " readOnly=" + getAttributeValue(READ_ONLY)
        + " rel=" + getAttributeValue(REL) + "}";
  }

  public String getType() {
    return ContentType.getAtomFeed().toString();
  }

  public void setType(String type) {
    throw new UnsupportedOperationException("Type property not modifiable in "
        + FeedLink.class.getSimpleName());
  }
}
