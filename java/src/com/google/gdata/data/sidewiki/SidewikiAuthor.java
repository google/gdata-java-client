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
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Person;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.extensions.ResourceId;
import com.google.gdata.util.Namespaces;

/**
 * Sidewiki author.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = "atom",
    nsUri = Namespaces.atom,
    localName = SidewikiAuthor.XML_NAME)
public class SidewikiAuthor extends Person {

  /** XML element name */
  static final String XML_NAME = "author";

  /**
   * Default mutable constructor.
   */
  public SidewikiAuthor() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(SidewikiAuthor.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(SidewikiAuthor.class, SidewikiUserDescription.class);
    extProfile.declare(SidewikiAuthor.class, EntriesNumber.class);
    extProfile.declare(SidewikiAuthor.class,
        new ExtensionDescription(Rating.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "rating", false, false, false));
    extProfile.declare(SidewikiAuthor.class,
        ResourceId.getDefaultDescription(true, false));
    extProfile.declare(SidewikiAuthor.class, SidewikiThumbnail.class);
  }

  /**
   * Returns the Sidewiki author description.
   *
   * @return Sidewiki author description
   */
  public SidewikiUserDescription getDescription() {
    return getExtension(SidewikiUserDescription.class);
  }

  /**
   * Sets the Sidewiki author description.
   *
   * @param description Sidewiki author description or <code>null</code> to
   *     reset
   */
  public void setDescription(SidewikiUserDescription description) {
    if (description == null) {
      removeExtension(SidewikiUserDescription.class);
    } else {
      setExtension(description);
    }
  }

  /**
   * Returns whether it has the Sidewiki author description.
   *
   * @return whether it has the Sidewiki author description
   */
  public boolean hasDescription() {
    return hasExtension(SidewikiUserDescription.class);
  }

  /**
   * Returns the entries number.
   *
   * @return entries number
   */
  public EntriesNumber getNumEntries() {
    return getExtension(EntriesNumber.class);
  }

  /**
   * Sets the entries number.
   *
   * @param numEntries entries number or <code>null</code> to reset
   */
  public void setNumEntries(EntriesNumber numEntries) {
    if (numEntries == null) {
      removeExtension(EntriesNumber.class);
    } else {
      setExtension(numEntries);
    }
  }

  /**
   * Returns whether it has the entries number.
   *
   * @return whether it has the entries number
   */
  public boolean hasNumEntries() {
    return hasExtension(EntriesNumber.class);
  }

  /**
   * Returns the user rating given by the user issued the request.
   *
   * @return user rating given by the user issued the request
   */
  public Rating getRating() {
    return getExtension(Rating.class);
  }

  /**
   * Sets the user rating given by the user issued the request.
   *
   * @param rating user rating given by the user issued the request or
   *     <code>null</code> to reset
   */
  public void setRating(Rating rating) {
    if (rating == null) {
      removeExtension(Rating.class);
    } else {
      setExtension(rating);
    }
  }

  /**
   * Returns whether it has the user rating given by the user issued the
   * request.
   *
   * @return whether it has the user rating given by the user issued the request
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
   * Returns the user portrait thumbnail url.
   *
   * @return user portrait thumbnail url
   */
  public SidewikiThumbnail getThumbnail() {
    return getExtension(SidewikiThumbnail.class);
  }

  /**
   * Sets the user portrait thumbnail url.
   *
   * @param thumbnail user portrait thumbnail url or <code>null</code> to reset
   */
  public void setThumbnail(SidewikiThumbnail thumbnail) {
    if (thumbnail == null) {
      removeExtension(SidewikiThumbnail.class);
    } else {
      setExtension(thumbnail);
    }
  }

  /**
   * Returns whether it has the user portrait thumbnail url.
   *
   * @return whether it has the user portrait thumbnail url
   */
  public boolean hasThumbnail() {
    return hasExtension(SidewikiThumbnail.class);
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
        ExtensionDescription.getDefaultDescription(SidewikiAuthor.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{SidewikiAuthor " + super.toString() + "}";
  }

}

