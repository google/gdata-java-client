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
 * GData schema extension describing calendarlist type property of a calendar
 *
 * 
 */
public class ListType extends ValueConstruct {

  public static final ListType FAVORITE =
      new ListType("favorite");

  public static final ListType ACCESSED =
      new ListType("accessed");

  public static final ListType OWNED =
      new ListType("owned");

  public static ExtensionDescription getDefaultDescription() {
    return new ExtensionDescription(ListType.class,
        Namespaces.gCalNs, "listtype");
  }

  public ListType() {
    this(null);
  }

  public ListType(String value) {
    super(Namespaces.gCalNs, "listttype", "value", value);
  }
}
