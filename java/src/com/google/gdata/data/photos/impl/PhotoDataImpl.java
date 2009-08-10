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
import com.google.gdata.data.photos.ExifTags;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoAlbumId;
import com.google.gdata.data.photos.GphotoChecksum;
import com.google.gdata.data.photos.GphotoClient;
import com.google.gdata.data.photos.GphotoCommentCount;
import com.google.gdata.data.photos.GphotoCommentsEnabled;
import com.google.gdata.data.photos.GphotoFeaturedDate;
import com.google.gdata.data.photos.GphotoHeight;
import com.google.gdata.data.photos.GphotoPosition;
import com.google.gdata.data.photos.GphotoRotation;
import com.google.gdata.data.photos.GphotoSize;
import com.google.gdata.data.photos.GphotoStarred;
import com.google.gdata.data.photos.GphotoStreamId;
import com.google.gdata.data.photos.GphotoTimestamp;
import com.google.gdata.data.photos.GphotoVersion;
import com.google.gdata.data.photos.GphotoVideoStatus;
import com.google.gdata.data.photos.GphotoViewCount;
import com.google.gdata.data.photos.GphotoWidth;
import com.google.gdata.data.photos.PhotoData;
import com.google.gdata.data.photos.pheed.PheedImageUrl;
import com.google.gdata.data.photos.pheed.PheedThumbnail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation class for photo data objects.  This class takes an
 * {@link ExtensionPoint} and uses it to provide all of the methods that
 * {@link PhotoData} specifies.  These methods are handled by using
 * extension classes to retrieve or set extensions of the appropriate type.
 *
 * 
 */
public class PhotoDataImpl extends GphotoDataImpl implements PhotoData {

  private final PointDataImpl pointData;
  private final BoxDataImpl boundingBoxData;
  private final MediaDataImpl mediaData;

  /**
   * Construct a new implementation of PhotoGphotoData with the given extension
   * point as the backing storage for data.
   */
  public PhotoDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
    pointData = new PointDataImpl(extensionPoint);
    boundingBoxData = new BoxDataImpl(extensionPoint);
    mediaData = new MediaDataImpl(extensionPoint);
  }

  /*
   * Declare all of the extensions on the photo data.
   */
  @Override
  @SuppressWarnings("deprecation")
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    declare(extProfile, PheedThumbnail.getDefaultDescription());
    declare(extProfile, PheedImageUrl.getDefaultDescription());

    declare(extProfile, GphotoVersion.getDefaultDescription(false, false));
    declare(extProfile, GphotoPosition.getDefaultDescription(false, false));
    declare(extProfile, GphotoWidth.getDefaultDescription(false, false));
    declare(extProfile, GphotoHeight.getDefaultDescription(false, false));
    declare(extProfile, GphotoRotation.getDefaultDescription(false, false));
    declare(extProfile, GphotoSize.getDefaultDescription(false, false));

    declare(extProfile, GphotoAlbumId.getDefaultDescription(false, false));
    declare(extProfile, GphotoAccess.getDefaultDescription(false, false));
    declare(extProfile, GphotoClient.getDefaultDescription(false, false));
    declare(extProfile, GphotoChecksum.getDefaultDescription(false, false));
    declare(extProfile, GphotoTimestamp.getDefaultDescription(false, false));
    declare(extProfile, GphotoStreamId.getDefaultDescription(false, false));

    declare(extProfile, GphotoVideoStatus.getDefaultDescription(false, false));

    declare(extProfile, ExifTags.getDefaultDescription());
    new ExifTags().declareExtensions(extProfile);

    declare(extProfile,
        GphotoCommentsEnabled.getDefaultDescription(false, false));
    declare(extProfile, GphotoCommentCount.getDefaultDescription(false, false));

    pointData.declareExtensions(extProfile);
    boundingBoxData.declareExtensions(extProfile);
    mediaData.declareExtensions(extProfile);
  }

  /**
   * @return the gphoto:version on the item.
   */
  public Long getVersion() {
    GphotoVersion ext = getExtension(GphotoVersion.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the version of the photo this item is about.
   *
   * @param version the version of the photo.
   */
  public void setVersion(Long version) {
    if (version != null) {
      setExtension(new GphotoVersion(version));
    } else {
      removeExtension(GphotoVersion.class);
    }
  }

  /**
   * @return the gphoto:position of the photo.
   */
  public Float getPosition() {
    GphotoPosition ext = getExtension(GphotoPosition.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the position of the photo this item is about.  This is the photo's
   * position in the album it is in (in the context of the feed).
   *
   * @param position the position of the photo in the album.
   */
  public void setPosition(Float position) {
    if (position != null) {
      setExtension(new GphotoPosition(position));
    } else {
      removeExtension(GphotoPosition.class);
    }
  }

  /**
   * @return the gphoto:albumId of the photo.
   */
  public String getAlbumId() {
    return getSimpleValue(GphotoAlbumId.class);
  }

  /**
   * Sets the album the photo is in.
   */
  public void setAlbumId(String albumId) {
    if (albumId != null) {
      setExtension(new GphotoAlbumId(albumId));
    } else {
      removeExtension(GphotoAlbumId.class);
    }
  }

  /**
   * Sets the album the photo is in.
   */
  public void setAlbumId(Long albumId) {
    if (albumId != null) {
      setExtension(new GphotoAlbumId(albumId.toString()));
    } else {
      removeExtension(GphotoAlbumId.class);
    }
  }

  /**
   * @return the access of the album that contains this photo.
   */
  public String getAlbumAccess() {
    GphotoAccess access = getExtension(GphotoAccess.class);
    return access == null ? null : access.getValue().toLowerCase();
  }

  /**
   * Set the access for the album that contains this photo.
   *
   * @param access the access of the album.
   */
  public void setAlbumAccess(String access) {
    if (access != null) {
      setExtension(new GphotoAccess(access));
    } else {
      removeExtension(GphotoAccess.class);
    }
  }

  /**
   * @return the gphoto:videostatus of the video/photo.
   */
  public String getVideoStatus() {
    return getSimpleValue(GphotoVideoStatus.class);
  }

  /**
   * Sets the video status of the video/photo entry.
   */
  public void setVideoStatus(String videoStatus) {
    if (videoStatus != null) {
      setExtension(new GphotoVideoStatus(videoStatus));
    } else {
      removeExtension(GphotoVideoStatus.class);
    }
  }

  /**
   * @return the gphoto:width of the photo.
   */
  public Long getWidth() {
    GphotoWidth ext = getExtension(GphotoWidth.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the width of the photo this item is about.
   *
   * @param width the width of the photo.
   */
  public void setWidth(Long width) {
    if (width != null) {
      setExtension(new GphotoWidth(width));
    } else {
      removeExtension(GphotoWidth.class);
    }
  }

  /**
   * @return the gphoto:height of the photo.
   */
  public Long getHeight() {
    GphotoHeight ext = getExtension(GphotoHeight.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the height of the photo the item is about.
   *
   * @param height the height of the photo.
   */
  public void setHeight(Long height) {
    if (height != null) {
      setExtension(new GphotoHeight(height));
    } else {
      removeExtension(GphotoHeight.class);
    }
  }

  /**
   * @return the gphoto:rotation of the photo.
   */
  public Integer getRotation() {
    GphotoRotation ext = getExtension(GphotoRotation.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the rotation in degrees of the photo.
   *
   * @param rotation the rotation of the photo.
   */
  public void setRotation(Integer rotation) {
    if (rotation != null) {
      setExtension(new GphotoRotation(rotation));
    } else {
      removeExtension(GphotoRotation.class);
    }
  }

  /**
   * @return the gphoto:size of the photo.
   */
  public Long getSize() {
    GphotoSize ext = getExtension(GphotoSize.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the size of the photo this item is about.
   *
   * @param size the size of the photo.
   */
  public void setSize(Long size) {
    if (size != null) {
      setExtension(new GphotoSize(size));
    } else {
      removeExtension(GphotoSize.class);
    }
  }

  /**
   * @return the gphoto:client of the photo.
   */
  public String getClient() {
    return getSimpleValue(GphotoClient.class);
  }

  /**
   * Set the client of the photo this item is about.
   *
   * @param client the client that created the photo.
   */
  public void setClient(String client) {
    if (client != null) {
      setExtension(new GphotoClient(client));
    } else {
      removeExtension(GphotoClient.class);
    }
  }

  /**
   * @return the gphoto:checksum of the photo.
   */
  public String getChecksum() {
    return getSimpleValue(GphotoChecksum.class);
  }

  /**
   * Set the checksum of the photo this item is about.
   *
   * @param checksum the checksum on the photo.
   */
  public void setChecksum(String checksum) {
    if (checksum != null) {
      setExtension(new GphotoChecksum(checksum));
    } else {
      removeExtension(GphotoChecksum.class);
    }
  }

  /**
   * @return the gphoto:timestamp of the photo.
   */
  public Date getTimestamp() {
    GphotoTimestamp ext = getExtension(GphotoTimestamp.class);
    return ext == null ? null : new Date(ext.getValue());
  }

  /**
   * Set the timestamp on the photo this item is about.
   *
   * @param timestamp the timestamp on the photo.
   */
  public void setTimestamp(Date timestamp) {
    if (timestamp != null) {
      setExtension(new GphotoTimestamp(timestamp));
    } else {
      removeExtension(GphotoTimestamp.class);
    }
  }

  /**
   * @return the exif:tags ExifTags for the photo.
   */
  public ExifTags getExifTags() {
    return getExtension(ExifTags.class);
  }

  /**
   * Set the exif tags on the photo this item is about.
   *
   * @param tags the exif tags for the photo.
   */
  public void setExifTags(ExifTags tags) {
    if (tags != null) {
      setExtension(tags);
    } else {
      removeExtension(ExifTags.class);
    }
  }

  /**
   * @return true if comments are enabled in the photo the item represents.
   */
  public Boolean getCommentsEnabled() {
    GphotoCommentsEnabled ext = getExtension(GphotoCommentsEnabled.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the whether comments are enabled in the photo this item represents.
   *
   * @param commentsEnabled true if comments are enabled in the photo.
   */
  public void setCommentsEnabled(Boolean commentsEnabled) {
    if (commentsEnabled != null) {
      setExtension(new GphotoCommentsEnabled(commentsEnabled));
    } else {
      removeExtension(GphotoCommentsEnabled.class);
    }
  }

  /**
   * @return the comment count on the photo this item represents.
   */
  public Integer getCommentCount() {
    GphotoCommentCount ext = getExtension(GphotoCommentCount.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * Set the number of comments on the photo this item represents.
   *
   * @param commentCount the number of comments on the photo.
   */
  public void setCommentCount(Integer commentCount) {
    if (commentCount != null) {
      setExtension(new GphotoCommentCount(commentCount));
    } else {
      removeExtension(GphotoCommentCount.class);
    }
  }

  /**
   * Get a list of stream ids on this element.
   */
  public List<String> getStreamIds() {
    List<GphotoStreamId> exts = getRepeatingExtension(GphotoStreamId.class);
    List<String> streamIds = new ArrayList<String>(exts.size());
    for (GphotoStreamId streamId : exts) {
      streamIds.add(streamId.getValue());
    }
    return streamIds;
  }

  /**
   * Add the streamId to the list of stream ids.
   */
  public void addStreamId(String streamId) {
    addRepeatingExtension(new GphotoStreamId(streamId));
  }
  
  /**
   * @return the number of views for this photo.
   */
  public Long getViewCount() {
    GphotoViewCount ext = getExtension(GphotoViewCount.class);
    return ext == null ? null : ext.getValue();
  }
  
  /**
   * Sets the view count for this photo.
   * @param viewCount the number of views for this photo.
   */
  public void setViewCount(Long viewCount) {
    if (viewCount != null) {
      setExtension(new GphotoViewCount(viewCount));
    } else {
      removeExtension(GphotoViewCount.class);
    }
  }
  
  /**
   * @return date that the photo was featured.
   */
  public Date getFeaturedDate() {
    GphotoFeaturedDate ext = getExtension(GphotoFeaturedDate.class);
    return ext == null ? null : new Date(ext.getValue());
  }

  /**
   * Sets the date that the photo was featured.
   * @param featuredDate the date that the photo was featured.
   */
  public void setFeaturedDate(Date featuredDate) {
    if (featuredDate != null) {
      setExtension(new GphotoFeaturedDate(featuredDate));
    } else {
      removeExtension(GphotoFeaturedDate.class);
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

  public Boolean isStarred() {
    GphotoStarred ext = getExtension(GphotoStarred.class);
    return ext == null ? null : ext.getValue();
  }

  public void setStarred(Boolean starred) {
    GphotoStarred ext = getExtension(GphotoStarred.class);
    if (ext == null) {
      ext = new GphotoStarred();
      setExtension(ext);
    }
    ext.setValue(starred);
  }

  public Integer getTotalStars() {
    GphotoStarred ext = getExtension(GphotoStarred.class);
    return ext == null ? null : ext.getTotal();
  }

  public void setTotalStars(Integer totalStars) {
    GphotoStarred ext = getExtension(GphotoStarred.class);
    if (ext == null) {
      ext = new GphotoStarred();
      setExtension(ext);
    }
    ext.setTotal(totalStars);
  }

}
