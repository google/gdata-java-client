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
import com.google.gdata.data.Link;
import com.google.gdata.data.media.MediaFeed;
import com.google.gdata.data.batch.BatchUtils;

/**
 * Video feed returned by the feed providers.
 * 
 * 
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_VIDEO)
public class VideoFeed extends MediaFeed<VideoFeed, VideoEntry> {

  public VideoFeed() {
    super(VideoEntry.class);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO);
  }

  public VideoFeed(BaseFeed<?, ?> base) {
    super(VideoEntry.class, base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }

  /**
   * Gets a link to the "Get Upload Token" action.
   *
   * @return a link with rel {@link YouTubeNamespace#GET_UPLOAD_TOKEN_REL} 
   *   or {@code null}.
   */
  public Link getGetUploadTokenActionLink() {
    for (Link link : getLinks()) {
      if (YouTubeNamespace.GET_UPLOAD_TOKEN_REL.equals(link.getRel())) {
        return link;
      }
    }
    return null;
  }
}
