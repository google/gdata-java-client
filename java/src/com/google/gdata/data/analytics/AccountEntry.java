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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;

import java.util.List;

/**
 * Entry element for account feed.
 *
 * 
 */
public class AccountEntry extends BaseEntry<AccountEntry> {

  /**
   * Default mutable constructor.
   */
  public AccountEntry() {
    super();
    setKind("analytics#account");
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public AccountEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AccountEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(AccountEntry.class,
        CustomVariable.getDefaultDescription(false, true));
    extProfile.declare(AccountEntry.class, Goal.getDefaultDescription(false,
        true));
    new Goal().declareExtensions(extProfile);
    extProfile.declare(AccountEntry.class, Property.getDefaultDescription(false,
        true));
    extProfile.declare(AccountEntry.class, TableId.getDefaultDescription(true,
        false));
  }

  /**
   * Returns the custom variables.
   *
   * @return custom variables
   */
  public List<CustomVariable> getCustomVariables() {
    return getRepeatingExtension(CustomVariable.class);
  }

  /**
   * Adds a new custom variable.
   *
   * @param customVariable custom variable
   */
  public void addCustomVariable(CustomVariable customVariable) {
    getCustomVariables().add(customVariable);
  }

  /**
   * Returns whether it has the custom variables.
   *
   * @return whether it has the custom variables
   */
  public boolean hasCustomVariables() {
    return hasRepeatingExtension(CustomVariable.class);
  }

  /**
   * Returns the goals.
   *
   * @return goals
   */
  public List<Goal> getGoals() {
    return getRepeatingExtension(Goal.class);
  }

  /**
   * Adds a new goal.
   *
   * @param goal goal
   */
  public void addGoal(Goal goal) {
    getGoals().add(goal);
  }

  /**
   * Returns whether it has the goals.
   *
   * @return whether it has the goals
   */
  public boolean hasGoals() {
    return hasRepeatingExtension(Goal.class);
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

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{AccountEntry " + super.toString() + "}";
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
