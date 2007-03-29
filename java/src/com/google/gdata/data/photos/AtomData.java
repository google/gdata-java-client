/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;
import com.google.gdata.data.TextConstruct;

import java.util.List;
import java.util.Set;

/**
 * A common interface for all of the methods that match between entry and feed.
 * This makes it possible to treat feeds and entries as being representative
 * of the same basic information, and allows retrieving/setting this information
 * in a consistent fashion.
 *
 * 
 */
public interface AtomData extends Extensible {

  /**
   * @return the feed or entry id.
   */
  public String getId();

  /**
   * Set the feed or entry id.
   *
   * @param id the id of the data item.
   */
  public void setId(String id);

  /**
   * @return the title of the item.
   */
  public TextConstruct getTitle();

  /**
   * Set the title of the item.
   *
   * @param title the title of the data item.
   */
  public void setTitle(TextConstruct title);

  /**
   * @return the description of the item.
   */
  public TextConstruct getDescription();

  /**
   * Set the description of the item.  Feeds will set subtitle, entries
   * will set summary.
   */
  public void setDescription(TextConstruct description);

  /**
   * @return the updated date.
   */
  public DateTime getUpdated();

  /**
   * Set the updated date.
   *
   * @param updated the updated date of the data item.
   */
  public void setUpdated(DateTime updated);

  /**
   * @return the rights of the data item.
   */
  public TextConstruct getRights();

  /**
   * Set the rights of the data item.
   *
   * @param rights the rights.
   */
  public void setRights(TextConstruct rights);

  /**
   * @return a modifiable set of categories.
   */
  public Set<Category> getCategories();

  /**
   * @return a modifiable set of links.
   */
  public List<Link> getLinks();

  /**
   * @return a modifiable list of people that were authors.
   */
  public List<Person> getAuthors();

  /**
   * @return a modifiable list of people that were contributors.
   */
  public List<Person> getContributors();
}
