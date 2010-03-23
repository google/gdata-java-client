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


package com.google.gdata.data.docs;

/**
 * Describes a publish link.
 *
 * 
 */
public class PublishLink {

  /** Link relation type. */
  public static final class Rel {

    /** Link for the publically viewable published document. */
    public static final String PUBLISH =
        "http://schemas.google.com/docs/2007#publish";

  }

  /** Private constructor to ensure class is not instantiated. */
  private PublishLink() {}

}

