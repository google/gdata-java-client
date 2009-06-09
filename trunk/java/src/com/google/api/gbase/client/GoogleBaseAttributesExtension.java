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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Namespace;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps track of attributes in the g: namespace.
 *
 * This class extends the Google data API with some knowledge about
 * the g: (Google Base attributes) namespace.
 *
 * The attributes are stored in a list as {@link GoogleBaseAttribute}s
 * Accessing GoogleBaseAttribute directly is possible, but usually not
 * recommended.
 *
 * Many methods are available in this class that will perform
 * the necessary type conversion from GoogleBaseAttribute to
 * String, Integer, Float, and {@link DateTime}.
 *
 * Shortcuts exist for a few well-known attributes.
 *
 * You usually get such an object using
 * {@link GoogleBaseEntry#getGoogleBaseAttributes()}.
 *
 * This class can be registered in an
 * {@link com.google.gdata.data.ExtensionProfile} using
 * {@link #DESCRIPTION} and then accessed from an
 * {@link com.google.gdata.data.ExtensionPoint} using
 * {@link com.google.gdata.data.ExtensionPoint#getExtension(Class)}.
 *
 */
public class GoogleBaseAttributesExtension implements Extension {

  /**
   * A description for this extension, to pass to an
   * {@link com.google.gdata.data.ExtensionProfile}.
   */
  public static final ExtensionDescription DESCRIPTION;

  /**
   * Attribute {@code <g:label>}.
   *
   * @see #getLabels()
   * @see #addLabel(String)
   */
  public static final String LABEL_ATTRIBUTE = "label";

  /**
   * Attribute {@code <g:item_type>}.
   *
   * @see #getItemType()
   * @see #setItemType(String)
   */
  public static final String ITEM_TYPE_ATTRIBUTE = "item type";

  /**
   * Attribute {@code <g:expiration_date>}.
   *
   * @see #getExpirationDate()
   * @see #setExpirationDate(DateTime)
   */
  public static final String EXPIRATION_DATE_ATTRIBUTE = "expiration date";

  /**
   * Attribute {@code <g:image_link>}.
   *
   * @see #getImageLink()
   * @see #getImageLinks()
   * @see #addImageLink(String)
   */
  public static final String IMAGE_LINK_ATTRIBUTE = "image link";

  /**
   * Attribute {@code <g:payment_accepted>}.
   *
   * @see #getPaymentMethods()
   * @see #addPaymentMethod(String)
   */
  public static final String PAYMENT_METHOD_ATTRIBUTE = "payment accepted";

  /**
   * Attribute {@code <g:price>}.
   *
   * @see #getPrice()
   * @see #setPrice(NumberUnit)
   * @see #setPrice(float, String)
   */
  public static final String PRICE_ATTRIBUTE = "price";

  /**
   * Attribute {@code <g:location>}.
   *
   * @see #getLocation()
   * @see #setLocation(String)
   */
  public static final String LOCATION_ATTRIBUTE = "location";

  /**
   * Attribute {@code <g:price_type>}.
   *
   * @see #getPriceType()
   * @see #setPriceType(String)
   */
  public static final String PRICE_TYPE_ATTRIBUTE = "price type";

  /**
   * Attribute {@code <g:quantity>}.
   *
   * @see #getQuantity()
   * @see #setQuantity(int)
   */
  public static final String QUANTITY_ATTRIBUTE = "quantity";

  /**
   * Attribute {@code <g:price_units>}.
   *
   * @see #getPriceUnits()
   * @see #setPriceUnits(String)
   */
  public static final String PRICE_UNITS_ATTRIBUTE = "price units";
  
  /**
   * Attribute {@code <g:shipping>}.
   *
   * @see #getShipping()
   * @see #addShipping(Shipping)
   */
  public static final String SHIPPING_ATTRIBUTE = "shipping";

  /**
   * Attribute {@code <g:tax>}.
   *
   * @see #getTax()
   * @see #addTax(Tax)
   */
  public static final String TAX_ATTRIBUTE = "tax";
  
  /**
   * Attribute {@code <g:tax_percent>}.
   *
   * @see #getTax()
   * @see #addTax(Tax)
   * @deprecated use {@link #TAX_ATTRIBUTE} instead
   */
  @Deprecated 
  public static final String TAX_PERCENT_ATTRIBUTE = "tax percent";

  /**
   * Attribute {@code <g:tax_region>}.
   *
   * @see #getTax()
   * @see #addTax(Tax)
   * @deprecated use {@link #TAX_ATTRIBUTE} instead
   */
  @Deprecated
  public static final String TAX_REGION_ATTRIBUTE = "tax region";

  /**
   * Attribute {@code <g:delivery_radius>}.
   *
   * @see #getDeliveryRadius()
   * @see #setDeliveryRadius(NumberUnit)
   * @see #setDeliveryRadius(float, String)
   */
  public static final String DELIVERY_RADIUS_ATTRIBUTE = "delivery radius";

  /**
   * Attribute {@code <g:pickup>}.
   *
   * @see #getPickup()
   * @see #setPickup(boolean)
   */
  public static final String PICKUP_ATTRIBUTE = "pickup";

  /**
   * Attribute {@code <g:delivery_notes>}.
   *
   * @see #getDeliveryNotes()
   * @see #setDeliveryNotes(String)
   */
  public static final String DELIVERY_NOTES_ATTRIBUTE = "delivery notes";

  /**
   * Attribute {@code <g:payment_notes>}.
   *
   * @see #getPaymentNotes()
   * @see #setPaymentNotes(String)
   */
  public static final String PAYMENT_NOTES_ATTRIBUTE = "payment notes";

  /**
   * Attribute {@code <g:application>}.
   *
   * @see #getApplication()
   * @see #setApplication(String)
   */
  public static final String APPLICATION_ATTRIBUTE = "application";

  /**
   * Attribute {@code <g:customer_id>}.
   *
   * @see #getCustomerId()
   */
  public static final String CUSTOMER_ID = "customer id";

  /** Meta attribute {@code gm:adjusted_name}. */
  static final String GM_ADJUSTED_NAME_ATTRIBUTE = "adjusted_name";
  
  /** Meta attribute {@code gm:adjusted_value}. */
  static final String GM_ADJUSTED_VALUE_ATTRIBUTE = "adjusted_value";

  /** Meta attribute {@code gm:thumbnail}. */
  static final String GM_THUMBNAIL_ATTRIBUTE = "thumbnail";
  
  /**
   * All the attributes available for the current
   * {@link com.google.gdata.data.ExtensionPoint} in this extension
   * namespace.
   *
   * Several {@link com.google.api.gbase.client.GoogleBaseAttribute}
   * might have the same name and even the same value. Order is
   * conserved, but should not be significant.
   */
  private final List<GoogleBaseAttribute> attributes =
      new ArrayList<GoogleBaseAttribute>();
  
  static {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GoogleBaseAttributesExtension.class);
    desc.setNamespace(GoogleBaseNamespaces.G);
    desc.setLocalName("*");
    desc.setRepeatable(false);
    desc.setAggregate(true);
    DESCRIPTION = desc;
  }
  
  /**
   * Gets the labels set for the entry.
   *
   * @return a collection of strings, which might be
   *   empty but not null
   */
  public Collection<? extends String> getLabels() {
    return getTextAttributeValues(LABEL_ATTRIBUTE);
  }

  /**
   * Adds a label to the entry.
   *
   * @param value
   */
  public void addLabel(String value) {
    addTextAttribute(LABEL_ATTRIBUTE, value);
  }


  /** Gets the item type. */
  public String getItemType() {
    return getTextAttribute(ITEM_TYPE_ATTRIBUTE);
  }

  /** Sets the item type. */
  public void setItemType(String value) {
    removeAttributes(ITEM_TYPE_ATTRIBUTE, GoogleBaseAttributeType.TEXT);
    addTextAttribute(ITEM_TYPE_ATTRIBUTE, value);
  }

  /** Gets the date and time at which the entry expires. */
  public DateTime getExpirationDate() {
    return getDateTimeAttribute(EXPIRATION_DATE_ATTRIBUTE);
  }

  /** Sets the date at which the entry expires. */
  public void setExpirationDate(DateTime date) {
    removeAttributes(EXPIRATION_DATE_ATTRIBUTE,
                     GoogleBaseAttributeType.DATE_TIME);
    addDateTimeAttribute(EXPIRATION_DATE_ATTRIBUTE, date);
  }

  /** Gets the first URL to an image representing this item. */
  public String getImageLink() {
    return getUrlAttribute(IMAGE_LINK_ATTRIBUTE);
  }

  /** Gets all URLs to images representing this item. */
  public List<? extends String> getImageLinks() {
    return getAttributeValuesAsString(IMAGE_LINK_ATTRIBUTE,
        GoogleBaseAttributeType.URL);
  }

  /** Adds an image URL. */
  public void addImageLink(String link) {
    addUrlAttribute(IMAGE_LINK_ATTRIBUTE, link);
  }

  /**
   * Gets a collection of accepted payment methods (Cash, WireTransfer, ...).
   *
   * @return a collection of strings, which might be empty but not null.
   */
  public Collection<? extends String> getPaymentMethods() {
    return getTextAttributeValues(PAYMENT_METHOD_ATTRIBUTE);
  }

  /**
   * Adds an accepted payment method.
   */
  public void addPaymentMethod(String method) {
    addTextAttribute(PAYMENT_METHOD_ATTRIBUTE, method);
  }

  /**
   * Gets the price of the item.
   *
   * @return a price (value and currency) or null
   */
  public NumberUnit<Float> getPrice() {
    return getFloatUnitAttribute(PRICE_ATTRIBUTE);
  }

  /**
   * Sets the price of the item.
   *
   * @param value
   */
  public void setPrice(NumberUnit<Float> value) {
    removeAttributes(PRICE_ATTRIBUTE);
    addFloatUnitAttribute(PRICE_ATTRIBUTE, value);
  }

  /**
   * Sets the price of the item.
   *
   * @param value
   * @param currency
   */
  public void setPrice(float value, String currency) {
    setPrice(new NumberUnit<Float>(value, currency));
  }

  /**
   * Gets the location.
   */
  public String getLocation() {
    return getLocationAttribute(LOCATION_ATTRIBUTE);
  }

  /**
   * Sets the location.
   */
  public void setLocation(String value) {
    removeAttributes(LOCATION_ATTRIBUTE);
    addLocationAttribute(LOCATION_ATTRIBUTE, value);
  }

  /** Sets price type. */
  public void setPriceType(String type) {
    removeAttributes(PRICE_TYPE_ATTRIBUTE);
    addTextAttribute(PRICE_TYPE_ATTRIBUTE, type);
  }

  /** Gets price type. */
  public String getPriceType() {
    return getTextAttribute(PRICE_TYPE_ATTRIBUTE);
  }

  /** Sets quantity. */
  public void setQuantity(int value) {
    removeAttributes(QUANTITY_ATTRIBUTE);
    addIntAttribute(QUANTITY_ATTRIBUTE, value);
  }

  /** Gets quantity, or null if not set. */
  public Integer getQuantity() {
    return getIntAttribute(QUANTITY_ATTRIBUTE);
  }

  /** Sets price units. */
  public void setPriceUnits(String value) {
    removeAttributes(PRICE_UNITS_ATTRIBUTE);
    addTextAttribute(PRICE_UNITS_ATTRIBUTE, value);
  }

  /** Gets price units. */
  public String getPriceUnits() {
    return getTextAttribute(PRICE_UNITS_ATTRIBUTE);
  }
  
  /** Adds shipping attribute. */
  public void addShipping(Shipping shipping) {
    addShippingAttribute(SHIPPING_ATTRIBUTE, shipping);
  }

  /** Gets shipping attributes. */
  public Collection<? extends Shipping> getShipping() {
    return getShippingAttributes(SHIPPING_ATTRIBUTE);
  }

  /** Adds tax attribute. */
  public void addTax(Tax tax) {
    addTaxAttribute(TAX_ATTRIBUTE, tax);
  }
  
  /** Gets tax attributes. */
  public Collection<? extends Tax> getTax() {
    return getTaxAttributes(TAX_ATTRIBUTE);
  }
  
  /** 
   * Sets tax percent attribute.
   * 
   * @deprecated use {@link #addTax(Tax)} instead
   */
  @Deprecated
  public void setTaxPercent(float taxPercent) {
    removeAttributes(TAX_PERCENT_ATTRIBUTE);
    addFloatAttribute(TAX_PERCENT_ATTRIBUTE, taxPercent);
  }

  /** 
   * Gets tax percent attribute, or null. 
   * 
   * @deprecated use {@link #getTax()} instead
   */
  @Deprecated
  public Float getTaxPercent() {
    return getFloatAttribute(TAX_PERCENT_ATTRIBUTE);
  }

  /** 
   * Sets tax region attribute. 
   * 
   * @deprecated use {@link #addTax(Tax)} instead 
   */
  @Deprecated
  public void setTaxRegion(String region) {
    removeAttributes(TAX_REGION_ATTRIBUTE);
    addTextAttribute(TAX_REGION_ATTRIBUTE, region);
  }

  /** 
   * Gets tax region attribute. 
   * 
   * @deprecated use {@link #getTax()} instead
   */
  @Deprecated
  public String getTaxRegion() {
    return getTextAttribute(TAX_REGION_ATTRIBUTE);
  }

  /** Sets delivery radius. */
  public void setDeliveryRadius(float value, String unit) {
    setDeliveryRadius(new NumberUnit<Float>(value, unit));
  }

  /** Sets delivery radius. */
  public void setDeliveryRadius(NumberUnit<Float> value) {
    removeAttributes(DELIVERY_RADIUS_ATTRIBUTE);
    addFloatUnitAttribute(DELIVERY_RADIUS_ATTRIBUTE, value);
  }

  /** Gets delivery radius. */
  public NumberUnit<Float> getDeliveryRadius() {
    return getFloatUnitAttribute(DELIVERY_RADIUS_ATTRIBUTE);
  }

  /** Sets pickup attribute. */
  public void setPickup(boolean pickup) {
    removeAttributes(PICKUP_ATTRIBUTE, GoogleBaseAttributeType.BOOLEAN);
    addBooleanAttribute(PICKUP_ATTRIBUTE, pickup);
  }

  /** Gets pickup attribute, or null. */
  public Boolean getPickup() {
    return getBooleanAttribute(PICKUP_ATTRIBUTE);
  }

  /** Sets delivery notes attribute. */
  public void setDeliveryNotes(String notes) {
    removeAttributes(DELIVERY_NOTES_ATTRIBUTE, GoogleBaseAttributeType.TEXT);
    addTextAttribute(DELIVERY_NOTES_ATTRIBUTE, notes);
  }

  /** Gets delivery notes attribute */
  public String getDeliveryNotes() {
    return getTextAttribute(DELIVERY_NOTES_ATTRIBUTE);
  }

  /** Sets payment notes attribute. */
  public void setPaymentNotes(String notes) {
    removeAttributes(PAYMENT_NOTES_ATTRIBUTE, GoogleBaseAttributeType.TEXT);
    addTextAttribute(PAYMENT_NOTES_ATTRIBUTE, notes);
  }

  /** Gets payment notes attribute */
  public String getPaymentNotes() {
    return getTextAttribute(PAYMENT_NOTES_ATTRIBUTE);
  }

  public Integer getCustomerId() {
    return getIntAttribute(CUSTOMER_ID);
  }

  /**
   * Gets a list of all the attributes available in the extension
   * namespace at the current {@link com.google.gdata.data.ExtensionPoint}.
   *
   * Attributes might be repeated.
   *
   * @return a list of {@link com.google.api.gbase.client.GoogleBaseAttribute},
   *   which might be empty but not null
   */
  public List<? extends GoogleBaseAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Gets the first attribute with a certain name.
   *
   * In most cases, there might be more than one attribute
   * with the same name. This method will ignore extra
   * attributes. Use {@link #getAttributes(String)} to make sure
   * you get all of them.
   *
   * @param name attribute name
   * @return one {@link com.google.api.gbase.client.GoogleBaseAttribute}
   *   or null if no attribute was found with this name
   */
  public GoogleBaseAttribute getAttribute(String name) {
    return getAttribute(name, null);
  }

  /**
   * Gets the first attribute with a certain name and type.
   *
   * In most cases, there might be more than one attribute
   * with the same name and type. This method will ignore extra
   * attributes. Use {@link #getAttributes(String)} to make sure
   * you get all of them.
   *
   * @param name attribute name
   * @param type attribute type (null to ignore the type)
   * @return one {@link com.google.api.gbase.client.GoogleBaseAttribute}
   *   or null if no attribute was found with this name
   */
  public GoogleBaseAttribute getAttribute(String name, GoogleBaseAttributeType type) {
    for (GoogleBaseAttribute attr : attributes) {
      if (hasNameAndType(attr, name, type)) {
        return attr;
      }
    }
    return null;
  }

  private boolean hasNameAndType(GoogleBaseAttribute attr, String name,
                                 GoogleBaseAttributeType type) {
    GoogleBaseAttributeType subtype = attr.getAttributeId().getType();
    return name.equals(attr.getAttributeId().getName()) &&
        (type == null || subtype != null && type.isSupertypeOf(subtype));
  }

  /**
   * Gets all the attributes with a certain name and type.
   *
   * @param name attribute name
   * @param type attribute type, null to ignore the type
   * @return a list of {@link com.google.api.gbase.client.GoogleBaseAttribute},
   *   which might be empty but not null
   */
  public List<? extends GoogleBaseAttribute> getAttributes(String name,
                                                           GoogleBaseAttributeType type) {
    List<GoogleBaseAttribute> retval = new ArrayList<GoogleBaseAttribute>();
     for (GoogleBaseAttribute attr : attributes) {
        if (hasNameAndType(attr, name, type)) {
          retval.add(attr);
        }
    }
    return retval;
  }

  /**
   * Gets all the attributes with a certain name and type.
   *
   * @param name attribute name
   * @return a list of {@link com.google.api.gbase.client.GoogleBaseAttribute},
   *   which might be empty but not null
   */
  public List<? extends GoogleBaseAttribute> getAttributes(String name) {
    return getAttributes(name, null);
  }

  /**
   * Adds an attribute to the list.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param attribute
   * @return the attribute passed as parameter
   */
  public GoogleBaseAttribute addAttribute(GoogleBaseAttribute attribute) {
    attributes.add(attribute);
    return attribute;
  }

  /**
   * Removes an attribute from the list.
   *
   * @param value
   */
  public void removeAttribute(GoogleBaseAttribute value) {
    attributes.remove(value);
  }

  /**
   * Removes all attributes with a certain name from the list.
   *
   * @param name name of the attributes that should be removed
   */
  public void removeAttributes(String name) {
    removeAttributes(name, null);
  }

  /**
   * Removes all attributes with a certain name and
   * type from the list.
   *
   * @param name name of the attributes that should be removed
   * @param type attribute type, null to ignore the type
   */
  public void removeAttributes(String name, GoogleBaseAttributeType type) {
    Iterator<GoogleBaseAttribute> iter = attributes.iterator();
    while (iter.hasNext()) {
      GoogleBaseAttribute attribute = iter.next();
      if (hasNameAndType(attribute, name, type)) {
        iter.remove();
      }
    }
  }

  /**
   * Removes all attributes from the list.
   */
  public void clearAttributes() {
    attributes.clear();
  }

  /**
   * Gets the first value of a specific attribute, as a string.
   *
   * If it makes sense for the attribute to appear more
   * than once, you might consider calling
   * {@link #getTextAttributeValues(String)} instead.
   *
   * This method checks the type of the attribute
   * that is being queried. Use
   * {@link #getAttributeAsString(String, GoogleBaseAttributeType)}
   * if you would like to get the value of non-string attributes.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   */
  public String getTextAttribute(String name) {
    return getAttributeAsString(name, GoogleBaseAttributeType.TEXT);
  }

  /**
   * Gets the first value of a specific reference attribute.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#REFERENCE}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no reference attribute
   *   with this name was found on the list
   */
  public String getReferenceAttribute(String name) {
    return getAttributeAsString(name, GoogleBaseAttributeType.REFERENCE);
  }
  
  /**
   * Gets the string representation of the first
   * attribute with matching name and type.
   *
   * This method does not check the type of the attributes
   * that are being queried. It just returns what it finds.
   *
   * @param name attribute name
   * @param type attribute type, null to ignore the type
   * @return the string representation of the first matching
   *   attribute or null if none was found
   */
  private String getAttributeAsString(String name,
                                      GoogleBaseAttributeType type) {
    GoogleBaseAttribute attribute = getAttribute(name, type);
    if (attribute == null) {
      return null;
    }
    return attribute.getValueAsString();
  }

  /**
   * Gets all the values of a specific attribute, as a list
   * of strings.
   *
   * This method checks the type of the attribute
   * that are being queried. Use
   * {@link #getAttributeAsString(String, GoogleBaseAttributeType)}
   * if you would like to get the value non-string attributes.
   *
   * @param attributeName
   * @return a list of strings, which might be empty but
   *   not null
   */
  public List<String> getTextAttributeValues(String attributeName) {
    return getAttributeValuesAsString(attributeName,
                                      GoogleBaseAttributeType.TEXT);
  }

  /**
   * Gets the string representation of all attributes with matching
   * names and types.
   *
   * @param name attribute name
   * @param type attribute type, null to ignore the type
   * @return a list of strings, which might be empty but not null
   */
  private List<String> getAttributeValuesAsString(
      String name, GoogleBaseAttributeType type) {
    List<? extends GoogleBaseAttribute> labels =
        getAttributes(name, type);
    List<String> retval = new ArrayList<String>(labels.size());
    for (GoogleBaseAttribute label : labels) {
      retval.add(label.getValueAsString());
    }
    return retval;
  }

  /**
   * Gets the first value of a specific attribute, as a Float.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#FLOAT}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public Float getFloatAttribute(String name) {
    return ConversionUtil.toFloat(
        getAttributeAsString(name, GoogleBaseAttributeType.FLOAT));
  }

  /**
   * Gets the first value of a specific attribute, as an Integer.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#INT}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public Integer getIntAttribute(String name) {
    return ConversionUtil.toInteger(
        getAttributeAsString(name,GoogleBaseAttributeType.INT));
  }

  /**
   * Gets the first value of a specific attribute, as a Number.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#NUMBER}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public Number getNumberAttribute(String name) {
    GoogleBaseAttribute attr =
        getAttribute(name, GoogleBaseAttributeType.NUMBER);
    return ConversionUtil.extractNumber(attr);
  }

  /**
   * Gets the first value of a specific attribute, as an Boolean.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#BOOLEAN}.
   *
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   */
  public Boolean getBooleanAttribute(String name) {
    return ConversionUtil.toBoolean(
        getAttributeAsString(name, GoogleBaseAttributeType.BOOLEAN));
  }

  /**
   * Gets the first value of a specific attribute, as a date and a time.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#DATE_TIME}.
   *
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public DateTime getDateTimeAttribute(String name) {
    return ConversionUtil.toDateOrDateTime(
        getAttributeAsString(name, GoogleBaseAttributeType.DATE_TIME));
  }

  /**
   * Gets the first value of a specific attribute, as a date.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#DATE_TIME} and
   * {@link GoogleBaseAttributeType#DATE}.
   *
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public DateTime getDateAttribute(String name) {
    return ConversionUtil.toDateOrDateTime(
        getAttributeAsString(name, GoogleBaseAttributeType.DATE));
  }

  /**
   * Gets the first value of a specific attribute, as a date/dateTime
   * range.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#DATE_TIME},
   * {@link GoogleBaseAttributeType#DATE} and
   * {@link GoogleBaseAttributeType#DATE_TIME_RANGE}.
   *
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public DateTimeRange getDateRangeAttribute(String name) {
    GoogleBaseAttribute attribute =
        getAttribute(name, GoogleBaseAttributeType.DATE_TIME_RANGE);
    return ConversionUtil.extractDateTimeRange(attribute);
  }

  /**
   * Gets the first value of a specific attribute, as an url.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#URL}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   */
  public String getUrlAttribute(String name) {
    return getAttributeAsString(name, GoogleBaseAttributeType.URL);
  }

  /**
   * Gets the first value of a specific attribute, as an integer,
   * followed by a unit name.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#INT_UNIT}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public NumberUnit<Integer> getIntUnitAttribute(String name) {
    return ConversionUtil.toIntUnit(
        getAttributeAsString(name, GoogleBaseAttributeType.INT_UNIT));
  }

  /**
   * Gets the first value of a specific attribute, as an float,
   * followed by a unit name.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#FLOAT_UNIT}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public NumberUnit<Float> getFloatUnitAttribute(String name) {
    return ConversionUtil.toFloatUnit(
        getAttributeAsString(name, GoogleBaseAttributeType.FLOAT_UNIT));
  }

  /**
   * Gets the first value of a specific attribute, as a Number,
   * followed by a unit name.
   *
   * This method only takes into account attributes of type
   * {@link GoogleBaseAttributeType#NUMBER_UNIT},
   * {@link GoogleBaseAttributeType#INT_UNIT} or
   * {@link GoogleBaseAttributeType#FLOAT_UNIT}.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public NumberUnit<? extends Number> getNumberUnitAttribute(String name) {
    return ConversionUtil.extractNumberUnit(
        getAttribute(name, GoogleBaseAttributeType.NUMBER_UNIT));
  }


  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#TEXT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addTextAttribute(String name, String value) {
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.TEXT,
                                                value));
  }
  
  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#REFERENCE}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addReferenceAttribute(String name, String value) {
    return addAttribute(new GoogleBaseAttribute(name, 
        GoogleBaseAttributeType.REFERENCE, value));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#INT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addIntAttribute(String name, int value) {
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.INT,
                                                Integer.toString(value)));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#FLOAT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addFloatAttribute(String name, float value) {
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.FLOAT,
                                                Float.toString(value)));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#NUMBER}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addNumberAttribute(String name, Number value) {
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.NUMBER,
                                                value.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#INT_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value
   * @param unit
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addIntUnitAttribute(String name, int value, String unit) {
    return addIntUnitAttribute(name, new NumberUnit<Integer>(value, unit));
  }

  /**
   * Adds an attribute of type
   *  {@link com.google.api.gbase.client.GoogleBaseAttributeType#INT_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addIntUnitAttribute(String name,
                                                 NumberUnit<Integer> value) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.INT_UNIT, value.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#FLOAT_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value
   * @param unit
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addFloatUnitAttribute(String name,
                                                   float value,
                                                   String unit) {
    return addFloatUnitAttribute(name, new NumberUnit<Float>(value, unit));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#FLOAT_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addFloatUnitAttribute(String name,
                                                   NumberUnit<Float> value) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.FLOAT_UNIT, value.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#NUMBER_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value
   * @param unit
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addNumberUnitAttribute(String name,
                                                    Number value,
                                                    String unit) {
    return addNumberUnitAttribute(name, new NumberUnit<Number>(value, unit));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#NUMBER_UNIT}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addNumberUnitAttribute(String name,
                                                    NumberUnit<Number> value) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.NUMBER_UNIT, value.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#DATE}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param date attribute value
   * @return the attribute object that has been created and added to the item
   * @exception IllegalArgumentException if the attribute value is
   *  not only a date, but a date and a time (see
   *  {@link com.google.gdata.data.DateTime#isDateOnly()})
   */
  public GoogleBaseAttribute addDateAttribute(String name, DateTime date) {
    if (!date.isDateOnly()) {
      throw new IllegalArgumentException("DateTime should be only a date, " +
          "NOT a date and a time. Call addDateTimeAttribute() instead.");
    }
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.DATE,
                                                date.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#DATE_TIME}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param dateTime attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addDateTimeAttribute(String name,
                                                  DateTime dateTime) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.DATE_TIME, dateTime.toString()));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#URL}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addUrlAttribute(String name, String value) {
    return addAttribute(new GoogleBaseAttribute(name,
                                                GoogleBaseAttributeType.URL,
                                                value));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#BOOLEAN}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param value attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addBooleanAttribute(String name, boolean value) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.BOOLEAN, Boolean.toString(value)));
  }

  /** Adds group attribute. */
  public void addGroupAttribute(String groupName, Group group) {
    addAttribute(ConversionUtil.createAttribute(groupName, group));
  }
  
  /**
   * Gets the first value of a specific attribute, as a
   * {@link com.google.api.gbase.client.Group}.
   * 
   * This method does not check the type of the attribute
   * that's being queried, it just gets the value and try
   * and convert it.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   */
  public Group getGroupAttribute(String name) {
    GoogleBaseAttribute value = getAttribute(name);
    if (value == null) {
      return null;
    }
    return ConversionUtil.extractGroup(value);
  }

  /**
   * Gets all the values of a specific attribute, as a collection of
   * {@link com.google.api.gbase.client.Group}s.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the values and try
   * and convert them.
   *
   * @param groupName the group name
   * @return a list of Group, which might be empty but not null
   */
  public Collection<? extends Group> getGroupAttributes(String groupName) {
    List<Group> retval = new ArrayList<Group>();
    for (GoogleBaseAttribute attr: attributes) {
      if (hasNameAndType(attr, groupName, GoogleBaseAttributeType.GROUP)) {
        retval.add(ConversionUtil.extractGroup(attr));
      }
    }
    return retval;
  }
  
  /**
   * Gets the first value of a specific attribute, as a
   * {@link com.google.api.gbase.client.Shipping}.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the value and try
   * and convert it.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted.
   */
  public Shipping getShippingAttribute(String name) {
    GoogleBaseAttribute value = getAttribute(name);
    if (value == null) {
      return null;
    }
    return ConversionUtil.extractShipping(value);
  }

  /**
   * Gets all the values of a specific attribute, as a list of
   * {@link com.google.api.gbase.client.Shipping}s.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the values and try
   * and convert them.
   *
   * @param name attribute name
   * @return a list of Shipping, which might be empty but not null
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public List<? extends Shipping> getShippingAttributes(String name) {
    List<Shipping> retval = new ArrayList<Shipping>();
    for (GoogleBaseAttribute attr: attributes) {
      if (hasNameAndType(attr, name, GoogleBaseAttributeType.SHIPPING)) {
        retval.add(ConversionUtil.extractShipping(attr));
      }
    }
    return retval;
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#SHIPPING}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param shipping attribute value
   */
  public void addShippingAttribute(String name, Shipping shipping) {
    addAttribute(ConversionUtil.createAttribute(name, shipping));
  }
  
  /**
   * Gets the first value of a specific attribute, as a
   * {@link com.google.api.gbase.client.Tax}.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the value and try
   * and convert it.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted.
   */
  public Tax getTaxAttribute(String name) {
    GoogleBaseAttribute value = getAttribute(name);
    if (value == null) {
      return null;
    }
    return ConversionUtil.extractTax(value);
  }
  
  /**
   * Gets all the values of a specific attribute, as a list of
   * {@link com.google.api.gbase.client.Tax}s.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the values and try
   * and convert them.
   *
   * @param name attribute name
   * @return a list of Tax, which might be empty but not null
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public List<? extends Tax> getTaxAttributes(String name) {
    List<Tax> retval = new ArrayList<Tax>();
    for (GoogleBaseAttribute attr: attributes) {
      if (hasNameAndType(attr, name, GoogleBaseAttributeType.TAX)) {
        retval.add(ConversionUtil.extractTax(attr));
      }
    }
    return retval;
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#TAX}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param tax attribute value
   */
  public void addTaxAttribute(String name, Tax tax) {
    addAttribute(ConversionUtil.createAttribute(name, tax));
  }
  
  /**
   * Gets the first value of a specific location attribute, as an
   * address.
   *
   * If you would like to get latitude and longitude and not
   * just the address, use {@link #getLocationAttributeAsObject(String)}
   * instead.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted.
   */
  public String getLocationAttribute(String name) {
    return getAttributeAsString(name, GoogleBaseAttributeType.LOCATION);
  }

  /**
   * Gets the first value of a specific location attribute, as a
   * location object.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted.
   */
  public Location getLocationAttributeAsObject(String name) {
    GoogleBaseAttribute attribute = getAttribute(name);
    if (attribute == null) {
      return null;
    }
    return ConversionUtil.extractLocation(attribute);
  }

  /**
   * Gets all the values of a specific location attribute, as a list of
   * strings.
   *
   * @param name attribute name
   * @return a list of locations, which might be empty but not null
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public List<? extends String> getLocationAttributes(String name) {
    return getAttributeValuesAsString(name, GoogleBaseAttributeType.LOCATION);
  }


  /**
   * Gets all the values of a specific attribute, as a list of
   * {@link Location} objects.
   *
   * @param name attribute name
   * @return a list of locations, which might be empty but not null
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public List<Location> getLocationAttributesAsObjects(String name) {
    List<? extends GoogleBaseAttribute> attributes =
        getAttributes(name, GoogleBaseAttributeType.LOCATION);
    List<Location> retval = new ArrayList<Location>(attributes.size());
    for (GoogleBaseAttribute attribute : attributes) {
      retval.add(ConversionUtil.extractLocation(attribute));
    }
    return retval;
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#LOCATION}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param location attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addLocationAttribute(String name, String location) {
    return addAttribute(new GoogleBaseAttribute(name,
        GoogleBaseAttributeType.LOCATION, location));
  }

  /**
   * Adds an attribute of type
   * {@link com.google.api.gbase.client.GoogleBaseAttributeType#LOCATION}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param location attribute value
   * @return the attribute object that has been created and added to the item
   */
  public GoogleBaseAttribute addLocationAttribute(String name,
      Location location) {
    return addAttribute(ConversionUtil.createAttribute(name, location));
  }

  /**
   * Gets the first value of a specific attribute, as a
   * {@link com.google.api.gbase.client.DateTimeRange}.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the value and try
   * and convert it.
   *
   * @param name attribute name
   * @return value of the attribute or null if no attribute
   *   with this name was found on the list
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public DateTimeRange getDateTimeRangeAttribute(String name) {
    GoogleBaseAttribute value = getAttribute(name);
    if (value == null) {
      return null;
    }
    return ConversionUtil.extractDateTimeRange(value);
  }

  /**
   * Gets all the values of a specific attribute, as a list of
   * {@link com.google.api.gbase.client.DateTimeRange}.
   *
   * This method does not check the type of the attribute
   * that's being queried, it just gets the values and try
   * and convert them.
   *
   * @param name attribute name
   * @return a list of DateTimeRange, which might be empty but not null
   * @exception NumberFormatException if some value was
   *   found that could not be converted
   */
  public List<? extends DateTimeRange> getDateTimeRangeAttributes(String name) {
    List<DateTimeRange> retval = new ArrayList<DateTimeRange>();
    for (GoogleBaseAttribute attr: attributes) {
      if (hasNameAndType(attr, name,
                         GoogleBaseAttributeType.DATE_TIME_RANGE)) {
        retval.add(ConversionUtil.extractDateTimeRange(attr));
      }
    }
    return retval;
  }

  /**
   * Adds an attribute of type
   * {@link GoogleBaseAttributeType#DATE_TIME_RANGE}.
   *
   * This method will never remove an attribute, even if it has
   * the same name as the new attribute. If you would like to set
   * an attribute that can only appear once, call
   * {@link #removeAttributes(String, GoogleBaseAttributeType)} first.
   *
   * @param name attribute name
   * @param dateTimeRange attribute value
   * @return the attribute object that has been created and added to the item
   * @exception IllegalArgumentException if the DateTimeRange is an empty
   *   range, in which case {@link #addDateTimeAttribute(String,
   *   com.google.gdata.data.DateTime)} should be used instead.
   */
  public GoogleBaseAttribute addDateTimeRangeAttribute(String name,
      DateTimeRange dateTimeRange) {
    if (dateTimeRange.isDateTimeOnly()) {
      // The server would reject such a range
      throw new IllegalArgumentException("Empty DateTimeRange. Add " +
          "it as a single DateTime using addDateTimeAttribute() instead.");
    }
    return addAttribute(ConversionUtil.createAttribute(name, dateTimeRange));
  }

  /**
   * Implements
   * {@link Extension#generate(XmlWriter, ExtensionProfile)}.
   *
   * This method generates XML code for the attribute in this extension.
   * It is meant to be called by the Google data library.
   *
   * @param xmlWriter
   * @param extensionProfile
   */
  public void generate(XmlWriter xmlWriter, ExtensionProfile extensionProfile)
      throws IOException {
    for (GoogleBaseAttribute attribute : attributes) {
      generateAttribute(attribute, xmlWriter);
    }
  }
  
  private void generateAttribute(GoogleBaseAttribute attribute, XmlWriter xmlWriter) 
      throws IOException {
    String elementName = convertToElementName(attribute.getAttributeId().getName());
    xmlWriter.startElement(GoogleBaseNamespaces.G,
                           elementName,
                           getXmlAttributes(attribute),
                           null);
    
    generateValue(attribute, xmlWriter);
    generateSubElements(attribute, xmlWriter);
    generateAdjustments(attribute, xmlWriter);
    generateSubAttributes(attribute, xmlWriter);
    xmlWriter.endElement();
  }

  /**
   * Generates XML code for the value of the {@code attribute}.
   *
   * @param attribute
   * @param xmlWriter
   * @throws java.io.IOException
   */
  private void generateValue(GoogleBaseAttribute attribute, XmlWriter xmlWriter)
      throws IOException {
    if (attribute.hasValue()) {
      xmlWriter.characters(attribute.getValueAsString());
    }
  }
  
  /**
   * Generates XML code for all sub-elements of the {@code attribute}.
   * 
   * @param attribute
   * @param xmlWriter
   * @throws IOException
   */
  private void generateSubElements(GoogleBaseAttribute attribute, 
      XmlWriter xmlWriter) throws IOException {
    if (attribute.hasSubElements()) {
      for (String name : attribute.getSubElementNames()) {
        for (String element : attribute.getSubElementValues(name)) {
          writeXmlNameValue(xmlWriter, GoogleBaseNamespaces.G, name, element);
        }
      }
    }
  }
  
  /**
   * Generates XML code for all sub-attributes of the {@code attribute}.
   * 
   * @param attribute
   * @param xmlWriter
   * @throws IOException
   */
  private void generateSubAttributes(GoogleBaseAttribute attribute, 
      XmlWriter xmlWriter) throws IOException {
    if (attribute.hasSubAttributes()) {
      for (GoogleBaseAttribute element : attribute.getSubAttributes()) {
        generateAttribute(element, xmlWriter);
      }
    }
  }

  /**
   * Generates XML code for the adjustments of the {@code attribute} 
   * ({@code adjusted_value}, {@code adjusted_name}). 
   *  
   * @param attribute
   * @param xmlWriter
   * @throws IOException
   */
  private void generateAdjustments(GoogleBaseAttribute attribute, 
      XmlWriter xmlWriter) throws IOException {
    if (attribute.hasAdjustments()) {
      Adjustments adjustments = attribute.getAdjustments();
      if (adjustments.getName() != null) {
        writeXmlNameValue(xmlWriter, GoogleBaseNamespaces.GM, 
            GM_ADJUSTED_NAME_ATTRIBUTE, adjustments.getName());
      }
      if (adjustments.getValue() != null) {
        writeXmlNameValue(xmlWriter, GoogleBaseNamespaces.GM, 
            GM_ADJUSTED_VALUE_ATTRIBUTE, adjustments.getValue());
      }
    }
  }

  /**
   * Writes a attribute to the {@code xmlWriter} with the specified 
   * {@code namespace} and {@code name} and with the text content defined by
   * the {@code value}.
   *   
   * @param xmlWriter
   * @param namespace
   * @param name
   * @param value
   * @throws IOException
   */
  private void writeXmlNameValue(XmlWriter xmlWriter, Namespace namespace, 
      String name, String value) throws IOException {
    xmlWriter.startElement(namespace,
            convertToElementName(name),
            null,
            null);
    xmlWriter.characters(value);
    xmlWriter.endElement();
  }

  /**
   * Converts an attribute name (with space) to an XML
   * element name (with underscores).
   *
   * @param attributeName
   * @return corresponding element name
   */
  private String convertToElementName(String attributeName) {
    return attributeName.replace(' ', '_');
  }

  /**
   * Converts an XML element local name (with underscores) to
   * an attribute name (with spaces).
   *
   * @param localName element name without namespace prefix
   * @return corresponding attribute name
   */
  private String convertToAttributeName(String localName) {
    return localName.replace('_', ' ');
  }

  /**
   * Returns the XML attributes that should be set to the
   * element corresponding to the given
   * {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * This method is meant to be implemented in subclasses that need
   * so set attributes. By default, it defines no attributes and
   * simply returns null.
   *
   * @param attribute
   * @return a collection of XML attributes or null
   */
  private Collection<XmlWriter.Attribute> getXmlAttributes(
      GoogleBaseAttribute attribute) {
    GoogleBaseAttributeType type = attribute.getAttributeId().getType();
    if (type == null) {
      return null;
    }
    List<XmlWriter.Attribute> attributes = new ArrayList<XmlWriter.Attribute>();
    attributes.add(new XmlWriter.Attribute("type", type.toString()));
    if (attribute.isPrivate()) {
      attributes.add(new XmlWriter.Attribute("access", "private"));
    }
    return attributes;
  }

  /**
   * Given an XML element local name and its attributes, creates
   * an {@link com.google.api.gbase.client.GoogleBaseAttribute}.
   *
   * This method reads the 'type' attribute and set it in the
   * {@link GoogleBaseAttribute}.
   *
   * @param attributeName attribute name, with spaces
   * @param xmlAttributes XML attributes for the current element
   * @return the GoogleBaseAttribute for the XML tag
   */
  private GoogleBaseAttribute createExtensionAttribute(
      String attributeName, Attributes xmlAttributes) {
    String type = xmlAttributes.getValue("type");
    String access = xmlAttributes.getValue("access");
    boolean privateAccess = "private".equals(access);
    return new GoogleBaseAttribute(attributeName,
                                   GoogleBaseAttributeType.getInstance(type),
                                   privateAccess);
  }

  /**
   * Implements
   * {@link Extension#getHandler(ExtensionProfile, String, String, Attributes)}.
   *
   * This method returns a handler object that can parse an attribute in
   * the current namespace and add the corresponding {@link GoogleBaseAttribute}
   * into the list.
   *
   * It is meant to be called by the Google data library.
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extensionProfile,
                                             String uri, String localName,
                                             Attributes attributes)
      throws ParseException, IOException {
    return new Handler(localName, attributes);
  }

  /**
   * Gets the application attribute.
   *
   * This attribute should contain the name of the last application that
   * modified the entry. This is informative only and should not be relied
   * on.
   *
   * @return application attribute
   */
  public String getApplication() {
    return getTextAttribute(APPLICATION_ATTRIBUTE);
  }


  /**
   * Sets the application attribute.
   *
   * This attribute should be set every time an entry is inserted
   * or updated. It is set automatically by
   * {@link GoogleBaseService#update(java.net.URL,
   *   com.google.gdata.data.BaseEntry)}
   * {@link GoogleBaseService#insert(java.net.URL,
   *   com.google.gdata.data.BaseEntry)} and
   * {@link GoogleBaseService#batch(java.net.URL,
   *   com.google.gdata.data.BaseFeed)}.
   *
   * @param name
   */
  public void setApplication(String name) {
    removeAttributes(APPLICATION_ATTRIBUTE, GoogleBaseAttributeType.TEXT);
    addTextAttribute(APPLICATION_ATTRIBUTE, name);
  }

  /**
   * Private handler for one XML tag that creates and adds
   * an {@link com.google.api.gbase.client.GoogleBaseAttribute} into the list.
   *
   */
  private class Handler extends XmlParser.ElementHandler {
    /*
    * This handler supposes that all tags in the namespace look like this:
    *
    * <x:tag_name [type='...']>...text...</x:tag_name>
    *
    * or like this:
    *
    * <x:tag_name [type='...']>
    *   <x:subtag1>...text...</x:subtag1>
    *   <x:subtag2>...text...</x:subtag2>
    *    ...
    * </x:tag_name> (with all sub tags appearing at most once)
    *
    * Anything else will be ignored.
    */
    private final GoogleBaseAttribute attribute;

    Handler(String localName, Attributes xmlAttributes) {
      String attributeName = convertToAttributeName(localName);
      this.attribute = createExtensionAttribute(attributeName, xmlAttributes);
      attributes.add(attribute);
    }

    @Override
    public void processEndElement() throws ParseException {
      if (super.value != null) {
        attribute.setValue(super.value);
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(final String uri, 
                                                    final String localName, 
                                                    Attributes attrs) {
      GoogleBaseAttributeType type = this.attribute.getType();
      if (type != null && GoogleBaseAttributeType.GROUP.isSupertypeOf(type)) {
        return new GroupSubAttrHandler(localName, attrs);
      }
      return new XmlParser.ElementHandler() {
        private int width = -1;
        private int height = -1;
       
        @Override
        public void processEndElement() {
          if (GoogleBaseNamespaces.GM_URI.equals(uri)) {
            if (GM_ADJUSTED_VALUE_ATTRIBUTE.equals(localName)) {
              attribute.getAdjustments().setValue(super.value);
            } else if (GM_ADJUSTED_NAME_ATTRIBUTE.equals(localName)) {
              attribute.getAdjustments().setName(super.value);              
            } else if (GM_THUMBNAIL_ATTRIBUTE.equals(localName)) {
              Thumbnail thumbnail = new Thumbnail();
              thumbnail.setUrl(super.value.trim());
              if ((width > 0) && (height > 0)) {
                thumbnail.setSize(width, height);
              }
              attribute.getThumbnails().add(thumbnail);
            }
            // if the uri is gm but the name is not recognized, we ignore it
          } else {
            // only non-gm uris are considered sub-elements
            attribute.appendSubElement(localName, super.value);
          }
        }
        
        @Override
        public void processAttribute(String namespace, String localName,
            String value) throws ParseException {
          if ("width".equals(localName)) {
            width = parseInteger(value);
          } else if ("height".equals(localName)) {
            height = parseInteger(value);
          }
        }
        
        private int parseInteger(String value) throws ParseException {
          try {
            return Integer.parseInt(value);
          } catch (NumberFormatException nfe) {
            throw new ParseException(
                "Invalid size value '" + value + "'", nfe);
          }
        }
      };        
    }
  }
  
  /**
   * Private handler for one XML tag of type "group" and creates and adds
   * an {@link com.google.api.gbase.client.GoogleBaseAttribute} into the list.
   */
  private class GroupSubAttrHandler extends XmlParser.ElementHandler {
    
    private final GoogleBaseAttribute attribute;
    
    GroupSubAttrHandler(String localName, Attributes xmlAttributes) {
      String attributeName = convertToAttributeName(localName);
      this.attribute = createExtensionAttribute(attributeName, xmlAttributes);
      attributes.get(attributes.size() - 1).addSubAttribute(attribute);
    }
    
    @Override
    public void processEndElement() {
      if (super.value != null) {
        attribute.setValue(super.value);
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(final String uri, 
                                                    final String localName, 
                                                    Attributes attrs) {
      return new XmlParser.ElementHandler() {
       
        @Override
        public void processEndElement() {
          if (GoogleBaseNamespaces.GM_URI.equals(uri)) {
            if (GM_ADJUSTED_VALUE_ATTRIBUTE.equals(localName)) {
              attribute.getAdjustments().setValue(super.value);
            } else if (GM_ADJUSTED_NAME_ATTRIBUTE.equals(localName)) {
              attribute.getAdjustments().setName(super.value);              
            }
            // if the uri is gm but the name is not recognized, we ignore it
          } else {
            // only non-gm uris are considered sub-elements
            attribute.appendSubElement(localName, super.value);
          }
        }
      };        
    }
  }  
}
