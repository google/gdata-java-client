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


package com.google.gdata.data.books;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Google Book Search.
 *
 * 
 */
public class BooksNamespace {

  private BooksNamespace() {}

  /** Google Book Search (GBS) namespace */
  public static final String GBS = "http://schemas.google.com/books/2008";

  /** Google Book Search (GBS) namespace prefix */
  public static final String GBS_PREFIX = GBS + "#";

  /** Google Book Search (GBS) namespace alias */
  public static final String GBS_ALIAS = "gbs";

  /** XML writer namespace for Google Book Search (GBS) */
  public static final XmlNamespace GBS_NS = new XmlNamespace(GBS_ALIAS, GBS);

}
