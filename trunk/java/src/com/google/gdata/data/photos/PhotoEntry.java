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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.photos.impl.PhotoDataImpl;
import com.google.gdata.util.ServiceException;

import java.util.Date;

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
  public PhotoEntry(BaseEntry sourceEntry) {
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

  // Delegating methods.

  public String getAlbumId() {
    return delegate.getAlbumId();
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

  public MediaKeywords getKeywords() {
    return delegate.getKeywords();
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

  public void setKeywords(MediaKeywords keywords) {
    delegate.setKeywords(keywords);
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
}
