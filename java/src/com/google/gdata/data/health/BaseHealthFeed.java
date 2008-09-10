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


package com.google.gdata.data.health;

import com.google.gdata.data.BaseFeed;

/**
 * Describes a health feed.
 *
 * @param <F> concrete feed type
 * @param <E> concrete entry type
 * 
 */
public abstract class BaseHealthFeed<F extends BaseHealthFeed,
    E extends BaseHealthEntry> extends BaseFeed<F, E> {

  /**
   * Default mutable constructor.
   *
   * @param entryClass entry class
   */
  public BaseHealthFeed(Class<E> entryClass) {
    super(entryClass);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param entryClass entry class
   * @param sourceFeed source feed
   */
  public BaseHealthFeed(Class<E> entryClass, BaseFeed<?, ?> sourceFeed) {
    super(entryClass, sourceFeed);
  }

  @Override
  public String toString() {
    return "{BaseHealthFeed " + super.toString() + "}";
  }

}
