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


package com.google.gdata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides an ElementVisitor implementation that aggregates multiple
 * nested ElementVisitor instances. This makes it possible to apply several
 * different unrelated visitor implementations during a single data model tree
 * traversal.
 */
public class CompositeElementVisitor implements ElementVisitor {

  /**
   * The visitors field contains a list of the active ElementVisitor instances
   * for the composite visitor.
   */
  private final List<ElementVisitor> visitors;

  /**
   * Contains the collection of inactive ElementVisitor instance and the
   * {@link ElementVisitor.StoppedException} that made them inactive.
   */
  private final Map<ElementVisitor, ElementVisitor.StoppedException>
      stoppedVisitors =
          new HashMap<ElementVisitor, ElementVisitor.StoppedException>();

  /**
   * The ignoringVisitors map contains information about visitors that are
   * currently ignoring child events. The key will be the visitor instance that
   * is ignoring child events and the value will be the element beneath which
   * all child events are being ignored.
   */
  private final Map<ElementVisitor, Element> ignoringVisitors =
      new HashMap<ElementVisitor, Element>();

  /**
   * Constructs a new CompositeElementVisitor instance containing the array of
   * visitor instances passed in. The order in which each visitor instance
   * appears in the array is significant; it defines the order in which events
   * for a given element will be delivered to the nested instances.
   *
   * @param visitors list of ElementVisitor instances to compose.
   */
  public CompositeElementVisitor(ElementVisitor... visitors) {
    this.visitors = new ArrayList<ElementVisitor>(visitors.length);
    for (ElementVisitor visitor : visitors) {
      this.visitors.add(visitor);
    }
  }

  /**
   * Adds a new {@link ElementVisitor} instance to the composite visitor. For
   * any element, events will be delivered to the added visitor after events
   * have been delivered to other visitors already contained in the composite
   * visitor at the time of addition.
   *
   * @param visitor the new visitor to add.
   */
  public void addVisitor(ElementVisitor visitor) {
    visitors.add(visitor);
  }

  /**
   * Returns the list of active element visitor instances. Any stopped visitors
   * will not appear in this list.
   *
   * @return list of visitor instances in order of event delivery.
   */
  public List<ElementVisitor> getVisitors() {
    return visitors;
  }

  /**
   * Returns any StoppedException thrown by the specified visitor, or
   * {@code null} if the visitor completed normally.
   */
  public StoppedException getStoppedException(ElementVisitor visitor) {
    return stoppedVisitors.get(visitor);
  }

  public boolean visit(Element parent, Element target,
      ElementMetadata<?, ?> metadata) throws StoppedException {

    // True if any nested visitor has stopped
    boolean newStopped = false;

    // Iterate through the list of active visitors
    for (ElementVisitor visitor : visitors) {

      // If ignoring child events, skip all events until a visitComplete
      // event is delivered on the associated extension point.
      if (ignoringVisitors.containsKey(visitor)) {
        continue;
      }
      try {
        boolean visitChildren = visitor.visit(parent, target, metadata);
        if (!visitChildren) {
          ignoringVisitors.put(visitor, target);
        }
      } catch (ElementVisitor.StoppedException se) {
        stoppedVisitors.put(visitor, se);
        newStopped = true;
      }
    }

    // Remove any stopped visitors and associated state.
    if (newStopped) {
      visitors.removeAll(stoppedVisitors.keySet());
      if (visitors.isEmpty()) {
        throw new ElementVisitor.StoppedException("All visitors stopped");
      }
    }

    // If all visitors are ignoring children, then don't traverse into them
    return visitors.size() != ignoringVisitors.size();
  }

  public void visitComplete(Element parent, Element target,
      ElementMetadata<?, ?> metadata) throws StoppedException {

    // Check any visitors that are currently ignoring children to see if
    // they should be re-enabled as a result of this event.
    List<ElementVisitor> resetList = null;
    for (Map.Entry<ElementVisitor, Element> stateEntry : ignoringVisitors
        .entrySet()) {
      Element ignoring = stateEntry.getValue();
      if (ignoring == target) {
        if (resetList == null) {
          resetList = new ArrayList<ElementVisitor>();
        }
        resetList.add(stateEntry.getKey());
      }
    }

    // Removed re-enabled visitors
    if (resetList != null) {
      for (ElementVisitor enabledVisitor : resetList) {
        ignoringVisitors.remove(enabledVisitor);
      }
    }

    // Deliver the visitComplete event to all active visitors.
    for (ElementVisitor visitor : visitors) {
      if (!ignoringVisitors.containsKey(visitor)) {
        visitor.visitComplete(parent, target, metadata);
      }
    }
  }
}
