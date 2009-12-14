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


package com.google.gdata.data.analytics;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Feed element in account feed.
 *
 * 
 */
public class AccountFeed extends BaseFeed<AccountFeed, AccountEntry> {

  /**
   * Default mutable constructor.
   */
  public AccountFeed() {
    super(AccountEntry.class);
    setKind("analytics#accounts");
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public AccountFeed(BaseFeed<?, ?> sourceFeed) {
    super(AccountEntry.class, sourceFeed);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AccountFeed.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(AccountFeed.class, Segment.getDefaultDescription(false,
        true));
    new Segment().declareExtensions(extProfile);
  }

  /**
   * Returns the segments.
   *
   * @return segments
   */
  public List<Segment> getSegments() {
    return getRepeatingExtension(Segment.class);
  }

  /**
   * Adds a new segment.
   *
   * @param segment segment
   */
  public void addSegment(Segment segment) {
    getSegments().add(segment);
  }

  /**
   * Returns whether it has the segments.
   *
   * @return whether it has the segments
   */
  public boolean hasSegments() {
    return hasRepeatingExtension(Segment.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{AccountFeed " + super.toString() + "}";
  }

}

