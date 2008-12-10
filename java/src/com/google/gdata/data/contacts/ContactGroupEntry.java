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
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a contact group entry.
 *
 * 
 */
@Kind.Term(ContactGroupEntry.KIND)
public class ContactGroupEntry extends BaseEntry<ContactGroupEntry> {

  /**
   * Contact group kind term value.
   */
  public static final String KIND = ContactsNamespace.GCONTACT_PREFIX + "group";

  /**
   * Contact group kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public ContactGroupEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ContactGroupEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ContactGroupEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ContactGroupEntry.class,
        new ExtensionDescription(Deleted.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(ContactGroupEntry.class,
        new ExtensionDescription(ExtendedProperty.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "extendedProperty", false, true,
        false));
    extProfile.declare(ContactGroupEntry.class, SystemGroup.class);
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
   * Returns the extended properties.
   *
   * @return extended properties
   */
  public List<ExtendedProperty> getExtendedProperties() {
    return getRepeatingExtension(ExtendedProperty.class);
  }

  /**
   * Adds a new extended property.
   *
   * @param extendedProperty extended property
   */
  public void addExtendedProperty(ExtendedProperty extendedProperty) {
    getExtendedProperties().add(extendedProperty);
  }

  /**
   * Returns whether it has the extended properties.
   *
   * @return whether it has the extended properties
   */
  public boolean hasExtendedProperties() {
    return hasRepeatingExtension(ExtendedProperty.class);
  }

  /**
   * Returns the system group.
   *
   * @return system group
   */
  public SystemGroup getSystemGroup() {
    return getExtension(SystemGroup.class);
  }

  /**
   * Sets the system group.
   *
   * @param systemGroup system group or <code>null</code> to reset
   */
  public void setSystemGroup(SystemGroup systemGroup) {
    if (systemGroup == null) {
      removeExtension(SystemGroup.class);
    } else {
      setExtension(systemGroup);
    }
  }

  /**
   * Returns whether it has the system group.
   *
   * @return whether it has the system group
   */
  public boolean hasSystemGroup() {
    return hasExtension(SystemGroup.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ContactGroupEntry " + super.toString() + "}";
  }

}
