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
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Object representation for the yt:rating tag.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "formatInfo")
public class YtFormatInfo extends AbstractExtension {
  
  private static final String FORMAT = "format";
  private static final String FRAME_RATE = "frameRate";
  private static final String TIME_OFFSET = "timeOffset";
  private static final String DROP_FRAME = "dropFrame";

  private String format;
  private String frameRate;
  private String timeOffset;
  private boolean dropFrame;

  
  /** Creates an empty tag. */
  public YtFormatInfo() {
    this.dropFrame = false;
  }

  /**
   * Creates a rating tag with the given attributes.
   * 
   * @param format caption file format name
   * @param frameRate frame rate of the video
   * @param timeOffset time offset to be applied to timestamps
   * @param dropFrame does the format use the dropFrame technique
   */
  public YtFormatInfo(String format, String frameRate, String timeOffset, 
      boolean dropFrame) {
    this.format = format;
    this.frameRate = frameRate;
    this.timeOffset = timeOffset;
    this.dropFrame = dropFrame;
 }

  
  /**
   * @return the format
   */
  public String getFormat() {
    return format;
  }
  
  /**
   * @param format the caption file format to set
   */
  public void setFormat(String format) {
    this.format = format;
  }
  
  
  /**
   * @return the frame rate
   */
  public String getFrameRate() {
    return frameRate;
  }
  
  /**
   * @param frameRate the frame rate to set
   */
  public void setFrameRate(String frameRate) {
    this.frameRate = frameRate;
  }
  
   
  /**
   * @return the time offset
   */
  public String getTimeOffset() {
    return timeOffset;
  }
  
  /**
   * @param timeOffset the time offset to set
   */
  public void setTimeOffset(String timeOffset) {
    this.timeOffset = timeOffset;
  }
  
   /**
   * @return whether dropFrame is used
   */
  public boolean getDropFrame() {
    return dropFrame;
  }
  
  /**
   * @param set whether dropFrame is used
   */
  public void setDropFrame(boolean dropFrame) {
    this.dropFrame = dropFrame;
  }


  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException {
    super.consumeAttributes(helper);
    format = helper.consume(FORMAT, false);
    frameRate = helper.consume(FRAME_RATE, false);
    timeOffset = helper.consume(TIME_OFFSET, false);
    dropFrame = helper.consumeBoolean(DROP_FRAME, false);
  }
  
  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (format != null) {
      generator.put(FORMAT, format);
    }
    
    if (frameRate != null) {
      generator.put(FRAME_RATE, frameRate);
    }
    
    if (timeOffset != null) {
      generator.put(TIME_OFFSET, timeOffset);
    }

    if (dropFrame) {
      generator.put(DROP_FRAME, dropFrame);
    }

  }
}
