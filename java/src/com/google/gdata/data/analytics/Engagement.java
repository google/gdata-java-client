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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Engagement goal.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.GA_ALIAS,
    nsUri = AnalyticsNamespace.GA,
    localName = Engagement.XML_NAME)
public class Engagement extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "engagement";

  /** XML "comparison" attribute name */
  private static final String COMPARISON = "comparison";

  /** XML "thresholdValue" attribute name */
  private static final String THRESHOLDVALUE = "thresholdValue";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** Engagement goal's comparison operation */
  private String comparison = null;

  /** Threshold value */
  private Long thresholdValue = null;

  /** Goal's threshold type */
  private String type = null;

  /** Engagement goal's comparison operation. */
  public static final class Comparison {

    /** Equal to. */
    public static final String EQ = "=";

    /** Greater than. */
    public static final String GT = ">";

    /** Less than. */
    public static final String LT = "<";

  }

  /** Goal's threshold type. */
  public static final class Type {

    /** PagesVisited engagement. */
    public static final String PAGES_VISITED = "pagesVisited";

    /** TimeOnSite engagement. */
    public static final String TIME_ON_SITE = "timeOnSite";

  }

  /**
   * Default mutable constructor.
   */
  public Engagement() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param comparison engagement goal's comparison operation.
   * @param thresholdValue threshold value.
   * @param type goal's threshold type.
   */
  public Engagement(String comparison, Long thresholdValue, String type) {
    super();
    setComparison(comparison);
    setThresholdValue(thresholdValue);
    setType(type);
    setImmutable(true);
  }

  /**
   * Returns the engagement goal's comparison operation.
   *
   * @return engagement goal's comparison operation
   */
  public String getComparison() {
    return comparison;
  }

  /**
   * Sets the engagement goal's comparison operation.
   *
   * @param comparison engagement goal's comparison operation or
   *     <code>null</code> to reset
   */
  public void setComparison(String comparison) {
    throwExceptionIfImmutable();
    this.comparison = comparison;
  }

  /**
   * Returns whether it has the engagement goal's comparison operation.
   *
   * @return whether it has the engagement goal's comparison operation
   */
  public boolean hasComparison() {
    return getComparison() != null;
  }

  /**
   * Returns the threshold value.
   *
   * @return threshold value
   */
  public Long getThresholdValue() {
    return thresholdValue;
  }

  /**
   * Sets the threshold value.
   *
   * @param thresholdValue threshold value or <code>null</code> to reset
   */
  public void setThresholdValue(Long thresholdValue) {
    throwExceptionIfImmutable();
    this.thresholdValue = thresholdValue;
  }

  /**
   * Returns whether it has the threshold value.
   *
   * @return whether it has the threshold value
   */
  public boolean hasThresholdValue() {
    return getThresholdValue() != null;
  }

  /**
   * Returns the goal's threshold type.
   *
   * @return goal's threshold type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the goal's threshold type.
   *
   * @param type goal's threshold type or <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the goal's threshold type.
   *
   * @return whether it has the goal's threshold type
   */
  public boolean hasType() {
    return getType() != null;
  }

  @Override
  protected void validate() {
    if (comparison == null) {
      throwExceptionForMissingAttribute(COMPARISON);
    }
    if (thresholdValue == null) {
      throwExceptionForMissingAttribute(THRESHOLDVALUE);
    }
    if (type == null) {
      throwExceptionForMissingAttribute(TYPE);
    }
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
        ExtensionDescription.getDefaultDescription(Engagement.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(COMPARISON, comparison);
    generator.put(THRESHOLDVALUE, thresholdValue);
    generator.put(TYPE, type);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    comparison = helper.consume(COMPARISON, true);
    thresholdValue = helper.consumeLong(THRESHOLDVALUE, true);
    type = helper.consume(TYPE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Engagement other = (Engagement) obj;
    return eq(comparison, other.comparison)
        && eq(thresholdValue, other.thresholdValue)
        && eq(type, other.type);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (comparison != null) {
      result = 37 * result + comparison.hashCode();
    }
    if (thresholdValue != null) {
      result = 37 * result + thresholdValue.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Engagement comparison=" + comparison + " thresholdValue=" +
        thresholdValue + " type=" + type + "}";
  }

}

