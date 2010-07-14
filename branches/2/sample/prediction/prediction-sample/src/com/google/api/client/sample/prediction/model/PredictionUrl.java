/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.sample.prediction.model;

import com.google.api.client.googleapis.GoogleUrl;

/**
 * Prediction URL builder.
 *
 * @author Yaniv Inbar
 */
public final class PredictionUrl extends GoogleUrl {

  /** Constructs a new Prediction URL from the given encoded URL. */
  public PredictionUrl(String encodedUrl) {
    super(encodedUrl);
    if (Debug.ENABLED) {
      prettyprint = true;
    }
  }

  /**
   * Constructs a new Prediction URL based on the given object path of the form
   * {@code "mybucket/myobject"}.
   */
  public static PredictionUrl fromBucketAndObjectNames(String objectPath) {
    PredictionUrl result =
        new PredictionUrl("https://www.googleapis.com/prediction/v1/training");
    // this will ensure that objectPath is encoded properly, e.g. "/" -> "%2F"
    result.pathParts.add(objectPath);
    result.pathParts.add("predict");
    return result;
  }
}
