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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a contact entry.
 *
 * 
 */
@Kind.Term(ContactEntry.KIND)
public class ContactEntry extends BasePersonEntry<ContactEntry> {

  /**
   * Contact kind term value.
   */
  public static final String KIND = ContactsNamespace.GCONTACT_PREFIX +
      "contact";

  /**
   * Contact kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public ContactEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ContactEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ContactEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ContactEntry.class,
        new ExtensionDescription(Deleted.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(ContactEntry.class,
        GroupMembershipInfo.getDefaultDescription(false, true));
    extProfile.declare(ContactEntry.class, YomiName.class);
  }

  /**
   * Returns the marker for deleted entries.
   *
   * @return marker for deleted entries
   */
  public Deleted getDeleted() {
    return getExtension(Deleted.class);
  }

  /**
   * Sets the marker for deleted entries.
   *
   * @param deleted marker for deleted entries or <code>null</code> to reset
   */
  public void setDeleted(Deleted deleted) {
    if (deleted == null) {
      removeExtension(Deleted.class);
    } else {
      setExtension(deleted);
    }
  }

  /**
   * Returns whether it has the marker for deleted entries.
   *
   * @return whether it has the marker for deleted entries
   */
  public boolean hasDeleted() {
    return hasExtension(Deleted.class);
  }

  /**
   * Returns the group membership infos.
   *
   * @return group membership infos
   */
  public List<GroupMembershipInfo> getGroupMembershipInfos() {
    return getRepeatingExtension(GroupMembershipInfo.class);
  }

  /**
   * Adds a new group membership info.
   *
   * @param groupMembershipInfo group membership info
   */
  public void addGroupMembershipInfo(GroupMembershipInfo groupMembershipInfo) {
    getGroupMembershipInfos().add(groupMembershipInfo);
  }

  /**
   * Returns whether it has the group membership infos.
   *
   * @return whether it has the group membership infos
   */
  public boolean hasGroupMembershipInfos() {
    return hasRepeatingExtension(GroupMembershipInfo.class);
  }

  /**
   * Returns the yomi name.
   *
   * @return yomi name
   */
  public YomiName getYomiName() {
    return getExtension(YomiName.class);
  }

  /**
   * Sets the yomi name.
   *
   * @param yomiName yomi name or <code>null</code> to reset
   */
  public void setYomiName(YomiName yomiName) {
    if (yomiName == null) {
      removeExtension(YomiName.class);
    } else {
      setExtension(yomiName);
    }
  }

  /**
   * Returns whether it has the yomi name.
   *
   * @return whether it has the yomi name
   */
  public boolean hasYomiName() {
    return hasExtension(YomiName.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ContactEntry " + super.toString() + "}";
  }

}

