/* Copyright (c) 2006 Google Inc.
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


package com.google.gdata.data.appsforyourdomain;

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * Google Apps for Your Domain namespace.
 * 
 */
public class Namespaces {

  /** Google Apps for Your Domain namespace */
  public static final String APPS = "http://schemas.google.com/apps/2006";
  public static final String APPS_PREFIX = APPS + "#";

  /** Describes the meaning of FeedLinks in the UserEntry */
  public static final String USER_NICKNAME_REL = APPS_PREFIX + "user.nicknames";
  public static final String USER_EMAILLIST_REL = APPS_PREFIX + "user.emailLists"; 

  /** Describes the meaning of FeedLinks in the EmailListEntry */
  public static final String EMAILLIST_RECIPIENT_REL = APPS_PREFIX + "emailList.recipients";
 
  /** Google data XML writer namespace. */
  public static final XmlWriter.Namespace APPS_NAMESPACE
      = new XmlWriter.Namespace("apps", APPS);

  private Namespaces() {
  }

}
