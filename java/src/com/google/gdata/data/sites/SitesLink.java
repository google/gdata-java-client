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


package com.google.gdata.data.sites;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.Link;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Describes a sites link.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = "atom",
    nsUri = Namespaces.atom,
    localName = SitesLink.XML_NAME)
public class SitesLink extends Link {

  /** XML element name */
  static final String XML_NAME = "link";

  /** XML "thr:count" attribute name */
  private static final String COUNT = "thr:count";

  /** XML "thr:updated" attribute name */
  private static final String UPDATED = "thr:updated";

  /** Provides a hint to clients as to the total number of replies contained by
   * the linked resource */
  private Integer count = null;

  /** Provides a hint to clients as to the date and time of the most recently
   * updated reply contained by the linked resource */
  private DateTime updated = null;

  /** Link relation type. */
  public static final class Rel {

    /** Attachments sites link. */
    public static final String ATTACHMENTS = SitesNamespace.SITES_PREFIX +
        "attachments";

    /** Current sites link. */
    public static final String CURRENT = SitesNamespace.SITES_PREFIX +
        "current";

    /** Invite sites link. */
    public static final String INVITE = SitesNamespace.SITES_PREFIX + "invite";

    /** Parent sites link. */
    public static final String PARENT = SitesNamespace.SITES_PREFIX + "parent";

    /** Replies sites link. */
    public static final String REPLIES = "replies";

    /** Revision sites link. */
    public static final String REVISION = SitesNamespace.SITES_PREFIX +
        "revision";

    /** Source sites link. */
    public static final String SOURCE = SitesNamespace.SITES_PREFIX + "source";

    /** Template sites link. */
    public static final String TEMPLATE = SitesNamespace.SITES_PREFIX +
        "template";

    /** WebAddressMapping sites link. */
    public static final String WEBADDRESSMAPPING = SitesNamespace.SITES_PREFIX +
        "webAddressMapping";

  }

  /** MIME type of link target. */
  public static final class Type {

    /** Application/xhtml+xml sites link. */
    public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";

  }

  /**
   * Default mutable constructor.
   */
  public SitesLink() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param count provides a hint to clients as to the total number of replies
   *     contained by the linked resource.
   * @param updated provides a hint to clients as to the date and time of the
   *     most recently updated reply contained by the linked resource.
   */
  public SitesLink(Integer count, String href, String hrefLang, Long length,
      String rel, String title, String titleLang, String type,
      DateTime updated) {
    super();
    setCount(count);
    setUpdated(updated);
    setImmutable(true);
  }

  /**
   * Returns the provides a hint to clients as to the total number of replies
   * contained by the linked resource.
   *
   * @return provides a hint to clients as to the total number of replies
   *     contained by the linked resource
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Sets the provides a hint to clients as to the total number of replies
   * contained by the linked resource.
   *
   * @param count provides a hint to clients as to the total number of replies
   *     contained by the linked resource or <code>null</code> to reset
   */
  public void setCount(Integer count) {
    throwExceptionIfImmutable();
    this.count = count;
  }

  /**
   * Returns whether it has the provides a hint to clients as to the total
   * number of replies contained by the linked resource.
   *
   * @return whether it has the provides a hint to clients as to the total
   *     number of replies contained by the linked resource
   */
  public boolean hasCount() {
    return getCount() != null;
  }

  /**
   * Returns the provides a hint to clients as to the date and time of the most
   * recently updated reply contained by the linked resource.
   *
   * @return provides a hint to clients as to the date and time of the most
   *     recently updated reply contained by the linked resource
   */
  public DateTime getUpdated() {
    return updated;
  }

  /**
   * Sets the provides a hint to clients as to the date and time of the most
   * recently updated reply contained by the linked resource.
   *
   * @param updated provides a hint to clients as to the date and time of the
   *     most recently updated reply contained by the linked resource or
   *     <code>null</code> to reset
   */
  public void setUpdated(DateTime updated) {
    throwExceptionIfImmutable();
    this.updated = updated;
  }

  /**
   * Returns whether it has the provides a hint to clients as to the date and
   * time of the most recently updated reply contained by the linked resource.
   *
   * @return whether it has the provides a hint to clients as to the date and
   *     time of the most recently updated reply contained by the linked
   *     resource
   */
  public boolean hasUpdated() {
    return getUpdated() != null;
  }

  @Override
  protected void validate() {
    if (count != null && count < 0) {
      throw new
          IllegalStateException("thr:count attribute must be non-negative: " +
          count);
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
        ExtensionDescription.getDefaultDescription(SitesLink.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(COUNT, count);
    generator.put(UPDATED, updated);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    count = helper.consumeInteger("count", false);
    updated = helper.consumeDateTime("updated", false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    SitesLink other = (SitesLink) obj;
    return eq(count, other.count)
        && eq(updated, other.updated);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (count != null) {
      result = 37 * result + count.hashCode();
    }
    if (updated != null) {
      result = 37 * result + updated.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{SitesLink count=" + count + " updated=" + updated + " " +
        super.toString() + "}";
  }

}

