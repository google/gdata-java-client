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
import com.google.gdata.data.Kind;

/**
 * The EventFeed class customizes the generic event feed class to define
 * a feed of events.
 */
@Kind.Term(EventEntry.EVENT_KIND)
public class EventFeed extends BaseEventFeed<EventFeed, EventEntry> {

  /**
   * Constructs a new {@code EventFeed} instance that is parameterized to
   * contain {@code EventEntry} instances.
   */
  public EventFeed() {
    super(EventEntry.class);
  }

  /**
   * Constructs a new {@code EventFeed} instance that is initialized using
   * data from another BaseFeed instance.
   */
  public EventFeed(BaseFeed<?, ?> sourceFeed) {
    super(EventEntry.class, sourceFeed);
  }
}
