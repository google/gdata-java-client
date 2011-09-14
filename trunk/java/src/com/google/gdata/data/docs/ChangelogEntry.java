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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * Describes an entry representing a single changed document of any type.
 *
 * 
 */
@Kind.Term(ChangelogEntry.KIND)
public class ChangelogEntry extends BaseDocumentListEntry<ChangelogEntry> {

  /**
   * Change kind term value.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX + "change";

  /**
   * Change kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "change");

  /**
   * Default mutable constructor.
   */
  public ChangelogEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ChangelogEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ChangelogEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ChangelogEntry.class,
        Changestamp.getDefaultDescription(true, false));
    extProfile.declare(ChangelogEntry.class, Removed.class);
  }

  /**
   * Returns the changestamp.
   *
   * @return changestamp
   */
  public Changestamp getChangestamp() {
    return getExtension(Changestamp.class);
  }

  /**
   * Sets the changestamp.
   *
   * @param changestamp changestamp or <code>null</code> to reset
   */
  public void setChangestamp(Changestamp changestamp) {
    if (changestamp == null) {
      removeExtension(Changestamp.class);
    } else {
      setExtension(changestamp);
    }
  }

  /**
   * Returns whether it has the changestamp.
   *
   * @return whether it has the changestamp
   */
  public boolean hasChangestamp() {
    return hasExtension(Changestamp.class);
  }

  /**
   * Returns the removed.
   *
   * @return removed
   */
  public Removed getRemoved() {
    return getExtension(Removed.class);
  }

  /**
   * Sets the removed.
   *
   * @param removed removed or <code>null</code> to reset
   */
  public void setRemoved(Removed removed) {
    if (removed == null) {
      removeExtension(Removed.class);
    } else {
      setExtension(removed);
    }
  }

  /**
   * Returns whether it has the removed.
   *
   * @return whether it has the removed
   */
  public boolean hasRemoved() {
    return hasExtension(Removed.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ChangelogEntry " + super.toString() + "}";
  }

}

