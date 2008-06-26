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


package com.google.gdata.data.appsforyourdomain.provisioning;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * 
 * 
 */
@Kind.Term(UserEntry.USER_KIND)
public class UserFeed extends BaseFeed<UserFeed, UserEntry> {

  /**
   * Constructs a new {@code UserFeed} instance that is parameterized to
   * contain {@code UserEntry} instances.
   */
  public UserFeed() {
    super(UserEntry.class);
    getCategories().add(UserEntry.USER_CATEGORY);
  }

  /**
   * Constructs a new {@code UserFeed} instance that is initialized using
   * data from another BaseFeed instance.
   */
  public UserFeed(BaseFeed sourceFeed) {
    super(UserEntry.class, sourceFeed);
    getCategories().add(UserEntry.USER_CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extensionProfile) {
    super.declareExtensions(extensionProfile);
  }
}
