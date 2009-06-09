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
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Abstract base class for entry content.
 *
 * 
 */
public abstract class Content implements IContent {

  /** 
   * Returns this content's type.   See {@link IContent.Type} for the set of
   * expected values.
   */
  public abstract int getType();

  /** Returns the human language that this content is written in. */
  public abstract String getLang();

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            extension profile for nested extensions.
   *
   * @throws  IOException
   */
  public abstract void generateAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException;

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   * @param   extProfile
   *            extension profile for nested extensions.
   *
   * @throws  IOException
   */
  public abstract void generateRss(XmlWriter w, ExtensionProfile extProfile)
      throws IOException;

  /**
   * Parses XML in the Atom format.
   *
   * @param   extProfile
   *            ExtensionProfile used for nested content.
   * @param   attrs
   *            XML attributes of the Content node.
   *            Used to determine the type of this node.
   *
   * @return  a child handler
   *
   * @throws  ParseException
   * @throws  IOException
   */
  public static ChildHandlerInfo getChildHandler(ExtensionProfile extProfile,
      Attributes attrs) throws ParseException, IOException {

    String type = attrs.getValue("", "type");
    ChildHandlerInfo childHandlerInfo = new ChildHandlerInfo();

    String src = attrs.getValue("", "src");
    if (src == null) {
      // In-line content

      if (type == null ||
          type.equals("text") ||
          type.equals("text/plain") ||
          type.equals("html") ||
          type.equals("text/html") ||
          type.equals("xhtml")) {

        TextContent tc = new TextContent();
        TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);
        tc.setContent(chi.textConstruct);
        childHandlerInfo.handler = chi.handler;
        childHandlerInfo.content = tc;

      } else {

        OtherContent oc = new OtherContent();
        childHandlerInfo.handler = oc.new AtomHandler(extProfile, attrs);
        childHandlerInfo.content = oc;
      }
    } else {
        OutOfLineContent oolc = new OutOfLineContent();
        childHandlerInfo.handler = oolc.new AtomHandler();
        childHandlerInfo.content = oolc;
    }

    return childHandlerInfo;
  }

  /**
   * Return type for
   * {@link Content#getChildHandler(ExtensionProfile, Attributes)} contains an
   * element handler and a content element.  This is mainly because Content
   * is not actually an ExtensionPoint so things are handled manually.
   */
  public static class ChildHandlerInfo {
    public XmlParser.ElementHandler handler;
    public Content content;
  }
}
