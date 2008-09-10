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


package com.google.gdata.data.youtube;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.media.mediarss.MediaRating;
import com.google.gdata.data.media.mediarss.MediaRssNamespace;
import com.google.gdata.util.ParseException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Adds an attribute {@code yt:country} to {@link MediaRating}.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "rating",
    isRepeatable = true)
public class YouTubeMediaRating extends MediaRating {
  /**
   * Default value for the tag yt:country.
   */
  private static final String ALL_COUNTRIES = "all";

  /**
   * State the 'country' attribute is in.
   */
  private enum CountryState {
    /** The attribute is unset. */
    UNSET,
    /** The attribute is set to {@code all}. */
    ALL,
    /** A country set is available in {@link #countries}. */
    COUNTRIES
  }

  private CountryState countryState = CountryState.UNSET;

  /**
   * Unmodifiable set of countries, never {@code null}.
   */
  private Set<String> countries = Collections.emptySet();

  /**
   * Describes the tag to an {@link com.google.gdata.data.ExtensionProfile}.
   */
  public static ExtensionDescription getDefaultDescription() {
    return ExtensionDescription.getDefaultDescription(
        YouTubeMediaRating.class);
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);

    String countryValue;
    switch (countryState) {
      case ALL:
        countryValue = ALL_COUNTRIES;
        break;

      case COUNTRIES:
        countryValue = join(countries);
        break;

      case UNSET:
        countryValue = null;
        break;

      default:
        throw new IllegalStateException("Unknown state " + countryState);
    }
    if (countryValue != null) {
      generator.put(YouTubeNamespace.PREFIX + ":country", countryValue);
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    super.consumeAttributes(attrsHelper);
    String countryValue = attrsHelper.consume("country", false);
    if (countryValue == null) {
      clearCountry();
    } else if (ALL_COUNTRIES.equals(countryValue)) {
      setAllCountries();
    } else {
      setCountries(split(countryValue));
    }
  }

  /**
   * Checks whether a country set is set.
   *
   * @return {@code true} if a country set is set, false if the rating
   *         applies to all countries
   */
  public boolean hasCountries() {
    return countryState == CountryState.COUNTRIES;
  }

  /**
   * Explicitely sets the attribute {@code country} to {@code all}.
   */
  public void setAllCountries() {
    countryState = CountryState.ALL;
    countries = Collections.emptySet();
  }

  /**
   * Clears the attribute {@code country} of any value.
   */
  public void clearCountry() {
    countryState = CountryState.UNSET;
    countries = Collections.emptySet();
  }

  /**
   * Defines the countries to which the rating applies.
   *
   * @param countries 2-letter country code set or {@code null} to
   *        revert to the default value
   */
  public void setCountries(Collection<String> countries) {
    if (countries == null || countries.isEmpty()) {
      clearCountry();
    } else {
      this.countryState = CountryState.COUNTRIES;
      LinkedHashSet<String> set = new LinkedHashSet<String>();
      for (String country : countries) {
        set.add(country);
      }
      this.countries = Collections.unmodifiableSet(set);
    }
  }

  /**
   * Gets the country set.
   *
   * @return country set, which may be empty but not {@code null}
   */
  public Set<String> getCountries() {
    return countries;
  }

  private static String join(Collection<String> strings) {
    StringBuilder builder = new StringBuilder();
    boolean isFirst = true;
    for (String string : strings) {
      if (isFirst) {
        isFirst = false;
      } else {
        builder.append(' ');
      }
      builder.append(string);
    }
    return builder.toString();
  }

  private static Set<String> split(String value) {
    StringTokenizer tokenizer = new StringTokenizer(value, " ");
    int count = tokenizer.countTokens();
    if (count == 0) {
      return Collections.emptySet();
    }
    LinkedHashSet<String> tokens = new LinkedHashSet<String>();
    while (tokenizer.hasMoreTokens()) {
      tokens.add(tokenizer.nextToken());
    }
    return Collections.unmodifiableSet(tokens);
  }
}
