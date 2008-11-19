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
 * yt:playlistid tag found in the subscription entry.
 * 
 * 
 *
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "playlistId")
public class YtPlaylistId  extends AbstractExtension {
  private String playlistId;
  /** 
   * Creates an empty tag. 
   */
  public YtPlaylistId() {
  }

  /**
   * Creates a tag and initializes its content.
   *
   * @param playlistId id of the playlist
   */
  public YtPlaylistId(String playlistId) {
    this.playlistId = playlistId;
  }
  
  public String getPlaylistId() {
    return playlistId;
  }
  
  public void setPlaylistId(String playlistId) {
    this.playlistId = playlistId;
  }
  
  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    generator.setContent(playlistId);
  }
  
  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    playlistId = helper.consumeContent(true);
  }
}
