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
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Category;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.util.Namespaces;

import java.util.Iterator;
import java.util.Set;

/**
 * Utility methods used in this package.
 *
 * 
 */
class EntryUtils {

  /** This is a utility class. */
  private EntryUtils() {
  }

  /**
   * Adds a kind category to the entry.
   *
   * @param entry
   * @param kind
   */
  static void addKindCategory(BaseEntry entry, String kind) {
    addKindCategory(entry.getCategories(), kind);
  }

  /**
   * Adds a kind category to the feed.
   *
   * @param entry
   * @param kind
   */
  static void addKindCategory(BaseFeed entry, String kind) {
    addKindCategory(entry.getCategories(), kind);
  }
  
  /**
   * Replaces the existing kind category with the new given value.
   * 
   * @param entry entry to change.
   * @param newKind the new kind to set.
   */
  static void changeKindCategory(BaseEntry<?> entry, String newKind) {
    for (Iterator<Category> iterator = entry.getCategories().iterator();
        iterator.hasNext();) {
      Category category = iterator.next();
      if (Namespaces.gKind.equals(category.getScheme())) {
        iterator.remove();
      }
    }
    
    addKindCategory(entry, newKind);
  }

  private static void addKindCategory(Set<Category> categories, String kind) {
    categories.add(new Category(Namespaces.gKind, kind));
  }

  /**
   * Returns the first matching {@code gd:feedLink} or {@code null}.
   *
   * @param rel value of the {@code rel} attribute
   */
  static FeedLink getFeedLink(BaseEntry entry, String rel) {
    for (FeedLink feedLink : entry.getRepeatingExtension(FeedLink.class)) {
      if (rel.equals(feedLink.getRel())) {
        return feedLink;
      }
    }
    return null;
  }
}
