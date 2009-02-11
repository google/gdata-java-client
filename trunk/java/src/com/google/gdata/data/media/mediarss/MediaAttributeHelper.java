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


package com.google.gdata.data.media.mediarss;

import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;

/**
 * Helps accessing media tag attributes.
 *
 * 
 */
public class MediaAttributeHelper {

  /** Gets the value of a NPT attribute and remove it from the list. */
  public static NormalPlayTime consumeNormalPlayTime(AttributeHelper attrHelper,
      String name)
      throws ParseException {
    String value = attrHelper.consume(name, false);
    if (value == null) {
      return null;
    }

    try {
      return NormalPlayTime.parse(value);
    } catch (java.text.ParseException e) {
      throw new ParseException(
          CoreErrorDomain.ERR.invalidTimeOffset.withInternalReason(
              "Invalid time offset value for attribute '" + name + "'"),
          e);
    }
  }
}
