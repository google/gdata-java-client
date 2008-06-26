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
 * Object representation for the yt:relationship tag
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "relationship")
public class YtRelationship extends AbstractExtension {

  /** Relationship value. */
  public static enum Status {
    SINGLE, TAKEN, OPEN;
  }

  private Status status;

  /** Creates an empty tag. */
  public YtRelationship() {
  }

  /** Creates a tag and initializes its content. */
  public YtRelationship(Status status) {
    this.status = status;
  }

  /**
   * Gets the relationship status.
   *
   * @return status or null
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the relationship status.
   *
   * @param status status or null
   */
  public void setStatus(Status status) {
    this.status = status;
  }


  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (status != null) {
      generator.setContent(status.toString().toLowerCase());
    }
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);
    try {
      status = Status.valueOf(helper.consumeContent(true).toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ParseException("Invalid relationship value");
    }
  }
}
