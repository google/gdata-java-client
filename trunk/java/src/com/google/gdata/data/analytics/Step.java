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
 * Single step in a multistep goal.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.GA_ALIAS,
    nsUri = AnalyticsNamespace.GA,
    localName = Step.XML_NAME)
public class Step extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "step";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** XML "number" attribute name */
  private static final String NUMBER = "number";

  /** XML "path" attribute name */
  private static final String PATH = "path";

  /** Step's name */
  private String name = null;

  /** Step's number in sequence */
  private Integer number = null;

  /** Step's path */
  private String path = null;

  /**
   * Default mutable constructor.
   */
  public Step() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param name step's name.
   * @param number step's number in sequence.
   * @param path step's path.
   */
  public Step(String name, Integer number, String path) {
    super();
    setName(name);
    setNumber(number);
    setPath(path);
    setImmutable(true);
  }

  /**
   * Returns the step's name.
   *
   * @return step's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the step's name.
   *
   * @param name step's name or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the step's name.
   *
   * @return whether it has the step's name
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the step's number in sequence.
   *
   * @return step's number in sequence
   */
  public Integer getNumber() {
    return number;
  }

  /**
   * Sets the step's number in sequence.
   *
   * @param number step's number in sequence or <code>null</code> to reset
   */
  public void setNumber(Integer number) {
    throwExceptionIfImmutable();
    this.number = number;
  }

  /**
   * Returns whether it has the step's number in sequence.
   *
   * @return whether it has the step's number in sequence
   */
  public boolean hasNumber() {
    return getNumber() != null;
  }

  /**
   * Returns the step's path.
   *
   * @return step's path
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the step's path.
   *
   * @param path step's path or <code>null</code> to reset
   */
  public void setPath(String path) {
    throwExceptionIfImmutable();
    this.path = path;
  }

  /**
   * Returns whether it has the step's path.
   *
   * @return whether it has the step's path
   */
  public boolean hasPath() {
    return getPath() != null;
  }

  @Override
  protected void validate() {
    if (name == null) {
      throwExceptionForMissingAttribute(NAME);
    }
    if (number == null) {
      throwExceptionForMissingAttribute(NUMBER);
    }
    if (path == null) {
      throwExceptionForMissingAttribute(PATH);
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
        ExtensionDescription.getDefaultDescription(Step.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(NAME, name);
    generator.put(NUMBER, number);
    generator.put(PATH, path);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    name = helper.consume(NAME, true);
    number = helper.consumeInteger(NUMBER, true);
    path = helper.consume(PATH, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Step other = (Step) obj;
    return eq(name, other.name)
        && eq(number, other.number)
        && eq(path, other.path);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (name != null) {
      result = 37 * result + name.hashCode();
    }
    if (number != null) {
      result = 37 * result + number.hashCode();
    }
    if (path != null) {
      result = 37 * result + path.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Step name=" + name + " number=" + number + " path=" + path + "}";
  }

}

