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


package com.google.gdata.data.webmastertools;

/**
 * DomainPreference is an enumerated type that indicates the preferred domain
 * to use for a concrete site for Webmaster tools. The possibilities are:
 * <pre class="code">
 *  - PREFERWWW: domain.com and www.domain.com should be  associated 
 *        and www.domain.com is preferred
 *  - PREFERNOWWW: domain.com and www.domain.com should be associated 
 *         domain.com is preferred
 *  - NONE: there should be no association 
 * </pre>
 * 
 * 
*/
public enum DomainPreference {
  
  NONE("none"),
  PREFER_WWW("preferwww"),
  PREFER_NO_WWW("prefernowww"),
  ;
  
  private String value;
  
  /**
   * Constructor. Associates a string with the enum value.
   * 
   * @param associatedString the string associated with the preference value.
   */
  private DomainPreference(String associatedString) {
    value = associatedString;
  }

  /**
   * Returns the string representation of the Domain Preference.
   */
  @Override
  public String toString() {
    return value;
  }
  
  /**
   * Get the default value for the domain preference.
   * 
   * @return default value for the domain preference.
   */
  public static DomainPreference getDefault() {
    return NONE;
  }
  
  /**
   * Parse a string and return a domain preference.
   * 
   * @param value a string representing a domain preference
   * @return a DomainPrefrence if the parameter can be successfully parsed
   * @throws IllegalArgumentException if the parameter is not a valid
   * DomainPreference string
   */
  public static DomainPreference fromString(String value) 
      throws IllegalArgumentException {
    for (DomainPreference preference : values()) {
      if (preference.toString().equals(value)) {
        return preference;
      }
    }
    
    throw new IllegalArgumentException(
        "The parameter is not a valid DomainPreference string");
  } 
}