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

import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.photos.CommentData;
import com.google.gdata.data.photos.GphotoAlbumId;
import com.google.gdata.data.photos.GphotoPhotoId;

/**
 * Implementation class for album data objects.  This class takes an
 * {@link ExtensionPoint} and uses it to provide all of the methods that
 * {@link CommentData} specifies.  These methods are handled by using
 * extension classes to retrieve or set extensions of the appropriate type.
 *
 * 
 */
public class CommentDataImpl extends GphotoDataImpl implements CommentData {

  /**
   * Construct a new implementation of CommentGphotoData with the given
   * extension point as the backing storage of data.
   */
  public CommentDataImpl(ExtensionPoint extensionPoint) {
    super(extensionPoint);
  }

  /*
   * Declare the extensions that comment objects use.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    declare(extProfile, GphotoAlbumId.getDefaultDescription(false, false));
    declare(extProfile, GphotoPhotoId.getDefaultDescription(false, false));
  }

  /**
   * @return the albumId of the album this comment is on.
   */
  public String getAlbumId() {
    return getSimpleValue(GphotoAlbumId.class);
  }

  /**
   * Set the albumId for the album this comment is on.
   *
   * @param albumId the albumId of the comment.
   */
  public void setAlbumId(Long albumId) {
    if (albumId != null) {
      setExtension(new GphotoAlbumId(albumId.toString()));
    } else {
      removeExtension(GphotoAlbumId.class);
    }
  }

  /**
   * Set the albumId for the album this comment is on.
   *
   * @param albumId the albumId of the comment.
   */
  public void setAlbumId(String albumId) {
    if (albumId != null) {
      setExtension(new GphotoAlbumId(albumId));
    } else {
      removeExtension(GphotoAlbumId.class);
    }
  }

  /**
   * @return the photoId of the photo this comment is on.
   */
  public String getPhotoId() {
    return getSimpleValue(GphotoPhotoId.class);
  }

  /**
   * Set the photoId for the photo this comment is on.
   *
   * @param photoId the photoId of the comment.
   */
  public void setPhotoId(Long photoId) {
    if (photoId != null) {
      setExtension(new GphotoPhotoId(photoId.toString()));
    } else {
      removeExtension(GphotoPhotoId.class);
    }
  }

  /**
   * Set the photoId for the photo this comment is on.
   *
   * @param photoId the photoId of the comment.
   */
  public void setPhotoId(String photoId) {
    if (photoId != null) {
      setExtension(new GphotoPhotoId(photoId));
    } else {
      removeExtension(GphotoPhotoId.class);
    }
  }
}
