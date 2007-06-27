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
import com.google.gdata.data.geo.PointData;
import com.google.gdata.util.ServiceException;

import java.util.Date;

/**
 * Data interface for album feeds and entries. This allows a common interface
 * between entries and feed that are about album kinds, making working with
 * these data objects easier.
 *
 * 
 */
public interface AlbumData extends GphotoData, PointData {

  /** The unqualified kind for an album. */
  public static final String KIND = "album";

  /** The fully qualified king term for albums. */
  public static final String ALBUM_KIND = Namespaces.PHOTOS_PREFIX + KIND;

  /** A category object for albums.  All album objects will have this set. */
  public static final Category ALBUM_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, ALBUM_KIND);

  /**
   * @return the (canonical) name of the album.
   */
  public String getName();

  /**
   * Set the name for the album this feed represents.
   *
   * @param name the canonical name of the album.
   */
  public void setName(String name);

  /**
   * @return the location of the album.
   */
  public String getLocation();

  /**
   * Set the location for the album this data object represents.
   *
   * @param location the location of the album.
   */
  public void setLocation(String location);

  /**
   * @return the date on the album, set by the user.
   */
  public Date getDate() throws ServiceException;

  /**
   * Set the date on the album.
   *
   * @param date the date on the album, set by the user.
   */
  public void setDate(Date date);

  /**
   * @return the access of the album.
   */
  public String getAccess();

  /**
   * Set the access for the album this data object represents.
   *
   * @param access the access of the album.
   */
  public void setAccess(String access);

  /**
   * @return the number of photos used in the album.
   */
  public Integer getPhotosUsed() throws ServiceException;

  /**
   * Set the number of photos used on the album this data object represents.
   *
   * @param photosUsed the number of photos used.
   */
  public void setPhotosUsed(Integer photosUsed);

  /**
   * @return the number of photos remaining in the album.
   */
  public Integer getPhotosLeft() throws ServiceException;

  /**
   * Set the number of photos remaining that can be uploaded to this album.
   *
   * @param photosLeft the number of photos left.
   */
  public void setPhotosLeft(Integer photosLeft);

  /**
   * @return the number of bytes used in the album.
   */
  public Long getBytesUsed() throws ServiceException;

  /**
   * Set the number of bytes used in the album this data object represents.
   *
   * @param bytesUsed the number of bytes used.
   */
  public void setBytesUsed(Long bytesUsed);

  /**
   * @return the username of the owner of the album.
   */
  public String getUsername();

  /**
   * Set the username for the owner of the album this data object represents.
   *
   * @param username the username of the owner.
   */
  public void setUsername(String username);

  /**
   * @return the nickname of the owner of the album.
   */
  public String getNickname();

  /**
   * Set the nickname for the owner of the album this data object represents.
   *
   * @param nickname the nickname of the owner.
   */
  public void setNickname(String nickname);

  /**
   * @return true if comments are enabled in this album the feed represents.
   */
  public Boolean getCommentsEnabled() throws ServiceException;

  /**
   * Set the whether comments are enabled in the album.
   *
   * @param commentsEnabled true if comments are enabled in the album.
   */
  public void setCommentsEnabled(Boolean commentsEnabled);

  /**
   * @return the comment count on the album.
   */
  public Integer getCommentCount() throws ServiceException;

  /**
   * Set the number of comments on the album this data object represents.
   *
   * @param commentCount the number of comments on the album.
   */
  public void setCommentCount(Integer commentCount);

}
