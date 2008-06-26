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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;

/**
 * Object representation for the yt:age tag.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "age")
public class YtAge extends AbstractExtension {

  private int age;

  /** Creates an empty age tag. */
  public YtAge() {
  }

  /**
   * Creates a tag and sets the age.
   *
   * @param age age
   */
  public YtAge(int age) {
    this.age = age;
  }

  /** Sets the age. */
  public void setAge(int age) {
    this.age = age;
  }

  /** Gets the age. */
  public int getAge() {
    return age;
  }


  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (age > 0) {
      generator.setContent(Integer.toString(age));
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    try {
      age = Integer.parseInt(helper.consumeContent(true));
    } catch (NumberFormatException e) {
      throw new ParseException("Age should be an integer");
    }
  }
}
