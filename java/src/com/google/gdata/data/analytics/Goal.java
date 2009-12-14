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

/**
 * Google Analytics goal.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.GA_ALIAS,
    nsUri = AnalyticsNamespace.GA,
    localName = Goal.XML_NAME)
public class Goal extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "goal";

  /** XML "active" attribute name */
  private static final String ACTIVE = "active";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "number" attribute name */
  private static final String NUMBER = "number";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Whether the goal is currently active */
  private Boolean active = null;

  /** Goal's name */
  private String name = null;

  /** Goal's number */
  private Integer number = null;

  /** Goal's monetary value */
  private Double value = null;

  /**
   * Default mutable constructor.
   */
  public Goal() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param active whether the goal is currently active.
   * @param name goal's name.
   * @param number goal's number.
   * @param value goal's monetary value.
   */
  public Goal(Boolean active, String name, Integer number, Double value) {
    super();
    setActive(active);
    setName(name);
    setNumber(number);
    setValue(value);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Goal.class)) {
      return;
    }
    extProfile.declare(Goal.class, Destination.class);
    new Destination().declareExtensions(extProfile);
    extProfile.declare(Goal.class, Engagement.class);
  }

  /**
   * Returns the whether the goal is currently active.
   *
   * @return whether the goal is currently active
   */
  public Boolean getActive() {
    return active;
  }

  /**
   * Sets the whether the goal is currently active.
   *
   * @param active whether the goal is currently active or <code>null</code> to
   *     reset
   */
  public void setActive(Boolean active) {
    throwExceptionIfImmutable();
    this.active = active;
  }

  /**
   * Returns whether it has the whether the goal is currently active.
   *
   * @return whether it has the whether the goal is currently active
   */
  public boolean hasActive() {
    return getActive() != null;
  }

  /**
   * Returns the destination.
   *
   * @return destination
   */
  public Destination getDestination() {
    return getExtension(Destination.class);
  }

  /**
   * Sets the destination.
   *
   * @param destination destination or <code>null</code> to reset
   */
  public void setDestination(Destination destination) {
    if (destination == null) {
      removeExtension(Destination.class);
    } else {
      setExtension(destination);
    }
  }

  /**
   * Returns whether it has the destination.
   *
   * @return whether it has the destination
   */
  public boolean hasDestination() {
    return hasExtension(Destination.class);
  }

  /**
   * Returns the engagement.
   *
   * @return engagement
   */
  public Engagement getEngagement() {
    return getExtension(Engagement.class);
  }

  /**
   * Sets the engagement.
   *
   * @param engagement engagement or <code>null</code> to reset
   */
  public void setEngagement(Engagement engagement) {
    if (engagement == null) {
      removeExtension(Engagement.class);
    } else {
      setExtension(engagement);
    }
  }

  /**
   * Returns whether it has the engagement.
   *
   * @return whether it has the engagement
   */
  public boolean hasEngagement() {
    return hasExtension(Engagement.class);
  }

  /**
   * Returns the goal's name.
   *
   * @return goal's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the goal's name.
   *
   * @param name goal's name or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the goal's name.
   *
   * @return whether it has the goal's name
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the goal's number.
   *
   * @return goal's number
   */
  public Integer getNumber() {
    return number;
  }

  /**
   * Sets the goal's number.
   *
   * @param number goal's number or <code>null</code> to reset
   */
  public void setNumber(Integer number) {
    throwExceptionIfImmutable();
    this.number = number;
  }

  /**
   * Returns whether it has the goal's number.
   *
   * @return whether it has the goal's number
   */
  public boolean hasNumber() {
    return getNumber() != null;
  }

  /**
   * Returns the goal's monetary value.
   *
   * @return goal's monetary value
   */
  public Double getValue() {
    return value;
  }

  /**
   * Sets the goal's monetary value.
   *
   * @param value goal's monetary value or <code>null</code> to reset
   */
  public void setValue(Double value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the goal's monetary value.
   *
   * @return whether it has the goal's monetary value
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (active == null) {
      throwExceptionForMissingAttribute(ACTIVE);
    }
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
    }
    if (number == null) {
      throwExceptionForMissingAttribute(NUMBER);
    }
    if (value == null) {
      throwExceptionForMissingAttribute(VALUE);
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
        ExtensionDescription.getDefaultDescription(Goal.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(ACTIVE, active);
    generator.put(NAME, name);
    generator.put(NUMBER, number);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    active = helper.consumeBoolean(ACTIVE, true);
    name = helper.consume(NAME, true);
    number = helper.consumeInteger(NUMBER, true);
    value = helper.consumeDouble(VALUE, true);
  }

  @Override
  public String toString() {
    return "{Goal active=" + active + " name=" + name + " number=" + number +
        " value=" + value + "}";
  }

}

