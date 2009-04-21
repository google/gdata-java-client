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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceConfigurationException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.Reader;

/**
 * OpenSearch description document.
 * See http://opensearch.a9.com/ for more information.
 *
 * 
 * 
 */
public class OpenSearchDescriptionDocument extends ExtensionPoint {

  public OpenSearchDescriptionDocument() {}

  public OpenSearchDescriptionDocument(OpenSearchDescriptionDocument doc) {
    url = doc.url;
    format = doc.format;
    shortName = doc.shortName;
    longName = doc.longName;
    description = doc.description;
    tags = doc.tags;
    image = doc.image;
    sampleSearch = doc.sampleSearch;
    developer = doc.developer;
    contact = doc.contact;
    attribution = doc.attribution;
    syndicationRight = doc.syndicationRight;
    adultContent = doc.adultContent;
  }

  /** The HTTP GET URL at which the search content can be found. */
  protected String url;
  public final String getUrl() { return url; }
  public void setUrl(String v) { url = v; }

  /** The XML schema used by the search engine specified by {@code url}. */
  protected String format;
  public final String getFormat() { return format; }
  public void setFormat(String v) { format = v; }

  /**
   * A brief name that will appear in UI elements that reference this
   * search content provider.
   */
  protected String shortName;
  public final String getShortName() { return shortName; }
  public void setShortName(String v) { shortName = v; }

  /**
   * The name by which this search content provider is referred to in
   * hypertext links, etc.
   */
  protected String longName;
  public final String getLongName() { return longName; }
  public void setLongName(String v) { longName = v; }

  /** A human readable text description of the search content provider. */
  protected String description;
  public final String getDescription() { return description; }
  public void setDescription(String v) { description = v; }

  /**
   * A space-delimited set of words that are used as keywords to identify and
   * categorize this search content.
   */
  protected String tags;
  public final String getTags() { return tags; }
  public void setTags(String v) { tags = v; }

  /**
   * A URL that identifies the location of a 64x64-pixel image that can be used
   * in association with this search engine.
   */
  protected String image;
  public final String getImage() { return image; }
  public void setImage(String v) { image = v; }

  /**
   * A search string that should be used when example searches are done against
   * this OpenSearch engine.
   */
  protected String sampleSearch;
  public final String getSampleSearch() { return sampleSearch; }
  public void setSampleSearch(String v) { sampleSearch = v; }

  /** The developer or maintainer of the OpenSearch feed. */
  protected String developer;
  public final String getDeveloper() { return developer; }
  public void setDeveloper(String v) { developer = v; }

  /** An email address at which the developer can be reached. */
  protected String contact;
  public final String getContact() { return contact; }
  public void setContact(String v) { contact = v; }

  /** A list of all content sources or platforms that should be credited. */
  protected String attribution;
  public final String getAttribution() { return attribution; }
  public void setAttribution(String v) { attribution = v; }

  /**
   * The degree to which the search results provided by this search engine can
   * be distributed.
   */
  protected int syndicationRight = SyndicationRight.OPEN;
  public final int getSyndicationRight() { return syndicationRight; }
  public void setSyndicationRight(int v) { syndicationRight = v; }

  /**
   * The SyndicationRight class defines constant values for syndication
   * rights types.
   */
  public static class SyndicationRight {

    /** Search results can be published or re-published without restriction. */
    public static final int OPEN = 0;

    /**
     * Search results can be published on the client site, but not further
     * republished.
     */
    public static final int LIMITED = 1;

    /**
     * Search feed may be queried, but the results may not be displayed at the
     * client site.
     */
    public static final int PRIVATE = 2;

    /** Search feed should not be queried. */
    public static final int CLOSED = 3;
  }

  /**
   * A boolean flag that must be set if the content provided is not suitable for
   * minors.
   */
  protected boolean adultContent = false;
  public final boolean getAdultContent() { return adultContent; }
  public void setAdultContent(boolean v) { adultContent = v; }

  /**
   * Generates XML.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  public void generate(XmlWriter w) throws IOException {

    XmlNamespace openSearchDescNs = Namespaces.getOpenSearchDescNs();

    w.startElement(openSearchDescNs, "OpenSearchDescription", null, null);

    if (url != null) {
      w.simpleElement(openSearchDescNs, "Url", null, url);
    }

    if (format != null) {
      w.simpleElement(openSearchDescNs, "Format", null, format);
    }

    if (shortName != null) {
      w.simpleElement(openSearchDescNs, "ShortName", null, shortName);
    }

    if (longName != null) {
      w.simpleElement(openSearchDescNs, "LongName", null, longName);
    }

    if (description != null) {
      w.simpleElement(openSearchDescNs, "Description", null, description);
    }

    if (tags != null) {
      w.simpleElement(openSearchDescNs, "Tags", null, tags);
    }

    if (image != null) {
      w.simpleElement(openSearchDescNs, "Image", null, image);
    }

    if (sampleSearch != null) {
      w.simpleElement(openSearchDescNs, "SampleSearch", null,
                      sampleSearch);
    }

    if (developer != null) {
      w.simpleElement(openSearchDescNs, "Developer", null,
                      developer);
    }

    if (contact != null) {
      w.simpleElement(openSearchDescNs, "Contact", null, contact);
    }

    if (attribution != null) {
      w.simpleElement(openSearchDescNs, "Attribution", null,
                      attribution);
    }

    String syndicationRightString;
    switch (syndicationRight) {
    case SyndicationRight.OPEN:
    default:
      syndicationRightString = "open";
      break;
    case SyndicationRight.LIMITED:
      syndicationRightString = "limited";
      break;
    case SyndicationRight.PRIVATE:
      syndicationRightString = "private";
      break;
    case SyndicationRight.CLOSED:
      syndicationRightString = "closed";
      break;
    }

    w.simpleElement(openSearchDescNs, "SyndicationRight", null,
                    syndicationRightString);

    if (adultContent) {
      w.simpleElement(openSearchDescNs, "AdultContent", null,
                      "true");
    }

    w.endElement(openSearchDescNs, "OpenSearchDescription");
  }

  /**
   * Validates that the OpenSearchDescriptionDocument has all required
   * properties.  The one exception is the <code>url</code> property,
   * which is dynamically generated based upon the feed that is queried.
   */
  public void validateConfiguration() throws ServiceConfigurationException {

    if (format == null)  {
      throw new ServiceConfigurationException(
          CoreErrorDomain.ERR.missingFormat);
    }

    if (shortName == null) {
      throw new ServiceConfigurationException(
          CoreErrorDomain.ERR.missingShortName);
    }

    if (description == null) {
      throw new ServiceConfigurationException(
          CoreErrorDomain.ERR.missingDescription);
    }

    if (tags == null) {
      throw new ServiceConfigurationException(
          CoreErrorDomain.ERR.missingTags);
    }

    if (contact == null) {
      throw new ServiceConfigurationException(
          CoreErrorDomain.ERR.missingContact);
    }
  }

  /**
   * Parses XML.
   *
   * @param   extProfile
   *            extension profile
   *
   * @param   reader
   *            XML input stream
   *
   * @throws   IOException
   *
   * @throws   ParseException
   */
  public void parse(ExtensionProfile extProfile, Reader reader)
      throws IOException, ParseException {

    XmlNamespace openSearchDescNs = Namespaces.getOpenSearchDescNs();
    new XmlParser().parse(reader, new Handler(extProfile),
                          openSearchDescNs.getUri(), "OpenSearchDescription");
  }

  /**
   * XmlParser ElementHandler for {@code openSearchDesc:OpenSearchDescription}
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile) {
      super(extProfile, OpenSearchDescriptionDocument.class);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      XmlNamespace openSearchDescNs = Namespaces.getOpenSearchDescNs();
      if (namespace.equals(openSearchDescNs.getUri())) {

        if (localName.equals("Url")) {

          ensureNull(url);
          return new UrlHandler();

        } else if (localName.equals("Format")) {
          ensureNull(format);
          return new FormatHandler();

        } else if (localName.equals("ShortName")) {

          ensureNull(shortName);
          return new ShortNameHandler();

        } else if (localName.equals("LongName")) {

          ensureNull(longName);
          return new LongNameHandler();

        } else if (localName.equals("Description")) {

          ensureNull(description);
          return new DescriptionHandler();

        } else if (localName.equals("Tags")) {

          ensureNull(tags);
          return new TagsHandler();

        } else if (localName.equals("Image")) {

          ensureNull(image);
          return new ImageHandler();

        } else if (localName.equals("SampleSearch")) {

          ensureNull(sampleSearch);
          return new SampleSearchHandler();

        } else if (localName.equals("Developer")) {

          ensureNull(developer);
          return new DeveloperHandler();

        } else if (localName.equals("Contact")) {

          ensureNull(contact);
          return new ContactHandler();

        } else if (localName.equals("Attribution")) {

          ensureNull(attribution);
          return new AttributionHandler();

        } else if (localName.equals("SyndicationRight")) {

          return new SyndicationRightHandler();

        } else if (localName.equals("AdultContent")) {

          return new AdultContentHandler();
        }
      } else {

        return super.getChildHandler(namespace, localName, attrs);
      }

      return null;
    }

    private void ensureNull(String s) throws ParseException {
      if (s != null) {
        throw new ParseException(
           CoreErrorDomain.ERR.duplicateValue);
      }
    }

    /** osrsDesc:Url parser. */
    private class UrlHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        url = value;
      }
    }

    /** osrsDesc:Format parser. */
    private class FormatHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        format = value;
      }
    }

    /** osrsDesc:ShortName parser. */
    private class ShortNameHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        shortName = value;
      }
    }

    /** osrsDesc:LongName parser. */
    private class LongNameHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        longName = value;
      }
    }

    /** osrsDesc:Description parser. */
    private class DescriptionHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        description = value;
      }
    }

    /** osrsDesc:Tags parser. */
    private class TagsHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        tags = value;
      }
    }

    /** osrsDesc:Image parser. */
    private class ImageHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        image = value;
      }
    }

    /** osrsDesc:SampleSearch parser. */
    private class SampleSearchHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        sampleSearch = value;
      }
    }

    /** osrsDesc:Developer parser. */
    private class DeveloperHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        developer = value;
      }
    }

    /** osrsDesc:Contact parser. */
    private class ContactHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        contact = value;
      }
    }

    /** osrsDesc:Attribution parser. */
    private class AttributionHandler extends XmlParser.ElementHandler {
      @Override
      public void processEndElement() {
        attribution = value;
      }
    }

    /** openSearchDesc:SyndicationRight parser. */
    private class SyndicationRightHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() {

        if (value.equalsIgnoreCase("open")) {
          syndicationRight = SyndicationRight.OPEN;
        } else if (value.equalsIgnoreCase("limited")) {
          syndicationRight = SyndicationRight.LIMITED;
        } else if (value.equalsIgnoreCase("private")) {
          syndicationRight = SyndicationRight.PRIVATE;
        } else if (value.equalsIgnoreCase("closed")) {
          syndicationRight = SyndicationRight.CLOSED;
        }
      }
    }

    /** openSearchDesc:AdultContent parser. */
    private class AdultContentHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() {

        if (!value.equals("false") &&
            !value.equals("FALSE") &&
            !value.equals("0") &&
            !value.equals("no") &&
            !value.equals("NO")) {

          adultContent = true;
        }
      }
    }
  }
}
