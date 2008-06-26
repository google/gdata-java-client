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
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;
import com.google.gdata.data.Source;
import com.google.gdata.data.TextConstruct;

import java.util.List;
import java.util.Set;

/**
 * A common interface for all of the methods that match between entry and feed.
 * This makes it possible to treat feeds and entries as being representative
 * of the same basic information, and allows retrieving/setting this information
 * in a consistent fashion.  Each method includes links to the {@link BaseEntry}
 * or {@link Source} methods they forward to.
 *
 * 
 */
public interface AtomData extends Extensible {

  /**
   * Get the id of the piece of data you're looking at.  This is the atom:id
   * and represents the atom-level identification of the item. This is forwarded
   * to either {@link BaseEntry#getId()} or {@link Source#getId()}
   * 
   * @return the feed or entry id.
   */
  public String getId();

  /**
   * Set the feed or entry atom:id. Forwarded to either
   * {@link BaseEntry#setId(String)} or {@link Source#setId(String)}. 
   *
   * @param id the id of the data item.
   */
  public void setId(String id);

  /**
   * Gets the title of the entry or feed represented by this piece of data.
   * Forwards to {@link BaseEntry#getTitle()} or {@link Source#getTitle()}.
   * 
   * @return the title of the item.
   */
  public TextConstruct getTitle();

  /**
   * Set the title of the feed or entry.  Forwards to
   * {@link BaseEntry#setTitle(TextConstruct)} or
   * {@link Source#setTitle(TextConstruct)}.
   *
   * @param title the title of the data item.
   */
  public void setTitle(TextConstruct title);

  /**
   * Gets the description of the entry or feed.  This forwards to either
   * {@link BaseEntry#getSummary()} or {@link Source#getSubtitle()}.
   * 
   * @return the description of the entry or feed.
   */
  public TextConstruct getDescription();

  /**
   * Set the description of the item.  Forwards to
   * {@link BaseEntry#setSummary(TextConstruct)} or
   * {@link Source#setSubtitle(TextConstruct)}.
   */
  public void setDescription(TextConstruct description);

  /**
   * Gets the updated time on this entry or feed. Forwards to
   * {@link BaseEntry#getUpdated()} or {@link Source#getUpdated()}.
   * 
   * @return the updated date.
   */
  public DateTime getUpdated();

  /**
   * Set the updated date on the entry or feed.  Forwards to
   * {@link BaseEntry#setUpdated(DateTime)} or
   * {@link Source#setUpdated(DateTime)}.
   *
   * @param updated the updated date of the data item.
   */
  public void setUpdated(DateTime updated);

  /**
   * Gets the rights associated with this entry or feed.  Forwards to
   * {@link BaseEntry#getRights()} or {@link Source#getRights()}.
   * 
   * @return the rights of the data item.
   */
  public TextConstruct getRights();

  /**
   * Set the rights of the entry or feed. Forwards to
   * {@link BaseEntry#setRights(TextConstruct)} or
   * {@link Source#setRights(TextConstruct)}.
   *
   * @param rights the rights.
   */
  public void setRights(TextConstruct rights);

  /**
   * Gets a set of categories on the entry or feed.  Forwards to
   * {@link BaseEntry#getCategories()} or {@link Source#getCategories()}.
   * 
   * @return a modifiable set of categories.
   */
  public Set<Category> getCategories();

  /**
   * Gets a modifiable list of links on the entry or feed.  Forwards to
   * {@link BaseEntry#getLinks()} or {@link Source#getLinks()}.
   * 
   * @return a modifiable set of links.
   */
  public List<Link> getLinks();

  /**
   * Gets a modifiable list of authors on this entry or feed. Forwards to
   * {@link BaseEntry#getAuthors()} or {@link Source#getAuthors()}.
   * 
   * @return a modifiable list of people that were authors.
   */
  public List<Person> getAuthors();

  /**
   * Gets a modifiable list of contributors on this entry or feed. Forwards to
   * {@link BaseEntry#getContributors()} or {@link Source#getContributors()}.
   * 
   * @return a modifiable list of people that were contributors.
   */
  public List<Person> getContributors();
}
