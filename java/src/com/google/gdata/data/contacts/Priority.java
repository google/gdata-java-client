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
 * Contact's priority.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Priority.XML_NAME)
public class Priority extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "priority";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  private static final AttributeHelper.EnumToAttributeValue<Rel>
      REL_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Rel>();

  /** Priority category */
  private Rel rel = null;

  /** Priority category. */
  public enum Rel {

    /** High priority. */
    HIGH,

    /** Low priority. */
    LOW,

    /** Normal priority. */
    NORMAL

  }

  /**
   * Default mutable constructor.
   */
  public Priority() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param rel priority category.
   */
  public Priority(Rel rel) {
    super();
    setRel(rel);
    setImmutable(true);
  }

  /**
   * Returns the priority category.
   *
   * @return priority category
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the priority category.
   *
   * @param rel priority category or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the priority category.
   *
   * @return whether it has the priority category
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
        ExtensionDescription.getDefaultDescription(Priority.class);
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
    Priority other = (Priority) obj;
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
    return "{Priority rel=" + rel + "}";
  }

}
