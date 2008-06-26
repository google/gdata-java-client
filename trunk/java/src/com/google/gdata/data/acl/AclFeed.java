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


package com.google.gdata.data.acl;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Kind;

/**
 * Defines a feed of an access control list (ACL).
 *
 * 
 */
@Kind.Term(AclEntry.ACCESS_RULE_KIND)
public class AclFeed extends BaseFeed<AclFeed, AclEntry> {

  /**
   * Default constructor for an ACL feed.
   */
  public AclFeed() {
    super(AclEntry.class);
    getCategories().add(AclEntry.ACCESS_RULE_CATEGORY);
  }

}
