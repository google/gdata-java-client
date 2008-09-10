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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * A YouTube playlist feed.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_PLAYLIST)
public class PlaylistFeed extends BaseFeed<PlaylistFeed, PlaylistEntry> {

  /**
   * Nonstandard categories that might be found in this feed.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.TAG_SCHEME
  };

  public PlaylistFeed() {
    super(PlaylistEntry.class);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_PLAYLIST);
  }

  public PlaylistFeed(BaseFeed<?, ?> base) {
    super(PlaylistEntry.class, base);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_PLAYLIST);
  }
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declareFeedExtension(YtPrivate.class);
    extProfile.declareFeedExtension(YouTubeMediaGroup.class);
    new YouTubeMediaGroup().declareExtensions(extProfile);
  }

  /** Gets the private field of the feed. */
  public boolean isPrivate() {
    return getExtension(YtPrivate.class) != null;
  }

  /** Sets the private field of the feed. */
  public void setPrivate(boolean value) {
    if (value) {
      setExtension(new YtPrivate());
    } else {
      removeExtension(YtPrivate.class);
    }
  }

  public YouTubeMediaGroup getMediaGroup() {
    return getExtension(YouTubeMediaGroup.class);
  }

  public YouTubeMediaGroup getOrCreateMediaGroup() {
    YouTubeMediaGroup group = getMediaGroup();
    if (group == null) {
      group = new YouTubeMediaGroup();
      setExtension(group);
    }
    return group;
  }
}
