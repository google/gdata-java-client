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


package com.google.gdata.data.blogger;

/**
 * Extends the base Link class with Blogger extensions.
 *
 * 
 */
public class BloggerLink {

  /** Link relation type. */
  public static final class Rel {

    /** Link that provides the URI of the web content. */
    public static final String REPLIES = "replies";

    /** Link to blog settings feed. */
    public static final String SETTINGS =
        "http://schemas.google.com/blogger/2008#settings";

    /** Link to blog templates feed. */
    public static final String TEMPLATE =
        "http://schemas.google.com/blogger/2008#template";

  }

  /** Private constructor to ensure class is not instantiated. */
  private BloggerLink() {}

}
