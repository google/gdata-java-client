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

import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaRating;
import com.google.gdata.data.media.mediarss.MediaRestriction;
import com.google.gdata.data.media.mediarss.MediaRssNamespace;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.media.mediarss.MediaTitle;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Subset of {@code <media:group>}.
 *
 * See description on
 * <a href="http://search.yahoo.com/mrss">http://search.yahoo.com/mrss</a>.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "group"
)
public class YouTubeMediaGroup extends MediaGroup {

  private static final String UPLOADER_ROLE = "uploader";

  /**
   * Gets the YouTube ID of the video.
   * 
   * @since 2.0
   */
  public String getVideoId() {
    YtVideoId videoId = getExtension(YtVideoId.class);
    return videoId == null ? null : videoId.getVideoId();
  }
  
  /**
   * Sets the YouTube video ID of the video.
   * 
   * @since 2.0
   */
  public void setVideoId(String videoId) {
    if (videoId == null) {
      removeExtension(YtVideoId.class);
    } else {
      setExtension(new YtVideoId(videoId));
    }
  }
  
  /** Gets the duration, in seconds, of the youtube video. */
  public Long getDuration() {
    YtDuration duration = getExtension(YtDuration.class);
    return duration == null ? null : duration.getSeconds();
  }

  /** Sets the duration, in seconds, of the youtube video. */
  public void setDuration(Long seconds) {
    if (seconds == null) {
      removeExtension(YtDuration.class);
    } else {
      YtDuration duration = new YtDuration();
      duration.setSeconds(seconds);
      setExtension(duration);
    }
  }
  
  /**
   * Returns the time the video was uploaded at.
   * 
   * @since 2.0
   */
  public DateTime getUploaded() {
    YtUploaded uploadTime = getExtension(YtUploaded.class);
    return uploadTime != null ? uploadTime.getDateTime() : null;
  }
  
  /** 
   * Sets or unsets the time the video was uploaded at.
   *
   * @since 2.0
   */
  public void setUploaded(DateTime dateTime) {
    if (dateTime == null) {
      removeExtension(YtUploaded.class);
    } else {
      setExtension(new YtUploaded(dateTime));
    }
  }

  /**
   * Sets the YouTube user who uploaded the video.
   *
   * @param uploader YouTube user name or {@code null}
   * @since 2.0
   */
  public void setUploader(String uploader) {
    for (Iterator<YouTubeMediaCredit> iterator = getYouTubeCredits().iterator();
        iterator.hasNext(); ) {
      MediaCredit credit = iterator.next();
      if (UPLOADER_ROLE.equals(credit.getRole())
          && YouTubeNamespace.CREDIT_SCHEME.equals(credit.getScheme())) {
        iterator.remove();
      }
    }
    if (uploader != null) {
      YouTubeMediaCredit credit = new YouTubeMediaCredit();
      credit.setScheme(YouTubeNamespace.CREDIT_SCHEME);
      credit.setRole(UPLOADER_ROLE);
      credit.setContent(uploader);
      addCredit(credit);
    }
  }

  /**
   * Gets the YouTube user who uploaded the video.
   *
   * @return YouTube user name or {@code null}
   * @since 2.0
   */
  public String getUploader() {
    YouTubeMediaCredit uploader = getUploaderTag();
    return uploader == null ? null : uploader.getContent();
  }

  /**
   * Checks whether the uploader is a partner.
   *
   * @return uploader type
   */
  public YouTubeMediaCredit.Type getUploaderType() {
    YouTubeMediaCredit uploader = getUploaderTag();
    return uploader == null ? null : uploader.getType();
  }

  /**
   * Returns a {@code media:credit} with role {@code uploader}.
   *
   * @return a {@code media:credit} tag or {@code null}
   */
  private YouTubeMediaCredit getUploaderTag() {
    for (YouTubeMediaCredit credit : getYouTubeCredits()) {
      if (UPLOADER_ROLE.equals(credit.getRole())
          && YouTubeNamespace.CREDIT_SCHEME.equals(credit.getScheme())) {
        return credit;
      }
    }
    return null;
  }

  /**
   * Gets a modifiable list of {@link YouTubeMediaContent}.
   *
   * @return list of {@code MediaContent}.
   */
  public List<YouTubeMediaContent> getYouTubeContents() {
    return getRepeatingExtension(YouTubeMediaContent.class);
  }

  /**
   * Adds a new {@link MediaContent}.
   *
   * YouTube entries can only contain {@link YouTubeMediaContent} and this
   * method checks that at runtime. Please use
   * {@link #addContent(YouTubeMediaContent)} instead.
   *
   * @param content
   * @throws IllegalArgumentException if {@code content} is not a
   *   {@link YouTubeMediaContent}
   */
  @Override
  public void addContent(MediaContent content) {
    if (!(content instanceof YouTubeMediaContent)) {
      throw new IllegalArgumentException("YouTube entries requires "
          + "YouTubeMediaContent");
    }
    super.addContent(content);
  }

  /**
   * Gets a read-only list of {@link MediaContent}.
   *
   * YouTube entries only contain {@link YouTubeMediaContent}. Please use
   * {@link #getYouTubeContents()} instead to make sure you have access to
   * a modifiable lis.
   *
   * This collection has been made read-only to make sure only
   * {@link YouTubeMediaContent} are ever added. Please use
   * {@link #getYouTubeContents()}/{@link #addContent(YouTubeMediaContent)} to
   * modify the list of media:content tags..
   *
   * @return a read-only collection of {@code MediaContent}
   */
  @Override
  public List<MediaContent> getContents() {
    final List<YouTubeMediaContent> contents = getYouTubeContents();
    return new AbstractList<MediaContent>() {

      @Override
      public MediaContent get(int index) {
        return contents.get(index);
      }

      @Override
      public int size() {
        return contents.size();
      }
    };
  }

  /**
   * Clears the list of {@code media:content} tags.
   */
  @Override
  public void clearContents() {
    getYouTubeContents().clear();
  }

  /**
   * Adds a {@code media:content} tag.
   *
   * @param content
   */
  public void addContent(YouTubeMediaContent content) {
    addRepeatingExtension(content);
  }

  /**
   * Gets a modifiable list of {@link YouTubeMediaRating}.
   *
   * @return list of {@code MediaRating}.
   */
  public List<YouTubeMediaRating> getYouTubeRatings() {
    return getRepeatingExtension(YouTubeMediaRating.class);
  }

  /**
   * Adds a new {@link MediaRating}.
   *
   * YouTube entries can only contain {@link YouTubeMediaRating} and this
   * method checks that at runtime. Please use
   * {@link #addRating(YouTubeMediaRating)} instead.
   *
   * @param rating
   * @throws IllegalArgumentException if {@code rating} is not a
   *   {@link YouTubeMediaRating}
   */
  @Override
  public void addRating(MediaRating rating) {
    if (!(rating instanceof YouTubeMediaRating)) {
      throw new IllegalArgumentException("YouTube entries requires "
          + "YouTubeMediaRating");
    }
    super.addRating(rating);
  }

  /**
   * Gets a read-only list of {@link MediaRating}.
   *
   * YouTube entries only contain {@link YouTubeMediaRating}. Please use
   * {@link #getYouTubeRatings()} instead to make sure you have access to
   * a modifiable lis.
   *
   * This collection has been made read-only to make sure only
   * {@link YouTubeMediaRating} are ever added. Please use
   * {@link #getYouTubeRatings()}/{@link #addRating(YouTubeMediaRating)} to
   * modify the list of media:rating tags..
   *
   * @return a read-only collection of {@code MediaRating}
   */
  @Override
  public List<MediaRating> getRatings() {
    final List<YouTubeMediaRating> ratings = getYouTubeRatings();
    return new AbstractList<MediaRating>() {

      @Override
      public MediaRating get(int index) {
        return ratings.get(index);
      }

      @Override
      public int size() {
        return ratings.size();
      }
    };
  }

  /**
   * Clears the list of {@code media:rating} tags.
   */
  @Override
  public void clearRatings() {
    getYouTubeRatings().clear();
  }

  /**
   * Adds a {@code media:rating} tag.
   *
   * @param rating
   */
  public void addRating(YouTubeMediaRating rating) {
    addRepeatingExtension(rating);
  }

  /**
   * Gets a modifiable list of {@link YouTubeMediaCredit}.
   *
   * @return list of {@code MediaCredit}.
   */
  public List<YouTubeMediaCredit> getYouTubeCredits() {
    return getRepeatingExtension(YouTubeMediaCredit.class);
  }

  /**
   * Adds a new {@link MediaCredit}.
   *
   * YouTube entries can only contain {@link YouTubeMediaCredit} and this
   * method checks that at runtime. Please use
   * {@link #addCredit(YouTubeMediaCredit)} instead.
   *
   * @param credit
   * @throws IllegalArgumentException if {@code credit} is not a
   *   {@link YouTubeMediaCredit}
   */
  @Override
  public void addCredit(MediaCredit credit) {
    if (!(credit instanceof YouTubeMediaCredit)) {
      throw new IllegalArgumentException("YouTube entries requires "
          + "YouTubeMediaCredit");
    }
    super.addCredit(credit);
  }

  /**
   * Gets a read-only list of {@link MediaCredit}.
   *
   * YouTube entries only contain {@link YouTubeMediaCredit}. Please use
   * {@link #getYouTubeCredits()} instead to make sure you have access to
   * a modifiable lis.
   *
   * This collection has been made read-only to make sure only
   * {@link YouTubeMediaCredit} are ever added. Please use
   * {@link #getYouTubeCredits()}/{@link #addCredit(YouTubeMediaCredit)} to
   * modify the list of media:credit tags..
   *
   * @return a read-only collection of {@code MediaCredit}
   */
  @Override
  public List<MediaCredit> getCredits() {
    final List<YouTubeMediaCredit> credits = getYouTubeCredits();
    return new AbstractList<MediaCredit>() {

      @Override
      public MediaCredit get(int index) {
        return credits.get(index);
      }

      @Override
      public int size() {
        return credits.size();
      }
    };
  }

  /**
   * Clears the list of {@code media:credit} tags.
   */
  @Override
  public void clearCredits() {
    getYouTubeCredits().clear();
  }

  /**
   * Adds a {@code media:credit} tag.
   *
   * @param credit
   */
  public void addCredit(YouTubeMediaCredit credit) {
    addRepeatingExtension(credit);
  }
  
  public void setAspectRatio(YtAspectRatio aspectRatio) {
    if (aspectRatio == null) {
      removeExtension(YtAspectRatio.class);
    } else {
      setExtension(aspectRatio);
    }
  }
  
  public YtAspectRatio getAspectRatio() {
    return getExtension(YtAspectRatio.class);
  }

  /** Checks the yt:private flag. */
  public boolean isPrivate() {
    YtPrivate privacyLevel = getExtension(YtPrivate.class);
    return privacyLevel != null;
  }

  /** Sets the yt:private flag. */
  public void setPrivate(boolean makePrivate) {
    if (makePrivate) {
      setExtension(new YtPrivate());
    } else {
      removeExtension(YtPrivate.class);
    }
  }
  
  /**
   * A YouTube media group can have only one YouTube category defined by this
   * scheme: {@link YouTubeNamespace#CATEGORY_SCHEME} and this method is a
   * helper for retrieving it. See also: {@link #getCategories()}.
   * <p>
   * If two or more such categories are specified it cannot be determined which
   * is the right one so this method will return null.
   * 
   * @return the YouTube category of this video, if such a category cannot be
   *         found or determined it returns null.
   */
  public MediaCategory getYouTubeCategory() {
    Set<MediaCategory> found = getCategoriesWithScheme(YouTubeNamespace.CATEGORY_SCHEME);
    if (found.size() == 1) {
      return found.iterator().next();
    } else if (found.size() > 1) {
      // could not determine the YouTube video category.
      return null;
    } else {
      // the default scheme is the YouTube category scheme,
      // so search for a category with no scheme (=default scheme) next.
      Set<MediaCategory> withNoScheme = getCategoriesWithScheme(null);
      if (withNoScheme.size() == 1) {
        return withNoScheme.iterator().next();
      } else {
        // could not determine the YouTube video category.
        return null;
      }
    }
  }
  
  /**
   * Sets or changes the previously set YouTube category.
   * 
   * @param name the new category name to set.
   */
  public void setYouTubeCategory(String name) {
    for (Iterator<MediaCategory> iterator = getCategories().iterator(); iterator.hasNext();) {
      MediaCategory category = iterator.next();
      if (YouTubeNamespace.CATEGORY_SCHEME.equals(category.getScheme())) {
        iterator.remove();
      }
    }
    
    addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, name));
  }

  /**
   * Declare extensions available in media:group on youtube feeds.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(YouTubeMediaGroup.class, YtAspectRatio.class);
    extProfile.declare(YouTubeMediaGroup.class, YtVideoId.class);
    extProfile.declare(YouTubeMediaGroup.class, YtDuration.class);
    extProfile.declare(YouTubeMediaGroup.class, YtPrivate.class);
    extProfile.declare(YouTubeMediaGroup.class, YtUploaded.class);
    extProfile.declare(YouTubeMediaGroup.class, YouTubeMediaContent.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaPlayer.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaKeywords.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaTitle.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaDescription.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaRestriction.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaCategory.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaThumbnail.class);
    extProfile.declare(YouTubeMediaGroup.class, YouTubeMediaRating.class);
    extProfile.declare(YouTubeMediaGroup.class, YouTubeMediaCredit.class);
    extProfile.declareArbitraryXmlExtension(YouTubeMediaGroup.class);
  }
}
