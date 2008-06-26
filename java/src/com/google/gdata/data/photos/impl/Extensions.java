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
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.photos.GphotoData;
import com.google.gdata.data.photos.Namespaces;


import java.util.Date;

/**
 * A container for all of the gphoto extensions that are not specific to any
 * particular kind, but usable in multiple places. If any of these become used
 * on every single interface they should be added to the {@link GphotoDataImpl}
 * interface, but as long as they are only used on a subset of kinds
 * they belong here.  As this is an implementation detail these classes should
 * not be used directly by any client code, but instead the appropriate
 * {@link GphotoData} implementation methods should be used.
 *
 * 
 */
public class Extensions {

  /**
   * The gphoto:commentsEnabled element.
   */
  public static class GphotoCommentsEnabled extends GphotoConstruct {
    public GphotoCommentsEnabled() {
      this(null);
    }

    public GphotoCommentsEnabled(Boolean commentsEnabled) {
      super("commentingEnabled", commentsEnabled == null ? null
          : commentsEnabled.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoCommentsEnabled.class,
          Namespaces.PHOTOS_NAMESPACE, "commentingEnabled");
    }
  }

  /**
   * The gphoto:commentCount element.
   */
  public static class GphotoCommentCount extends GphotoConstruct {
    public GphotoCommentCount() {
      this(null);
    }

    public GphotoCommentCount(Integer commentCount) {
      super("commentCount", commentCount == null ? null : commentCount
          .toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoCommentCount.class,
          Namespaces.PHOTOS_NAMESPACE, "commentCount");
    }
  }


  /**
   * The gphoto:albumId field.
   */
  public static class GphotoAlbumId extends GphotoConstruct {
    public GphotoAlbumId() {
      this((String) null);
    }

    public GphotoAlbumId(Long albumId) {
      this(albumId == null ? null : albumId.toString());
    }

    public GphotoAlbumId(String albumId) {
      super("albumid", albumId);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoAlbumId.class,
          Namespaces.PHOTOS_NAMESPACE, "albumid");
    }
  }

  /**
   * The gphoto:photoId field.
   */
  public static class GphotoPhotoId extends GphotoConstruct {
    public GphotoPhotoId() {
      this((String) null);
    }

    public GphotoPhotoId(Long photoId) {
      this(photoId == null ? null : photoId.toString());
    }

    public GphotoPhotoId(String photoId) {
      super("photoid", photoId);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoPhotoId.class,
          Namespaces.PHOTOS_NAMESPACE, "photoid");
    }
  }

  /**
   * The gphoto:thumbnail element.
   */
  public static class GphotoThumbnail extends GphotoConstruct {
    public GphotoThumbnail() {
      this(null);
    }

    public GphotoThumbnail(String thumbUrl) {
      super("thumbnail", thumbUrl);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoThumbnail.class,
          Namespaces.PHOTOS_NAMESPACE, "thumbnail");
    }
  }

  /**
   * The gphoto:version element.
   */
  public static class GphotoVersion extends GphotoConstruct {
    public GphotoVersion() {
      this(null);
    }

    public GphotoVersion(Long version) {
      super("version", version == null ? null : version.toString());
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoVersion.class,
          Namespaces.PHOTOS_NAMESPACE, "version");
    }
  }

  /**
   * The gphoto:user field.
   */
  public static class GphotoUsername extends GphotoConstruct {
    public GphotoUsername() {
      this(null);
    }

    public GphotoUsername(String username) {
      super("user", username);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoUsername.class,
          Namespaces.PHOTOS_NAMESPACE, "user");
    }
  }

  /**
   * The gphoto:nickname field.
   */
  public static class GphotoNickname extends GphotoConstruct {
    public GphotoNickname() {
      this(null);
    }

    public GphotoNickname(String nickname) {
      super("nickname", nickname);
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoNickname.class,
          Namespaces.PHOTOS_NAMESPACE, "nickname");
    }
  }

  /**
   * Simple timestamp element, has the date of the item.
   */
  public static class GphotoTimestamp extends GphotoConstruct {
    public GphotoTimestamp() {
      this(null);
    }

    public GphotoTimestamp(Date date) {
      super("timestamp", date == null ? null : Long.toString(date.getTime()));
    }

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(GphotoTimestamp.class,
          Namespaces.PHOTOS_NAMESPACE, "timestamp");
    }
  }

  /**
   * Parent class for gphoto:foo extensions.
   */
  public static abstract class GphotoConstruct extends ValueConstruct {
    public GphotoConstruct(String name) {
      this(name, null);
    }

    public GphotoConstruct(String name, String value) {
      super(Namespaces.PHOTOS_NAMESPACE, name, null, value);
      setRequired(false);
      
      // We support empty strings on the wire, which the default ValueConstruct
      // doesn't, so we override the default value to be empty.
      if (value == null) {
        setValue("");
      }
    }
  }

  // Private constructor, no instantiation.
  private Extensions() {}
}
