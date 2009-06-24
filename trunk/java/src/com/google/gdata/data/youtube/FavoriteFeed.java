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
 * A YouTube favorite feed.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_FAVORITE)
public class FavoriteFeed extends BaseFeed<FavoriteFeed, FavoriteEntry> {
  /**
   * Creates an empty favorite feed.
   */
  public FavoriteFeed() {
    super(FavoriteEntry.class);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_FAVORITE);
  }
  
  /**
   * Creates a copy of the given feed.
   * 
   * @param base feed to copy.
   */
  public FavoriteFeed(BaseFeed<?, ?> base) {
    super(FavoriteEntry.class, base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_FAVORITE);
  }
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
  }
}
