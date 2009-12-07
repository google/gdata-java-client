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


package com.google.gdata.data.gtt;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Translator Toolkit API.
 *
 * 
 */
public class GttNamespace {

  private GttNamespace() {}

  /** Google Translator Toolkit (GTT) namespace */
  public static final String GTT = "http://schemas.google.com/gtt/2009/11";

  /** Google Translator Toolkit (GTT) namespace prefix */
  public static final String GTT_PREFIX = GTT + "#";

  /** Google Translator Toolkit (GTT) namespace alias */
  public static final String GTT_ALIAS = "gtt";

  /** XML writer namespace for Google Translator Toolkit (GTT) */
  public static final XmlNamespace GTT_NS = new XmlNamespace(GTT_ALIAS, GTT);

}

