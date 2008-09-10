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

import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.util.ParseException;

/**
 * {@code <media:text>}.
 *
 * See description on
 * <a href="http://search.yahoo.com/mrss">http://search.yahoo.com/mrss</a>.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = MediaRssNamespace.PREFIX,
    nsUri = MediaRssNamespace.URI,
    localName = "text")
public class MediaText extends AbstractTextElement {
  private NormalPlayTime start;
  private NormalPlayTime end;
  private String lang;

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public NormalPlayTime getEnd() {
    return end;
  }

  public void setEnd(NormalPlayTime end) {
    this.end = end;
  }

  public NormalPlayTime getStart() {
    return start;
  }

  public void setStart(NormalPlayTime start) {
    this.start = start;
  }

  @Override
  public void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    if (start != null) {
      generator.put("start", start.getNptHhmmssRepresentation());
    }
    if (end != null) {
      generator.put("end", end.getNptHhmmssRepresentation());
    }
    generator.put("lang", lang);
  }

  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    super.consumeAttributes(attrsHelper);
    start = MediaAttributeHelper.consumeNormalPlayTime(attrsHelper, "start");
    end = MediaAttributeHelper.consumeNormalPlayTime(attrsHelper, "end");
    lang = attrsHelper.consume("lang", false);
  }
}

