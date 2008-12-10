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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.threading.Total;

/**
 * Describes a blog post entry.
 *
 * 
 */
public class PostEntry extends BaseEntry<PostEntry> {

  /**
   * Default mutable constructor.
   */
  public PostEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public PostEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(PostEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(PostEntry.class,
        new ExtensionDescription(MediaThumbnail.class, new XmlNamespace("media",
        "http://search.yahoo.com/mrss/"), "thumbnail", false, false, false));
    extProfile.declare(PostEntry.class, Total.class);
  }

  /**
   * Returns the media thumbnail.
   *
   * @return media thumbnail
   */
  public MediaThumbnail getThumbnail() {
    return getExtension(MediaThumbnail.class);
  }

  /**
   * Sets the media thumbnail.
   *
   * @param thumbnail media thumbnail or <code>null</code> to reset
   */
  public void setThumbnail(MediaThumbnail thumbnail) {
    if (thumbnail == null) {
      removeExtension(MediaThumbnail.class);
    } else {
      setExtension(thumbnail);
    }
  }

  /**
   * Returns whether it has the media thumbnail.
   *
   * @return whether it has the media thumbnail
   */
  public boolean hasThumbnail() {
    return hasExtension(MediaThumbnail.class);
  }

  /**
   * Returns the total.
   *
   * @return total
   */
  public Total getTotal() {
    return getExtension(Total.class);
  }

  /**
   * Sets the total.
   *
   * @param total total or <code>null</code> to reset
   */
  public void setTotal(Total total) {
    if (total == null) {
      removeExtension(Total.class);
    } else {
      setExtension(total);
    }
  }

  /**
   * Returns whether it has the total.
   *
   * @return whether it has the total
   */
  public boolean hasTotal() {
    return hasExtension(Total.class);
  }

  /**
   * Returns the link that identifies a resource related to the entry.
   *
   * @return Link that identifies a resource related to the entry or {@code
   *     null} for none.
   */
  public Link getEnclosureLink() {
    return getLink(Link.Rel.ENCLOSURE, null);
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
  public Link getRepliesHtmlLink() {
    return getLink(BloggerLink.Rel.REPLIES, Link.Type.HTML);
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
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{PostEntry " + super.toString() + "}";
  }

}
