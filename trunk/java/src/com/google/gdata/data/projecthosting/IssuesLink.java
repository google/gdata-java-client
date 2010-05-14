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


package com.google.gdata.data.projecthosting;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.Link;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Extends the base Link class with Project Hosting extensions.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = "atom",
    nsUri = Namespaces.atom,
    localName = IssuesLink.XML_NAME)
public class IssuesLink extends Link {

  /** XML element name */
  static final String XML_NAME = "link";

  /** XML "thr:count" attribute name */
  private static final String COUNT = "thr:count";

  /** XML "thr:updated" attribute name */
  private static final String UPDATED = "thr:updated";

  /** Count */
  private Integer count = null;

  /** Updated */
  private DateTime updated = null;

  /** Link relation type. */
  public static final class Rel {

    /** Replies Project Hosting Link class. */
    public static final String REPLIES = "replies";

  }

  /**
   * Default mutable constructor.
   */
  public IssuesLink() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param count count.
   * @param updated updated.
   */
  public IssuesLink(Integer count, String href, Long length, Rel rel,
      String title, String type, DateTime updated) {
    super();
    setCount(count);
    setUpdated(updated);
    setImmutable(true);
  }

  /**
   * Returns the count.
   *
   * @return count
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Sets the count.
   *
   * @param count count or <code>null</code> to reset
   */
  public void setCount(Integer count) {
    throwExceptionIfImmutable();
    this.count = count;
  }

  /**
   * Returns whether it has the count.
   *
   * @return whether it has the count
   */
  public boolean hasCount() {
    return getCount() != null;
  }

  /**
   * Returns the updated.
   *
   * @return updated
   */
  public DateTime getUpdated() {
    return updated;
  }

  /**
   * Sets the updated.
   *
   * @param updated updated or <code>null</code> to reset
   */
  public void setUpdated(DateTime updated) {
    throwExceptionIfImmutable();
    this.updated = updated;
  }

  /**
   * Returns whether it has the updated.
   *
   * @return whether it has the updated
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
        ExtensionDescription.getDefaultDescription(IssuesLink.class);
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
    IssuesLink other = (IssuesLink) obj;
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
    return "{IssuesLink count=" + count + " updated=" + updated + " " +
        super.toString() + "}";
  }

}

