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


package com.google.gdata.data.threading;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Threading.
 *
 * 
 */
public class ThreadingNamespace {

  private ThreadingNamespace() {}

  /** Atom threading extensions (THR) namespace */
  public static final String THR = "http://purl.org/syndication/thread/1.0";

  /** Atom threading extensions (THR) namespace prefix */
  public static final String THR_PREFIX = THR + "#";

  /** Atom threading extensions (THR) namespace alias */
  public static final String THR_ALIAS = "thr";

  /** XML writer namespace for Atom threading extensions (THR) */
  public static final XmlNamespace THR_NS = new XmlNamespace(THR_ALIAS, THR);

}
