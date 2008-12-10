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

package com.google.gdata.data.webmastertools;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Represents list of available markup languages for mobile sitemap. The idea is
 * that this class lists all the available options for a given site and user
 * picks one of the options when submitting mobile sitemap.
 *
 * Example:
 *  <pre class="code">
 *    <feed ...>
 *        ...
 *        <wt:sitemap-mobile>
 *            <wt:markup-language>HTML</wt:markup-language>
 *            <wt:markup-language>WAP</wt:markup-language>
 *        </wt:sitemap-mobile>
 *    </feed>
 *  </pre>
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = SitemapMobile.SITEMAP_MOBILE)
public class SitemapMobile extends ExtensionPoint {

  public static final String SITEMAP_MOBILE = "sitemap-mobile";
  private static final String SITEMAP_MARKUP_LANG = "markup-language";

  public SitemapMobile() { }

  /** Add another language to the markup language list. */
  public void addMarkupLanguage(String markupLanguage) {
    MarkupLanguageConstruct val = new MarkupLanguageConstruct();
    val.setValue(markupLanguage);
    this.addRepeatingExtension(val);
  }

  /** Get all the markup languages. */
  public Collection<String> getMarkupLanguages() {
    Collection<MarkupLanguageConstruct> langs;
    langs = this.getRepeatingExtension(MarkupLanguageConstruct.class);

    ArrayList<String> ret = new ArrayList<String>(langs.size());
    for (MarkupLanguageConstruct l : langs) {
      ret.add(l.getValue());
    }

    return ret;
  }

  @Override
  public void generate(XmlWriter writer, ExtensionProfile profile)
      throws IOException {
    ExtensionDescription desc;
    
    desc = ExtensionDescription.getDefaultDescription(this.getClass());
    writer.startElement(desc.getNamespace(), desc.getLocalName(), null, null);

    generateExtensions(writer, profile);

    writer.endElement();
  }

  @Override
  public XmlParser.ElementHandler getHandler(
      ExtensionProfile extProfile,
      String namespace,
      String localName,
      Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <wt:sitemap-mobile> parser. */
  public class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile profile) {
      super(profile, SitemapMobile.class);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(
        String namespace,
        String localName,
        Attributes attrs) throws ParseException, IOException {
      // Based on the node name invoke one of the node handlers.
      if (localName == SITEMAP_MARKUP_LANG) {
        MarkupLanguageConstruct lang = new MarkupLanguageConstruct();
        addRepeatingExtension(lang);
        return lang.getHandler(null, namespace, localName, attrs);
      }

      // Handle the exceptional case, should never happen
      return super.getChildHandler(namespace, localName, attrs); // COV_NF_LINE
    }
  }

  /**
   * Represents a single GData XML node, that is a markup language enumeration
   * for mobile sitemap.
   *
   * 
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_MARKUP_LANG)
  static class MarkupLanguageConstruct extends ValueConstruct {
    public MarkupLanguageConstruct() {
      super(Namespaces.WT_NAMESPACE, SITEMAP_MARKUP_LANG, null);
    }    
  }
}
