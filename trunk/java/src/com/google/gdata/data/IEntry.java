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

/**
 * Shared interface for model and data BaseEntry to implement.
 *
 * 
 */
public interface IEntry extends IAtom {

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
   * Returns the atom:summary element of this entry.
   */
  public ITextConstruct getSummary();

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
   * Gets the resumable-edit-media link, which is the link to PUT an updated
   * version of the media content to using Resumable Upload IO.  Will return
   * null if the media-edit link does not exist.
   */
  public ILink getResumableEditMediaLink();

  /**
   * Returns the set of selected fields for the entry if the entry contains a
   * partial representation or {@code null} if it is a full representation.
   */
  public String getSelectedFields();

  /**
   * Sets the current fields selection for this partial entry.  A
   * value of {@code null} indicates the entry is not a partial entry.
   */
  public void setSelectedFields(String fields);
}
