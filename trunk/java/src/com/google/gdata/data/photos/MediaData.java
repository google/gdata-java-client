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

import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;

import java.util.List;

/**
 * An interface for anything that supports mediarss content to implement,
 * provides helpful get and set methods for getting and setting mediarss
 * elements.  This takes some of the pain out of sticking data into the media
 * group nested element.  This interface is currently supported by the album
 * and photo data interfaces.
 * 
 * 
 */
public interface MediaData extends Extensible {

  /**
   * Retrieve the media:group element on this element.  This will return null
   * if there was no media:group element set.
   *
   * @return the media:group element on the element.
   */
  public MediaGroup getMediaGroup();

  /**
   * Retrieve the list of media:content elements inside the media:group on
   * this element.  If either the media:group or media:content elements are
   * missing then an empty list will be returned.
   * 
   * @return a list of media:content elements found on the element.
   */
  public List<MediaContent> getMediaContents();
  
  /**
   * Retrieve the list of media:category elements inside the media:group on
   * this element.  If either the media:group or media:category elements are
   * missing then an empty list will be returned.
   * 
   * 
   * @return a list of media:category elements found on the element.
   */
  public List<MediaCategory> getMediaCategories();
  
  /**
   * Retrieve the list of media:credit elements inside the media:group on
   * this element.  If either the media:group or media:credit elements are
   * missing then an empty list will be returned.
   * 
   * @return a list of media:credit elements found on this element.
   */
  public List<MediaCredit> getMediaCredits();
  
  /**
   * Retrieve the list of media:thumbnail elements inside the media:group on
   * this element.  If either the media:group or media:thumbnail elements are
   * missing then an empty list will be returned.
   * 
   * @return a list of media:thumbnail elements found on the element.
   */
  public List<MediaThumbnail> getMediaThumbnails();
  
  /**
   * Retrieve the list of media:keywords elements inside the media:group on
   * this element.  If either the media:group or media:keywords elements are
   * missing then {@code null} will be returned.
   *
   * @return the media:keywords elements found on the element, or {@code null}.
   */
  public MediaKeywords getMediaKeywords();

  /**
   * Set the media keywords on this element to the given keywords.  Will create
   * the media:group element as needed.
   */
  public void setKeywords(MediaKeywords keywords);
}
