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
 * Object representation for the yt:countHint tag.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "countHint")
public class YtCountHint extends AbstractExtension {

  private int countHint;

  /** Creates an empty countHint tag. */
  public YtCountHint() {
  }

  /**
   * Creates a tag and sets the countHint.
   *
   * @param countHint countHint
   */
  public YtCountHint(int countHint) {
    this.countHint = countHint;
  }

  /** Sets the countHint. */
  public void setValue(int countHint) {
    this.countHint = countHint;
  }

  /** Gets the countHint. */
  public int getValue() {
    return countHint;
  }


  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    generator.setContent(Integer.toString(countHint));
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    try {
      countHint = Integer.parseInt(helper.consumeContent(true));
    } catch (NumberFormatException e) {
      throw new ParseException("CountHint should be an integer");
    }
  }
}
