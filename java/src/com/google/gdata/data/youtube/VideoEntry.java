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
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.PubControl;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.geo.impl.GmlPoint;
import com.google.gdata.data.geo.impl.GmlPos;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.media.mediarss.MediaRssNamespace;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Video entry for the youtube feeds.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "video"
)
@Kind.Term(YouTubeNamespace.KIND_VIDEO)
public class VideoEntry extends MediaEntry<VideoEntry> {

  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.CATEGORY_SCHEME,
    YouTubeNamespace.KEYWORD_SCHEME,
    YouTubeNamespace.DEVELOPER_TAG_SCHEME
  };

  /**
   * Creates an empty video entry.
   */
  public VideoEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO);
  }

  /** Creates a copy of another entry. */
  public VideoEntry(BaseEntry<?> original) {
    super(original);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_VIDEO);
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

  /**
   * Sets the yt:racy flag.
   * 
   * @deprecated in favor of {@link MediaGroup#getRatings()} with scheme {@link
   *             YouTubeNamespace#MEDIA_RATING_SCHEME}. Removed in version 2.
   */
  @Deprecated
  public void setRacy(boolean racy) {
    if (racy) {
      setExtension(new YtRacy());
    } else {
      removeExtension(YtRacy.class);
    }
  }

  /**
   * Checks the yt:racy flag.
   * 
   * @deprecated in favor of {@link MediaGroup#getRatings()} with scheme {@link
   *             YouTubeNamespace#MEDIA_RATING_SCHEME}. Removed in version 2
   */
  @Deprecated
  public boolean isRacy() {
    YtRacy racy = getExtension(YtRacy.class);
    return racy != null;
  }

  /** Sets the value of the yt:recorded tag. */
  public void setRecorded(DateTime date) {
    if (date == null) {
      removeExtension(YtRecorded.class);
    } else {
      setExtension(new YtRecorded(date));
    }
  }

  /** Gets the value of the yt:recorded tag. */
  public DateTime getRecorded() {
    YtRecorded recorded = getExtension(YtRecorded.class);
    return recorded == null ? null : recorded.getDate();
  }

  /** Adds a georss:where tag. */
  public void setGeoCoordinates(GeoRssWhere where) {
    if (where == null) {
      removeExtension(GeoRssWhere.class);
    } else {
      setExtension(where);
    }
  }

  /** Gets the georss:where tag. */
  public GeoRssWhere getGeoCoordinates() {
    return getExtension(GeoRssWhere.class);
  }

  /** Gets the yt:location tag. */
  public String getLocation() {
    YtLocation tag = getExtension(YtLocation.class);
    return tag == null ? null : tag.getContent();
  }

  /** Sets the yt:location tag. */
  public void setLocation(String location) {
    if (location == null) {
      removeExtension(YtLocation.class);
    } else {
      setExtension(new YtLocation(location));
    }
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

  /** Returns a link to the video rating feed. */
  public Link getRatingLink() {
    return getLink(YouTubeNamespace.RATINGS_REL, Link.Type.ATOM);
  }

  /** Returns a link to the video complaints feed. */
  public Link getComplaintsLink() {
    return getLink(YouTubeNamespace.COMPLAINTS_REL, Link.Type.ATOM);
  }

  /** Returns a link to the related videos feed. */
  public Link getRelatedVideosLink() {
    return getLink(YouTubeNamespace.RELATED_REL, Link.Type.ATOM);
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
  
  /** Gets the yt:rating tag. */
  public YtRating getYtRating() {
    return getExtension(YtRating.class);
  }
  
  /** Sets the yt:rating. */
  public void setYtRating(YtRating rating) {
    if (rating == null) {
      removeExtension(YtRating.class);
    } else {
      setExtension(rating);
    }
  }

  /**
   * Sets the publication state of this entry, using the tag
   * app:control/yt:status.
   *
   * @param state publication state or {@code null}
   */
  public void setPublicationState(YtPublicationState state) {
    PubControl control = getPubControl();
    if (state == null) {
      if (control != null) {
        control.removeExtension(YtPublicationState.class);
        // check if we can remove app. control
        if (control.isDraft() == false && control.getExtensions().isEmpty()) {
          setPubControl(null);
        }
      }
    } else {
      if (control == null) {
        control = new PubControl();
        setPubControl(control);
      }
      control.setExtension(state);
    }
  }

  /**
   * Gets the publication state of this entry from the tag
   * app:control/yt:status.
   *
   * @return publication state or {@code null}, in which case
   *     the video is live
   */
  public YtPublicationState getPublicationState() {
    PubControl control  = getPubControl();
    return control == null ? null : control.getExtension(YtPublicationState.class);
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

  /**
   * Sets the completion state of this entry, using the tag
   * app:control/yt:incomplete.
   */
  public void setYtIncomplete(boolean ytIncomplete) {
    PubControl control = getPubControl();
    if (!ytIncomplete) {
      if (control != null) {
        control.removeExtension(YtIncomplete.class);
        // check if we can remove app. control
        if (control.getExtensions().isEmpty()) {
          setPubControl(null);
        }
      }
    } else {
      if (control == null) {
        control = new PubControl();
        setPubControl(control);
      }
      control.setExtension(new YtIncomplete());
      control.setDraft(true);
    }
  }

  /**
   * Gets the completion state of this entry from the tag
   * app:control/yt:incomplete.
   *
   * @return {@code true} if the entry is marked incomplete
   *         {@code false} otherwise.
   */
  public boolean isYtIncomplete() {
    PubControl control = getPubControl();
    return control != null && control.getExtension(YtIncomplete.class) != null;
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(PubControl.class, YtPublicationState.class);

    extProfile.declare(VideoEntry.class, Comments.getDefaultDescription());
    extProfile.declare(VideoEntry.class, Rating.getDefaultDescription(false));
    extProfile.declareAdditionalNamespace(Namespaces.gNs);
    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);

    extProfile.declare(VideoEntry.class, YtRacy.class);
    extProfile.declare(VideoEntry.class, YtRecorded.class);
    extProfile.declare(VideoEntry.class, YtStatistics.class);
    extProfile.declare(VideoEntry.class, YtNoEmbed.class);
    extProfile.declare(VideoEntry.class, YtLocation.class);
    extProfile.declare(VideoEntry.class, YtRating.class);

    extProfile.declare(VideoEntry.class, YouTubeMediaGroup.class);
    new YouTubeMediaGroup().declareExtensions(extProfile);
    extProfile.declareAdditionalNamespace(MediaRssNamespace.NS);

    extProfile.declare(VideoEntry.class, GeoRssWhere.getDefaultDescription(false));
    extProfile.declareAdditionalNamespace(
        com.google.gdata.data.geo.Namespaces.GEO_RSS_NAMESPACE);
    
    extProfile.declare(GeoRssWhere.class, GmlPoint.getDefaultDescription(false));
    extProfile.declare(GmlPoint.class, GmlPos.getDefaultDescription(false));
    extProfile.declareAdditionalNamespace(
        com.google.gdata.data.geo.Namespaces.GML_NAMESPACE);

    extProfile.declare(Link.class, YtToken.class);
    
    // Ignore unsupported XML tags instead of rejecting them.
    // Very useful in a client.
    extProfile.declareArbitraryXmlExtension(VideoEntry.class);
  }
}
