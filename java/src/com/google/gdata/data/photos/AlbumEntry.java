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
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Entry for album kinds, contains album metadata.
 *
 * 
 */
@Kind.Term(AlbumEntry.KIND)
public class AlbumEntry extends GphotoEntry<AlbumEntry> implements AtomData,
    AlbumData {

  /**
   * Album kind term value.
   */
  public static final String KIND = Namespaces.PHOTOS_PREFIX + "album";

  /**
   * Album kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public AlbumEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public AlbumEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AlbumEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class, GphotoAccess.class);
    extProfile.declare(AlbumEntry.class, GphotoBytesUsed.class);
    extProfile.declare(AlbumEntry.class, GphotoCommentCount.class);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GphotoCommentsEnabled.class,
        new XmlNamespace("gphoto", "http://schemas.google.com/photos/2007"),
        "commentingEnabled", false, false, false));
    extProfile.declare(AlbumEntry.class, GphotoTimestamp.class);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(W3CPoint.class, new XmlNamespace("geo",
        "http://www.w3.org/2003/01/geo/wgs84_pos#"), "Point", false, false,
        false));
    new W3CPoint().declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GeoRssBox.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "box", false, false, false));
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GeoRssPoint.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "point", false, false, false));
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GeoRssWhere.class, new XmlNamespace("georss",
        "http://www.georss.org/georss"), "where", false, false, false));
    new GeoRssWhere().declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GmlEnvelope.class, new XmlNamespace("gml",
        "http://www.opengis.net/gml"), "Envelope", false, false, false));
    new GmlEnvelope().declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(GmlPoint.class, new XmlNamespace("gml",
        "http://www.opengis.net/gml"), "Point", false, false, false));
    new GmlPoint().declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class, GphotoLocation.class);
    extProfile.declare(AlbumEntry.class,
        new ExtensionDescription(MediaGroup.class, new XmlNamespace("media",
        "http://search.yahoo.com/mrss/"), "group", false, false, false));
    new MediaGroup().declareExtensions(extProfile);
    extProfile.declare(AlbumEntry.class, GphotoName.class);
    extProfile.declare(AlbumEntry.class, GphotoNickname.class);
    extProfile.declare(AlbumEntry.class, GphotoPhotosLeft.class);
    extProfile.declare(AlbumEntry.class, GphotoPhotosUsed.class);
    extProfile.declare(AlbumEntry.class, GphotoUsername.class);
  }

  /**
   * Returns the access level for the album.
   *
   * @return access level for the album
   */
  public GphotoAccess getAccessExt() {
    return getExtension(GphotoAccess.class);
  }

  /**
   * Sets the access level for the album.
   *
   * @param accessExt access level for the album or <code>null</code> to reset
   */
  public void setAccessExt(GphotoAccess accessExt) {
    if (accessExt == null) {
      removeExtension(GphotoAccess.class);
    } else {
      setExtension(accessExt);
    }
  }

  /**
   * Returns whether it has the access level for the album.
   *
   * @return whether it has the access level for the album
   */
  public boolean hasAccessExt() {
    return hasExtension(GphotoAccess.class);
  }

  /**
   * Returns the number of bytes used by this album.
   *
   * @return number of bytes used by this album
   */
  public GphotoBytesUsed getBytesUsedExt() {
    return getExtension(GphotoBytesUsed.class);
  }

  /**
   * Sets the number of bytes used by this album.
   *
   * @param bytesUsedExt number of bytes used by this album or <code>null</code>
   *     to reset
   */
  public void setBytesUsedExt(GphotoBytesUsed bytesUsedExt) {
    if (bytesUsedExt == null) {
      removeExtension(GphotoBytesUsed.class);
    } else {
      setExtension(bytesUsedExt);
    }
  }

  /**
   * Returns whether it has the number of bytes used by this album.
   *
   * @return whether it has the number of bytes used by this album
   */
  public boolean hasBytesUsedExt() {
    return hasExtension(GphotoBytesUsed.class);
  }

  /**
   * Returns the number of comments on this album.
   *
   * @return number of comments on this album
   */
  public GphotoCommentCount getCommentCountExt() {
    return getExtension(GphotoCommentCount.class);
  }

  /**
   * Sets the number of comments on this album.
   *
   * @param commentCountExt number of comments on this album or
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
   * Returns whether it has the number of comments on this album.
   *
   * @return whether it has the number of comments on this album
   */
  public boolean hasCommentCountExt() {
    return hasExtension(GphotoCommentCount.class);
  }

  /**
   * Returns the comments enabled field, which is true if comments are enabled
   * for this album.
   *
   * @return comments enabled field, which is true if comments are enabled for
   *     this album
   */
  public GphotoCommentsEnabled getCommentsEnabledExt() {
    return getExtension(GphotoCommentsEnabled.class);
  }

  /**
   * Sets the comments enabled field, which is true if comments are enabled for
   * this album.
   *
   * @param commentsEnabledExt comments enabled field, which is true if comments
   *     are enabled for this album or <code>null</code> to reset
   */
  public void setCommentsEnabledExt(GphotoCommentsEnabled commentsEnabledExt) {
    if (commentsEnabledExt == null) {
      removeExtension(GphotoCommentsEnabled.class);
    } else {
      setExtension(commentsEnabledExt);
    }
  }

  /**
   * Returns whether it has the comments enabled field, which is true if
   * comments are enabled for this album.
   *
   * @return whether it has the comments enabled field, which is true if
   *     comments are enabled for this album
   */
  public boolean hasCommentsEnabledExt() {
    return hasExtension(GphotoCommentsEnabled.class);
  }

  /**
   * Returns the user date of the album.
   *
   * @return user date of the album
   */
  public GphotoTimestamp getDateExt() {
    return getExtension(GphotoTimestamp.class);
  }

  /**
   * Sets the user date of the album.
   *
   * @param dateExt user date of the album or <code>null</code> to reset
   */
  public void setDateExt(GphotoTimestamp dateExt) {
    if (dateExt == null) {
      removeExtension(GphotoTimestamp.class);
    } else {
      setExtension(dateExt);
    }
  }

  /**
   * Returns whether it has the user date of the album.
   *
   * @return whether it has the user date of the album
   */
  public boolean hasDateExt() {
    return hasExtension(GphotoTimestamp.class);
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
   * Returns the textual location of the album.
   *
   * @return textual location of the album
   */
  public GphotoLocation getLocationExt() {
    return getExtension(GphotoLocation.class);
  }

  /**
   * Sets the textual location of the album.
   *
   * @param locationExt textual location of the album or <code>null</code> to
   *     reset
   */
  public void setLocationExt(GphotoLocation locationExt) {
    if (locationExt == null) {
      removeExtension(GphotoLocation.class);
    } else {
      setExtension(locationExt);
    }
  }

  /**
   * Returns whether it has the textual location of the album.
   *
   * @return whether it has the textual location of the album
   */
  public boolean hasLocationExt() {
    return hasExtension(GphotoLocation.class);
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
   * Returns the canonical name of the album.
   *
   * @return canonical name of the album
   */
  public GphotoName getNameExt() {
    return getExtension(GphotoName.class);
  }

  /**
   * Sets the canonical name of the album.
   *
   * @param nameExt canonical name of the album or <code>null</code> to reset
   */
  public void setNameExt(GphotoName nameExt) {
    if (nameExt == null) {
      removeExtension(GphotoName.class);
    } else {
      setExtension(nameExt);
    }
  }

  /**
   * Returns whether it has the canonical name of the album.
   *
   * @return whether it has the canonical name of the album
   */
  public boolean hasNameExt() {
    return hasExtension(GphotoName.class);
  }

  /**
   * Returns the nickname of the owner of the album.
   *
   * @return nickname of the owner of the album
   */
  public GphotoNickname getNicknameExt() {
    return getExtension(GphotoNickname.class);
  }

  /**
   * Sets the nickname of the owner of the album.
   *
   * @param nicknameExt nickname of the owner of the album or <code>null</code>
   *     to reset
   */
  public void setNicknameExt(GphotoNickname nicknameExt) {
    if (nicknameExt == null) {
      removeExtension(GphotoNickname.class);
    } else {
      setExtension(nicknameExt);
    }
  }

  /**
   * Returns whether it has the nickname of the owner of the album.
   *
   * @return whether it has the nickname of the owner of the album
   */
  public boolean hasNicknameExt() {
    return hasExtension(GphotoNickname.class);
  }

  /**
   * Returns the number of photos that can be uploaded to this album.
   *
   * @return number of photos that can be uploaded to this album
   */
  public GphotoPhotosLeft getPhotosLeftExt() {
    return getExtension(GphotoPhotosLeft.class);
  }

  /**
   * Sets the number of photos that can be uploaded to this album.
   *
   * @param photosLeftExt number of photos that can be uploaded to this album or
   *     <code>null</code> to reset
   */
  public void setPhotosLeftExt(GphotoPhotosLeft photosLeftExt) {
    if (photosLeftExt == null) {
      removeExtension(GphotoPhotosLeft.class);
    } else {
      setExtension(photosLeftExt);
    }
  }

  /**
   * Returns whether it has the number of photos that can be uploaded to this
   * album.
   *
   * @return whether it has the number of photos that can be uploaded to this
   *     album
   */
  public boolean hasPhotosLeftExt() {
    return hasExtension(GphotoPhotosLeft.class);
  }

  /**
   * Returns the number of photos that have been uploaded to this album.
   *
   * @return number of photos that have been uploaded to this album
   */
  public GphotoPhotosUsed getPhotosUsedExt() {
    return getExtension(GphotoPhotosUsed.class);
  }

  /**
   * Sets the number of photos that have been uploaded to this album.
   *
   * @param photosUsedExt number of photos that have been uploaded to this album
   *     or <code>null</code> to reset
   */
  public void setPhotosUsedExt(GphotoPhotosUsed photosUsedExt) {
    if (photosUsedExt == null) {
      removeExtension(GphotoPhotosUsed.class);
    } else {
      setExtension(photosUsedExt);
    }
  }

  /**
   * Returns whether it has the number of photos that have been uploaded to this
   * album.
   *
   * @return whether it has the number of photos that have been uploaded to this
   *     album
   */
  public boolean hasPhotosUsedExt() {
    return hasExtension(GphotoPhotosUsed.class);
  }

  /**
   * Returns the username of the owner of the album.
   *
   * @return username of the owner of the album
   */
  public GphotoUsername getUsernameExt() {
    return getExtension(GphotoUsername.class);
  }

  /**
   * Sets the username of the owner of the album.
   *
   * @param usernameExt username of the owner of the album or <code>null</code>
   *     to reset
   */
  public void setUsernameExt(GphotoUsername usernameExt) {
    if (usernameExt == null) {
      removeExtension(GphotoUsername.class);
    } else {
      setExtension(usernameExt);
    }
  }

  /**
   * Returns whether it has the username of the owner of the album.
   *
   * @return whether it has the username of the owner of the album
   */
  public boolean hasUsernameExt() {
    return hasExtension(GphotoUsername.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{AlbumEntry " + super.toString() + "}";
  }


  /**
   * Retrieve the album feed and associated entries.  The kinds parameter is a
   * list of the associated entries to return.  For example
   * <code>AlbumFeed albumAndPhotos = albumEntry.getFeed(PhotoData.KIND,
   *     TagData.KIND);</code>  If no kind parameters are passed, the default of
   * {@link PhotoData#KIND} will be used.
   *
   * @see PhotoData#KIND
   * @see TagData#KIND
   * @param kinds the kinds of entries to retrieve, or empty to use the default.
   * @return a feed of the album and the requested kinds.
   */
  public AlbumFeed getFeed(String... kinds)
      throws IOException, ServiceException {
    return getFeed(AlbumFeed.class, kinds);
  }

  public String getAccess() {
    GphotoAccess access = getAccessExt();
    return access == null ? null : access.getValue().toLowerCase();
  }

  public Long getBytesUsed() {
    GphotoBytesUsed bytesUsed = getBytesUsedExt();
    return bytesUsed == null ? null : bytesUsed.getValue();
  }

  public Integer getCommentCount() {
    GphotoCommentCount commentCount = getCommentCountExt();
    return commentCount == null ? null : commentCount.getValue();
  }

  public Boolean getCommentsEnabled() {
    GphotoCommentsEnabled commentsEnabled = getCommentsEnabledExt();
    return commentsEnabled == null ? null : commentsEnabled.getValue();
  }

  public Date getDate() {
    GphotoTimestamp date = getDateExt();
    if (date == null) {
      return null;
    }
    return new Date(date.getValue());
  }

  public String getLocation() {
    GphotoLocation location = getLocationExt();
    return location == null ? null : location.getValue();
  }

  public String getName() {
    GphotoName name = getNameExt();
    return name == null ? null : name.getValue();
  }

  public String getNickname() {
    GphotoNickname nickname = getNicknameExt();
    return nickname == null ? null : nickname.getValue();
  }

  public Integer getPhotosLeft() {
    GphotoPhotosLeft photosLeft = getPhotosLeftExt();
    return photosLeft == null ? null : photosLeft.getValue();
  }

  public Integer getPhotosUsed() {
    GphotoPhotosUsed photosUsed = getPhotosUsedExt();
    return photosUsed == null ? null : photosUsed.getValue();
  }

  public String getUsername() {
    GphotoUsername username = getUsernameExt();
    return username == null ? null : username.getValue();
  }

  public void setAccess(String access) {
    GphotoAccess accessExt = null;
    if (access != null) {
      accessExt = new GphotoAccess(access);
    }
    setAccessExt(accessExt);
  }

  public void setBytesUsed(Long bytesUsed) {
    GphotoBytesUsed usedExt = null;
    if (bytesUsed != null) {
      usedExt = new GphotoBytesUsed(bytesUsed);
    }
    setBytesUsedExt(usedExt);
  }

  public void setCommentCount(Integer commentCount) {
    GphotoCommentCount countExt = null;
    if (commentCount != null) {
      countExt = new GphotoCommentCount(commentCount);
    }
    setCommentCountExt(countExt);
  }

  public void setCommentsEnabled(Boolean commentsEnabled) {
    GphotoCommentsEnabled enabledExt = null;
    if (commentsEnabled != null) {
      enabledExt = new GphotoCommentsEnabled(commentsEnabled);
    }
    setCommentsEnabledExt(enabledExt);
  }

  public void setDate(Date date) {
    GphotoTimestamp dateExt = null;
    if (date != null) {
      dateExt = new GphotoTimestamp(date);
    }
    setDateExt(dateExt);
  }

  public void setLocation(String location) {
    GphotoLocation locExt = null;
    if (location != null) {
      locExt = new GphotoLocation(location);
    }
    setLocationExt(locExt);
  }

  public void setName(String name) {
    GphotoName nameExt = null;
    if (name != null) {
      nameExt = new GphotoName(name);
    }
    setNameExt(nameExt);
  }

  public void setNickname(String nickname) {
    GphotoNickname nicknameExt = null;
    if (nickname != null) {
      nicknameExt = new GphotoNickname(nickname);
    }
    setNicknameExt(nicknameExt);
  }

  public void setPhotosLeft(Integer photosLeft) {
    GphotoPhotosLeft leftExt = null;
    if (photosLeft != null) {
      leftExt = new GphotoPhotosLeft(photosLeft);
    }
    setPhotosLeftExt(leftExt);
  }

  public void setPhotosUsed(Integer photosUsed) {
    GphotoPhotosUsed usedExt = null;
    if (photosUsed != null) {
      usedExt = new GphotoPhotosUsed(photosUsed);
    }
    setPhotosUsedExt(usedExt);
  }

  public void setUsername(String username) {
    GphotoUsername usernameExt = null;
    if (username != null) {
      usernameExt = new GphotoUsername(username);
    }
    setUsernameExt(usernameExt);
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
}
