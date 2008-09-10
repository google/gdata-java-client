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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.impl.AlbumDataImpl;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Feed for an Album in our google photos api.  This feed represents an album
 * as the container for other objects.  An Album feed contains entries
 * of {@link PhotoEntry} or {@link TagEntry} kind.  The album feed itself also
 * contains all of the metadata available as part of an {@link AlbumData}
 * object.
 *
 * 
 */
@Kind.Term(AlbumData.ALBUM_KIND)
public class AlbumFeed extends GphotoFeed<AlbumFeed>
    implements AlbumData, AtomData {

  private final AlbumData delegate;

  /**
   * Constructs a new empty album feed.
   */
  public AlbumFeed() {
    super();
    getCategories().add(AlbumData.ALBUM_CATEGORY);
    this.delegate = new AlbumDataImpl(this);
  }

  /**
   * Constructs a new album feed from a shallow copy of the data in the source
   * feed.  This is used to get the correct entry type based on the category of
   * the entry.
   */
  public AlbumFeed(BaseFeed<?, ?> sourceFeed) {
    super(sourceFeed);
    getCategories().add(AlbumData.ALBUM_CATEGORY);
    this.delegate = new AlbumDataImpl(this);
  }

  /*
   * Declare all of the extensions on the album feed.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  /**
   * Insert a photo into this album by inserting a {@link MediaSource}
   * containing the photo data.
   */
  public PhotoEntry insertPhoto(MediaSource photoSource)
      throws ServiceException, IOException {
    return super.insert(photoSource, PhotoEntry.class);
  }

  /**
   * Get a list of entries of the {@link PhotoEntry} kind.
   */
  public List<PhotoEntry> getPhotoEntries() {
    return getEntries(PhotoEntry.class);
  }

  /**
   * Get a list of entries of the {@link TagEntry} kind.
   */
  public List<TagEntry> getTagEntries() {
    return getEntries(TagEntry.class);
  }

  // Delegating methods.

  public String getAccess() {
    return delegate.getAccess();
  }

  public Long getBytesUsed() throws ServiceException {
    return delegate.getBytesUsed();
  }

  public Integer getCommentCount() throws ServiceException {
    return delegate.getCommentCount();
  }

  public Boolean getCommentsEnabled() throws ServiceException {
    return delegate.getCommentsEnabled();
  }

  public Date getDate() throws ServiceException {
    return delegate.getDate();
  }

  public String getLocation() {
    return delegate.getLocation();
  }

  public String getName() {
    return delegate.getName();
  }

  public String getNickname() {
    return delegate.getNickname();
  }

  public Integer getPhotosLeft() throws ServiceException {
    return delegate.getPhotosLeft();
  }

  public Integer getPhotosUsed() throws ServiceException {
    return delegate.getPhotosUsed();
  }

  public String getUsername() {
    return delegate.getUsername();
  }

  public void setAccess(String access) {
    delegate.setAccess(access);
  }

  public void setBytesUsed(Long bytesUsed) {
    delegate.setBytesUsed(bytesUsed);
  }

  public void setCommentCount(Integer commentCount) {
    delegate.setCommentCount(commentCount);
  }

  public void setCommentsEnabled(Boolean commentsEnabled) {
    delegate.setCommentsEnabled(commentsEnabled);
  }

  public void setDate(Date date) {
    delegate.setDate(date);
  }

  public void setLocation(String location) {
    delegate.setLocation(location);
  }

  public void setName(String name) {
    delegate.setName(name);
  }

  public void setNickname(String nickname) {
    delegate.setNickname(nickname);
  }

  public void setPhotosLeft(Integer photosLeft) {
    delegate.setPhotosLeft(photosLeft);
  }

  public void setPhotosUsed(Integer photosUsed) {
    delegate.setPhotosUsed(photosUsed);
  }

  public void setUsername(String username) {
    delegate.setUsername(username);
  }
  
  public void setGeoLocation(Double lat, Double lon)
      throws IllegalArgumentException {
    delegate.setGeoLocation(lat, lon);
  }

  public void setGeoLocation(Point point) {
    delegate.setGeoLocation(point);
  }
  
  public Point getGeoLocation() {
    return delegate.getGeoLocation();
  }
  
  public void clearPoint() {
    delegate.clearPoint();
  }
  
  public void clearGeoBoundingBox() {
    delegate.clearGeoBoundingBox();
  }

  public Box getGeoBoundingBox() {
    return delegate.getGeoBoundingBox();
  }

  public void setGeoBoundingBox(Point lowerLeft, Point upperRight) {
    delegate.setGeoBoundingBox(lowerLeft, upperRight);
  }

  public void setGeoBoundingBox(Box boundingBox) {
    delegate.setGeoBoundingBox(boundingBox);
  }
  
  public MediaGroup getMediaGroup() {
    return delegate.getMediaGroup();
  }

  public List<MediaContent> getMediaContents() {
    return delegate.getMediaContents();
  }

  public List<MediaCategory> getMediaCategories() {
    return delegate.getMediaCategories();
  }
  
  public List<MediaCredit> getMediaCredits() {
    return delegate.getMediaCredits();
  }
  
  public List<MediaThumbnail> getMediaThumbnails() {
    return delegate.getMediaThumbnails();
  }
  
  public MediaKeywords getMediaKeywords() {
    return delegate.getMediaKeywords();
  }
  
  public void setKeywords(MediaKeywords keywords) {
    delegate.setKeywords(keywords);
  }
}
