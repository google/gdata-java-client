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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Entry element for data feed.
 *
 * 
 */
public class DataEntry extends BaseEntry<DataEntry> {

  /**
   * Default mutable constructor.
   */
  public DataEntry() {
    super();
    setKind("analytics#datarow");
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public DataEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(DataEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(DataEntry.class, Dimension.getDefaultDescription(false,
        true));
    extProfile.declare(DataEntry.class,
        AnalyticsLink.getDefaultDescription(false, true));
    extProfile.declare(DataEntry.class, Metric.getDefaultDescription(false,
        true));
  }

  /**
   * Returns the dimensions.
   *
   * @return dimensions
   */
  public List<Dimension> getDimensions() {
    return getRepeatingExtension(Dimension.class);
  }

  /**
   * Adds a new dimension.
   *
   * @param dimension dimension
   */
  public void addDimension(Dimension dimension) {
    getDimensions().add(dimension);
  }

  /**
   * Returns whether it has the dimensions.
   *
   * @return whether it has the dimensions
   */
  public boolean hasDimensions() {
    return hasRepeatingExtension(Dimension.class);
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

  @Override
  public String toString() {
    return "{DataEntry " + super.toString() + "}";
  }


  /**
   * Gets the dimension with the given name in this entry.
   *
   * @param name The name of the dimension to retrieve.
   * @return The named dimension, or null if the dimension is not present in
   *     this entry
   */
  public Dimension getDimension(String name) {
    List<Dimension> dimensions = getDimensions();
    for (Dimension dimension : dimensions) {
      if (dimension.getName().equalsIgnoreCase(name)) {
        return dimension;
      }
    }
    return null;
  }

  /**
   * Gets the metric with the given name in this entry.
   *
   * @param name The name of the metric to retrieve.
   * @return The named metric, or null if the metric is not present in this
   *     entry
   */
  public Metric getMetric(String name) {
    List<Metric> metrics = getMetrics();
    for (Metric metric : metrics) {
      if (metric.getName().equalsIgnoreCase(name)) {
        return metric;
      }
    }
    return null;
  }

  /**
   * Gets the long value of the metric with the given name in this entry.
   * @return the long value of the named metric, or null if the named metric
   *     is not present in this entry
   */
  public Long longValueOf(String name) {
    Metric m = getMetric(name);
    if (m == null) {
      return null;
    } else {
      return m.longValue();
    }
  }

  /**
   * Gets the double value of the metric with the given name in this entry.
   * @return the double value of the named metric, or null if the named metric
   *     is not present in this entry
   */
  public Double doubleValueOf(String name) {
    Metric m = getMetric(name);
    if (m == null) {
      return null;
    } else {
      return m.doubleValue();
    }
  }

  /**
   * Retrieves the string value of the named dimension or metric in this entry.
   * @return The value of the named dimension or metric, or null if it is not
   *     present in this entry
   */
  public String stringValueOf(String name) {
    Dimension d = getDimension(name);
    if (d != null) {
      return d.getValue();
    }
    Metric m = getMetric(name);
    if (m != null) {
      return m.getValue();
    }
    return null;
  }

}
