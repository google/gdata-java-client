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

import javax.annotation.Nullable;

/**
 * Base interface for Atom resource types.  Contains a common set of methods
 * across both entries and feeds and a can also be used as a parameter type in
 * contexts where either a feed or an entry is acceptable.
 *
 * @see IEntry
 * @see IFeed
 */
public interface IAtom {

  /**
   * Returns the list of all authors on this resource.
   */
  public List<? extends IPerson> getAuthors();

  /**
   * Returns a set of categories on this resource.
   */
  public Set<? extends ICategory> getCategories();

  /**
   * Get the unique id for this resource.  Represents the atom:id element.
   */
  public String getId();

  /**
   * Sets the unique id for this resource.
   */
  public void setId(String id);

  /**
   * Get a {@link DateTime} instance representing the last time this resource
   * was updated.  Represents the atom:updated element.
   */
  public DateTime getUpdated();

  /**
   * Sets the last time this resource was updated.
   */
  public void setUpdated(DateTime updated);

  /**
   * Returns a list of atom:link elements on this resource.  If there are no
   * links, an empty list will be returned.
   */
  public List<? extends ILink> getLinks();

  /**
   * Returns a particular atom:link element with the given rel and type, or null
   * if one was not found.
   */
  public ILink getLink(String rel, String type);

  /**
   * Adds a link with the given rel, type, and href.
   */
  public ILink addLink(String rel, String type, String href);

  /**
   * Remove all links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch {@code rel} value to match or {@code null} to match any
   *     {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *     {@code type} value.
   */
  public void removeLinks(String relToMatch, String typeToMatch);
  
  /**
   * Removes all links from the this resource.
   */
  public void removeLinks();

  /**
   * Returns the self link for the resource.
   */
  public ILink getSelfLink();

  /**
   * Returns the atom:title element of this resource.
   */
  public ITextConstruct getTitle();

  /**
   * Gets the value of the gd:etag attribute for this resource.
   *
   * See RFC 2616, Section 3.11.
   */
  public String getEtag();

  /**
   * Sets the value of the gd:etag attribute for this resource.
   */
  public void setEtag(String etag);

  /**
   * Returns the value of the gd:kind attribute for this resource.  Returns
   * {@code null} if the kind attribute is missing.
   */
  public String getKind();
  
  /**
   * Sets the value of the gd:kind attribute for this resource.  A value of
   * {@code null} will remove the kind attribute.
   */
  public void setKind(String kind);

  /**
   * Version ID. This is a unique number representing this particular
   * resource. Every update changes the version ID (unless the update
   * doesn't modify anything, in which case it's permissible for
   * version ID to stay the same). Services are free to interpret this
   * string in the most convenient way. Some services may choose to use
   * a monotonically increasing sequence of version IDs. Other services
   * may compute a hash of entry properties or feed content and use that.
   * <p>
   * This property is only used for services to communicate the current
   * version ID back to the servlet. It is NOT set when resources are
   * parsed (either from requests or from arbitrary XML).
   */
  public String getVersionId();

  /**
   * Sets the versionId.  See {@link #getVersionId()} for a description of what
   * the versionId is used for.
   */
  public void setVersionId(String versionId);


  /**
   * Sets the service that this resource is being used with.
   */
  public void setService(Service s);
}
