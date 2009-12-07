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


package com.google.gdata.data.finance;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Google Finance Portfolio Data API.
 *
 * 
 */
public class FinanceNamespace {

  private FinanceNamespace() {}

  /** Google Finance (GF) namespace */
  public static final String GF = "http://schemas.google.com/finance/2007";

  /** Google Finance (GF) namespace prefix */
  public static final String GF_PREFIX = GF + "#";

  /** Google Finance (GF) namespace alias */
  public static final String GF_ALIAS = "gf";

  /** XML writer namespace for Google Finance (GF) */
  public static final XmlNamespace GF_NS = new XmlNamespace(GF_ALIAS, GF);

}

