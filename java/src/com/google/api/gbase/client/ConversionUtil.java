/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.data.DateTime;

/**
 * Some methods for converting strings found in XML files
 * into other types.
 */
class ConversionUtil {

  /**
   * Name of the element inside an attribute of type shipping
   * that contains destination country information.
   */
  private static final String SHIPPING_COUNTRY_ELEMENT_NAME = "country";

  /**
   * Name of the element inside an attribute of type shipping
   * that contains the price.
   */
  private static final String SHIPPING_PRICE_ELEMENT_NAME = "price";

  /**
   * Name of the element inside an attribute of type shipping
   * that contains the shipping service name (shipping method).
   */
  private static final String SHIPPING_SERVICE_ELEMENT_NAME = "service";

  /**
   * Name of the element inside an attribute of type location
   * that contains the latitude.
   */
  private static final String LATITUDE_ELEMENT_NAME = "latitude";

  /**
   * Name of the element inside of an attribute of type location
   * that contains the longitude.
   */
  private static final String LONGITUDE_ELEMENT_NAME = "longitude";


  /** This class is not meant to be instantiated. */
  private ConversionUtil() {
  }


  /** Converts a string to a Float. */
  static Float toFloat(String text) {
    if (text == null) {
      return null;
    }
    return new Float(text);
  }

  /** Converts a string to an Integer. */
  static Integer toInteger(String text) {
    if (text == null) {
      return null;
    }
    return new Integer(text);
  }

  /** Converts a string to a Boolean. */
  static Boolean toBoolean(String text) {
    if (text == null) {
      return null;
    }
    return new Boolean(text);
  }

  /** Converts a string containing a date or a date and a time to a DateTime. */
  static DateTime toDateOrDateTime(String text) {
    if (text == null) {
      return null;
    }
    return DateTime.parseDateTimeChoice(text);
  }

  /**
   * Converts a integer followed by a unit name into an object.
   *
   * @param string a string of the form: <code>integer " " unit</code> or null
   * @return the corresponding object or null
   * @exception NumberFormatException if the conversion failed
   */
  public static NumberUnit<Integer> toIntUnit(String string) {
    if (string == null) {
      return null;
    }
    int firstSpace = findFirstSpace(string);
    String beforeSpace = beforeSpace(string, firstSpace);
    return new NumberUnit<Integer>(new Integer(beforeSpace),
        parseUnit(string, firstSpace));
  }

  /**
   * Converts a float followed by a unit name into an object.
   *
   * @param string a string of the form: <code>float " " unit</code> or null
   * @return the corresponding object or null
   * @exception NumberFormatException if the conversion failed
   */
  public static NumberUnit<Float> toFloatUnit(String string) {
    if (string == null) {
      return null;
    }
    int firstSpace = findFirstSpace(string);
    String beforeSpace = beforeSpace(string, firstSpace);
    return new NumberUnit<Float>(new Float(beforeSpace),
                               parseUnit(string, firstSpace));
  }

  private static String beforeSpace(String string, int firstSpace) {
    return string.substring(0, firstSpace);
  }

  private static String parseUnit(String string, int firstSpace) {
    return string.substring(firstSpace+1).trim();
  }

  private static int findFirstSpace(String string) {
    int firstSpace = string.indexOf(" ");
    if (firstSpace == -1 || firstSpace==string.length()) {
      throw new NumberFormatException("missing unit in '" + string + "'");
    }
    return firstSpace;
  }

  /**
   * Creates a {@link com.google.api.gbase.client.GoogleBaseAttribute} of type
   * {@link GoogleBaseAttributeType#DATE_TIME_RANGE}
   * and initializes it using the current state of the object.
   *
   * @param name attribute name
   * @param range attribute value
   * @return a new {@link com.google.api.gbase.client.GoogleBaseAttribute}
   */
  public static GoogleBaseAttribute createAttribute(String name,
                                                    DateTimeRange range) {
    return new GoogleBaseAttribute(name, 
                                   GoogleBaseAttributeType.DATE_TIME_RANGE, 
                                   range.toString());
  }

  /**
   * Extracts a {@link DateTimeRange} from the value of a
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * @param attribute
   * @exception NumberFormatException if the conversion failed, either
   *   because on of the date/time was invalid or if the expected start
   *   and end tags were missing
   */
  public static DateTimeRange
      extractDateTimeRange(GoogleBaseAttribute attribute) {
    String range = attribute.getValueAsString();
    int space = range.indexOf(' ');
    if (space == -1) {
      /* Not really a range, but since date and dateTime are subtypes
       * of DateTimeRange, try and convert them into a DateTimeRange.
       */
      return new DateTimeRange(toDateOrDateTime(range));
    } else {
      String startStr = range.substring(0, space);
      String endStr = range.substring(space+1);
      return new DateTimeRange(toDateOrDateTime(startStr),
                               toDateOrDateTime(endStr));
    }
  }

  /**
   * Extracts a {@link Shipping} object from the value of a
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * @param attribute
   * @exception NumberFormatException if the conversion failed, usually
   *   because either the country or the price tag was missing
   */
  public static Shipping extractShipping(GoogleBaseAttribute attribute) {
    String country =
        attribute.getSubElementValue(SHIPPING_COUNTRY_ELEMENT_NAME);
    String price =
        attribute.getSubElementValue(SHIPPING_PRICE_ELEMENT_NAME);
    String service =
        attribute.getSubElementValue(SHIPPING_SERVICE_ELEMENT_NAME);
    if (country == null) {
      throw new NumberFormatException(
          "missing 'country' element in shipping attribute: " + attribute);
    }
    if (price == null) {
      throw new NumberFormatException(
          "missing 'price' element in shipping attribute: " + attribute);
    }
    NumberUnit<Float> priceUnit;
    try {
      priceUnit = toFloatUnit(price);
    } catch (NumberFormatException e) {
      priceUnit = new NumberUnit<Float>(Float.parseFloat(price), null);
    }
    return new Shipping(country, service, priceUnit.getValue(),
        priceUnit.getUnit());
  }

  /**
   * Creates a {@link GoogleBaseAttribute} of type
   * {@link GoogleBaseAttributeType#SHIPPING} and initializes it using the
   * current state of the object.
   * @param name attribute name
   * @param shipping attribute value
   * @return a new {@link com.google.api.gbase.client.GoogleBaseAttribute}
   */
  public static GoogleBaseAttribute createAttribute(String name,
                                                    Shipping shipping) {
    GoogleBaseAttribute attribute =
        new GoogleBaseAttribute(name, GoogleBaseAttributeType.SHIPPING);

    attribute.setSubElement(SHIPPING_COUNTRY_ELEMENT_NAME,
                            shipping.getCountry());
    attribute.setSubElement(SHIPPING_SERVICE_ELEMENT_NAME,
        shipping.getService());

    String priceWithUnit = Float.toString(shipping.getPrice());
    if (shipping.getCurrency() != null) {
      priceWithUnit += " " + shipping.getCurrency();
    }
    attribute.setSubElement(SHIPPING_PRICE_ELEMENT_NAME, priceWithUnit);
    return attribute;
  }

  /**
   * Extracts a {@link Location} object from the value of a
   * {@link GoogleBaseAttribute}.
   *
   * @param attribute
   * @return NumberFormatException if the conversion failed, usually
   *   because the latitude or longitude information are invalid numbers
   */
  public static Location extractLocation(GoogleBaseAttribute attribute) {
    String address = attribute.getValueAsString();
    Location retval = new Location(address);

    String latString = attribute.getSubElementValue(LATITUDE_ELEMENT_NAME);
    String longString = attribute.getSubElementValue(LONGITUDE_ELEMENT_NAME);
    if (latString != null && longString != null) {
      retval.setLatitude(Float.parseFloat(latString));
      retval.setLongitude(Float.parseFloat(longString));
    }
    return retval;
  }

  public static GoogleBaseAttribute createAttribute(String name,
      Location location) {
    GoogleBaseAttribute attribute =
        new GoogleBaseAttribute(name, GoogleBaseAttributeType.LOCATION);
    attribute.setValue(location.getAddress());
    if (location.hasCoordinates()) {
      attribute.setSubElement(LATITUDE_ELEMENT_NAME,
          Float.toString(location.getLatitude()));
      attribute.setSubElement(LONGITUDE_ELEMENT_NAME,
          Float.toString(location.getLongitude()));
    }
    return attribute;
  }

  /**
   * Extracts a {@link Number} from a {@link GoogleBaseAttribute}.
   *
   * @param attr an attribute of type NUMBER, FLOAT or INT, may be null
   * @return a Number, which might be an Integer or a Float depending
   *   on the type of the attribute, or null if {@code attr} was null
   */
  public static Number extractNumber(GoogleBaseAttribute attr) {
    if (attr == null) {
      return null;
    }

    /* If the attribute has been declared as being an int,
     * treat it as such, otherwise create  a Float.
     */
    if (GoogleBaseAttributeType.FLOAT.equals(attr.getAttributeId().getType())) {
      return toFloat(attr.getValueAsString());
    } else {
      return toInteger(attr.getValueAsString());
    }
  }

  /**
   * Extracts a <code>NumberUnit<Number></code> from a
   * {@link GoogleBaseAttribute}.
   *
   * @param attr an attribute of type NUMBER_UNIT, FLOAT_UNIT or INT_UNIT,
   *   may be null
   * @return a NumberUnit object whose number might be an Integer
   *   or a Float, depending on the type of the attribute, or null
   *   if {@code attr} was null
   */
  public static NumberUnit<? extends Number> extractNumberUnit(
      GoogleBaseAttribute attr) {
    if (attr == null) {
      return null;
    }

    if (GoogleBaseAttributeType.INT_UNIT.equals(attr.getAttributeId().getType())) {
      return toIntUnit(attr.getValueAsString());
    }
    return toFloatUnit(attr.getValueAsString());
  }
}
