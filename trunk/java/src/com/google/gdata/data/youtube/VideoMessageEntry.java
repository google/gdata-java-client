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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * A YouTube shared video entry.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_VIDEO_MESSAGE)
public class VideoMessageEntry extends VideoEntry {

  /** Creates an empty inbox entry. */
  public VideoMessageEntry() {
    super();
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO_MESSAGE);
  }

  /** Creates a copy of another entry. */
  public VideoMessageEntry(BaseEntry<?> original) {
    super(original);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO_MESSAGE);
  }

  /**
   * Declares extensions that can appear in this entry.
   *
   * The namespaces should be declared by the feeds themselves
   * if they want them to appear in the top element of the feed.
   *
   * @param extProfile profile to register extensions to
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    // yt:description only in version 1
    extProfile.declare(VideoMessageEntry.class, YtDescription.class);
    extProfile.declareArbitraryXmlExtension(PlaylistEntry.class);
  }

  /** 
   * Gets the message sent together with the video. 
   *
   * @deprecated Valid only in version 1. Replaced version 2.0 with
   *             atom:summary.
   */
  @Deprecated
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /** 
   * Sets the message sent together with the video. 
   *
   * @deprecated Valid only in version 1. Replaced version 2.0 with
   *             atom:summary.
   */
  @Deprecated
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(YtDescription.class);
    } else {
      setExtension(new YtDescription(description));
    }
  }
}
