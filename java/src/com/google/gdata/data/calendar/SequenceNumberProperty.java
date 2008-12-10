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


package com.google.gdata.data.calendar;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * GData schema extension describing sequence number of an event.
 * The sequence number is a non-negative integer and is described in
 * section 4.8.7.4 of RFC 2445.
 * Currently this is only a read-only entry.
 *
 * 
 */
public class SequenceNumberProperty extends ValueConstruct {

  public static ExtensionDescription getDefaultDescription() {
    return new ExtensionDescription(SequenceNumberProperty.class,
        Namespaces.gCalNs, "sequence");
  }

  public SequenceNumberProperty() {
    this(null);
  }

  public SequenceNumberProperty(String value) {
    super(Namespaces.gCalNs, "sequence", "value", value);
  }
}
