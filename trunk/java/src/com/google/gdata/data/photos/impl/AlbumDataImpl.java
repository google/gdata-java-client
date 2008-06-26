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
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.geo.impl.PointDataImpl;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumData;
import com.google.gdata.data.photos.Namespaces;
import com.google.gdata.data.photos.impl.Extensions.GphotoCommentCount;
import com.google.gdata.data.photos.impl.Extensions.GphotoCommentsEnabled;
import com.google.gdata.data.photos.impl.Extensions.GphotoConstruct;
import com.google.gdata.data.photos.impl.Extensions.GphotoNickname;
import com.google.gdata.data.photos.impl.Extensions.GphotoTimestamp;
import com.google.gdata.data.photos.impl.Extensions.GphotoUsername;
import com.google.gdata.data.photos.pheed.PheedImageUrl;
import com.google.gdata.data.photos.pheed.PheedThumbnail;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;

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

  private final PointDataImpl geoData;
  private final MediaDataImpl mediaData;
  
  /**
   * Construct a new implementation of AlbumGphotoData with the given
   * extension point as the backing storage for data.
   */
  public AlbumDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
    geoData = new PointDataImpl(extensionPoint);
    mediaData = new MediaDataImpl(extensionPoint);
  }

  /*
   * Declare the extensions that album objects use.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    
    declare(extProfile, PheedThumbnail.getDefaultDescription());
    declare(extProfile, PheedImageUrl.getDefaultDescription());

    declare(extProfile, GphotoName.getDefaultDescription());
    declare(extProfile, GphotoLocation.getDefaultDescription());
    declare(extProfile, GphotoTimestamp.getDefaultDescription());
    declare(extProfile, GphotoAccess.getDefaultDescription());
    declare(extProfile, GphotoPhotosUsed.getDefaultDescription());
    declare(extProfile, GphotoPhotosLeft.getDefaultDescription());
    declare(extProfile, GphotoBytesUsed.getDefaultDescription());

    declare(extProfile, GphotoUsername.getDefaultDescription());
    declare(extProfile, GphotoNickname.getDefaultDescription());

    declare(extProfile,
        GphotoCommentsEnabled.getDefaultDescription());
    declare(extProfile,
        GphotoCommentCount.getDefaultDescription());
    
    geoData.declareExtensions(extProfile);
    mediaData.declareExtensions(extProfile);
    
  }

  /**
   * @return the photo:thumbnail on the entry.
   */
  public String getThumbnail() {
    return getSimpleValue(PheedThumbnail.class);
  }

  /**
   * Set the thumbnail url for use in the photo:thumbnail element.
   *
   * @param thumbUrl the full url to the thumbnail.
   */
  public void setThumbnail(String thumbUrl) {
    if (thumbUrl != null) {
      setExtension(new PheedThumbnail(thumbUrl));
    } else {
      removeExtension(PheedThumbnail.class);
    }
  }

  /**
   * @return the photo:imgsrc on the entry.
   */
  public String getImageUrl() {
    return getSimpleValue(PheedImageUrl.class);
  }

  /**
   * Set the full image url for use in the photo:imgsrc element.
   *
   * @param imageUrl the full url to the image.
   */
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
  public Date getDate() throws ServiceException {
    return getDateValue(GphotoTimestamp.class);
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
    return getSimpleValue(GphotoAccess.class);
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
  public Integer getPhotosUsed() throws ParseException {
    return getIntegerValue(GphotoPhotosUsed.class);
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
  public Integer getPhotosLeft() throws ParseException {
    return getIntegerValue(GphotoPhotosLeft.class);
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
  public Long getBytesUsed() throws ParseException {
    return getLongValue(GphotoBytesUsed.class);
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
    return getBooleanValue(GphotoCommentsEnabled.class);
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
  public Integer getCommentCount() throws ParseException {
    return getIntegerValue(GphotoCommentCount.class);
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
    geoData.setGeoLocation(lat, lon);
  }
  
  public void setGeoLocation(Point point) {
    geoData.setGeoLocation(point);
  }
  
  public Point getGeoLocation() {
    return geoData.getGeoLocation();
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
   * The gphoto:name element.
   */
  public static class GphotoName extends GphotoConstruct {
    public GphotoName() {
      this(null);
    }

    public GphotoName(String name) {
      super("name", name);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoName.class,
          Namespaces.PHOTOS_NAMESPACE, "name");
    }
  }

  /**
   * The gphoto:location element.
   */
  public static class GphotoLocation extends GphotoConstruct {
    public GphotoLocation() {
      this(null);
    }

    public GphotoLocation(String location) {
      super("location", location);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoLocation.class,
          Namespaces.PHOTOS_NAMESPACE, "location");
    }
  }

  /**
   * The gphoto:access element.
   */
  public static class GphotoAccess extends GphotoConstruct {
    public GphotoAccess() {
      this(null);
    }

    public GphotoAccess(String access) {
      super("access", access);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoAccess.class,
          Namespaces.PHOTOS_NAMESPACE, "access");
    }
  }

  /**
   * The gphoto:photosUsed element.
   */
  public static class GphotoPhotosUsed extends GphotoConstruct {
    public GphotoPhotosUsed() {
      this(null);
    }

    public GphotoPhotosUsed(Integer photosUsed) {
      super("numphotos", photosUsed == null
          ? null
          : Integer.toString(photosUsed));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoPhotosUsed.class,
          Namespaces.PHOTOS_NAMESPACE, "numphotos");
    }
  }

  /**
   * The gphoto:photosLeft element.
   */
  public static class GphotoPhotosLeft extends GphotoConstruct {
    public GphotoPhotosLeft() {
      this(null);
    }

    public GphotoPhotosLeft(Integer photosUsed) {
      super("numphotosremaining", photosUsed == null
          ? null
          : Integer.toString(photosUsed));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoPhotosLeft.class,
          Namespaces.PHOTOS_NAMESPACE, "numphotosremaining");
    }
  }

  /**
   * The gphoto:bytesUsed element.
   */
  public static class GphotoBytesUsed extends GphotoConstruct {
    public GphotoBytesUsed() {
      this(null);
    }

    public GphotoBytesUsed(Long bytesUsed) {
      super("bytesUsed", bytesUsed == null ? null : Long.toString(bytesUsed));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoBytesUsed.class,
          Namespaces.PHOTOS_NAMESPACE, "bytesUsed");
    }
  }
}
