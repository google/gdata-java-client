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


package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes a rating.
 *
 * 
 */
public class Rating extends Element {

  /** Programmatic value that describes the aspect that's being rated, if not
   * specified, the rating is an overall rating. */
  public static final class Rel {

    /** Overall rating, higher values mean better ratings. */
    public static final String OVERALL = Namespaces.gPrefix + "overall";

    /** Price rating, higher values mean better ratings. */
    public static final String PRICE = Namespaces.gPrefix + "price";

    /** Quality rating, higher values mean better ratings. */
    public static final String QUALITY = Namespaces.gPrefix + "quality";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      OVERALL,
      PRICE,
      QUALITY};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Rating> KEY = ElementKey.of(new QName(Namespaces.gNs, "rating"),
      Void.class, Rating.class);

  /**
   * Average rating.
   */
  public static final AttributeKey<Float> AVERAGE = AttributeKey.of(new
      QName(null, "average"), Float.class);

  /**
   * Rating scale's maximum value.
   */
  public static final AttributeKey<Integer> MAX = AttributeKey.of(new
      QName(null, "max"), Integer.class);

  /**
   * Rating scale's minimum value.
   */
  public static final AttributeKey<Integer> MIN = AttributeKey.of(new
      QName(null, "min"), Integer.class);

  /**
   * Number of ratings taken into account when computing the average value.
   */
  public static final AttributeKey<Integer> NUM_RATERS = AttributeKey.of(new
      QName(null, "numRaters"), Integer.class);

  /**
   * Programmatic value that describes the aspect that's being rated, if not
   * specified, the rating is an overall rating.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Rating value.
   */
  public static final AttributeKey<Integer> VALUE = AttributeKey.of(new
      QName(null, "value"), Integer.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(AVERAGE);
    builder.addAttribute(MAX);
    builder.addAttribute(MIN);
    builder.addAttribute(NUM_RATERS);
    builder.addAttribute(REL);
    builder.addAttribute(VALUE);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Rating() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Rating(ElementKey<?, ? extends Rating> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected Rating(ElementKey<?, ? extends Rating> key, Element source) {
    super(key, source);
  }

  @Override
  public Rating lock() {
    return (Rating) super.lock();
  }

  /**
   * Returns the average rating.
   *
   * @return average rating
   */
  public Float getAverage() {
    return super.getAttributeValue(AVERAGE);
  }

  /**
   * Sets the average rating.
   *
   * @param average average rating or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Rating setAverage(Float average) {
    super.setAttributeValue(AVERAGE, average);
    return this;
  }

  /**
   * Returns whether it has the average rating.
   *
   * @return whether it has the average rating
   */
  public boolean hasAverage() {
    return super.hasAttribute(AVERAGE);
  }

  /**
   * Returns the rating scale's maximum value.
   *
   * @return rating scale's maximum value
   */
  public Integer getMax() {
    return super.getAttributeValue(MAX);
  }

  /**
   * Sets the rating scale's maximum value.
   *
   * @param max rating scale's maximum value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Rating setMax(Integer max) {
    super.setAttributeValue(MAX, max);
    return this;
  }

  /**
   * Returns whether it has the rating scale's maximum value.
   *
   * @return whether it has the rating scale's maximum value
   */
  public boolean hasMax() {
    return super.hasAttribute(MAX);
  }

  /**
   * Returns the rating scale's minimum value.
   *
   * @return rating scale's minimum value
   */
  public Integer getMin() {
    return super.getAttributeValue(MIN);
  }

  /**
   * Sets the rating scale's minimum value.
   *
   * @param min rating scale's minimum value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Rating setMin(Integer min) {
    super.setAttributeValue(MIN, min);
    return this;
  }

  /**
   * Returns whether it has the rating scale's minimum value.
   *
   * @return whether it has the rating scale's minimum value
   */
  public boolean hasMin() {
    return super.hasAttribute(MIN);
  }

  /**
   * Returns the number of ratings taken into account when computing the average
   * value.
   *
   * @return number of ratings taken into account when computing the average
   *     value
   */
  public Integer getNumRaters() {
    return super.getAttributeValue(NUM_RATERS);
  }

  /**
   * Sets the number of ratings taken into account when computing the average
   * value.
   *
   * @param numRaters number of ratings taken into account when computing the
   *     average value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Rating setNumRaters(Integer numRaters) {
    super.setAttributeValue(NUM_RATERS, numRaters);
    return this;
  }

  /**
   * Returns whether it has the number of ratings taken into account when
   * computing the average value.
   *
   * @return whether it has the number of ratings taken into account when
   *     computing the average value
   */
  public boolean hasNumRaters() {
    return super.hasAttribute(NUM_RATERS);
  }

  /**
   * Returns the programmatic value that describes the aspect that's being
   * rated, if not specified, the rating is an overall rating.
   *
   * @return programmatic value that describes the aspect that's being rated, if
   *     not specified, the rating is an overall rating
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the programmatic value that describes the aspect that's being rated,
   * if not specified, the rating is an overall rating.
   *
   * @param rel programmatic value that describes the aspect that's being rated,
   *     if not specified, the rating is an overall rating or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public Rating setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the programmatic value that describes the aspect
   * that's being rated, if not specified, the rating is an overall rating.
   *
   * @return whether it has the programmatic value that describes the aspect
   *     that's being rated, if not specified, the rating is an overall rating
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the rating value.
   *
   * @return rating value
   */
  public Integer getValue() {
    return super.getAttributeValue(VALUE);
  }

  /**
   * Sets the rating value.
   *
   * @param value rating value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Rating setValue(Integer value) {
    super.setAttributeValue(VALUE, value);
    return this;
  }

  /**
   * Returns whether it has the rating value.
   *
   * @return whether it has the rating value
   */
  public boolean hasValue() {
    return super.hasAttribute(VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Rating other = (Rating) obj;
    return eq(getAverage(), other.getAverage())
        && eq(getMax(), other.getMax())
        && eq(getMin(), other.getMin())
        && eq(getNumRaters(), other.getNumRaters())
        && eq(getRel(), other.getRel())
        && eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getAverage() != null) {
      result = 37 * result + getAverage().hashCode();
    }
    if (getMax() != null) {
      result = 37 * result + getMax().hashCode();
    }
    if (getMin() != null) {
      result = 37 * result + getMin().hashCode();
    }
    if (getNumRaters() != null) {
      result = 37 * result + getNumRaters().hashCode();
    }
    if (getRel() != null) {
      result = 37 * result + getRel().hashCode();
    }
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


