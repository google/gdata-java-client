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


package com.google.gdata.data.sites;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

/**
 * A read-only feed from which a particular version of a document can be
 * fetched.
 *
 * 
 */
public class RevisionFeed extends BaseFeed<RevisionFeed, BaseContentEntry> {

  /**
   * Default mutable constructor.
   */
  public RevisionFeed() {
    super(ContentEntry.class);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public RevisionFeed(BaseFeed<?, ?> sourceFeed) {
    super(ContentEntry.class, sourceFeed);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(RevisionFeed.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(RevisionFeed.class, SitesLink.getDefaultDescription(true,
        true));
  }

  @Override
  public String toString() {
    return "{RevisionFeed " + super.toString() + "}";
  }

}

