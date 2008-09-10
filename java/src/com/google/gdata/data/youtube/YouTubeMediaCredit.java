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


package com.google.gdata.data.youtube;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.media.mediarss.MediaCredit;
import com.google.gdata.data.media.mediarss.MediaRssNamespace;
import com.google.gdata.util.ParseException;

/**
 * Adds attributes to {@code media:credit}.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "credit",
    isRepeatable = true)
public class YouTubeMediaCredit extends MediaCredit {

  /** Partner type. */
  public enum Type {
    PARTNER;
  }

  private String typeString;

  /**
   * Returns the type as an enum.
   *
   * @return a type or {@code null} if no type was set or if the
   *         type is unknown
   */
  public Type getType() {
    if (typeString == null) {
      return null;
    }
    try {
      return Type.valueOf(typeString.toUpperCase());
    } catch (IllegalArgumentException unknownType) {
      return null;
    }
  }

  /**
   * Sets the type as an enum.
   */
  public void setType(Type type) {
    this.typeString = type == null ? null : type.toString().toLowerCase();
  }

  /**
   * Returns the type as a string.
   */
  public String getTypeString() {
    return typeString;
  }

  /**
   * Sets the type as a string.
   */
  public void setTypeString(String typeString) {
    this.typeString = typeString;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);

    if (typeString != null) {
      generator.put(YouTubeNamespace.PREFIX + ":type", typeString);
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    super.consumeAttributes(attrsHelper);

    typeString = attrsHelper.consume("type", false);
  }
}
