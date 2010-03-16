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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;

/**
 * Describes a keyword entry.
 *
 * 
 */
@Kind.Term(KeywordEntry.KIND)
public class KeywordEntry extends BaseEntry<KeywordEntry> {

  /**
   * Keywords Entry kind term value.
   */
  public static final String KIND = Namespaces.WT_PREFIX + "keyword_entry";

  /**
   * Keywords Entry kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public KeywordEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public KeywordEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public String toString() {
    return "{KeywordEntry " + super.toString() + "}";
  }

}

