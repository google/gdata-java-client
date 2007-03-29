/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

/**
 * An ATOM feed containing entries of type {@link GoogleBaseEntry}.
 *
 * Feeds are usually created by {@link GoogleBaseService#getFeed(java.net.URL)} 
 * or {@link GoogleBaseService#query(com.google.gdata.client.Query)}, but they
 * can be created and initialized independently as well. 
 * 
 */
public class GoogleBaseFeed extends BaseFeed<GoogleBaseFeed, GoogleBaseEntry> {

  private final GoogleBaseAttributesExtension googleBaseAttributesExtension;

  /**
   * Creates a new, empty feed.
   */
  public GoogleBaseFeed()
  {
    super(GoogleBaseEntry.class);
    googleBaseAttributesExtension = new GoogleBaseAttributesExtension();
    addExtension(googleBaseAttributesExtension);
  }

  /**
   * Accesses tags in the g: namespace.
   *
   * @return extension corresponding to tags in the g: namespace, never
   *   null but might be empty.
   */
  public GoogleBaseAttributesExtension getGoogleBaseAttributes() {
    return googleBaseAttributesExtension;
  }

  /**
   * Adds an entry. This is a convenience method equivalent to:
   * <code>getEntries().add()</code>.
   * 
   * @param entry
   */
  public void addEntry(GoogleBaseEntry entry) {
    getEntries().add(entry);
  }

  /** 
   * Declares the g: extension. 
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Declare arbitrary XML support for the feed instances, so any
    // extensions not explicitly declared in the profile will be captured.
    extProfile.declareArbitraryXmlExtension(GoogleBaseFeed.class);
    extProfile.declareFeedExtension(GoogleBaseAttributesExtension.DESCRIPTION);
    super.declareExtensions(extProfile);
  }
}
