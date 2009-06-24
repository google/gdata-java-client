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
 * Entries that appear on the user playlist feed.
 *
 * This entry does not contain a playlist, but a link to a playlist. See
 * {@link PlaylistFeed} and {@link PlaylistEntry} for the playlist feed
 * objects.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_PLAYLIST_LINK)
public class PlaylistLinkEntry extends FeedLinkEntry<PlaylistLinkEntry>{

  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.TAG_SCHEME
  };

  public PlaylistLinkEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_PLAYLIST_LINK);
  }

  public PlaylistLinkEntry(BaseEntry<?> base) {
    super(base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_PLAYLIST_LINK);
  }

  /** Sets the private flag. */
  public void setPrivate(boolean value) {
    if (value) {
      setExtension(new YtPrivate());
    } else {
      removeExtension(YtPrivate.class);
    }
  }

  /** Gets the value of the private flag. */
  public boolean isPrivate() {
    return getExtension(YtPrivate.class) != null;
  }
  
  /**
   * Get the id of the playlist.
   * @return the playlist id
   */
  public String getPlaylistId() {
    YtPlaylistId playlistId = getExtension(YtPlaylistId.class);
    return playlistId == null ? null : playlistId.getPlaylistId();
  }  
  
  /**
   * Set the id of the playlist. 
   * @param playlistId the id of the playlist
   */
  public void setPlaylistId(String playlistId) {
    if (playlistId != null) {
      addExtension(new YtPlaylistId(playlistId));
    } else {
      removeExtension(YtPlaylistId.class);
    }
  }
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(PlaylistLinkEntry.class, YtPlaylistId.class);
  }
}
