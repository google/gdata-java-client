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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Describes a data source.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AnalyticsNamespace.DXP_ALIAS,
    nsUri = AnalyticsNamespace.DXP,
    localName = DataSource.XML_NAME)
public class DataSource extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "dataSource";

  /**
   * Default mutable constructor.
   */
  public DataSource() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(DataSource.class)) {
      return;
    }
    extProfile.declare(DataSource.class, Property.getDefaultDescription(false,
        true));
    extProfile.declare(DataSource.class, TableId.getDefaultDescription(true,
        false));
    extProfile.declare(DataSource.class, TableName.getDefaultDescription(true,
        false));
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

  /**
   * Returns the data source ID.
   *
   * @return data source ID
   */
  public TableId getTableId() {
    return getExtension(TableId.class);
  }

  /**
   * Sets the data source ID.
   *
   * @param tableId data source ID or <code>null</code> to reset
   */
  public void setTableId(TableId tableId) {
    if (tableId == null) {
      removeExtension(TableId.class);
    } else {
      setExtension(tableId);
    }
  }

  /**
   * Returns whether it has the data source ID.
   *
   * @return whether it has the data source ID
   */
  public boolean hasTableId() {
    return hasExtension(TableId.class);
  }

  /**
   * Returns the table name.
   *
   * @return table name
   */
  public TableName getTableName() {
    return getExtension(TableName.class);
  }

  /**
   * Sets the table name.
   *
   * @param tableName table name or <code>null</code> to reset
   */
  public void setTableName(TableName tableName) {
    if (tableName == null) {
      removeExtension(TableName.class);
    } else {
      setExtension(tableName);
    }
  }

  /**
   * Returns whether it has the table name.
   *
   * @return whether it has the table name
   */
  public boolean hasTableName() {
    return hasExtension(TableName.class);
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
        ExtensionDescription.getDefaultDescription(DataSource.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{DataSource}";
  }


   /**
    * Retrieves the value of the property with the given name.
    *
    * @param name The name of the property to retrieve
    * @return The named property's value, or null if the property was not
    *     present
    */
   public String getProperty(String name) {
     for (Property property : getProperties()) {
       if (property.getName().equals(name)) {
         return property.getValue();
       }
     }
     return null;
   }

}
