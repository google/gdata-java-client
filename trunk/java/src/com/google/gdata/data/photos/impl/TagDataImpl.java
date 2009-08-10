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


package com.google.gdata.data.photos.impl;

import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.photos.GphotoWeight;
import com.google.gdata.data.photos.TagData;

/**
 * Implementation class for tag data objects.  This class takes an
 * {@link ExtensionPoint} and uses it to provide all of the methods that
 * {@link TagData} specifies.  These methods are handled by using
 * extension classes to retrieve or set extensions of the appropriate type.
 *
 * 
 */
public class TagDataImpl extends GphotoDataImpl implements TagData {

  /**
   * Construct a new implementation of TagGphotoData with the given
   * extension point as the backing storage for data.
   */
  public TagDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
  }

  /*
   * Declare the extensions that tag objects use.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    declare(extProfile, GphotoWeight.getDefaultDescription(false, false));
  }

  /*
   * Get the weight of the tag.  May be null if the weight was not set or
   * is the default.
   */
  public Integer getWeight() {
    GphotoWeight ext = getExtension(GphotoWeight.class);
    return ext == null ? null : ext.getValue();
  }

  /*
   * Set the weight of the tag, can be used to clear the value by setting with
   * a null.
   */
  public void setWeight(Integer weight) {
    if (weight != null) {
      setExtension(new GphotoWeight(weight));
    } else {
      removeExtension(GphotoWeight.class);
    }
  }
}
