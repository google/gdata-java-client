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

import com.google.gdata.util.common.base.StringUtil;
import com.google.common.collect.Multimap;
import com.google.gdata.data.DateTime;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Some methods for converting strings found in XML files
 * into other types.
 */
class ConversionUtil {

  private enum TaxField {
    Country("country"),
    Region("region"),
    Rate("rate"),
    TaxShip("tax_ship");
    
    private final String elemName;
    private final String attrName;
    
    TaxField(String elemName) {
      this.elemName = elemName;
      this.attrName = elemName.replace('_', ' ');
    }
    
    public String getElemName() {
      return elemName;
    }
    
    public String getAttrName() {
      return attrName;
    }
  }
  
  private enum ShippingField {
    Country("country"),
    Region("region"),
    Price("price"),
    Service("service");
    
    private final String elemName;
    private final String attrName;
    
    ShippingField(String elemName) {
      this.elemName = elemName;
      this.attrName = elemName.replace('_', ' ');
    }
    
    public String getElemName() {
      return elemName;
    }
    
    public String getAttrName() {
      return attrName;
    }
  }
  
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
   * Extracts a {@link Group} object from the value of a
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * @param attribute
   */
  public static Group extractGroup(GoogleBaseAttribute attribute) {
    return new Group(attribute.getSubAttributes());
  }
  
  /**
   * Creates a {@link GoogleBaseAttribute} of type
   * {@link GoogleBaseAttributeType#GROUP} and initializes it using the
   * current state of the object. Sub-attributes of group with type
   * GROUP or GROUP's derived types are ignored.
   * 
   * @param name attribute name
   * @param group attribute value
   * @return a new {@link com.google.api.gbase.client.GoogleBaseAttribute}
   */
  public static GoogleBaseAttribute createAttribute(String name, Group group) {
    GoogleBaseAttribute attribute = new GoogleBaseAttribute(name, GoogleBaseAttributeType.GROUP);
    Multimap<String, GoogleBaseAttribute> subAttributes = group.getAllSubAttributes();
    for (GoogleBaseAttribute attr : subAttributes.values()) {
      GoogleBaseAttributeType type = attr.getType();
      if (!GoogleBaseAttributeType.GROUP.equals(type)
          && !GoogleBaseAttributeType.GROUP.equals(type.getSupertype())
          && (attr.hasValue() || attr.hasSubElements())) {
        attribute.addSubAttribute(attr);
      }
    }
    return attribute;
  }
  
  /**
   * Extracts a {@link Shipping} object from the value of a
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * @param attribute
   * @exception NumberFormatException if the conversion failed 
   *    because the price is missing or not a number.
   */
  public static Shipping extractShipping(GoogleBaseAttribute attribute) {
    if (attribute.hasSubAttributes()) {
      return extractShippingFromAttributes(attribute);
    } else {
      return extractShippingFromElements(attribute);
    }
  }
  
  private static Shipping extractShippingFromAttributes(GoogleBaseAttribute attribute) {
    NumberUnit<Float> priceUnit = null;
    String country = null;
    Collection<String> regions = new ArrayList<String>();;
    String service = null;
    for (GoogleBaseAttribute attr : attribute.getSubAttributes()) {
      String name = attr.getName();
      if (ShippingField.Country.getAttrName().equals(name)) {
        country = attr.getValueAsString();
      } else if (ShippingField.Region.getAttrName().equals(name)) {
        regions.add(attr.getValueAsString());
      } else if (ShippingField.Price.getAttrName().equals(name)) {
        String price = attr.getValueAsString();
        try {
          priceUnit = toFloatUnit(price);
        } catch (NumberFormatException e) {
          priceUnit = new NumberUnit<Float>(Float.parseFloat(price), null);
        }
      } else if (ShippingField.Service.getAttrName().equals(name)) {
        service = attr.getValueAsString();
      } else {
        throw new IllegalArgumentException("Sub-attribute " + name 
            + " is not supported in Shipping.");
      } 
    }
    if (priceUnit == null) {
      throw new NumberFormatException(
          "missing 'price' element in shipping attribute: " + attribute);
    }
    return new Shipping(country, regions, service, priceUnit.getValue(),
        priceUnit.getUnit());
  }
  
  private static Shipping extractShippingFromElements(GoogleBaseAttribute attribute) {
    String country = attribute.getSubElementValue(ShippingField.Country.getElemName());
    Collection<String> regions = attribute.getSubElementValues(ShippingField.Region.getElemName());
    String price = attribute.getSubElementValue(ShippingField.Price.getElemName());
    String service = attribute.getSubElementValue(ShippingField.Service.getElemName());
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
    return new Shipping(country, regions, service, priceUnit.getValue(),
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

    if(shipping.getCountry() != null) {
      GoogleBaseAttribute countryAttr = new GoogleBaseAttribute(
          ShippingField.Country.getAttrName(), null, shipping.getCountry());
      attribute.addSubAttribute(countryAttr);
    }
    for (String region : shipping.getRegions()) {
      GoogleBaseAttribute regionAttr = new GoogleBaseAttribute(
          ShippingField.Region.getAttrName(), null, region);
      attribute.addSubAttribute(regionAttr);
    }
    if (shipping.getService() != null) {
      GoogleBaseAttribute serviceAttr = new GoogleBaseAttribute(
          ShippingField.Service.getAttrName(), null, shipping.getService());
      attribute.addSubAttribute(serviceAttr);
    }

    String priceWithUnit = Float.toString(shipping.getPrice());
    if (shipping.getCurrency() != null) {
      priceWithUnit += " " + shipping.getCurrency();
    }
    GoogleBaseAttribute priceAttr = new GoogleBaseAttribute(ShippingField.Price.getAttrName(),
        null, priceWithUnit);
    attribute.addSubAttribute(priceAttr);
    return attribute;
  }
  
  /**
   * Extracts a {@link Tax} object from the value of a
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * @param attribute
   * @exception NumberFormatException if the conversion failed, usually
   *   because the rate is missing or not a number.
   */
  public static Tax extractTax(GoogleBaseAttribute attribute) {
    if (attribute.hasSubAttributes()) {
      return extractTaxFromAttributes(attribute);
    } else {
      return extractTaxFromElements(attribute);
    }
  }
  
  private static Tax extractTaxFromAttributes(GoogleBaseAttribute attribute) {
    String rateString = null;
    String country = null;
    Collection<String> regions = new ArrayList<String>();
    Boolean taxShip = null;
    for (GoogleBaseAttribute attr : attribute.getSubAttributes()) {
      String name = attr.getName();
      if (TaxField.Country.getAttrName().equals(name)) {
        country = attr.getValueAsString();
      } else if (TaxField.Region.getAttrName().equals(name)) {
        regions.add(attr.getValueAsString());
      } else if (TaxField.Rate.getAttrName().equals(name)) {
        rateString = attr.getValueAsString();
      } else if (TaxField.TaxShip.getAttrName().equals(name)) {
        String taxShipString = attr.getValueAsString();
        if (taxShipString != null) {
          taxShip = Boolean.valueOf(taxShipString);
        }
      } else {
        throw new IllegalArgumentException("Sub-attribute " + name + " is not supported in Tax.");
      }
    }
    if (StringUtil.isEmpty(rateString)) {
      throw new NumberFormatException(
          "missing 'rate' element in tax attribute: " + attribute);
    }
    float rate = Float.valueOf(rateString.trim()).floatValue();
    return new Tax(country, regions, rate, taxShip);
  }
  
  private static Tax extractTaxFromElements(GoogleBaseAttribute attribute) {
    String country = attribute.getSubElementValue(TaxField.Country.getElemName());
    Collection<String> regions = attribute.getSubElementValues(TaxField.Region.getElemName());
    String rateString = attribute.getSubElementValue(TaxField.Rate.getElemName());
    if (rateString == null) {
      throw new NumberFormatException(
          "missing 'rate' element in tax attribute: " + attribute);
    }
    float rate = Float.valueOf(rateString.trim()).floatValue();
    String taxShipString = attribute.getSubElementValue(TaxField.TaxShip.getElemName());
    Boolean taxShip = null;
    if (taxShipString != null) {
      taxShip = Boolean.valueOf(taxShipString);
    }
    return new Tax(country, regions, rate, taxShip);
  }
  
  /**
   * Creates a {@link GoogleBaseAttribute} of type
   * {@link GoogleBaseAttributeType#TAX} and initializes it using the
   * current state of the object.
   * @param name attribute name
   * @param tax attribute value
   * @return a new {@link com.google.api.gbase.client.GoogleBaseAttribute}
   */
  public static GoogleBaseAttribute createAttribute(String name, Tax tax) {
    GoogleBaseAttribute attribute = new GoogleBaseAttribute(name, GoogleBaseAttributeType.TAX);
    GoogleBaseAttribute attr = null;
    if (tax.getCountry() != null) {
      attr = new GoogleBaseAttribute(TaxField.Country.getAttrName(), null, tax.getCountry());
      attribute.addSubAttribute(attr);
    }
    for (String region : tax.getRegions()) {
      attr = new GoogleBaseAttribute(TaxField.Region.getAttrName(), null, region);
      attribute.addSubAttribute(attr);
    }
    attr = new GoogleBaseAttribute(TaxField.Rate.getAttrName(), null, 
        Float.toString(tax.getRate()));
    attribute.addSubAttribute(attr);
    if (tax.getTaxShip() != null) {
      attr = new GoogleBaseAttribute(TaxField.TaxShip.getAttrName(), null, 
          tax.getTaxShip() ? "true":"false");
      attribute.addSubAttribute(attr);
    }
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
