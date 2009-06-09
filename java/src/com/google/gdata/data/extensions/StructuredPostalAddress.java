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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Describes a structured postal address.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = StructuredPostalAddress.XML_NAME)
public class StructuredPostalAddress extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "structuredPostalAddress";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "mailClass" attribute name */
  private static final String MAILCLASS = "mailClass";

  /** XML "primary" attribute name */
  private static final String PRIMARY = "primary";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  /** XML "usage" attribute name */
  private static final String USAGE = "usage";

  /** Simple string value used to name this address */
  private String label = null;

  /** The mail class */
  private String mailClass = null;

  /** Whether this is the primary postal address */
  private Boolean primary = null;

  /** The postal address type */
  private String rel = null;

  /** The context for the address use */
  private String usage = null;

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

  }

  /** The postal address type. */
  public static final class Rel {

    /** Home structured postal address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other structured postal address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work structured postal address. */
    public static final String WORK = Namespaces.gPrefix + "work";

  }

  /** The context for the address use. */
  public static final class Usage {

    /** General structured postal address. */
    public static final String GENERAL = Namespaces.gPrefix + "general";

    /** Local structured postal address. */
    public static final String LOCAL = Namespaces.gPrefix + "local";

  }

  /**
   * Default mutable constructor.
   */
  public StructuredPostalAddress() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param label Simple string value used to name this address.
   * @param mailClass The mail class.
   * @param primary whether this is the primary postal address.
   * @param rel the postal address type.
   * @param usage The context for the address use.
   */
  public StructuredPostalAddress(String label, String mailClass,
      Boolean primary, String rel, String usage) {
    super();
    setLabel(label);
    setMailClass(mailClass);
    setPrimary(primary);
    setRel(rel);
    setUsage(usage);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(StructuredPostalAddress.class)) {
      return;
    }
    extProfile.declare(StructuredPostalAddress.class, Agent.class);
    extProfile.declare(StructuredPostalAddress.class, City.class);
    extProfile.declare(StructuredPostalAddress.class, Country.class);
    extProfile.declare(StructuredPostalAddress.class, FormattedAddress.class);
    extProfile.declare(StructuredPostalAddress.class, HouseName.class);
    extProfile.declare(StructuredPostalAddress.class, Neighborhood.class);
    extProfile.declare(StructuredPostalAddress.class, PoBox.class);
    extProfile.declare(StructuredPostalAddress.class, PostCode.class);
    extProfile.declare(StructuredPostalAddress.class, Region.class);
    extProfile.declare(StructuredPostalAddress.class, Street.class);
    extProfile.declare(StructuredPostalAddress.class, Subregion.class);
  }

  /**
   * Returns the Used in work addresses.  Also for 'in care of' or 'c/o'.
   *
   * @return Used in work addresses.  Also for 'in care of' or 'c/o'
   */
  public Agent getAgent() {
    return getExtension(Agent.class);
  }

  /**
   * Sets the Used in work addresses.  Also for 'in care of' or 'c/o'.
   *
   * @param agent Used in work addresses.  Also for 'in care of' or 'c/o' or
   *     <code>null</code> to reset
   */
  public void setAgent(Agent agent) {
    if (agent == null) {
      removeExtension(Agent.class);
    } else {
      setExtension(agent);
    }
  }

  /**
   * Returns whether it has the Used in work addresses.  Also for 'in care of'
   * or 'c/o'.
   *
   * @return whether it has the Used in work addresses.  Also for 'in care of'
   *     or 'c/o'
   */
  public boolean hasAgent() {
    return hasExtension(Agent.class);
  }

  /**
   * Returns the Can be city, village, town, borough, etc.
   *
   * @return Can be city, village, town, borough, etc
   */
  public City getCity() {
    return getExtension(City.class);
  }

  /**
   * Sets the Can be city, village, town, borough, etc.
   *
   * @param city Can be city, village, town, borough, etc or <code>null</code>
   *     to reset
   */
  public void setCity(City city) {
    if (city == null) {
      removeExtension(City.class);
    } else {
      setExtension(city);
    }
  }

  /**
   * Returns whether it has the Can be city, village, town, borough, etc.
   *
   * @return whether it has the Can be city, village, town, borough, etc
   */
  public boolean hasCity() {
    return hasExtension(City.class);
  }

  /**
   * Returns the The country name.
   *
   * @return The country name
   */
  public Country getCountry() {
    return getExtension(Country.class);
  }

  /**
   * Sets the The country name.
   *
   * @param country The country name or <code>null</code> to reset
   */
  public void setCountry(Country country) {
    if (country == null) {
      removeExtension(Country.class);
    } else {
      setExtension(country);
    }
  }

  /**
   * Returns whether it has the The country name.
   *
   * @return whether it has the The country name
   */
  public boolean hasCountry() {
    return hasExtension(Country.class);
  }

  /**
   * Returns the The full, unstructured address.
   *
   * @return The full, unstructured address
   */
  public FormattedAddress getFormattedAddress() {
    return getExtension(FormattedAddress.class);
  }

  /**
   * Sets the The full, unstructured address.
   *
   * @param formattedAddress The full, unstructured address or <code>null</code>
   *     to reset
   */
  public void setFormattedAddress(FormattedAddress formattedAddress) {
    if (formattedAddress == null) {
      removeExtension(FormattedAddress.class);
    } else {
      setExtension(formattedAddress);
    }
  }

  /**
   * Returns whether it has the The full, unstructured address.
   *
   * @return whether it has the The full, unstructured address
   */
  public boolean hasFormattedAddress() {
    return hasExtension(FormattedAddress.class);
  }

  /**
   * Returns the Used in places where houses or buildings have names.
   *
   * @return Used in places where houses or buildings have names
   */
  public HouseName getHousename() {
    return getExtension(HouseName.class);
  }

  /**
   * Sets the Used in places where houses or buildings have names.
   *
   * @param housename Used in places where houses or buildings have names or
   *     <code>null</code> to reset
   */
  public void setHousename(HouseName housename) {
    if (housename == null) {
      removeExtension(HouseName.class);
    } else {
      setExtension(housename);
    }
  }

  /**
   * Returns whether it has the Used in places where houses or buildings have
   * names.
   *
   * @return whether it has the Used in places where houses or buildings have
   *     names
   */
  public boolean hasHousename() {
    return hasExtension(HouseName.class);
  }

  /**
   * Returns the Simple string value used to name this address.
   *
   * @return Simple string value used to name this address
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the Simple string value used to name this address.
   *
   * @param label Simple string value used to name this address or
   *     <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the Simple string value used to name this address.
   *
   * @return whether it has the Simple string value used to name this address
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the The mail class.
   *
   * @return The mail class
   */
  public String getMailClass() {
    return mailClass;
  }

  /**
   * Sets the The mail class.
   *
   * @param mailClass The mail class or <code>null</code> to reset
   */
  public void setMailClass(String mailClass) {
    throwExceptionIfImmutable();
    this.mailClass = mailClass;
  }

  /**
   * Returns whether it has the The mail class.
   *
   * @return whether it has the The mail class
   */
  public boolean hasMailClass() {
    return getMailClass() != null;
  }

  /**
   * Returns the Neighborhood. Used to disambiguate a street.
   *
   * @return Neighborhood. Used to disambiguate a street
   */
  public Neighborhood getNeighborhood() {
    return getExtension(Neighborhood.class);
  }

  /**
   * Sets the Neighborhood. Used to disambiguate a street.
   *
   * @param neighborhood Neighborhood. Used to disambiguate a street or
   *     <code>null</code> to reset
   */
  public void setNeighborhood(Neighborhood neighborhood) {
    if (neighborhood == null) {
      removeExtension(Neighborhood.class);
    } else {
      setExtension(neighborhood);
    }
  }

  /**
   * Returns whether it has the Neighborhood. Used to disambiguate a street.
   *
   * @return whether it has the Neighborhood. Used to disambiguate a street
   */
  public boolean hasNeighborhood() {
    return hasExtension(Neighborhood.class);
  }

  /**
   * Returns the P.O. box.
   *
   * @return P.O. box
   */
  public PoBox getPobox() {
    return getExtension(PoBox.class);
  }

  /**
   * Sets the P.O. box.
   *
   * @param pobox P.O. box or <code>null</code> to reset
   */
  public void setPobox(PoBox pobox) {
    if (pobox == null) {
      removeExtension(PoBox.class);
    } else {
      setExtension(pobox);
    }
  }

  /**
   * Returns whether it has the P.O. box.
   *
   * @return whether it has the P.O. box
   */
  public boolean hasPobox() {
    return hasExtension(PoBox.class);
  }

  /**
   * Returns the Postal code.
   *
   * @return Postal code
   */
  public PostCode getPostcode() {
    return getExtension(PostCode.class);
  }

  /**
   * Sets the Postal code.
   *
   * @param postcode Postal code or <code>null</code> to reset
   */
  public void setPostcode(PostCode postcode) {
    if (postcode == null) {
      removeExtension(PostCode.class);
    } else {
      setExtension(postcode);
    }
  }

  /**
   * Returns whether it has the Postal code.
   *
   * @return whether it has the Postal code
   */
  public boolean hasPostcode() {
    return hasExtension(PostCode.class);
  }

  /**
   * Returns the whether this is the primary postal address.
   *
   * @return whether this is the primary postal address
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Sets the whether this is the primary postal address.
   *
   * @param primary whether this is the primary postal address or
   *     <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    throwExceptionIfImmutable();
    this.primary = primary;
  }

  /**
   * Returns whether it has the whether this is the primary postal address.
   *
   * @return whether it has the whether this is the primary postal address
   */
  public boolean hasPrimary() {
    return getPrimary() != null;
  }

  /**
   * Returns the Region is state, province, county (in Ireland), Land (in
   * Germany), departement (in France), etc.
   *
   * @return Region is state, province, county (in Ireland), Land (in Germany),
   *     departement (in France), etc
   */
  public Region getRegion() {
    return getExtension(Region.class);
  }

  /**
   * Sets the Region is state, province, county (in Ireland), Land (in Germany),
   * departement (in France), etc.
   *
   * @param region Region is state, province, county (in Ireland), Land (in
   *     Germany), departement (in France), etc or <code>null</code> to reset
   */
  public void setRegion(Region region) {
    if (region == null) {
      removeExtension(Region.class);
    } else {
      setExtension(region);
    }
  }

  /**
   * Returns whether it has the Region is state, province, county (in Ireland),
   * Land (in Germany), departement (in France), etc.
   *
   * @return whether it has the Region is state, province, county (in Ireland),
   *     Land (in Germany), departement (in France), etc
   */
  public boolean hasRegion() {
    return hasExtension(Region.class);
  }

  /**
   * Returns the the postal address type.
   *
   * @return the postal address type
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets the the postal address type.
   *
   * @param rel the postal address type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the the postal address type.
   *
   * @return whether it has the the postal address type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  /**
   * Returns the Can be street, avenue, road, etc.  This element also includes
   * the house number and room/apartment/flat/floor number.
   *
   * @return Can be street, avenue, road, etc.  This element also includes the
   *     house number and room/apartment/flat/floor number
   */
  public Street getStreet() {
    return getExtension(Street.class);
  }

  /**
   * Sets the Can be street, avenue, road, etc.  This element also includes the
   * house number and room/apartment/flat/floor number.
   *
   * @param street Can be street, avenue, road, etc.  This element also includes
   *     the house number and room/apartment/flat/floor number or
   *     <code>null</code> to reset
   */
  public void setStreet(Street street) {
    if (street == null) {
      removeExtension(Street.class);
    } else {
      setExtension(street);
    }
  }

  /**
   * Returns whether it has the Can be street, avenue, road, etc.  This element
   * also includes the house number and room/apartment/flat/floor number.
   *
   * @return whether it has the Can be street, avenue, road, etc.  This element
   *     also includes the house number and room/apartment/flat/floor number
   */
  public boolean hasStreet() {
    return hasExtension(Street.class);
  }

  /**
   * Returns the Subregion is county (US), province (in Italy), etc.
   *
   * @return Subregion is county (US), province (in Italy), etc
   */
  public Subregion getSubregion() {
    return getExtension(Subregion.class);
  }

  /**
   * Sets the Subregion is county (US), province (in Italy), etc.
   *
   * @param subregion Subregion is county (US), province (in Italy), etc or
   *     <code>null</code> to reset
   */
  public void setSubregion(Subregion subregion) {
    if (subregion == null) {
      removeExtension(Subregion.class);
    } else {
      setExtension(subregion);
    }
  }

  /**
   * Returns whether it has the Subregion is county (US), province (in Italy),
   * etc.
   *
   * @return whether it has the Subregion is county (US), province (in Italy),
   *     etc
   */
  public boolean hasSubregion() {
    return hasExtension(Subregion.class);
  }

  /**
   * Returns the The context for the address use.
   *
   * @return The context for the address use
   */
  public String getUsage() {
    return usage;
  }

  /**
   * Sets the The context for the address use.
   *
   * @param usage The context for the address use or <code>null</code> to reset
   */
  public void setUsage(String usage) {
    throwExceptionIfImmutable();
    this.usage = usage;
  }

  /**
   * Returns whether it has the The context for the address use.
   *
   * @return whether it has the The context for the address use
   */
  public boolean hasUsage() {
    return getUsage() != null;
  }

  @Override
  protected void validate() {
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
        ExtensionDescription.getDefaultDescription(
        StructuredPostalAddress.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LABEL, label);
    generator.put(MAILCLASS, mailClass);
    generator.put(PRIMARY, primary);
    generator.put(REL, rel);
    generator.put(USAGE, usage);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    label = helper.consume(LABEL, false);
    mailClass = helper.consume(MAILCLASS, false);
    primary = helper.consumeBoolean(PRIMARY, false);
    rel = helper.consume(REL, false);
    usage = helper.consume(USAGE, false);
  }

  @Override
  public String toString() {
    return "{StructuredPostalAddress label=" + label + " mailClass=" + mailClass
        + " primary=" + primary + " rel=" + rel + " usage=" + usage + "}";
  }

}
