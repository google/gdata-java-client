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


package com.google.gdata.data.youtube;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Kind;

/**
 * A feed that contains a list of {@link ComplaintEntry}.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_COMPLAINT)
public class ComplaintFeed
    extends BaseFeed<ComplaintFeed, ComplaintEntry> {

  public ComplaintFeed() {
    super(ComplaintEntry.class);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_COMPLAINT);
  }

  public ComplaintFeed(BaseFeed base) {
    super(ComplaintEntry.class, base);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_COMPLAINT);
  }
}
