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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.ServiceException;

import java.io.IOException;

/**
 * Entry for user kinds, contains user metadata.
 *
 * 
 */
@Kind.Term(UserEntry.KIND)
public class UserEntry extends GphotoEntry<UserEntry> implements AtomData,
    UserData {

  /**
   * User kind term value.
   */
  public static final String KIND = Namespaces.PHOTOS_PREFIX + "user";

  /**
   * User kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public UserEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public UserEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(UserEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(UserEntry.class, GphotoMaxPhotos.class);
    extProfile.declare(UserEntry.class, GphotoNickname.class);
    extProfile.declare(UserEntry.class, GphotoQuotaLimit.class);
    extProfile.declare(UserEntry.class, GphotoQuotaUsed.class);
    extProfile.declare(UserEntry.class, GphotoThumbnail.class);
    extProfile.declare(UserEntry.class, GphotoUsername.class);
  }

  /**
   * Returns the maximum number of photos allowed per album.
   *
   * @return maximum number of photos allowed per album
   */
  public GphotoMaxPhotos getMaxPhotosExt() {
    return getExtension(GphotoMaxPhotos.class);
  }

  /**
   * Sets the maximum number of photos allowed per album.
   *
   * @param maxPhotosExt maximum number of photos allowed per album or
   *     <code>null</code> to reset
   */
  public void setMaxPhotosExt(GphotoMaxPhotos maxPhotosExt) {
    if (maxPhotosExt == null) {
      removeExtension(GphotoMaxPhotos.class);
    } else {
      setExtension(maxPhotosExt);
    }
  }

  /**
   * Returns whether it has the maximum number of photos allowed per album.
   *
   * @return whether it has the maximum number of photos allowed per album
   */
  public boolean hasMaxPhotosExt() {
    return hasExtension(GphotoMaxPhotos.class);
  }

  /**
   * Returns the display nickname of the user.
   *
   * @return display nickname of the user
   */
  public GphotoNickname getNicknameExt() {
    return getExtension(GphotoNickname.class);
  }

  /**
   * Sets the display nickname of the user.
   *
   * @param nicknameExt display nickname of the user or <code>null</code> to
   *     reset
   */
  public void setNicknameExt(GphotoNickname nicknameExt) {
    if (nicknameExt == null) {
      removeExtension(GphotoNickname.class);
    } else {
      setExtension(nicknameExt);
    }
  }

  /**
   * Returns whether it has the display nickname of the user.
   *
   * @return whether it has the display nickname of the user
   */
  public boolean hasNicknameExt() {
    return hasExtension(GphotoNickname.class);
  }

  /**
   * Returns the limit in bytes of the storage space for the user.
   *
   * @return limit in bytes of the storage space for the user
   */
  public GphotoQuotaLimit getQuotaLimitExt() {
    return getExtension(GphotoQuotaLimit.class);
  }

  /**
   * Sets the limit in bytes of the storage space for the user.
   *
   * @param quotaLimitExt limit in bytes of the storage space for the user or
   *     <code>null</code> to reset
   */
  public void setQuotaLimitExt(GphotoQuotaLimit quotaLimitExt) {
    if (quotaLimitExt == null) {
      removeExtension(GphotoQuotaLimit.class);
    } else {
      setExtension(quotaLimitExt);
    }
  }

  /**
   * Returns whether it has the limit in bytes of the storage space for the
   * user.
   *
   * @return whether it has the limit in bytes of the storage space for the user
   */
  public boolean hasQuotaLimitExt() {
    return hasExtension(GphotoQuotaLimit.class);
  }

  /**
   * Returns the currently used quota of the user account.
   *
   * @return currently used quota of the user account
   */
  public GphotoQuotaUsed getQuotaUsedExt() {
    return getExtension(GphotoQuotaUsed.class);
  }

  /**
   * Sets the currently used quota of the user account.
   *
   * @param quotaUsedExt currently used quota of the user account or
   *     <code>null</code> to reset
   */
  public void setQuotaUsedExt(GphotoQuotaUsed quotaUsedExt) {
    if (quotaUsedExt == null) {
      removeExtension(GphotoQuotaUsed.class);
    } else {
      setExtension(quotaUsedExt);
    }
  }

  /**
   * Returns whether it has the currently used quota of the user account.
   *
   * @return whether it has the currently used quota of the user account
   */
  public boolean hasQuotaUsedExt() {
    return hasExtension(GphotoQuotaUsed.class);
  }

  /**
   * Returns the user portrait thumbnail.
   *
   * @return user portrait thumbnail
   */
  public GphotoThumbnail getThumbnailExt() {
    return getExtension(GphotoThumbnail.class);
  }

  /**
   * Sets the user portrait thumbnail.
   *
   * @param thumbnailExt user portrait thumbnail or <code>null</code> to reset
   */
  public void setThumbnailExt(GphotoThumbnail thumbnailExt) {
    if (thumbnailExt == null) {
      removeExtension(GphotoThumbnail.class);
    } else {
      setExtension(thumbnailExt);
    }
  }

  /**
   * Returns whether it has the user portrait thumbnail.
   *
   * @return whether it has the user portrait thumbnail
   */
  public boolean hasThumbnailExt() {
    return hasExtension(GphotoThumbnail.class);
  }

  /**
   * Returns the username or currently selected persona.
   *
   * @return username or currently selected persona
   */
  public GphotoUsername getUsernameExt() {
    return getExtension(GphotoUsername.class);
  }

  /**
   * Sets the username or currently selected persona.
   *
   * @param usernameExt username or currently selected persona or
   *     <code>null</code> to reset
   */
  public void setUsernameExt(GphotoUsername usernameExt) {
    if (usernameExt == null) {
      removeExtension(GphotoUsername.class);
    } else {
      setExtension(usernameExt);
    }
  }

  /**
   * Returns whether it has the username or currently selected persona.
   *
   * @return whether it has the username or currently selected persona
   */
  public boolean hasUsernameExt() {
    return hasExtension(GphotoUsername.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{UserEntry " + super.toString() + "}";
  }


  /**
   * Retrieve the user feed and associated entries.  The kinds parameter is a
   * list of the associated entries to return.  For example
   * <code>UserFeed userAndPhotos = userEntry.getFeed(PhotoData.KIND,
   *     TagData.KIND);</code>  If no kind parameters are passed, the default of
   * {@link AlbumData#KIND} will be used.
   *
   * @see AlbumData#KIND
   * @see PhotoData#KIND
   * @see TagData#KIND
   * @param kinds the kinds of entries to retrieve, or empty to use the default.
   * @return a feed of the album and the requested kinds.
   */
  public UserFeed getFeed(String... kinds)
      throws IOException, ServiceException {
    return getFeed(UserFeed.class, kinds);
  }

  public Integer getMaxPhotos() {
    GphotoMaxPhotos ext = getMaxPhotosExt();
    return ext == null ? null : ext.getValue();
  }

  public String getNickname() {
    GphotoNickname ext = getNicknameExt();
    return ext == null ? null : ext.getValue();
  }

  public Long getQuotaLimit() {
    GphotoQuotaLimit ext = getQuotaLimitExt();
    return ext == null ? null : ext.getValue();
  }

  public Long getQuotaUsed() {
    GphotoQuotaUsed ext = getQuotaUsedExt();
    return ext == null ? null : ext.getValue();
  }

  public String getThumbnail() {
    GphotoThumbnail ext = getThumbnailExt();
    return ext == null ? null : ext.getValue();
  }

  public String getUsername() {
    GphotoUsername ext = getUsernameExt();
    return ext == null ? null : ext.getValue();
  }

  public void setMaxPhotos(Integer max) {
    GphotoMaxPhotos ext = null;
    if (max != null) {
      ext = new GphotoMaxPhotos(max);
    }
    setMaxPhotosExt(ext);
  }

  public void setNickname(String nickname) {
    GphotoNickname ext = null;
    if (nickname != null) {
      ext = new GphotoNickname(nickname);
    }
    setNicknameExt(ext);
  }

  public void setQuotaLimit(Long quota) {
    GphotoQuotaLimit ext = null;
    if (quota != null) {
      ext = new GphotoQuotaLimit(quota);
    }
    setQuotaLimitExt(ext);
  }

  public void setQuotaUsed(Long quota) {
    GphotoQuotaUsed ext = null;
    if (quota != null) {
      ext = new GphotoQuotaUsed(quota);
    }
    setQuotaUsedExt(ext);
  }

  public void setThumbnail(String thumbnail) {
    GphotoThumbnail ext = null;
    if (thumbnail != null) {
      ext = new GphotoThumbnail(thumbnail);
    }
    setThumbnailExt(ext);
  }

  public void setUsername(String username) {
    GphotoUsername ext = null;
    if (username != null) {
      ext = new GphotoUsername(username);
    }
    setUsernameExt(ext);
  }
}
