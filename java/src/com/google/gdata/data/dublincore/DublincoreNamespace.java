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


package com.google.gdata.data.dublincore;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Dublin Core Metadata Initiative.
 *
 * 
 */
public class DublincoreNamespace {

  private DublincoreNamespace() {}

  /** Dublin Core Metadata Initiative, http://dublincore.org (DC) namespace */
  public static final String DC = "http://purl.org/dc/terms";

  /** Dublin Core Metadata Initiative, http://dublincore.org (DC) namespace
   * prefix */
  public static final String DC_PREFIX = DC + "#";

  /** Dublin Core Metadata Initiative, http://dublincore.org (DC) namespace
   * alias */
  public static final String DC_ALIAS = "dc";

  /** XML writer namespace for Dublin Core Metadata Initiative,
   * http://dublincore.org (DC) */
  public static final XmlNamespace DC_NS = new XmlNamespace(DC_ALIAS, DC);

}
