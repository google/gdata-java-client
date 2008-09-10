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


package com.google.gdata.data.health;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * Describes a profile entry.
 *
 * 
 */
@Kind.Term(ProfileEntry.KIND)
public class ProfileEntry extends BaseHealthEntry<ProfileEntry> {

  /**
   * Profile kind kind term value.
   */
  public static final String KIND = HealthNamespace.H9KINDS_PREFIX + "profile";

  /**
   * Profile kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public ProfileEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ProfileEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ProfileEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ProfileEntry.class, ContinuityOfCareRecord.class);
    extProfile.declare(ProfileEntry.class, ProfileMetaData.class);
  }

  /**
   * Returns the continuity of care record.
   *
   * @return continuity of care record
   */
  public ContinuityOfCareRecord getContinuityOfCareRecord() {
    return getExtension(ContinuityOfCareRecord.class);
  }

  /**
   * Sets the continuity of care record.
   *
   * @param continuityOfCareRecord continuity of care record or
   *     <code>null</code> to reset
   */
  public void setContinuityOfCareRecord(ContinuityOfCareRecord
      continuityOfCareRecord) {
    if (continuityOfCareRecord == null) {
      removeExtension(ContinuityOfCareRecord.class);
    } else {
      setExtension(continuityOfCareRecord);
    }
  }

  /**
   * Returns whether it has the continuity of care record.
   *
   * @return whether it has the continuity of care record
   */
  public boolean hasContinuityOfCareRecord() {
    return hasExtension(ContinuityOfCareRecord.class);
  }

  /**
   * Returns the profile meta data.
   *
   * @return profile meta data
   */
  public ProfileMetaData getProfileMetaData() {
    return getExtension(ProfileMetaData.class);
  }

  /**
   * Sets the profile meta data.
   *
   * @param profileMetaData profile meta data or <code>null</code> to reset
   */
  public void setProfileMetaData(ProfileMetaData profileMetaData) {
    if (profileMetaData == null) {
      removeExtension(ProfileMetaData.class);
    } else {
      setExtension(profileMetaData);
    }
  }

  /**
   * Returns whether it has the profile meta data.
   *
   * @return whether it has the profile meta data
   */
  public boolean hasProfileMetaData() {
    return hasExtension(ProfileMetaData.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ProfileEntry " + super.toString() + "}";
  }

}
