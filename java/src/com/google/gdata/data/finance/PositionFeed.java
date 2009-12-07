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


package com.google.gdata.data.finance;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Kind;

/**
 * Describes a Finance position feed.
 *
 * 
 */
@Kind.Term(PositionEntry.KIND)
public class PositionFeed extends BaseFeed<PositionFeed, PositionEntry> {

  /**
   * Default mutable constructor.
   */
  public PositionFeed() {
    super(PositionEntry.class);
    getCategories().add(PositionEntry.CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public PositionFeed(BaseFeed<?, ?> sourceFeed) {
    super(PositionEntry.class, sourceFeed);
  }

  @Override
  public String toString() {
    return "{PositionFeed " + super.toString() + "}";
  }

}

