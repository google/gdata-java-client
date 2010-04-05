/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.youtube.v2;

import com.google.api.client.escape.CharEscapers;

/** URI builder for YouTube feeds. */
public final class YouTubePath {

  public static final byte TYPE_VIDEOS = 0;

  public static final byte TYPE_EVENTS = 1;

  public static final byte TYPE_USER_FAVORITES = 2;

  public static final byte TYPE_USER_FRIENDS_ACTIVITY = 3;

  public static final byte TYPE_USER_UPLOADS = 4;

  public static final byte TYPE_USER_PLAYLISTS = 5;

  public byte type = TYPE_VIDEOS;

  public String itemId;

  public String user;

  YouTubePath() {
  }

  public static YouTubePath events() {
    YouTubePath result = new YouTubePath();
    result.type = TYPE_EVENTS;
    return result;
  }

  public static YouTubePath videos() {
    return new YouTubePath();
  }

  public static YouTubePath userFavorites(String user) {
    return forUser(TYPE_USER_FAVORITES, user);
  }

  public static YouTubePath userFriendsActivity(String user) {
    return forUser(TYPE_USER_FRIENDS_ACTIVITY, user);
  }

  public static YouTubePath userPlaylists(String user) {
    return forUser(TYPE_USER_PLAYLISTS, user);
  }

  public static YouTubePath userUploads(String user) {
    return forUser(TYPE_USER_UPLOADS, user);
  }

  public String build() {
    StringBuilder buf = new StringBuilder();
    buf.append("http://gdata.youtube.com/feeds/api/");
    switch (type) {
      case TYPE_EVENTS:
        buf.append("events");
        break;
      case TYPE_VIDEOS:
        buf.append("videos");
        break;
      default:
        buf.append("users/").append(CharEscapers.escapeUriPath(user));
        switch (type) {
          case TYPE_USER_FAVORITES:
            buf.append("/favorites");
            break;
          case TYPE_USER_FRIENDS_ACTIVITY:
            buf.append("/friendsactivity");
            break;
          case TYPE_USER_PLAYLISTS:
            buf.append("/playlists");
            break;
          case TYPE_USER_UPLOADS:
            buf.append("/uploads");
            break;
        }
        break;
    }
    String itemId = this.itemId;
    if (itemId != null) {
      buf.append('/').append(itemId);
    }
    return buf.toString();
  }

  private static YouTubePath forUser(byte type, String user) {
    YouTubePath result = new YouTubePath();
    result.type = type;
    result.user = user == null ? "default" : user;
    return result;
  }
}
