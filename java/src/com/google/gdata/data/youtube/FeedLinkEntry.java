/* Copyright (c) 2006 Google Inc.
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
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.media.mediarss.MediaThumbnail;

/**
 * An entry that's actually a glorified feed link, used in
 * playlists and subscription feeds.
 *
 * 
 */
public abstract class FeedLinkEntry<T extends BaseEntry> extends BaseEntry<T> {

  /**
   * Creates an unitialized entry.
   *
   * This constructor is package-local so that the class cannot be
   * subclassed outside of this package.
   */
  FeedLinkEntry() {
  }

  /**
   * Copies an entry.
   *
   * This constructor is package-local so that the class cannot be
   * subclassed outside of this package.
   *
   * @param base original entry
   */
  FeedLinkEntry(BaseEntry base) {
    super(base);
  }

  /** Gets the feed link. */
  public FeedLink getFeedLink() {
    return getExtension(FeedLink.class);
  }

  /** Sets the feed link. */
  public void setFeedLink(FeedLink feedLink) {
    if (feedLink == null) {
      removeExtension(FeedLink.class);
    } else {
      setExtension(feedLink);
    }
  }

  /** Gets the thumbnail. */
  public MediaThumbnail getThumbnail() {
    return getExtension(MediaThumbnail.class);
  }

  /** Sets the thumbnail. */
  public void setThumbnail(MediaThumbnail thumbnail) {
    if (thumbnail == null) {
      removeExtension(MediaThumbnail.class);
    } else {
      setExtension(thumbnail);
    }
  }

  /** Gets the plaintext user-provided description. */
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /** Sets the plaintext user-provided description. */
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(YtDescription.class);
    } else {
      setExtension(new YtDescription(description));
    }
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    Class<? extends FeedLinkEntry> concreteClass = getClass();

    ExtensionDescription thumbnailDescription = MediaThumbnail
        .getDefaultDescription();
    // There can be only one thumbnail in this feed.
    thumbnailDescription.setRepeatable(false);
    extProfile.declare(concreteClass, thumbnailDescription);
    extProfile.declare(concreteClass, YtPrivate.class);
    extProfile.declare(concreteClass, YtDescription.class);

    extProfile.declare(concreteClass, FeedLink.getDefaultDescription());
    extProfile.declareArbitraryXmlExtension(concreteClass);
  }
}
