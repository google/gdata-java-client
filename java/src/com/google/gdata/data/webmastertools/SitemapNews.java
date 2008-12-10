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
 * Represents list of available publication labels for News sitemap. The idea is
 * that this class lists all the available options for a given site and user
 * picks one of the options when submitting News sitemap.
 *
 * Example:
 *   <pre class="code">
 *     <feed ...>
 *       ...
 *       <wt:sitemap-news>
 *         <wt:publication-label>Label1</wt:publication-label>
 *         <wt:publication-label>Label2</wt:publication-label>
 *       </wt:sitemap-news>
 *    </feed>
 *  </pre>
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = SitemapNews.SITEMAP_NEWS)
public class SitemapNews extends ExtensionPoint {

  public static final String SITEMAP_NEWS = "sitemap-news";
  private static final String SITEMAP_PUBLICATION_LABEL = "publication-label";

  public SitemapNews() { }

  /** Add another language to the enumeration. */
  public void addPublicationLabel(String label) {
    PublicationLabelConstruct val = new PublicationLabelConstruct();
    val.setValue(label);
    this.addRepeatingExtension(val);
  }

  /** Get all the languages. */
  public Collection<String> getPublicationLabels() {
    Collection<PublicationLabelConstruct> labels;
    labels = this.getRepeatingExtension(PublicationLabelConstruct.class);

    ArrayList<String> ret = new ArrayList<String>(labels.size());
    for (PublicationLabelConstruct l : labels) {
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

  /** <wt:sitemap-news> parser. */
  public class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile profile) {
      super(profile, SitemapNews.class);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(
        String namespace,
        String localName,
        Attributes attrs) throws ParseException, IOException {
      // Based on the node name invoke one of the node handlers.
      if (localName == SITEMAP_PUBLICATION_LABEL) {
        PublicationLabelConstruct label = new PublicationLabelConstruct();
        addRepeatingExtension(label);
        return label.getHandler(null, namespace, localName, attrs);
      }

      // Handle the exceptional case, should never happen
      return super.getChildHandler(namespace, localName, attrs); // COV_NF_LINE
    }
  }

  /**
   * Represents a single GData XML node, that is a publication label for news
   * sitemap.
   *
   * 
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SITEMAP_PUBLICATION_LABEL)  
  static class PublicationLabelConstruct extends ValueConstruct {
    public PublicationLabelConstruct() {
      super(Namespaces.WT_NAMESPACE, SITEMAP_PUBLICATION_LABEL, null);
    }    
  }
}
