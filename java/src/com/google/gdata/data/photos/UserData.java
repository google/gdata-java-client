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

import com.google.gdata.data.Category;

/**
 * An interface for the user data objects.  This is implemented by both
 * UserEntry and UserFeed, which allows the use of a common interface
 * when handling items of the User kind.
 *
 * 
 */
public interface UserData extends GphotoData {

  /** The unqualified kind for a user. */
  public static final String KIND = "user";

  /** The fully qualified kind term for users. */
  public static final String USER_KIND = Namespaces.PHOTOS_PREFIX + KIND;

  /** A category object for users.  All user objects will have this set. */
  public static final Category USER_CATEGORY
      = new Category(com.google.gdata.util.Namespaces.gKind, USER_KIND);

  /**
   * @return the username of the user this data represents.
   */
  public String getUsername();

  /**
   * Set the username for the user this data represents.
   *
   * @param username the username of the user.
   */
  public void setUsername(String username);

  /**
   * @return the nickname of the user this data represents.
   */
  public String getNickname();

  /**
   * Set the nickname of the user this data represents.
   *
   * @param nickname the nickname of the user.
   */
  public void setNickname(String nickname);

  /**
   * @return the thumbnail for the user this data represents.
   */
  public String getThumbnail();

  /**
   * Set the thumbnail for the user portrait.
   *
   * @param thumbnail the url to the user portrait image.
   */
  public void setThumbnail(String thumbnail);

  /**
   * @return the quota used for this user in bytes.
   */
  public Long getQuotaUsed();

  /**
   * Set the used quota for the user.
   *
   * @param quota the quota usage in bytes for this user.
   */
  public void setQuotaUsed(Long quota);

  /**
   * @return the quota limit for the user in bytes.
   */
  public Long getQuotaLimit();

  /**
   * Set the quota limit for the user this data represents.
   *
   * @param quota the quota in bytes for the user.
   */
  public void setQuotaLimit(Long quota);

  /**
   * @return the maximum number of photos the user can have in an album.
   */
  public Integer getMaxPhotos();

  /**
   * Set the maximum number of photos the user is allowed.
   *
   * @param max the number of photos the user is allowed.
   */
  public void setMaxPhotos(Integer max);
}
