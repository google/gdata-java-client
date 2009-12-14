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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * List of metadata updates.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ProjectHostingNamespace.ISSUES_ALIAS,
    nsUri = ProjectHostingNamespace.ISSUES,
    localName = Updates.XML_NAME)
public class Updates extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "updates";

  /**
   * Default mutable constructor.
   */
  public Updates() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Updates.class)) {
      return;
    }
    extProfile.declare(Updates.class,
        BlockedOnUpdate.getDefaultDescription(false, true));
    extProfile.declare(Updates.class, CcUpdate.getDefaultDescription(false,
        true));
    extProfile.declare(Updates.class, Label.getDefaultDescription(false, true));
    extProfile.declare(Updates.class, MergedIntoUpdate.class);
    extProfile.declare(Updates.class, OwnerUpdate.class);
    extProfile.declare(Updates.class, Status.class);
    extProfile.declare(Updates.class, Summary.class);
  }

  /**
   * Returns the blocked on updates.
   *
   * @return blocked on updates
   */
  public List<BlockedOnUpdate> getBlockedOnUpdates() {
    return getRepeatingExtension(BlockedOnUpdate.class);
  }

  /**
   * Adds a new blocked on update.
   *
   * @param blockedOnUpdate blocked on update
   */
  public void addBlockedOnUpdate(BlockedOnUpdate blockedOnUpdate) {
    getBlockedOnUpdates().add(blockedOnUpdate);
  }

  /**
   * Returns whether it has the blocked on updates.
   *
   * @return whether it has the blocked on updates
   */
  public boolean hasBlockedOnUpdates() {
    return hasRepeatingExtension(BlockedOnUpdate.class);
  }

  /**
   * Returns the cc updates.
   *
   * @return cc updates
   */
  public List<CcUpdate> getCcUpdates() {
    return getRepeatingExtension(CcUpdate.class);
  }

  /**
   * Adds a new cc update.
   *
   * @param ccUpdate cc update
   */
  public void addCcUpdate(CcUpdate ccUpdate) {
    getCcUpdates().add(ccUpdate);
  }

  /**
   * Returns whether it has the cc updates.
   *
   * @return whether it has the cc updates
   */
  public boolean hasCcUpdates() {
    return hasRepeatingExtension(CcUpdate.class);
  }

  /**
   * Returns the labels.
   *
   * @return labels
   */
  public List<Label> getLabels() {
    return getRepeatingExtension(Label.class);
  }

  /**
   * Adds a new label.
   *
   * @param label label
   */
  public void addLabel(Label label) {
    getLabels().add(label);
  }

  /**
   * Returns whether it has the labels.
   *
   * @return whether it has the labels
   */
  public boolean hasLabels() {
    return hasRepeatingExtension(Label.class);
  }

  /**
   * Returns the merged into update.
   *
   * @return merged into update
   */
  public MergedIntoUpdate getMergedIntoUpdate() {
    return getExtension(MergedIntoUpdate.class);
  }

  /**
   * Sets the merged into update.
   *
   * @param mergedIntoUpdate merged into update or <code>null</code> to reset
   */
  public void setMergedIntoUpdate(MergedIntoUpdate mergedIntoUpdate) {
    if (mergedIntoUpdate == null) {
      removeExtension(MergedIntoUpdate.class);
    } else {
      setExtension(mergedIntoUpdate);
    }
  }

  /**
   * Returns whether it has the merged into update.
   *
   * @return whether it has the merged into update
   */
  public boolean hasMergedIntoUpdate() {
    return hasExtension(MergedIntoUpdate.class);
  }

  /**
   * Returns the owner update.
   *
   * @return owner update
   */
  public OwnerUpdate getOwnerUpdate() {
    return getExtension(OwnerUpdate.class);
  }

  /**
   * Sets the owner update.
   *
   * @param ownerUpdate owner update or <code>null</code> to reset
   */
  public void setOwnerUpdate(OwnerUpdate ownerUpdate) {
    if (ownerUpdate == null) {
      removeExtension(OwnerUpdate.class);
    } else {
      setExtension(ownerUpdate);
    }
  }

  /**
   * Returns whether it has the owner update.
   *
   * @return whether it has the owner update
   */
  public boolean hasOwnerUpdate() {
    return hasExtension(OwnerUpdate.class);
  }

  /**
   * Returns the status.
   *
   * @return status
   */
  public Status getStatus() {
    return getExtension(Status.class);
  }

  /**
   * Sets the status.
   *
   * @param status status or <code>null</code> to reset
   */
  public void setStatus(Status status) {
    if (status == null) {
      removeExtension(Status.class);
    } else {
      setExtension(status);
    }
  }

  /**
   * Returns whether it has the status.
   *
   * @return whether it has the status
   */
  public boolean hasStatus() {
    return hasExtension(Status.class);
  }

  /**
   * Returns the summary.
   *
   * @return summary
   */
  public Summary getSummary() {
    return getExtension(Summary.class);
  }

  /**
   * Sets the summary.
   *
   * @param summary summary or <code>null</code> to reset
   */
  public void setSummary(Summary summary) {
    if (summary == null) {
      removeExtension(Summary.class);
    } else {
      setExtension(summary);
    }
  }

  /**
   * Returns whether it has the summary.
   *
   * @return whether it has the summary
   */
  public boolean hasSummary() {
    return hasExtension(Summary.class);
  }

  @Override
  protected void validate() {
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
        ExtensionDescription.getDefaultDescription(Updates.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Updates}";
  }

}

