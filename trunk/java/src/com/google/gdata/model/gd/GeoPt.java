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

import com.google.gdata.data.DateTime;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * GData schema extension describing a geographic location.
 *
 * 
 */
public class GeoPt extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      GeoPt> KEY = ElementKey.of(new QName(Namespaces.gNs, "geoPt"), Void.class,
      GeoPt.class);

  /**
   * Elevation.
   */
  public static final AttributeKey<Float> ELEV = AttributeKey.of(new QName(null,
      "elev"), Float.class);

  /**
   * Label.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Latitude.
   */
  public static final AttributeKey<Float> LAT = AttributeKey.of(new QName(null,
      "lat"), Float.class);

  /**
   * Longitude.
   */
  public static final AttributeKey<Float> LON = AttributeKey.of(new QName(null,
      "lon"), Float.class);

  /**
   * Time.
   */
  public static final AttributeKey<DateTime> TIME = AttributeKey.of(new
      QName(null, "time"), DateTime.class);

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
    builder.addAttribute(ELEV);
    builder.addAttribute(LABEL);
    builder.addAttribute(LAT);
    builder.addAttribute(LON);
    builder.addAttribute(TIME);
  }

  /**
   * Constructs an instance using the default key.
   */
  public GeoPt() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected GeoPt(ElementKey<?, ? extends GeoPt> key) {
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
  protected GeoPt(ElementKey<?, ? extends GeoPt> key, Element source) {
    super(key, source);
  }

  @Override
  public GeoPt lock() {
    return (GeoPt) super.lock();
  }

  /**
   * Returns the Elevation.
   *
   * @return Elevation
   */
  public Float getElev() {
    return super.getAttributeValue(ELEV);
  }

  /**
   * Sets the Elevation.
   *
   * @param elev Elevation or {@code null} to reset
   * @return this to enable chaining setters
   */
  public GeoPt setElev(Float elev) {
    super.setAttributeValue(ELEV, elev);
    return this;
  }

  /**
   * Returns whether it has the Elevation.
   *
   * @return whether it has the Elevation
   */
  public boolean hasElev() {
    return super.hasAttribute(ELEV);
  }

  /**
   * Returns the Label.
   *
   * @return Label
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the Label.
   *
   * @param label Label or {@code null} to reset
   * @return this to enable chaining setters
   */
  public GeoPt setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the Label.
   *
   * @return whether it has the Label
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the Latitude.
   *
   * @return Latitude
   */
  public Float getLat() {
    return super.getAttributeValue(LAT);
  }

  /**
   * Sets the Latitude.
   *
   * @param lat Latitude or {@code null} to reset
   * @return this to enable chaining setters
   */
  public GeoPt setLat(Float lat) {
    super.setAttributeValue(LAT, lat);
    return this;
  }

  /**
   * Returns whether it has the Latitude.
   *
   * @return whether it has the Latitude
   */
  public boolean hasLat() {
    return super.hasAttribute(LAT);
  }

  /**
   * Returns the Longitude.
   *
   * @return Longitude
   */
  public Float getLon() {
    return super.getAttributeValue(LON);
  }

  /**
   * Sets the Longitude.
   *
   * @param lon Longitude or {@code null} to reset
   * @return this to enable chaining setters
   */
  public GeoPt setLon(Float lon) {
    super.setAttributeValue(LON, lon);
    return this;
  }

  /**
   * Returns whether it has the Longitude.
   *
   * @return whether it has the Longitude
   */
  public boolean hasLon() {
    return super.hasAttribute(LON);
  }

  /**
   * Returns the Time.
   *
   * @return Time
   */
  public DateTime getTime() {
    return super.getAttributeValue(TIME);
  }

  /**
   * Sets the Time.
   *
   * @param time Time or {@code null} to reset
   * @return this to enable chaining setters
   */
  public GeoPt setTime(DateTime time) {
    super.setAttributeValue(TIME, time);
    return this;
  }

  /**
   * Returns whether it has the Time.
   *
   * @return whether it has the Time
   */
  public boolean hasTime() {
    return super.hasAttribute(TIME);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    GeoPt other = (GeoPt) obj;
    return eq(getElev(), other.getElev())
        && eq(getLabel(), other.getLabel())
        && eq(getLat(), other.getLat())
        && eq(getLon(), other.getLon())
        && eq(getTime(), other.getTime());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getElev() != null) {
      result = 37 * result + getElev().hashCode();
    }
    if (getLabel() != null) {
      result = 37 * result + getLabel().hashCode();
    }
    if (getLat() != null) {
      result = 37 * result + getLat().hashCode();
    }
    if (getLon() != null) {
      result = 37 * result + getLon().hashCode();
    }
    if (getTime() != null) {
      result = 37 * result + getTime().hashCode();
    }
    return result;
  }
}


