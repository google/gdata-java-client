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
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistryBuilder;
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
import com.google.gdata.model.atom.Link.Rel;
import com.google.gdata.model.atom.Source.Generator;
import com.google.gdata.model.atompub.Edited;
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

  /**
   * Add the RSS transforms to the default metadata trees.
   */
  public static void addTransforms(MetadataRegistryBuilder registry) {
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

  private static void addCategoryTransforms(MetadataRegistryBuilder registry) {
    registry.build(Category.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "category"))
        .setVirtualValue(
            new MetadataValueTransform(Category.TERM, Category.LABEL));

    registry.build(Category.KEY, Category.SCHEME, RSS)
        .setName(new QName("domain"));

    registry.build(Category.KEY, Category.LABEL, RSS)
        .setVisible(false);

    registry.build(Category.KEY, Category.TERM, RSS)
        .setVisible(false);
  }

  private static void addContentTransforms(MetadataRegistryBuilder registry) {
    registry.build(TextContent.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "description"))
        .whitelistAttributes();
    registry.build(TextContent.CONSTRUCT, RSS)
        .whitelistAttributes();
  }

  private static void addEntryTransforms(MetadataRegistryBuilder registry) {
    registry.build(Entry.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "item"));

    registry.build(Entry.KEY, Entry.ETAG, RSS)
        .setVisible(false);

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        List<Attribute> attrs = getAttributes(e);
        if (attrs == null) {
          attrs = new ArrayList<Attribute>();
        }
        attrs.add(new Attribute("isPermaLink", "false"));

        Collection<XmlNamespace> namespaces = getNamespaces(parent, e);

        ElementMetadata<?, ?> meta = e.getMetadata();
        xw.startElement(meta.getName().getNs(), meta.getName().getLocalName(),
            attrs, namespaces);
        return true;
      }
    });
    registry.build(Entry.KEY, Entry.ID, RSS)
        .setName(new QName(Namespaces.rssNs, "guid"))
        .setProperties(properties);

    registry.build(Entry.KEY, Entry.TITLE, RSS)
        .setName(new QName(Namespaces.rssNs, "title"));

    registry.build(Entry.KEY, Entry.PUBLISHED, RSS)
        .setName(new QName(Namespaces.rssNs, "pubDate"))
        .setVirtualValue(new VirtualValue() {
          public Object generate(Element element) {
            DateTime date = element.getTextValue(Entry.PUBLISHED);
            return date == null ? "" : date.toStringRfc822();
          }

          public void parse(Element element, Object value)
              throws ParseException {
            DateTime parsed = DateTime.parseRfc822(value.toString());
            element.setTextValue(parsed);
          }
        });

    XmlWireFormatProperties personProperties = new XmlWireFormatProperties();
    personProperties.setElementGenerator(
        new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        if (!(e instanceof Person)) {
          return super.startElement(xw, parent, e);
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

        ElementMetadata<?, ?> meta = e.getMetadata();
        xw.simpleElement(meta.getName().getNs(),
            meta.getName().getLocalName(), null, text.toString());
        return false;
      }

      @Override
      public void textContent(XmlWriter xw, Element e) {}

      @Override
      public void endElement(XmlWriter xw, Element e) {}
    });

    registry.build(Entry.KEY, Author.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "author"))
        .setProperties(personProperties);

    registry.build(Entry.KEY, Contributor.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "author"))
        .setProperties(personProperties);

    registry.build(Entry.KEY, Entry.RIGHTS, RSS).setVisible(false);
  }

  private static void addEntryOutOfLineContentTransforms(
      MetadataRegistryBuilder registry) {
    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        if (!(e instanceof OutOfLineContent)) {
          return super.startElement(xw, parent, e);
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
      public void textContent(XmlWriter xw, Element e) {}

      @Override
      public void endElement(XmlWriter xw, Element e) {}
    });

    registry.build(OutOfLineContent.KEY, RSS)
        .setProperties(properties);
  }

  private static void addFeedTransforms(MetadataRegistryBuilder registry) {
    registry.build(Feed.KEY, Entry.ETAG, RSS)
        .setVisible(false);

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        Collection<XmlNamespace> namespaces = getNamespaces(parent, e);
        List<XmlWriter.Attribute> attrs = getAttributes(e);
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
      public void endElement(XmlWriter xw, Element e) throws IOException {
        xw.endElement(Namespaces.rssNs, "channel");
        super.endElement(xw, e);
      }
    });
    registry.build(Feed.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "rss"))
        .setProperties(properties);

    registry.build(Feed.KEY, Feed.UPDATED, RSS)
        .setName(new QName(Namespaces.rssNs, "lastBuildDate"))
        .setVirtualValue(new VirtualValue() {
          public Object generate(Element element) {
            DateTime date = element.getTextValue(Feed.UPDATED);
            return date == null ? "" : date.toStringRfc822();
          }

          public void parse(Element element, Object value)
              throws ParseException {
            DateTime parsed = DateTime.parseRfc822(value.toString());
            element.setTextValue(parsed);
          }
        });
  }

  private static void addLinkTransforms(MetadataRegistryBuilder registry) {
    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        if (!(e instanceof Link)) {
          return super.startElement(xw, parent, e);
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
      public void textContent(XmlWriter xw, Element e) {}

      @Override
      public void endElement(XmlWriter xw, Element e) {}
    });

    registry.build(Link.KEY, RSS)
        .setProperties(properties);
  }

  private static void addSourceTransforms(MetadataRegistryBuilder registry) {
    registry.build(Source.CONSTRUCT, Source.TITLE, RSS)
        .setName(new QName(Namespaces.rssNs, "title"));

    registry.build(Source.CONSTRUCT, Source.SUBTITLE, RSS)
        .setName(new QName(Namespaces.rssNs, "description"));

    XmlWireFormatProperties properties = new XmlWireFormatProperties();
    properties.setElementGenerator(new XmlGenerator.XmlElementGenerator() {
      @Override
      public boolean startElement(XmlWriter xw, Element parent, Element e)
          throws IOException {
        boolean isIcon = e.getElementId().equals(Source.ICON.getId());
        boolean isLogo = e.getElementId().equals(Source.LOGO.getId());
        if ((!isIcon && !isLogo) || !(parent instanceof Source)) {
          return super.startElement(xw, parent, e);
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
      public void textContent(XmlWriter xw, Element e) {}

      @Override
      public void endElement(XmlWriter xw, Element e) {}
    });
    registry.build(Source.CONSTRUCT, Source.ICON, RSS)
        .setProperties(properties);
    registry.build(Source.CONSTRUCT, Source.LOGO, RSS)
        .setProperties(properties);

    registry.build(Source.CONSTRUCT, Source.RIGHTS, RSS)
        .setVisible(true)
        .setName(new QName(Namespaces.rssNs, "copyright"));

    registry.build(Source.CONSTRUCT, Author.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "managingEditor"))
        .setVirtualValue(new MetadataValueTransform(Person.NAME));
  }

  private static void addPersonTransforms(MetadataRegistryBuilder registry) {
    registry.build(Person.KEY, Person.EMAIL, RSS)
        .setVisible(false);
    registry.build(Person.KEY, Person.NAME, RSS)
        .setVisible(false);
    registry.build(Person.KEY, Person.URI, RSS)
        .setVisible(false);
  }

  private static void addGeneratorTransforms(MetadataRegistryBuilder registry) {
    registry.build(Generator.KEY, RSS)
        .setName(new QName(Namespaces.rssNs, "generator"));

    registry.build(Generator.KEY, Generator.URI, RSS)
        .setVisible(false);
    registry.build(Generator.KEY, Generator.VERSION, RSS)
        .setVisible(false);
  }

  private static void addAppEditedTransforms(MetadataRegistryBuilder registry) {
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
