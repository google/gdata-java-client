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
 * Entry element for experiment feed.
 *
 * 
 */
public class ExperimentEntry extends BaseEntry<ExperimentEntry> {

  /**
   * Default mutable constructor.
   */
  public ExperimentEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ExperimentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ExperimentEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ExperimentEntry.class, GwoAnalyticsAccountId.class);
    extProfile.declare(ExperimentEntry.class, GwoAutoPruneMode.class);
    extProfile.declare(ExperimentEntry.class, GwoControlScript.class);
    extProfile.declare(ExperimentEntry.class, GwoConversionScript.class);
    extProfile.declare(ExperimentEntry.class, GwoCoverage.class);
    extProfile.declare(ExperimentEntry.class, GwoExperimentId.class);
    extProfile.declare(ExperimentEntry.class, GwoExperimentNotes.class);
    extProfile.declare(ExperimentEntry.class, GwoExperimentType.class);
    extProfile.declare(ExperimentEntry.class, GwoNumAbPageVariations.class);
    extProfile.declare(ExperimentEntry.class, GwoNumCombinations.class);
    extProfile.declare(ExperimentEntry.class, GwoNumSections.class);
    extProfile.declare(ExperimentEntry.class, GwoSourceExperimentId.class);
    extProfile.declare(ExperimentEntry.class, GwoStatus.class);
    extProfile.declare(ExperimentEntry.class, GwoTrackingScript.class);
    extProfile.declare(ExperimentEntry.class, GwoVerificationCombo.class);
    extProfile.declare(ExperimentEntry.class,
        GwoVerificationComboCoverage.class);
  }

  /**
   * Returns the Analytics account ID.
   *
   * @return Analytics account ID
   */
  public GwoAnalyticsAccountId getAnalyticsAccountId() {
    return getExtension(GwoAnalyticsAccountId.class);
  }

  /**
   * Sets the Analytics account ID.
   *
   * @param analyticsAccountId Analytics account ID or <code>null</code> to
   *     reset
   */
  public void setAnalyticsAccountId(GwoAnalyticsAccountId analyticsAccountId) {
    if (analyticsAccountId == null) {
      removeExtension(GwoAnalyticsAccountId.class);
    } else {
      setExtension(analyticsAccountId);
    }
  }

  /**
   * Returns whether it has the Analytics account ID.
   *
   * @return whether it has the Analytics account ID
   */
  public boolean hasAnalyticsAccountId() {
    return hasExtension(GwoAnalyticsAccountId.class);
  }

  /**
   * Returns the auto-prune mode.
   *
   * @return auto-prune mode
   */
  public GwoAutoPruneMode getAutoPruneMode() {
    return getExtension(GwoAutoPruneMode.class);
  }

  /**
   * Sets the auto-prune mode.
   *
   * @param autoPruneMode auto-prune mode or <code>null</code> to reset
   */
  public void setAutoPruneMode(GwoAutoPruneMode autoPruneMode) {
    if (autoPruneMode == null) {
      removeExtension(GwoAutoPruneMode.class);
    } else {
      setExtension(autoPruneMode);
    }
  }

  /**
   * Returns whether it has the auto-prune mode.
   *
   * @return whether it has the auto-prune mode
   */
  public boolean hasAutoPruneMode() {
    return hasExtension(GwoAutoPruneMode.class);
  }

  /**
   * Returns the script to control the experiment.
   *
   * @return script to control the experiment
   */
  public GwoControlScript getControlScript() {
    return getExtension(GwoControlScript.class);
  }

  /**
   * Sets the script to control the experiment.
   *
   * @param controlScript script to control the experiment or <code>null</code>
   *     to reset
   */
  public void setControlScript(GwoControlScript controlScript) {
    if (controlScript == null) {
      removeExtension(GwoControlScript.class);
    } else {
      setExtension(controlScript);
    }
  }

  /**
   * Returns whether it has the script to control the experiment.
   *
   * @return whether it has the script to control the experiment
   */
  public boolean hasControlScript() {
    return hasExtension(GwoControlScript.class);
  }

  /**
   * Returns the script to record conversions.
   *
   * @return script to record conversions
   */
  public GwoConversionScript getConversionScript() {
    return getExtension(GwoConversionScript.class);
  }

  /**
   * Sets the script to record conversions.
   *
   * @param conversionScript script to record conversions or <code>null</code>
   *     to reset
   */
  public void setConversionScript(GwoConversionScript conversionScript) {
    if (conversionScript == null) {
      removeExtension(GwoConversionScript.class);
    } else {
      setExtension(conversionScript);
    }
  }

  /**
   * Returns whether it has the script to record conversions.
   *
   * @return whether it has the script to record conversions
   */
  public boolean hasConversionScript() {
    return hasExtension(GwoConversionScript.class);
  }

  /**
   * Returns the percentage of traffic to send to the experiment.
   *
   * @return percentage of traffic to send to the experiment
   */
  public GwoCoverage getCoverage() {
    return getExtension(GwoCoverage.class);
  }

  /**
   * Sets the percentage of traffic to send to the experiment.
   *
   * @param coverage percentage of traffic to send to the experiment or
   *     <code>null</code> to reset
   */
  public void setCoverage(GwoCoverage coverage) {
    if (coverage == null) {
      removeExtension(GwoCoverage.class);
    } else {
      setExtension(coverage);
    }
  }

  /**
   * Returns whether it has the percentage of traffic to send to the experiment.
   *
   * @return whether it has the percentage of traffic to send to the experiment
   */
  public boolean hasCoverage() {
    return hasExtension(GwoCoverage.class);
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

  /**
   * Returns the experiment's notes.
   *
   * @return experiment's notes
   */
  public GwoExperimentNotes getExperimentNotes() {
    return getExtension(GwoExperimentNotes.class);
  }

  /**
   * Sets the experiment's notes.
   *
   * @param experimentNotes experiment's notes or <code>null</code> to reset
   */
  public void setExperimentNotes(GwoExperimentNotes experimentNotes) {
    if (experimentNotes == null) {
      removeExtension(GwoExperimentNotes.class);
    } else {
      setExtension(experimentNotes);
    }
  }

  /**
   * Returns whether it has the experiment's notes.
   *
   * @return whether it has the experiment's notes
   */
  public boolean hasExperimentNotes() {
    return hasExtension(GwoExperimentNotes.class);
  }

  /**
   * Returns the experiment type.
   *
   * @return experiment type
   */
  public GwoExperimentType getExperimentType() {
    return getExtension(GwoExperimentType.class);
  }

  /**
   * Sets the experiment type.
   *
   * @param experimentType experiment type or <code>null</code> to reset
   */
  public void setExperimentType(GwoExperimentType experimentType) {
    if (experimentType == null) {
      removeExtension(GwoExperimentType.class);
    } else {
      setExtension(experimentType);
    }
  }

  /**
   * Returns whether it has the experiment type.
   *
   * @return whether it has the experiment type
   */
  public boolean hasExperimentType() {
    return hasExtension(GwoExperimentType.class);
  }

  /**
   * Returns the number of page variations in an A/B experiment.
   *
   * @return number of page variations in an A/B experiment
   */
  public GwoNumAbPageVariations getNumAbPageVariations() {
    return getExtension(GwoNumAbPageVariations.class);
  }

  /**
   * Sets the number of page variations in an A/B experiment.
   *
   * @param numAbPageVariations number of page variations in an A/B experiment
   *     or <code>null</code> to reset
   */
  public void setNumAbPageVariations(GwoNumAbPageVariations numAbPageVariations)
      {
    if (numAbPageVariations == null) {
      removeExtension(GwoNumAbPageVariations.class);
    } else {
      setExtension(numAbPageVariations);
    }
  }

  /**
   * Returns whether it has the number of page variations in an A/B experiment.
   *
   * @return whether it has the number of page variations in an A/B experiment
   */
  public boolean hasNumAbPageVariations() {
    return hasExtension(GwoNumAbPageVariations.class);
  }

  /**
   * Returns the number of combinations.
   *
   * @return number of combinations
   */
  public GwoNumCombinations getNumCombinations() {
    return getExtension(GwoNumCombinations.class);
  }

  /**
   * Sets the number of combinations.
   *
   * @param numCombinations number of combinations or <code>null</code> to reset
   */
  public void setNumCombinations(GwoNumCombinations numCombinations) {
    if (numCombinations == null) {
      removeExtension(GwoNumCombinations.class);
    } else {
      setExtension(numCombinations);
    }
  }

  /**
   * Returns whether it has the number of combinations.
   *
   * @return whether it has the number of combinations
   */
  public boolean hasNumCombinations() {
    return hasExtension(GwoNumCombinations.class);
  }

  /**
   * Returns the number of sections in a multivariate experiment.
   *
   * @return number of sections in a multivariate experiment
   */
  public GwoNumSections getNumSections() {
    return getExtension(GwoNumSections.class);
  }

  /**
   * Sets the number of sections in a multivariate experiment.
   *
   * @param numSections number of sections in a multivariate experiment or
   *     <code>null</code> to reset
   */
  public void setNumSections(GwoNumSections numSections) {
    if (numSections == null) {
      removeExtension(GwoNumSections.class);
    } else {
      setExtension(numSections);
    }
  }

  /**
   * Returns whether it has the number of sections in a multivariate experiment.
   *
   * @return whether it has the number of sections in a multivariate experiment
   */
  public boolean hasNumSections() {
    return hasExtension(GwoNumSections.class);
  }

  /**
   * Returns the experiment ID that identifies the source experiment.
   *
   * @return experiment ID that identifies the source experiment
   */
  public GwoSourceExperimentId getSourceExperimentId() {
    return getExtension(GwoSourceExperimentId.class);
  }

  /**
   * Sets the experiment ID that identifies the source experiment.
   *
   * @param sourceExperimentId experiment ID that identifies the source
   *     experiment or <code>null</code> to reset
   */
  public void setSourceExperimentId(GwoSourceExperimentId sourceExperimentId) {
    if (sourceExperimentId == null) {
      removeExtension(GwoSourceExperimentId.class);
    } else {
      setExtension(sourceExperimentId);
    }
  }

  /**
   * Returns whether it has the experiment ID that identifies the source
   * experiment.
   *
   * @return whether it has the experiment ID that identifies the source
   *     experiment
   */
  public boolean hasSourceExperimentId() {
    return hasExtension(GwoSourceExperimentId.class);
  }

  /**
   * Returns the experiment status.
   *
   * @return experiment status
   */
  public GwoStatus getStatus() {
    return getExtension(GwoStatus.class);
  }

  /**
   * Sets the experiment status.
   *
   * @param status experiment status or <code>null</code> to reset
   */
  public void setStatus(GwoStatus status) {
    if (status == null) {
      removeExtension(GwoStatus.class);
    } else {
      setExtension(status);
    }
  }

  /**
   * Returns whether it has the experiment status.
   *
   * @return whether it has the experiment status
   */
  public boolean hasStatus() {
    return hasExtension(GwoStatus.class);
  }

  /**
   * Returns the script to track this experiment.
   *
   * @return script to track this experiment
   */
  public GwoTrackingScript getTrackingScript() {
    return getExtension(GwoTrackingScript.class);
  }

  /**
   * Sets the script to track this experiment.
   *
   * @param trackingScript script to track this experiment or <code>null</code>
   *     to reset
   */
  public void setTrackingScript(GwoTrackingScript trackingScript) {
    if (trackingScript == null) {
      removeExtension(GwoTrackingScript.class);
    } else {
      setExtension(trackingScript);
    }
  }

  /**
   * Returns whether it has the script to track this experiment.
   *
   * @return whether it has the script to track this experiment
   */
  public boolean hasTrackingScript() {
    return hasExtension(GwoTrackingScript.class);
  }

  /**
   * Returns the verification combination index.
   *
   * @return verification combination index
   */
  public GwoVerificationCombo getVerificationCombo() {
    return getExtension(GwoVerificationCombo.class);
  }

  /**
   * Sets the verification combination index.
   *
   * @param verificationCombo verification combination index or
   *     <code>null</code> to reset
   */
  public void setVerificationCombo(GwoVerificationCombo verificationCombo) {
    if (verificationCombo == null) {
      removeExtension(GwoVerificationCombo.class);
    } else {
      setExtension(verificationCombo);
    }
  }

  /**
   * Returns whether it has the verification combination index.
   *
   * @return whether it has the verification combination index
   */
  public boolean hasVerificationCombo() {
    return hasExtension(GwoVerificationCombo.class);
  }

  /**
   * Returns the percentage of traffic to send to the verify combo.
   *
   * @return percentage of traffic to send to the verify combo
   */
  public GwoVerificationComboCoverage getVerificationComboCoverage() {
    return getExtension(GwoVerificationComboCoverage.class);
  }

  /**
   * Sets the percentage of traffic to send to the verify combo.
   *
   * @param verificationComboCoverage percentage of traffic to send to the
   *     verify combo or <code>null</code> to reset
   */
  public void setVerificationComboCoverage(GwoVerificationComboCoverage
      verificationComboCoverage) {
    if (verificationComboCoverage == null) {
      removeExtension(GwoVerificationComboCoverage.class);
    } else {
      setExtension(verificationComboCoverage);
    }
  }

  /**
   * Returns whether it has the percentage of traffic to send to the verify
   * combo.
   *
   * @return whether it has the percentage of traffic to send to the verify
   *     combo
   */
  public boolean hasVerificationComboCoverage() {
    return hasExtension(GwoVerificationComboCoverage.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ExperimentEntry " + super.toString() + "}";
  }

}

