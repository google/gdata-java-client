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

import java.util.Iterator;

/**
 * Entries that appear on the publisher playlist feed.
 *
 * This entry does not contain a playlist, but a link to a playlist. See
 * {@link PlaylistFeed} and {@link PlaylistEntry} for the playlist feed
 * objects.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_SUBSCRIPTION)
public class SubscriptionEntry extends FeedLinkEntry<SubscriptionEntry>{

  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.SUBSCRIPTIONTYPE_SCHEME
  };


  /**
   * Subscription types. More types will appear over time.
   */
  public static enum Type {
    CHANNEL, FAVORITES, QUERY, PLAYLIST, USER;

    /**
     * Gets the category term corresponding to this subscription
     * type.
     *
     * @return category term
     */
    public String getTerm() {
      return toString().toLowerCase();
    }

    /**
     * Given a term of a category with scheme
     * {@link YouTubeNamespace#KIND_SUBSCRIPTION}, look for
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

  public SubscriptionEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_SUBSCRIPTION);
  }

  public SubscriptionEntry(BaseEntry<?> base) {
    super(base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_SUBSCRIPTION);
  }

  /**
   * Adds a type category to this entry.
   *
   * @param type subscription types
   */
  public void addSubscriptionType(Type type) {
    getCategories().add(
        new Category(YouTubeNamespace.SUBSCRIPTIONTYPE_SCHEME, type.getTerm()));
  }

  /**
   * Gets the type of the subscription, if it is of a known type.
   *
   * @return subscription type or null if it is of an unknown type
   */
  public Type getSubscriptionType() {
    for (Category category : getCategories()) {
      if (YouTubeNamespace.SUBSCRIPTIONTYPE_SCHEME.equals(
          category.getScheme())) {
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
   * @param type new subscription type or {@code null}
   */
  public void setSubscriptionType(Type type) {
    clearSubscriptionType();
    if (type != null) {
      addSubscriptionType(type);
    }
  }

  /**
   * Removes all categories with scheme {@link YouTubeNamespace.SUBSCRIPTIONTYPE_SCHEME}.
   */
  private void clearSubscriptionType() {
    for (Iterator<Category> categories = getCategories().iterator(); 
        categories.hasNext(); ) {
      Category category = categories.next();
      if (YouTubeNamespace.SUBSCRIPTIONTYPE_SCHEME.equals(
          category.getScheme())) {
        categories.remove();
      }
    } 
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
  
  /**
   * Get the value of the optional username
   * @return the username
   */
  public String getUsername() {
    YtUsername username = getExtension(YtUsername.class);
    return username == null ? null : username.getContent();
  }
  
  /**
   * Set the optional query string of a subscription with the type query.
   * @param query the query string
   */
  public void setQueryString(String query) {
    if (query != null) {
      addExtension(new YtQueryString(query));
    } else {
      removeExtension(YtQueryString.class);
    }
  }
  
  /**
   * Get the query string of the subscription.
   * @return the query string
   */
  public String getQueryString() {
    YtQueryString query = getExtension(YtQueryString.class);
    return query == null ? null : query.getContent();
  }
  
  /**
   * Set the title of the playlist subscribed to.   
   *
   * @param playlistTitle the playlistTitle
   */
  public void setPlaylistTitle(String playlistTitle) {
    if (playlistTitle != null) {
      addExtension(new YtPlaylistTitle(playlistTitle));
    } else {
      removeExtension(YtPlaylistTitle.class);
    }
  }
  
  /**
   * Get the title of the playlist subscribed to.
   * @return the playlist title
   */
  public String getPlaylistTitle() {
    YtPlaylistTitle playlistTitle = getExtension(YtPlaylistTitle.class);
    return playlistTitle == null ? null : playlistTitle.getContent();
  }
  
  /**
   * Set the optional id of the playlist subscribed to. 
   * @param playlistId the id of the playlist
   */
  public void setPlaylistId(String playlistId) {
    if (playlistId != null) {
      addExtension(new YtPlaylistId(playlistId));
    } else {
      removeExtension(YtPlaylistId.class);
    }
  }
  
  /**
   * Get the id of the playlist subscribed to.
   * @return the playlist id
   */
  public String getPlaylistId() {
    YtPlaylistId playlistId = getExtension(YtPlaylistId.class);
    return playlistId == null ? null : playlistId.getPlaylistId();
  }  
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    
    extProfile.declare(SubscriptionEntry.class, YtUsername.class);
    extProfile.declare(SubscriptionEntry.class, YtQueryString.class);
    extProfile.declare(SubscriptionEntry.class, YtPlaylistTitle.class);
    extProfile.declare(SubscriptionEntry.class, YtPlaylistId.class);
  }
}
