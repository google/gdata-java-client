/* Copyright (c) 2007 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.media.mediarss.MediaContent;

/**
 * Media entry representing a media attachment of a Google Base item. It is used
 * in conjuction with the {@link GoogleBaseMediaFeed} for managing the media
 * attachments (e.g. images) of an item.
 * <p>
 * An object of this type will include the following information:
 * <ul>
 *   <li> {@code atom:title} contains the caption of the media attachment.
 *   <li> {@code atom:content} points to the cached version of the attachment
 *   <li> {@code media:content} points to the safe cached version of the
 *   attachment and will include several links to generated, cached thumbnails
 *   <li> {@code atom:link} with the 'via' relation points to the URI from
 *   where the attachment was crawled by the cache system. If the attachment was
 *   directly uploaded by a user, this atom link will not be included in the
 *   entry.
 * </ul>
 */
public class GoogleBaseMediaEntry extends MediaEntry<GoogleBaseMediaEntry> {
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(GoogleBaseMediaEntry.class, MediaContent.class);    
  }
}
