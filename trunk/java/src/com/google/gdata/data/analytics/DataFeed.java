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

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Feed element in data feed.
 *
 * 
 */
public class DataFeed extends BaseFeed<DataFeed, DataEntry> {

  /**
   * Default mutable constructor.
   */
  public DataFeed() {
    super(DataEntry.class);
    setKind("analytics#data");
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public DataFeed(BaseFeed<?, ?> sourceFeed) {
    super(DataEntry.class, sourceFeed);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(DataFeed.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(DataFeed.class, Aggregates.class);
    new Aggregates().declareExtensions(extProfile);
    extProfile.declare(DataFeed.class, ContainsSampledData.class);
    extProfile.declare(DataFeed.class, DataSource.getDefaultDescription(false,
        true));
    new DataSource().declareExtensions(extProfile);
    extProfile.declare(DataFeed.class, EndDate.getDefaultDescription(true,
        false));
    extProfile.declare(DataFeed.class, Segment.getDefaultDescription(false,
        true));
    new Segment().declareExtensions(extProfile);
    extProfile.declare(DataFeed.class, StartDate.getDefaultDescription(true,
        false));
  }

  /**
   * Returns the aggregates.
   *
   * @return aggregates
   */
  public Aggregates getAggregates() {
    return getExtension(Aggregates.class);
  }

  /**
   * Sets the aggregates.
   *
   * @param aggregates aggregates or <code>null</code> to reset
   */
  public void setAggregates(Aggregates aggregates) {
    if (aggregates == null) {
      removeExtension(Aggregates.class);
    } else {
      setExtension(aggregates);
    }
  }

  /**
   * Returns whether it has the aggregates.
   *
   * @return whether it has the aggregates
   */
  public boolean hasAggregates() {
    return hasExtension(Aggregates.class);
  }

  /**
   * Returns the flag indicating whether response contains sampled data.
   *
   * @return flag indicating whether response contains sampled data
   */
  public ContainsSampledData getContainsSampledData() {
    return getExtension(ContainsSampledData.class);
  }

  /**
   * Sets the flag indicating whether response contains sampled data.
   *
   * @param containsSampledData flag indicating whether response contains
   *     sampled data or <code>null</code> to reset
   */
  public void setContainsSampledData(ContainsSampledData containsSampledData) {
    if (containsSampledData == null) {
      removeExtension(ContainsSampledData.class);
    } else {
      setExtension(containsSampledData);
    }
  }

  /**
   * Returns whether it has the flag indicating whether response contains
   * sampled data.
   *
   * @return whether it has the flag indicating whether response contains
   *     sampled data
   */
  public boolean hasContainsSampledData() {
    return hasExtension(ContainsSampledData.class);
  }

  /**
   * Returns the data sources.
   *
   * @return data sources
   */
  public List<DataSource> getDataSources() {
    return getRepeatingExtension(DataSource.class);
  }

  /**
   * Adds a new data source.
   *
   * @param dataSource data source
   */
  public void addDataSource(DataSource dataSource) {
    getDataSources().add(dataSource);
  }

  /**
   * Returns whether it has the data sources.
   *
   * @return whether it has the data sources
   */
  public boolean hasDataSources() {
    return hasRepeatingExtension(DataSource.class);
  }

  /**
   * Returns the end date.
   *
   * @return end date
   */
  public EndDate getEndDate() {
    return getExtension(EndDate.class);
  }

  /**
   * Sets the end date.
   *
   * @param endDate end date or <code>null</code> to reset
   */
  public void setEndDate(EndDate endDate) {
    if (endDate == null) {
      removeExtension(EndDate.class);
    } else {
      setExtension(endDate);
    }
  }

  /**
   * Returns whether it has the end date.
   *
   * @return whether it has the end date
   */
  public boolean hasEndDate() {
    return hasExtension(EndDate.class);
  }

  /**
   * Returns the segments.
   *
   * @return segments
   */
  public List<Segment> getSegments() {
    return getRepeatingExtension(Segment.class);
  }

  /**
   * Adds a new segment.
   *
   * @param segment segment
   */
  public void addSegment(Segment segment) {
    getSegments().add(segment);
  }

  /**
   * Returns whether it has the segments.
   *
   * @return whether it has the segments
   */
  public boolean hasSegments() {
    return hasRepeatingExtension(Segment.class);
  }

  /**
   * Returns the start date.
   *
   * @return start date
   */
  public StartDate getStartDate() {
    return getExtension(StartDate.class);
  }

  /**
   * Sets the start date.
   *
   * @param startDate start date or <code>null</code> to reset
   */
  public void setStartDate(StartDate startDate) {
    if (startDate == null) {
      removeExtension(StartDate.class);
    } else {
      setExtension(startDate);
    }
  }

  /**
   * Returns whether it has the start date.
   *
   * @return whether it has the start date
   */
  public boolean hasStartDate() {
    return hasExtension(StartDate.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{DataFeed " + super.toString() + "}";
  }

}

