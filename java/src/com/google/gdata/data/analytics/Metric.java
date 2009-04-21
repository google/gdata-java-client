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
 * Metric value.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = Metric.XML_NAME)
public class Metric extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "metric";

  /** XML "confidenceInterval" attribute name */
  private static final String CONFIDENCEINTERVAL = "confidenceInterval";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** 95% confidence interval for this metric (lower is better) */
  private Double confidenceInterval = null;

  /** Name */
  private String name = null;

  /** The format of data represented by the metric */
  private String type = null;

  /** Value */
  private String value = null;

  /** The format of data represented by the metric. */
  public static final class Type {

    /** Currency metric. */
    public static final String CURRENCY = "currency";

    /** Float metric. */
    public static final String FLOAT = "float";

    /** Integer metric. */
    public static final String INTEGER = "integer";

    /** Percent metric. */
    public static final String PERCENT = "percent";

    /** Time metric. */
    public static final String TIME = "time";

    /** Us currency metric. */
    public static final String US_CURRENCY = "us_currency";

  }

  /**
   * Default mutable constructor.
   */
  public Metric() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param confidenceInterval 95% confidence interval for this metric (lower is
   *     better).
   * @param name name.
   * @param type the format of data represented by the metric.
   * @param value value.
   */
  public Metric(Double confidenceInterval, String name, String type,
      String value) {
    super();
    setConfidenceInterval(confidenceInterval);
    setName(name);
    setType(type);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the 95% confidence interval for this metric (lower is better).
   *
   * @return 95% confidence interval for this metric (lower is better)
   */
  public Double getConfidenceInterval() {
    return confidenceInterval;
  }

  /**
   * Sets the 95% confidence interval for this metric (lower is better).
   *
   * @param confidenceInterval 95% confidence interval for this metric (lower is
   *     better) or <code>null</code> to reset
   */
  public void setConfidenceInterval(Double confidenceInterval) {
    throwExceptionIfImmutable();
    this.confidenceInterval = confidenceInterval;
  }

  /**
   * Returns whether it has the 95% confidence interval for this metric (lower
   * is better).
   *
   * @return whether it has the 95% confidence interval for this metric (lower
   *     is better)
   */
  public boolean hasConfidenceInterval() {
    return getConfidenceInterval() != null;
  }

  /**
   * Returns the name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name name or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the name.
   *
   * @return whether it has the name
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the the format of data represented by the metric.
   *
   * @return the format of data represented by the metric
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the the format of data represented by the metric.
   *
   * @param type the format of data represented by the metric or
   *     <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the the format of data represented by the metric.
   *
   * @return whether it has the the format of data represented by the metric
   */
  public boolean hasType() {
    return getType() != null;
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
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
        ExtensionDescription.getDefaultDescription(Metric.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(CONFIDENCEINTERVAL, confidenceInterval);
    generator.put(NAME, name);
    generator.put(TYPE, type);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    confidenceInterval = helper.consumeDouble(CONFIDENCEINTERVAL, false);
    name = helper.consume(NAME, true);
    type = helper.consume(TYPE, false);
    value = helper.consume(VALUE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Metric other = (Metric) obj;
    return eq(confidenceInterval, other.confidenceInterval)
        && eq(name, other.name)
        && eq(type, other.type)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (confidenceInterval != null) {
      result = 37 * result + confidenceInterval.hashCode();
    }
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Metric confidenceInterval=" + confidenceInterval + " name=" + name
        + " type=" + type + " value=" + value + "}";
  }


  /**
   * Gets a numeric representation of this metric's value. The exact subclass
   * of Number returned can be determined by the result of getType(): if
   * getType() returns {@code "integer"} then the value returned by this method
   * is an instance of java.lang.Integer; otherwise it is an instance of
   * java.lang.Double.
   *
   * @return A numeric representation of the value of this metric
   * @throws NumberFormatException If the value of this metric cannot be parsed
   *     into the the most appropriate numeric format for its type
   */
  public Number numericValue() {
    String type = getType();
    if (Type.INTEGER.equals(type)) {
      return Long.parseLong(getValue());
    } else {
      // type is currency, float, percent, time, us_currency, an unknown type,
      // or not set
      return Double.parseDouble(getValue());
    }
  }

  /**
   * Gets the long value of this metric. If the underlying data is a
   * floating-point number, then the number returned by this method is
   * the result of casting that floating-point number to type long.
   */
  public long longValue() {
    return numericValue().longValue();
  }

  /**
   * Gets the double value of this metric. If the underlying data is an
   * integer value, then the number returned by this method is
   * the result of casting that integer to type double.
   */
  public double doubleValue() {
    return numericValue().doubleValue();
  }

}
