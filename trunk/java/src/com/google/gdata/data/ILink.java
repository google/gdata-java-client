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


package com.google.gdata.data;

import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

/**
 * Common interface for Links.
 * 
 * 
 */
public interface ILink extends Reference {
  /**
   * The Rel class defines constants for some common link relation types.
   */
  public static final class Rel {

    /**
     * Link provides the URI of the feed or entry. If this
     * relation appears on a feed that is the result of performing a
     * query, then this URI includes the same query parameters (or at
     * least querying this URI produces the same result as querying with
     * those parameters).
     */
    public static final String SELF = "self";


    /** Link provides the URI of previous page in a paged feed. */
    public static final String PREVIOUS = "previous";


    /** Link provides the URI of next page in a paged feed. */
    public static final String NEXT = "next";


    /**
     * Link provides the URI of an alternate format of the
     * entry's or feed's contents. The {@code type} property of the link
     * specifies a media type.
     */
    public static final String ALTERNATE = "alternate";

    /**
     * Link provides the URI of a related link to the entry
     */
    public static final String RELATED = "related";

    /**
     * Link provides the URI of the full feed (without any
     * query parameters).
     */
    public static final String FEED = Namespaces.gPrefix + "feed";


    /**
     * Link provides the URI that can be used to post new
     * entries to the feed. This relation does not exist if the feed is
     * read-only.
     */
    public static final String ENTRY_POST = Namespaces.gPrefix + "post";


    /**
     * Link provides the URI that can be used to edit the entry.
     * This relation does not exist if the entry is read-only.
     */
    public static final String ENTRY_EDIT = "edit";

    /**
     * Link provides the URI that can be used to edit the media
     * associated with an entry.  This relation does not exist if
     * there is no associated media or the media is read-only.
     */
    public static final String MEDIA_EDIT = "edit-media";

    /**
     * Link provides the URI that can be used to add a media entry to the feed.
     * This relation does not exist if the feed is read-only, not a media feed
     * or if resumable uploads are not supported.
     */
    public static final String RESUMABLE_CREATE_MEDIA =
        Namespaces.gPrefix + "resumable-create-media";

    /**
     * Link provides the URI that can be used to edit the media
     * associated with an entry in a resumable fashion.  This relation
     * does not exist if there is no associated media, the media is read-only
     * or resumable uploads are not supported.
     */
    public static final String RESUMABLE_EDIT_MEDIA =
        Namespaces.gPrefix + "resumable-edit-media";

    /**
     * Previous media edit link relation value that will temporarily be
     * supported to enable back compatibility for Picasa Web.  This rel
     * will be deleted after all usage has been migrated to use
     * {@link #MEDIA_EDIT}.
     * 
     * @deprecated use {@link Rel#MEDIA_EDIT} instead.
     */
    @Deprecated
    public static final String MEDIA_EDIT_BACKCOMPAT = "media-edit";

    /**
     * Link provides the URI that can be used to insert, update
     * and delete entries on this feed. This relation does not exist
     * if the feed is read-only or if batching not enabled on this
     * feed.
     */
    public static final String FEED_BATCH = Namespaces.gPrefix + "batch";

    /**
     * Link provides the URI that of link that provides the data
     * for the content in the feed.
     */
    public static final String VIA = "via";

    /**
     * Relation for links to enclosure (podcasting) files.
     */
    public static final String ENCLOSURE = "enclosure";
    
    /**
     * Relation for links that provide the URI of a hub that enables
     * registration for real-time updates to the resource.
     */
    public static final String HUB = "hub";

    private Rel() {}
  }

  /**
   * The Type class contains several common link content types.
   */
  public static final class Type {

    /** Defines the link type used for Atom content. */
    public static final String ATOM = ContentType.ATOM.getMediaType();


    /** Defines the link type used for HTML content. */
    public static final String HTML = ContentType.TEXT_HTML.getMediaType();
    
    private Type() {}
  }

  
  /**
   * Returns the link relation type.  Common values are defined in the
   * {@link Rel} class.
   * 
   * @see Rel
   */
  public String getRel();
  
  /**
   * Sets the link relation type.
   */
  public void setRel(String rel);

  /**
   * Returns the mime type of the link.
   */
  public String getType();

  /**
   * Sets the mime type of the link.
   */
  public void setType(String type);
}
