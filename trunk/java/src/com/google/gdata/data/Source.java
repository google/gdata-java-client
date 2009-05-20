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
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Source class represents an Atom feed source object
 * primarily on the data model for an {@code <atom:source>}
 * element.
 *
 * Here is the Relax-NG schema that represents an Atom 1.0
 * Source:
 * <pre>
 * atomSource =
 *   element atom:source {
 *     atomCommonAttributes,
 *     (atomAuthor*
 *      & atomCategory*
 *      & atomContributor*
 *      & atomGenerator?
 *      & atomIcon?
 *      & atomId?
 *      & atomLink*
 *      & atomLogo?
 *      & atomRights?
 *      & atomSubtitle?
 *      & atomTitle?
 *      & atomUpdated?
 *      & extensionElement*)
 *   }
 * </pre>
 *
 * 
 * 
 */
public class Source extends ExtensionPoint {

  /**
   * The SourceState class provides a simple structure that encapsulates
   * the attributes of an Atom source that should be shared with a shallow
   * copy if the entry is adapted to a more specific Source
   * {@link Kind.Adaptor} subtypes.
   *
   * @see Source#Source(Source)
   */
  protected static class SourceState {

    /** Feed ID. */
    public  String id;

    /** Last updated timestamp. */
    public DateTime updated;

    /** Categories. */
    public HashSet<Category> categories = new HashSet<Category>();

    /** Title. */
    public TextConstruct title;

    /** Subtitle. */
    public TextConstruct subtitle;

    /** Rights. */
    public TextConstruct rights;

    /** Icon URI. */
    public String icon;

    /** Logo image URI. */
    public String logo;

    /** Links. */
    public LinkedList<Link> links = new LinkedList<Link>();

    /** Authors. */
    public LinkedList<Person> authors = new LinkedList<Person>();

    /** Contributors. */
    public LinkedList<Person> contributors = new LinkedList<Person>();

    /** Generator. */
    public Generator generator;
  }

  /**
   * Basic state for this source.   May be shared across multiple adapted
   * instances associated with the same logical source.
   */
  protected SourceState srcState;

  /**
   * Constructs a new {@link Source} instance with no initial state.
   */
  public Source() {
    srcState = new SourceState();
  }

  /**
   * Copy constructor that initializes a new Source instance to have
   * identical contents to another instance, using a shared reference to
   * the same {@link SourceState}.
   */
  protected Source(Source sourceSource) {
    super(sourceSource);
    srcState = sourceSource.srcState;
  }

  public String getId() { return srcState.id; }
  public void setId(String v) { srcState.id = v; }

  public DateTime getUpdated() { return srcState.updated; }
  public void setUpdated(DateTime v) { srcState.updated = v; }

  public Set<Category> getCategories() { return srcState.categories; }

  public TextConstruct getTitle() { return srcState.title; }
  public void setTitle(TextConstruct v) { srcState.title = v; }

  public TextConstruct getSubtitle() { return srcState.subtitle; }
  public void setSubtitle(TextConstruct v) { srcState.subtitle = v; }

  public TextConstruct getRights() { return srcState.rights; }
  public void setRights(TextConstruct v) { srcState.rights = v; }

  public String getIcon() { return srcState.icon; }
  public void setIcon(String v) { srcState.icon = v; }

  public String getLogo() { return srcState.logo; }
  public void setLogo(String v) { srcState.logo = v; }

  public List<Link> getLinks() { return srcState.links; }

  public List<Person> getAuthors() { return srcState.authors; }

  public List<Person> getContributors() { return srcState.contributors; }

  public Generator getGenerator() { return srcState.generator; }
  public void setGenerator(Generator v) { srcState.generator = v; }

  public Generator setGenerator(String version, String uri, String name) {
    Generator gen = new Generator();
    gen.setVersion(version);
    gen.setUri(uri);
    gen.setName(name);
    setGenerator(gen);
    return gen;
  }
  
  /**
   * Retrieves the first link with the supplied {@code rel} and/or
   * {@code type} value.
   * <p>
   * If either parameter is {@code null}, doesn't return matches
   * for that parameter.
   */
  public Link getLink(String rel, String type) {

    for (Link link: srcState.links) {
      if (link.matches(rel, type)) {
        return link;
      }
    }

    return null;
  }

  /**
   * Return the links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   * @return matching links.
   */
  public List<Link> getLinks(String relToMatch, String typeToMatch) {
    List<Link> result = new ArrayList<Link>();
    for (Link link : srcState.links) {
      if (link.matches(relToMatch, typeToMatch)) {
        result.add(link);
      }
    }
    return result;
  }

  public void addLink(Link link) {
    srcState.links.add(link);
  }
  
  public Link addLink(String rel, String type, String href) {
    Link link = new Link(rel, type, href);
    addLink(link);
    return link;
  }

  /**
   * Remove all links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   */
  public void removeLinks(String relToMatch, String typeToMatch) {
    for (Iterator<Link> iterator = srcState.links.iterator();
        iterator.hasNext();) {
      Link link = iterator.next();
      if (link.matches(relToMatch, typeToMatch)) {
        iterator.remove();
      }
    }
  }
  
  /**
   * Removes all links.
   */
  public void removeLinks() {
    srcState.links.clear();
  }

  /**
   * Adds a link pointing to an HTML representation.
   *
   * @param   htmlUri
   *            link URI
   *
   * @param   lang
   *            optional language code
   *
   * @param   title
   *            optional title
   */
  public void addHtmlLink(String htmlUri, String lang, String title) {

    Link link = new Link();
    link.setRel(Link.Rel.ALTERNATE);
    link.setType(Link.Type.HTML);
    link.setHref(htmlUri);

    if (lang != null) {
      link.setHrefLang(lang);
    }

    if (title != null) {
      link.setTitle(title);
    }

    srcState.links.add(link);
  }

  /**
   * Retrieves the first HTML link.
   *
   * @return   the link
   */
  public Link getHtmlLink() {
    Link htmlLink = getLink(Link.Rel.ALTERNATE, Link.Type.HTML);
    return htmlLink;
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   *
   * @param   extProfile
   *            extension profile
   *
   * @throws  IOException
   */
  public void generateAtom(XmlWriter w,
                           ExtensionProfile extProfile) throws IOException {

    generateStartElement(w, Namespaces.atomNs, "source",
                         null, null);

    // Generate inner content
    generateInnerAtom(w, extProfile);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.atomNs, "source");
  }

  /**
   * Generates inner XML content in the Atom format.
   *
   * @param   w
   *            output writer
   *
   * @param   extProfile
   *            extension profile
   *
   * @throws  IOException
   */
  protected void generateInnerAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    if (srcState.id != null) {
      w.simpleElement(Namespaces.atomNs, "id", null, srcState.id);
    }

    if (srcState.updated != null) {
      w.simpleElement(Namespaces.atomNs, "updated", null,
          srcState.updated.toString());
    }

    w.startRepeatingElement();
    for (Category cat: srcState.categories) {
      cat.generateAtom(w);
    }
    w.endRepeatingElement();

    if (srcState.title != null) {
      srcState.title.generateAtom(w, "title");
    }

    if (srcState.subtitle != null) {
      srcState.subtitle.generateAtom(w, "subtitle");
    }

    if (srcState.rights != null) {
      srcState.rights.generateAtom(w, "rights");
    }

    if (srcState.icon != null) {
      w.simpleElement(Namespaces.atomNs, "icon", null, srcState.icon);
    }

    if (srcState.logo != null) {
      w.simpleElement(Namespaces.atomNs, "logo", null, srcState.logo);
    }

    w.startRepeatingElement();
    for (Link link: srcState.links) {
      link.generateAtom(w, extProfile);
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person author: srcState.authors) {
      author.generateAtom(extProfile, w, "author");
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person contributor: srcState.contributors) {
      contributor.generateAtom(extProfile, w, "contributor");
    }
    w.endRepeatingElement();

    if (srcState.generator != null) {
      srcState.generator.generateAtom(w);
    }
  }

  /**
   * Parses XML in the Atom format.
   *
   * @param   extProfile
   *            extension profile
   *
   * @param   stream
   *            XML input stream
   *
   * @throws  IOException
   *
   * @throws  ParseException
   */
  public void parseAtom(ExtensionProfile extProfile,
                        InputStream stream) throws IOException,
                                                   ParseException {

    SourceHandler handler = new SourceHandler(extProfile);
    new XmlParser().parse(stream, handler, Namespaces.atom, "source");
  }

  /**
   * Parses XML in the Atom format.
   *
   * @param   extProfile
   *            extension profile
   *
   * @param   reader
   *            XML Reader.  The caller is responsible for ensuring that
   *            the character encoding is correct.
   */
  public void parseAtom(ExtensionProfile extProfile,
                        Reader reader) throws IOException,
                                              ParseException {

    SourceHandler handler = new SourceHandler(extProfile);
    new XmlParser().parse(reader, handler, Namespaces.atom, "source");
  }

  /** {@code <atom:source>} parser. */
  public class SourceHandler extends ExtensionPoint.ExtensionHandler {

    public SourceHandler(ExtensionProfile extProfile) {
      super(extProfile, Source.class);
    }


    protected SourceHandler(ExtensionProfile extProfile,
        Class<? extends ExtensionPoint> extClass) {
      super(extProfile, extClass);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {

        if (localName.equals("id")) {

          return new IdHandler();

        } else if (localName.equals("updated")) {

          return new UpdatedHandler();

        } else if (localName.equals("category")) {

          Category cat = new Category();
          Kind.Adaptable adaptable;
          if (Source.this instanceof Kind.Adaptable) {
            adaptable = (Kind.Adaptable)Source.this;
          } else {
            adaptable = null;
          }
          return cat.new AtomHandler(extProfile, srcState.categories,
              adaptable);

        } else if (localName.equals("title")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (srcState.title != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateTitle);
          }

          srcState.title = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("subtitle")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (srcState.subtitle != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateSubtitle);
          }

          srcState.subtitle = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("rights")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (srcState.rights != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateRights);
          }

          srcState.rights = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("icon")) {

          return new IconHandler();

        } else if (localName.equals("logo")) {

          return new LogoHandler();

        } else if (localName.equals("link")) {

          Link link = new Link();
          srcState.links.add(link);
          return link.new AtomHandler(extProfile);

        } else if (localName.equals("author")) {

          Person author = new Person();
          srcState.authors.add(author);
          return author.new AtomHandler(extProfile);

        } else if (localName.equals("contributor")) {

          Person contributor = new Person();
          srcState.contributors.add(contributor);
          return contributor.new AtomHandler(extProfile);

        } else if (localName.equals("generator")) {

          if (srcState.generator != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateGenerator);
          }

          srcState.generator = new Generator();
          return srcState.generator.new AtomHandler();
        }
      }  else {

        return super.getChildHandler(namespace, localName, attrs);
      }

      return null;
    }

    /** &lt;atom:id&gt; parser. */
    private class IdHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (srcState.id != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateFeedId);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.idValueRequired);
        }

        srcState.id = value;
      }
    }

    /** &lt;atom:updated&gt; parser. */
    class UpdatedHandler extends Rfc3339Handler {
      
      @Override
      public void processEndElement() throws ParseException {
        super.processEndElement();
        srcState.updated = getDateTime();
      }
    }

    /** &lt;atom:icon&gt; parser. */
    private class IconHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (srcState.icon != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateIcon);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.iconValueRequired);
        }

        srcState.icon = value;
      }
    }

    /** &lt;atom:logo&gt; parser. */
    private class LogoHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (srcState.logo != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateLogo);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.logoValueRequired);
        }

        srcState.logo = value;
      }
    }
  }
}
