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

/**
 * Entry for comment kinds, contains comment metadata.
 *
 * 
 */
@Kind.Term(CommentEntry.KIND)
public class CommentEntry extends GphotoEntry<CommentEntry> implements AtomData,
    CommentData {

  /**
   * Comment kind term value.
   */
  public static final String KIND = Namespaces.PHOTOS_PREFIX + "comment";

  /**
   * Comment kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public CommentEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public CommentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CommentEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CommentEntry.class, GphotoAlbumId.class);
    extProfile.declare(CommentEntry.class,
        CommentAuthor.getDefaultDescription(false, true));
    new CommentAuthor().declareExtensions(extProfile);
    extProfile.declare(CommentEntry.class, GphotoPhotoId.class);
  }

  /**
   * Returns the album ID of the album this comment is on.
   *
   * @return album ID of the album this comment is on
   */
  public GphotoAlbumId getAlbumIdExt() {
    return getExtension(GphotoAlbumId.class);
  }

  /**
   * Sets the album ID of the album this comment is on.
   *
   * @param albumIdExt album ID of the album this comment is on or
   *     <code>null</code> to reset
   */
  public void setAlbumIdExt(GphotoAlbumId albumIdExt) {
    if (albumIdExt == null) {
      removeExtension(GphotoAlbumId.class);
    } else {
      setExtension(albumIdExt);
    }
  }

  /**
   * Returns whether it has the album ID of the album this comment is on.
   *
   * @return whether it has the album ID of the album this comment is on
   */
  public boolean hasAlbumIdExt() {
    return hasExtension(GphotoAlbumId.class);
  }

  /**
   * Returns the photo ID of the album this comment is on.
   *
   * @return photo ID of the album this comment is on
   */
  public GphotoPhotoId getPhotoIdExt() {
    return getExtension(GphotoPhotoId.class);
  }

  /**
   * Sets the photo ID of the album this comment is on.
   *
   * @param photoIdExt photo ID of the album this comment is on or
   *     <code>null</code> to reset
   */
  public void setPhotoIdExt(GphotoPhotoId photoIdExt) {
    if (photoIdExt == null) {
      removeExtension(GphotoPhotoId.class);
    } else {
      setExtension(photoIdExt);
    }
  }

  /**
   * Returns whether it has the photo ID of the album this comment is on.
   *
   * @return whether it has the photo ID of the album this comment is on
   */
  public boolean hasPhotoIdExt() {
    return hasExtension(GphotoPhotoId.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CommentEntry " + super.toString() + "}";
  }


  public String getAlbumId() {
    GphotoAlbumId ext = getAlbumIdExt();
    return ext == null ? null : ext.getValue();
  }

  public String getPhotoId() {
    GphotoPhotoId ext = getPhotoIdExt();
    return ext == null ? null : ext.getValue();
  }

  public void setAlbumId(Long albumId) {
    GphotoAlbumId ext = null;
    if (albumId != null) {
      ext = GphotoAlbumId.from(albumId);
    }
    setAlbumIdExt(ext);
  }

  public void setAlbumId(String albumId) {
    GphotoAlbumId ext = null;
    if (albumId != null) {
      ext = new GphotoAlbumId(albumId);
    }
    setAlbumIdExt(ext);
  }

  public void setPhotoId(Long photoId) {
    GphotoPhotoId ext = null;
    if (photoId != null) {
      ext = GphotoPhotoId.from(photoId);
    }
    setPhotoIdExt(ext);
  }

  public void setPhotoId(String photoId) {
    GphotoPhotoId ext = null;
    if (photoId != null) {
      ext = new GphotoPhotoId(photoId);
    }
    setPhotoIdExt(ext);
  }
}
