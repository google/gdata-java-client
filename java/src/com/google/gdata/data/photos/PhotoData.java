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
import java.util.List;

/**
 * Base data object for photo feeds and entries, this interface contains the
 * methods that all photo objects must provide.  Note that some information
 * may not be available based on the source of the photo data.
 *
 * 
 */
public interface PhotoData extends GphotoData, PointData, BoxData, MediaData {

  /** The unqualified kind for a photo. */
  public static final String KIND = "photo";

  /** The fully qualified kind term for photos. */
  public static final String PHOTO_KIND = Namespaces.PHOTOS_PREFIX + KIND;

  /** A category object for photos.  All photo objects will have this set. */
  public static final Category PHOTO_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, PHOTO_KIND);

  /**
   * The version of the image.  This is the version of the image itself, and is
   * changed whenever the image content is changed (such as by rotation).
   *
   * @return the gphoto:version on the photo.
   */
  public Long getVersion() throws ServiceException;

  /**
   * Set the version of the photo.  Used by the server to set the version, this
   * is read only on the client.
   *
   * @param version the version of the photo.
   */
  public void setVersion(Long version);

  /**
   * The position of the photo in the album.  This can be used to order the
   * photo based on where the user has placed it within the album.
   *
   * @return the gphoto:position of the photo.
   */
  public Float getPosition() throws ServiceException;

  /**
   * Set the position of the photo.  This is the photo's position in the album
   * it is in.  This can be changed to place a photo between two other photos
   * by choosing a position between the other two positions.  Note that the
   * server may change the positions to provide more space between photos if
   * the limits of floating point rounding would cause errors, so the most
   * recent position should be used before modifying a photo.  Optimistic
   * concurrency will catch this, however.
   *
   * @param position the position of the photo in the album.
   */
  public void setPosition(Float position);

  /**
   * The id of the album the photo is on.
   *
   * @return the gphoto:albumId of the photo.
   */
  public String getAlbumId();

  /**
   * @return the access of the album that contains this photo.
   */
  public String getAlbumAccess();

  /**
   * Set the access for the album that contains this photo.
   *
   * @param access the access of the album.
   */
  public void setAlbumAccess(String access);

  /**
   * Set the albumId of the photo as a string, the album the photo is in.
   */
  public void setAlbumId(String albumId);

  /**
   * Sets the id of the album the photo is in.  If the albumid is changed the
   * photo will be moved to the new album (as long as the provided albumid is
   * valid).
   *
   * Set the albumId of the photo as a long, the album the photo is in.
   */
  public void setAlbumId(Long albumId);

  /**
   * The video status in case the photo entry is a video.
   *
   * @return the gphoto:videostatus of the video/photo.
   */
  public String getVideoStatus();

  /**
   * Set the video status of the photo entry.
   */
  public void setVideoStatus(String videoStatus);

  /**
   * The width of the photo in pixels.  This is the width of the original image
   * stored in the database, but not necessarily the width of the image returned
   * by querying the image url (depending on image size).
   *
   * @return the gphoto:width of the photo.
   */
  public Long getWidth() throws ServiceException;

  /**
   * Set the width of the photo.  Used by the server, this has no effect on
   * the client.
   *
   * @param width the width of the photo.
   */
  public void setWidth(Long width);

  /**
   * The height of the photo in pixels.  This is the height of the original
   * image stored in the database, but not necessarily the height of the image
   * returned by querying the image url (depending on image size).
   *
   * @return the gphoto:height of the photo.
   */
  public Long getHeight() throws ServiceException;

  /**
   * Set the height of the photo.  Used by the server, this has no effect on
   * the client.
   *
   * @param height the height of the photo.
   */
  public void setHeight(Long height);

  /**
   * The rotation of the photo.  This is only useful when downloading the
   * original photo as the rotation is taken into account on thumbnails and the
   * image is always provided in the right orientation.
   *
   * @return the gphoto:rotation of the photo.
   */
  public Integer getRotation() throws ServiceException;

  /**
   * Set the rotation in degrees of the photo.  This can be used to rotate
   * the photo on the server.
   *
   * @param rotation the rotation of the photo.
   */
  public void setRotation(Integer rotation);

  /**
   * The size of the photo in bytes.  Read only, calculated by the server.
   *
   * @return the gphoto:size of the photo.
   */
  public Long getSize() throws ServiceException;

  /**
   * Set the size of the photo.  This is used on the server, setting this on
   * the client will have no effect.
   *
   * @param size the size of the photo in bytes.
   */
  public void setSize(Long size);

  /**
   * The client string stored on the photo.  Clients can use this to provide
   * additional information about the photo that they can use later.
   *
   * @return the gphoto:client of the photo.
   */
  public String getClient();

  /**
   * Set the client string on the photo.
   *
   * @param client the client that created the photo.
   */
  public void setClient(String client);

  /**
   * The checksum on the photo.  This is another piece of client-provided
   * information that will be stored on the server and returned to the client.
   *
   * @return the gphoto:checksum of the photo.
   */
  public String getChecksum();

  /**
   * Set the checksum of the photo.  Returned by the server, this is an opaque
   * string and will not be used by the server.
   *
   * @param checksum the checksum on the photo, computed by a client.
   */
  public void setChecksum(String checksum);

  /**
   * The time the photo was taken.  Modifiable by the client.
   *
   * @return the gphoto:timestamp of the photo.
   */
  public Date getTimestamp() throws ServiceException;

  /**
   * Set the timestamp on the photo.  This is the time the photo itself was
   * taken.
   *
   * @param timestamp the timestamp on the photo.
   */
  public void setTimestamp(Date timestamp);

  /**
   * The exif information on the photo.  See the documentation for
   * {@link ExifTags} for more information on available exif fields.
   *
   * @return the exif tags for the photo.
   */
  public ExifTags getExifTags();

  /**
   * Sets the exif tags for the photo.  This will override existing exif data
   * that was calculated when the photo was first uploaded.
   *
   * @param tags the exif tags for the photo, a collection of exif tags.
   */
  public void setExifTags(ExifTags tags);

  /**
   * Gets the status of the commenting preference on the photo.  If comments
   * are not enabled on the photo no comments may be sent.
   *
   * @return true if comments are enabled on the photo.
   */
  public Boolean getCommentsEnabled() throws ServiceException;

  /**
   * Sets whether comments can be made on the photo.  The client can change
   * whether comments are allowed as long as they are authenticated as the owner
   * of the photo.
   *
   * @param commentsEnabled true if comments are enabled in the photo.
   */
  public void setCommentsEnabled(Boolean commentsEnabled);

  /**
   * The number of comments on this photo.
   *
   * @return the comment count on the photo.
   */
  public Integer getCommentCount() throws ServiceException;

  /**
   * Set the number of comments on the photo.  Used by the server, this field
   * is calculated and cannot be modified by the client.
   *
   * @param commentCount the number of comments on the photo.
   */
  public void setCommentCount(Integer commentCount);

  /**
   * @return the number of views for this photo.
   */
  public Long getViewCount();

  /**
   * Sets the view count for this photo.
   * @param viewCount the number of views for this photo.
   */
  public void setViewCount(Long viewCount);
  
  /**
   * @return date that the photo was featured.
   */
  public Date getFeaturedDate();
   
  /**
   * Sets the date that the photo was featured.
   * @param featuredDate the date that the photo was featured.
   */
  public void setFeaturedDate(Date featuredDate);
  
  /**
   * Stream ids can be used to specify additional information about where a
   * photo came from, or where it is being used.  For example, a client may
   * mark each photo they edit with a streamid so that the list of edited
   * photos can be queried efficiently.
   *
   * @return a list of streamIds associated with this photo.
   */
  public List<String> getStreamIds();
  
  /**
   * Add an individual streamId to the photo.  The streamid is an opaque string
   * stored along with the photo and can be used to identify where a photo is
   * from or used.
   */
  public void addStreamId(String streamId);

  /**
   * If this photo is starred by current user.
   *
   * @return if this photo is starred by current user.
   */
  public Boolean isStarred();

  /**
   * Sets if this photo is starred by current user.
   *
   * @param starred If this photo is starred by current user.
   */
  public void setStarred(Boolean starred);

  /**
   * The number of users who starred this photo.
   *
   * @return the number of users who starred this photo.
   */
  public Integer getTotalStars();

  /**
   * Sets the number of users who starred this photo.
   *
   * @param totalStars the number of users who starred this photo.
   */
  public void setTotalStars(Integer totalStars);

}
