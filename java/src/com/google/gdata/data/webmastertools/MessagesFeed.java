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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Category;

/**
 * The MessagesFeed class extends the BaseFeed class to define
 * a feed of messages. This are the messages from the Webmaster Tools
 * Notification Console that are directed to the webmaster.
 *
 * 
 */
public class MessagesFeed extends BaseFeed<MessagesFeed, MessageEntry> {

  /**
   * Creates a {@code MessagesFeed} feed that contains {@code MessageEntry}
   * entries.
   */
  public MessagesFeed() {
    super(MessageEntry.class);
    this.getCategories().add(CATEGORY);
  }

  /**
   * Kind category used to label feed.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_MESSAGES_FEED);

  /**
   * Declares extensions. The only extension that we add to the feed is
   * the Webmaster Tools namespace. See {@link MessageEntry} for the other
   * custom extensions at the entry level.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declareAdditionalNamespace(Namespaces.WT_NAMESPACE);
  }
}
