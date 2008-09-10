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
import com.google.gdata.data.photos.impl.CommentDataImpl;

/**
 * Entry for data of the comment kind.  This is used to create comment entries
 * in the Picasaweb GData api.
 *
 * 
 */
@Kind.Term(CommentData.COMMENT_KIND)
public class CommentEntry extends GphotoEntry<CommentEntry>
    implements CommentData, AtomData {

  private final CommentData delegate;

  /**
   * Construct a new empty comment entry.
   */
  public CommentEntry() {
    super();
    getCategories().add(CommentData.COMMENT_CATEGORY);
    this.delegate = new CommentDataImpl(this);
  }

  /**
   * Construct a new comment entry doing a shallow copy of the data in the
   * passed in source entry.
   */
  public CommentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(CommentData.COMMENT_CATEGORY);
    this.delegate = new CommentDataImpl(this);
  }

  /*
   * Declare the extensions the comment entry uses.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  // Delegating methods.

  public String getAlbumId() {
    return delegate.getAlbumId();
  }

  public String getPhotoId() {
    return delegate.getPhotoId();
  }

  public void setAlbumId(Long albumId) {
    delegate.setAlbumId(albumId);
  }

  public void setAlbumId(String albumId) {
    delegate.setAlbumId(albumId);
  }

  public void setPhotoId(Long photoId) {
    delegate.setPhotoId(photoId);
  }

  public void setPhotoId(String photoId) {
    delegate.setPhotoId(photoId);
  }
}
