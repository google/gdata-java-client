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

import com.google.gdata.client.Service;

import java.util.List;
import java.util.Set;

/**
 * Shared interface for model and data BaseEntry to implement.
 * 
 * 
 */
public interface IEntry {

  /**
   * Get the unique id for this entry.  Represents the atom:id element.
   */
  public String getId();

  /**
   * Sets the unique id for this entry.
   */
  public void setId(String id);

  /**
   * Get the title of this entry.  Represents the atom:title element.
   */
  public ITextConstruct getTitle();
  
  /**
   * Version ID. This is a unique number representing this particular
   * entry. Every update changes the version ID (unless the update
   * doesn't modify anything, in which case it's permissible for
   * version ID to stay the same). Services are free to interpret this
   * string in the most convenient way. Some services may choose to use
   * a monotonically increasing sequence of version IDs. Other services
   * may compute a hash of entry properties and use that.
   * <p>
   * This property is only used for services to communicate the current
   * version ID back to the servlet. It is NOT set when entries are
   * parsed (either from requests or from arbitrary XML).
   */
  public String getVersionId();
  
  /**
   * Sets the versionId.  See {@link #getVersionId()} for a description of what
   * the versionId is used for.
   */
  public void setVersionId(String versionId);
  
  /**
   * Get a {@link DateTime} instance representing the last time this entry was
   * updated.  Represents the atom:updated element.
   */
  public DateTime getUpdated();
  
  /**
   * Sets the last time this entry was updated.
   */
  public void setUpdated(DateTime updated);
  
  /**
   * Get a {@link DateTime} instance representing the last time this entry was
   * edited.  Represents the app:edited element.
   */
  public DateTime getEdited();

  /**
   * Set the last time this entry was edited using the app:edited element.
   */
  public void setEdited(DateTime edited);

  /**
   * Get a {@link DateTime} instance representing the time that this entry was
   * created.  Represents the atom:published element.
   */
  public DateTime getPublished();
  
  /**
   * Sets the date of publishing for this entry.  Used on the server to specify
   * when the entry was created.
   */
  public void setPublished(DateTime published);

  /**
   * Gets the value of the gd:etag attribute for this entry.
   * 
   * See RFC 2616, Section 3.11.
   */
  public String getEtag();
  
  /**
   * Sets the value of the gd:etag attribute for this entry.
   */
  public void setEtag(String etag);

  /**
   * Returns {@code true} if the entry can be modified by a client.
   */
  public boolean getCanEdit();
  
  /**
   * Sets whether the server allows this entry to be modified by the client.
   */
  public void setCanEdit(boolean canEdit);
  
  /**
   * Gets the content of this entry.  Represents the atom:content element.
   */
  public IContent getContent();

  /**
   * Sets the service that this entry is being used with.
   */
  public void setService(Service s);

  /**
   * Gets the self link, which points back at this entry. Useful for retrieving
   * the latest version of an entry.  Will return null if no self link is
   * available.
   */
  public ILink getSelfLink();
  
  /**
   * Gets the edit link, which is the link to PUT an updated version of the
   * entry to.  Will return null if no edit link is available.
   */
  public ILink getEditLink();
  
  /**
   * Gets the media-edit link, which is the link to PUT an updated version of
   * the media content to.  Will return null if the media-edit link does not
   * exist.
   */
  public ILink getMediaEditLink();
  
  /**
   * Retrieves the first link with the supplied {@code rel} and/or
   * {@code type} value.
   * 
   * <p>If either parameter is {@code null}, it will not return matches
   * for that paramter.
   * 
   * @param rel link relation.
   * @param type link type.
   * @return first matching link.
   */
  public ILink getLink(String rel, String type);
  
  /**
   * Gets a list containing all links on this entry.
   */
  public List<? extends ILink> getLinks();
  
  /**
   * Creates and adds a link to the entry.
   * 
   * @param rel the value of the "rel" attribute on the link
   * @param type the value of the "type" attribute on the link
   * @param href the value of the "href" attribute on the link
   * @return the link that was created
   */
  public ILink addLink(String rel, String type, String href);

  /**
   * Gets a list of all authors on this entry.
   * 
   * @return a list of authors for the entry.
   */
  public List<? extends IPerson> getAuthors();
  
  /**
   * Gets a set of all categories on this entry.
   * 
   * @return a set of categories for the entry.
   */
  public Set<? extends ICategory> getCategories();
}
