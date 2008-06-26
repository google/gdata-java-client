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
import com.google.gdata.util.ParseException;

/**
 * An extension with a content and nothing else.
 *
 * 
 */
public abstract class AbstractFreeTextExtension extends AbstractExtension {
  private String content;

  /** Creates an empty tag. */
  protected AbstractFreeTextExtension() {
  }

  /**
   * Creates a tag and initializes its content.
   *
   * @param content
   */
  protected AbstractFreeTextExtension(String content) {
    this.content = content;
  }

  /** Gets the content string. */
  public String getContent() {
    return content;
  }

  /** Sets the content string. */
  public void setContent(String content) {
    this.content = content;
  }


  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);

    if (content != null) {
      generator.setContent(content);
    }
  }


  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    super.consumeAttributes(helper);

    // Accept empty text
    content = helper.consumeContent(false);
    if (content == null) {
      content = "";
    }
  }
}
