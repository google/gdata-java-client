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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * VideoId class.
 *
 * This sets the yt:videoid in the MediaGroup.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "videoid"
)
public class YtVideoId extends AbstractExtension {
  private String videoId;

  /** Creates an empty video ID tag. */
  public YtVideoId() {

  }

  /** Creates and initializes a video ID tag. */
  public YtVideoId(String videoId) {
    this.videoId = videoId;
  }

  /** Gets video ID. */
  public String getVideoId() {
    return videoId;
  }

  /** Sets video ID. */
  public void setVideoId(String id) {
    this.videoId = id;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    generator.setContent(videoId);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    videoId = helper.consumeContent(true);
  }
}
