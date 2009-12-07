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


package com.google.gdata.data.gtt;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.acl.AclNamespace;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.extensions.LastModifiedBy;
import com.google.gdata.data.media.MediaEntry;

/**
 * Describes a document entry.
 *
 * 
 */
public class DocumentEntry extends MediaEntry<DocumentEntry> {

  /**
   * Default mutable constructor.
   */
  public DocumentEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public DocumentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(DocumentEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(DocumentEntry.class,
        new ExtensionDescription(Deleted.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(DocumentEntry.class, DocumentSource.class);
    extProfile.declare(DocumentEntry.class, GlossariesElement.class);
    new GlossariesElement().declareExtensions(extProfile);
    extProfile.declare(DocumentEntry.class, LastModifiedBy.class);
    extProfile.declare(DocumentEntry.class, NumberOfSourceWords.class);
    extProfile.declare(DocumentEntry.class, PercentComplete.class);
    extProfile.declare(DocumentEntry.class, SourceLanguage.class);
    extProfile.declare(DocumentEntry.class, TargetLanguage.class);
    extProfile.declare(DocumentEntry.class, TmsElement.class);
    new TmsElement().declareExtensions(extProfile);
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
   * Returns the document source.
   *
   * @return document source
   */
  public DocumentSource getDocumentSource() {
    return getExtension(DocumentSource.class);
  }

  /**
   * Sets the document source.
   *
   * @param documentSource document source or <code>null</code> to reset
   */
  public void setDocumentSource(DocumentSource documentSource) {
    if (documentSource == null) {
      removeExtension(DocumentSource.class);
    } else {
      setExtension(documentSource);
    }
  }

  /**
   * Returns whether it has the document source.
   *
   * @return whether it has the document source
   */
  public boolean hasDocumentSource() {
    return hasExtension(DocumentSource.class);
  }

  /**
   * Returns the glossaries element.
   *
   * @return glossaries element
   */
  public GlossariesElement getGlossary() {
    return getExtension(GlossariesElement.class);
  }

  /**
   * Sets the glossaries element.
   *
   * @param glossary glossaries element or <code>null</code> to reset
   */
  public void setGlossary(GlossariesElement glossary) {
    if (glossary == null) {
      removeExtension(GlossariesElement.class);
    } else {
      setExtension(glossary);
    }
  }

  /**
   * Returns whether it has the glossaries element.
   *
   * @return whether it has the glossaries element
   */
  public boolean hasGlossary() {
    return hasExtension(GlossariesElement.class);
  }

  /**
   * Returns the last modified by.
   *
   * @return last modified by
   */
  public LastModifiedBy getLastModifiedBy() {
    return getExtension(LastModifiedBy.class);
  }

  /**
   * Sets the last modified by.
   *
   * @param lastModifiedBy last modified by or <code>null</code> to reset
   */
  public void setLastModifiedBy(LastModifiedBy lastModifiedBy) {
    if (lastModifiedBy == null) {
      removeExtension(LastModifiedBy.class);
    } else {
      setExtension(lastModifiedBy);
    }
  }

  /**
   * Returns whether it has the last modified by.
   *
   * @return whether it has the last modified by
   */
  public boolean hasLastModifiedBy() {
    return hasExtension(LastModifiedBy.class);
  }

  /**
   * Returns the number of source words.
   *
   * @return number of source words
   */
  public NumberOfSourceWords getNumberOfSourceWords() {
    return getExtension(NumberOfSourceWords.class);
  }

  /**
   * Sets the number of source words.
   *
   * @param numberOfSourceWords number of source words or <code>null</code> to
   *     reset
   */
  public void setNumberOfSourceWords(NumberOfSourceWords numberOfSourceWords) {
    if (numberOfSourceWords == null) {
      removeExtension(NumberOfSourceWords.class);
    } else {
      setExtension(numberOfSourceWords);
    }
  }

  /**
   * Returns whether it has the number of source words.
   *
   * @return whether it has the number of source words
   */
  public boolean hasNumberOfSourceWords() {
    return hasExtension(NumberOfSourceWords.class);
  }

  /**
   * Returns the percent complete.
   *
   * @return percent complete
   */
  public PercentComplete getPercentComplete() {
    return getExtension(PercentComplete.class);
  }

  /**
   * Sets the percent complete.
   *
   * @param percentComplete percent complete or <code>null</code> to reset
   */
  public void setPercentComplete(PercentComplete percentComplete) {
    if (percentComplete == null) {
      removeExtension(PercentComplete.class);
    } else {
      setExtension(percentComplete);
    }
  }

  /**
   * Returns whether it has the percent complete.
   *
   * @return whether it has the percent complete
   */
  public boolean hasPercentComplete() {
    return hasExtension(PercentComplete.class);
  }

  /**
   * Returns the source language.
   *
   * @return source language
   */
  public SourceLanguage getSourceLanguage() {
    return getExtension(SourceLanguage.class);
  }

  /**
   * Sets the source language.
   *
   * @param sourceLanguage source language or <code>null</code> to reset
   */
  public void setSourceLanguage(SourceLanguage sourceLanguage) {
    if (sourceLanguage == null) {
      removeExtension(SourceLanguage.class);
    } else {
      setExtension(sourceLanguage);
    }
  }

  /**
   * Returns whether it has the source language.
   *
   * @return whether it has the source language
   */
  public boolean hasSourceLanguage() {
    return hasExtension(SourceLanguage.class);
  }

  /**
   * Returns the target language.
   *
   * @return target language
   */
  public TargetLanguage getTargetLanguage() {
    return getExtension(TargetLanguage.class);
  }

  /**
   * Sets the target language.
   *
   * @param targetLanguage target language or <code>null</code> to reset
   */
  public void setTargetLanguage(TargetLanguage targetLanguage) {
    if (targetLanguage == null) {
      removeExtension(TargetLanguage.class);
    } else {
      setExtension(targetLanguage);
    }
  }

  /**
   * Returns whether it has the target language.
   *
   * @return whether it has the target language
   */
  public boolean hasTargetLanguage() {
    return hasExtension(TargetLanguage.class);
  }

  /**
   * Returns the tms element.
   *
   * @return tms element
   */
  public TmsElement getTranslationMemory() {
    return getExtension(TmsElement.class);
  }

  /**
   * Sets the tms element.
   *
   * @param translationMemory tms element or <code>null</code> to reset
   */
  public void setTranslationMemory(TmsElement translationMemory) {
    if (translationMemory == null) {
      removeExtension(TmsElement.class);
    } else {
      setExtension(translationMemory);
    }
  }

  /**
   * Returns whether it has the tms element.
   *
   * @return whether it has the tms element
   */
  public boolean hasTranslationMemory() {
    return hasExtension(TmsElement.class);
  }

  /**
   * Returns the link that provides the URI of the feed for the access control
   * list for the entry.
   *
   * @return Link that provides the URI of the feed for the access control list
   *     for the entry or {@code null} for none.
   */
  public Link getAccessControlListLink() {
    return getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{DocumentEntry " + super.toString() + "}";
  }

}

