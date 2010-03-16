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


package com.google.gdata.data.maps;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Content;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.OtherContent;
import com.google.gdata.data.batch.BatchId;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.data.batch.BatchOperation;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.extensions.CustomProperty;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.data.extensions.ResourceId;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.XmlBlob;

import java.io.IOException;
import java.util.List;

/**
 * Describes a feature entry.
 *
 * 
 */
@Kind.Term(FeatureEntry.KIND)
public class FeatureEntry extends BaseEntry<FeatureEntry> {

  /**
   * Feature feature category kind term value.
   */
  public static final String KIND = MapsNamespace.MAPS_PREFIX + "feature";

  /**
   * Feature feature category kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public FeatureEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public FeatureEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(FeatureEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(BatchId.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "id", false, false, false));
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(BatchInterrupted.class,
        new XmlNamespace("batch", "http://schemas.google.com/gdata/batch"),
        "interrupted", false, false, false));
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(BatchOperation.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "operation", false, false,
        false));
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(BatchStatus.class, new XmlNamespace("batch",
        "http://schemas.google.com/gdata/batch"), "status", false, false,
        false));
    extProfile.declare(FeatureEntry.class,
        CustomProperty.getDefaultDescription(false, true));
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(Deleted.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(FeatureEntry.class,
        new ExtensionDescription(PostalAddress.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "postalAddress", false, false,
        false));
    extProfile.declare(FeatureEntry.class, ResourceId.class);
    extProfile.declare(FeatureEntry.class, StructuredPostalAddress.class);
    new StructuredPostalAddress().declareExtensions(extProfile);
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
   * Returns the custom properties.
   *
   * @return custom properties
   */
  public List<CustomProperty> getCustomProperties() {
    return getRepeatingExtension(CustomProperty.class);
  }

  /**
   * Adds a new custom property.
   *
   * @param customProperty custom property
   */
  public void addCustomProperty(CustomProperty customProperty) {
    getCustomProperties().add(customProperty);
  }

  /**
   * Returns whether it has the custom properties.
   *
   * @return whether it has the custom properties
   */
  public boolean hasCustomProperties() {
    return hasRepeatingExtension(CustomProperty.class);
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
   * Returns the postal address.
   *
   * @return postal address
   */
  public PostalAddress getPostalAddress() {
    return getExtension(PostalAddress.class);
  }

  /**
   * Sets the postal address.
   *
   * @param postalAddress postal address or <code>null</code> to reset
   */
  public void setPostalAddress(PostalAddress postalAddress) {
    if (postalAddress == null) {
      removeExtension(PostalAddress.class);
    } else {
      setExtension(postalAddress);
    }
  }

  /**
   * Returns whether it has the postal address.
   *
   * @return whether it has the postal address
   */
  public boolean hasPostalAddress() {
    return hasExtension(PostalAddress.class);
  }

  /**
   * Returns the resource id.
   *
   * @return resource id
   */
  public ResourceId getResourceId() {
    return getExtension(ResourceId.class);
  }

  /**
   * Sets the resource id.
   *
   * @param resourceId resource id or <code>null</code> to reset
   */
  public void setResourceId(ResourceId resourceId) {
    if (resourceId == null) {
      removeExtension(ResourceId.class);
    } else {
      setExtension(resourceId);
    }
  }

  /**
   * Returns whether it has the resource id.
   *
   * @return whether it has the resource id
   */
  public boolean hasResourceId() {
    return hasExtension(ResourceId.class);
  }

  /**
   * Returns the structured postal address.
   *
   * @return structured postal address
   */
  public StructuredPostalAddress getStructuredPostalAddress() {
    return getExtension(StructuredPostalAddress.class);
  }

  /**
   * Sets the structured postal address.
   *
   * @param structuredPostalAddress structured postal address or
   *     <code>null</code> to reset
   */
  public void setStructuredPostalAddress(StructuredPostalAddress
      structuredPostalAddress) {
    if (structuredPostalAddress == null) {
      removeExtension(StructuredPostalAddress.class);
    } else {
      setExtension(structuredPostalAddress);
    }
  }

  /**
   * Returns whether it has the structured postal address.
   *
   * @return whether it has the structured postal address
   */
  public boolean hasStructuredPostalAddress() {
    return hasExtension(StructuredPostalAddress.class);
  }

  /**
   * Returns the link that provides the URI of the full feed (without any query
   * parameters).
   *
   * @return Link that provides the URI of the full feed (without any query
   *     parameters) or {@code null} for none.
   */
  public Link getAtomFeedLink() {
    return getLink(Link.Rel.FEED, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{FeatureEntry " + super.toString() + "}";
  }



  /** True if this feature has an interesting read-only view, for example as
   * driving directions. */
  private boolean hasView;
  private boolean isKmlDefault = true;
  public static final ContentType KML_CONTENT =
      new ContentType("application/vnd.google-earth.kml+xml");

  public void setKmlDefault(boolean isDefault) {
    isKmlDefault = isDefault;
  }

  public boolean hasView() {
    return hasView;
  }

  public void setHasView(boolean hasView) {
    this.hasView = hasView;
  }

  /**
   * Gets the URL of the view projection of this feature.
   *
   * @return URL of the view projection of this feature or {@code null}
   */
  public Link getViewLink() {
    return getLink(MapsNamespace.MAPS_PREFIX + "view", null);
  }

  /**
   * Override generateAtom() method to change the default namespace to kml.
   */
  @Override
  public void generateAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    if (isKmlDefault) {
      w.setDefaultNamespace(DataConstants.KML_NAMESPACE);
    }
    super.generateAtom(w, extProfile);
  }

  /**
   * @return the content of the feature in KML format as an XmlBlob.
   */
  public XmlBlob getKml() {
    Content content = getContent();
    if (null == content || !(content instanceof OtherContent)) {
      return null;
    }
    return ((OtherContent) getContent()).getXml();
  }

  /**
   * Sets the KML content of the feature as an XmlBlob.  Unless
   * overridden by setKmlDefault, the default namespace of the entry
   * is kml, so the KML placemarks don't need any namespace prefix.
   *
   * @param kml A string representing a KML placemark.
   */
  public void setKml(XmlBlob kml) {
    OtherContent content = new OtherContent();
    content.setXml(kml);
    content.setMimeType(KML_CONTENT);
    setContent(content);
  }
}
