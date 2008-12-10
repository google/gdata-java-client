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
 * GData schema extension describing whether this is a sync scenario where the
 * Ical UID and Sequence number are honored during inserts and updates.
 *
 * 
 */
public class SyncEventProperty extends ValueConstruct {

  public static final SyncEventProperty TRUE =
      new SyncEventProperty(String.valueOf(true));

  public static final SyncEventProperty FALSE =
      new SyncEventProperty(String.valueOf(false));

  public static ExtensionDescription getDefaultDescription() {
    return new ExtensionDescription(SyncEventProperty.class,
        Namespaces.gCalNs, "syncEvent");
  }

  public SyncEventProperty() {
    this(null);
  }

  public SyncEventProperty(String value) {
    super(Namespaces.gCalNs, "syncEvent", "value", value);
  }
}
