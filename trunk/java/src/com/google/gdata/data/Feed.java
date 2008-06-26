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



/**
 * The Feed class customizes the BaseFeed class to represent the most
 * generic possible feed type.  One usage for this class is to enable the
 * parsing of feeds where the extension model is unknown at the start of
 * the parsing process.  Using in combination with {@link ExtensionProfile}
 * auto-extension, the feed can be parsed generically, and then the
 * {@link #getAdaptedFeed()} can be used to retrieve a more-specfic feed
 * type based upon the {@link Category} kind elements founds within the
 * parsed feed content.
 *
 * @see ExtensionProfile#setAutoExtending(boolean)
 *
 * 
 */
public class Feed extends BaseFeed<Feed, Entry> {

  /**
   * Constructs a new Feed instance that is parameterized to contain
   * Entry instances.
   */
  public Feed() { super(Entry.class); }


  @Override
  public void declareExtensions(ExtensionProfile extProfile) {

    // Declare arbitrary XML support for the feed instances, so any
    // extensions not explicitly declared in the profile will be captured.
    extProfile.declareArbitraryXmlExtension(BaseFeed.class);

    super.declareExtensions(extProfile);
  }
}
