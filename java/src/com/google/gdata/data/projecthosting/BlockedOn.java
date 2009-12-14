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


package com.google.gdata.data.projecthosting;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

/**
 * Issue this issue is blocked on.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ProjectHostingNamespace.ISSUES_ALIAS,
    nsUri = ProjectHostingNamespace.ISSUES,
    localName = BlockedOn.XML_NAME)
public class BlockedOn extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "blockedOn";

  /**
   * Default mutable constructor.
   */
  public BlockedOn() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(BlockedOn.class)) {
      return;
    }
    extProfile.declare(BlockedOn.class, Id.getDefaultDescription(true, false));
    extProfile.declare(BlockedOn.class, Project.class);
  }

  /**
   * Returns the id.
   *
   * @return id
   */
  public Id getId() {
    return getExtension(Id.class);
  }

  /**
   * Sets the id.
   *
   * @param id id or <code>null</code> to reset
   */
  public void setId(Id id) {
    if (id == null) {
      removeExtension(Id.class);
    } else {
      setExtension(id);
    }
  }

  /**
   * Returns whether it has the id.
   *
   * @return whether it has the id
   */
  public boolean hasId() {
    return hasExtension(Id.class);
  }

  /**
   * Returns the project.
   *
   * @return project
   */
  public Project getProject() {
    return getExtension(Project.class);
  }

  /**
   * Sets the project.
   *
   * @param project project or <code>null</code> to reset
   */
  public void setProject(Project project) {
    if (project == null) {
      removeExtension(Project.class);
    } else {
      setExtension(project);
    }
  }

  /**
   * Returns whether it has the project.
   *
   * @return whether it has the project
   */
  public boolean hasProject() {
    return hasExtension(Project.class);
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
        ExtensionDescription.getDefaultDescription(BlockedOn.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{BlockedOn}";
  }

}

