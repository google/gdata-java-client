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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.photos.MediaData;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of the MediaData interface.  This currently only supports
 * mediarss content inside of a media:group element, it doesn't handle inlined
 * media:content or media:thumbnail elements.  This class uses a passed in
 * {@link ExtensionPoint} to pull extensions from.
 * 
 * 
 */
public class MediaDataImpl implements MediaData {

  private ExtensionPoint extPoint;

  /**
   * Construct a new implementation of MediaData with the given
   * extension point as the backing storage for data.
   */
  public MediaDataImpl(ExtensionPoint extensionPoint) {
    this.extPoint = extensionPoint;
  }

  /*
   * Declares the standard media group setup on this object.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    Class<? extends ExtensionPoint> extClass = extPoint.getClass();
    extProfile.declare(extClass, MediaGroup.getDefaultDescription());
    extProfile.declareArbitraryXmlExtension(MediaGroup.class);
    
    // Declare that BaseEntry gets the media extensions as well.
    if (BaseEntry.class.isAssignableFrom(extClass)) {
      extProfile.declare(BaseEntry.class, MediaGroup.getDefaultDescription());
    }

    extProfile.declare(MediaGroup.class, MediaContent
        .getDefaultDescription(true));
    extProfile.declareArbitraryXmlExtension(MediaContent.class);
    extProfile.declare(MediaGroup.class,
        ExtensionDescription.getDefaultDescription(MediaTitle.class));
    extProfile.declare(MediaGroup.class,
        ExtensionDescription.getDefaultDescription(MediaDescription.class));
    extProfile.declare(MediaGroup.class, MediaKeywords.getDefaultDescription());
    ExtensionDescription thumbDesc = MediaThumbnail.getDefaultDescription();
    thumbDesc.setRepeatable(true);
    extProfile.declare(MediaGroup.class, thumbDesc);
    extProfile.declareArbitraryXmlExtension(MediaThumbnail.class);
    extProfile.declare(MediaGroup.class,
        ExtensionDescription.getDefaultDescription(MediaCredit.class));
  }
  
  /*
   * Returns the media:group element on the extension point.
   */
  public MediaGroup getMediaGroup() {
    return extPoint.getExtension(MediaGroup.class);
  }

  /**
   * @return the list of media:content elements on the extension point.
   */
  public List<MediaContent> getMediaContents() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    
    return group.getContents();
  }

  /*
   * Returns the list of media:category elements on the extension point.
   */
  public List<MediaCategory> getMediaCategories() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    
    return group.getCategories();
  }

  /*
   * Returns the list of media:credit elements on the extension point.
   */
  public List<MediaCredit> getMediaCredits() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    
    return group.getCredits();
  }
  
  /*
   * Returns the list of media:thumbnail elements on the extension point.
   */
  public List<MediaThumbnail> getMediaThumbnails() {
    MediaGroup group = getMediaGroup();
    if (group == null) {
      return Collections.emptyList();
    }
    
    return group.getThumbnails();
  }
  
  /*
   * Returns the media:keywords that are the keywords on the extension point.
   */
  public MediaKeywords getMediaKeywords() {
    MediaGroup group = getMediaGroup();
    if (group != null) {
      return group.getKeywords();
    }
    return null;
  }

  /*
   * Sets the media:keywords to use.  This will create media:group as well if
   * it doesn't yet exist.
   */
  public void setKeywords(MediaKeywords keywords) {
    MediaGroup group = extPoint.getExtension(MediaGroup.class);
    if (group == null) {
      group = new MediaGroup();
      extPoint.addExtension(group);
    }
    group.setKeywords(keywords);
  }
  
  /*
   * Delegate these to our extension point.
   */
  public void addExtension(Extension extension) {
    extPoint.addExtension(extension);
  }
  
  public void setExtension(Extension extension) {
    extPoint.setExtension(extension);
  }
  
  public void addRepeatingExtension(Extension extension) {
    extPoint.addRepeatingExtension(extension);
  }

  public void removeExtension(Class<? extends Extension> extensionClass) {
    extPoint.removeExtension(extensionClass);
  }
  
  public void removeExtension(Extension extension) {
    extPoint.removeExtension(extension);
  }
  
  public void removeRepeatingExtension(Extension extension) {
    extPoint.removeRepeatingExtension(extension);
  }
}
