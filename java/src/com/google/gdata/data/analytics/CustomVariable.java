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


package com.google.gdata.data.analytics;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Data about a user-defined custom variable.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.GA_ALIAS,
    nsUri = AnalyticsNamespace.GA,
    localName = CustomVariable.XML_NAME)
public class CustomVariable extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "customVariable";

  /** XML "index" attribute name */
  private static final String INDEX = "index";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "scope" attribute name */
  private static final String SCOPE = "scope";

  /** Index */
  private Integer index = null;

  /** Name */
  private String name = null;

  /** Scope */
  private String scope = null;

  /** Scope. */
  public static final class Scope {

    /** Hit custom variable. */
    public static final String HIT = "hit";

    /** Visit custom variable. */
    public static final String VISIT = "visit";

    /** Visitor custom variable. */
    public static final String VISITOR = "visitor";

  }

  /**
   * Default mutable constructor.
   */
  public CustomVariable() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param index index.
   * @param name name.
   * @param scope scope.
   */
  public CustomVariable(Integer index, String name, String scope) {
    super();
    setIndex(index);
    setName(name);
    setScope(scope);
    setImmutable(true);
  }

  /**
   * Returns the index.
   *
   * @return index
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * Sets the index.
   *
   * @param index index or <code>null</code> to reset
   */
  public void setIndex(Integer index) {
    throwExceptionIfImmutable();
    this.index = index;
  }

  /**
   * Returns whether it has the index.
   *
   * @return whether it has the index
   */
  public boolean hasIndex() {
    return getIndex() != null;
  }

  /**
   * Returns the name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name name or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the name.
   *
   * @return whether it has the name
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the scope.
   *
   * @return scope
   */
  public String getScope() {
    return scope;
  }

  /**
   * Sets the scope.
   *
   * @param scope scope or <code>null</code> to reset
   */
  public void setScope(String scope) {
    throwExceptionIfImmutable();
    this.scope = scope;
  }

  /**
   * Returns whether it has the scope.
   *
   * @return whether it has the scope
   */
  public boolean hasScope() {
    return getScope() != null;
  }

  @Override
  protected void validate() {
    if (index == null) {
      throwExceptionForMissingAttribute(INDEX);
    }
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
    }
    if (scope == null) {
      throwExceptionForMissingAttribute(SCOPE);
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
        ExtensionDescription.getDefaultDescription(CustomVariable.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(INDEX, index);
    generator.put(NAME, name);
    generator.put(SCOPE, scope);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    index = helper.consumeInteger(INDEX, true);
    name = helper.consume(NAME, true);
    scope = helper.consume(SCOPE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CustomVariable other = (CustomVariable) obj;
    return eq(index, other.index)
        && eq(name, other.name)
        && eq(scope, other.scope);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (index != null) {
      result = 37 * result + index.hashCode();
    }
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    if (scope != null) {
      result = 37 * result + scope.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{CustomVariable index=" + index + " name=" + name + " scope=" +
        scope + "}";
  }

}

