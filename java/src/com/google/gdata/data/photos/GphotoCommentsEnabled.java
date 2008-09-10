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


package com.google.gdata.data.photos;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * True if the album allows commenting.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.PHOTOS_ALIAS,
    nsUri = Namespaces.PHOTOS,
    localName = GphotoCommentsEnabled.XML_NAME)
public class GphotoCommentsEnabled extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "commentingEnabled";

  /** Whether the album allows comments */
  private Boolean value = null;

  /**
   * Default mutable constructor.
   */
  public GphotoCommentsEnabled() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value whether the album allows comments.
   */
  public GphotoCommentsEnabled(Boolean value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the whether the album allows comments.
   *
   * @return whether the album allows comments
   */
  public Boolean getValue() {
    return value;
  }

  /**
   * Sets the whether the album allows comments.
   *
   * @param value whether the album allows comments or <code>null</code> to
   *     reset
   */
  public void setValue(Boolean value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the whether the album allows comments.
   *
   * @return whether it has the whether the album allows comments
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(GphotoCommentsEnabled.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.setContent(value.toString());
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    String unsanitizedValue = helper.consume(null, false);

    // This is a hack due to Panasonic shipping cameras that send "ture" instead
    // of true for this parameter.
    if (unsanitizedValue == null) {
      value = false;
    } else if ("true".equals(unsanitizedValue) ||
        "1".equals(unsanitizedValue) ||
        "ture".equals(unsanitizedValue)) {
      value = true;
    } else if ("false".equals(unsanitizedValue) ||
        "0".equals(unsanitizedValue)) {
      value = false;
    } else {
      throw new ParseException("Invalid boolean value for attribute: '" +
          unsanitizedValue + "'");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    GphotoCommentsEnabled other = (GphotoCommentsEnabled) obj;
    return eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{GphotoCommentsEnabled value=" + value + "}";
  }

}
