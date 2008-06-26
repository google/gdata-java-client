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


package com.google.gdata.data.codesearch;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;


/**
 * The CodeSearchFeed class customizes the generic BaseFeed class to define
 * a feed to search for code.
 *
 * 
 */
@Kind.Term(CodeSearchEntry.CODESEARCH_KIND)
public class CodeSearchFeed extends BaseFeed<CodeSearchFeed, CodeSearchEntry> {

  /**
   * Constructs a new {@code CodeSearchFeed} instance that is parameterized to
   * contain {@code CodeSearchEntry} instances.
   */
  public CodeSearchFeed() {
    super(CodeSearchEntry.class);
    getCategories().add(CodeSearchEntry.CODESEARCH_CATEGORY);
  }

  /**
   * Constructs a new {@code CodeSearchFeed} instance that is initialized using
   * data from another BaseFeed instance.
   */
  public CodeSearchFeed(BaseFeed sourceFeed) {
    super(CodeSearchEntry.class, sourceFeed);
    getCategories().add(CodeSearchEntry.CODESEARCH_CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {

    // Add any feed-level extension declarations here.

    super.declareExtensions(extProfile);
  }

  // Any feed-level extension accessor APIs would go here
}
