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


package com.google.gdata.data.photos.impl;

import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.geo.impl.BoxDataImpl;
import com.google.gdata.data.geo.impl.PointDataImpl;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumData;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoBytesUsed;
import com.google.gdata.data.photos.GphotoCommentCount;
import com.google.gdata.data.photos.GphotoCommentsEnabled;
import com.google.gdata.data.photos.GphotoLocation;
import com.google.gdata.data.photos.GphotoName;
import com.google.gdata.data.photos.GphotoNickname;
import com.google.gdata.data.photos.GphotoPhotosLeft;
import com.google.gdata.data.photos.GphotoPhotosUsed;
import com.google.gdata.data.photos.GphotoTimestamp;
import com.google.gdata.data.photos.GphotoUsername;
import com.google.gdata.data.photos.pheed.PheedImageUrl;
import com.google.gdata.data.photos.pheed.PheedThumbnail;

import java.util.Date;
import java.util.List;

/**
 * Implementation class for album data objects.  This class takes an
 * {@link ExtensionPoint} and uses it to provide all of the methods that
 * {@link AlbumData} specifies.  These methods are handled by using
 * extension classes to retrieve or set extensions of the appropriate type.
 *
 * 
 */
public class AlbumDataImpl extends GphotoDataImpl implements AlbumData {

  private final PointDataImpl pointData;
  private final BoxDataImpl boundingBoxData;
  private final MediaDataImpl mediaData;
  
  /**
   * Construct a new implementation of AlbumGphotoData with the given
   * extension point as the backing storage for data.
   */
  public AlbumDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
    pointData = new PointDataImpl(extensionPoint);
    boundingBoxData = new BoxDataImpl(extensionPoint);
    mediaData = new MediaDataImpl(extensionPoint);
  }

  /*
   * Declare the extensions that album objects use.
   */
  @Override
  @SuppressWarnings("deprecation")
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    
    declare(extProfile, PheedThumbnail.getDefaultDescription());
    declare(extProfile, PheedImageUrl.getDefaultDescription());

    declare(extProfile, GphotoName.getDefaultDescription(false, false));
    declare(extProfile, GphotoLocation.getDefaultDescription(false, false));
    declare(extProfile, GphotoTimestamp.getDefaultDescription(false, false));
    declare(extProfile, GphotoAccess.getDefaultDescription(false, false));
    declare(extProfile, GphotoPhotosUsed.getDefaultDescription(false, false));
    declare(extProfile, GphotoPhotosLeft.getDefaultDescription(false, false));
    declare(extProfile, GphotoBytesUsed.getDefaultDescription(false, false));

    declare(extProfile, GphotoUsername.getDefaultDescription(false, false));
    declare(extProfile, GphotoNickname.getDefaultDescription(false, false));

    declare(extProfile,
        GphotoCommentsEnabled.getDefaultDescription(false, false));
    declare(extProfile,
        GphotoCommentCount.getDefaultDescription(false, false));
    
    pointData.declareExtensions(extProfile);
    boundingBoxData.declareExtensions(extProfile);
    mediaData.declareExtensions(extProfile);
    
  }

  /**
   * Returns the photo:thumbnail element for the album.
   * 
   * @return the photo:thumbnail on the entry.
   * @deprecated use the media:thumbnail element to get thumbnails.
   */
  @Deprecated
  public String getThumbnail() {
    return getSimpleValue(PheedThumbnail.class);
  }

  /**
   * Set the thumbnail url for use in the photo:thumbnail element.
   *
   * @param thumbUrl the full url to the thumbnail.
   * @deprecated use the media:thumbnail element to set thumbnails.
   */
  @Deprecated
  public void setThumbnail(String thumbUrl) {
    if (thumbUrl != null) {
      setExtension(new PheedThumbnail(thumbUrl));
    } else {
      removeExtension(PheedThumbnail.class);
    }
  }

  /**
   * @return the photo:imgsrc on the entry.
   * @deprecated use the media:content element to get the image source.
   */
  @Deprecated
  public String getImageUrl() {
    return getSimpleValue(PheedImageUrl.class);
  }

  /**
   * Set the full image url for use in the photo:imgsrc element.
   *
   * @param imageUrl the full url to the image.
   * @deprecated set the media:content element with the image source.
   */
  @Deprecated
  public void setImageUrl(String imageUrl) {
    if (imageUrl != null) {
      setExtension(new PheedImageUrl(imageUrl));
    } else {
      removeExtension(PheedImageUrl.class);
    }
  }

  /**
   * @return the (canonical) name of the album this entry represents.
   */
  public String getName() {
    return getSimpleValue(GphotoName.class);
  }

  /**
   * Set the name for the album this entry represents.
   *
   * @param name the canonical name of the album.
   */
  public void setName(String name) {
    if (name != null) {
      setExtension(new GphotoName(name));
    } else {
      removeExtension(GphotoName.class);
    }
  }

  /**
   * @return the location of the album this entry represents.
   */
  public String getLocation() {
    return getSimpleValue(GphotoLocation.class);
  }

  /**
   * Set the location for the album this entry represents.
   *
   * @param location the location of the album.
   */
  public void setLocation(String location) {
    if (location != null) {
      setExtension(new GphotoLocation(location));
    } else {
      removeExtension(GphotoLocation.class);
    }
  }

  /**
   * Gets the date on the album, this is the date set by the user.
   */
  public Date getDate() {
    GphotoTimestamp ext = getExtension(GphotoTimestamp.class);
    return ext == null ? null : new Date(ext.getValue());
  }

  /**
   * Sets the date on the album, this is the user-defined date.
   */
  public void setDate(Date date) {
    if (date != null) {
      setExtension(new GphotoTimestamp(date));
    } else {
      removeExtension(GphotoTimestamp.class);
    }
  }

  /**
   * @return the access of the album this entry represents.
   */
  public String getAccess() {
    GphotoAccess access = getExtension(GphotoAccess.class);
    return access == null ? null : access.getValue().toLowerCase();
  }

  /**
   * Set the access for the album this entry represents.
   *
   * @param access the access of the album.
   */
  public void setAccess(String access) {
    if (access != null) {
      setExtension(new GphotoAccess(access));
    } else {
      removeExtension(GphotoAccess.class);
    }
  }

  /**
   * @return the number of photos used in the album this entry represents.
   */
  public Integer getPhotosUsed() {
    GphotoPhotosUsed photosUsed = getExtension(GphotoPhotosUsed.class);
    return photosUsed == null ? null : photosUsed.getValue();
  }

  /**
   * Set the number of photos used on the album this entry represents.
   *
   * @param photosUsed the number of photos used.
   */
  public void setPhotosUsed(Integer photosUsed) {
    if (photosUsed != null) {
      setExtension(new GphotoPhotosUsed(photosUsed));
    } else {
      removeExtension(GphotoPhotosUsed.class);
    }
  }

  /**
   * @return the number of photos remaining in the album this entry represents.
   */
  public Integer getPhotosLeft() {
    GphotoPhotosLeft left = getExtension(GphotoPhotosLeft.class);
    return left == null ? null : left.getValue();
  }

  /**
   * Set the number of photos remaining in the album this entry represents.
   *
   * @param photosLeft the number of photos left.
   */
  public void setPhotosLeft(Integer photosLeft) {
    if (photosLeft != null) {
      setExtension(new GphotoPhotosLeft(photosLeft));
    } else {
      removeExtension(GphotoPhotosLeft.class);
    }
  }

  /**
   * @return the number of bytes used in the album this entry represents.
   */
  public Long getBytesUsed() {
    GphotoBytesUsed used = getExtension(GphotoBytesUsed.class);
    return used == null ? null : used.getValue();
  }

  /**
   * Set the number of bytes used in the album this entry represents.
   *
   * @param bytesUsed the number of bytes used.
   */
  public void setBytesUsed(Long bytesUsed) {
    if (bytesUsed != null) {
      setExtension(new GphotoBytesUsed(bytesUsed));
    } else {
      removeExtension(GphotoBytesUsed.class);
    }
  }

  /**
   * @return the username of the owner of the album this entry represents.
   */
  public String getUsername() {
    return getSimpleValue(GphotoUsername.class);
  }

  /**
   * Set the username for the owner of the album this entry represents.
   *
   * @param username the username of the owner.
   */
  public void setUsername(String username) {
    if (username != null) {
      setExtension(new GphotoUsername(username));
    } else {
      removeExtension(GphotoUsername.class);
    }
  }

  /**
   * @return the nickname of the owner of the album this entry represents.
   */
  public String getNickname() {
    return getSimpleValue(GphotoNickname.class);
  }

  /**
   * Set the nickname for the owner of the album this entry represents.
   *
   * @param nickname the nickname of the owner.
   */
  public void setNickname(String nickname) {
    if (nickname != null) {
      setExtension(new GphotoNickname(nickname));
    } else {
      removeExtension(GphotoNickname.class);
    }
  }

  /**
   * @return true if comments are enabled in this album the entry represents.
   */
  public Boolean getCommentsEnabled() {
    GphotoCommentsEnabled left = getExtension(GphotoCommentsEnabled.class);
    return left == null ? null : left.getValue();
  }

  /**
   * Set the whether comments are enabled in the album this entry represents.
   *
   * @param commentsEnabled true if comments are enabled in the album.
   */
  public void setCommentsEnabled(Boolean commentsEnabled) {
    if (commentsEnabled != null) {
      setExtension(new GphotoCommentsEnabled(commentsEnabled));
    } else {
      removeExtension(GphotoCommentsEnabled.class);
    }
  }

  /**
   * @return the comment count on the album this entry represents.
   */
  public Integer getCommentCount() {
    GphotoCommentCount left = getExtension(GphotoCommentCount.class);
    return left == null ? null : left.getValue();
  }

  /**
   * Set the number of comments on the album this entry represents.
   *
   * @param commentCount the number of comments on the album.
   */
  public void setCommentCount(Integer commentCount) {
    if (commentCount != null) {
      setExtension(new GphotoCommentCount(commentCount));
    } else {
      removeExtension(GphotoCommentCount.class);
    }
  }
  
  /*
   * These delegate to the backing geo data.
   */
  public void setGeoLocation(Double lat, Double lon) {
    pointData.setGeoLocation(lat, lon);
  }
  
  public void setGeoLocation(Point point) {
    pointData.setGeoLocation(point);
  }
  
  public Point getGeoLocation() {
    return pointData.getGeoLocation();
  }
  
  public Box getGeoBoundingBox() {
    return boundingBoxData.getGeoBoundingBox();
  }

  public void setGeoBoundingBox(Point lowerLeft, Point upperRight) {
    boundingBoxData.setGeoBoundingBox(lowerLeft, upperRight);
  }

  public void setGeoBoundingBox(Box boundingBox) {
    boundingBoxData.setGeoBoundingBox(boundingBox);
  }
  
  public void clearPoint() {
    pointData.clearPoint();
  }

  public void clearGeoBoundingBox() {
    boundingBoxData.clearGeoBoundingBox();
  }

  /*
   * These delegate to the backing media data.
   */
  public MediaGroup getMediaGroup() {
    return mediaData.getMediaGroup();
  }
  
  public List<MediaContent> getMediaContents() {
    return mediaData.getMediaContents();
  }

  public List<MediaCategory> getMediaCategories() {
    return mediaData.getMediaCategories();
  }
  
  public List<MediaCredit> getMediaCredits() {
    return mediaData.getMediaCredits();
  }
  
  public List<MediaThumbnail> getMediaThumbnails() {
    return mediaData.getMediaThumbnails();
  }

  public MediaKeywords getMediaKeywords() {
    return mediaData.getMediaKeywords();
  }
  
  public void setKeywords(MediaKeywords keywords) {
    mediaData.setKeywords(keywords);
  }
}
