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


package com.google.gdata.data.docs;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.QuotaBytesUsed;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes an Archive entry.
 *
 * 
 */
@Kind.Term(ArchiveEntry.KIND)
public class ArchiveEntry extends BaseEntry<ArchiveEntry> {

  /**
   * Archive kind term value.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX + "archive";

  /**
   * Archive kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "archive");

  /**
   * Default mutable constructor.
   */
  public ArchiveEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ArchiveEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ArchiveEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ArchiveEntry.class, ArchiveComplete.class);
    extProfile.declare(ArchiveEntry.class,
        ArchiveConversion.getDefaultDescription(true, true));
    extProfile.declare(ArchiveEntry.class,
        ArchiveFailure.getDefaultDescription(false, true));
    extProfile.declare(ArchiveEntry.class, ArchiveNotify.class);
    extProfile.declare(ArchiveEntry.class, ArchiveNotifyStatus.class);
    extProfile.declare(ArchiveEntry.class,
        ArchiveResourceId.getDefaultDescription(false, true));
    extProfile.declare(ArchiveEntry.class, ArchiveStatus.class);
    extProfile.declare(ArchiveEntry.class, ArchiveTotal.class);
    extProfile.declare(ArchiveEntry.class, ArchiveTotalComplete.class);
    extProfile.declare(ArchiveEntry.class, ArchiveTotalFailure.class);
    extProfile.declare(ArchiveEntry.class, QuotaBytesUsed.class);
  }

  /**
   * Returns the archive complete.
   *
   * @return archive complete
   */
  public ArchiveComplete getArchiveComplete() {
    return getExtension(ArchiveComplete.class);
  }

  /**
   * Sets the archive complete.
   *
   * @param archiveComplete archive complete or <code>null</code> to reset
   */
  public void setArchiveComplete(ArchiveComplete archiveComplete) {
    if (archiveComplete == null) {
      removeExtension(ArchiveComplete.class);
    } else {
      setExtension(archiveComplete);
    }
  }

  /**
   * Returns whether it has the archive complete.
   *
   * @return whether it has the archive complete
   */
  public boolean hasArchiveComplete() {
    return hasExtension(ArchiveComplete.class);
  }

  /**
   * Returns the archive conversions.
   *
   * @return archive conversions
   */
  public List<ArchiveConversion> getArchiveConversions() {
    return getRepeatingExtension(ArchiveConversion.class);
  }

  /**
   * Adds a new archive conversion.
   *
   * @param archiveConversion archive conversion
   */
  public void addArchiveConversion(ArchiveConversion archiveConversion) {
    getArchiveConversions().add(archiveConversion);
  }

  /**
   * Returns whether it has the archive conversions.
   *
   * @return whether it has the archive conversions
   */
  public boolean hasArchiveConversions() {
    return hasRepeatingExtension(ArchiveConversion.class);
  }

  /**
   * Returns the archive failures.
   *
   * @return archive failures
   */
  public List<ArchiveFailure> getArchiveFailures() {
    return getRepeatingExtension(ArchiveFailure.class);
  }

  /**
   * Adds a new archive failure.
   *
   * @param archiveFailure archive failure
   */
  public void addArchiveFailure(ArchiveFailure archiveFailure) {
    getArchiveFailures().add(archiveFailure);
  }

  /**
   * Returns whether it has the archive failures.
   *
   * @return whether it has the archive failures
   */
  public boolean hasArchiveFailures() {
    return hasRepeatingExtension(ArchiveFailure.class);
  }

  /**
   * Returns the archive notify.
   *
   * @return archive notify
   */
  public ArchiveNotify getArchiveNotify() {
    return getExtension(ArchiveNotify.class);
  }

  /**
   * Sets the archive notify.
   *
   * @param archiveNotify archive notify or <code>null</code> to reset
   */
  public void setArchiveNotify(ArchiveNotify archiveNotify) {
    if (archiveNotify == null) {
      removeExtension(ArchiveNotify.class);
    } else {
      setExtension(archiveNotify);
    }
  }

  /**
   * Returns whether it has the archive notify.
   *
   * @return whether it has the archive notify
   */
  public boolean hasArchiveNotify() {
    return hasExtension(ArchiveNotify.class);
  }

  /**
   * Returns the archive notify status.
   *
   * @return archive notify status
   */
  public ArchiveNotifyStatus getArchiveNotifyStatus() {
    return getExtension(ArchiveNotifyStatus.class);
  }

  /**
   * Sets the archive notify status.
   *
   * @param archiveNotifyStatus archive notify status or <code>null</code> to
   *     reset
   */
  public void setArchiveNotifyStatus(ArchiveNotifyStatus archiveNotifyStatus) {
    if (archiveNotifyStatus == null) {
      removeExtension(ArchiveNotifyStatus.class);
    } else {
      setExtension(archiveNotifyStatus);
    }
  }

  /**
   * Returns whether it has the archive notify status.
   *
   * @return whether it has the archive notify status
   */
  public boolean hasArchiveNotifyStatus() {
    return hasExtension(ArchiveNotifyStatus.class);
  }

  /**
   * Returns the archive resource ids.
   *
   * @return archive resource ids
   */
  public List<ArchiveResourceId> getArchiveResourceIds() {
    return getRepeatingExtension(ArchiveResourceId.class);
  }

  /**
   * Adds a new archive resource id.
   *
   * @param archiveResourceId archive resource id
   */
  public void addArchiveResourceId(ArchiveResourceId archiveResourceId) {
    getArchiveResourceIds().add(archiveResourceId);
  }

  /**
   * Returns whether it has the archive resource ids.
   *
   * @return whether it has the archive resource ids
   */
  public boolean hasArchiveResourceIds() {
    return hasRepeatingExtension(ArchiveResourceId.class);
  }

  /**
   * Returns the archive status.
   *
   * @return archive status
   */
  public ArchiveStatus getArchiveStatus() {
    return getExtension(ArchiveStatus.class);
  }

  /**
   * Sets the archive status.
   *
   * @param archiveStatus archive status or <code>null</code> to reset
   */
  public void setArchiveStatus(ArchiveStatus archiveStatus) {
    if (archiveStatus == null) {
      removeExtension(ArchiveStatus.class);
    } else {
      setExtension(archiveStatus);
    }
  }

  /**
   * Returns whether it has the archive status.
   *
   * @return whether it has the archive status
   */
  public boolean hasArchiveStatus() {
    return hasExtension(ArchiveStatus.class);
  }

  /**
   * Returns the archive total.
   *
   * @return archive total
   */
  public ArchiveTotal getArchiveTotal() {
    return getExtension(ArchiveTotal.class);
  }

  /**
   * Sets the archive total.
   *
   * @param archiveTotal archive total or <code>null</code> to reset
   */
  public void setArchiveTotal(ArchiveTotal archiveTotal) {
    if (archiveTotal == null) {
      removeExtension(ArchiveTotal.class);
    } else {
      setExtension(archiveTotal);
    }
  }

  /**
   * Returns whether it has the archive total.
   *
   * @return whether it has the archive total
   */
  public boolean hasArchiveTotal() {
    return hasExtension(ArchiveTotal.class);
  }

  /**
   * Returns the archive total complete.
   *
   * @return archive total complete
   */
  public ArchiveTotalComplete getArchiveTotalComplete() {
    return getExtension(ArchiveTotalComplete.class);
  }

  /**
   * Sets the archive total complete.
   *
   * @param archiveTotalComplete archive total complete or <code>null</code> to
   *     reset
   */
  public void setArchiveTotalComplete(ArchiveTotalComplete archiveTotalComplete)
      {
    if (archiveTotalComplete == null) {
      removeExtension(ArchiveTotalComplete.class);
    } else {
      setExtension(archiveTotalComplete);
    }
  }

  /**
   * Returns whether it has the archive total complete.
   *
   * @return whether it has the archive total complete
   */
  public boolean hasArchiveTotalComplete() {
    return hasExtension(ArchiveTotalComplete.class);
  }

  /**
   * Returns the archive total failure.
   *
   * @return archive total failure
   */
  public ArchiveTotalFailure getArchiveTotalFailure() {
    return getExtension(ArchiveTotalFailure.class);
  }

  /**
   * Sets the archive total failure.
   *
   * @param archiveTotalFailure archive total failure or <code>null</code> to
   *     reset
   */
  public void setArchiveTotalFailure(ArchiveTotalFailure archiveTotalFailure) {
    if (archiveTotalFailure == null) {
      removeExtension(ArchiveTotalFailure.class);
    } else {
      setExtension(archiveTotalFailure);
    }
  }

  /**
   * Returns whether it has the archive total failure.
   *
   * @return whether it has the archive total failure
   */
  public boolean hasArchiveTotalFailure() {
    return hasExtension(ArchiveTotalFailure.class);
  }

  /**
   * Returns the quota bytes used.
   *
   * @return quota bytes used
   */
  public QuotaBytesUsed getQuotaBytesUsed() {
    return getExtension(QuotaBytesUsed.class);
  }

  /**
   * Sets the quota bytes used.
   *
   * @param quotaBytesUsed quota bytes used or <code>null</code> to reset
   */
  public void setQuotaBytesUsed(QuotaBytesUsed quotaBytesUsed) {
    if (quotaBytesUsed == null) {
      removeExtension(QuotaBytesUsed.class);
    } else {
      setExtension(quotaBytesUsed);
    }
  }

  /**
   * Returns whether it has the quota bytes used.
   *
   * @return whether it has the quota bytes used
   */
  public boolean hasQuotaBytesUsed() {
    return hasExtension(QuotaBytesUsed.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ArchiveEntry " + super.toString() + "}";
  }

}

