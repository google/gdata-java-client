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


package com.google.gdata.data.extensions;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Extension class for manipulating entries of the Event kind.
 */
@Kind.Term(EventEntry.EVENT_KIND)
public class EventEntry extends BaseEventEntry<EventEntry> {

  /**
   * Kind term value for Event category labels.
   */
  public static final String EVENT_KIND = Namespaces.gPrefix + "event";

  /**
   * Kind category used to label feeds or entries that have Event extension
   * data.
   */
  public static final Category EVENT_CATEGORY =
      new Category(Namespaces.gKind, EVENT_KIND);

  /**
   * Constructs a new EventEntry with the appropriate kind category
   * to indicate that it is an event.
   */
  public EventEntry() {
    super();
  }

  /**
   * Constructs a new EventEntry instance by doing a shallow copy of data
   * from an existing BaseEntry instance.
   */
  public EventEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by an EventEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(EventEntry.class, Who.getDefaultDescription());
  }

  /**
   * Returns the list of event participants.
   */
  public List<Who> getParticipants() {
    return getRepeatingExtension(Who.class);
  }

  /**
   * Adds a new event participant.
   */
  public void addParticipant(Who participant) {
    getParticipants().add(participant);
  }
}
