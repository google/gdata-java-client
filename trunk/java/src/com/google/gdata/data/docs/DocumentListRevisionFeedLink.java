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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.docs.RevisionFeed;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.util.Namespaces;

/**
 * Represents a FeedLink referring to a RevisionFeed.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = "feedLink",
    isRepeatable = true)
public class DocumentListRevisionFeedLink extends FeedLink<RevisionFeed> {
  public DocumentListRevisionFeedLink() {
    super(RevisionFeed.class);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    ExtensionProfile ep = new ExtensionProfile();
    new RevisionFeed().declareExtensions(ep);
    extProfile.declareFeedLinkProfile(ep);
  }
}
