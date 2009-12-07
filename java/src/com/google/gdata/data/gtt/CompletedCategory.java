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

/**
 * Describes a completed category.
 *
 * 
 */
public class CompletedCategory {

  /** Human-readable label. */
  public static final class Label {

    /** Completed label. */
    public static final String COMPLETED = "completed";

  }

  /** Scheme (domain). */
  public static final class Scheme {

    /** Translation state of the document. */
    public static final String TRANSLATIONSTATE = GttNamespace.GTT_PREFIX +
        "translationState";

  }

  /** Term. */
  public static final class Term {

    /** Completed term. */
    public static final String COMPLETED = GttNamespace.GTT_PREFIX +
        "completed";

  }

  /** Private constructor to ensure class is not instantiated. */
  private CompletedCategory() {}

}

