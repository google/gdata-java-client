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

/**
 * Entry element for combination feed.
 *
 * 
 */
public class CombinationEntry extends BaseEntry<CombinationEntry> {

  /**
   * Default mutable constructor.
   */
  public CombinationEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public CombinationEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CombinationEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CombinationEntry.class, GwoComboActive.class);
    extProfile.declare(CombinationEntry.class, GwoComboId.class);
    extProfile.declare(CombinationEntry.class, GwoComboString.class);
    extProfile.declare(CombinationEntry.class, GwoExperimentId.class);
  }

  /**
   * Returns the flag indicating if the combination is active.
   *
   * @return flag indicating if the combination is active
   */
  public GwoComboActive getComboActive() {
    return getExtension(GwoComboActive.class);
  }

  /**
   * Sets the flag indicating if the combination is active.
   *
   * @param comboActive flag indicating if the combination is active or
   *     <code>null</code> to reset
   */
  public void setComboActive(GwoComboActive comboActive) {
    if (comboActive == null) {
      removeExtension(GwoComboActive.class);
    } else {
      setExtension(comboActive);
    }
  }

  /**
   * Returns whether it has the flag indicating if the combination is active.
   *
   * @return whether it has the flag indicating if the combination is active
   */
  public boolean hasComboActive() {
    return hasExtension(GwoComboActive.class);
  }

  /**
   * Returns the combination ID.
   *
   * @return combination ID
   */
  public GwoComboId getComboId() {
    return getExtension(GwoComboId.class);
  }

  /**
   * Sets the combination ID.
   *
   * @param comboId combination ID or <code>null</code> to reset
   */
  public void setComboId(GwoComboId comboId) {
    if (comboId == null) {
      removeExtension(GwoComboId.class);
    } else {
      setExtension(comboId);
    }
  }

  /**
   * Returns whether it has the combination ID.
   *
   * @return whether it has the combination ID
   */
  public boolean hasComboId() {
    return hasExtension(GwoComboId.class);
  }

  /**
   * Returns the string that specifies a combination in an experiment.
   *
   * @return string that specifies a combination in an experiment
   */
  public GwoComboString getComboString() {
    return getExtension(GwoComboString.class);
  }

  /**
   * Sets the string that specifies a combination in an experiment.
   *
   * @param comboString string that specifies a combination in an experiment or
   *     <code>null</code> to reset
   */
  public void setComboString(GwoComboString comboString) {
    if (comboString == null) {
      removeExtension(GwoComboString.class);
    } else {
      setExtension(comboString);
    }
  }

  /**
   * Returns whether it has the string that specifies a combination in an
   * experiment.
   *
   * @return whether it has the string that specifies a combination in an
   *     experiment
   */
  public boolean hasComboString() {
    return hasExtension(GwoComboString.class);
  }

  /**
   * Returns the experiment ID.
   *
   * @return experiment ID
   */
  public GwoExperimentId getExperimentId() {
    return getExtension(GwoExperimentId.class);
  }

  /**
   * Sets the experiment ID.
   *
   * @param experimentId experiment ID or <code>null</code> to reset
   */
  public void setExperimentId(GwoExperimentId experimentId) {
    if (experimentId == null) {
      removeExtension(GwoExperimentId.class);
    } else {
      setExtension(experimentId);
    }
  }

  /**
   * Returns whether it has the experiment ID.
   *
   * @return whether it has the experiment ID
   */
  public boolean hasExperimentId() {
    return hasExtension(GwoExperimentId.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CombinationEntry " + super.toString() + "}";
  }

}

