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

package com.google.gdata.data.extensions;

import com.google.gdata.util.Namespaces;
import com.google.gdata.data.Category;

/**
 * A utility aggregating gdata common data entry labels
 *
 * 
 */
public final class Labels {

  private Labels() {
    // utility class
  }

  /** Google data labeling namespace */
  private static final String gLabels = Namespaces.g + "/labels";

  private static Category newLabel(String termAndLabel) {
    return new Category(gLabels, gLabels + "#" + termAndLabel, termAndLabel);
  }

  /** Common category for a starred item */
  public static final Category STARRED = newLabel("starred");

  /** Common category for a trashed item */
  public static final Category TRASHED = newLabel("trashed");

  /** Common category for a published item */
  public static final Category PUBLISHED = newLabel("published");

  /** Common category for a private item */
  public static final Category PRIVATE = newLabel("private");

  /** Common category for an item owned by the current user */
  public static final Category MINE = newLabel("mine");

  /** Common category for an item shared with the domain */
  public static final Category SHARED_WITH_DOMAIN =
      newLabel("shared-with-domain");

  /** Common category for a hidden item */
  public static final Category HIDDEN = newLabel("hidden");
  
  /** Common category for a viewed item */
  public static final Category VIEWED = newLabel("viewed");
  
  /** Common category for an item with restricted downloading */
  public static final Category RESTRICTED_DOWNLOAD = newLabel("restricted-download");
}
