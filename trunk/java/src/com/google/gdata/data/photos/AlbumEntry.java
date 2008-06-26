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
import com.google.gdata.data.geo.Point;
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
 * Entry class for entries of the album kind.  This represents a particular
 * album in a user's Picasawebalbums account.
 *
 * 
 */
@Kind.Term(AlbumData.ALBUM_KIND)
public class AlbumEntry extends GphotoEntry<AlbumEntry>
    implements AlbumData, AtomData {

  private final AlbumData delegate;

  /**
   * Construct a new empty album entry.
   */
  public AlbumEntry() {
    super();
    getCategories().add(AlbumData.ALBUM_CATEGORY);
    this.delegate = new AlbumDataImpl(this);
  }

  /**
   * Construct a new album entry doing a shallow copy of the data in the
   * passed in source entry.
   */
  public AlbumEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(AlbumData.ALBUM_CATEGORY);
    this.delegate = new AlbumDataImpl(this);
  }

  /*
   * Declare all extensions on our delegate.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
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
