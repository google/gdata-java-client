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


package com.google.gdata.data.sidewiki;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.extensions.ResourceId;

/**
 * Describes a Sidewiki entry in the feed of Sidewiki entries.
 *
 * 
 */
public class SidewikiEntry extends BaseEntry<SidewikiEntry> {

  /**
   * Default mutable constructor.
   */
  public SidewikiEntry() {
    super();
    setKind("sidewiki#sidewikiEntry");
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public SidewikiEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(SidewikiEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(SidewikiEntry.class,
        SidewikiAuthor.getDefaultDescription(false, true));
    new SidewikiAuthor().declareExtensions(extProfile);
    extProfile.declare(SidewikiEntry.class, Source.class);
    extProfile.declare(SidewikiEntry.class,
        new ExtensionDescription(Rating.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "rating", false, false, false));
    extProfile.declare(SidewikiEntry.class,
        ResourceId.getDefaultDescription(true, false));
    extProfile.declare(SidewikiEntry.class, Usefulness.class);
  }

  /**
   * Returns the Sidewiki entry source.
   *
   * @return Sidewiki entry source
   */
  public Source getEntrySource() {
    return getExtension(Source.class);
  }

  /**
   * Sets the Sidewiki entry source.
   *
   * @param entrySource Sidewiki entry source or <code>null</code> to reset
   */
  public void setEntrySource(Source entrySource) {
    if (entrySource == null) {
      removeExtension(Source.class);
    } else {
      setExtension(entrySource);
    }
  }

  /**
   * Returns whether it has the Sidewiki entry source.
   *
   * @return whether it has the Sidewiki entry source
   */
  public boolean hasEntrySource() {
    return hasExtension(Source.class);
  }

  /**
   * Returns the Entry ratings given by all users and by the user issued the
   * request.
   *
   * @return Entry ratings given by all users and by the user issued the request
   */
  public Rating getRating() {
    return getExtension(Rating.class);
  }

  /**
   * Sets the Entry ratings given by all users and by the user issued the
   * request.
   *
   * @param rating Entry ratings given by all users and by the user issued the
   *     request or <code>null</code> to reset
   */
  public void setRating(Rating rating) {
    if (rating == null) {
      removeExtension(Rating.class);
    } else {
      setExtension(rating);
    }
  }

  /**
   * Returns whether it has the Entry ratings given by all users and by the user
   * issued the request.
   *
   * @return whether it has the Entry ratings given by all users and by the user
   *     issued the request
   */
  public boolean hasRating() {
    return hasExtension(Rating.class);
  }

  /**
   * Returns the Sidewiki author id.
   *
   * @return Sidewiki author id
   */
  public ResourceId getResourceId() {
    return getExtension(ResourceId.class);
  }

  /**
   * Sets the Sidewiki author id.
   *
   * @param resourceId Sidewiki author id or <code>null</code> to reset
   */
  public void setResourceId(ResourceId resourceId) {
    if (resourceId == null) {
      removeExtension(ResourceId.class);
    } else {
      setExtension(resourceId);
    }
  }

  /**
   * Returns whether it has the Sidewiki author id.
   *
   * @return whether it has the Sidewiki author id
   */
  public boolean hasResourceId() {
    return hasExtension(ResourceId.class);
  }

  /**
   * Returns the usefulness of entry.
   *
   * @return usefulness of entry
   */
  public Usefulness getUsefulness() {
    return getExtension(Usefulness.class);
  }

  /**
   * Sets the usefulness of entry.
   *
   * @param usefulness usefulness of entry or <code>null</code> to reset
   */
  public void setUsefulness(Usefulness usefulness) {
    if (usefulness == null) {
      removeExtension(Usefulness.class);
    } else {
      setExtension(usefulness);
    }
  }

  /**
   * Returns whether it has the usefulness of entry.
   *
   * @return whether it has the usefulness of entry
   */
  public boolean hasUsefulness() {
    return hasExtension(Usefulness.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{SidewikiEntry " + super.toString() + "}";
  }

}

