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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a list item entry.
 *
 * 
 */
@Kind.Term(ListItemEntry.KIND)
public class ListItemEntry extends BaseContentEntry<ListItemEntry> {

  /**
   * Listitem list item kind kind term value.
   */
  public static final String KIND = SitesNamespace.SITES_PREFIX + "listitem";

  /**
   * Listitem list item kind kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND,
      "listitem");

  /**
   * Default mutable constructor.
   */
  public ListItemEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ListItemEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ListItemEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ListItemEntry.class, Field.getDefaultDescription(false,
        true));
  }

  /**
   * Returns the fields.
   *
   * @return fields
   */
  public List<Field> getFields() {
    return getRepeatingExtension(Field.class);
  }

  /**
   * Adds a new field.
   *
   * @param field field
   */
  public void addField(Field field) {
    getFields().add(field);
  }

  /**
   * Returns whether it has the fields.
   *
   * @return whether it has the fields
   */
  public boolean hasFields() {
    return hasRepeatingExtension(Field.class);
  }

  /**
   * Returns the parent sites link.
   *
   * @return Parent sites link or {@code null} for none.
   */
  public Link getParentLink() {
    return getLink(SitesLink.Rel.PARENT, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ListItemEntry " + super.toString() + "}";
  }

}

