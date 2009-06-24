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

import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * Object representation for the yt:aspectRatio tag.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "aspectRatio")
public class YtAspectRatio extends AbstractExtension {

  public enum Value {
    WIDE_SCREEN("widescreen");
    
    private final String xmlName;
    
    private Value(String xmlName) {
      this.xmlName = xmlName;
    }
    
    public String getXmlName() {
      return xmlName;
    }
  }
  
  private Value value;
  
  public YtAspectRatio() {
    value = null;
  }

  /**
   * Creates a tag and sets the aspect ratio.
   *
   * @param value the aspect ratio
   */
  public YtAspectRatio(Value value) {
    this.value = value;
  }

  /** Sets the aspect ratio. */
  public void setValue(Value value) {
    this.value = value;
  }

  /** Gets the aspect ratio. */
  public Value getValue() {
    return value;
  }
  
  @Override
  protected void generateAttributes(List<Attribute> attrs, AttributeGenerator generator) {
    super.generateAttributes(attrs, generator);
    generator.setContent(value.getXmlName());
  }
  
  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException {
    super.consumeAttributes(helper);
    String content = helper.consumeContent(true);
    for (Value value : Value.values()) {
      if (value.xmlName.equals(content)) {
        this.value = value;
        break;
      }
    }
  }
  
  @Override
  protected void validate() throws IllegalStateException {
    super.validate();
    if (value == null) {
      throw new IllegalStateException("The value of yt:aspectRatio may not be null.");
    }
  }
}
