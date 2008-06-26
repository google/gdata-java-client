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
 * GData schema extension describing hidden property of a calendar
 *
 * 
 */
public class HiddenProperty extends ValueConstruct {

  public static ExtensionDescription getDefaultDescription() {
    return new ExtensionDescription(HiddenProperty.class,
        Namespaces.gCalNs, "hidden");
  }

  public HiddenProperty() {
    this(null);
  }

  public HiddenProperty(String value) {
    super(Namespaces.gCalNs, "hidden", "value", value);
  }

  public static final HiddenProperty TRUE =
      new HiddenProperty("true");

  public static final HiddenProperty FALSE =
      new HiddenProperty("false");
}
