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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
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
   * @exception IllegalArgumentException if {@code content} is not a
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

      public MediaContent get(int index) {
        return contents.get(index);
      }

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
    extProfile.declare(YouTubeMediaGroup.class, YtDuration.class);
    extProfile.declare(YouTubeMediaGroup.class, YtPrivate.class);
    extProfile.declare(YouTubeMediaGroup.class, YouTubeMediaContent.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaPlayer.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaKeywords.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaTitle.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaDescription.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaRestriction.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaCategory.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaThumbnail.class);
    extProfile.declare(YouTubeMediaGroup.class, MediaRating.class);
    extProfile.declareArbitraryXmlExtension(YouTubeMediaGroup.class);
  }
}
