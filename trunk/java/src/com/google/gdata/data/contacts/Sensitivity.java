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


package com.google.gdata.data.contacts;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Contact's sensitivity.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Sensitivity.XML_NAME)
public class Sensitivity extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "sensitivity";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  private static final AttributeHelper.EnumToAttributeValue<Rel>
      REL_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Rel>();

  /** Sensitivity type */
  private Rel rel = null;

  /** Sensitivity type. */
  public enum Rel {

    /** Confidential sensitivity. */
    CONFIDENTIAL,

    /** Normal sensitivity. */
    NORMAL,

    /** Personal sensitivity. */
    PERSONAL,

    /** Private sensitivity. */
    PRIVATE

  }

  /**
   * Default mutable constructor.
   */
  public Sensitivity() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param rel sensitivity type.
   */
  public Sensitivity(Rel rel) {
    super();
    setRel(rel);
    setImmutable(true);
  }

  /**
   * Returns the sensitivity type.
   *
   * @return sensitivity type
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the sensitivity type.
   *
   * @param rel sensitivity type or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the sensitivity type.
   *
   * @return whether it has the sensitivity type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  protected void validate() {
    if (rel == null) {
      throwExceptionForMissingAttribute(REL);
    }
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
        ExtensionDescription.getDefaultDescription(Sensitivity.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(REL, rel, REL_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    rel = helper.consumeEnum(REL, true, Rel.class, null,
        REL_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Sensitivity other = (Sensitivity) obj;
    return eq(rel, other.rel);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (rel != null) {
      result = 37 * result + rel.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Sensitivity rel=" + rel + "}";
  }

}

