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

package com.google.gdata.data.appsforyourdomain.migration;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * The MailItemFeed class customizes the generic BaseEntry class to define
 * a feed of MailItem entries.
 *
 * 
 */
@Kind.Term(MailItemEntry.MAILITEM_KIND)
public class MailItemFeed extends BaseFeed<MailItemFeed, MailItemEntry> {

  /**
   * Constructs a new {@code MailItemFeed} instance that is parameterized to
   * contain {@code MailItemEntry} instances.
   */
  public MailItemFeed() { 
    super(MailItemEntry.class);
    getCategories().add(MailItemEntry.MAILITEM_CATEGORY);
  }
  
  /**
   * Constructs a new {@code MailItem} instance that is initialized using
   * data from another BaseFeed instance.
   */
  public MailItemFeed(BaseFeed<?, ?> sourceFeed) {
    super(MailItemEntry.class, sourceFeed);
    getCategories().add(MailItemEntry.MAILITEM_CATEGORY);
  }   
  
  /** Declares feed-level extensions into the extension profile. */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // No feed-level extensions.
    super.declareExtensions(extProfile);
  }   
}