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

package com.google.api.client;

import java.util.Map;

/**
 * Utilities for working with "entities", which are defined in this library to
 * be objects that map string keys to values.
 */
public class Entities {

  /**
   * Returns the map for the given entity, which is either itself if it is
   * already a map or {@link ReflectionMap}.
   */
  public static Map<String, Object> mapOf(Object entity) {
    if (entity instanceof Map<?, ?>) {
      @SuppressWarnings("unchecked")
      Map<String, Object> result = (Map<String, Object>) entity;
      return result;
    }
    return new ReflectionMap(entity);
  }

  private Entities() {
  }
}
