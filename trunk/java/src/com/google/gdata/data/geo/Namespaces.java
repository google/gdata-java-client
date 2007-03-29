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


package com.google.gdata.data.geo;

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * Geo namespaces for the various schemas available to geo-information.
 * 
 * 
 */
public class Namespaces {

  /** 
   * Namespace for W3C's proposal for specifying geo-information.
   * Please see the W3C document {@linkplain http://www.w3.org/2003/01/geo 
   * http://www.w3.org/2003/01/geo} for more information.
   */
  public static final String W3C_GEO
      = "http://www.w3.org/2003/01/geo/wgs84_pos#";
  public static final String W3C_GEO_ALIAS = "geo";
  
  public static final XmlWriter.Namespace W3C_GEO_NAMESPACE
      = new XmlWriter.Namespace(W3C_GEO_ALIAS, W3C_GEO);
  
  private Namespaces() {};
}
