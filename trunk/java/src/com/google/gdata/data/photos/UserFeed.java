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
import com.google.gdata.data.photos.impl.UserDataImpl;

import java.util.List;

/**
 * Feed for a User in our google photos api.  This feed represents a user
 * as the container for other objects.  A User feed contains entries
 * of {@link AlbumEntry} or {@link TagEntry} kind.  The user feed itself also
 * contains all of the metadata available as part of a {@link UserData} object.
 *
 * 
 */
@Kind.Term(UserData.USER_KIND)
public class UserFeed extends GphotoFeed<UserFeed> implements UserData,
    AtomData {

  private final UserData delegate;

  /**
   * Constructs a new empty user feed.
   */
  public UserFeed() {
    super();
    getCategories().add(UserData.USER_CATEGORY);
    this.delegate = new UserDataImpl(this);
  }

  /**
   * Constructs a new user feed from a shallow copy of the data in the source
   * feed.  This is used to get the correct entry type based on the category of
   * the entry.
   */
  public UserFeed(BaseFeed<?, ?> sourceFeed) {
    super(sourceFeed);
    getCategories().add(UserData.USER_CATEGORY);
    this.delegate = new UserDataImpl(this);
  }

  /*
   * Declare the extensions the user feed uses.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  /**
   * Get a list of entries of the {@link AlbumEntry} kind.
   */
  public List<AlbumEntry> getAlbumEntries() {
    return super.getEntries(AlbumEntry.class);
  }

  /**
   * Get a list of entries of the {@link TagEntry} kind.
   */
  public List<TagEntry> getTagEntries() {
    return super.getEntries(TagEntry.class);
  }

  // Delegating methods.

  public Integer getMaxPhotos() {
    return delegate.getMaxPhotos();
  }

  public String getNickname() {
    return delegate.getNickname();
  }

  public Long getQuotaLimit() {
    return delegate.getQuotaLimit();
  }

  public Long getQuotaUsed() {
    return delegate.getQuotaUsed();
  }

  public String getThumbnail() {
    return delegate.getThumbnail();
  }

  public String getUsername() {
    return delegate.getUsername();
  }

  public void setMaxPhotos(Integer max) {
    delegate.setMaxPhotos(max);
  }

  public void setNickname(String nickname) {
    delegate.setNickname(nickname);
  }

  public void setQuotaLimit(Long quota) {
    delegate.setQuotaLimit(quota);
  }

  public void setQuotaUsed(Long quota) {
    delegate.setQuotaUsed(quota);
  }

  public void setThumbnail(String thumbnail) {
    delegate.setThumbnail(thumbnail);
  }

  public void setUsername(String username) {
    delegate.setUsername(username);
  }
}
