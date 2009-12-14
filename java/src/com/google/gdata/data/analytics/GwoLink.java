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


package com.google.gdata.data.analytics;

/**
 * Extends the base Link class with GWO extensions.
 *
 * 
 */
public class GwoLink {

  /** Link relation type. */
  public static final class Rel {

    /** Experiment configuration URL. */
    public static final String CONFIGURATION_URL = AnalyticsNamespace.GWO_PREFIX
        + "configurationUrl";

    /** Experiment goal URL. */
    public static final String GOAL_URL = AnalyticsNamespace.GWO_PREFIX +
        "goalUrl";

    /** A/b page variation URL. */
    public static final String PAGE_VARIATION_URL =
        AnalyticsNamespace.GWO_PREFIX + "abPageVariationUrl";

    /** Experiment combination preview URL. */
    public static final String PREVIEW_URL = AnalyticsNamespace.GWO_PREFIX +
        "previewUrl";

    /** Experiment report URL. */
    public static final String REPORT_URL = AnalyticsNamespace.GWO_PREFIX +
        "reportUrl";

    /** Experiment test URL. */
    public static final String TEST_URL = AnalyticsNamespace.GWO_PREFIX +
        "testUrl";

  }

  /** Private constructor to ensure class is not instantiated. */
  private GwoLink() {}

}

