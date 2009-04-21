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
 * GData namespace definitions related to Analytics.
 *
 * 
 */
public class AnalyticsNamespace {

  private AnalyticsNamespace() {}

  /** Dxp (DXP) namespace */
  public static final String DXP = "http://schemas.google.com/analytics/2009";

  /** Dxp (DXP) namespace prefix */
  public static final String DXP_PREFIX = DXP + "#";

  /** Dxp (DXP) namespace alias */
  public static final String DXP_ALIAS = "dxp";

  /** XML writer namespace for Dxp (DXP) */
  public static final XmlNamespace DXP_NS = new XmlNamespace(DXP_ALIAS, DXP);

}
