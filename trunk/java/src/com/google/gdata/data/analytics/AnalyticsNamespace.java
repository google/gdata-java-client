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


package com.google.gdata.data.analytics;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Analytics Data Export API.
 *
 * 
 */
public class AnalyticsNamespace {

  private AnalyticsNamespace() {}

  /** Google Analytics (DXP) namespace */
  public static final String DXP = "http://schemas.google.com/analytics/2009";

  /** Google Analytics (DXP) namespace prefix */
  public static final String DXP_PREFIX = DXP + "#";

  /** Google Analytics (DXP) namespace alias */
  public static final String DXP_ALIAS = "dxp";

  /** XML writer namespace for Google Analytics (DXP) */
  public static final XmlNamespace DXP_NS = new XmlNamespace(DXP_ALIAS, DXP);

  /** Analytics-specific data (GA) namespace */
  public static final String GA = "http://schemas.google.com/ga/2009";

  /** Analytics-specific data (GA) namespace prefix */
  public static final String GA_PREFIX = GA + "#";

  /** Analytics-specific data (GA) namespace alias */
  public static final String GA_ALIAS = "ga";

  /** XML writer namespace for Analytics-specific data (GA) */
  public static final XmlNamespace GA_NS = new XmlNamespace(GA_ALIAS, GA);

  /** Gwo (GWO) namespace */
  public static final String GWO =
      "http://schemas.google.com/analytics/websiteoptimizer/2009";

  /** Gwo (GWO) namespace prefix */
  public static final String GWO_PREFIX = GWO + "#";

  /** Gwo (GWO) namespace alias */
  public static final String GWO_ALIAS = "gwo";

  /** XML writer namespace for Gwo (GWO) */
  public static final XmlNamespace GWO_NS = new XmlNamespace(GWO_ALIAS, GWO);

}

