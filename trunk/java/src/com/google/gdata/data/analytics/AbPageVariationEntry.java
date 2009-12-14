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
 * Entry element for abpagevariation feed.
 *
 * 
 */
public class AbPageVariationEntry extends BaseEntry<AbPageVariationEntry> {

  /**
   * Default mutable constructor.
   */
  public AbPageVariationEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public AbPageVariationEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AbPageVariationEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(AbPageVariationEntry.class, GwoAbPageVariationId.class);
    extProfile.declare(AbPageVariationEntry.class, GwoExperimentId.class);
  }

  /**
   * Returns the A/B experiment page variation ID.
   *
   * @return A/B experiment page variation ID
   */
  public GwoAbPageVariationId getAbPageVariationId() {
    return getExtension(GwoAbPageVariationId.class);
  }

  /**
   * Sets the A/B experiment page variation ID.
   *
   * @param abPageVariationId A/B experiment page variation ID or
   *     <code>null</code> to reset
   */
  public void setAbPageVariationId(GwoAbPageVariationId abPageVariationId) {
    if (abPageVariationId == null) {
      removeExtension(GwoAbPageVariationId.class);
    } else {
      setExtension(abPageVariationId);
    }
  }

  /**
   * Returns whether it has the A/B experiment page variation ID.
   *
   * @return whether it has the A/B experiment page variation ID
   */
  public boolean hasAbPageVariationId() {
    return hasExtension(GwoAbPageVariationId.class);
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
    return "{AbPageVariationEntry " + super.toString() + "}";
  }

}

