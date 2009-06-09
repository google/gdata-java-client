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

import java.util.Map;
import java.util.HashMap;

/**
 * Attribute types.
 *
 * This is an open type enumeration. Use {@link #getInstance(String)} to
 * convert a string into a GoogleBaseAttributeType.
 *
 * When not parsing XML streams, always use the constants defined
 * in this class when referring to a specific type.
 *
 * Use equality (A.equals(B)) and not identity (A == B) when comparing
 * types.
 */
public class GoogleBaseAttributeType {

  /**
   * All known types, for {@link #getInstance(String)}.
   */
  private static final Map<String, GoogleBaseAttributeType> KNOWN_TYPES =
      new HashMap<String, GoogleBaseAttributeType>();

  /** Text values. */
  public static final GoogleBaseAttributeType TEXT =
      createAndRegister("text");

  /**
   * Any kind of number. Example: <code>10</code>, <code>3.14</code>.
   *
   * This is a supertype of {@link #INT} and {@link #NUMBER}.
   */
  public static final GoogleBaseAttributeType NUMBER =
      createAndRegister("number");

  /** Whole number values. */
  public static final GoogleBaseAttributeType INT =
      createAndRegister(NUMBER, "int");

  /** Numbers with decimal digits. */
  public static final GoogleBaseAttributeType FLOAT =
      createAndRegister(NUMBER, "float");

  /**
   * A number and a string. Example: <code>1.5 m</code>, <code>12 lb</code>.
   *
   * This is a supertype of {@link #INT_UNIT} and {@link #FLOAT_UNIT}.
   */
  public static final GoogleBaseAttributeType NUMBER_UNIT =
      createAndRegister("numberUnit");

  /** Whole number value and a string. Example: <code>10 km</code>)*/
  public static final GoogleBaseAttributeType INT_UNIT =
      createAndRegister(NUMBER_UNIT, "intUnit");

  /** Numbers with decimal digits and a string. Example: <code>1.5 m</code>*/
  public static final GoogleBaseAttributeType FLOAT_UNIT =
      createAndRegister(NUMBER_UNIT, "floatUnit");

  /**
   * Start and end dates for an event.
   *
   * An attribute of this type is represented by two date strings in format
   * YYYY-MM-DD (RFC3339)separated by a space.
   *
   * Example:
   * <pre>1975-09-25 1975-09-25</pre>
   *
   * @see com.google.api.gbase.client.DateTimeRange
   */
  public static final GoogleBaseAttributeType DATE_TIME_RANGE =
      createAndRegister("dateTimeRange");

  /** Date  for an event, in format YYYY-MM-DD (RFC3339). */
  public static final GoogleBaseAttributeType DATE =
      createAndRegister(DATE_TIME_RANGE, "date");

  /** Date and time for an event, in format YYYY-MM-DDThh:mm:ss (RFC3339). */
  public static final GoogleBaseAttributeType DATE_TIME =
      createAndRegister(DATE, "dateTime");

  /** HTTP URL. */
  public static final GoogleBaseAttributeType URL =
      createAndRegister("url");

  /** Either true or false. */
  public static final GoogleBaseAttributeType BOOLEAN =
      createAndRegister("boolean");
  
  /**
   * Group of related attributes.
   */
  public static final GoogleBaseAttributeType GROUP = createAndRegister("group");

  /**
   * Shipping information.
   *
   * Example:
   * <pre>&lt;country&gt;DE&lt;/country&gt;
   * &lt;region&gt;Munich&lt;/region&gt;
   * &lt;price&gt;12.50 eur&lt;/price&gt;
   * &lt;service&gt;Deutsche Post&lt;/service&gt;
   * </pre>
   */
  public static final GoogleBaseAttributeType SHIPPING = createAndRegister(GROUP, "shipping");

  /**
   * Tax information.
   *
   * Example:
   * <pre>&lt;country&gt;DE&lt;/country&gt;
   * &lt;region&gt;Munich&lt;/region&gt;
   * &lt;rate&gt;12.50 eur&lt;/rate&gt;
   * &lt;tax_ship&gt;true&lt;/tax_ship&gt;
   * </pre>
   */
  public static final GoogleBaseAttributeType TAX = createAndRegister(GROUP, "tax");

  /**
   * A location, a string describing the address.
   */
  public static final GoogleBaseAttributeType LOCATION =
      createAndRegister("location");

  /**
   * A reference to another Google Base Item.
   */
  public static final GoogleBaseAttributeType REFERENCE = 
      createAndRegister("reference");
  
  /** Type name */
  private final String name;

  /** Supertype (if there is any.) */
  private final GoogleBaseAttributeType supertype;

  /**
   * Creates a new type and registers it so that {@link #getInstance(String)}
   * will find it.
   *
   * @param name
   * @return the new type
   */
  private static GoogleBaseAttributeType createAndRegister(String name) {
    return createAndRegister(null, name);
  }

  /**
   * Creates a new type with a supertype and registers it so that
   * {@link #getInstance(String)} will find it.
   *
   * @param supertype
   * @param name
   * @return the new type
   */
  private static GoogleBaseAttributeType createAndRegister(
      GoogleBaseAttributeType supertype, String name) {
    GoogleBaseAttributeType type = new GoogleBaseAttributeType(supertype, name);
    KNOWN_TYPES.put(type.getName(), type);
    return type;
  }

  /**
   * Create a new type with a supertype.
   *
   * Outside of this class, use {@link #getInstance(String)} instead.
   * @param supertype can be null if the type has no supertype
   * @param name
   */
  private GoogleBaseAttributeType(GoogleBaseAttributeType supertype,
                                  String name) {
    this.supertype = supertype;
    this.name = name.intern();
  }

  /**
   * Gets the supertype or null.
   *
   * @return supertype, if one has been defined
   */
  public GoogleBaseAttributeType getSupertype() {
    return supertype;
  }

  /**
   * Gets the type name.
   *
   * @return type name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the type name.
   *
   * @return type name
   */
  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GoogleBaseAttributeType)) {
      return false;
    }
    GoogleBaseAttributeType other = (GoogleBaseAttributeType)o;
    return name == other.name;
  }

  @Override
  public int hashCode() {
    return 11 + name.hashCode();
  }


  /**
   * Get a type instance.
   *
   * @param typeName name of a type found in a feed. Current version of
   *   the API will always return one of the types listed in this class
   *   but future versions might support new types, so <code>getInstance</code>
   *   accepts any type name
   * @return an instance of GoogleBaseAttributeType, or <code>null</code>
   *   if <code>typeName</code> was null
   */
  public static GoogleBaseAttributeType getInstance(String typeName) {
    if (typeName == null) {
      return null;
    }
    GoogleBaseAttributeType commonType = KNOWN_TYPES.get(typeName);
    if (commonType != null) {
      return commonType;
    } else {
      return new GoogleBaseAttributeType(null, typeName);
    }
  }

  /**
   * Checks whether the given type is the same as this type or a subtype.
   *
   * @param subtype
   * @return true if <code>subtype</code> is the same type or a subtype
   *   of the current type
   */
  public boolean isSupertypeOf(GoogleBaseAttributeType subtype) {
    if (this.equals(subtype)) {
      return true;
    }
    GoogleBaseAttributeType otherSupertype = subtype.getSupertype();
    if (otherSupertype == null) {
      return false;
    }
    return isSupertypeOf(otherSupertype);
  }
}
