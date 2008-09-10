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


package com.google.gdata.data.calendar;

import com.google.gdata.data.acl.AclRole;

/**
 * A holder for calendar-specific ACL roles (their semantics are described
 * in {@link AccessLevelProperty}).
 * 
 * 
 */
public final class CalendarAclRole {

  /** Google Calendar "ROOT" ACL role */ 
  public static final AclRole ROOT = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.ROOT.getValue());
  
  /** Google Calendar "OWNER" ACL role */
  public static final AclRole OWNER = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.OWNER.getValue());
  
  /** Google Calendar "EDITOR" ACL role */
  public static final AclRole EDITOR = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.EDITOR.getValue());
  
  /** Google Calendar "OVERRIDE" ACL role */
  public static final AclRole OVERRIDE = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.OVERRIDE.getValue());

  /** Google Calendar "RESPOND" ACL role */
  public static final AclRole RESPOND = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.RESPOND.getValue());

  /** Google Calendar "FREEBUSY" ACL role */
  public static final AclRole FREEBUSY = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.FREEBUSY.getValue());
  
  /** Google Calendar "READ" ACL role */
  public static final AclRole READ = new AclRole(
      Namespaces.gCalPrefix + AccessLevelProperty.READ.getValue());

  /** Generic "NONE" ACL role (support demanded by the ACL feeds design doc) */
  public static final AclRole NONE = AclRole.NONE;
  
  private CalendarAclRole() {}

}
