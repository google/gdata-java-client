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

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Access Control Lists (ACL's).
 *
 * 
 */
public class AclNamespace {

  private AclNamespace() {}

  /** Google Access Control List namespace */
  public static final String gAcl = "http://schemas.google.com/acl/2007";

  /** Google Access Control List namespace prefix */
  public static final String gAclPrefix = gAcl + "#";

  /** Default Google Access Control List namespace alias */
  public static final String gAclAlias = "gAcl";

  /** XML writer namespace for Google ACL */
  public static final XmlNamespace gAclNs = new XmlNamespace(gAclAlias, gAcl);

  /**
   * Link provides the URI of the feed for the access control list for the
   * entry.
   */
  public static final String LINK_REL_ACCESS_CONTROL_LIST =
      gAclPrefix + "accessControlList";

  /**
   * Link provides the URI of the entry that is controlled by the access
   * control list feed.
   */
  public static final String LINK_REL_CONTROLLED_OBJECT =
      gAclPrefix + "controlledObject";

}
