/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;

import java.util.Collection;

/**
 * Common methods for writing media: extensions.
 *
 * 
 */
class ExtensionUtils {

  /**
   * Creates a standard extension description for entry tags in the
   * media: namespace.
   *
   * @param tagName
   * @param tagClass
   * @return extension description
   */
  static ExtensionDescription
      getDefaultDescription(String tagName,
      Class<? extends Extension> tagClass) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(tagClass);
    desc.setNamespace(MediaRssNamespace.NS);
    desc.setLocalName(tagName);
    return desc;
  }

  /** Adds a string attribute, unless it is null. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, String value) {
    if (value != null) {
      attrs.add(new XmlWriter.Attribute(name, value));
    }
  }

  /** Adds an integer attribute, unless it is 0. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, int value) {
    if (value != 0) {
      attrs.add(new XmlWriter.Attribute(name, Integer.toString(value)));
    }
  }

  /** Adds a long attribute, unless it is 0. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, long value) {
    if (value != 0) {
      attrs.add(new XmlWriter.Attribute(name, Long.toString(value)));
    }
  }

  /** Adds a boolean attribute. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, boolean value) {
    attrs.add(new XmlWriter.Attribute(name, value));
  }

  /** Adds an enumerated value attribute, unless it is 0. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, Enum value) {
    if (value != null) {
      attrs.add(new XmlWriter.Attribute(name, value.toString().toLowerCase()));
    }
  }

  /** Adds a NormalPlayTime attribute, unless it is null. */
  public static void addAttribute(Collection<XmlWriter.Attribute> attrs,
      String name, NormalPlayTime time) {
    if (time != null) {
      attrs.add(new XmlWriter.Attribute(name,
          time.getNptHhmmssRepresentation()));
    }
  }
}
