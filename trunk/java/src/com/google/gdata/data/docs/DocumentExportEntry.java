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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;

import java.util.List;

/**
 * Describes an entry representing a request to bulk export documents from a
 * user.
 *
 * 
 */
public class DocumentExportEntry extends BaseEntry<DocumentExportEntry> {

  /**
   * Default mutable constructor.
   */
  public DocumentExportEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public DocumentExportEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(DocumentExportEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(DocumentExportEntry.class,
        ExportDocId.getDefaultDescription(false, true));
    extProfile.declare(DocumentExportEntry.class,
        DocumentExportRequestor.class);
    extProfile.declare(DocumentExportEntry.class, ObjectNumber.class);
    extProfile.declare(DocumentExportEntry.class,
        QueryParameter.getDefaultDescription(false, true));
    extProfile.declare(DocumentExportEntry.class, ExportStatus.class);
  }

  /**
   * Returns the export doc ids.
   *
   * @return export doc ids
   */
  public List<ExportDocId> getExportDocIds() {
    return getRepeatingExtension(ExportDocId.class);
  }

  /**
   * Adds a new export doc id.
   *
   * @param exportDocId export doc id
   */
  public void addExportDocId(ExportDocId exportDocId) {
    getExportDocIds().add(exportDocId);
  }

  /**
   * Returns whether it has the export doc ids.
   *
   * @return whether it has the export doc ids
   */
  public boolean hasExportDocIds() {
    return hasRepeatingExtension(ExportDocId.class);
  }

  /**
   * Returns the document export requestor.
   *
   * @return document export requestor
   */
  public DocumentExportRequestor getExportRequestor() {
    return getExtension(DocumentExportRequestor.class);
  }

  /**
   * Sets the document export requestor.
   *
   * @param exportRequestor document export requestor or <code>null</code> to
   *     reset
   */
  public void setExportRequestor(DocumentExportRequestor exportRequestor) {
    if (exportRequestor == null) {
      removeExtension(DocumentExportRequestor.class);
    } else {
      setExtension(exportRequestor);
    }
  }

  /**
   * Returns whether it has the document export requestor.
   *
   * @return whether it has the document export requestor
   */
  public boolean hasExportRequestor() {
    return hasExtension(DocumentExportRequestor.class);
  }

  /**
   * Returns the object number.
   *
   * @return object number
   */
  public ObjectNumber getObjectNumber() {
    return getExtension(ObjectNumber.class);
  }

  /**
   * Sets the object number.
   *
   * @param objectNumber object number or <code>null</code> to reset
   */
  public void setObjectNumber(ObjectNumber objectNumber) {
    if (objectNumber == null) {
      removeExtension(ObjectNumber.class);
    } else {
      setExtension(objectNumber);
    }
  }

  /**
   * Returns whether it has the object number.
   *
   * @return whether it has the object number
   */
  public boolean hasObjectNumber() {
    return hasExtension(ObjectNumber.class);
  }

  /**
   * Returns the query parameters.
   *
   * @return query parameters
   */
  public List<QueryParameter> getQueries() {
    return getRepeatingExtension(QueryParameter.class);
  }

  /**
   * Adds a new query parameter.
   *
   * @param query query parameter
   */
  public void addQuery(QueryParameter query) {
    getQueries().add(query);
  }

  /**
   * Returns whether it has the query parameters.
   *
   * @return whether it has the query parameters
   */
  public boolean hasQueries() {
    return hasRepeatingExtension(QueryParameter.class);
  }

  /**
   * Returns the export status.
   *
   * @return export status
   */
  public ExportStatus getStatus() {
    return getExtension(ExportStatus.class);
  }

  /**
   * Sets the export status.
   *
   * @param status export status or <code>null</code> to reset
   */
  public void setStatus(ExportStatus status) {
    if (status == null) {
      removeExtension(ExportStatus.class);
    } else {
      setExtension(status);
    }
  }

  /**
   * Returns whether it has the export status.
   *
   * @return whether it has the export status
   */
  public boolean hasStatus() {
    return hasExtension(ExportStatus.class);
  }

  /**
   * Returns the link for the zip file contains all exported documents.
   *
   * @return Link for the zip file contains all exported documents or {@code
   *     null} for none.
   */
  public Link getDocumentExportLink() {
    return getLink(DocumentListLink.Rel.EXPORT,
        DocumentListLink.Type.APPLICATION_ZIP);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{DocumentExportEntry " + super.toString() + "}";
  }

}

