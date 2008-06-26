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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * The feed for getting a list of worksheets within a sheet.
 *
 * 
 */
@Kind.Term(WorksheetEntry.KIND)
public class WorksheetFeed
    extends BaseFeed<WorksheetFeed, WorksheetEntry> {

  /** Constructs an empty feed. */
  public WorksheetFeed() {
    super(WorksheetEntry.class);
    getCategories().add(WorksheetEntry.CATEGORY);
  }

  /** Constructs a new worksheet feed from an existing feed instance. */
  public WorksheetFeed(BaseFeed sourceFeed) {
    super(WorksheetEntry.class, sourceFeed);
    getCategories().add(WorksheetEntry.CATEGORY);
  }

  /** Declares feed-level extensions into the extension profile. */
  public void declareExtensions(ExtensionProfile extProfile) {
    // No feed-level extensions.
    super.declareExtensions(extProfile);
  }
}
