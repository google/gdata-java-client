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

package com.google.api.data.sample.picasa.model;

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.util.Name;

import java.io.IOException;
import java.util.List;

public class UserFeed extends Feed {

  @Name("entry")
  public List<AlbumEntry> albums;

  public static UserFeed executeGet(GoogleTransport transport, String link)
      throws IOException {
    PicasaUri uri = new PicasaUri(link);
    uri.kinds = "album";
    uri.maxResults = 3;
    return (UserFeed) Feed.executeGet(transport, uri, UserFeed.class);
  }

  public AlbumEntry insertAlbum(GoogleTransport transport, AlbumEntry entry)
      throws IOException {
    return (AlbumEntry) super.executeInsert(transport, entry);
  }
}
