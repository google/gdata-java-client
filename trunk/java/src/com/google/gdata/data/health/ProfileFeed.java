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


package com.google.gdata.data.health;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;

/**
 * Describes a profile feed.
 *
 * 
 */
@Kind.Term(ProfileEntry.KIND)
public class ProfileFeed extends BaseHealthFeed<ProfileFeed, ProfileEntry> {

  /**
   * Default mutable constructor.
   */
  public ProfileFeed() {
    super(ProfileEntry.class);
    getCategories().add(ProfileEntry.CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public ProfileFeed(BaseFeed<?, ?> sourceFeed) {
    super(ProfileEntry.class, sourceFeed);
  }

  /**
   * Returns the link that provides the URI that can be used to edit the entry.
   *
   * @return Link that provides the URI that can be used to edit the entry or
   *     {@code null} for none.
   */
  public Link getEditLink() {
    return getLink(Link.Rel.ENTRY_EDIT, Link.Type.ATOM);
  }

  @Override
  public String toString() {
    return "{ProfileFeed " + super.toString() + "}";
  }

}
