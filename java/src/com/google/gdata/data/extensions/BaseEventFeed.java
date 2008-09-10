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


package com.google.gdata.data.extensions;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

/**
 * Customizes the base feed class to define a feed of events.
 *
 * @param <F> event feed class
 * @param <E> event entry class
 */
public abstract class BaseEventFeed
    <F extends BaseEventFeed<F, E>, E extends BaseEventEntry<E>>
    extends BaseFeed<F, E> {

  /**
   * Constructs a new event feed instance that is parameterized to contain event
   * entry instances.
   *
   * @param entryClass event entry class
   */
  public BaseEventFeed(Class<E> entryClass) {
    super(entryClass);
    getCategories().add(EventEntry.EVENT_CATEGORY);
  }

  /**
   * Constructs a new event feed instance that is initialized using data from
   * another base feed instance.
   *
   * @param entryClass event entry class
   * @param sourceFeed source feed
   */
  public BaseEventFeed(Class<E> entryClass, BaseFeed<?, ?> sourceFeed) {
    super(entryClass, sourceFeed);
    getCategories().add(EventEntry.EVENT_CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {

    // Feed level declarations
    extProfile.declare(BaseEventFeed.class, Where.getDefaultDescription());

    // Adds entry level declarations
    super.declareExtensions(extProfile);
  }

  // Any feed-level extension accessor APIs would go here
}
