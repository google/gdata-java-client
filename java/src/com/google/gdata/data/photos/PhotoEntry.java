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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.geo.impl.BoxDataImpl;
import com.google.gdata.data.geo.impl.GeoRssBox;
import com.google.gdata.data.geo.impl.GeoRssPoint;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.geo.impl.GmlEnvelope;
import com.google.gdata.data.geo.impl.GmlPoint;
import com.google.gdata.data.geo.impl.PointDataImpl;
import com.google.gdata.data.geo.impl.W3CPoint;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Entry for photo kinds, contains photo metadata.
 *
 * 
 */
@Kind.Term(PhotoEntry.KIND)
public class PhotoEntry extends GphotoEntry<PhotoEntry> implements AtomData,
    PhotoData {

  /**
   * Photo kind term value.
   */
  public static final String KIND = Namespaces.PHOTOS_PREFIX + "photo";

  /**
   * Photo kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public PhotoEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public PhotoEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(PhotoEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class, GphotoAccess.class);
    extProfile.declare(PhotoEntry.class, GphotoAlbumId.class);
    extProfile.declare(PhotoEntry.class, GphotoChecksum.class);
    extProfile.declare(PhotoEntry.class, GphotoClient.class);
    extProfile.declare(PhotoEntry.class, GphotoCommentCount.class);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GphotoCommentsEnabled.class,
        new XmlNamespace("gphoto", "http://schemas.google.com/photos/2007"),
        "commentingEnabled", false, false, false));
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(ExifTags.class, new XmlNamespace("exif",
        "http://schemas.google.com/photos/exif/2007"), "tags", false, false,
        false));
    new ExifTags().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class, GphotoFeaturedDate.class);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(W3CPoint.class, new XmlNamespace("geo",
        "http://www.w3.org/2003/01/geo/wgs84_pos#"), "Point", false, false,
        false));
    new W3CPoint().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GeoRssBox.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "box", false, false, false));
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GeoRssPoint.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "point", false, false, false));
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GeoRssWhere.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "where", false, false, false));
    new GeoRssWhere().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GmlEnvelope.class, new XmlNamespace("gml",
        "http://www.opengis.net/gml"), "Envelope", false, false, false));
    new GmlEnvelope().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(GmlPoint.class, new XmlNamespace("gml",
        "http://www.opengis.net/gml"), "Point", false, false, false));
    new GmlPoint().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class, GphotoHeight.class);
    extProfile.declare(PhotoEntry.class, GphotoImageVersion.class);
    extProfile.declare(PhotoEntry.class,
        new ExtensionDescription(MediaGroup.class, new XmlNamespace("media",
        "http://search.yahoo.com/mrss/"), "group", false, false, false));
    new MediaGroup().declareExtensions(extProfile);
    extProfile.declare(PhotoEntry.class, GphotoOriginalVideo.class);
    extProfile.declare(PhotoEntry.class, GphotoPosition.class);
    extProfile.declare(PhotoEntry.class, GphotoRotation.class);
    extProfile.declare(PhotoEntry.class, GphotoSize.class);
    extProfile.declare(PhotoEntry.class, GphotoStarred.class);
    extProfile.declare(PhotoEntry.class,
        GphotoStreamId.getDefaultDescription(false, true));
    extProfile.declare(PhotoEntry.class, GphotoTimestamp.class);
    extProfile.declare(PhotoEntry.class, GphotoVersion.class);
    extProfile.declare(PhotoEntry.class, GphotoVideoStatus.class);
    extProfile.declare(PhotoEntry.class, GphotoViewCount.class);
    extProfile.declare(PhotoEntry.class, GphotoWidth.class);
  }

  /**
   * Returns the access level for the album.
   *
   * @return access level for the album
   */
  public GphotoAccess getAlbumAccessExt() {
    return getExtension(GphotoAccess.class);
  }

  /**
   * Sets the access level for the album.
   *
   * @param albumAccessExt access level for the album or <code>null</code> to
   *     reset
   */
  public void setAlbumAccessExt(GphotoAccess albumAccessExt) {
    if (albumAccessExt == null) {
      removeExtension(GphotoAccess.class);
    } else {
      setExtension(albumAccessExt);
    }
  }

  /**
   * Returns whether it has the access level for the album.
   *
   * @return whether it has the access level for the album
   */
  public boolean hasAlbumAccessExt() {
    return hasExtension(GphotoAccess.class);
  }

  /**
   * Returns the album ID of the album this photo is in.
   *
   * @return album ID of the album this photo is in
   */
  public GphotoAlbumId getAlbumIdExt() {
    return getExtension(GphotoAlbumId.class);
  }

  /**
   * Sets the album ID of the album this photo is in.
   *
   * @param albumIdExt album ID of the album this photo is in or
   *     <code>null</code> to reset
   */
  public void setAlbumIdExt(GphotoAlbumId albumIdExt) {
    if (albumIdExt == null) {
      removeExtension(GphotoAlbumId.class);
    } else {
      setExtension(albumIdExt);
    }
  }

  /**
   * Returns whether it has the album ID of the album this photo is in.
   *
   * @return whether it has the album ID of the album this photo is in
   */
  public boolean hasAlbumIdExt() {
    return hasExtension(GphotoAlbumId.class);
  }

  /**
   * Returns the the checksum for the photo provided by the client.
   *
   * @return the checksum for the photo provided by the client
   */
  public GphotoChecksum getChecksumExt() {
    return getExtension(GphotoChecksum.class);
  }

  /**
   * Sets the the checksum for the photo provided by the client.
   *
   * @param checksumExt the checksum for the photo provided by the client or
   *     <code>null</code> to reset
   */
  public void setChecksumExt(GphotoChecksum checksumExt) {
    if (checksumExt == null) {
      removeExtension(GphotoChecksum.class);
    } else {
      setExtension(checksumExt);
    }
  }

  /**
   * Returns whether it has the the checksum for the photo provided by the
   * client.
   *
   * @return whether it has the the checksum for the photo provided by the
   *     client
   */
  public boolean hasChecksumExt() {
    return hasExtension(GphotoChecksum.class);
  }

  /**
   * Returns the the client that uploaded the photo.
   *
   * @return the client that uploaded the photo
   */
  public GphotoClient getClientExt() {
    return getExtension(GphotoClient.class);
  }

  /**
   * Sets the the client that uploaded the photo.
   *
   * @param clientExt the client that uploaded the photo or <code>null</code> to
   *     reset
   */
  public void setClientExt(GphotoClient clientExt) {
    if (clientExt == null) {
      removeExtension(GphotoClient.class);
    } else {
      setExtension(clientExt);
    }
  }

  /**
   * Returns whether it has the the client that uploaded the photo.
   *
   * @return whether it has the the client that uploaded the photo
   */
  public boolean hasClientExt() {
    return hasExtension(GphotoClient.class);
  }

  /**
   * Returns the the count of comments on this photo.
   *
   * @return the count of comments on this photo
   */
  public GphotoCommentCount getCommentCountExt() {
    return getExtension(GphotoCommentCount.class);
  }

  /**
   * Sets the the count of comments on this photo.
   *
   * @param commentCountExt the count of comments on this photo or
   *     <code>null</code> to reset
   */
  public void setCommentCountExt(GphotoCommentCount commentCountExt) {
    if (commentCountExt == null) {
      removeExtension(GphotoCommentCount.class);
    } else {
      setExtension(commentCountExt);
    }
  }

  /**
   * Returns whether it has the the count of comments on this photo.
   *
   * @return whether it has the the count of comments on this photo
   */
  public boolean hasCommentCountExt() {
    return hasExtension(GphotoCommentCount.class);
  }

  /**
   * Returns the whether comments are enabled on this photo.
   *
   * @return whether comments are enabled on this photo
   */
  public GphotoCommentsEnabled getCommentsEnabledExt() {
    return getExtension(GphotoCommentsEnabled.class);
  }

  /**
   * Sets the whether comments are enabled on this photo.
   *
   * @param commentsEnabledExt whether comments are enabled on this photo or
   *     <code>null</code> to reset
   */
  public void setCommentsEnabledExt(GphotoCommentsEnabled commentsEnabledExt) {
    if (commentsEnabledExt == null) {
      removeExtension(GphotoCommentsEnabled.class);
    } else {
      setExtension(commentsEnabledExt);
    }
  }

  /**
   * Returns whether it has the whether comments are enabled on this photo.
   *
   * @return whether it has the whether comments are enabled on this photo
   */
  public boolean hasCommentsEnabledExt() {
    return hasExtension(GphotoCommentsEnabled.class);
  }

  /**
   * Returns the the exif information on the photo.
   *
   * @return the exif information on the photo
   */
  public ExifTags getExifTags() {
    return getExtension(ExifTags.class);
  }

  /**
   * Sets the the exif information on the photo.
   *
   * @param exifTags the exif information on the photo or <code>null</code> to
   *     reset
   */
  public void setExifTags(ExifTags exifTags) {
    if (exifTags == null) {
      removeExtension(ExifTags.class);
    } else {
      setExtension(exifTags);
    }
  }

  /**
   * Returns whether it has the the exif information on the photo.
   *
   * @return whether it has the the exif information on the photo
   */
  public boolean hasExifTags() {
    return hasExtension(ExifTags.class);
  }

  /**
   * Returns the datetime in unix timestamp format for when the photo was
   * featured.
   *
   * @return datetime in unix timestamp format for when the photo was featured
   */
  public GphotoFeaturedDate getFeaturedDateExt() {
    return getExtension(GphotoFeaturedDate.class);
  }

  /**
   * Sets the datetime in unix timestamp format for when the photo was featured.
   *
   * @param featuredDateExt datetime in unix timestamp format for when the photo
   *     was featured or <code>null</code> to reset
   */
  public void setFeaturedDateExt(GphotoFeaturedDate featuredDateExt) {
    if (featuredDateExt == null) {
      removeExtension(GphotoFeaturedDate.class);
    } else {
      setExtension(featuredDateExt);
    }
  }

  /**
   * Returns whether it has the datetime in unix timestamp format for when the
   * photo was featured.
   *
   * @return whether it has the datetime in unix timestamp format for when the
   *     photo was featured
   */
  public boolean hasFeaturedDateExt() {
    return hasExtension(GphotoFeaturedDate.class);
  }

  /**
   * Returns the geolocation as a geo:point.
   *
   * @return geolocation as a geo:point
   */
  public W3CPoint getGeoPoint() {
    return getExtension(W3CPoint.class);
  }

  /**
   * Sets the geolocation as a geo:point.
   *
   * @param geoPoint geolocation as a geo:point or <code>null</code> to reset
   */
  public void setGeoPoint(W3CPoint geoPoint) {
    if (geoPoint == null) {
      removeExtension(W3CPoint.class);
    } else {
      setExtension(geoPoint);
    }
  }

  /**
   * Returns whether it has the geolocation as a geo:point.
   *
   * @return whether it has the geolocation as a geo:point
   */
  public boolean hasGeoPoint() {
    return hasExtension(W3CPoint.class);
  }

  /**
   * Returns the geo bounding box as a georss:box.
   *
   * @return geo bounding box as a georss:box
   */
  public GeoRssBox getGeoRssBox() {
    return getExtension(GeoRssBox.class);
  }

  /**
   * Sets the geo bounding box as a georss:box.
   *
   * @param geoRssBox geo bounding box as a georss:box or <code>null</code> to
   *     reset
   */
  public void setGeoRssBox(GeoRssBox geoRssBox) {
    if (geoRssBox == null) {
      removeExtension(GeoRssBox.class);
    } else {
      setExtension(geoRssBox);
    }
  }

  /**
   * Returns whether it has the geo bounding box as a georss:box.
   *
   * @return whether it has the geo bounding box as a georss:box
   */
  public boolean hasGeoRssBox() {
    return hasExtension(GeoRssBox.class);
  }

  /**
   * Returns the geolocation as a georss:point.
   *
   * @return geolocation as a georss:point
   */
  public GeoRssPoint getGeoRssPoint() {
    return getExtension(GeoRssPoint.class);
  }

  /**
   * Sets the geolocation as a georss:point.
   *
   * @param geoRssPoint geolocation as a georss:point or <code>null</code> to
   *     reset
   */
  public void setGeoRssPoint(GeoRssPoint geoRssPoint) {
    if (geoRssPoint == null) {
      removeExtension(GeoRssPoint.class);
    } else {
      setExtension(geoRssPoint);
    }
  }

  /**
   * Returns whether it has the geolocation as a georss:point.
   *
   * @return whether it has the geolocation as a georss:point
   */
  public boolean hasGeoRssPoint() {
    return hasExtension(GeoRssPoint.class);
  }

  /**
   * Returns the geolocation as a georss:where.
   *
   * @return geolocation as a georss:where
   */
  public GeoRssWhere getGeoRssWhere() {
    return getExtension(GeoRssWhere.class);
  }

  /**
   * Sets the geolocation as a georss:where.
   *
   * @param geoRssWhere geolocation as a georss:where or <code>null</code> to
   *     reset
   */
  public void setGeoRssWhere(GeoRssWhere geoRssWhere) {
    if (geoRssWhere == null) {
      removeExtension(GeoRssWhere.class);
    } else {
      setExtension(geoRssWhere);
    }
  }

  /**
   * Returns whether it has the geolocation as a georss:where.
   *
   * @return whether it has the geolocation as a georss:where
   */
  public boolean hasGeoRssWhere() {
    return hasExtension(GeoRssWhere.class);
  }

  /**
   * Returns the geo bounding box as a gml:Envelope.
   *
   * @return geo bounding box as a gml:Envelope
   */
  public GmlEnvelope getGmlEnvelope() {
    return getExtension(GmlEnvelope.class);
  }

  /**
   * Sets the geo bounding box as a gml:Envelope.
   *
   * @param gmlEnvelope geo bounding box as a gml:Envelope or <code>null</code>
   *     to reset
   */
  public void setGmlEnvelope(GmlEnvelope gmlEnvelope) {
    if (gmlEnvelope == null) {
      removeExtension(GmlEnvelope.class);
    } else {
      setExtension(gmlEnvelope);
    }
  }

  /**
   * Returns whether it has the geo bounding box as a gml:Envelope.
   *
   * @return whether it has the geo bounding box as a gml:Envelope
   */
  public boolean hasGmlEnvelope() {
    return hasExtension(GmlEnvelope.class);
  }

  /**
   * Returns the geolocation as a gml:point.
   *
   * @return geolocation as a gml:point
   */
  public GmlPoint getGmlPoint() {
    return getExtension(GmlPoint.class);
  }

  /**
   * Sets the geolocation as a gml:point.
   *
   * @param gmlPoint geolocation as a gml:point or <code>null</code> to reset
   */
  public void setGmlPoint(GmlPoint gmlPoint) {
    if (gmlPoint == null) {
      removeExtension(GmlPoint.class);
    } else {
      setExtension(gmlPoint);
    }
  }

  /**
   * Returns whether it has the geolocation as a gml:point.
   *
   * @return whether it has the geolocation as a gml:point
   */
  public boolean hasGmlPoint() {
    return hasExtension(GmlPoint.class);
  }

  /**
   * Returns the the height of the photo.
   *
   * @return the height of the photo
   */
  public GphotoHeight getHeightExt() {
    return getExtension(GphotoHeight.class);
  }

  /**
   * Sets the the height of the photo.
   *
   * @param heightExt the height of the photo or <code>null</code> to reset
   */
  public void setHeightExt(GphotoHeight heightExt) {
    if (heightExt == null) {
      removeExtension(GphotoHeight.class);
    } else {
      setExtension(heightExt);
    }
  }

  /**
   * Returns whether it has the the height of the photo.
   *
   * @return whether it has the the height of the photo
   */
  public boolean hasHeightExt() {
    return hasExtension(GphotoHeight.class);
  }

  /**
   * Returns the the version of the image bytes.
   *
   * @return the version of the image bytes
   */
  public GphotoImageVersion getImageVersionExt() {
    return getExtension(GphotoImageVersion.class);
  }

  /**
   * Sets the the version of the image bytes.
   *
   * @param imageVersionExt the version of the image bytes or <code>null</code>
   *     to reset
   */
  public void setImageVersionExt(GphotoImageVersion imageVersionExt) {
    if (imageVersionExt == null) {
      removeExtension(GphotoImageVersion.class);
    } else {
      setExtension(imageVersionExt);
    }
  }

  /**
   * Returns whether it has the the version of the image bytes.
   *
   * @return whether it has the the version of the image bytes
   */
  public boolean hasImageVersionExt() {
    return hasExtension(GphotoImageVersion.class);
  }

  /**
   * Returns the mediarss group for media metadata.
   *
   * @return mediarss group for media metadata
   */
  public MediaGroup getMediaGroup() {
    return getExtension(MediaGroup.class);
  }

  /**
   * Sets the mediarss group for media metadata.
   *
   * @param mediaGroup mediarss group for media metadata or <code>null</code> to
   *     reset
   */
  public void setMediaGroup(MediaGroup mediaGroup) {
    if (mediaGroup == null) {
      removeExtension(MediaGroup.class);
    } else {
      setExtension(mediaGroup);
    }
  }

  /**
   * Returns whether it has the mediarss group for media metadata.
   *
   * @return whether it has the mediarss group for media metadata
   */
  public boolean hasMediaGroup() {
    return hasExtension(MediaGroup.class);
  }

  /**
   * Returns the the properties of the original video.
   *
   * @return the properties of the original video
   */
  public GphotoOriginalVideo getOriginalVideo() {
    return getExtension(GphotoOriginalVideo.class);
  }

  /**
   * Sets the the properties of the original video.
   *
   * @param originalVideo the properties of the original video or
   *     <code>null</code> to reset
   */
  public void setOriginalVideo(GphotoOriginalVideo originalVideo) {
    if (originalVideo == null) {
      removeExtension(GphotoOriginalVideo.class);
    } else {
      setExtension(originalVideo);
    }
  }

  /**
   * Returns whether it has the the properties of the original video.
   *
   * @return whether it has the the properties of the original video
   */
  public boolean hasOriginalVideo() {
    return hasExtension(GphotoOriginalVideo.class);
  }

  /**
   * Returns the position of the photo in its album.
   *
   * @return position of the photo in its album
   */
  public GphotoPosition getPositionExt() {
    return getExtension(GphotoPosition.class);
  }

  /**
   * Sets the position of the photo in its album.
   *
   * @param positionExt position of the photo in its album or <code>null</code>
   *     to reset
   */
  public void setPositionExt(GphotoPosition positionExt) {
    if (positionExt == null) {
      removeExtension(GphotoPosition.class);
    } else {
      setExtension(positionExt);
    }
  }

  /**
   * Returns whether it has the position of the photo in its album.
   *
   * @return whether it has the position of the photo in its album
   */
  public boolean hasPositionExt() {
    return hasExtension(GphotoPosition.class);
  }

  /**
   * Returns the the rotation of the photo in degrees.
   *
   * @return the rotation of the photo in degrees
   */
  public GphotoRotation getRotationExt() {
    return getExtension(GphotoRotation.class);
  }

  /**
   * Sets the the rotation of the photo in degrees.
   *
   * @param rotationExt the rotation of the photo in degrees or
   *     <code>null</code> to reset
   */
  public void setRotationExt(GphotoRotation rotationExt) {
    if (rotationExt == null) {
      removeExtension(GphotoRotation.class);
    } else {
      setExtension(rotationExt);
    }
  }

  /**
   * Returns whether it has the the rotation of the photo in degrees.
   *
   * @return whether it has the the rotation of the photo in degrees
   */
  public boolean hasRotationExt() {
    return hasExtension(GphotoRotation.class);
  }

  /**
   * Returns the the size of the photo in bytes.
   *
   * @return the size of the photo in bytes
   */
  public GphotoSize getSizeExt() {
    return getExtension(GphotoSize.class);
  }

  /**
   * Sets the the size of the photo in bytes.
   *
   * @param sizeExt the size of the photo in bytes or <code>null</code> to reset
   */
  public void setSizeExt(GphotoSize sizeExt) {
    if (sizeExt == null) {
      removeExtension(GphotoSize.class);
    } else {
      setExtension(sizeExt);
    }
  }

  /**
   * Returns whether it has the the size of the photo in bytes.
   *
   * @return whether it has the the size of the photo in bytes
   */
  public boolean hasSizeExt() {
    return hasExtension(GphotoSize.class);
  }

  /**
   * Returns the if viewer starred the photo and total number of stars.
   *
   * @return if viewer starred the photo and total number of stars
   */
  public GphotoStarred getStarredExt() {
    return getExtension(GphotoStarred.class);
  }

  /**
   * Sets the if viewer starred the photo and total number of stars.
   *
   * @param starredExt if viewer starred the photo and total number of stars or
   *     <code>null</code> to reset
   */
  public void setStarredExt(GphotoStarred starredExt) {
    if (starredExt == null) {
      removeExtension(GphotoStarred.class);
    } else {
      setExtension(starredExt);
    }
  }

  /**
   * Returns whether it has the if viewer starred the photo and total number of
   * stars.
   *
   * @return whether it has the if viewer starred the photo and total number of
   *     stars
   */
  public boolean hasStarredExt() {
    return hasExtension(GphotoStarred.class);
  }

  /**
   * Returns the the streamIds on this photo.
   *
   * @return the streamIds on this photo
   */
  public List<GphotoStreamId> getStreamIdsExt() {
    return getRepeatingExtension(GphotoStreamId.class);
  }

  /**
   * Adds a new the streamIds on this photo.
   *
   * @param streamIdsExt the streamIds on this photo
   */
  public void addStreamIdsExt(GphotoStreamId streamIdsExt) {
    getStreamIdsExt().add(streamIdsExt);
  }

  /**
   * Returns whether it has the the streamIds on this photo.
   *
   * @return whether it has the the streamIds on this photo
   */
  public boolean hasStreamIdsExt() {
    return hasRepeatingExtension(GphotoStreamId.class);
  }

  /**
   * Returns the the time the photo was taken.
   *
   * @return the time the photo was taken
   */
  public GphotoTimestamp getTimestampExt() {
    return getExtension(GphotoTimestamp.class);
  }

  /**
   * Sets the the time the photo was taken.
   *
   * @param timestampExt the time the photo was taken or <code>null</code> to
   *     reset
   */
  public void setTimestampExt(GphotoTimestamp timestampExt) {
    if (timestampExt == null) {
      removeExtension(GphotoTimestamp.class);
    } else {
      setExtension(timestampExt);
    }
  }

  /**
   * Returns whether it has the the time the photo was taken.
   *
   * @return whether it has the the time the photo was taken
   */
  public boolean hasTimestampExt() {
    return hasExtension(GphotoTimestamp.class);
  }

  /**
   * Returns the version of the photo metadata.
   *
   * @return version of the photo metadata
   */
  public GphotoVersion getVersionExt() {
    return getExtension(GphotoVersion.class);
  }

  /**
   * Sets the version of the photo metadata.
   *
   * @param versionExt version of the photo metadata or <code>null</code> to
   *     reset
   */
  public void setVersionExt(GphotoVersion versionExt) {
    if (versionExt == null) {
      removeExtension(GphotoVersion.class);
    } else {
      setExtension(versionExt);
    }
  }

  /**
   * Returns whether it has the version of the photo metadata.
   *
   * @return whether it has the version of the photo metadata
   */
  public boolean hasVersionExt() {
    return hasExtension(GphotoVersion.class);
  }

  /**
   * Returns the the status of the video upload if this is a video.
   *
   * @return the status of the video upload if this is a video
   */
  public GphotoVideoStatus getVideoStatusExt() {
    return getExtension(GphotoVideoStatus.class);
  }

  /**
   * Sets the the status of the video upload if this is a video.
   *
   * @param videoStatusExt the status of the video upload if this is a video or
   *     <code>null</code> to reset
   */
  public void setVideoStatusExt(GphotoVideoStatus videoStatusExt) {
    if (videoStatusExt == null) {
      removeExtension(GphotoVideoStatus.class);
    } else {
      setExtension(videoStatusExt);
    }
  }

  /**
   * Returns whether it has the the status of the video upload if this is a
   * video.
   *
   * @return whether it has the the status of the video upload if this is a
   *     video
   */
  public boolean hasVideoStatusExt() {
    return hasExtension(GphotoVideoStatus.class);
  }

  /**
   * Returns the the number of views for this image.
   *
   * @return the number of views for this image
   */
  public GphotoViewCount getViewCountExt() {
    return getExtension(GphotoViewCount.class);
  }

  /**
   * Sets the the number of views for this image.
   *
   * @param viewCountExt the number of views for this image or <code>null</code>
   *     to reset
   */
  public void setViewCountExt(GphotoViewCount viewCountExt) {
    if (viewCountExt == null) {
      removeExtension(GphotoViewCount.class);
    } else {
      setExtension(viewCountExt);
    }
  }

  /**
   * Returns whether it has the the number of views for this image.
   *
   * @return whether it has the the number of views for this image
   */
  public boolean hasViewCountExt() {
    return hasExtension(GphotoViewCount.class);
  }

  /**
   * Returns the the width of the photo.
   *
   * @return the width of the photo
   */
  public GphotoWidth getWidthExt() {
    return getExtension(GphotoWidth.class);
  }

  /**
   * Sets the the width of the photo.
   *
   * @param widthExt the width of the photo or <code>null</code> to reset
   */
  public void setWidthExt(GphotoWidth widthExt) {
    if (widthExt == null) {
      removeExtension(GphotoWidth.class);
    } else {
      setExtension(widthExt);
    }
  }

  /**
   * Returns whether it has the the width of the photo.
   *
   * @return whether it has the the width of the photo
   */
  public boolean hasWidthExt() {
    return hasExtension(GphotoWidth.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{PhotoEntry " + super.toString() + "}";
  }


  // allowing the java generator to generate clean apis for simple elements.

  /**
   * Retrieve the photo feed and associated entries.  The kinds parameter is a
   * list of the type of associated entries to return.  For example
   * <code>PhotoFeed photoAndComments = photoEntry.getFeed(CommentEntry.KIND,
   *     TagEntry.KIND);</code>  If no kind parameters are passed, the default
   * of {@link CommentEntry#KIND} will be used.
   *
   * @see CommentEntry#KIND
   * @see TagEntry#KIND
   * @param kinds the kinds of entries to retrieve, or empty to use the default.
   * @return a feed of the photo and the requested kinds.
   */
  public PhotoFeed getFeed(String... kinds)
      throws IOException, ServiceException {
    return getFeed(PhotoFeed.class, kinds);
  }

  public String getAlbumId() {
    GphotoAlbumId ext = getAlbumIdExt();
    return ext == null ? null : ext.getValue();
  }

  public String getAlbumAccess() {
    GphotoAccess ext = getAlbumAccessExt();
    return ext == null ? null : ext.getValue();
  }

  public String getVideoStatus() {
    GphotoVideoStatus ext = getVideoStatusExt();
    return ext == null ? null : ext.getValue();
  }

  public String getChecksum() {
    GphotoChecksum ext = getChecksumExt();
    return ext == null ? null : ext.getValue();
  }

  public String getClient() {
    GphotoClient ext = getClientExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Integer getCommentCount() throws ServiceException {
    GphotoCommentCount ext = getCommentCountExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Boolean getCommentsEnabled() throws ServiceException {
    GphotoCommentsEnabled ext = getCommentsEnabledExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @return date that the photo was featured.
   */
  public Date getFeaturedDate() {
    GphotoFeaturedDate ext = getExtension(GphotoFeaturedDate.class);
    return ext == null ? null : new Date(ext.getValue());
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Long getHeight() throws ServiceException {
    GphotoHeight ext = getHeightExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Float getPosition() throws ServiceException {
    GphotoPosition ext = getPositionExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Integer getRotation() throws ServiceException {
    GphotoRotation ext = getRotationExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Long getSize() throws ServiceException {
    GphotoSize ext = getSizeExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Date getTimestamp() throws ServiceException {
    GphotoTimestamp ext = getTimestampExt();
    return ext == null ? null : new Date(ext.getValue());
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Long getVersion() throws ServiceException {
    GphotoVersion ext = getVersionExt();
    return ext == null ? null : ext.getValue();
  }

  /**
   * @return the number of views for this photo.
   */
  public Long getViewCount() {
    GphotoViewCount ext = getExtension(GphotoViewCount.class);
    return ext == null ? null : ext.getValue();
  }

  /**
   * @throws ServiceException no longer, but used to.
   */
  public Long getWidth() throws ServiceException {
    GphotoWidth ext = getWidthExt();
    return ext == null ? null : ext.getValue();
  }

  public void setAlbumId(Long albumId) {
    GphotoAlbumId ext = null;
    if (albumId != null) {
      ext = GphotoAlbumId.from(albumId);
    }
    setAlbumIdExt(ext);
  }

  public void setAlbumId(String albumId) {
    GphotoAlbumId ext = null;
    if (albumId != null) {
      ext = new GphotoAlbumId(albumId);
    }
    setAlbumIdExt(ext);
  }

  public void setAlbumAccess(String access) {
    GphotoAccess ext = null;
    if (access != null) {
      ext = new GphotoAccess(access);
    }
    setAlbumAccessExt(ext);
  }

  public void setVideoStatus(String videoStatus) {
    GphotoVideoStatus ext = null;
    if (videoStatus != null) {
      ext = new GphotoVideoStatus(videoStatus);
    }
    setVideoStatusExt(ext);
  }

  public void setChecksum(String checksum) {
    GphotoChecksum ext = null;
    if (checksum != null) {
      ext = new GphotoChecksum(checksum);
    }
    setChecksumExt(ext);
  }

  public void setClient(String client) {
    GphotoClient ext = null;
    if (client != null) {
      ext = new GphotoClient(client);
    }
    setClientExt(ext);
  }

  public void setCommentCount(Integer commentCount) {
    GphotoCommentCount ext = null;
    if (commentCount != null) {
      ext = new GphotoCommentCount(commentCount);
    }
    setCommentCountExt(ext);
  }

  public void setCommentsEnabled(Boolean commentsEnabled) {
    GphotoCommentsEnabled ext = null;
    if (commentsEnabled != null) {
      ext = new GphotoCommentsEnabled(commentsEnabled);
    }
    setCommentsEnabledExt(ext);
  }

  public void setFeaturedDate(Date featuredDate) {
    GphotoFeaturedDate ext = null;
    if (featuredDate != null) {
      ext = new GphotoFeaturedDate(featuredDate);
    }
    setFeaturedDateExt(ext);
  }

  public void setHeight(Long height) {
    GphotoHeight ext = null;
    if (height != null) {
      ext = new GphotoHeight(height);
    }
    setHeightExt(ext);
  }

  public void setPosition(Float position) {
    GphotoPosition ext = null;
    if (position != null) {
      ext = new GphotoPosition(position);
    }
    setPositionExt(ext);
  }

  public void setRotation(Integer rotation) {
    GphotoRotation ext = null;
    if (rotation != null) {
      ext = new GphotoRotation(rotation);
    }
    setRotationExt(ext);
  }

  public void setSize(Long size) {
    GphotoSize ext = null;
    if (size != null) {
      ext = new GphotoSize(size);
    }
    setSizeExt(ext);
  }

  public void setTimestamp(Date timestamp) {
    GphotoTimestamp ext = null;
    if (timestamp != null) {
      ext = new GphotoTimestamp(timestamp);
    }
    setTimestampExt(ext);
  }

  public void setVersion(Long version) {
    GphotoVersion ext = null;
    if (version != null) {
      ext = new GphotoVersion(version);
    }
    setVersionExt(ext);
  }

  public void setViewCount(Long viewCount) {
    GphotoViewCount ext = null;
    if (viewCount != null) {
      ext = new GphotoViewCount(viewCount);
    }
    setViewCountExt(ext);
  }

  public void setWidth(Long width) {
    GphotoWidth ext = null;
    if (width != null) {
      ext = new GphotoWidth(width);
    }
    setWidthExt(ext);
  }

  public Point getGeoLocation() {
    return PointDataImpl.getPoint(this);
  }

  public void setGeoLocation(Double lat, Double lon) {
    setGeoLocation(new GeoRssWhere(lat, lon));
  }

  public void setGeoLocation(Point point) {
    PointDataImpl.setPoint(this, point);
  }

  public void setGeoBoundingBox(Point lowerLeft, Point upperRight) {
    setGeoBoundingBox(new GeoRssWhere(lowerLeft, upperRight));
  }

  public void setGeoBoundingBox(Box boundingBox) {
    BoxDataImpl.setBox(this, boundingBox);
  }

  public Box getGeoBoundingBox() {
    return BoxDataImpl.getBox(this);
  }

  public void clearPoint() {
   PointDataImpl.clearPoint(this);
  }

  public void clearGeoBoundingBox() {
    BoxDataImpl.clearBox(this);
  }

  public void addStreamId(String streamId) {
    GphotoStreamId ext = new GphotoStreamId(streamId);
    addStreamIdsExt(ext);
  }

  public List<String> getStreamIds() {
    List<GphotoStreamId> exts = getStreamIdsExt();
    List<String> result = new ArrayList<String>();
    for (GphotoStreamId ext : exts) {
      result.add(ext.getValue());
    }
    return result;
  }

  public List<MediaCategory> getMediaCategories() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    return group.getCategories();
  }

  public List<MediaContent> getMediaContents() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    return group.getContents();
  }

  public List<MediaCredit> getMediaCredits() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    return group.getCredits();
  }

  public MediaKeywords getMediaKeywords() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return null;
    }
    return group.getKeywords();
  }

  public List<MediaThumbnail> getMediaThumbnails() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    return group.getThumbnails();
  }

  public void setKeywords(MediaKeywords keywords) {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      group = new MediaGroup();
      setMediaGroup(group);
    }
    group.setKeywords(keywords);
  }

  public Boolean isStarred() {
    GphotoStarred ext = getStarredExt();
    return ext == null ? null : ext.getValue();
  }

  public void setStarred(Boolean starred) {
    GphotoStarred ext = getStarredExt();
    if (ext == null) {
      ext = new GphotoStarred();
      setStarredExt(ext);
    }
    ext.setValue(starred);
  }

  public Integer getTotalStars() {
    GphotoStarred ext = getStarredExt();
    return ext == null ? null : ext.getTotal();
  }

  public void setTotalStars(Integer totalStars) {
    GphotoStarred ext = getStarredExt();
    if (ext == null) {
      ext = new GphotoStarred();
      setStarredExt(ext);
    }
    ext.setTotal(totalStars);
  }

}
