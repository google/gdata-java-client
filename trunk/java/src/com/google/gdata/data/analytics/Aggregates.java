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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Describes an aggregates.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = Aggregates.XML_NAME)
public class Aggregates extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "aggregates";

  /**
   * Default mutable constructor.
   */
  public Aggregates() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Aggregates.class)) {
      return;
    }
    extProfile.declare(Aggregates.class, Metric.getDefaultDescription(false,
        true));
  }

  /**
   * Returns the metrics.
   *
   * @return metrics
   */
  public List<Metric> getMetrics() {
    return getRepeatingExtension(Metric.class);
  }

  /**
   * Adds a new metric.
   *
   * @param metric metric
   */
  public void addMetric(Metric metric) {
    getMetrics().add(metric);
  }

  /**
   * Returns whether it has the metrics.
   *
   * @return whether it has the metrics
   */
  public boolean hasMetrics() {
    return hasRepeatingExtension(Metric.class);
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Aggregates.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Aggregates}";
  }

}

