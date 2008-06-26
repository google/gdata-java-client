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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;

/**
 * Object representation for the yt:gender tag
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "gender")
public class YtGender extends AbstractExtension {

  /** Gender Value. */
  public static enum Value {
    MALE("m"), FEMALE("f");

    private String id;

    private Value(String id) {
      this.id = id;
    }

    /** Returns a one-letter identifier. */
    public String getId() {
      return id;
    }

    /**
     * Returns the value corresponding to an id.
     *
     * @param id identifier, 'm' or 'f'
     * @return a value or null
     */
    public static Value fromId(String id) {
      for (Value gender : values()) {
        if (gender.getId().equals(id)) {
          return gender;
        }
      }
      return null;
    }
  }

  private Value gender;

  /** Creates an empty tag. */
  public YtGender() {
  }

  /**
   * Creates a tag and initializes its content.
   *
   * @param gender {@link Value#FEMALE}, {@link Value#MALE} or {@code null}
   */
  public YtGender(Value gender) {
    this.gender = gender;
  }

  /**
   * Gets the gender.
   *
   * @return {@link Value#FEMALE}, {@link Value#MALE} or {@code null}
   */
  public Value getGender() {
    return gender;
  }

  /**
   * Sets the gender.
   *
   * @param gender {@link Value#FEMALE}, {@link Value#MALE} or {@code null}
   */
  public void setGender(Value gender) {
    this.gender = gender;
  }


  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (gender != null) {
      generator.setContent(gender.getId());
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    gender = Value.fromId(helper.consumeContent(true));
    if (gender == null) {
      // It is necessarily null because the value is invalid, since
      // consumeContent(true) requires something to be there.
      throw new ParseException("Unknown gender. It should be m or f.");
    }
  }
}
