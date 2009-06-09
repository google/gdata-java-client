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


package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * GData schema extension describing an object's rating.
 *
 * 
 */
public class Rating extends ExtensionPoint {


  /**
   * Rating type. Describes the meaning of this rating.
   */
  public static final class Rel {
    /** Overall rating. Higher values mean better ratings. */
    public static final String OVERALL = null;
    /** Quality rating. Higher values mean better quality. */
    public static final String QUALITY = Namespaces.gPrefix + "quality";
    /** Price rating. Higher values mean higher prices. */
    public static final String PRICE = Namespaces.gPrefix + "price";
  }

  /**
   * Constructs an empty {@code Rating}.
   */
  public Rating() {
    this(null);
  }
  
  /**
   * Constructs a {@code Rating} class with a specified rating.
   * 
   * @param rating the value of the rating
   */
  public Rating(Integer rating) {
    this.rating = rating;
  }

  /** Rating type. */
  protected String rel;

  public String getRel() {
    return rel;
  }

  public void setRel(String v) {
    rel = v;
  }


  /** Rating value (required). */
  protected Integer rating;

  public Integer getValue() {
    return rating;
  }

  public void setValue(Integer r) {
    rating = r;
  }


  /** Minimum rating value on rating scale (optional). */
  protected Integer min;

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer r) {
    min = r;
  }


  /** Maximum rating value on rating scale (optional). */
  protected Integer max;

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer r) {
    max = r;
  }

  /** Number of ratings that was taken into account (for average ratings). */
  protected Integer numRaters;

  public Integer getNumRaters() {
    return numRaters;
  }

  public void setNumRaters(Integer r) {
    numRaters = r;
  }

  /** Average ratings, output with a fixed a 2 digit precision. */
  protected Float average;

  public Float getAverage() {
    return average;
  }

  public void setAverage(Float r) {
    average = r;
  }

  /**
   * Returns the suggested extension description with configurable
   * repeatability.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Rating.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("rating");
    desc.setRepeatable(repeatable);
    return desc;
  }

  /** Returns the suggested extension description and is repeatable. */
  public static ExtensionDescription getDefaultDescription() {
    return getDefaultDescription(true);
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (rating != null) {
      attrs.add(new XmlWriter.Attribute("value", rating.toString()));
    }

    if (min != null) {
      attrs.add(new XmlWriter.Attribute("min", min.toString()));
    }

    if (max != null) {
      attrs.add(new XmlWriter.Attribute("max", max.toString()));
    }

    if (numRaters != null) {
      attrs.add(new XmlWriter.Attribute("numRaters", numRaters.toString()));
    }

    if (average != null) {
      attrs.add(new XmlWriter.Attribute(
          "average", String.format(Locale.US, "%2.2f", average)));
    }

    generateStartElement(w, Namespaces.gNs, "rating", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "rating");
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }


  /** <gd:rating> parser */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {

      super(extProfile, Rating.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws NumberFormatException {

      if (namespace.equals("")) {
        if (localName.equals("value")) {
          rating = Integer.parseInt(value);
        } else if (localName.equals("max")) {
          max = Integer.parseInt(value);
        } else if (localName.equals("min")) {
          min = Integer.parseInt(value);
        } else if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("numRaters")) {
          numRaters = Integer.parseInt(value);
        } else if (localName.equals("average")) {
          average = Float.valueOf(value);
        }
      }
    }

    @Override
    public void processEndElement() throws ParseException {

      if (rating == null && average == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.valueOrAverageRequired);
      }

      if (rating != null) {
        if (min != null && rating < min || max != null && rating > max) {
          throw new ParseException(
              CoreErrorDomain.ERR.invalidValueRatingAttribute);
        }
      }

      if (average != null) {
        if (min != null && average < min || max != null && average > max) {
          throw new ParseException(
              CoreErrorDomain.ERR.invalidAverageRatingAttribute);
        }
      }
    }
  }
}
