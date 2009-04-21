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

package com.google.gdata.data;

import com.google.gdata.data.media.IMediaContent;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ParseException;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * The MediaContent class extends {@link OutOfLineContent} to add
 * {@link MediaSource} handling for the content.  This
 * class is used in contexts where a client or provider wants to provide
 * access to the actual media data via a MediaSource (instead of an href).
 *
 * 
 */
public class MediaContent extends OutOfLineContent implements IMediaContent {

  /**
   * MediaSource associated with the external content.
   */
  protected MediaSource mediaSource;

  /** Returns the media source associated with the content or {@code null}. */
  public MediaSource getMediaSource() { return mediaSource; }

  /**
   * Sets the media source associated with the content (may be {@code null}
   * if no supplied content.
   */
  public void setMediaSource(MediaSource v) { mediaSource = v; }

  public static ChildHandlerInfo getChildHandler(ExtensionProfile extProfile,
      Attributes attrs) throws ParseException, IOException {

    String src = attrs.getValue("", "src");
    if (src != null) {

      // Media content
      ChildHandlerInfo childHandlerInfo = new ChildHandlerInfo();

      MediaContent mc = new MediaContent();
      childHandlerInfo.handler = mc.new AtomHandler();
      childHandlerInfo.content = mc;
      return childHandlerInfo;

    } else {

      // Delegate all non-media content handling
      return Content.getChildHandler(extProfile, attrs);
    }
  }
}
