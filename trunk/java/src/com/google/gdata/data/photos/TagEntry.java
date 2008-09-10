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


package com.google.gdata.data.photos;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.photos.impl.TagDataImpl;
import com.google.gdata.util.ParseException;

/**
 * A simple entry for a tag object.  This can be used to create new tags
 * on elements in the system.
 *
 * 
 */
@Kind.Term(TagData.TAG_KIND)
public class TagEntry extends GphotoEntry<TagEntry> implements TagData {

  private final TagData delegate;

  /**
   * Constructs a new empty tag entry.
   */
  public TagEntry() {
    super();
    getCategories().add(TagData.TAG_CATEGORY);
    this.delegate = new TagDataImpl(this);
  }

  /**
   * Construct a new tag entry doing a shallow copy of the data in the
   * passed in source entry.
   */
  public TagEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(TagData.TAG_CATEGORY);
    this.delegate = new TagDataImpl(this);
  }

  /*
   * Add the declaration of the weight field.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  // Delegating methods.

  public Integer getWeight() throws ParseException {
    return delegate.getWeight();
  }

  public void setWeight(Integer weight) {
    delegate.setWeight(weight);
  }
}
