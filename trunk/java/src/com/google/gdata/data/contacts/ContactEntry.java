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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a contact entry.
 *
 * 
 */
@Kind.Term(ContactEntry.KIND)
public class ContactEntry extends BaseEntry<ContactEntry> {

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
        new ExtensionDescription(Deleted.class, new XmlWriter.Namespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(ContactEntry.class, new ExtensionDescription(Email.class,
        new XmlWriter.Namespace("gd", "http://schemas.google.com/g/2005"),
        "email", false, true, false));
    extProfile.declare(ContactEntry.class,
        new ExtensionDescription(ExtendedProperty.class,
        new XmlWriter.Namespace("gd", "http://schemas.google.com/g/2005"),
        "extendedProperty", false, true, false));
    extProfile.declare(ContactEntry.class,
        GroupMembershipInfo.getDefaultDescription(false, true));
    extProfile.declare(ContactEntry.class, Im.getDefaultDescription(false,
        true));
    extProfile.declare(ContactEntry.class,
        Organization.getDefaultDescription(false, true));
    new Organization().declareExtensions(extProfile);
    extProfile.declare(ContactEntry.class,
        new ExtensionDescription(PhoneNumber.class,
        new XmlWriter.Namespace("gd", "http://schemas.google.com/g/2005"),
        "phoneNumber", false, true, false));
    extProfile.declare(ContactEntry.class,
        new ExtensionDescription(PostalAddress.class,
        new XmlWriter.Namespace("gd", "http://schemas.google.com/g/2005"),
        "postalAddress", false, true, false));
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
   * Returns the email addresses.
   *
   * @return email addresses
   */
  public List<Email> getEmailAddresses() {
    return getRepeatingExtension(Email.class);
  }

  /**
   * Adds a new email address.
   *
   * @param emailAddress email address
   */
  public void addEmailAddress(Email emailAddress) {
    getEmailAddresses().add(emailAddress);
  }

  /**
   * Returns whether it has the email addresses.
   *
   * @return whether it has the email addresses
   */
  public boolean hasEmailAddresses() {
    return hasRepeatingExtension(Email.class);
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
   * Returns the instant messaging addresses.
   *
   * @return instant messaging addresses
   */
  public List<Im> getImAddresses() {
    return getRepeatingExtension(Im.class);
  }

  /**
   * Adds a new instant messaging address.
   *
   * @param imAddress instant messaging address
   */
  public void addImAddress(Im imAddress) {
    getImAddresses().add(imAddress);
  }

  /**
   * Returns whether it has the instant messaging addresses.
   *
   * @return whether it has the instant messaging addresses
   */
  public boolean hasImAddresses() {
    return hasRepeatingExtension(Im.class);
  }

  /**
   * Returns the organizations.
   *
   * @return organizations
   */
  public List<Organization> getOrganizations() {
    return getRepeatingExtension(Organization.class);
  }

  /**
   * Adds a new organization.
   *
   * @param organization organization
   */
  public void addOrganization(Organization organization) {
    getOrganizations().add(organization);
  }

  /**
   * Returns whether it has the organizations.
   *
   * @return whether it has the organizations
   */
  public boolean hasOrganizations() {
    return hasRepeatingExtension(Organization.class);
  }

  /**
   * Returns the phone numbers.
   *
   * @return phone numbers
   */
  public List<PhoneNumber> getPhoneNumbers() {
    return getRepeatingExtension(PhoneNumber.class);
  }

  /**
   * Adds a new phone number.
   *
   * @param phoneNumber phone number
   */
  public void addPhoneNumber(PhoneNumber phoneNumber) {
    getPhoneNumbers().add(phoneNumber);
  }

  /**
   * Returns whether it has the phone numbers.
   *
   * @return whether it has the phone numbers
   */
  public boolean hasPhoneNumbers() {
    return hasRepeatingExtension(PhoneNumber.class);
  }

  /**
   * Returns the postal addresses.
   *
   * @return postal addresses
   */
  public List<PostalAddress> getPostalAddresses() {
    return getRepeatingExtension(PostalAddress.class);
  }

  /**
   * Adds a new postal address.
   *
   * @param postalAddress postal address
   */
  public void addPostalAddress(PostalAddress postalAddress) {
    getPostalAddresses().add(postalAddress);
  }

  /**
   * Returns whether it has the postal addresses.
   *
   * @return whether it has the postal addresses
   */
  public boolean hasPostalAddresses() {
    return hasRepeatingExtension(PostalAddress.class);
  }

  /**
   * Returns the link to edit contact photo.
   *
   * @return Link to edit contact photo or {@code null} for none.
   */
  public Link getContactEditPhotoLink() {
    return getLink(ContactLink.Rel.EDIT_CONTACT_PHOTO, ContactLink.Type.IMAGE);
  }

  /**
   * Returns the link that provides the contact photo.
   *
   * @return Link that provides the contact photo or {@code null} for none.
   */
  public Link getContactPhotoLink() {
    return getLink(ContactLink.Rel.CONTACT_PHOTO, ContactLink.Type.IMAGE);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ContactEntry " + super.toString() + "}";
  }


  /**
   * Returns the gender.
   *
   * @return gender
   */
  public Gender getGender() {
    return getExtension(Gender.class);
  }

  /**
   * Sets the gender.
   *
   * @param gender gender or <code>null</code> to reset
   */
  public void setGender(Gender gender) {
    if (gender == null) {
      removeExtension(Gender.class);
    } else {
      setExtension(gender);
    }
  }

  /**
   * Returns whether it has the gender.
   *
   * @return whether it has the gender
   */
  public boolean hasGender() {
    return hasExtension(Gender.class);
  }
}
