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


package com.google.gdata.data.photos.pheed;

import com.google.gdata.data.ValueConstruct;

/**
 * Parent class for photo:foo extensions.
 *
 * @deprecated the pheed construct is deprecated in favor of media rss.
 *
 * 
 */
@Deprecated
public abstract class PheedConstruct extends ValueConstruct {
  public PheedConstruct(String name) {
    this(name, null);
  }

  public PheedConstruct(String name, String value) {
    super(com.google.gdata.data.photos.pheed.Namespaces.PHEED_NAMESPACE, name,
        null, value);
    setRequired(false);
    
    // We support empty strings on the wire, which the default ValueConstruct
    // doesn't, so we override the default value to be empty.
    if (value == null) {
      setValue("");
    }
  }
}
