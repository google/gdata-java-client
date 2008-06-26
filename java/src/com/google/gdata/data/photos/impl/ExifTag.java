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


package com.google.gdata.data.photos.impl;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.photos.Namespaces;

/**
 * An exif tag with a given name and value.  All exif tags are represented
 * as just a name and a string value.  This element is marked "repeatable"
 * because we allow arbitrary exif tags to be passed around.
 *
 * 
 */
public class ExifTag extends ValueConstruct {

  private final String name;

  /**
   * Construct an empty exif tag with the given name.  Used when parsing
   * the values from the client.
   */
  public ExifTag(String name) {
    this(name, null);
  }

  /**
   * Construct an exif tag of &lt;ns:name&gt;value&lt;/ns:name&gt;.
   */
  public ExifTag(String name, String value) {
    super(Namespaces.EXIF_NAMESPACE, name, null, value);
    this.name = name;
    setRequired(false);
  }

  /**
   * Get the name of this exif tag.
   */
  public String getName() {
    return name;
  }

  /**
   * Describe this tag.  Does not include the name because that is set
   * at runtime, rather than at compile time.
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription extDesc = new ExtensionDescription();
    extDesc.setRepeatable(true);
    extDesc.setExtensionClass(ExifTag.class);
    extDesc.setNamespace(Namespaces.EXIF_NAMESPACE);
    extDesc.setLocalName("*");
    return extDesc;
  }
}
