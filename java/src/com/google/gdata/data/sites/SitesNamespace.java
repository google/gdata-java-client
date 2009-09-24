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

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Sites Data API.
 *
 * 
 */
public class SitesNamespace {

  private SitesNamespace() {}

  /** Google Sites (SITES) namespace */
  public static final String SITES = "http://schemas.google.com/sites/2008";

  /** Google Sites (SITES) namespace prefix */
  public static final String SITES_PREFIX = SITES + "#";

  /** Google Sites (SITES) namespace alias */
  public static final String SITES_ALIAS = "sites";

  /** XML writer namespace for Google Sites (SITES) */
  public static final XmlNamespace SITES_NS = new XmlNamespace(SITES_ALIAS,
      SITES);

}

