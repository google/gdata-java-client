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
 * Describes a structured postal address.
 *
 * 
 */
public class StructuredPostalAddress extends Element {

  /** The mail class. */
  public static final class MailClass {

    /** Both structured postal address. */
    public static final String BOTH = Namespaces.gPrefix + "both";

    /** Letters structured postal address. */
    public static final String LETTERS = Namespaces.gPrefix + "letters";

    /** Neither structured postal address. */
    public static final String NEITHER = Namespaces.gPrefix + "neither";

    /** Parcels structured postal address. */
    public static final String PARCELS = Namespaces.gPrefix + "parcels";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      BOTH,
      LETTERS,
      NEITHER,
      PARCELS};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private MailClass() {}
  }

  /** The postal address type. */
  public static final class Rel {

    /** Home structured postal address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other structured postal address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work structured postal address. */
    public static final String WORK = Namespaces.gPrefix + "work";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      HOME,
      OTHER,
      WORK};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /** The context for the address use. */
  public static final class Usage {

    /** General structured postal address. */
    public static final String GENERAL = Namespaces.gPrefix + "general";

    /** Local structured postal address. */
    public static final String LOCAL = Namespaces.gPrefix + "local";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      GENERAL,
      LOCAL};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Usage() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      StructuredPostalAddress> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "structuredPostalAddress"), Void.class, StructuredPostalAddress.class);

  /**
   * Simple string value used to name this address.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * The mail class.
   */
  public static final AttributeKey<String> MAIL_CLASS = AttributeKey.of(new
      QName(null, "mailClass"), String.class);

  /**
   * Whether this is the primary postal address.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * The postal address type.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * The context for the address use.
   */
  public static final AttributeKey<String> USAGE = AttributeKey.of(new
      QName(null, "usage"), String.class);

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
    builder.addAttribute(LABEL);
    builder.addAttribute(MAIL_CLASS);
    builder.addAttribute(PRIMARY);
    builder.addAttribute(REL);
    builder.addAttribute(USAGE);
    builder.addElement(Agent.KEY);
    builder.addElement(City.KEY);
    builder.addElement(Country.KEY);
    builder.addElement(FormattedAddress.KEY);
    builder.addElement(HouseName.KEY);
    builder.addElement(Neighborhood.KEY);
    builder.addElement(PoBox.KEY);
    builder.addElement(PostCode.KEY);
    builder.addElement(Region.KEY);
    builder.addElement(Street.KEY);
    builder.addElement(Subregion.KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public StructuredPostalAddress() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected StructuredPostalAddress(ElementKey<?,
      ? extends StructuredPostalAddress> key) {
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
  protected StructuredPostalAddress(ElementKey<?,
      ? extends StructuredPostalAddress> key, Element source) {
    super(key, source);
  }

  @Override
  public StructuredPostalAddress lock() {
    return (StructuredPostalAddress) super.lock();
  }

  /**
   * Returns the Used in work addresses.  Also for 'in care of' or 'c/o'.
   *
   * @return Used in work addresses.  Also for 'in care of' or 'c/o'
   */
  public Agent getAgent() {
    return super.getElement(Agent.KEY);
  }

  /**
   * Sets the Used in work addresses.  Also for 'in care of' or 'c/o'.
   *
   * @param agent Used in work addresses.  Also for 'in care of' or 'c/o' or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setAgent(Agent agent) {
    super.setElement(Agent.KEY, agent);
    return this;
  }

  /**
   * Returns whether it has the Used in work addresses.  Also for 'in care of'
   * or 'c/o'.
   *
   * @return whether it has the Used in work addresses.  Also for 'in care of'
   *     or 'c/o'
   */
  public boolean hasAgent() {
    return super.hasElement(Agent.KEY);
  }

  /**
   * Returns the Can be city, village, town, borough, etc.
   *
   * @return Can be city, village, town, borough, etc
   */
  public City getCity() {
    return super.getElement(City.KEY);
  }

  /**
   * Sets the Can be city, village, town, borough, etc.
   *
   * @param city Can be city, village, town, borough, etc or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setCity(City city) {
    super.setElement(City.KEY, city);
    return this;
  }

  /**
   * Returns whether it has the Can be city, village, town, borough, etc.
   *
   * @return whether it has the Can be city, village, town, borough, etc
   */
  public boolean hasCity() {
    return super.hasElement(City.KEY);
  }

  /**
   * Returns the The country name.
   *
   * @return The country name
   */
  public Country getCountry() {
    return super.getElement(Country.KEY);
  }

  /**
   * Sets the The country name.
   *
   * @param country The country name or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setCountry(Country country) {
    super.setElement(Country.KEY, country);
    return this;
  }

  /**
   * Returns whether it has the The country name.
   *
   * @return whether it has the The country name
   */
  public boolean hasCountry() {
    return super.hasElement(Country.KEY);
  }

  /**
   * Returns the The full, unstructured address.
   *
   * @return The full, unstructured address
   */
  public FormattedAddress getFormattedAddress() {
    return super.getElement(FormattedAddress.KEY);
  }

  /**
   * Sets the The full, unstructured address.
   *
   * @param formattedAddress The full, unstructured address or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setFormattedAddress(FormattedAddress
      formattedAddress) {
    super.setElement(FormattedAddress.KEY, formattedAddress);
    return this;
  }

  /**
   * Returns whether it has the The full, unstructured address.
   *
   * @return whether it has the The full, unstructured address
   */
  public boolean hasFormattedAddress() {
    return super.hasElement(FormattedAddress.KEY);
  }

  /**
   * Returns the Used in places where houses or buildings have names.
   *
   * @return Used in places where houses or buildings have names
   */
  public HouseName getHousename() {
    return super.getElement(HouseName.KEY);
  }

  /**
   * Sets the Used in places where houses or buildings have names.
   *
   * @param housename Used in places where houses or buildings have names or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setHousename(HouseName housename) {
    super.setElement(HouseName.KEY, housename);
    return this;
  }

  /**
   * Returns whether it has the Used in places where houses or buildings have
   * names.
   *
   * @return whether it has the Used in places where houses or buildings have
   *     names
   */
  public boolean hasHousename() {
    return super.hasElement(HouseName.KEY);
  }

  /**
   * Returns the Simple string value used to name this address.
   *
   * @return Simple string value used to name this address
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the Simple string value used to name this address.
   *
   * @param label Simple string value used to name this address or {@code null}
   *     to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the Simple string value used to name this address.
   *
   * @return whether it has the Simple string value used to name this address
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the The mail class.
   *
   * @return The mail class
   */
  public String getMailClass() {
    return super.getAttributeValue(MAIL_CLASS);
  }

  /**
   * Sets the The mail class.
   *
   * @param mailClass The mail class or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setMailClass(String mailClass) {
    super.setAttributeValue(MAIL_CLASS, mailClass);
    return this;
  }

  /**
   * Returns whether it has the The mail class.
   *
   * @return whether it has the The mail class
   */
  public boolean hasMailClass() {
    return super.hasAttribute(MAIL_CLASS);
  }

  /**
   * Returns the Neighborhood. Used to disambiguate a street.
   *
   * @return Neighborhood. Used to disambiguate a street
   */
  public Neighborhood getNeighborhood() {
    return super.getElement(Neighborhood.KEY);
  }

  /**
   * Sets the Neighborhood. Used to disambiguate a street.
   *
   * @param neighborhood Neighborhood. Used to disambiguate a street or {@code
   *     null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setNeighborhood(Neighborhood neighborhood) {
    super.setElement(Neighborhood.KEY, neighborhood);
    return this;
  }

  /**
   * Returns whether it has the Neighborhood. Used to disambiguate a street.
   *
   * @return whether it has the Neighborhood. Used to disambiguate a street
   */
  public boolean hasNeighborhood() {
    return super.hasElement(Neighborhood.KEY);
  }

  /**
   * Returns the P.O. box.
   *
   * @return P.O. box
   */
  public PoBox getPobox() {
    return super.getElement(PoBox.KEY);
  }

  /**
   * Sets the P.O. box.
   *
   * @param pobox P.O. box or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setPobox(PoBox pobox) {
    super.setElement(PoBox.KEY, pobox);
    return this;
  }

  /**
   * Returns whether it has the P.O. box.
   *
   * @return whether it has the P.O. box
   */
  public boolean hasPobox() {
    return super.hasElement(PoBox.KEY);
  }

  /**
   * Returns the Postal code.
   *
   * @return Postal code
   */
  public PostCode getPostcode() {
    return super.getElement(PostCode.KEY);
  }

  /**
   * Sets the Postal code.
   *
   * @param postcode Postal code or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setPostcode(PostCode postcode) {
    super.setElement(PostCode.KEY, postcode);
    return this;
  }

  /**
   * Returns whether it has the Postal code.
   *
   * @return whether it has the Postal code
   */
  public boolean hasPostcode() {
    return super.hasElement(PostCode.KEY);
  }

  /**
   * Returns the whether this is the primary postal address.
   *
   * @return whether this is the primary postal address
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary postal address.
   *
   * @param primary whether this is the primary postal address or {@code null}
   *     to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
    return this;
  }

  /**
   * Returns whether it has the whether this is the primary postal address.
   *
   * @return whether it has the whether this is the primary postal address
   */
  public boolean hasPrimary() {
    return super.hasAttribute(PRIMARY);
  }

  /**
   * Returns the Region is state, province, county (in Ireland), Land (in
   * Germany), departement (in France), etc.
   *
   * @return Region is state, province, county (in Ireland), Land (in Germany),
   *     departement (in France), etc
   */
  public Region getRegion() {
    return super.getElement(Region.KEY);
  }

  /**
   * Sets the Region is state, province, county (in Ireland), Land (in Germany),
   * departement (in France), etc.
   *
   * @param region Region is state, province, county (in Ireland), Land (in
   *     Germany), departement (in France), etc or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setRegion(Region region) {
    super.setElement(Region.KEY, region);
    return this;
  }

  /**
   * Returns whether it has the Region is state, province, county (in Ireland),
   * Land (in Germany), departement (in France), etc.
   *
   * @return whether it has the Region is state, province, county (in Ireland),
   *     Land (in Germany), departement (in France), etc
   */
  public boolean hasRegion() {
    return super.hasElement(Region.KEY);
  }

  /**
   * Returns the the postal address type.
   *
   * @return the postal address type
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the the postal address type.
   *
   * @param rel the postal address type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the the postal address type.
   *
   * @return whether it has the the postal address type
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the Can be street, avenue, road, etc.  This element also includes
   * the house number and room/apartment/flat/floor number.
   *
   * @return Can be street, avenue, road, etc.  This element also includes the
   *     house number and room/apartment/flat/floor number
   */
  public Street getStreet() {
    return super.getElement(Street.KEY);
  }

  /**
   * Sets the Can be street, avenue, road, etc.  This element also includes the
   * house number and room/apartment/flat/floor number.
   *
   * @param street Can be street, avenue, road, etc.  This element also includes
   *     the house number and room/apartment/flat/floor number or {@code null}
   *     to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setStreet(Street street) {
    super.setElement(Street.KEY, street);
    return this;
  }

  /**
   * Returns whether it has the Can be street, avenue, road, etc.  This element
   * also includes the house number and room/apartment/flat/floor number.
   *
   * @return whether it has the Can be street, avenue, road, etc.  This element
   *     also includes the house number and room/apartment/flat/floor number
   */
  public boolean hasStreet() {
    return super.hasElement(Street.KEY);
  }

  /**
   * Returns the Subregion is county (US), province (in Italy), etc.
   *
   * @return Subregion is county (US), province (in Italy), etc
   */
  public Subregion getSubregion() {
    return super.getElement(Subregion.KEY);
  }

  /**
   * Sets the Subregion is county (US), province (in Italy), etc.
   *
   * @param subregion Subregion is county (US), province (in Italy), etc or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setSubregion(Subregion subregion) {
    super.setElement(Subregion.KEY, subregion);
    return this;
  }

  /**
   * Returns whether it has the Subregion is county (US), province (in Italy),
   * etc.
   *
   * @return whether it has the Subregion is county (US), province (in Italy),
   *     etc
   */
  public boolean hasSubregion() {
    return super.hasElement(Subregion.KEY);
  }

  /**
   * Returns the The context for the address use.
   *
   * @return The context for the address use
   */
  public String getUsage() {
    return super.getAttributeValue(USAGE);
  }

  /**
   * Sets the The context for the address use.
   *
   * @param usage The context for the address use or {@code null} to reset
   * @return this to enable chaining setters
   */
  public StructuredPostalAddress setUsage(String usage) {
    super.setAttributeValue(USAGE, usage);
    return this;
  }

  /**
   * Returns whether it has the The context for the address use.
   *
   * @return whether it has the The context for the address use
   */
  public boolean hasUsage() {
    return super.hasAttribute(USAGE);
  }

}


