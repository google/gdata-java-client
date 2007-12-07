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

import com.google.gdata.data.media.MediaFeed;

/**
 * Media feed used for managing the media attachments for one Google Base Item.
 * Every item accessible via the {@code /items} feed contains a media feed in
 * the form of a {@link com.google.gdata.data.extensions.FeedLink} with relation
 * {@code media}.
 */
public class GoogleBaseMediaFeed extends MediaFeed<GoogleBaseMediaFeed, GoogleBaseMediaEntry> {

  public GoogleBaseMediaFeed() {
    super(GoogleBaseMediaEntry.class);
  }
  
}
