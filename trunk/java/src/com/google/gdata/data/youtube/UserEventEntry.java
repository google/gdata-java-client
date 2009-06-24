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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.Namespaces;

import java.util.Iterator;

/**
 * An atom entry containing a user event.
 * 
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_USER_EVENT)
public class UserEventEntry extends BaseEntry<UserEventEntry> {
  
  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.USEREVENTS_SCHEME
  };
    
  /**
   * User event types. More types will appear over time.
   */
  public static enum Type {
    VIDEO_UPLOADED, VIDEO_RATED, VIDEO_FAVORITED, VIDEO_SHARED, VIDEO_COMMENTED,
    USER_SUBSCRIPTION_ADDED, FRIEND_ADDED; 

    /**
     * Gets the category term corresponding to this user event type
     *
     * @return category term
     */
    public String getTerm() {
      return toString().toLowerCase();
    }

    /**
     * Given a term of a category with scheme
     * {@link YouTubeNamespace#KIND_USER_EVENT}, look for
     * the corresponding {@code Type} enum value.
     *
     * @return corresponding enum value or null if none exists
     */
    public static Type fromTerm(String term) {
      try {
        return Type.valueOf(term.toUpperCase());
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }
  
  public UserEventEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_USER_EVENT);    
  }
  
  /**
   * Returns the type of the user event, if it is of a known type.
   *
   * @return user event type or null if it is of an unknown type
   */
  public Type getUserEventType() {
    for (Category category : getCategories()) {
      if (YouTubeNamespace.USEREVENTS_SCHEME.equals(category.getScheme())) {
        Type type = Type.fromTerm(category.getTerm());
        if (type != null) {
          return type;
        }
      }
    }
    return null;
  }
  
  /**
   * Sets the type category of this entry.
   *
   * If a type category already exists, it will be removed.
   *
   * @param type new user event type or {@code null}
   */
  public void setEventType(Type type) {
    clearEventType();
    if (type != null) {
      getCategories().add(new Category(YouTubeNamespace.USEREVENTS_SCHEME, type.getTerm()));
    }
  }

  /**
   * Removes all categories with scheme 
   * {@link YouTubeNamespace#USEREVENTS_SCHEME}.
   */
  private void clearEventType() {
    for (Iterator<Category> categories = getCategories().iterator(); 
        categories.hasNext(); ) {
      Category category = categories.next();
      if (YouTubeNamespace.USEREVENTS_SCHEME.equals(category.getScheme())) {
        categories.remove();
      }
    } 
  }

  /**
   * Gets the YouTube ID of the video.
   */
  public String getVideoId() {
    YtVideoId videoId = getExtension(YtVideoId.class);
    return videoId == null ? null : videoId.getVideoId();
  }
  
  /**
   * Sets the YouTube video ID of the video.
   */
  public void setVideoId(String videoId) {
    if (videoId == null) {
      removeExtension(YtVideoId.class);
    } else {
      setExtension(new YtVideoId(videoId));
    }
  }
  
  /**
   * Get the value of the optional username
   * @return the username
   */
  public String getUsername() {
    YtUsername username = getExtension(YtUsername.class);
    return username == null ? null : username.getContent();
  }  
  
  /**
   * Set the optional username.
   * @param username content of the yt:username tag
   */
  public void setUsername(String username) {
    if (username != null) {
      addExtension(new YtUsername(username));
    } else {
      removeExtension(YtUsername.class);
    }
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

  /**
   * Declares the {@link ExtensionDescription} of each {@link Extension} 
   * expected by the implementing {@link ExtensionPoint} in the target profile.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);    
    
    // Extensions for the video entry.
    extProfile.declareAdditionalNamespace(Namespaces.gNs);    
    extProfile.declareAdditionalNamespace(
        com.google.gdata.data.geo.Namespaces.GEO_RSS_NAMESPACE);
    extProfile.declareAdditionalNamespace(
        com.google.gdata.data.geo.Namespaces.GML_NAMESPACE);
    
    extProfile.declare(UserEventEntry.class, YtVideoId.class);
    extProfile.declare(UserEventEntry.class, YtUsername.class);
    extProfile.declare(UserEventEntry.class, Rating.getDefaultDescription(false));
    
    extProfile.declareArbitraryXmlExtension(UserEventEntry.class);
  }
}
