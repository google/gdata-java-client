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


package com.google.gdata.data.sites;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * An activity entry for content recovery.
 *
 * 
 */
@Kind.Term(RecoveryActivityEntry.KIND)
public class RecoveryActivityEntry extends
    BaseActivityEntry<RecoveryActivityEntry> {

  /**
   * Recovery recovery kind kind term value.
   */
  public static final String KIND = SitesNamespace.SITES_PREFIX + "recovery";

  /**
   * Recovery recovery kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "recovery");

  /**
   * Default mutable constructor.
   */
  public RecoveryActivityEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public RecoveryActivityEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public String toString() {
    return "{RecoveryActivityEntry " + super.toString() + "}";
  }

}

