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
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.OutOfLineContent;
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
  FeedLinkEntry(BaseEntry<?> base) {
    super(base);
  }

  /**
   * Gets the feed link.
   *
   * @deprecated Starting with version 2, the feed link can be found in the content.
   *             See {@link #getFeedUrl} and {@link #getCountHint}.
   */
  public FeedLink<?> getFeedLink() {
    return getExtension(FeedLink.class);
  }

  /**
   * Sets the feed link.
   *
   * @deprecated Starting with version 2, the feed link can be found in the content.
   */
  public void setFeedLink(FeedLink<?> feedLink) {
    if (feedLink == null) {
      removeExtension(FeedLink.class);
    } else {
      setExtension(feedLink);
    }
  }

  /**
   * Gets the URL of the enclosed feed.
   *
   * This method works both in version 1 and 2.
   *
   * @return URL to the enclosed feed or {@code null}
   */
  public String getFeedUrl() {
    if (getContent() instanceof OutOfLineContent) {
      return ((OutOfLineContent) getContent()).getUri();
    }
    FeedLink<?> feedLink = getFeedLink();
    if (feedLink != null) {
      return feedLink.getHref();
    }
    return null;
  }

  /**
   * Gets an estimate of how many entries can be found
   * in the enclosed feed.
   *
   * This method works both in version 1 and 2
   *
   * @return an estimate of the number of entries in the
   *         enclosed feed or {@code null}
   */
  public Integer getCountHint() {
    YtCountHint countHint = getExtension(YtCountHint.class);
    if (countHint != null) {
      return countHint.getValue();
    }

    FeedLink<?> feedLink = getFeedLink();
    if (feedLink != null) {
      return feedLink.getCountHint();
    }

    return null;
  }

  /**
   * Sets the estimate of how many entries can be
   * found in the enclosed feed.
   *
   * This method adds a tag {@code yt:countHint}.
   *
   * @param hint count hint or {@code null}
   * @since 2.0
   */
  public void setCountHint(Integer hint) {
    if (hint == null) {
      removeExtension(YtCountHint.class);
    } else {
      setExtension(new YtCountHint(hint));
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

  /**
   * Gets the plaintext user-provided description.
   *
   * @deprecated Valid only in version 1. Replaced in version 2.0 with
   *             atom:summary.
   */
  @Deprecated
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /**
   * Sets the plaintext user-provided description.
   *
   * @deprecated Valid only in version 1. Replaced in version 2.0 with
   *             atom:summary.
   */
  @Deprecated
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

    // Only in version 1
    extProfile.declare(concreteClass, YtDescription.class);
    
    ExtensionDescription feedLinkDescription = FeedLink.getDefaultDescription();
    feedLinkDescription.setRepeatable(false);
    extProfile.declare(concreteClass, feedLinkDescription);

    // Only in version 2
    extProfile.declare(concreteClass, YtCountHint.class);

    extProfile.declareArbitraryXmlExtension(concreteClass);
  }
}
