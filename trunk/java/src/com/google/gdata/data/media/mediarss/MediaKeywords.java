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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Collection;

/**
 * {@code <media:keywords>}.
 *
 * See description on
 * <a href="http://search.yahoo.com/mrss">http://search.yahoo.com/mrss</a>.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "keywords"
)
public class MediaKeywords extends AbstractExtension {
  private final List<String> keywords = new ArrayList<String>();

  /** Describes the tag to an {@link com.google.gdata.data.ExtensionProfile}. */
  public static ExtensionDescription getDefaultDescription() {
    return ExtensionDescription.getDefaultDescription(MediaKeywords.class);
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void addKeyword(String keyword) {
    keywords.add(keyword);
  }

  public void addKeywords(Collection<String> keywords) {
    this.keywords.addAll(keywords);
  }

  public void clearKeywords() {
    keywords.clear();
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    StringBuffer content = new StringBuffer();
    boolean isFirst = true;
    for (String keyword: keywords) {
      if (isFirst) {
        isFirst = false;
      } else {
        content.append(", ");
      }
      content.append(keyword);
    }

    generator.setContent(content.toString());
  }

  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper) 
      throws ParseException {
    String value = attrsHelper.consumeContent(false);
    keywords.clear();
    if (value != null) {
      StringTokenizer tokenizer = new StringTokenizer(value, ",");
      while(tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken().trim();
        keywords.add(token);
      }
    }
  }
}
