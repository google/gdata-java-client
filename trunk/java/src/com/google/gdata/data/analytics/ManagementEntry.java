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
import com.google.gdata.data.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry element for GA data source feed.
 *
 * 
 */
public class ManagementEntry extends BaseEntry<ManagementEntry> {

  /**
   * Default mutable constructor.
   */
  public ManagementEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public ManagementEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(ManagementEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(ManagementEntry.class, Goal.class);
    new Goal().declareExtensions(extProfile);
    extProfile.declare(ManagementEntry.class,
        AnalyticsLink.getDefaultDescription(false, true));
    extProfile.declare(ManagementEntry.class,
        Property.getDefaultDescription(false, true));
    extProfile.declare(ManagementEntry.class, Segment.class);
    new Segment().declareExtensions(extProfile);
  }

  /**
   * Returns the goal.
   *
   * @return goal
   */
  public Goal getGoal() {
    return getExtension(Goal.class);
  }

  /**
   * Sets the goal.
   *
   * @param goal goal or <code>null</code> to reset
   */
  public void setGoal(Goal goal) {
    if (goal == null) {
      removeExtension(Goal.class);
    } else {
      setExtension(goal);
    }
  }

  /**
   * Returns whether it has the goal.
   *
   * @return whether it has the goal
   */
  public boolean hasGoal() {
    return hasExtension(Goal.class);
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
   * Returns the segment.
   *
   * @return segment
   */
  public Segment getSegment() {
    return getExtension(Segment.class);
  }

  /**
   * Sets the segment.
   *
   * @param segment segment or <code>null</code> to reset
   */
  public void setSegment(Segment segment) {
    if (segment == null) {
      removeExtension(Segment.class);
    } else {
      setExtension(segment);
    }
  }

  /**
   * Returns whether it has the segment.
   *
   * @return whether it has the segment
   */
  public boolean hasSegment() {
    return hasExtension(Segment.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{ManagementEntry " + super.toString() + "}";
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

  /**
   * Returns all the parent links contained in the entry.
   */
  public List<AnalyticsLink> getParentLinks() {
    List<AnalyticsLink> links = new ArrayList<AnalyticsLink>();
    for (Link link : getLinks()) {
      if (link.getRel().equals(AnalyticsLink.Rel.PARENT)) {
        links.add((AnalyticsLink) link);
      }
    }
    return links;
  }

  /**
   * Returns all the child links contained in the entry.
   */
  public List<AnalyticsLink> getChildLinks() {
    List<AnalyticsLink> links = new ArrayList<AnalyticsLink>();
    for (Link link : getLinks()) {
      if (link.getRel().equals(AnalyticsLink.Rel.CHILD)) {
        links.add((AnalyticsLink) link);
      }
    }
    return links;
  }

  /**
   * Returns the link with the given targetKind. It returns {@code null} if a
   * link with the given targetKind value is not found.
   */
  public AnalyticsLink getChildLink(String targetKind) {
    for (Link link : getLinks()) {
      if (link.getRel().equals(AnalyticsLink.Rel.CHILD)) {

        AnalyticsLink analyticsLink = (AnalyticsLink) link;
        if (analyticsLink.getTargetKind().equals(targetKind)) {
          return analyticsLink;
        }
      }
    }
    return null;
  }

}

