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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * The original video info field.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.PHOTOS_ALIAS,
    nsUri = Namespaces.PHOTOS,
    localName = GphotoOriginalVideo.XML_NAME)
public class GphotoOriginalVideo extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "originalvideo";

  /** XML "channels" attribute name */
  private static final String CHANNELS = "channels";

  /** XML "duration" attribute name */
  private static final String DURATION = "duration";

  /** XML "height" attribute name */
  private static final String HEIGHT = "height";

  /** XML "samplingrate" attribute name */
  private static final String SAMPLINGRATE = "samplingrate";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "width" attribute name */
  private static final String WIDTH = "width";

  /** Number of audio channels */
  private Integer channels = null;

  /** Video playback duration in milliseconds */
  private Long duration = null;

  /** Video height */
  private Integer height = null;

  /** Audio sample rate in kHz */
  private Float samplingrate = null;

  /** Video format type */
  private String type = null;

  /** Video width */
  private Integer width = null;

  /**
   * Default mutable constructor.
   */
  public GphotoOriginalVideo() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param channels number of audio channels.
   * @param duration video playback duration in milliseconds.
   * @param height video height.
   * @param samplingrate audio sample rate in kHz.
   * @param type video format type.
   * @param width video width.
   */
  public GphotoOriginalVideo(Integer channels, Long duration, Integer height,
      Float samplingrate, String type, Integer width) {
    super();
    setChannels(channels);
    setDuration(duration);
    setHeight(height);
    setSamplingrate(samplingrate);
    setType(type);
    setWidth(width);
    setImmutable(true);
  }

  /**
   * Returns the number of audio channels.
   *
   * @return number of audio channels
   */
  public Integer getChannels() {
    return channels;
  }

  /**
   * Sets the number of audio channels.
   *
   * @param channels number of audio channels or <code>null</code> to reset
   */
  public void setChannels(Integer channels) {
    throwExceptionIfImmutable();
    this.channels = channels;
  }

  /**
   * Returns whether it has the number of audio channels.
   *
   * @return whether it has the number of audio channels
   */
  public boolean hasChannels() {
    return getChannels() != null;
  }

  /**
   * Returns the video playback duration in milliseconds.
   *
   * @return video playback duration in milliseconds
   */
  public Long getDuration() {
    return duration;
  }

  /**
   * Sets the video playback duration in milliseconds.
   *
   * @param duration video playback duration in milliseconds or
   *     <code>null</code> to reset
   */
  public void setDuration(Long duration) {
    throwExceptionIfImmutable();
    this.duration = duration;
  }

  /**
   * Returns whether it has the video playback duration in milliseconds.
   *
   * @return whether it has the video playback duration in milliseconds
   */
  public boolean hasDuration() {
    return getDuration() != null;
  }

  /**
   * Returns the video height.
   *
   * @return video height
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * Sets the video height.
   *
   * @param height video height or <code>null</code> to reset
   */
  public void setHeight(Integer height) {
    throwExceptionIfImmutable();
    this.height = height;
  }

  /**
   * Returns whether it has the video height.
   *
   * @return whether it has the video height
   */
  public boolean hasHeight() {
    return getHeight() != null;
  }

  /**
   * Returns the audio sample rate in kHz.
   *
   * @return audio sample rate in kHz
   */
  public Float getSamplingrate() {
    return samplingrate;
  }

  /**
   * Sets the audio sample rate in kHz.
   *
   * @param samplingrate audio sample rate in kHz or <code>null</code> to reset
   */
  public void setSamplingrate(Float samplingrate) {
    throwExceptionIfImmutable();
    this.samplingrate = samplingrate;
  }

  /**
   * Returns whether it has the audio sample rate in kHz.
   *
   * @return whether it has the audio sample rate in kHz
   */
  public boolean hasSamplingrate() {
    return getSamplingrate() != null;
  }

  /**
   * Returns the video format type.
   *
   * @return video format type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the video format type.
   *
   * @param type video format type or <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the video format type.
   *
   * @return whether it has the video format type
   */
  public boolean hasType() {
    return getType() != null;
  }

  /**
   * Returns the video width.
   *
   * @return video width
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * Sets the video width.
   *
   * @param width video width or <code>null</code> to reset
   */
  public void setWidth(Integer width) {
    throwExceptionIfImmutable();
    this.width = width;
  }

  /**
   * Returns whether it has the video width.
   *
   * @return whether it has the video width
   */
  public boolean hasWidth() {
    return getWidth() != null;
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
        ExtensionDescription.getDefaultDescription(GphotoOriginalVideo.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(CHANNELS, channels);
    generator.put(DURATION, duration);
    generator.put(HEIGHT, height);
    generator.put(SAMPLINGRATE, samplingrate);
    generator.put(TYPE, type);
    generator.put(WIDTH, width);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    channels = helper.consumeInteger(CHANNELS, false);
    duration = helper.consumeLong(DURATION, false);
    height = helper.consumeInteger(HEIGHT, false);
    samplingrate = helper.consumeFloat(SAMPLINGRATE, false);
    type = helper.consume(TYPE, false);
    width = helper.consumeInteger(WIDTH, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    GphotoOriginalVideo other = (GphotoOriginalVideo) obj;
    return eq(channels, other.channels)
        && eq(duration, other.duration)
        && eq(height, other.height)
        && eq(samplingrate, other.samplingrate)
        && eq(type, other.type)
        && eq(width, other.width);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (channels != null) {
      result = 37 * result + channels.hashCode();
    }
    if (duration != null) {
      result = 37 * result + duration.hashCode();
    }
    if (height != null) {
      result = 37 * result + height.hashCode();
    }
    if (samplingrate != null) {
      result = 37 * result + samplingrate.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (width != null) {
      result = 37 * result + width.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{GphotoOriginalVideo channels=" + channels + " duration=" + duration
        + " height=" + height + " samplingrate=" + samplingrate + " type=" +
        type + " width=" + width + "}";
  }

}
