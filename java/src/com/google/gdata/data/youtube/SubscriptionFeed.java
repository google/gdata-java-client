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


package com.google.gdata.data.youtube;

import com.google.gdata.data.Kind;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.batch.BatchUtils;

/**
 * A feed that contains playlist link entries.
 *
 * This feed does not represent a playlist, but a collection of links to
 * playlist feeds. See {@link com.google.gdata.data.youtube.PlaylistFeed}
 * and {@link com.google.gdata.data.youtube.PlaylistEntry} for
 * the playlist feed.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_SUBSCRIPTION)
public class SubscriptionFeed
    extends BaseFeed<SubscriptionFeed, SubscriptionEntry> {

  public SubscriptionFeed() {
    super(SubscriptionEntry.class);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_SUBSCRIPTION);
  }

  public SubscriptionFeed(BaseFeed<?, ?> base) {
    super(SubscriptionEntry.class, base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_SUBSCRIPTION);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }
}
