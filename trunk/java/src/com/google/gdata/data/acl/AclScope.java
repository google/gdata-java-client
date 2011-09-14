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


package com.google.gdata.data.acl;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Describes the scope of an entry in an access control list.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AclNamespace.gAclAlias,
    nsUri = AclNamespace.gAcl,
    localName = AclScope.SCOPE,
    isRequired = true)
public class AclScope extends AbstractExtension {

  /** XML "scope" element name */
  static final String SCOPE = "scope";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** helper to produce lower case name from Type value */
  private static final AttributeHelper.LowerCaseEnumToAttributeValue<Type>
      TYPE_ENUM_TO_ATTRIBUTE_VALUE =
          new AttributeHelper.LowerCaseEnumToAttributeValue<Type>();

  /** predefined values for the "type" attribute */
  public enum Type {
    INVITE,
    USER,
    DOMAIN,
    GROUP,
    DEFAULT
  }

  public AclScope() {
    super();
  }

  public AclScope(AclScope.Type type, String value) {
    super();
    setType(type);
    setValue(value);
    setImmutable(true);
  }

  public AclScope(AclScope.Type type, String value, String name) {
    super();
    setType(type);
    setValue(value);
    setName(name);
    setImmutable(true);
  }

  /** type */
  private AclScope.Type type = null;
  public Type getType() { return type; }
  public void setType(AclScope.Type type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /** value */
  private String value = null;
  public String getValue() { return value; }
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /** name */
  private String name = null;
  public String getName() { return name; }
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Return a standard external representation of this scope, suitable
   * for use as an Acl Entry identifier.
   */
  public String toExternalForm() {
    if (type == Type.DEFAULT) {
      return getTypeIdentifier(type);
    }
    return getTypeIdentifier(type) + ":" + value;
  }

  /**
   * Given a standard external representation, return the scope that it
   * represents or null if an invalid representation.  This is the inverse
   * operation of toExternalForm.
   */
  public static AclScope fromExternalForm(String externalForm) {
    if (externalForm == null) {
      return null;
    }
    if (externalForm.equals(getTypeIdentifier(Type.DEFAULT))) {
      return new AclScope(Type.DEFAULT, null);
    }
    String[] components = externalForm.split(":");
    if (components.length != 2) {
      return null;
    }
    Type type = getType(components[0]);
    if (type == null || type == Type.DEFAULT) {
      return null;
    }
    return new AclScope(type, components[1]);
  }

  @Override
  protected void validate() {
    if (type == null) {
      throwExceptionForMissingAttribute(TYPE);
    }
    if (type == AclScope.Type.DEFAULT) {
      if (value != null) {
        throw new IllegalStateException(
            "attribute " + VALUE + " should not be set for default type");
      }
    } else if (value == null) {
      throwExceptionForMissingAttribute(VALUE);
    }
  }

  @Override
  public void putAttributes(AttributeGenerator generator) {
    generator.put(TYPE, type, TYPE_ENUM_TO_ATTRIBUTE_VALUE);
    generator.put(VALUE, value);
    generator.put(NAME, name);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    type = helper.consumeEnum(TYPE, true, AclScope.Type.class, null,
        TYPE_ENUM_TO_ATTRIBUTE_VALUE);
    value = helper.consume(VALUE, false);
    name = helper.consume(NAME, false);
  }

  private static String getTypeIdentifier(Type type) {
    return TYPE_ENUM_TO_ATTRIBUTE_VALUE.getAttributeValue(type);
  }

  private static Type getType(String identifier) {
    for (Type type : Type.values()) {
      if (TYPE_ENUM_TO_ATTRIBUTE_VALUE.getAttributeValue(type)
          .equals(identifier)) {
        return type;
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!sameClassAs(o)) {
      return false;
    }
    AclScope vc = (AclScope)o;
    return eq(value, vc.value) && eq(type, vc.type) && eq(name, vc.name);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "[AclScope type=" + type + " value=" + value + " name=" + name + "]";
  }
}
