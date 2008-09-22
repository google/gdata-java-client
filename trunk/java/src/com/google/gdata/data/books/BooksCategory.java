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

import com.google.gdata.util.Namespaces;

/**
 * Describes a books category.
 *
 * 
 */
public class BooksCategory {

  /** Scheme (domain). */
  public static final class Scheme {

    /** Kind books category. */
    public static final String KIND = Namespaces.gPrefix + "kind";

    /** Http://schemas google com/books/2008/labels books category. */
    public static final String LABELS_SCHEME =
        "http://schemas.google.com/books/2008/labels";

  }

  /** Private constructor to ensure class is not instantiated. */
  private BooksCategory() {}

}
