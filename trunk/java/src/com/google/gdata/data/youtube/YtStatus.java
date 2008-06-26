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
 * Object representation for the yt:status tag on the user contact feed.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "status")
public class YtStatus extends AbstractExtension {

  /** Status value. */
  public static enum Value {
    PENDING, ACCEPTED, REJECTED, REQUESTED
  }

  private YtStatus.Value status;

  /** Creates an empty tag. */
  public YtStatus() {
  }

  /** Creates a tag and initializes its content. */
  public YtStatus(YtStatus.Value status) {
    this.status = status;
  }

  /**
   * Gets the status status.
   *
   * @return status or null
   */
  public YtStatus.Value getStatus() {
    return status;
  }

  /**
   * Sets the status status.
   *
   * @param status status or null
   */
  public void setStatus(YtStatus.Value status) {
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
      status =
          YtStatus.Value.valueOf(helper.consumeContent(true).toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ParseException("Invalid status value");
    }
  }
}
