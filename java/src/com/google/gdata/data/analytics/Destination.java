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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * Destination-URL goal.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.GA_ALIAS,
    nsUri = AnalyticsNamespace.GA,
    localName = Destination.XML_NAME)
public class Destination extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "destination";

  /** XML "caseSensitive" attribute name */
  private static final String CASESENSITIVE = "caseSensitive";

  /** XML "expression" attribute name */
  private static final String EXPRESSION = "expression";

  /** XML "matchType" attribute name */
  private static final String MATCHTYPE = "matchType";

  /** XML "step1Required" attribute name */
  private static final String STEP1REQUIRED = "step1Required";

  /** Whether path URL matching is case sensitive */
  private Boolean caseSensitive = null;

  /** Goal's path expression */
  private String expression = null;

  /** The match type for the path expression */
  private String matchType = null;

  /** Whether step 1 is required for this goal */
  private Boolean step1Required = null;

  /** The match type for the path expression. */
  public static final class MatchType {

    /** Exact destination. */
    public static final String EXACT = "exact";

    /** Head destination. */
    public static final String HEAD = "head";

    /** Regex destination. */
    public static final String REGEX = "regex";

  }

  /**
   * Default mutable constructor.
   */
  public Destination() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param caseSensitive whether path URL matching is case sensitive.
   * @param expression goal's path expression.
   * @param matchType the match type for the path expression.
   * @param step1Required whether step 1 is required for this goal.
   */
  public Destination(Boolean caseSensitive, String expression, String matchType,
      Boolean step1Required) {
    super();
    setCaseSensitive(caseSensitive);
    setExpression(expression);
    setMatchType(matchType);
    setStep1Required(step1Required);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Destination.class)) {
      return;
    }
    extProfile.declare(Destination.class, Step.getDefaultDescription(false,
        true));
  }

  /**
   * Returns the whether path URL matching is case sensitive.
   *
   * @return whether path URL matching is case sensitive
   */
  public Boolean getCaseSensitive() {
    return caseSensitive;
  }

  /**
   * Sets the whether path URL matching is case sensitive.
   *
   * @param caseSensitive whether path URL matching is case sensitive or
   *     <code>null</code> to reset
   */
  public void setCaseSensitive(Boolean caseSensitive) {
    throwExceptionIfImmutable();
    this.caseSensitive = caseSensitive;
  }

  /**
   * Returns whether it has the whether path URL matching is case sensitive.
   *
   * @return whether it has the whether path URL matching is case sensitive
   */
  public boolean hasCaseSensitive() {
    return getCaseSensitive() != null;
  }

  /**
   * Returns the goal's path expression.
   *
   * @return goal's path expression
   */
  public String getExpression() {
    return expression;
  }

  /**
   * Sets the goal's path expression.
   *
   * @param expression goal's path expression or <code>null</code> to reset
   */
  public void setExpression(String expression) {
    throwExceptionIfImmutable();
    this.expression = expression;
  }

  /**
   * Returns whether it has the goal's path expression.
   *
   * @return whether it has the goal's path expression
   */
  public boolean hasExpression() {
    return getExpression() != null;
  }

  /**
   * Returns the the match type for the path expression.
   *
   * @return the match type for the path expression
   */
  public String getMatchType() {
    return matchType;
  }

  /**
   * Sets the the match type for the path expression.
   *
   * @param matchType the match type for the path expression or
   *     <code>null</code> to reset
   */
  public void setMatchType(String matchType) {
    throwExceptionIfImmutable();
    this.matchType = matchType;
  }

  /**
   * Returns whether it has the the match type for the path expression.
   *
   * @return whether it has the the match type for the path expression
   */
  public boolean hasMatchType() {
    return getMatchType() != null;
  }

  /**
   * Returns the whether step 1 is required for this goal.
   *
   * @return whether step 1 is required for this goal
   */
  public Boolean getStep1Required() {
    return step1Required;
  }

  /**
   * Sets the whether step 1 is required for this goal.
   *
   * @param step1Required whether step 1 is required for this goal or
   *     <code>null</code> to reset
   */
  public void setStep1Required(Boolean step1Required) {
    throwExceptionIfImmutable();
    this.step1Required = step1Required;
  }

  /**
   * Returns whether it has the whether step 1 is required for this goal.
   *
   * @return whether it has the whether step 1 is required for this goal
   */
  public boolean hasStep1Required() {
    return getStep1Required() != null;
  }

  /**
   * Returns the steps.
   *
   * @return steps
   */
  public List<Step> getSteps() {
    return getRepeatingExtension(Step.class);
  }

  /**
   * Adds a new step.
   *
   * @param step step
   */
  public void addStep(Step step) {
    getSteps().add(step);
  }

  /**
   * Returns whether it has the steps.
   *
   * @return whether it has the steps
   */
  public boolean hasSteps() {
    return hasRepeatingExtension(Step.class);
  }

  @Override
  protected void validate() {
    if (caseSensitive == null) {
      throwExceptionForMissingAttribute(CASESENSITIVE);
    }
    if (expression == null) {
      throwExceptionForMissingAttribute(EXPRESSION);
    }
    if (matchType == null) {
      throwExceptionForMissingAttribute(MATCHTYPE);
    }
    if (step1Required == null) {
      throwExceptionForMissingAttribute(STEP1REQUIRED);
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
        ExtensionDescription.getDefaultDescription(Destination.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(CASESENSITIVE, caseSensitive);
    generator.put(EXPRESSION, expression);
    generator.put(MATCHTYPE, matchType);
    generator.put(STEP1REQUIRED, step1Required);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    caseSensitive = helper.consumeBoolean(CASESENSITIVE, true);
    expression = helper.consume(EXPRESSION, true);
    matchType = helper.consume(MATCHTYPE, true);
    step1Required = helper.consumeBoolean(STEP1REQUIRED, true);
  }

  @Override
  public String toString() {
    return "{Destination caseSensitive=" + caseSensitive + " expression=" +
        expression + " matchType=" + matchType + " step1Required=" +
        step1Required + "}";
  }

}

