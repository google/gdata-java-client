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

/**
 * Extends the base Link class with Health extensions.
 *
 * 
 */
public class HealthLink {

  /** Link relation type. */
  public static final class Rel {

    /** Complete url of an entry, indicating the smallest feed containing the
     * entry. */
    public static final String HTTP_SCHEMAS_GOOGLE_COM_HEALTH_DATA_COMPLETE =
        "http://schemas.google.com/health/data#complete";

  }

  /** Private constructor to ensure class is not instantiated. */
  private HealthLink() {}

}
