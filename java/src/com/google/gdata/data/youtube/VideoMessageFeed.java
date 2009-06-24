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
import com.google.gdata.data.batch.BatchUtils;

/**
 * Shared video feed (inbox).
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_VIDEO_MESSAGE)
public class VideoMessageFeed extends BaseFeed<VideoMessageFeed, VideoMessageEntry> {

  public VideoMessageFeed() {
    super(VideoMessageEntry.class);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO_MESSAGE);
  }

  public VideoMessageFeed(BaseFeed<?,?> base) {
    super(VideoMessageEntry.class, base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO_MESSAGE);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }
}
