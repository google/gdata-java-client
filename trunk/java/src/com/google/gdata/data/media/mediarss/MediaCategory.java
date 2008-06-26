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


package com.google.gdata.data.media.mediarss;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * {@code <media:category>}.
 *
 * See description on
 * <a href="http://search.yahoo.com/mrss">http://search.yahoo.com/mrss</a>.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "category",
    isRepeatable = true)
public class MediaCategory extends AbstractElementWithContent {

  /**
   * Default scheme as defined by the specification.
   *
   * This scheme is defined on : http://search.yahoo.com/mrss/category_schema
   */
  public static final String DEFAULT_SCHEME =
    "http://search.yahoo.com/mrss/category_schema";

  private String scheme;
  private String label;

  /** Creates an empty category tag. */
  public MediaCategory() {
  }

  /** Creates an initializes a category tag. */
  public MediaCategory(String scheme, String content) {
    this.scheme = scheme;
    setContent(content);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    generator.put("label", label);
    generator.put("scheme", scheme);
  }

  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    super.consumeAttributes(attrsHelper);
    this.label = attrsHelper.consume("label", false);
    this.scheme = attrsHelper.consume("scheme", false);
  }
}
