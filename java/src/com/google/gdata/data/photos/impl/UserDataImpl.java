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
import com.google.gdata.data.photos.Namespaces;
import com.google.gdata.data.photos.UserData;
import com.google.gdata.data.photos.impl.Extensions.GphotoConstruct;
import com.google.gdata.data.photos.impl.Extensions.GphotoNickname;
import com.google.gdata.data.photos.impl.Extensions.GphotoThumbnail;
import com.google.gdata.data.photos.impl.Extensions.GphotoUsername;

/**
 * Implementation class for user data objects.  This class takes an
 * {@link ExtensionPoint} and uses it to provide all of the methods that
 * {@link UserData} specifies.  These methods are handled by using
 * extension classes to retrieve or set extensions of the appropriate type.
 *
 * 
 */
public class UserDataImpl extends GphotoDataImpl implements UserData {

  /**
   * Construct a new implementation of UserGphotoData with the given
   * extension point as the backing storage for data.
   */
  public UserDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
  }

  /*
   * Declare the extensions the user object uses.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    declare(extProfile, GphotoUsername.getDefaultDescription());
    declare(extProfile, GphotoNickname.getDefaultDescription());
    declare(extProfile, GphotoThumbnail.getDefaultDescription());
    declare(extProfile, GphotoQuotaUsed.getDefaultDescription());
    declare(extProfile, GphotoQuotaLimit.getDefaultDescription());
    declare(extProfile, GphotoMaxPhotos.getDefaultDescription());
  }

  /**
   * @return the username of the user this item represents.
   */
  public String getUsername() {
    return getSimpleValue(GphotoUsername.class);
  }

  /**
   * Set the username for the user this item represents.
   *
   * @param username the username of the user.
   */
  public void setUsername(String username) {
    if (username != null) {
      setExtension(new GphotoUsername(username));
    } else {
      removeExtension(GphotoUsername.class);
    }
  }

  /**
   * @return the nickname of the user this item represents.
   */
  public String getNickname() {
    return getSimpleValue(GphotoNickname.class);
  }

  /**
   * Set the nickname of the user this item represents.
   *
   * @param nickname the nickname of the user.
   */
  public void setNickname(String nickname) {
    if (nickname != null) {
      setExtension(new GphotoNickname(nickname));
    } else {
      removeExtension(GphotoNickname.class);
    }
  }

  /**
   * @return the thumbnail for the user this item represents.
   */
  public String getThumbnail() {
    return getSimpleValue(GphotoThumbnail.class);
  }

  /**
   * Set the thumbnail for the user portrait.
   *
   * @param thumbnail the url to the user portrait image.
   */
  public void setThumbnail(String thumbnail) {
    if (thumbnail != null) {
      setExtension(new GphotoThumbnail(thumbnail));
    } else {
      removeExtension(GphotoThumbnail.class);
    }
  }

  /**
   * @return the quota used up for this user.
   */
  public Long getQuotaUsed() {
    String value = getSimpleValue(GphotoQuotaUsed.class);
    return value == null ? null : Long.valueOf(value);
  }

  /**
   * Set the used quota for the user.
   *
   * @param quota the quota usage in bytes for this user.
   */
  public void setQuotaUsed(Long quota) {
    if (quota != null) {
      setExtension(new GphotoQuotaUsed(quota));
    } else {
      removeExtension(GphotoQuotaUsed.class);
    }
  }

  /**
   * @return the quota limit for the user in bytes.
   */
  public Long getQuotaLimit() {
    String quota = getSimpleValue(GphotoQuotaLimit.class);
    return quota == null ? null : Long.valueOf(quota);
  }

  /**
   * Set the quota limit for the user this item represents.
   *
   * @param quota the quota in bytes for the user.
   */
  public void setQuotaLimit(Long quota) {
    if (quota != null) {
      setExtension(new GphotoQuotaLimit(quota));
    } else {
      removeExtension(GphotoQuotaLimit.class);
    }
  }

  /**
   * @return the maximum number of photos the user can have in an album.
   */
  public Integer getMaxPhotos() {
    String max = getSimpleValue(GphotoMaxPhotos.class);
    return max == null ? null : Integer.valueOf(max);
  }

  /**
   * Set the maximum number of photos the user is allowed.
   *
   * @param max the number of photos the user is allowed.
   */
  public void setMaxPhotos(Integer max) {
    if (max != null) {
      setExtension(new GphotoMaxPhotos(max));
    } else {
      removeExtension(GphotoMaxPhotos.class);
    }
  }

  /**
   * The gphoto:quotacurrent field.
   */
  public static class GphotoQuotaUsed extends GphotoConstruct {
    public GphotoQuotaUsed() {
      this(null);
    }

    public GphotoQuotaUsed(Long quota) {
      super("quotacurrent", quota == null ? null : Long.toString(quota));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoQuotaUsed.class,
          Namespaces.PHOTOS_NAMESPACE, "quotacurrent");
    }
  }

  /**
   * The gphoto:quotalimit field.
   */
  public static class GphotoQuotaLimit extends GphotoConstruct {
    public GphotoQuotaLimit() {
      this(null);
    }

    public GphotoQuotaLimit(Long quota) {
      super("quotalimit", quota == null ? null : Long.toString(quota));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoQuotaLimit.class,
          Namespaces.PHOTOS_NAMESPACE, "quotalimit");
    }
  }

  /**
   * The gphoto:maxPhotos field.
   */
  public static class GphotoMaxPhotos extends GphotoConstruct {
    public GphotoMaxPhotos() {
      this(null);
    }

    public GphotoMaxPhotos(Integer max) {
      super("maxPhotosPerAlbum", max == null ? null : Long.toString(max));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoMaxPhotos.class,
          Namespaces.PHOTOS_NAMESPACE, "maxPhotosPerAlbum");
    }
  }
}
