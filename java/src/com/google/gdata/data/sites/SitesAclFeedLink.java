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


package com.google.gdata.data.sites;

import com.google.gdata.data.acl.AclNamespace;

/**
 * Describes a sites ACL feed link.
 *
 * 
 */
public class SitesAclFeedLink {

  /** Feed relation type. */
  public static final class Rel {

    /** Acccess control list feed link. */
    public static final String ACCESS_CONTROL_LIST = AclNamespace.gAclPrefix +
        "accessControlList";

  }

  /** Private constructor to ensure class is not instantiated. */
  private SitesAclFeedLink() {}

}

