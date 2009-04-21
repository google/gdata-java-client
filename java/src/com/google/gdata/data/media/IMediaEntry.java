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


package com.google.gdata.data.media;

import com.google.gdata.data.IEntry;

/**
 * The IMediaEntry interface defines a common base interface for GData media
 * entries that is shared between the old and new data models.
 */
public interface IMediaEntry extends IEntry {
  
  /**
   * Returns the {@link MediaSource} that contains the media data for the entry.
   */
  public MediaSource getMediaSource();
  
  /**
   * Sets the {@link MediaSource} that contains the media data for the entry.
   */
  public void setMediaSource(MediaSource source);
}
