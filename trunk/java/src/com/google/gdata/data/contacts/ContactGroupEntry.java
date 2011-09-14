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
import com.google.gdata.data.batch.BatchId;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.data.batch.BatchOperation;
import com.google.gdata.data.batch.BatchStatus;
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
        new ExtensionDescription(BatchId.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "id", false, false, false));
    extProfile.declare(ContactGroupEntry.class,
        new ExtensionDescription(BatchInterrupted.class,
        new XmlNamespace("batch", "http://schemas.google.com/gdata/batch"),
        "interrupted", false, false, false));
    extProfile.declare(ContactGroupEntry.class,
        new ExtensionDescription(BatchOperation.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "operation", false, false,
        false));
    extProfile.declare(ContactGroupEntry.class,
        new ExtensionDescription(BatchStatus.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "status", false, false,
        false));
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
   * Returns the batch identifier.
   *
   * @return batch identifier
   */
  public BatchId getBatchId() {
    return getExtension(BatchId.class);
  }

  /**
   * Sets the batch identifier.
   *
   * @param batchId batch identifier or <code>null</code> to reset
   */
  public void setBatchId(BatchId batchId) {
    if (batchId == null) {
      removeExtension(BatchId.class);
    } else {
      setExtension(batchId);
    }
  }

  /**
   * Returns whether it has the batch identifier.
   *
   * @return whether it has the batch identifier
   */
  public boolean hasBatchId() {
    return hasExtension(BatchId.class);
  }

  /**
   * Returns the batch interruption information.
   *
   * @return batch interruption information
   */
  public BatchInterrupted getBatchInterrupted() {
    return getExtension(BatchInterrupted.class);
  }

  /**
   * Sets the batch interruption information.
   *
   * @param batchInterrupted batch interruption information or <code>null</code>
   *     to reset
   */
  public void setBatchInterrupted(BatchInterrupted batchInterrupted) {
    if (batchInterrupted == null) {
      removeExtension(BatchInterrupted.class);
    } else {
      setExtension(batchInterrupted);
    }
  }

  /**
   * Returns whether it has the batch interruption information.
   *
   * @return whether it has the batch interruption information
   */
  public boolean hasBatchInterrupted() {
    return hasExtension(BatchInterrupted.class);
  }

  /**
   * Returns the batch operation.
   *
   * @return batch operation
   */
  public BatchOperation getBatchOperation() {
    return getExtension(BatchOperation.class);
  }

  /**
   * Sets the batch operation.
   *
   * @param batchOperation batch operation or <code>null</code> to reset
   */
  public void setBatchOperation(BatchOperation batchOperation) {
    if (batchOperation == null) {
      removeExtension(BatchOperation.class);
    } else {
      setExtension(batchOperation);
    }
  }

  /**
   * Returns whether it has the batch operation.
   *
   * @return whether it has the batch operation
   */
  public boolean hasBatchOperation() {
    return hasExtension(BatchOperation.class);
  }

  /**
   * Returns the batch response status information.
   *
   * @return batch response status information
   */
  public BatchStatus getBatchStatus() {
    return getExtension(BatchStatus.class);
  }

  /**
   * Sets the batch response status information.
   *
   * @param batchStatus batch response status information or <code>null</code>
   *     to reset
   */
  public void setBatchStatus(BatchStatus batchStatus) {
    if (batchStatus == null) {
      removeExtension(BatchStatus.class);
    } else {
      setExtension(batchStatus);
    }
  }

  /**
   * Returns whether it has the batch response status information.
   *
   * @return whether it has the batch response status information
   */
  public boolean hasBatchStatus() {
    return hasExtension(BatchStatus.class);
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
   * Returns the contact extended properties.
   *
   * @return contact extended properties
   */
  public List<ExtendedProperty> getExtendedProperties() {
    return getRepeatingExtension(ExtendedProperty.class);
  }

  /**
   * Adds a new contact extended property.
   *
   * @param extendedProperty contact extended property
   */
  public void addExtendedProperty(ExtendedProperty extendedProperty) {
    getExtendedProperties().add(extendedProperty);
  }

  /**
   * Returns whether it has the contact extended properties.
   *
   * @return whether it has the contact extended properties
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

