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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.PubControl;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaThumbnail;

/**
 * Constants for GoogleBase-specific namespaces.
 */
class GoogleBaseNamespaces {

  /**
   * URI of the gm: namespace.
   */
  public static final String GM_URI = "http://base.google.com/ns-metadata/1.0";

  /**
   * URI of the g: namespace.
   */
  public static final String G_URI = "http://base.google.com/ns/1.0";

  /**
   * Default prefix for the gm: namespace.
   */
  public static final String GM_ALIAS = "gm";

  /**
   * The Google Base gm: namespace used in metadata entries
   */
  public static final XmlWriter.Namespace GM =
      new XmlWriter.Namespace(GM_ALIAS, GM_URI);

  /**
   * Default prefix for the g: namespace.
   */
  public static final String G_ALIAS = "g";

  /**
   * The Google Base g: namespace used for attributes.
   */
  public static final XmlWriter.Namespace G =
      new XmlWriter.Namespace(G_ALIAS, G_URI);

  /**
   * Declares both g: and gm: extensions into the extension profile.
   *
   * @param extProfile extension profile
   */
  public static void declareAllExtensions(ExtensionProfile extProfile) {
    declareGExtensions(extProfile);
    declareGMExtensions(extProfile);
    declareMediaExtensions(extProfile);
  }

  /**
   * Declares the g: extension into the extension profile, both for feeds
   * and for entries.
   *
   * @param extProfile extension profile
   */
  public static void declareGExtensions(ExtensionProfile extProfile) {
    extProfile.declareEntryExtension(GoogleBaseAttributesExtension.DESCRIPTION);
    extProfile.declareFeedExtension(GoogleBaseAttributesExtension.DESCRIPTION);
    
    ExtensionDescription feedLinkExtDesc = 
        ExtensionDescription.getDefaultDescription(FeedLink.class);
    feedLinkExtDesc.setRepeatable(true);
    extProfile.declare(GoogleBaseEntry.class, feedLinkExtDesc);
  }

  /**
   * Declares all gm: extensions into the extension profile.
   *
   * @param extProfile extension profile
   */
  public static void declareGMExtensions(ExtensionProfile extProfile) {
    extProfile.declareEntryExtension(Stats.class);
    extProfile.declareEntryExtension(AttributeHistogram.class);
    extProfile.declareEntryExtension(GmAttributes.class);
    extProfile.declareEntryExtension(GmItemType.class);
    extProfile.declare(PubControl.class, GmDisapproved.getDefaultDescription());
    extProfile.declare(PubControl.class, 
        GmPublishingPriority.getDefaultDescription());
  }
  
  public static void declareMediaExtensions(ExtensionProfile extProfile) {
    extProfile.declare(GoogleBaseMediaEntry.class, MediaContent.class);
    extProfile.declare(MediaContent.class, MediaThumbnail.class);
  }
}
