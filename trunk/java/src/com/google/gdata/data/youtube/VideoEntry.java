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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.media.MediaEntry;

import java.util.List;

/**
 * Video entry for the youtube feeds.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_VIDEO)
public class VideoEntry extends MediaEntry<VideoEntry> {

  /**
   * Creates an empty video entry.
   */
  public VideoEntry() {
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_VIDEO);
  }

  /** Creates a copy of another entry. */
  public VideoEntry(BaseEntry original) {
    super(original);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_VIDEO);
  }

  /**
   * Creates a new video entry and initializes it.
   *
`   * @param id entry atom/rss id
   */
  public VideoEntry(String id) {
    this();
    setId(id);
  }

  /** Sets yt:statistics tag. */
  public void setStatistics(YtStatistics stats) {
    if (stats == null) {
      removeExtension(YtStatistics.class);
    } else {
      setExtension(stats);
    }
  }

  /** Gets yt:statistics tag. */
  public YtStatistics getStatistics() {
    return getExtension(YtStatistics.class);
  }

  /** Gets whether external sites can embed and play this video. */
  public boolean isEmbeddable() {
    YtNoEmbed noEmbed =
        getExtension(YtNoEmbed.class);
    return noEmbed == null;
  }

  public void setEmbeddable(boolean embeddable) {
    if (embeddable) {
      removeExtension(YtNoEmbed.class);
    } else {
      setExtension(new YtNoEmbed());
    }
  }

  /** Sets the yt:racy flag. */
  public void setRacy(boolean racy) {
    if (racy) {
      setExtension(new YtRacy());
    } else {
      removeExtension(YtRacy.class);
    }
  }

  /** Checks the yt:racy flag. */
  public boolean isRacy() {
    YtRacy racy = getExtension(YtRacy.class);
    return racy != null;
  }

  /** Gets all gd:feedLink tags. */
  public List<FeedLink> getFeedLinks() {
    return getRepeatingExtension(FeedLink.class);
  }

  /** Gets the comments tag or {@code null}. */
  public Comments getComments() {
    return getExtension(Comments.class);
  }

  /** Sets the comments tag. */
  public void setComments(Comments comments) {
    if (comments == null) {
      removeExtension(Comments.class);
    } else {
      setExtension(comments);
    }
  }

  /** Returns a link to the video responses feed. */
  public Link getVideoResponsesLink() {
    return getLink(YouTubeNamespace.RESPONSES_REL, Link.Type.ATOM);
  }

  /** Gets the gd:rating tag. */
  public Rating getRating() {
    return getExtension(Rating.class);
  }

  /** Sets the gd:rating tag. */
  public void setRating(Rating rating) {
    if (rating == null) {
      removeExtension(Rating.class);
    } else {
      setExtension(rating);
    }
  }

  public YouTubeMediaGroup getMediaGroup() {
    return getExtension(YouTubeMediaGroup.class);
  }

  public YouTubeMediaGroup getOrCreateMediaGroup() {
    YouTubeMediaGroup group = getMediaGroup();
    if (group == null) {
      group = new YouTubeMediaGroup();
      setExtension(group);
    }
    return group;
  }

  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(VideoEntry.class, Comments.getDefaultDescription());
    extProfile.declare(VideoEntry.class, Rating.getDefaultDescription(false));
    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);

    extProfile.declare(VideoEntry.class, YtRacy.class);
    extProfile.declare(VideoEntry.class, YtStatistics.class);
    extProfile.declare(VideoEntry.class, YtNoEmbed.class);

    extProfile.declare(VideoEntry.class, YouTubeMediaGroup.class);
    new YouTubeMediaGroup().declareExtensions(extProfile);

    // Ignore unsupported XML tags instead of rejecting them.
    // Very useful in a client.
    extProfile.declareArbitraryXmlExtension(VideoEntry.class);
  }
}
