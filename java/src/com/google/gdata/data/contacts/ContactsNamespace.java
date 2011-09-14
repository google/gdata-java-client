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


package com.google.gdata.data.contacts;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Contacts Data API.
 *
 * 
 */
public class ContactsNamespace {

  private ContactsNamespace() {}

  /** Google Contacts (GCONTACT) namespace */
  public static final String GCONTACT =
      "http://schemas.google.com/contact/2008";

  /** Google Contacts (GCONTACT) namespace prefix */
  public static final String GCONTACT_PREFIX = GCONTACT + "#";

  /** Google Contacts (GCONTACT) namespace alias */
  public static final String GCONTACT_ALIAS = "gContact";

  /** XML writer namespace for Google Contacts (GCONTACT) */
  public static final XmlNamespace GCONTACT_NS = new
      XmlNamespace(GCONTACT_ALIAS, GCONTACT);

}

