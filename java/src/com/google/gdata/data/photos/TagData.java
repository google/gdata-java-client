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


package com.google.gdata.data.photos;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.util.ParseException;

/**
 * Basic extension to include tag-specific data, this is the shared interface
 * for all tag objects in the Picasaweb GData api.  To view or set the actual
 * content of a tag clients should use {@link BaseEntry#getContent()} and
 * {@link BaseEntry#setContent(TextConstruct)}.
 *
 * 
 */
public interface TagData extends GphotoData {

  /** The unqualified kind for a tag. */
  public static final String KIND = "tag";

  /** The fully qualified kind term for tags. */
  public static final String TAG_KIND = Namespaces.PHOTOS_PREFIX + KIND;

  /** A category object for tags.  All tag objects will have this set. */
  public static final Category TAG_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, TAG_KIND);

  /**
   * The weight of the tag is the number of times the tag appears in the
   * collection of tags currently being viewed.  This is a server-provided
   * context sensitive measure of how often the tag is used, and can be used
   * to implement tag-cloud like views.
   * 
   * @return the weight of the tag, or null if it doesn't have one.
   */
  public Integer getWeight() throws ParseException;

  /**
   * Set the weight of the tag.  Used by the server, this field is read only
   * on the client.
   */
  public void setWeight(Integer weight);
}
