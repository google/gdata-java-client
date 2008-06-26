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
   * The username of the user.  The username is the user's current persona,
   * and is not a valid email address but is instead a unique name of the user
   * on the server.
   * 
   * @return the username of the user this data represents.
   */
  public String getUsername();

  /**
   * Set the username for the user this data represents.  This is used by the
   * server and setting it on the client will have no effect.
   *
   * @param username the username of the user.
   */
  public void setUsername(String username);

  /**
   * The nickname of the user.  This is the display name of the user, defined
   * by editing the user information through the UI.
   * 
   * @return the nickname of the user this data represents.
   */
  public String getNickname();

  /**
   * Set the nickname of the user this data represents.  Used on the server to
   * set the field of the entry or feed, this will have no effect on the
   * client.
   *
   * @param nickname the nickname of the user.
   */
  public void setNickname(String nickname);

  /**
   * A url to the user's portrait. 
   * 
   * @return the thumbnail for the user this data represents.
   */
  public String getThumbnail();

  /**
   * Set the thumbnail for the user portrait.  This is used on the server and
   * does not yet have the effect of changing the portrait when set on the
   * client.
   *
   * @param thumbnail the url to the user portrait image.
   */
  public void setThumbnail(String thumbnail);

  /**
   * The quota in bytes that the user has used.  This is calculated on the
   * server.
   * 
   * @return the quota used for this user in bytes.
   */
  public Long getQuotaUsed();

  /**
   * Set the used quota for the user.  Has no effect on the client.
   *
   * @param quota the quota usage in bytes for this user.
   */
  public void setQuotaUsed(Long quota);

  /**
   * The quota limit of the user.  Set on the server, only modifiable by
   * purchasing more storage through the UI.
   * 
   * @return the quota limit for the user in bytes.
   */
  public Long getQuotaLimit();

  /**
   * Set the quota limit for the user this data represents.  Used by the server
   * to set the field in the response, has no effect on the client.
   *
   * @param quota the quota in bytes for the user.
   */
  public void setQuotaLimit(Long quota);

  /**
   * The number of photos a user can have in a single album.  Calculated on
   * the server.
   * 
   * @return the maximum number of photos the user can have in an album.
   */
  public Integer getMaxPhotos();

  /**
   * Set the maximum number of photos the user is allowed.  Used by the server
   * to set the field, has no effect on the client.
   *
   * @param max the number of photos the user is allowed.
   */
  public void setMaxPhotos(Integer max);
}
