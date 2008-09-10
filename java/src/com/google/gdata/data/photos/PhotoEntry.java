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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.impl.PhotoDataImpl;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Entry class for photos.  Contains getters and setters for all of the fields
 * that we support on a photo data object.
 *
 * 
 */
@Kind.Term(PhotoData.PHOTO_KIND)
public class PhotoEntry extends GphotoEntry<PhotoEntry> implements PhotoData,
    AtomData {

  private final PhotoData delegate;

  /**
   * Constructs a new empty photo entry.
   */
  public PhotoEntry() {
    super();
    getCategories().add(PhotoData.PHOTO_CATEGORY);
    this.delegate = new PhotoDataImpl(this);
  }

  /**
   * Construct a new photo entry doing a shallow copy of the data in the
   * passed in source entry.
   */
  public PhotoEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(PhotoData.PHOTO_CATEGORY);
    this.delegate = new PhotoDataImpl(this);
  }

  /*
   * Declare all of the extensions on the photo entry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  /**
   * Retrieve the photo feed and associated entries.  The kinds parameter is a
   * list of the associated entries to return.  For example
   * <code>PhotoFeed photoTagsAndComments = photoEntry.getFeed(CommentData.KIND,
   *     TagData.KIND);</code>  If no kind parameters are passed, the default of
   * {@link CommentData#KIND} will be used.
   *
   * @see CommentData#KIND
   * @see TagData#KIND
   * @param kinds the kinds of entries to retrieve, or empty to use the default.
   * @return a feed of the photo and the requested kinds.
   */
  public PhotoFeed getFeed(String... kinds)
      throws IOException, ServiceException {
    return getFeed(PhotoFeed.class, kinds);
  }

  // Delegating methods.

  public String getAlbumId() {
    return delegate.getAlbumId();
  }
  
  public String getVideoStatus() {
    return delegate.getVideoStatus();
  }
  
  public String getChecksum() {
    return delegate.getChecksum();
  }

  public String getClient() {
    return delegate.getClient();
  }

  public Integer getCommentCount() throws ServiceException {
    return delegate.getCommentCount();
  }

  public Boolean getCommentsEnabled() throws ServiceException {
    return delegate.getCommentsEnabled();
  }

  public ExifTags getExifTags() {
    return delegate.getExifTags();
  }

  public Long getHeight() throws ServiceException {
    return delegate.getHeight();
  }

  public Float getPosition() throws ServiceException {
    return delegate.getPosition();
  }

  public Integer getRotation() throws ServiceException {
    return delegate.getRotation();
  }

  public Long getSize() throws ServiceException {
    return delegate.getSize();
  }

  public Date getTimestamp() throws ServiceException {
    return delegate.getTimestamp();
  }

  public Long getVersion() throws ServiceException {
    return delegate.getVersion();
  }

  public Long getWidth() throws ServiceException {
    return delegate.getWidth();
  }

  public void setAlbumId(Long albumId) {
    delegate.setAlbumId(albumId);
  }

  public void setAlbumId(String albumId) {
    delegate.setAlbumId(albumId);
  }

  public void setVideoStatus(String videoStatus) {
    delegate.setVideoStatus(videoStatus);
  }  
  
  public void setChecksum(String checksum) {
    delegate.setChecksum(checksum);
  }

  public void setClient(String client) {
    delegate.setClient(client);
  }

  public void setCommentCount(Integer commentCount) {
    delegate.setCommentCount(commentCount);
  }

  public void setCommentsEnabled(Boolean commentsEnabled) {
    delegate.setCommentsEnabled(commentsEnabled);
  }

  public void setExifTags(ExifTags tags) {
    delegate.setExifTags(tags);
  }

  public void setHeight(Long height) {
    delegate.setHeight(height);
  }

  public void setPosition(Float position) {
    delegate.setPosition(position);
  }

  public void setRotation(Integer rotation) {
    delegate.setRotation(rotation);
  }

  public void setSize(Long size) {
    delegate.setSize(size);
  }

  public void setTimestamp(Date timestamp) {
    delegate.setTimestamp(timestamp);
  }

  public void setVersion(Long version) {
    delegate.setVersion(version);
  }

  public void setWidth(Long width) {
    delegate.setWidth(width);
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
  
  public void addStreamId(String streamId) {
    delegate.addStreamId(streamId);
  }

  public List<String> getStreamIds() {
    return delegate.getStreamIds();
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
