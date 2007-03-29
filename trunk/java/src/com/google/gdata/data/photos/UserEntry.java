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
import com.google.gdata.data.photos.impl.UserDataImpl;

/**
 * Entry class specific to the user kind.  Contains setters and getters for
 * all fields specific to user data.
 *
 * 
 */
@Kind.Term(UserData.USER_KIND)
public class UserEntry extends GphotoEntry<UserEntry> implements UserData,
    AtomData {

  private final UserData delegate;

  /**
   * Constructs a new empty user entry.
   */
  public UserEntry() {
    super();
    getCategories().add(UserData.USER_CATEGORY);
    this.delegate = new UserDataImpl(this);
  }

  /**
   * Construct a new user entry doing a shallow copy of the data in the
   * passed in source entry.
   */
  public UserEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(UserData.USER_CATEGORY);
    this.delegate = new UserDataImpl(this);
  }

  /*
   * Declare the extensions the user entry uses.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
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
