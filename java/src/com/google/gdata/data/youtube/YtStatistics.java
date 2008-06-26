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
 * A tag containing statistics about the entry it's in.
 *
 * 
 */
@ExtensionDescription.Default (
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "statistics"
)
public class YtStatistics extends AbstractExtension {
  private long viewCount;
  private long favoriteCount;


  /** Gets view count, 0 by default. */
  public long getViewCount() {
    return viewCount;
  }

  /** Sets view count. */
  public void setViewCount(long viewCount) {
    this.viewCount = viewCount;
  }

  /** Gets favorite count, 0 by default. */
  public long getFavoriteCount() {
    return favoriteCount;
  }

  /** Sets favorite count. */
  public void setFavoriteCount(long favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    if (viewCount > 0) {
      generator.put("viewCount", viewCount);
    }
    if (favoriteCount > 0) {
      generator.put("favoriteCount", favoriteCount);
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    viewCount = helper.consumeLong("viewCount", false, 0L);
    favoriteCount = helper.consumeLong("favoriteCount", false, 0L);
  }
}
