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


package com.google.gdata.data.youtube;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaRssNamespace;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;


/**
 * Adds an attribute in the yt: namespace to {@code media:content}.
 *
 * This is really awkward to do in the gdata framework.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "content",
    isRepeatable = true
)
public class YouTubeMediaContent extends MediaContent {
  /** Opaque YouTube format identifier (optional) */
  private Integer youTubeFormat;

  /**
   * Describes the tag to an {@link com.google.gdata.data.ExtensionProfile}.
   */
  public static ExtensionDescription getDefaultDescription() {
    return ExtensionDescription.getDefaultDescription(
        YouTubeMediaContent.class);
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (youTubeFormat != null) {
      generator.put(YouTubeNamespace.PREFIX + ":format",
          youTubeFormat.toString());
    }
  }


  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    super.consumeAttributes(attrsHelper);
    youTubeFormat = attrsHelper.consumeInteger("format", false, -1);
    if (youTubeFormat == -1) {
      youTubeFormat = null;
    }
  }

  /** Gets the youtube video format. */
  public Integer getYouTubeFormat() {
    return youTubeFormat;
  }

  /** Sets the youtube video format, an opaque format number at this level. */
  public void setYouTubeFormat(Integer youTubeFormat) {
    this.youTubeFormat = youTubeFormat;
  }
}
