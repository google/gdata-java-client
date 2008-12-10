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


package com.google.gdata.data.media.mediarss;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionDescription;

/**
 * GData definitions for the
 * <a href="http://search.yahoo.com/mrss">Yahoo media: namespace</a>.
 *
 * 
 */
public class MediaRssNamespace {
  /** Namespace URI */
  public static final String URI = "http://search.yahoo.com/mrss/";

  /** Standard namespace prefix. */
  public static final String PREFIX = "media";

  /** Namespace object. */
  public static final XmlNamespace NS = new XmlNamespace(PREFIX, URI);

  /**
   * Description of extensions that can be used inside feed,
   * extension and media:group tags.
   */
  private static final ExtensionDescription[] STANDARD_EXTENSIONS = {
      ExtensionDescription.getDefaultDescription(MediaRating.class),
      ExtensionDescription.getDefaultDescription(MediaTitle.class),
      ExtensionDescription.getDefaultDescription(MediaDescription.class),
      MediaKeywords.getDefaultDescription(),
      MediaThumbnail.getDefaultDescription(),
      ExtensionDescription.getDefaultDescription(MediaCategory.class),
      ExtensionDescription.getDefaultDescription(MediaHash.class),
      MediaPlayer.getDefaultDescription(),
      ExtensionDescription.getDefaultDescription(MediaCredit.class),
      ExtensionDescription.getDefaultDescription(MediaCopyright.class),
          ExtensionDescription.getDefaultDescription(MediaText.class),
      ExtensionDescription.getDefaultDescription(MediaRestriction.class),
  };

  /**
   * Extends given profile with Yahoo media RSS extensions.
   *
   * @param profile the profile to be extended
   */
  public static void declareAll(ExtensionProfile profile) {
    profile.declareAdditionalNamespace(NS);

    // Register some extensions that require special treatment
    profile.declare(BaseEntry.class, MediaGroup.getDefaultDescription());
    profile.declare(BaseEntry.class, MediaContent.getDefaultDescription(false));
    profile.declare(MediaGroup.class, MediaContent.getDefaultDescription(true));

    // Register all standard extension everywhere, including in the aggregators
    // declared above (MediaGroup and MediaContent)
    for (ExtensionDescription desc : STANDARD_EXTENSIONS) {
      profile.declare(BaseEntry.class, desc);
      profile.declare(BaseFeed.class, desc);
      profile.declare(MediaGroup.class, desc);
      profile.declare(MediaContent.class, desc);
    }
  }
}
