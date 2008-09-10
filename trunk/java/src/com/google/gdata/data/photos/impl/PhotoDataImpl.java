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

import com.google.gdata.data.ExtensionDescription;
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
import com.google.gdata.data.photos.Namespaces;
import com.google.gdata.data.photos.PhotoData;
import com.google.gdata.data.photos.impl.Extensions.GphotoAlbumId;
import com.google.gdata.data.photos.impl.Extensions.GphotoCommentCount;
import com.google.gdata.data.photos.impl.Extensions.GphotoCommentsEnabled;
import com.google.gdata.data.photos.impl.Extensions.GphotoConstruct;
import com.google.gdata.data.photos.impl.Extensions.GphotoTimestamp;
import com.google.gdata.data.photos.impl.Extensions.GphotoVersion;
import com.google.gdata.data.photos.pheed.PheedImageUrl;
import com.google.gdata.data.photos.pheed.PheedThumbnail;
import com.google.gdata.data.photos.pheed.PheedVideoUrl;
import com.google.gdata.util.ParseException;

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

    declare(extProfile, GphotoVersion.getDefaultDescription());
    declare(extProfile, GphotoPosition.getDefaultDescription());
    declare(extProfile, GphotoWidth.getDefaultDescription());
    declare(extProfile, GphotoHeight.getDefaultDescription());
    declare(extProfile, GphotoRotation.getDefaultDescription());
    declare(extProfile, GphotoSize.getDefaultDescription());

    declare(extProfile, GphotoAlbumId.getDefaultDescription());
    declare(extProfile, GphotoClient.getDefaultDescription());
    declare(extProfile, GphotoChecksum.getDefaultDescription());
    declare(extProfile, GphotoTimestamp.getDefaultDescription());
    declare(extProfile, GphotoExifTime.getDefaultDescription());
    declare(extProfile, GphotoStreamId.getDefaultDescription());

    declare(extProfile, GphotoVideoStatus.getDefaultDescription());
    
    declare(extProfile, ExifTags.getDefaultDescription());
    new ExifTags().declareExtensions(extProfile);

    declare(extProfile, GphotoCommentsEnabled.getDefaultDescription());
    declare(extProfile, GphotoCommentCount.getDefaultDescription());

    pointData.declareExtensions(extProfile);
    boundingBoxData.declareExtensions(extProfile);
    mediaData.declareExtensions(extProfile);
  }

  /**
   * Get the video url for the video that is being pointed at.
   */
  public String getVideoUrl() {
    return getSimpleValue(GphotoVideoUrl.class);
  }

  /**
   * Set the video url that points to the url of the actual video.
   * PheedVideoUrl is deprecated but included for backwards compatibility with
   * older clients.
   */
  @SuppressWarnings("deprecation")
  public void setVideoUrl(String videoUrl) {
    if (videoUrl != null) {
      setExtension(new GphotoVideoUrl(videoUrl));
      setExtension(new PheedVideoUrl(videoUrl));
    } else {
      removeExtension(GphotoVideoUrl.class);
      removeExtension(PheedVideoUrl.class);
    }
  }

  /**
   * @return the gphoto:version on the item.
   */
  public Long getVersion() throws ParseException {
    return getLongValue(GphotoVersion.class);
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
  public Float getPosition() throws ParseException {
    return getFloatValue(GphotoPosition.class);
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
      setExtension(new GphotoAlbumId(albumId));
    } else {
      removeExtension(GphotoAlbumId.class);
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
  public Long getWidth() throws ParseException {
    return getLongValue(GphotoWidth.class);
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
  public Long getHeight() throws ParseException {
    return getLongValue(GphotoHeight.class);
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
  public Integer getRotation() throws ParseException {
    return getIntegerValue(GphotoRotation.class);
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
  public Long getSize() throws ParseException {
    return getLongValue(GphotoSize.class);
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
  public Date getTimestamp() throws ParseException {
    return getDateValue(GphotoTimestamp.class);
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
    return getBooleanValue(GphotoCommentsEnabled.class);
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
  public Integer getCommentCount() throws ParseException {
    return getIntegerValue(GphotoCommentCount.class);
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
  
  /**
   * Simple position element, has a single float for the position.
   */
  public static class GphotoPosition extends GphotoConstruct {
    public GphotoPosition() {
      this(null);
    }

    public GphotoPosition(Float position) {
      super("position", position == null ? null : position.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoPosition.class,
          Namespaces.PHOTOS_NAMESPACE, "position");
    }
  }

  /**
   * Simple width element, has a single long for the width.
   */
  public static class GphotoWidth extends GphotoConstruct {
    public GphotoWidth() {
      this(null);
    }

    public GphotoWidth(Long width) {
      super("width", width == null ? null : width.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoWidth.class,
          Namespaces.PHOTOS_NAMESPACE, "width");
    }
  }

  /**
   * Simple height element, has a single long for the height.
   */
  public static class GphotoHeight extends GphotoConstruct {
    public GphotoHeight() {
      this(null);
    }

    public GphotoHeight(Long height) {
      super("height", height == null ? null : height.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoHeight.class,
          Namespaces.PHOTOS_NAMESPACE, "height");
    }
  }

  /**
   * Simple rotation element, has a single int for the rotation.
   */
  public static class GphotoRotation extends GphotoConstruct {
    public GphotoRotation() {
      this(null);
    }

    public GphotoRotation(Integer rotation) {
      super("rotation", rotation == null ? null : rotation.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoRotation.class,
          Namespaces.PHOTOS_NAMESPACE, "rotation");
    }
  }

  /**
   * Simple size element, has a single long for the size.
   */
  public static class GphotoSize extends GphotoConstruct {
    public GphotoSize() {
      this(null);
    }

    public GphotoSize(Long size) {
      super("size", size == null ? null : size.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoSize.class,
          Namespaces.PHOTOS_NAMESPACE, "size");
    }
  }

  /**
   * Simple client element, has a single string for the client.
   */
  public static class GphotoClient extends GphotoConstruct {
    public GphotoClient() {
      this(null);
    }

    public GphotoClient(String client) {
      super("client", client);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoClient.class,
          Namespaces.PHOTOS_NAMESPACE, "client");
    }
  }

  /**
   * Simple checksum element, has a single string for the checksum.
   * Checksums are provided by clients of the system.
   */
  public static class GphotoChecksum extends GphotoConstruct {
    public GphotoChecksum() {
      this(null);
    }

    public GphotoChecksum(String checksum) {
      super("checksum", checksum);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoChecksum.class,
          Namespaces.PHOTOS_NAMESPACE, "checksum");
    }
  }

  /**
   * Simple exif_time element, has the date of the photo from its exif data.
   * @deprecated use {@link ExifTags} instead.
   */
  @Deprecated
  public static class GphotoExifTime extends GphotoConstruct {
    public GphotoExifTime() {
      this(null);
    }

    public GphotoExifTime(Date exifDate) {
      super("exiftime",
          exifDate == null ? null : Long.toString(exifDate.getTime()));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoExifTime.class,
          Namespaces.PHOTOS_NAMESPACE, "exiftime");
    }
  }

  /**
   * Simple videosrc element, has the url to the video source of the item.
   */
  public static class GphotoVideoUrl extends GphotoConstruct {
    public GphotoVideoUrl() {
      this(null);
    }

    public GphotoVideoUrl(String videoUrl) {
      super("videosrc", videoUrl);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoVideoUrl.class,
          Namespaces.PHOTOS_NAMESPACE, "videosrc");
    }
  }
  
  /**
   * The gphoto:videostatus field.
   */
  public static class GphotoVideoStatus extends GphotoConstruct {
    public GphotoVideoStatus() {
      this((String) null);
    }

    public GphotoVideoStatus(String videoStatus) {
      super("videostatus", videoStatus);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoVideoStatus.class,
          Namespaces.PHOTOS_NAMESPACE, "videostatus");
    }
  }
  
  public static class GphotoStreamId extends GphotoConstruct {
    public GphotoStreamId() {
      this(null);
    }

    public GphotoStreamId(String streamId) {
      super("streamId", streamId);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoStreamId.class,
          Namespaces.PHOTOS_NAMESPACE, "streamId", false, true, false);
    }
  }
}
