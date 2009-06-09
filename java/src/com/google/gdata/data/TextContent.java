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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter;

import java.io.IOException;

/**
 * Variant of {@link Content} for entries containing text.
 *
 * 
 */
public class TextContent extends Content implements ITextContent {

  
  /** Class constructor. */
  public TextContent() {}

  /** Class constructor specifying the content for this element to contain. */
  public TextContent(TextConstruct content) {
    this.content = content;
  }

  /** @return the type (TEXT) of this content */
  @Override
  public int getType() { return Content.Type.TEXT; }

  /** @return  the human language that this text is written in */
  @Override
  public String getLang() {
    if (content == null) {
      return null;
    }

    return content.getLang();
  }

  /** Content. */
  protected TextConstruct content;
  /** @return the text content */
  public TextConstruct getContent() { return content; }
  /** Specifies the text content. */
  public void setContent(TextConstruct v) { content = v; }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            Extension Profile for nested extensions
   *
   * @throws  IOException
   */
  @Override
  public void generateAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (content != null) {
      content.generateAtom(w, "content");
    }
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            Extension Profile for nested extensions
   *
   * @throws  IOException
   */
  @Override
  public void generateRss(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (content != null) {
      content.generateRss(w, "description", TextConstruct.RssFormat.FULL_HTML);
    }
  }
}

