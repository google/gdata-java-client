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
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.util.ServiceException;

import java.util.Date;

/**
 * Base data object for photo feeds and entries, this interface contains the
 * methods that all photo objects must provide.
 *
 * In the future, this class will also provide access to the EXIF data of the
 * photo, but this won't be available until a future release.
 *
 * 
 */
public interface PhotoData extends GphotoData, PointData {

  /** The category name for photos. */
  public static final String PHOTO_KIND = Namespaces.PHOTOS_PREFIX + "photo";

  /** A category object for photos.  All photo objects will have this set. */
  public static final Category PHOTO_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, PHOTO_KIND);

  /**
   * @return the gphoto:version on the photo.
   */
  public Long getVersion() throws ServiceException;

  /**
   * Set the version of the photo.
   *
   * @param version the version of the photo.
   */
  public void setVersion(Long version);

  /**
   * @return the gphoto:position of the photo.
   */
  public Float getPosition() throws ServiceException;

  /**
   * Set the position of the photo.  This is the photo's position in the album
   * it is in (in the context of the request).
   *
   * @param position the position of the photo in the album.
   */
  public void setPosition(Float position);

  /**
   * @return the gphoto:albumId of the photo.
   */
  public String getAlbumId();

  /**
   * Set the albumId of the photo as a string, the album the photo is in.
   */
  public void setAlbumId(String albumId);

  /**
   * Set the albumId of the photo as a long, the album the photo is in.
   */
  public void setAlbumId(Long albumId);

  /**
   * @return the gphoto:width of the photo.
   */
  public Long getWidth() throws ServiceException;

  /**
   * Set the width of the photo.
   *
   * @param width the width of the photo.
   */
  public void setWidth(Long width);

  /**
   * @return the gphoto:height of the photo.
   */
  public Long getHeight() throws ServiceException;

  /**
   * Set the height of the photo.
   *
   * @param height the height of the photo.
   */
  public void setHeight(Long height);

  /**
   * @return the gphoto:rotation of the photo.
   */
  public Integer getRotation() throws ServiceException;

  /**
   * Set the rotation in degrees of the photo.
   *
   * @param rotation the rotation of the photo.
   */
  public void setRotation(Integer rotation);

  /**
   * @return the gphoto:size of the photo.
   */
  public Long getSize() throws ServiceException;

  /**
   * Set the size of the photo.
   *
   * @param size the size of the photo in bytes.
   */
  public void setSize(Long size);

  /**
   * @return the gphoto:client of the photo.
   */
  public String getClient();

  /**
   * Set the client of the photo.
   *
   * @param client the client that created the photo.
   */
  public void setClient(String client);

  /**
   * @return the gphoto:checksum of the photo.
   */
  public String getChecksum();

  /**
   * Set the checksum of the photo.
   *
   * @param checksum the checksum on the photo, computed by a client.
   */
  public void setChecksum(String checksum);

  /**
   * @return the gphoto:timestamp of the photo.
   */
  public Date getTimestamp() throws ServiceException;

  /**
   * Set the timestamp on the photo.
   *
   * @param timestamp the timestamp on the photo.
   */
  public void setTimestamp(Date timestamp);

  /**
   * @return the exif tags for the photo.
   */
  public ExifTags getExifTags();

  /**
   * Sets the exif tags for the photo.
   *
   * @param tags the exif tags for the photo, a collection of exif tags.
   */
  public void setExifTags(ExifTags tags);

  /**
   * @return true if comments are enabled on the photo.
   */
  public Boolean getCommentsEnabled() throws ServiceException;

  /**
   * Set the whether comments are enabled on the photo.
   *
   * @param commentsEnabled true if comments are enabled in the photo.
   */
  public void setCommentsEnabled(Boolean commentsEnabled);

  /**
   * @return the comment count on the photo.
   */
  public Integer getCommentCount() throws ServiceException;

  /**
   * Set the number of comments on the photo.
   *
   * @param commentCount the number of comments on the photo.
   */
  public void setCommentCount(Integer commentCount);

  /**
   * Helper method to get the keywords from the media:group element directly.
   *
   * @return the media:keywords that are the keywords on the entry.
   */
  public MediaKeywords getKeywords();

  /**
   * Set the keywords on the photo by adding these keywords to the media:group
   * element if it exists, or creating one if it doesn't.
   */
  public void setKeywords(MediaKeywords keywords);
    
}
