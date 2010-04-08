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

import com.google.api.client.ClassInfo;
import com.google.api.client.Entity;
import com.google.api.client.FieldInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Implements support for HTTP form content encoding parsing of type {@code
 * application/x-www-form-urlencoded} as specified in the <a href=
 * "http://www.w3.org/TR/1998/REC-html40-19980424/interact/forms.html#h-17.13.4.1"
 * >HTML 4.0 Specification</a>.
 */
public final class UrlEncodedFormHttpParser implements HttpParser {

  /**
   * Disables logging of content, for example if content has sensitive data such
   * as an authentication token. Defaults to {@code false}.
   */
  public volatile boolean disableContentLogging;

  public UrlEncodedFormHttpParser() {
  }

  public String getContentType() {
    return "application/x-www-form-urlencoded";
  }

  public <T> T parse(HttpResponse response, Class<T> entityClass)
      throws IOException {
    T newInstance = ClassInfo.newInstance(entityClass);
    ClassInfo classInfo = ClassInfo.of(entityClass);
    if (!this.disableContentLogging) {
      response.disableContentLogging = true;
    }
    parse(response.parseAsString(), newInstance);
    return newInstance;
  }

  public static void parse(String content, Object object) {
    Class<?> clazz = object.getClass();
    ClassInfo classInfo = ClassInfo.of(clazz);
    Entity entity =
        Entity.class.isAssignableFrom(clazz) ? (Entity) object : null;
    @SuppressWarnings("unchecked")
    Map<Object, Object> map =
        Map.class.isAssignableFrom(clazz) ? (Map<Object, Object>) object : null;
    int cur = 0;
    int length = content.length();
    while (cur < length) {
      int amp = content.indexOf('&', cur);
      if (amp == -1) {
        amp = length;
      }
      int equals = content.indexOf('=', cur);
      if (equals <= cur || equals >= amp - 1) {
        throw new IllegalArgumentException("malformed URL encoding: "
            + content.substring(cur, amp));
      }
      String name = content.substring(cur, equals);
      String value = content.substring(equals + 1, amp);
      // get the field from the type information
      Field field = classInfo.getField(name);
      if (field != null) {
        Class<?> fieldClass = field.getType();
        Object fieldValue;
        if (fieldClass == boolean.class || fieldClass == Boolean.class) {
          fieldValue = Boolean.valueOf(value);
        } else {
          fieldValue = value;
        }
        FieldInfo.setFieldValue(field, object, fieldValue);
      } else if (entity != null) {
        entity.set(name, value);
      } else if (map != null) {
        map.put(name, value);
      }
      cur = amp + 1;
    }
  }
}
