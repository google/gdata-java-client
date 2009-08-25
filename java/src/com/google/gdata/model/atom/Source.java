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


package com.google.gdata.model.atom;

import com.google.common.collect.Lists;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.IGenerator;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Source class represents an Atom feed source object primarily on the data
 * model for an {@code <atom:source>} element.
 *
 * <p>Here is the Relax-NG schema that represents an Atom 1.0 Source:
 *
 * <p><pre>
 * atomSource =
 *   element atom:source {
 *     atomCommonAttributes,
 *     (atomAuthor*
 *      &amp; atomCategory*
 *      &amp; atomContributor*
 *      &amp; atomGenerator?
 *      &amp; atomIcon?
 *      &amp; atomId?
 *      &amp; atomLink*
 *      &amp; atomLogo?
 *      &amp; atomRights?
 *      &amp; atomSubtitle?
 *      &amp; atomTitle?
 *      &amp; atomUpdated?
 *      &amp; extensionElement*)
 *   }
 * </pre>
 */
public class Source extends Element {

  /**
   * The key for Source used as a construct.  This will apply to all uses
   * of Source regardless of QName, in particular to Feed.
   */
  public static final ElementKey<Void, Source> CONSTRUCT =
      ElementKey.of(null, Source.class);

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Source> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "source"), Void.class, Source.class);

  /**
   * The atom:id element.
   *     somewhere else.
   */
  public static final ElementKey<String, Element> ID = ElementKey.of(
      new QName(Namespaces.atomNs, "id"));

  /**
   * atom:updated
   */
  public static final ElementKey<DateTime, Element> UPDATED = ElementKey.of(
      new QName(Namespaces.atomNs, "updated"), DateTime.class, Element.class);

  /**
   * atom:title
   */
  public static final ElementKey<String, TextContent> TITLE = ElementKey.of(
      new QName(Namespaces.atomNs, "title"), String.class, TextContent.class);

  /**
   * atom:subtitle
   */
  public static final ElementKey<String, TextContent> SUBTITLE = ElementKey.of(
      new QName(Namespaces.atomNs, "subtitle"), String.class,
      TextContent.class);

  /**
   * atom:rights
   */
  public static final ElementKey<String, TextContent> RIGHTS = ElementKey.of(
      new QName(Namespaces.atomNs, "rights"), String.class, TextContent.class);

  /**
   * atom:icon
   */
  public static final ElementKey<URI, Element> ICON = ElementKey.of(
      new QName(Namespaces.atomNs, "icon"), URI.class, Element.class);

  /**
   * atom:logo
   */
  public static final ElementKey<URI, Element> LOGO = ElementKey.of(
      new QName(Namespaces.atomNs, "logo"), URI.class, Element.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(CONSTRUCT)) {
      return;
    }

    // Build the default metadata for all of our directly included elements.
    registry.build(ID);
    registry.build(UPDATED);
    registry.build(TITLE);
    registry.build(SUBTITLE);
    registry.build(RIGHTS);
    registry.build(ICON);
    registry.build(LOGO);

    // The builder for this element
    ElementCreator builder = registry.build(CONSTRUCT);

    // Local properties
    builder.addElement(ID);
    builder.addElement(UPDATED);
    builder.addElement(Category.KEY);
    builder.addElement(TITLE);
    builder.addElement(SUBTITLE);
    builder.addElement(RIGHTS);
    builder.addElement(ICON);
    builder.addElement(LOGO);
    builder.addElement(Link.KEY);
    builder.addElement(Author.KEY);
    builder.addElement(Contributor.KEY);
    builder.addElement(Generator.KEY);

    // Build atom:source based on the source construct.
    registry.build(KEY);
  }

  /**
   * Class representing atom:generator.
   */
  public static class Generator extends Element implements IGenerator {

    /**
     * The key for this element.
     */
    @SuppressWarnings("hiding")
    public static final ElementKey<String, Generator> KEY = ElementKey.of(
        new QName(Namespaces.atomNs, "generator"), String.class, Generator.class);

    /**
     * Metadata for the version attribute.
     */
    public static final AttributeKey<String> VERSION = AttributeKey.of(
        new QName("version"));

    /**
     * Metadata for the uri attribute.
     */
    public static final AttributeKey<URI> URI = AttributeKey.of(
        new QName("uri"), URI.class);

    /**
     * Registers the metadata for this element.
     */
    public static void registerMetadata(MetadataRegistry registry) {
      if (registry.isRegistered(KEY)) {
        return;
      }

      ElementCreator builder = registry.build(KEY);
      builder.addAttribute(VERSION);
      builder.addAttribute(URI);
    }

    /**
     * Constructs a new instance using the default metadata.
     */
    public Generator() {
      super(KEY);
    }

    /**
     * Constructs a new instance using the specified element metadata.
     *
     * @param key element key for this element.
     */
    public Generator(ElementKey<?, ? extends Generator> key) {
      super(key);
    }

    /** Version. */
    public String getVersion() {
      return getAttributeValue(VERSION);
    }

    public void setVersion(String v) {
      setAttributeValue(VERSION, v);
    }

    /**
     * Gets URI associated with source.
     *
     * @deprecated Use {@link #getUriUri()} instead.
     *
     * @return URI
     */
    @Deprecated
    public String getHref() {
      URI uri = getUriUri();
      if (uri == null) {
        return null;
      }
      return uri.toString();
    }

    /**
     * Gets URI associated with source.
     *
     * @return URI
     */
    public URI getUriUri() {
      return getAttributeValue(URI);
    }

    /**
     * Sets URI associated with source.
     *
     * @deprecated Use {@link #setUri(URI)} instead.
     *
     * @param v URI
     */
    @Deprecated
    public void setUri(String v) {
      try {
        setUri(v == null ? null : new URI(v));
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException(e);
      }
    }

    /**
     * Sets URI associated with source.
     *
     * @param v URI
     */
    public void setUri(URI v) {
      setAttributeValue(URI, v);
    }

    /** Generator name. */
    public String getName() {
      return (String) getTextValue();
    }

    public void setName(String v) {
      setTextValue(v);
    }
  }

  /**
   * Constructs a new instance using the default metadata.
   */
  public Source() {
    super(KEY);
  }

  /**
   * Constructs a new instance using the specified element metadata.
   *
   * @param key element key for this element.
   */
  protected Source(ElementKey<?, ? extends Source> key) {
    super(key);
  }

  /**
   * Copy constructor that initializes a new Source instance to have identical
   * contents to another instance, using a shared reference to the same child
   * element instances. Default metadata is used.
   */
  protected Source(Source sourceSource) {
    this(KEY, sourceSource);
  }

  /**
   * Copy constructor that initializes a new Source instance to have identical
   * contents to another element, using a shared state. The element key is given
   * by the caller.
   *
   * @param key element key to associate with copy
   * @param source source to copy data from
   */
  protected Source(ElementKey<?, ? extends Source> key, Element source) {
    super(key, source);
  }

  public String getId() {
    return getElementValue(ID);
  }

  public void setId(String v) {
    setElement(ID, (v == null) ? null : new Element(ID).setTextValue(v));
  }

  public DateTime getUpdated() {
    return getElementValue(UPDATED);
  }

  public void setUpdated(DateTime v) {
    setElement(UPDATED,
        (v == null) ? null : new Element(UPDATED).setTextValue(v));
  }

  public Set<Category> getCategories() {
    return getElementSet(Category.KEY);
  }

  public void addCategory(Category v) {
    addElement(Category.KEY, v);
  }

  public void clearCategories() {
    removeElement(Category.KEY);
  }

  public TextContent getTitle() {
    return getElement(TITLE);
  }

  public void setTitle(TextContent v) {
    setElement(TITLE, v);
  }

  public TextContent getSubtitle() {
    return getElement(SUBTITLE);
  }

  public void setSubtitle(TextContent v) {
    setElement(SUBTITLE, v);
  }

  public TextContent getRights() {
    return getElement(RIGHTS);
  }

  public void setRights(TextContent v) {
    setElement(RIGHTS, v);
  }

  /**
   * Gets icon URI associated with source.
   *
   * @deprecated Use {@link #getIconUri()} instead.
   *
   * @return icon URI
   */
  @Deprecated
  public String getIcon() {
    URI uri = getIconUri();
    if (uri == null) {
      return null;
    }
    return uri.toString();
  }

  /**
   * Gets icon URI associated with source.
   *
   * @return icon URI
   */
  public URI getIconUri() {
    return getElementValue(ICON);
  }

  /**
   * Sets icon URI associated with source.
   *
   * @deprecated Use {@link #setIcon(URI)} instead.
   *
   * @param v icon URI
   */
  @Deprecated
  public void setIcon(String v) {
    try {
      setIcon(v == null ? null : new URI(v));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Sets icon URI associated with source.
   *
   * @param v icon URI
   */
  public void setIcon(URI v) {
    setElement(ICON, (v == null) ? null : new Element(ICON).setTextValue(v));
  }

  /**
   * Gets logo URI associated with source.
   *
   * @deprecated Use {@link #getLogoUri()} instead.
   *
   * @return logo URI
   */
  @Deprecated
  public String getLogo() {
    URI uri = getLogoUri();
    if (uri == null) {
      return null;
    }
    return uri.toString();
  }

  /**
   * Gets logo URI associated with source.
   *
   * @return logo URI
   */
  public URI getLogoUri() {
    return getElementValue(LOGO);
  }

  /**
   * Sets logo URI associated with source.
   *
   * @deprecated Use {@link #setLogo(URI)} instead.
   *
   * @param v logo URI
   */
  @Deprecated
  public void setLogo(String v) {
    try {
      setLogo(v == null ? null : new URI(v));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Sets logo URI associated with source.
   *
   * @param v logo URI
   */
  public void setLogo(URI v) {
    setElement(LOGO, (v == null) ? null : new Element(LOGO).setTextValue(v));
  }

  public List<Link> getLinks() {
    return getElements(Link.KEY);
  }

  public void addLink(Link v) {
    addElement(Link.KEY, v);
  }

  public Link addLink(String rel, String type, String href) {
    try {
      Link link = new Link(rel, type, new URI(href));
      addLink(link);
      return link;
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public boolean removeLink(Link link) {
    return removeElement(Link.KEY, link);
  }

  public void clearLinks() {
    removeElement(Link.KEY);
  }

  @SuppressWarnings("unchecked")
  public List<Person> getAuthors() {
    return (List<Person>) (List<?>) getElements(Author.KEY);
  }

  public void addAuthor(Person v) {
    addElement(Author.KEY, v);
  }

  public void addAuthors(List<Person> v) {
    for (Person p : v) {
      addAuthor(p);
    }
  }

  public boolean removeAuthor(Person v) {
    return removeElement(Author.KEY, v);
  }

  public void clearAuthors() {
    removeElement(Author.KEY);
  }

  @SuppressWarnings("unchecked")
  public List<Person> getContributors() {
    return (List<Person>) (List<?>) getElements(Contributor.KEY);
  }

  public void addContributor(Person v) {
    addElement(Contributor.KEY, v);
  }

  public void addContributors(List<Person> v) {
    for (Person p : v) {
      addContributor(p);
    }
  }

  public boolean removeContributor(Person v) {
    return removeElement(Contributor.KEY, v);
  }

  public void clearContributors() {
    removeElement(Contributor.KEY);
  }

  public Generator getGenerator() {
    return getElement(Generator.KEY);
  }

  public void setGenerator(Generator v) {
    setElement(Generator.KEY, v);
  }

  public Generator setGenerator(String version, String uri, String name) {
    Generator gen = new Generator();
    gen.setVersion(version);
    gen.setUri(uri);
    gen.setName(name);
    setGenerator(gen);
    return gen;
  }

  /**
   * Retrieves the first link with the supplied {@code rel} and/or {@code type}
   * value.
   * <p>
   * If either parameter is {@code null}, doesn't return matches for that
   * parameter.
   */
  public Link getLink(String rel, String type) {

    for (Link link : getLinks()) {
      if (link.matches(rel, type)) {
        return link;
      }
    }

    return null;
  }

  /**
   * Return the links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch {@code rel} value to match or {@code null} to match any
   *        {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *        {@code type} value.
   * @return matching links.
   */
  public List<Link> getLinks(String relToMatch, String typeToMatch) {
    List<Link> result = new ArrayList<Link>();
    for (Link link : getLinks()) {
      if (link.matches(relToMatch, typeToMatch)) {
        result.add(link);
      }
    }
    return result;
  }

  /**
   * Remove all links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch {@code rel} value to match or {@code null} to match any
   *        {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *        {@code type} value.
   */
  public void removeLinks(String relToMatch, String typeToMatch) {
    List<Link> toRemove = Lists.newArrayList();
    for (Link link : getLinks()) {
      if (link.matches(relToMatch, typeToMatch)) {
        toRemove.add(link);
      }
    }
    for (Link link : toRemove) {
      removeLink(link);
    }
  }

  /**
   * Adds a link pointing to an HTML representation.
   *
   * @param htmlUrl link URL
   *
   * @param lang optional language code
   *
   * @param title optional title
   */
  public void addHtmlLink(String htmlUrl, String lang, String title) {
    try {
      Link link =
          new Link(Link.Rel.ALTERNATE, Link.Type.HTML, new URI(htmlUrl));
      if (lang != null) {
        link.setHrefLang(lang);
      }
      if (title != null) {
        link.setTitle(title);
      }
      addElement(Link.KEY, link);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves the first HTML link.
   *
   * @return the link
   */
  public Link getHtmlLink() {
    Link htmlLink = getLink(Link.Rel.ALTERNATE, Link.Type.HTML);
    return htmlLink;
  }
}
