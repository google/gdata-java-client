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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * Describes a feed for retrieving a list of changed documents.
 *
 * 
 */
@Kind.Term(ChangelogEntry.KIND)
public class ChangelogFeed extends BaseFeed<ChangelogFeed, ChangelogEntry> {

  /**
   * Default mutable constructor.
   */
  public ChangelogFeed() {
    super(ChangelogEntry.class);
    getCategories().add(ChangelogEntry.CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public ChangelogFeed(BaseFeed<?, ?> sourceFeed) {
    super(ChangelogEntry.class, sourceFeed);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ChangelogFeed.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ChangelogFeed.class,
        LargestChangestamp.getDefaultDescription(true, false));
  }

  /**
   * Returns the largest changestamp.
   *
   * @return largest changestamp
   */
  public LargestChangestamp getLargestChangestamp() {
    return getExtension(LargestChangestamp.class);
  }

  /**
   * Sets the largest changestamp.
   *
   * @param largestChangestamp largest changestamp or <code>null</code> to reset
   */
  public void setLargestChangestamp(LargestChangestamp largestChangestamp) {
    if (largestChangestamp == null) {
      removeExtension(LargestChangestamp.class);
    } else {
      setExtension(largestChangestamp);
    }
  }

  /**
   * Returns whether it has the largest changestamp.
   *
   * @return whether it has the largest changestamp
   */
  public boolean hasLargestChangestamp() {
    return hasExtension(LargestChangestamp.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ChangelogFeed " + super.toString() + "}";
  }

}

