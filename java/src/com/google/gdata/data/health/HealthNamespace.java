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


package com.google.gdata.data.health;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Health.
 *
 * 
 */
public class HealthNamespace {

  private HealthNamespace() {}

  /** Continuity of Care Record namespace (CCR) namespace */
  public static final String CCR = "urn:astm-org:CCR";

  /** Continuity of Care Record namespace (CCR) namespace prefix */
  public static final String CCR_PREFIX = CCR + "#";

  /** Continuity of Care Record namespace (CCR) namespace alias */
  public static final String CCR_ALIAS = "ccr";

  /** XML writer namespace for Continuity of Care Record namespace (CCR) */
  public static final XmlNamespace CCR_NS = new XmlNamespace(CCR_ALIAS, CCR);

  /** The h9 namespace (H9) namespace */
  public static final String H9 = "http://schemas.google.com/health/data";

  /** The h9 namespace (H9) namespace prefix */
  public static final String H9_PREFIX = H9 + "#";

  /** The h9kinds namespace (H9KINDS) namespace */
  public static final String H9KINDS = "http://schemas.google.com/health/kinds";

  /** The h9kinds namespace (H9KINDS) namespace prefix */
  public static final String H9KINDS_PREFIX = H9KINDS + "#";

  /** The h9 metadata namespace (H9M) namespace */
  public static final String H9M = "http://schemas.google.com/health/metadata";

  /** The h9 metadata namespace (H9M) namespace prefix */
  public static final String H9M_PREFIX = H9M + "#";

  /** The h9 metadata namespace (H9M) namespace alias */
  public static final String H9M_ALIAS = "h9m";

  /** XML writer namespace for The h9 metadata namespace (H9M) */
  public static final XmlNamespace H9M_NS = new XmlNamespace(H9M_ALIAS, H9M);


  /** The h9 namespace (H9) namespace alias */
  public static final String H9_ALIAS = "h9";

  /** XML writer namespace for The h9 namespace (H9) */
  public static final XmlNamespace H9_NS = new
      XmlNamespace(H9_ALIAS, H9);

  /** The h9kinds namespace (H9KINDS) namespace alias */
  public static final String H9KINDS_ALIAS = "h9kinds";

  /** XML writer namespace for The h9kinds namespace (H9KINDS) */
  public static final XmlNamespace H9KINDS_NS = new
      XmlNamespace(H9KINDS_ALIAS, H9KINDS);

  /** Link Rel for a complete url of an entry, indicating the smallest feed
   * containing the entry. */
  public static final String REL_COMPLETE_URL = H9 + "#complete";

  /**
   * Google Health recognizes the enumerated Category Schemes for GData Category
   * queries.
   **/
  public enum CategoryScheme {

    /**
     * CCR scheme  identifies the category of all CCR-categories or subsections.
     * Possible term
     *  values are:
     * <p>
     * <ul>
     * <li> labtest </li>
     * <li> medication </li>
     * <li> condition </li>
     * <li> symptom </li>
     * <li> vitalsigns </li>
     * <li> procedure </li>
     * <li> immunization </li>
     * <li> familyhistory </li>
     * <li> allergy </li>
     * <li> demographics </li>
     * </ul>
     * </p>
     */
    ccr("http://schemas.google.com/health/ccr"),
    /**
     * Item scheme identifies the category of all health-items. Possible term
     * values are the names
     * of a health record. [e.g. Lipitor].
     */
    item("http://schemas.google.com/health/item"),

    /**
     * Code scheme identifies CCR codes.  Possible term values are the codes.
     *
     */
    code("http://schemas.google.com/health/code");

    CategoryScheme(final String iri) {
      this.iri = iri;
    }

    /**
     * Returns the  IRI which identifies this categorization scheme.
     *
     * @return the IRI identifier.
     */
    public String getIri() {
      return iri;
    }
    private final String iri;
  }
}
