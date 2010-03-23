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


package com.google.gdata.data.docs;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Documents List Data API.
 *
 * 
 */
public class DocsNamespace {

  private DocsNamespace() {}

  /** Google Documents List (DOCS) namespace */
  public static final String DOCS = "http://schemas.google.com/docs/2007";

  /** Google Documents List (DOCS) namespace prefix */
  public static final String DOCS_PREFIX = DOCS + "#";

  /** Google Documents List (DOCS) namespace alias */
  public static final String DOCS_ALIAS = "docs";

  /** XML writer namespace for Google Documents List (DOCS) */
  public static final XmlNamespace DOCS_NS = new XmlNamespace(DOCS_ALIAS, DOCS);

}

