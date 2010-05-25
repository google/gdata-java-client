/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http;

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

/**
 * Implements support for HTTP form content encoding parsing of type {@code
 * application/x-www-form-urlencoded} as specified in the <a href=
 * "http://www.w3.org/TR/1998/REC-html40-19980424/interact/forms.html#h-17.13.4.1"
 * >HTML 4.0 Specification</a>.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class UrlEncodedParser implements HttpParser {

  /**
   * Whether to disable response content logging (unless {@link Level#ALL} is
   * loggable which forces all logging).
   * <p>
   * Useful for example if content has sensitive data such as an authentication
   * token. Defaults to {@code false}.
   */
  public boolean disableContentLogging;

  public String getContentType() {
    return "application/x-www-form-urlencoded";
  }

  public <T> T parse(HttpResponse response, Class<T> dataClass)
      throws IOException {
    if (this.disableContentLogging) {
      response.disableContentLogging = true;
    }
    T newInstance = ClassInfo.newInstance(dataClass);
    parse(response.parseAsString(), newInstance);
    return newInstance;
  }

  /** Parses the given content into the given data object of key/value pairs. */
  public static void parse(String content, Object data) {
    Class<?> clazz = data.getClass();
    ClassInfo classInfo = ClassInfo.of(clazz);
    GenericData genericData =
        GenericData.class.isAssignableFrom(clazz) ? (GenericData) data : null;
    @SuppressWarnings("unchecked")
    Map<Object, Object> map =
        Map.class.isAssignableFrom(clazz) ? (Map<Object, Object>) data : null;
    int cur = 0;
    int length = content.length();
    while (cur < length) {
      int amp = content.indexOf('&', cur);
      if (amp == -1) {
        amp = length;
      }
      int equals = content.indexOf('=', cur);
      if (equals <= cur || equals >= amp) {
        throw new IllegalArgumentException("malformed URL encoding: "
            + content.substring(cur, amp));
      }
      String name = CharEscapers.decodeUri(content.substring(cur, equals));
      String value = CharEscapers.decodeUri(content.substring(equals + 1, amp));
      // get the field from the type information
      Field field = classInfo.getField(name);
      if (field != null) {
        Object fieldValue = parseValue(value, field.getType());
        FieldInfo.setFieldValue(field, data, fieldValue);
      } else if (genericData != null) {
        genericData.set(name, value);
      } else if (map != null) {
        map.put(name, value);
      }
      cur = amp + 1;
    }
  }

  private static Object parseValue(String stringValue, Class<?> fieldClass) {
    if (fieldClass == null || fieldClass == String.class) {
      return stringValue;
    }
    if (fieldClass == Integer.class || fieldClass == int.class) {
      return new Integer(stringValue);
    }
    if (fieldClass == Short.class || fieldClass == short.class) {
      return new Short(stringValue);
    }
    if (fieldClass == Byte.class || fieldClass == byte.class) {
      return new Byte(stringValue);
    }
    if (fieldClass == Long.class || fieldClass == long.class) {
      return new Long(stringValue);
    }
    if (fieldClass == Double.class || fieldClass == double.class) {
      return new Double(stringValue);
    }
    if (fieldClass == Character.class || fieldClass == char.class) {
      if (stringValue.length() != 1) {
        throw new IllegalArgumentException(
            "expected type Character/char but got " + fieldClass);
      }
      return stringValue.charAt(0);
    }
    if (fieldClass == DateTime.class) {
      return DateTime.parseRfc3339(stringValue);
    }
    if (fieldClass == Boolean.class || fieldClass == boolean.class) {
      return "true".equals(stringValue) ? Boolean.TRUE : Boolean.FALSE;
    }
    if (fieldClass == Float.class || fieldClass == float.class) {
      return Float.valueOf(stringValue);
    }
    throw new IllegalArgumentException("unexpected type: " + fieldClass);
  }
}
