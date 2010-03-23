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

package com.google.gdata.data.docs;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * An entry representing a folder in a {@link FolderFeed}.
 * 
 * 
 */
@Kind.Term(FolderEntry.KIND)
public class FolderEntry extends DocumentListEntry {

  /**
   * Label for category.
   */
  public static final String LABEL = "folder";

  /**
   * Kind category term used to label the entries.
   */
  public static final String KIND = DocumentListFeed.DOCUMENT_NAMESPACE
      + "#" + FolderEntry.LABEL;

  /**
   * Category used to label entries which are of folder type.
   */
  public static final Category CATEGORY =
    new Category(Namespaces.gKind, KIND, LABEL);

  /**
   * Constructs a new uninitialized entry, to be populated by the GData
   * parsers.
   */
  public FolderEntry() {
    getCategories().remove(DocumentListEntry.CATEGORY);
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public FolderEntry(BaseEntry<FolderEntry> sourceEntry) {
    super(sourceEntry);
  }
}
