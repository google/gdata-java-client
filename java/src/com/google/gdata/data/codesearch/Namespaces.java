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

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * CodeSearch name space element: <gcs:..>. Used for all extensions
 * related to codesearch
 *
 * 
 */

public final class Namespaces {
  /** Google Codesearch (c) namespace */
  static final public String gCS = "http://schemas.google.com/codesearch/2006";
  static final public String gCSPrefix = "gcs";

  /** Google data XML writer namespace. */
  static final public XmlWriter.Namespace gCSNs =
  new XmlWriter.Namespace("gcs", gCS);

}
