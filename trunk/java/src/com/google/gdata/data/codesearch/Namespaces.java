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


package com.google.gdata.data.codesearch;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Google Code Search.
 *
 * 
 */
public class Namespaces {

  private Namespaces() {}

  /** Google Code Search (GCS) namespace */
  public static final String gCS = "http://schemas.google.com/codesearch/2006";

  /** Google Code Search (GCS) namespace prefix */
  public static final String gCSNamespacePrefix = gCS + "#";

  /** Google Code Search (GCS) namespace alias */
  public static final String gCSPrefix = "gcs";

  /** XML writer namespace for Google Code Search (GCS) */
  public static final XmlNamespace gCSNs = new XmlNamespace(gCSPrefix, gCS);

}
