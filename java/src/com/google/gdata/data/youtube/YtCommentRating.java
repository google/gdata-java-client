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
 * Object representation for the yt:commentRating tag.
 *
 * As YouTube does only sum up the votes (-1, 1). Therefore,
 * gd:rating can not be used (min, max range would be required).
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "commentRating")
public class YtCommentRating extends AbstractExtension {

  private int total;

  /** Creates an empty rating tag. */
  public YtCommentRating() {
  }

  /**
   * Creates a tag and sets the rating.
   *
   * @param rating 
   */
  public YtCommentRating(int rating) {
    this.total = rating;
  }

  /** Sets the sum of all ratings (-1, 1). */
  public void setTotal(int total) {
    this.total = total;
  }

  /** Gets the sum of all ratings (-1, 1). */
  public int getTotal() {
    return total;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    generator.put("total", total);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    total = helper.consumeInteger("total", false, 0);
  }
}
