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


package com.google.gdata.data.health;

import com.google.gdata.util.Namespaces;

/**
 * Describes a health category.
 *
 * 
 */
public class HealthCategory {

  /** Scheme (domain). */
  public static final class Scheme {

    /** Http://schemas google com/health/ccr health category. */
    public static final String CCR_SCHEME =
        "http://schemas.google.com/health/ccr";

    /** Http://schemas google com/health/item health category. */
    public static final String ITEM_SCHEME =
        "http://schemas.google.com/health/item";

    /** Kind health category. */
    public static final String KIND = Namespaces.gPrefix + "kind";

  }

  /** Private constructor to ensure class is not instantiated. */
  private HealthCategory() {}

}
