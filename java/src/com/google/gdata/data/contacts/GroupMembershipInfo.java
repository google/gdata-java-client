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


package com.google.gdata.data.contacts;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Denotes contact's group membership.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = GroupMembershipInfo.XML_NAME)
public class GroupMembershipInfo extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "groupMembershipInfo";

  /** XML "deleted" attribute name */
  private static final String DELETED = "deleted";

  /** XML "href" attribute name */
  private static final String HREF = "href";

  /** Whether the contact was removed from the group */
  private Boolean deleted = null;

  /** URI of the group */
  private String href = null;

  /**
   * Default mutable constructor.
   */
  public GroupMembershipInfo() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param deleted whether the contact was removed from the group.
   * @param href URI of the group.
   */
  public GroupMembershipInfo(Boolean deleted, String href) {
    super();
    setDeleted(deleted);
    setHref(href);
    setImmutable(true);
  }

  /**
   * Returns the whether the contact was removed from the group.
   *
   * @return whether the contact was removed from the group
   */
  public Boolean getDeleted() {
    return deleted;
  }

  /**
   * Sets the whether the contact was removed from the group.
   *
   * @param deleted whether the contact was removed from the group or
   *     <code>null</code> to reset
   */
  public void setDeleted(Boolean deleted) {
    throwExceptionIfImmutable();
    this.deleted = deleted;
  }

  /**
   * Returns whether it has the whether the contact was removed from the group.
   *
   * @return whether it has the whether the contact was removed from the group
   */
  public boolean hasDeleted() {
    return getDeleted() != null;
  }

  /**
   * Returns the URI of the group.
   *
   * @return URI of the group
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the URI of the group.
   *
   * @param href URI of the group or <code>null</code> to reset
   */
  public void setHref(String href) {
    throwExceptionIfImmutable();
    this.href = href;
  }

  /**
   * Returns whether it has the URI of the group.
   *
   * @return whether it has the URI of the group
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  @Override
  protected void validate() {
    if (href == null) {
      throwExceptionForMissingAttribute(HREF);
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
        ExtensionDescription.getDefaultDescription(GroupMembershipInfo.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(DELETED, deleted);
    generator.put(HREF, href);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    deleted = helper.consumeBoolean(DELETED, false);
    href = helper.consume(HREF, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    GroupMembershipInfo other = (GroupMembershipInfo) obj;
    return eq(deleted, other.deleted)
        && eq(href, other.href);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (deleted != null) {
      result = 37 * result + deleted.hashCode();
    }
    if (href != null) {
      result = 37 * result + href.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{GroupMembershipInfo deleted=" + deleted + " href=" + href + "}";
  }

}

