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

import com.google.gdata.data.Category;
import com.google.gdata.data.geo.BoxData;
import com.google.gdata.data.geo.PointData;
import com.google.gdata.util.ServiceException;

import java.util.Date;

/**
 * Data interface for album feeds and entries. This allows a common interface
 * between entries and feed that are about album kinds, making working with
 * these data objects easier.
 * 
 * An album may contain media in the form of the cover image for the album,
 * as well as containing geographic information in the form of coordinates.
 *
 * 
 */
public interface AlbumData extends GphotoData, PointData, BoxData, MediaData {

  /** The unqualified kind for an album. */
  public static final String KIND = "album";

  /** The fully qualified king term for albums. */
  public static final String ALBUM_KIND = Namespaces.PHOTOS_PREFIX + KIND;

  /** A category object for albums.  All album objects will have this set. */
  public static final Category ALBUM_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, ALBUM_KIND);

  /**
   * The name of the album is its canonicalized title.  This is the name that
   * can be used in urls to access the album by name rather than by id.
   * 
   * @return the (canonical) name of the album.
   */
  public String getName();

  /**
   * Set the canonical name of the album.  This is used on the server.
   * If you wish to change the title of the album you should instead use
   * {@link AtomData#setTitle(com.google.gdata.data.TextConstruct)}.
   *
   * @param name the canonical name of the album.
   */
  public void setName(String name);

  /**
   * The location of the album is a string representing where the photos in
   * the album where taken.
   * 
   * @return the location of the album.
   */
  public String getLocation();

  /**
   * Set the location string for where the photos in the album were taken.
   * This string may be geocoded by the server to provide coordinates based
   * on the location.
   *
   * @param location the location of the album.
   */
  public void setLocation(String location);

  /**
   * The date the album was taken.  Date ranges are not currently allowed.
   * This is typically set to the date the earliest photo in the album was 
   * taken.
   * 
   * @return the date on the album, set by the user.
   */
  public Date getDate() throws ServiceException;

  /**
   * Set the date of the album.  Dates that are too early will be truncated by
   * the server, where too early currently means anything before 1970.
   *
   * @param date the date the album represents.
   */
  public void setDate(Date date);

  /**
   * The access level of the album, either private, protected, or public.
   * This may be null when the access level of the album is not known.
   * 
   * @return the access of the album.
   */
  public String getAccess();

  /**
   * Set the access for the album this data object represents.  Valid values are
   * "private" or "public".  Anything other than "public" will be set as
   * "private", which is the default.
   *
   * @param access the access level of the album.
   */
  public void setAccess(String access);

  /**
   * The number of photos that are contained in this album.  This should be
   * treated as a read only field, it is calculated on the server.
   * 
   * @return the number of photos used in the album.
   */
  public Integer getPhotosUsed() throws ServiceException;

  /**
   * Set the number of photos used on the album this data object represents.
   * This is used by the server, changing this value on the client will have
   * no effect.
   *
   * @param photosUsed the number of photos used.
   */
  public void setPhotosUsed(Integer photosUsed);

  /**
   * The number of spaces for photos that are available in the album.  This
   * should be treated as a read only field as it is calculated on the server.
   * Once 0 photos are left no more photos can be uploaded to this album until
   * photos have been deleted.
   * 
   * @return the number of photos remaining in the album.
   */
  public Integer getPhotosLeft() throws ServiceException;

  /**
   * Set the number of photos remaining that can be uploaded to this album.
   * This is used by the server, changing this value on the client will have
   * no effect.
   *
   * @param photosLeft the number of photos left.
   */
  public void setPhotosLeft(Integer photosLeft);

  /**
   * The number of bytes that are used by photos in this album.  Useful to see
   * how much space each album is taking up of the user's quota.  This method
   * is calculated by the server so it should be considered read-only by the
   * client.
   * 
   * @return the number of bytes used in the album.
   */
  public Long getBytesUsed() throws ServiceException;

  /**
   * Set the number of bytes used in the album this data object represents.
   * This method is used by the server, setting this on the client will have no
   * effect.
   *
   * @param bytesUsed the number of bytes used.
   */
  public void setBytesUsed(Long bytesUsed);

  /**
   * The username of the owner of the album.  This username can be used to query
   * for the gallery of the owner of the album, for example.
   * 
   * @return the username of the owner of the album.
   */
  public String getUsername();

  /**
   * Set the username for the owner of the album this data object represents.
   * This is used by the server, setting it on the client will have no effect.
   * We currently do not support moving albums between users.
   *
   * @param username the username of the owner.
   */
  public void setUsername(String username);

  /**
   * The nickname of the user who owns the album.  This is provided to make it
   * easier to display ownership information about albums.  This is a read only
   * field calculated by the server based on the owner of the album.
   * 
   * @return the nickname of the owner of the album.
   */
  public String getNickname();

  /**
   * Set the nickname for the owner of the album this data object represents.
   * This is used by the server and has no effect on the client.
   *
   * @param nickname the nickname of the owner.
   */
  public void setNickname(String nickname);

  /**
   * Whether or not commenting is allowed on this album.  If set to true then
   * comments may be added to the album, if set to false then no comments can
   * be made on photos in this album.
   * 
   * @return true if comments are enabled in this album the feed represents.
   */
  public Boolean getCommentsEnabled() throws ServiceException;

  /**
   * Set the whether comments are enabled in the album.  This will change the
   * setting on the server if set by the owner of the album and updated.
   *
   * @param commentsEnabled true if comments are enabled in the album.
   */
  public void setCommentsEnabled(Boolean commentsEnabled);

  /**
   * The number of comments on the album.  This is calculated based on the
   * comments stored on the server.
   * 
   * @return the comment count on the album.
   */
  public Integer getCommentCount() throws ServiceException;

  /**
   * Set the number of comments on the album.  Used by the server, this has
   * no effect on the client.
   *
   * @param commentCount the number of comments on the album.
   */
  public void setCommentCount(Integer commentCount);

}
