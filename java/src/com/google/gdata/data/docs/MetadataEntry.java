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
import com.google.gdata.data.extensions.QuotaBytesTotal;
import com.google.gdata.data.extensions.QuotaBytesUsed;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a Metadata entry.
 *
 * 
 */
@Kind.Term(MetadataEntry.KIND)
public class MetadataEntry extends BaseEntry<MetadataEntry> {

  /**
   * Metadata kind term value.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX + "metadata";

  /**
   * Metadata kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "metadata");

  /**
   * Default mutable constructor.
   */
  public MetadataEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public MetadataEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(MetadataEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(MetadataEntry.class,
        ExportFormat.getDefaultDescription(false, true));
    extProfile.declare(MetadataEntry.class, Feature.getDefaultDescription(false,
        true));
    new Feature().declareExtensions(extProfile);
    extProfile.declare(MetadataEntry.class,
        ImportFormat.getDefaultDescription(false, true));
    extProfile.declare(MetadataEntry.class,
        MaxUploadSize.getDefaultDescription(false, true));
    extProfile.declare(MetadataEntry.class, QuotaBytesTotal.class);
    extProfile.declare(MetadataEntry.class, QuotaBytesUsed.class);
    extProfile.declare(MetadataEntry.class, QuotaBytesUsedInTrash.class);
  }

  /**
   * Returns the export formats.
   *
   * @return export formats
   */
  public List<ExportFormat> getExportFormats() {
    return getRepeatingExtension(ExportFormat.class);
  }

  /**
   * Adds a new export format.
   *
   * @param exportFormat export format
   */
  public void addExportFormat(ExportFormat exportFormat) {
    getExportFormats().add(exportFormat);
  }

  /**
   * Returns whether it has the export formats.
   *
   * @return whether it has the export formats
   */
  public boolean hasExportFormats() {
    return hasRepeatingExtension(ExportFormat.class);
  }

  /**
   * Returns the features.
   *
   * @return features
   */
  public List<Feature> getFeatures() {
    return getRepeatingExtension(Feature.class);
  }

  /**
   * Adds a new feature.
   *
   * @param feature feature
   */
  public void addFeature(Feature feature) {
    getFeatures().add(feature);
  }

  /**
   * Returns whether it has the features.
   *
   * @return whether it has the features
   */
  public boolean hasFeatures() {
    return hasRepeatingExtension(Feature.class);
  }

  /**
   * Returns the import formats.
   *
   * @return import formats
   */
  public List<ImportFormat> getImportFormats() {
    return getRepeatingExtension(ImportFormat.class);
  }

  /**
   * Adds a new import format.
   *
   * @param importFormat import format
   */
  public void addImportFormat(ImportFormat importFormat) {
    getImportFormats().add(importFormat);
  }

  /**
   * Returns whether it has the import formats.
   *
   * @return whether it has the import formats
   */
  public boolean hasImportFormats() {
    return hasRepeatingExtension(ImportFormat.class);
  }

  /**
   * Returns the max upload sizes.
   *
   * @return max upload sizes
   */
  public List<MaxUploadSize> getMaxUploadSizes() {
    return getRepeatingExtension(MaxUploadSize.class);
  }

  /**
   * Adds a new max upload size.
   *
   * @param maxUploadSize max upload size
   */
  public void addMaxUploadSize(MaxUploadSize maxUploadSize) {
    getMaxUploadSizes().add(maxUploadSize);
  }

  /**
   * Returns whether it has the max upload sizes.
   *
   * @return whether it has the max upload sizes
   */
  public boolean hasMaxUploadSizes() {
    return hasRepeatingExtension(MaxUploadSize.class);
  }

  /**
   * Returns the quota bytes total.
   *
   * @return quota bytes total
   */
  public QuotaBytesTotal getQuotaBytesTotal() {
    return getExtension(QuotaBytesTotal.class);
  }

  /**
   * Sets the quota bytes total.
   *
   * @param quotaBytesTotal quota bytes total or <code>null</code> to reset
   */
  public void setQuotaBytesTotal(QuotaBytesTotal quotaBytesTotal) {
    if (quotaBytesTotal == null) {
      removeExtension(QuotaBytesTotal.class);
    } else {
      setExtension(quotaBytesTotal);
    }
  }

  /**
   * Returns whether it has the quota bytes total.
   *
   * @return whether it has the quota bytes total
   */
  public boolean hasQuotaBytesTotal() {
    return hasExtension(QuotaBytesTotal.class);
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

  /**
   * Returns the quota bytes used in trash.
   *
   * @return quota bytes used in trash
   */
  public QuotaBytesUsedInTrash getQuotaBytesUsedInTrash() {
    return getExtension(QuotaBytesUsedInTrash.class);
  }

  /**
   * Sets the quota bytes used in trash.
   *
   * @param quotaBytesUsedInTrash quota bytes used in trash or <code>null</code>
   *     to reset
   */
  public void setQuotaBytesUsedInTrash(QuotaBytesUsedInTrash
      quotaBytesUsedInTrash) {
    if (quotaBytesUsedInTrash == null) {
      removeExtension(QuotaBytesUsedInTrash.class);
    } else {
      setExtension(quotaBytesUsedInTrash);
    }
  }

  /**
   * Returns whether it has the quota bytes used in trash.
   *
   * @return whether it has the quota bytes used in trash
   */
  public boolean hasQuotaBytesUsedInTrash() {
    return hasExtension(QuotaBytesUsedInTrash.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{MetadataEntry " + super.toString() + "}";
  }

}

