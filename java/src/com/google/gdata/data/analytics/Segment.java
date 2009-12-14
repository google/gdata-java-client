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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * Describes a segment.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = Segment.XML_NAME)
public class Segment extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "segment";

  /** XML "id" attribute name */
  private static final String ID = "id";

  /** XML "name" attribute name */
  private static final String NAME = "name";

  /** Id of the segment */
  private String id = null;

  /** Name of the segment */
  private String name = null;

  /**
   * Default mutable constructor.
   */
  public Segment() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param id id of the segment.
   * @param name name of the segment.
   */
  public Segment(String id, String name) {
    super();
    setId(id);
    setName(name);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Segment.class)) {
      return;
    }
    extProfile.declare(Segment.class, Definition.class);
    extProfile.declare(Segment.class, Property.getDefaultDescription(false,
        true));
  }

  /**
   * Returns the definition.
   *
   * @return definition
   */
  public Definition getDefinition() {
    return getExtension(Definition.class);
  }

  /**
   * Sets the definition.
   *
   * @param definition definition or <code>null</code> to reset
   */
  public void setDefinition(Definition definition) {
    if (definition == null) {
      removeExtension(Definition.class);
    } else {
      setExtension(definition);
    }
  }

  /**
   * Returns whether it has the definition.
   *
   * @return whether it has the definition
   */
  public boolean hasDefinition() {
    return hasExtension(Definition.class);
  }

  /**
   * Returns the id of the segment.
   *
   * @return id of the segment
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id of the segment.
   *
   * @param id id of the segment or <code>null</code> to reset
   */
  public void setId(String id) {
    throwExceptionIfImmutable();
    this.id = id;
  }

  /**
   * Returns whether it has the id of the segment.
   *
   * @return whether it has the id of the segment
   */
  public boolean hasId() {
    return getId() != null;
  }

  /**
   * Returns the name of the segment.
   *
   * @return name of the segment
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the segment.
   *
   * @param name name of the segment or <code>null</code> to reset
   */
  public void setName(String name) {
    throwExceptionIfImmutable();
    this.name = name;
  }

  /**
   * Returns whether it has the name of the segment.
   *
   * @return whether it has the name of the segment
   */
  public boolean hasName() {
    return getName() != null;
  }

  /**
   * Returns the properties.
   *
   * @return properties
   */
  public List<Property> getProperties() {
    return getRepeatingExtension(Property.class);
  }

  /**
   * Adds a new property.
   *
   * @param property property
   */
  public void addProperty(Property property) {
    getProperties().add(property);
  }

  /**
   * Returns whether it has the properties.
   *
   * @return whether it has the properties
   */
  public boolean hasProperties() {
    return hasRepeatingExtension(Property.class);
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
        ExtensionDescription.getDefaultDescription(Segment.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(ID, id);
    generator.put(NAME, name);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    id = helper.consume(ID, false);
    name = helper.consume(NAME, false);
  }

  @Override
  public String toString() {
    return "{Segment id=" + id + " name=" + name + "}";
  }


  /**
   * Returns the value of the named property of this entry. More specifically,
   * it returns the content of the {@code value} attribute of the
   * {@code dxp:property} whose {@code name} attribute matches the argument.
   * Returns {@code null} if no such property exists.
   *
   * @param name the property to retrieve from this entry
   * @return string value of the named property or null if it doesn't exist
   */
  public String getProperty(String name) {
    // We assume that each Property object has unique non null name.  This code
    // will ignore Property
    // with null name and if there are two Property objects with the same name,
    // it will return the
    // first one it found.
    if (hasProperties()) {
      for (Property property : getProperties()) {
        if (property.hasName() && property.getName().equalsIgnoreCase(name)) {
          return property.getValue();
        }
      }
    }
    return null;
  }

}
