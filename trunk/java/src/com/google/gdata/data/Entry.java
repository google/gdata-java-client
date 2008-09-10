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
 * The Entry class customizes the BaseFeed class to represent the most
 * generic possible entry type.  One usage for this class is to enable the
 * parsing of an entry where the extension model is unknown at the start of
 * the parsing process.  Using in combination with {@link ExtensionProfile}
 * auto-extension, the feed can be parsed generically, and then the
 * {@link #getAdaptedEntry()} can be used to retrieve a more-specfic entry
 * type based upon the {@link Category} kind elements founds within the
 * parsed entry content.
 *
 * @see ExtensionProfile#setAutoExtending(boolean)
 * APIs.
 *
 * 
 * 
 */
public class Entry extends BaseEntry<Entry> {

  /**
   * Constructs a new uninitialized Entry instance.
   */
  public Entry() {}

  /**
   * Constructs a new Entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public Entry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The Entry class declares support for processing of arbitrary entry
   * extension data (as XmlBlobs).  Subtypes which want stricter
   * parsing should override this method and not delegate to the base
   * implementation.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {

    // Declare arbitrary XML support for the entry instances, so any
    // extensions not explicitly declared in the profile will be captured.
    extProfile.declareArbitraryXmlExtension(BaseEntry.class);
  }
}
