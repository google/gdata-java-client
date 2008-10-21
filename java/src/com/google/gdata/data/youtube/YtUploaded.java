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
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Video upload time tag inside "media:group": "yt:uploaded".
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "uploaded")
public class YtUploaded extends AbstractExtension {
  private DateTime dateTime;
  
  public YtUploaded() {
  }

  public YtUploaded(DateTime dateTime) {
    this.dateTime = dateTime;
  }
  
  /**
   * Returns the currently set uploaded date time or null if none set.
   */
  public DateTime getDateTime() {
    return dateTime;
  }
  
  /**
   * Changes the uploaded date time.
   */
  public void setDateTime(DateTime dateTime) {
    this.dateTime = dateTime;
  }
  
  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException {
    super.consumeAttributes(helper);

    try {
      dateTime = DateTime.parseDateTime(helper.consumeContent(true));
    } catch (NumberFormatException e) {
      throw new ParseException("Invalid date time format.");
    }
  }
  
  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (dateTime != null) {
      generator.setContent(dateTime.toString());
    }
  }
}
