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


package com.google.gdata.data.blogger;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Link;

/**
 * Describes a blog entry in the feed of a user's blogs.
 *
 * 
 */
public class BlogEntry extends BaseEntry<BlogEntry> {

  /**
   * Default mutable constructor.
   */
  public BlogEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public BlogEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  /**
   * Returns the link that provides the URI that can be used to post new entries
   * to the feed.
   *
   * @return Link that provides the URI that can be used to post new entries to
   *     the feed or {@code null} for none.
   */
  public Link getEntryPostLink() {
    return getLink(Link.Rel.ENTRY_POST, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI of the full feed (without any query
   * parameters).
   *
   * @return Link that provides the URI of the full feed (without any query
   *     parameters) or {@code null} for none.
   */
  public Link getFeedLink() {
    return getLink(Link.Rel.FEED, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI of the web content.
   *
   * @return Link that provides the URI of the web content or {@code null} for
   *     none.
   */
  public Link getRepliesLink() {
    return getLink(BloggerLink.Rel.REPLIES, Link.Type.ATOM);
  }

  @Override
  public String toString() {
    return "{BlogEntry " + super.toString() + "}";
  }

}
