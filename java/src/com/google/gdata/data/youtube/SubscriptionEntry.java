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
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;


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
   * Subscription types. More types will appear over time.
   */
  public static enum Type {
    CHANNEL, FAVORITES, QUERY;

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
     * {@link YouTubeNamespace.KIND_SUBSCRIPTION}, look for
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
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_SUBSCRIPTION);
  }

  public SubscriptionEntry(BaseEntry base) {
    super(base);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_SUBSCRIPTION);
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
}
