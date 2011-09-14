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


package com.google.gdata.data.contacts;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Kind;

/**
 * Describes a contact feed.
 *
 * 
 */
@Kind.Term(ContactEntry.KIND)
public class ContactFeed extends BaseFeed<ContactFeed, ContactEntry> {

  /**
   * Default mutable constructor.
   */
  public ContactFeed() {
    super(ContactEntry.class);
    getCategories().add(ContactEntry.CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public ContactFeed(BaseFeed<?, ?> sourceFeed) {
    super(ContactEntry.class, sourceFeed);
  }

  @Override
  public String toString() {
    return "{ContactFeed " + super.toString() + "}";
  }

}

