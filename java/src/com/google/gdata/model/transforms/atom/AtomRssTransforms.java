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


package com.google.gdata.model.transforms.atom;

import static com.google.gdata.model.MetadataContext.RSS;

import com.google.common.collect.Lists;
import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ILink.Rel;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.MetadataValueTransform;
import com.google.gdata.model.QName;
import com.google.gdata.model.Metadata.VirtualValue;
import com.google.gdata.model.atom.Author;
import com.google.gdata.model.atom.Category;
import com.google.gdata.model.atom.Content;
import com.google.gdata.model.atom.Contributor;
import com.google.gdata.model.atom.Entry;
import com.google.gdata.model.atom.Feed;
import com.google.gdata.model.atom.Link;
import com.google.gdata.model.atom.OutOfLineContent;
import com.google.gdata.model.atom.Person;
import com.google.gdata.model.atom.Source;
import com.google.gdata.model.atom.TextContent;
import com.google.gdata.model.atom.Source.Generator;
import com.google.gdata.model.atompub.Edited;
import com.google.gdata.model.gd.GdAttributes;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.XmlGenerator;
import com.google.gdata.wireformats.XmlWireFormatProperties;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * RSS transforms for atom.  This contains the transforms required to output
 * atom classes in RSS using the new data model.
 *
 * 
 */
public class AtomRssTransforms {

  // Constants for our rss names.
  private static final QName DOMAIN = new QName("domain");
  private static final QName CATEGORY = new QName(Namespaces.rssNs, "category");
  private static final QName ITEM = new QName(Namespaces.rssNs, "item");
  private static final QName GUID = new QName(Namespaces.rssNs, "guid");
  private static final QName TITLE = new QName(Namespaces.rssNs, "title");
  private static final QName PUB_DATE = new QName(Namespaces.rssNs, "pubDate");
  private static final QName AUTHOR = new QName(Namespaces.rssNs, "author");
  private static final QName RSS_NAME = new QName(Namespaces.rssNs, "rss");
  private static final QName DESCRIPTION =
    new QName(Namespaces.rssNs, "description");
  private static final QName LAST_BUILD_DATE =
      new QName(Namespaces.rssNs, "lastBuildDate");
  private static final QName COPYRIGHT =
      new QName(Namespaces.rssNs, "copyright");
  private static final QName MANAGING_EDITOR =
      new QName(Namespaces.rssNs, "managingEditor");
  private static final QName GENERATOR =
      new QName(Namespaces.rssNs, "generator");

  /**
   * Add the RSS transforms to the default metadata trees.
   */
  public static void addTransforms(MetadataRegistry registry) {
    addCategoryTransforms(registry);
    addContentTransforms(registry);
    addEntryTransforms(registry);
    addEntryOutOfLineContentTransforms(registry);
    addFeedTransforms(registry);
    addLinkTransforms(registry);
    addSourceTransforms(registry);
    addPersonTransforms(registry);
    addGeneratorTransforms(registry);
    addAppEditedTransforms(registry);
  }

  private static void addCategoryTransforms(MetadataRegistry registry) {
    registry.build(Category.KEY, RSS)
        .setName(CATEGORY)
        .setVirtualValue(
            new MetadataValueTransform(Category.TERM, Category.LABEL));

    registry.build(Category.KEY, Category.SCHEME, RSS)
        .setName(DOMAIN);

    registry.build(Category.KEY, Category.LABEL, RSS)
        .setVisible(false);

    registry.build(Category.KEY, Category.TERM, RSS)
        .setVisible(false);
  }

  private static void addContentTransforms(MetadataRegistry registry) {
    registry.build(TextContent.KEY, RSS)
        .setName(DESCRIPTION)
        .whitelistAttributes();
    registry.build(TextContent.CONSTRUCT, RSS)
        .whitelistAttributes();
  }

  private static void addEntryTransforms(MetadataRegistry registry) {
    registry.build(Entry.KEY, RSS).setName(ITEM);

    registry.build(Entry.KEY, GdAttributes.ETAG, RSS)
        .setVisible(false);

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      protected List<Attribute> getAttributes(Element e,
          ElementMetadata<?, ?> metadata) {
        List<Attribute> attrs = super.getAttributes(e, metadata);
        if (attrs == null) {
          attrs = Lists.newArrayListWithExpectedSize(1);
        }
        attrs.add(new Attribute("isPermaLink", "false"));
        return attrs;
      }
    });
    registry.build(Entry.KEY, Entry.ID, RSS)
        .setName(GUID)
        .setProperties(properties);

    registry.build(Entry.KEY, Entry.TITLE, RSS)
        .setName(TITLE);

    registry.build(Entry.KEY, Entry.PUBLISHED, RSS)
        .setName(PUB_DATE)
        .setVirtualValue(new VirtualValue() {
          public Object generate(Element element,
              ElementMetadata<?, ?> metadata) {
            DateTime date = element.getTextValue(Entry.PUBLISHED);
            return date == null ? "" : date.toStringRfc822();
          }

          public void parse(Element element, ElementMetadata<?, ?> metadata,
              Object value) throws ParseException {
            DateTime parsed = DateTime.parseRfc822(value.toString());
            element.setTextValue(parsed);
          }
        });

    XmlWireFormatProperties personProperties = new XmlWireFormatProperties();
    personProperties.setElementGenerator(
        new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {
        if (!(e instanceof Person)) {
          return super.startElement(xw, parent, e, metadata);
        }

        Person person = (Person) e;
        String email = person.getEmail();
        String name = person.getName();

        StringBuilder text = new StringBuilder();
        boolean hasEmail = email != null;

        if (hasEmail) {
          text.append(email);
        }

        if (name != null) {
          if (hasEmail) {
            text.append(" (");
          }
          text.append(name);
          if (hasEmail) {
            text.append(")");
          }
        }

        QName xmlName = getName(e, metadata);
        xw.simpleElement(xmlName.getNs(), xmlName.getLocalName(),
            null, text.toString());
        return false;
      }

      @Override
      public void textContent(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}

      @Override
      public void endElement(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}
    });

    registry.build(Entry.KEY, Author.KEY, RSS)
        .setName(AUTHOR)
        .setProperties(personProperties);

    registry.build(Entry.KEY, Contributor.KEY, RSS)
        .setName(AUTHOR)
        .setProperties(personProperties);

    registry.build(Entry.KEY, Entry.RIGHTS, RSS).setVisible(false);
  }

  private static void addEntryOutOfLineContentTransforms(
      MetadataRegistry registry) {
    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {
        if (!(e instanceof OutOfLineContent)) {
          return super.startElement(xw, parent, e, metadata);
        }

        OutOfLineContent content = (OutOfLineContent) e;
        ContentType type = content.getMimeType();
        URI src = content.getSrc();
        generateEnclosure(xw,
            type == null ? null : type.getMediaType(),
            src == null ? null : src.toString(),
            content.getLength());
        return false;
      }

      @Override
      public void textContent(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}

      @Override
      public void endElement(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}
    });

    registry.build(OutOfLineContent.KEY, RSS)
        .setProperties(properties);
  }

  private static void addFeedTransforms(final MetadataRegistry registry) {
    registry.build(Feed.KEY, GdAttributes.ETAG, RSS)
        .setVisible(false);

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {

        Collection<XmlNamespace> namespaces =
            getNamespaces(parent, e, metadata);
        List<XmlWriter.Attribute> attrs = getAttributes(e, metadata);
        if (attrs == null) {
          attrs = Lists.newArrayList();
        }
        attrs.add(new Attribute("version", "2.0"));

        xw.startElement(Namespaces.rssNs, "rss", attrs, namespaces);
        xw.startElement(Namespaces.rssNs, "channel", null, null);

        if (!e.hasElement(Source.SUBTITLE) && !e.hasElement(Content.KEY)) {
          // To preserve strict RSS2 compliance and backward compatibility:
          // since atom feeds do not require a subtitle or text content, put in
          // an empty description into RSS. This keeps RSS2 validators happy
          xw.simpleElement(Namespaces.rssNs, "description", null, "");
        }
        return true;
      }

      @Override
      public void endElement(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {
        xw.endElement(Namespaces.rssNs, "channel");
        super.endElement(xw, e, metadata);
      }
    });
    registry.build(Feed.KEY, RSS)
        .setName(RSS_NAME)
        .setProperties(properties);

    registry.build(Feed.KEY, Feed.UPDATED, RSS)
        .setName(LAST_BUILD_DATE)
        .setVirtualValue(new VirtualValue() {
          public Object generate(Element element,
              ElementMetadata<?, ?> metadata) {
            DateTime date = element.getTextValue(Feed.UPDATED);
            return date == null ? "" : date.toStringRfc822();
          }

          public void parse(Element element, ElementMetadata<?, ?> metadata,
              Object value) throws ParseException {
            DateTime parsed = DateTime.parseRfc822(value.toString());
            element.setTextValue(parsed);
          }
        });
  }

  private static void addLinkTransforms(MetadataRegistry registry) {
    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {
        if (!(e instanceof Link)) {
          return super.startElement(xw, parent, e, metadata);
        }
        Link link = (Link) e;
        String rel = link.getRel();
        String type = link.getType();
        String href = link.getHref();
        long length = link.getLength();

        if (rel != null && rel.equals("enclosure")) {
          generateEnclosure(xw, type, href, length);
        } else if ("comments".equals(rel)) {
          xw.simpleElement(Namespaces.rssNs, "comments", null, href);

        } else if (Rel.ALTERNATE.equals(rel)) {
          xw.simpleElement(Namespaces.rssNs, "link", null, href);

        } else if (Rel.VIA.equals(rel)) {
          if (href != null) {
            List<XmlWriter.Attribute> attrs =
                Collections.singletonList(new XmlWriter.Attribute("url", href));
            xw.simpleElement(Namespaces.rssNs, "source", attrs, null);
          }
        }
        return false;
      }

      @Override
      public void textContent(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}

      @Override
      public void endElement(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}
    });

    registry.build(Link.KEY, RSS)
        .setProperties(properties);
  }

  private static void addSourceTransforms(MetadataRegistry registry) {
    registry.build(Source.CONSTRUCT, Source.TITLE, RSS).setName(TITLE);

    registry.build(Source.CONSTRUCT, Source.SUBTITLE, RSS).setName(DESCRIPTION);

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e,
          ElementMetadata<?, ?> metadata) throws IOException {
        boolean isIcon = e.getElementId().equals(Source.ICON.getId());
        boolean isLogo = e.getElementId().equals(Source.LOGO.getId());

        if ((!isIcon && !isLogo) || !(parent instanceof Source)) {
          return super.startElement(xw, parent, e, metadata);
        }

        Source source = (Source) parent;

        if (isIcon && source.hasElement(Source.LOGO)) {
          // atom:logo takes precedence
          return false;
        }

        xw.startElement(Namespaces.rssNs, "image", null, null);
        xw.simpleElement(Namespaces.rssNs, "url", null,
            String.valueOf(e.getTextValue()));

        TextContent title = source.getTitle();
        if (title != null) {
          xw.simpleElement(Namespaces.rssNs, "title", null,
              title.getPlainText());
        }
        Link htmlLink = source.getHtmlLink();
        if (htmlLink != null) {
          xw.simpleElement(Namespaces.rssNs, "link", null, htmlLink.getHref());
        }
        xw.endElement(Namespaces.rssNs, "image");

        return false;
      }

      @Override
      public void textContent(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}

      @Override
      public void endElement(XmlWriter xw, Element e,
          ElementMetadata<?, ?> metadata) {}
    });
    registry.build(Source.CONSTRUCT, Source.ICON, RSS)
        .setProperties(properties);
    registry.build(Source.CONSTRUCT, Source.LOGO, RSS)
        .setProperties(properties);

    registry.build(Source.CONSTRUCT, Source.RIGHTS, RSS)
        .setVisible(true)
        .setName(COPYRIGHT);

    registry.build(Source.CONSTRUCT, Author.KEY, RSS)
        .setName(MANAGING_EDITOR)
        .setVirtualValue(new MetadataValueTransform(Person.NAME));
  }

  private static void addPersonTransforms(MetadataRegistry registry) {
    registry.build(Person.KEY, Person.EMAIL, RSS)
        .setVisible(false);
    registry.build(Person.KEY, Person.NAME, RSS)
        .setVisible(false);
    registry.build(Person.KEY, Person.URI, RSS)
        .setVisible(false);
  }

  private static void addGeneratorTransforms(MetadataRegistry registry) {
    registry.build(Generator.KEY, RSS)
        .setName(GENERATOR);

    registry.build(Generator.KEY, Generator.URI, RSS)
        .setVisible(false);
    registry.build(Generator.KEY, Generator.VERSION, RSS)
        .setVisible(false);
  }

  private static void addAppEditedTransforms(MetadataRegistry registry) {
    registry.build(Edited.KEY, RSS).setVisible(false);
  }

  private static void generateEnclosure(XmlWriter xw, String type, String href,
      long length) throws IOException {
    List<XmlWriter.Attribute> attrs =
        new ArrayList<XmlWriter.Attribute>(3);

    if (type != null) {
      attrs.add(new XmlWriter.Attribute("type", type));
    }

    if (href != null) {
      attrs.add(new XmlWriter.Attribute("url", href));
    }

    // To preserve strict RSS2 compliance and backward compatibility: Since RSS
    // requires a length, then put it length=0 when the length is unknown.  This
    // keeps RSS2 validators happy
    attrs.add(new XmlWriter.Attribute(
        "length", Long.toString(length == -1 ? 0 : length)));

    xw.simpleElement(Namespaces.rssNs, "enclosure", attrs, null);
  }

  private AtomRssTransforms() {}
}
